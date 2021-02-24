package modelos;

/**
 * Clase Cantidad. Esta clase crea Objetos Cantidad, recupera y modifica los valores en la 
 * base de datos.
 * 
 * @author Rubén Torrejón
 *
 */
public class Cantidad {

	//ESTADO
	protected int idCantidad;
	protected int compra;
	protected int producto;
	protected int cantidad;
	
	
	
	/**
	 * Constructor por defecto
	 */
	public Cantidad() {
		super();
	}

	
	
	/**
	 * Constructor con parámetros
	 * 
	 * @param idCantidad
	 * @param compra
	 * @param producto
	 * @param cantidad
	 */
	public Cantidad(int idCantidad, int compra, int producto, int cantidad) {
		super();
		this.idCantidad = idCantidad;
		this.compra = compra;
		this.producto = producto;
		this.cantidad = cantidad;
	}

	
	
	// Métodos Getters
	public int getIdCantidad() {
		return idCantidad;
	}
	
	public int getCompra() {
		return compra;
	}
	
	public int getProducto() {
		return producto;
	}
	
	public int getCantidad() {
		return cantidad;
	}

	
	
	// Métodos Setters
	public void setIdCantidad(int idCantidad) {
		this.idCantidad = idCantidad;
	}

	public void setCompra(int compra) {
		this.compra = compra;
	}

	public void setProducto(int producto) {
		this.producto = producto;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	
	/**
	 * Método toString, que devuelve un String con todos los detalles de una cantidad específica
	 */
	@Override
	public String toString() {
		return "Compra: " + compra + ", Producto: " + producto + ", Cantidad: " + cantidad;
	}
	
}
