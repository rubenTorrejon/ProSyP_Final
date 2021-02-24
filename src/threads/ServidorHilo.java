package threads;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import dao.*;
import modelos.*;
import recursos.Correo;

/**
 * Clase ServidorHilo. Esta clase funciona en paralelo con todas las cajas que se
 * inicien. Tiene programados todos los métodos lógicos que interactuarán con la 
 * BBDD.
 * 
 * @author Rubén Torrejón
 *
 */
public class ServidorHilo extends Thread {

	// Flujo de entrada y salida
	private DataInputStream entradaTexto;
	private ObjectOutputStream salidaObjeto;
	private Correo correo;
	
    // Objeto socket
	private Socket socket;	

	// Objetos DAO
	private EmpleadoDAO empleDAO;
	private CompraDAO compraDAO;
	private CantidadDAO cantidadDAO;
	private ProductoDAO productoDAO;
	
	// Booleano para controlar el while true del método run
	private boolean sesionIniciada = true;
	
	
	/**
	 * Constructor por defecto
	 * @param miSocket
	 */
 	public ServidorHilo(Socket miSocket) {
		super();
		socket = miSocket;
		init();
	}
    
	
	
	/**
	 * Método inicializador de los objetos de entrada y salida con la caja,
	 * así como los objetos DAO que realizarán las acciones programadas.
	 */
	private void init() {
		try {
			entradaTexto = new DataInputStream(socket.getInputStream());
	    	salidaObjeto = new ObjectOutputStream(socket.getOutputStream());
	    	
	    	empleDAO = new EmpleadoDAO();
	    	compraDAO = new CompraDAO();
	    	cantidadDAO = new CantidadDAO();
	    	productoDAO = new ProductoDAO();
	    	
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	/**
	 * Método run que responde a la orden start creando distintos hilos a
	 * medida que se generen cajas. Este método matará un hilo si se cierra
	 * su caja correspondiente.
	 */
	public void run() {
		while(sesionIniciada) {
			try {
				leerMensaje(entradaTexto.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	
	/**
	 * Método que lee los mensajes que le llegan desde la caja y deriva a la orden
	 * correspondiente.
	 * <br><br>
	 * Los mensajes siempre van a ser del tipo "Orden;n;n;n", siendo "Orden" la acción
	 * que queremos realizar y las "n" distintos números que irán acompañando a la orden
	 * en caso de ser necesarios y se utilizarán como parámetros para los métodos.
	 * <br><br>
	 * Los distintos valores de Orden son: Login, Compra, Caja y Salir.
	 * 
	 * @param mensajeEntrada
	 */
	public void leerMensaje(String mensajeEntrada){
			
		String[] parts = mensajeEntrada.split(";");
		
		int idEmpleado = Integer.parseInt(parts[1]);
				
		switch (parts[0]) {

	        case "Login":
	        	loguearEmpleado(idEmpleado);
	            break;
	            
	       case "Compra":
	        	int producto = Integer.parseInt(parts[2]);
	        	int cantidad = Integer.parseInt(parts[3]);
	        	comprarProducto(idEmpleado,producto,cantidad);
	           	break;
	        	
	        case "Caja":
	        	verCajaDelDia();	        	
	            break;
          	         
	        case "Salir":
	        	cerrarSesion(idEmpleado);
	            break;

	        default:
	        	System.out.println("\nError al conectar con la base de datos. Inténtelo de nuevo más adelante.\n");
	        	cerrarSesion(1);
	        	break;
	
        }
	}
	
	
	
	/**
	 * Método para la realización de la compra de un producto. Este método recibe como
	 * parámetros el empleado que realiza la compra, el producto a comprar y la cantidad
	 * deseada.
	 * <br><br>
	 * Si la compra puede realizarse, recibe un "true" y ejecuta la modificación del stock
	 * y las inserciones en la BBDD.
	 * <br><br>
	 * Si no pudiera realizarse por existir menos stock del que hay en tienda, lanza mensaje
	 * de error y anula las inserciones en la BBDD.
	 * 
	 * @param idEmpleado
	 * @param producto
	 * @param cantidad
	 */
	public void comprarProducto(int idEmpleado, int producto, int cantidad) {
		
		boolean stockActualizado = productoDAO.modificarStock(producto, cantidad);
		
    	if(stockActualizado) {
    		int idCompra = compraDAO.addCompra(idEmpleado);
    		cantidadDAO.addCantidad(idCompra, producto, cantidad);
    		int stockNuevo = productoDAO.buscarProductoById(producto).getCantidad();
    		String nombreProducto = productoDAO.buscarProductoById(producto).getNombre();
    		String mensaje = "\nCompra realizada con éxito. Quedan "+stockNuevo+" unidades del producto \""+nombreProducto+"\"";
    		try {
    			salidaObjeto.writeObject(mensaje);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}	
    		if (stockNuevo == 0) {
    			correo.enviarEmail(nombreProducto);
    		}
    	} else {
    		int stockNuevo = productoDAO.buscarProductoById(producto).getCantidad();
    		String nombreProducto = productoDAO.buscarProductoById(producto).getNombre();
    		String mensaje = "\nLo sentimos. La compra no ha podido realizarse porque sólo quedan "+stockNuevo+" unidades del producto \""+nombreProducto+"\"";
    		try {
    			salidaObjeto.writeObject(mensaje);
    		} catch (IOException e) {

    			e.printStackTrace();
    		}
    	}

	}

	
	
	/**
	 * Método que realiza el login de un empleado en la caja. Este método funciona obteniendo un
	 * String con un valor numérico que transformara a Int, y realizando una búsqueda de los empleados
	 * mediante la ID.
	 * <br><br>
	 * Si el valor del parámetro que se obtiene no coincidiese con la ID de ningún
	 * empleado, devolverá un objeto empleado null.
	 * @param idEmpleado
	 * , proporcionado por la caja.
	 */
	public void loguearEmpleado(int idEmpleado) {
		// Instancia de objeto empleadoDAO para realizar la búsqueda
    	EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    	// Obtención del objeto empleado mediante su ID
    	Empleado empleado = empleadoDAO.buscarEmpleadoById(idEmpleado);
    	// Flujo de salida al cliente
    	try {
			salidaObjeto.writeObject(empleado);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	
	/**
	 * Método que permite consultar el total de ventas que se han realizado durante el día en curso.
	 * <br><br>
	 * Este método recibe de la BBDD y envía a la caja una lista con todos los productos que
	 * se han vendido ese día y el total de las ventas, y lo imprimirá por pantalla.
	 */
	public void verCajaDelDia() {
		try {
			salidaObjeto.writeObject(empleDAO.consultarCaja());
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	
	
	/**
	 * Método que cierra la sesión de la caja abierta. Este método recibe como parámetro la id
	 * del empleado que estaba logueado en esa caja y actualizará la entrada de dicho empleado,
	 * estableciendo como última sesión la fecha actual, tomada del sistema.
	 * 
	 * @param idEmpleado
	 */
	public void cerrarSesion(int idEmpleado) {		
		try {
			empleDAO.modificarUltimaSesion(idEmpleado);
			entradaTexto.close();
			salidaObjeto.close();
			sesionIniciada = false;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
    
    

	

