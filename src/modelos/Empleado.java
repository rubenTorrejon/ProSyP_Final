package modelos;

import java.io.Serializable;

/**
 * Clase Empleado. Esta clase crea Objetos Empleado, recupera y modifica los valores en la 
 * base de datos.
 * 
 * @author Rubén Torrejón
 *
 */
public class Empleado implements Serializable {

	//Serial
	private static final long serialVersionUID = 1L;
	
	//ESTADOS
	public int idEmpleado;
	public String nombre;
	public String ultimaSesion;
	public String fechaContratacion;
	
	
	
	/**
	 * Constructor por defecto
	 */
	public Empleado() {
		super();
	}


	
	/**
	 * Constructor por parámetros
	 * @param idEmpleado
	 * @param nombre
	 * @param ultimaSesion
	 * @param fechaContratacion
	 */
	public Empleado(int idEmpleado, String nombre, String ultimaSesion, String fechaContratacion) {
		super();
		this.idEmpleado = idEmpleado;
		this.nombre = nombre;
		this.ultimaSesion = ultimaSesion;
		this.fechaContratacion = fechaContratacion;
	}


	
	// Getters
	public int getIdEmpleado() {
		return idEmpleado;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getUltimaSesion() {
		return ultimaSesion;
	}
	
	public String getFechaContratacion() {
		return fechaContratacion;
	}



	// Setters
	public void setIdEmpleado(int idEmpleado) {
		this.idEmpleado = idEmpleado;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setUltimaSesion(String ultimaSesion) {
		this.ultimaSesion = ultimaSesion;
	}

	public void setFechaContratacion(String fechaContratacion) {
		this.fechaContratacion = fechaContratacion;
	}


	
	/**
	 * Método toString, que devuelve un String con todos los detalles de un empleado específico
	 */
	@Override
	public String toString() {
		return "ID de empleado: " + idEmpleado + "\nNombre: " + nombre + 
			"\nÚltima Sesión: " + ultimaSesion + "\nFecha de contratación: " + fechaContratacion;
	}
	
	
	
	/**
	 * Método que crea un mensaje de bienvenida en el momento de loguearse un empleado en caja.
	 * @return
	 */
	public String mensajeBienvenida() {
		return "Bienvenido de nuevo. Los datos que tenemos en nuestro sistema son los siguientes:\n";
	}
	
	
}
