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
 * Esta clase interact�a directamente con el usuario y con el servidor. Solicitar�
 * al usuario un n�mero de empleado y, tras comprobar que existe en la BBDD, permitir�
 * elegir entre alguna de las opciones programadas.
 * 
 * @author Rub�n Torrej�n
 *
 */
public class Caja {

	// Objetos necesarios para la conexi�n de conexi�n.
	protected static String Host = "localhost";
	protected static int Puerto = 7500;
	protected static Socket socket;
	
	// Objeto String que nos indica el n�mero de empleado. Se inicia vac�o
	protected static String IdEmpleado = "";
	
	// Objetos empleado y empleado DAO, recibido a traves del servidor.
	protected static Empleado empleado;
	protected static EmpleadoDAO empleDAO;
	
	// Objetos de env�o y recepci�n de informaci�n entre los distintos niveles
	protected static DataOutputStream textoSalida;
	protected static ObjectInputStream entrada;
	private static Scanner scanner;
	
	
	
	/**
	 * M�todo main de arranque de la caja
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		init();
		intentarConexion();
	}
	
	
	
	/**
	 * M�todo init. Este m�todo inicia en el main los objetos de conexi�n y
	 * comunicaci�n con el servidor, as� como el scanner.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public static void init() throws UnknownHostException, IOException {
		
		//Inicio del programa cliente y mensaje de confirmaci�n
		socket = new Socket(Host, Puerto);
		
		// Flujo de entrada y salida al servidor
		textoSalida = new DataOutputStream(socket.getOutputStream());
		entrada = new ObjectInputStream(socket.getInputStream());
		
		scanner = new Scanner(System.in);
	}
	
	
	
	/**
	 * M�todo que lee el n�mero de empleado y lo env�a al m�todo conectar
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
	public static void intentarConexion() throws IOException, ClassNotFoundException {

		System.out.println("Introduzca su n�mero de empleado:\n");

		Scanner scanner = new Scanner(System.in);
		IdEmpleado = scanner.nextLine();
		
		conectar(IdEmpleado);
		
		scanner.close();
	}
	
	
	
	/**
	 * M�todo para comprobar si el mensaje que ha sido introducido en el sistema es
	 * un n�mero o no.\nEn caso de ser un n�mero entero, devolver� un valor true.\nEn 
	 * caso de ser un String o un n�mero de tipo distinto a int, devolver� false.
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
     * M�todo de filtrado. Si el valor introducido es un valor de tipo int, intentar� conectar con
     * la base de datos a trav�s del m�todo "comunicar". En caso contrario, volver� a pedir que el
     * empleado introduzca su n�mero de id_empleado.
     * @param idEmpleado
     * @throws IOException
     * @throws ClassNotFoundException 
     */
	public static void conectar (String idEmpleado) throws IOException, ClassNotFoundException {
       
		if (esUnNumero(idEmpleado)) {
			
			// Si el valor introducido es un int, lanzamos mensaje de confirmaci�n y comunicamos con el servidor
			System.out.println("\nConectado con el sistema...");
			loguear(idEmpleado);	
			
		} else {
			
			// Si el valor introducido no es un int, lanzamos mensaje de error y volvemos a pedir
			System.out.println("\nERROR: Introduzca un n�mero de empleado v�lido, gracias.");
			intentarConexion();
			
		}
	}
	
	
	
	/**
	 * M�todo de comunicaci�n entre la consola de caja y el servidor. Este m�todo recibe el id del
	 * empleado ya filtrado y se comunica con el servidor.
	 * <br><br>
	 * Si el n�mero de empleado pertenece a alg�n empleado en la BBDD, recibir� un objeto de tipo empleado
	 * con todos los campos seteados y le solicitar� que indique la acci�n que desea realizad.
	 * En caso contrario, recibir� un empleado null. Si el empleado es null, solicita un n�mero de empleado nuevo.
	 * @param idEmpleado
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static void loguear(String idEmpleado) throws IOException, ClassNotFoundException {
		
		// Env�o al servidor del mensaje recibido por consola
		textoSalida.writeUTF("Login;"+idEmpleado);
	
		empleado = (Empleado) entrada.readObject();	
		
		if(empleado!=null) {
			System.out.println(empleado.mensajeBienvenida());
			System.out.println(empleado.toString());
			System.out.println("\nElija una acci�n de entre las siguientes:");
			elegirAccion();
		} else {
			System.out.println("El n�mero proporcionado no pertenece a ning�n empleado.");
			intentarConexion();
		}
		
	}
	
	
	
	/**
	 * Este m�todo discrimina la acci�n a realizar por el empleado ya logueado. Proporciona varias
	 * opciones:
	 * <ol>
	 * <li> Realiza una compra, indicando el n�mero de producto de una lista preestablecida
	 * 		 y el n�mero de unidades que se quieren de la misma.</li>
	 * <li> Solicita a la BBDD el total de caja que se ha hecho en el d�a.</li>
	 * <li> Cerrar sesi�n, actualizando los datos de conexi�n.</li>
	 * </ol>
	 */
	public static void elegirAccion () {
        
		System.out.println("\n1: Comprar art�culo. \n2: Consultar caja del d�a. \n3: Salir.\n");
		
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
	        	System.out.println("\nSelecci�n incorrecta. Por favor, seleccione una acci�n de la lista escribiendo el n�mero correspondiente.");
	    		elegirAccion();
	        	break;
        }
    }
	
	
	
	/**
	 * M�todo que realiza y a�ade una compra a la BBDD. Este m�todo solicita, primero, un n�mero de producto
	 * al usuario. Si el n�mero es correcto y est� en la lista, preguntar� el n�mero de unidades que se desean.
	 * Si el n�mero de unidades es correcto, incluye la compra y los detalles en la BBDD y modifica el Stock.
	 */
	public static void realizarCompra() {
		
		System.out.println("\nPor favor, indique el n�mero correspondiente al producto que desea comprar:\n"
				+ "\n1: Disco Duro.\n2: USB.\n3: Monitor.\n4: Raton.\n5: Caja.\n6: Placa Base.\n7: Tarjeta Gr�fica.\n");
	
		String producto = scanner.nextLine();
		
		if(esUnNumero(producto)) {
			
			int numProducto = Integer.parseInt(producto);
			
			if(numProducto>0 && numProducto<=7) {
				
				System.out.println("\nPor favor, indique la cantidad de unidades que desea:\n");
				String unidades = scanner.nextLine();
				
				if(esUnNumero(unidades)) {
					
					int numUnidades = Integer.parseInt(unidades);
					
					if(numUnidades<0) {
					
						// Env�o al servidor del mensaje recibido por consola
						try {
							textoSalida.writeUTF("Compra;"+empleado.getIdEmpleado()+";"+producto+";"+unidades);			
							String mensaje = (String) entrada.readObject();
							System.out.println(mensaje);
						} catch (IOException | ClassNotFoundException e) {
							e.printStackTrace();
						}
						
					} else {
					System.out.println("\nERROR: El n�mero de unidades solicitada debe ser mayor que 0. Volvamos a empezar.");
					elegirAccion();
				} // Fin del if (el n�mero de unidades es mayor que 0)
					
				} else {
					System.out.println("\nERROR: Tiene que especificar un n�mero de unidades. Volvamos a empezar.");
					elegirAccion();
				} // Fin del if (unidades es un numero)
				
			} else {
				System.out.println("\nERROR: El n�mero seleccionado no pertenece a ning�n art�culo. Volvamos a empezar.");
				elegirAccion();
			} // Fin del if (el n�mero est� entre 1 y 7)
		
		} else {
			System.out.println("\nERROR: Tiene que indicar un art�culo de la lista mediante su n�mero. Volvamos a empezar.");
			elegirAccion();
		} // Fin del if (producto es un n�mero)
		
		System.out.println("\n�Desea hacer algo m�s?");
		elegirAccion();
	}
	
	
	
	/**
	 * M�todo que solicita al servidor la caja del d�a. Este m�todo no env�a par�metros, ya que
	 * no modifica la BBDD y lo puede realizar cualquier usuario.
	 * <br><br>
	 * El m�todo recibe una lista de String basados en indicar el total vendido de cada art�culo
	 * en el d�a que se solicita la informacion a la BBDD, y finalmente el total vendido en todo
	 * el d�a.
	 */
	public static void pedirCaja() {
						
		// Env�o al servidor del mensaje recibido por consola
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

		System.out.println("\n�Desea hacer algo m�s?");
		elegirAccion();
	}
	
	
	
	/**
	 * M�todo de logout del usuario.
	 * <br><br>
	 * Este m�todo env�a el id del empleado a la BBDD, que actualiza la fecha de �ltima sesi�n,
	 * y cierra todos los objetos iniciados en el m�todo init.
	 */
	public static void salir() {
		
		System.out.println("\nGracias por utilizar nuestro software, "+empleado.getNombre()+". Esperamos verte pronto de nuevo por aqu�.");
		
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
	

