package recursos;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

import dao.EmpleadoDAO;
import modelos.Empleado;

/**
 * Clase Caja.
 * 
 * Esta clase interactúa directamente con el usuario y con el servidor. Solicitará
 * al usuario un número de empleado y, tras comprobar que existe en la BBDD, permitirá
 * elegir entre alguna de las opciones programadas.
 * 
 * @author Rubén Torrejón
 *
 */
public class Caja {

	// Objetos necesarios para la conexión de conexión.
	protected static String Host = "localhost";
	protected static int Puerto = 7500;
	protected static Socket socket;
	
	// Objeto String que nos indica el número de empleado. Se inicia vacío
	protected static String IdEmpleado = "";
	
	// Objetos empleado y empleado DAO, recibido a traves del servidor.
	protected static Empleado empleado;
	protected static EmpleadoDAO empleDAO;
	
	// Objetos de envío y recepción de información entre los distintos niveles
	protected static DataOutputStream textoSalida;
	protected static ObjectInputStream entrada;
	private static Scanner scanner;
	
	
	
	/**
	 * Método main de arranque de la caja
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		init();
		intentarConexion();
	}
	
	
	
	/**
	 * Método init. Este método inicia en el main los objetos de conexión y
	 * comunicación con el servidor, así como el scanner.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void init() throws UnknownHostException, IOException {
		
		//Inicio del programa cliente y mensaje de confirmación
		socket = new Socket(Host, Puerto);
		
		// Flujo de entrada y salida al servidor
		textoSalida = new DataOutputStream(socket.getOutputStream());
		entrada = new ObjectInputStream(socket.getInputStream());
		
		scanner = new Scanner(System.in);
	}
	
	
	
	/**
	 * Método que lee el número de empleado y lo envía al método conectar
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void intentarConexion() throws IOException, ClassNotFoundException {

		System.out.println("Introduzca su número de empleado:\n");

		Scanner scanner = new Scanner(System.in);
		IdEmpleado = scanner.nextLine();
		
		conectar(IdEmpleado);
		
		scanner.close();
	}
	
	
	
	/**
	 * Método para comprobar si el mensaje que ha sido introducido en el sistema es
	 * un número o no.\nEn caso de ser un número entero, devolverá un valor true.\nEn 
	 * caso de ser un String o un número de tipo distinto a int, devolverá false.
	 * @param idEmpleado
	 * @return true or false
	 */
    public static boolean esUnNumero(String idEmpleado) {

        boolean resultado;

        try {
            Integer.parseInt(idEmpleado);
            resultado = true;
        } catch (NumberFormatException excepcion) {
            resultado = false;
        }

        return resultado;
    }
	
	
    
    /**
     * Método de filtrado. Si el valor introducido es un valor de tipo int, intentará conectar con
     * la base de datos a través del método "comunicar". En caso contrario, volverá a pedir que el
     * empleado introduzca su número de id_empleado.
     * @param idEmpleado
     * @throws IOException
     * @throws ClassNotFoundException 
     */
	public static void conectar (String idEmpleado) throws IOException, ClassNotFoundException {
       
		if (esUnNumero(idEmpleado)) {
			
			// Si el valor introducido es un int, lanzamos mensaje de confirmación y comunicamos con el servidor
			System.out.println("\nConectado con el sistema...");
			loguear(idEmpleado);	
			
		} else {
			
			// Si el valor introducido no es un int, lanzamos mensaje de error y volvemos a pedir
			System.out.println("\nERROR: Introduzca un número de empleado válido, gracias.");
			intentarConexion();
			
		}
	}
	
	
	
	/**
	 * Método de comunicación entre la consola de caja y el servidor. Este método recibe el id del
	 * empleado ya filtrado y se comunica con el servidor.
	 * <br><br>
	 * Si el número de empleado pertenece a algún empleado en la BBDD, recibirá un objeto de tipo empleado
	 * con todos los campos seteados y le solicitará que indique la acción que desea realizad.
	 * En caso contrario, recibirá un empleado null. Si el empleado es null, solicita un número de empleado nuevo.
	 * @param idEmpleado
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void loguear(String idEmpleado) throws IOException, ClassNotFoundException {
		
		// Envío al servidor del mensaje recibido por consola
		textoSalida.writeUTF("Login;"+idEmpleado);
	
		empleado = (Empleado) entrada.readObject();	
		
		if(empleado!=null) {
			System.out.println(empleado.mensajeBienvenida());
			System.out.println(empleado.toString());
			System.out.println("\nElija una acción de entre las siguientes:");
			elegirAccion();
		} else {
			System.out.println("El número proporcionado no pertenece a ningún empleado.");
			intentarConexion();
		}
		
	}
	
	
	
	/**
	 * Este método discrimina la acción a realizar por el empleado ya logueado. Proporciona varias
	 * opciones:
	 * <ol>
	 * <li> Realiza una compra, indicando el número de producto de una lista preestablecida
	 * 		 y el número de unidades que se quieren de la misma.</li>
	 * <li> Solicita a la BBDD el total de caja que se ha hecho en el día.</li>
	 * <li> Cerrar sesión, actualizando los datos de conexión.</li>
	 * </ol>
	 */
	public static void elegirAccion () {
        
		System.out.println("\n1: Comprar artículo. \n2: Consultar caja del día. \n3: Salir.\n");
		
		String accion = scanner.nextLine();
		
		switch (accion) {
		
	        case "1":
	        	realizarCompra();
	            break;
	
	        case "2":
	    		pedirCaja();
	            break;
	
	        case "3":
	        	salir();
	            break;
	
	        default:
	        	System.out.println("\nSelección incorrecta. Por favor, seleccione una acción de la lista escribiendo el número correspondiente.");
	    		elegirAccion();
	        	break;
        }
    }
	
	
	
	/**
	 * Método que realiza y añade una compra a la BBDD. Este método solicita, primero, un número de producto
	 * al usuario. Si el número es correcto y está en la lista, preguntará el número de unidades que se desean.
	 * Si el número de unidades es correcto, incluye la compra y los detalles en la BBDD y modifica el Stock.
	 */
	public static void realizarCompra() {
		
		System.out.println("\nPor favor, indique el número correspondiente al producto que desea comprar:\n"
				+ "\n1: Disco Duro.\n2: USB.\n3: Monitor.\n4: Raton.\n5: Caja.\n6: Placa Base.\n7: Tarjeta Gráfica.\n");
	
		String producto = scanner.nextLine();
		
		if(esUnNumero(producto)) {
			
			int numProducto = Integer.parseInt(producto);
			
			if(numProducto>0 && numProducto<=7) {
				
				System.out.println("\nPor favor, indique la cantidad de unidades que desea:\n");
				String unidades = scanner.nextLine();
				
				if(esUnNumero(unidades)) {
					
					int numUnidades = Integer.parseInt(unidades);
					
					if(numUnidades<0) {
					
						// Envío al servidor del mensaje recibido por consola
						try {
							textoSalida.writeUTF("Compra;"+empleado.getIdEmpleado()+";"+producto+";"+unidades);			
							String mensaje = (String) entrada.readObject();
							System.out.println(mensaje);
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
						
					} else {
					System.out.println("\nERROR: El número de unidades solicitada debe ser mayor que 0. Volvamos a empezar.");
					elegirAccion();
				} // Fin del if (el número de unidades es mayor que 0)
					
				} else {
					System.out.println("\nERROR: Tiene que especificar un número de unidades. Volvamos a empezar.");
					elegirAccion();
				} // Fin del if (unidades es un numero)
				
			} else {
				System.out.println("\nERROR: El número seleccionado no pertenece a ningún artículo. Volvamos a empezar.");
				elegirAccion();
			} // Fin del if (el número está entre 1 y 7)
		
		} else {
			System.out.println("\nERROR: Tiene que indicar un artículo de la lista mediante su número. Volvamos a empezar.");
			elegirAccion();
		} // Fin del if (producto es un número)
		
		System.out.println("\n¿Desea hacer algo más?");
		elegirAccion();
	}
	
	
	
	/**
	 * Método que solicita al servidor la caja del día. Este método no envía parámetros, ya que
	 * no modifica la BBDD y lo puede realizar cualquier usuario.
	 * <br><br>
	 * El método recibe una lista de String basados en indicar el total vendido de cada artículo
	 * en el día que se solicita la informacion a la BBDD, y finalmente el total vendido en todo
	 * el día.
	 */
	public static void pedirCaja() {
						
		// Envío al servidor del mensaje recibido por consola
		try {
			textoSalida.writeUTF("Caja;"+empleado.getIdEmpleado());
			@SuppressWarnings("unchecked")
			ArrayList<String> cajaDelDia = (ArrayList<String>) entrada.readObject();
			for (String i:cajaDelDia) {
				System.out.println(i);
			}
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		System.out.println("\n¿Desea hacer algo más?");
		elegirAccion();
	}
	
	
	
	/**
	 * Método de logout del usuario.
	 * <br><br>
	 * Este método envía el id del empleado a la BBDD, que actualiza la fecha de última sesión,
	 * y cierra todos los objetos iniciados en el método init.
	 */
	public static void salir() {
		
		System.out.println("\nGracias por utilizar nuestro software, "+empleado.getNombre()+". Esperamos verte pronto de nuevo por aquí.");
		
		try {
			textoSalida.writeUTF("Salir;"+empleado.getIdEmpleado());
			textoSalida.close();
			entrada.close();
			socket.close();
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
	

