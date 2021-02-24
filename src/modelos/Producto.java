package modelos;

/**
 * Clase Producto. Esta clase crea Objetos Producto, recupera y modifica los valores en la 
 * base de datos.
 * 
 * @author Rubén Torrejón
 *
 */
public class Producto {

	//ESTADOS
	public int idProducto;
	public String nombre;
	public int precio;
	public int cantidad;
	
	
	
	/**
	 * Constructor por defecto
	 */
	public Producto() {
		super();
	}


	
	/**
	 * Constructor por parámetros
	 * @param idProducto
	 * @param nombre
	 * @param precio
	 * @param cantidad
	 */
	public Producto(int idProducto, String nombre, int precio, int cantidad) {
		super();
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.precio = precio;
		this.cantidad = cantidad;
	}

	

	// Getters
	public int getIdProducto() {
		return idProducto;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public int getPrecio() {
		return precio;
	}
	
	public int getCantidad() {
		return cantidad;
	}



	// Setters
	public void setIdProducto(int idProducto) {
		this.idProducto = idProducto;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setPrecio(int precio) {
		this.precio = precio;
	}
	
	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}
	

	
	/**
	 * Método toString, que devuelve un String con todos los detalles de un producto específico
	 */
	@Override
	public String toString() {
		return "Producto [idProducto=" + idProducto + ", nombre=" + nombre + ", precio=" + precio + ", cantidad=" + cantidad + "]";
	}
	
}
