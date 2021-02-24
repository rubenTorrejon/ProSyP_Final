package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modelos.Producto;

/**
 * Clase ProductoDAO, con todos los m�todos de consulta y modificaci�n necesarios
 * de la tabla Producto en la BBDD.
 * 
 * @author Rub�n Torrej�n
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
	 * M�todo que, una vez pasada la ID de un producto, nos devuelve el objeto producto al que pertenece dicha ID.
	 * @param miIdProducto
	 * @return Producto
	 */
	public Producto buscarProductoById(int miIdProducto) {
		
		Producto miProducto= null;
		
		PreparedStatement preparedStmt;
		
		// B�squeda del producto
		try {
			 String query = "SELECT * FROM producto where id_producto = ?";
			 preparedStmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			 preparedStmt.setInt(1,miIdProducto);
			 rs = preparedStmt.executeQuery();
										
			//mientras el resultset tenga filas crear� clientes, les setea el estado y lo a�ade a la lista 
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
	 * M�todo para actualizar el stock de un producto del que se realiza una compra.
	 * <br><br>
	 * Este m�todo recibe como par�metro el id del producto, del que obtendr� el stock
	 * actual, y la cantidad de producto comprada.
	 * <br><br>
	 * Una vez obtenidos esos datos, calcular� el nuevo stock restando al stock actual
	 * la cantidad comprada. Si el resultado es menor que 0, no permitir� ejecutar.
	 * @param miIdProducto
	 * @param cantidadComprada
	 */
	public boolean modificarStock (int idProducto, int cantidadComprada) {
		
		PreparedStatement preparedStmt;
		
		boolean stockActualizado = false;
		
		// Obtenci�n del producto
		ProductoDAO productoDAO = new ProductoDAO();
		Producto producto = new Producto();
		producto = productoDAO.buscarProductoById(idProducto);
		
		// Obtenci�n del stock actual y c�lculo del nuevo stock
		int stockActual = producto.getCantidad();
		int stockNuevo = stockActual-cantidadComprada;
		
		if(stockNuevo >= 0) {	
			// Orden de actualizaci�n del stock
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