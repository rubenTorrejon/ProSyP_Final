package modelos;

/**
 * Clase Compra. Esta clase crea Objetos Compra, recupera y modifica los valores en la 
 * base de datos.
 * 
 * @author Rub�n Torrej�n
 *
 */
public class Compra {
	
	//ESTADO
	public int idCompra;
	public String fecha;
	public int empleado;
	
	
	
	/**
	 * Constructor por defecto
	 */
	public Compra() {
		super();
	}

	

	/**
	 * Constructor con par�metros
	 * 
	 * @param idCompra
	 * @param fecha
	 * @param empleado
	 */
	public Compra(int idCompra, String fecha, int empleado) {
		super();
		this.idCompra = idCompra;
		this.fecha = fecha;
		this.empleado = empleado;
	}


	
	// Getters
	public int getIdCompra() {
		return idCompra;
	}
	
	public String getFecha() {
		return fecha;
	}
	
	public int getEmpleado() {
		return empleado;
	}



	// Setters
	public void setIdCompra(int idCompra) {
		this.idCompra = idCompra;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public void setEmpleado(int empleado) {
		this.empleado = empleado;
	}



	/**
	 * M�todo toString, que devuelve un String con todos los detalles de una compra espec�fica
	 */
	@Override
	public String toString() {
		return "Compra [idCompra: " + idCompra + ", fecha: " + fecha + ", empleado: " + empleado + "]";
	}
	
	
	
	

}
