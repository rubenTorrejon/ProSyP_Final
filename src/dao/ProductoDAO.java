package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modelos.Producto;

/**
 * Clase ProductoDAO, con todos los métodos de consulta y modificación necesarios
 * de la tabla Producto en la BBDD.
 * 
 * @author Rubén Torrejón
 *
 */
public class ProductoDAO extends AbstractDAO {

	
	//ESTADOS
	protected Statement st;
	protected ResultSet rs;
	protected Producto miProducto;
	
	
	
	/**
	 * Constructor por defecto
	 */
	public ProductoDAO() {
		super();
		miProducto = new Producto();
		st = null;
		rs=null;
	}
	
	
	
	/**
	 * Método que, una vez pasada la ID de un producto, nos devuelve el objeto producto al que pertenece dicha ID.
	 * @param miIdProducto
	 * @return Producto
	 */
	public Producto buscarProductoById(int miIdProducto) {
		
		Producto miProducto= null;
		
		PreparedStatement preparedStmt;
		
		// Búsqueda del producto
		try {
			 String query = "SELECT * FROM producto where id_producto = ?";
			 preparedStmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			 preparedStmt.setInt(1,miIdProducto);
			 rs = preparedStmt.executeQuery();
										
			//mientras el resultset tenga filas creará clientes, les setea el estado y lo añade a la lista 
			if(rs.next()) {
				miProducto = new Producto();
				miProducto.setIdProducto(rs.getInt(1));
				miProducto.setNombre(rs.getString(2));
				miProducto.setPrecio(rs.getInt(3));
				miProducto.setCantidad(rs.getInt(4));	
			}	
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return miProducto;
	}
	
	

	/**
	 * Método para actualizar el stock de un producto del que se realiza una compra.
	 * <br><br>
	 * Este método recibe como parámetro el id del producto, del que obtendrá el stock
	 * actual, y la cantidad de producto comprada.
	 * <br><br>
	 * Una vez obtenidos esos datos, calculará el nuevo stock restando al stock actual
	 * la cantidad comprada. Si el resultado es menor que 0, no permitirá ejecutar.
	 * @param miIdProducto
	 * @param cantidadComprada
	 */
	public boolean modificarStock (int idProducto, int cantidadComprada) {
		
		PreparedStatement preparedStmt;
		
		boolean stockActualizado = false;
		
		// Obtención del producto
		ProductoDAO productoDAO = new ProductoDAO();
		Producto producto = new Producto();
		producto = productoDAO.buscarProductoById(idProducto);
		
		// Obtención del stock actual y cálculo del nuevo stock
		int stockActual = producto.getCantidad();
		int stockNuevo = stockActual-cantidadComprada;
		
		if(stockNuevo >= 0) {	
			// Orden de actualización del stock
			try {
				 String query = "UPDATE producto SET cantidad_stock = ? WHERE (ID_producto = ?)";
				 preparedStmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
				 preparedStmt.setInt(1, stockNuevo);
				 preparedStmt.setInt(2, idProducto);
		         preparedStmt.executeUpdate();
		         stockActualizado = true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return stockActualizado;
	}

	
	
}