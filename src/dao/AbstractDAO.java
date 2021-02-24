package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.Constant;

/**
 * Clase AbstractDAO, con los m�todos de conexi�n a la BBDD comunes a todas las clases DAO.
 * 
 * @author Rub�n Torrej�n
 *
 */
abstract class AbstractDAO {
	
	// ESTADO
	protected Connection con;
	protected ResultSet rs;
	
	
	
	/**
	 * Constructor
	 */
	public AbstractDAO() {
		ConectarBD();
	}
	
	
	
	/**
	 * M�todo para conectarse a la BBDD en local 
	 */
	private void ConectarBD() {
		try {
			Class.forName(Constant.CONTROLADOR);
			this.con = DriverManager.getConnection(Constant.URL, Constant.USUARIO, Constant.CLAVE);
		}catch (SQLException e) {
			e.printStackTrace();
		}catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}