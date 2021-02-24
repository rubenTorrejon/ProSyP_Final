package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import modelos.Cantidad;

/**
 * Clase CantidadDAO, con todos los m�todos de consulta y modificaci�n necesarios
 * de la tabla Cantidad en la BBDD.
 * 
 * @author Rub�n Torrej�n
 *
 */
public class CantidadDAO extends AbstractDAO {

	//ESTADO
	protected Statement st;
	protected ResultSet rs;
	protected Cantidad miCantidad;
	
	
	/**
	 * Constructor por defecto
	 */
	public CantidadDAO() {
		super();
		miCantidad = new Cantidad();
		st = null;
		rs = null;
	}
	
	
	
	/**
	 * M�todo para a�adir los detalles de una compra a la BBDD en la tabla Cantidad
	 * 
	 * @param miCompra
	 * , el ID de la compra especificada.
	 * @param miProducto
	 * , el ID del producto comprado.
	 * @param miCantidad
	 * , las unidades de producto compradas.
	 */
	public void addCantidad(int miCompra, int miProducto, int miCantidad) {
		
		PreparedStatement preparedStmt;
		
		try {
			
			preparedStmt = con.prepareStatement("INSERT INTO cantidad (compra,producto,cantidad) VALUES (?,?,?)");
			
			preparedStmt.setInt(1,miCompra);
			preparedStmt.setInt(2,miProducto);
			preparedStmt.setInt(3,miCantidad);
			
			preparedStmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	

}
