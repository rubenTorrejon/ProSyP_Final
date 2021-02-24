package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import modelos.Compra;

/**
 * Clase CompraDAO, con todos los métodos de consulta y modificación necesarios
 * de la tabla Compra en la BBDD.
 * 
 * @author Rubén Torrejón
 *
 */
public class CompraDAO extends AbstractDAO{

	
	//ESTADO
	protected Statement st;
	protected ResultSet rs;
	protected Compra miCompra;
	
	
	
	/**
	 * Constructor por defecto
	 */
	public CompraDAO() {
		super();
		miCompra = new Compra();
		st = null;
		rs=null;
	}
	
	
	
	/**
	 * Método que añade a la BBDD una compra. Este método recibe como parámetro el ID del 
	 * empleado logado en la caja.
	 * <br><br>
	 * Además, una vez creada la compra nueva, buscará y guardará en memoria el valor del ID
	 * de compra generado, para poder utilizarlo para guardar las cantidades de cada producto
	 * en la tabla Cantidad.
	 * 
	 * @param idEmpleado
	 * @return int idCompra
	 */
	public int addCompra(int idEmpleado) {
		
		PreparedStatement preparedStmt;

		Calendar fecha = Calendar.getInstance();
		String fechaDeHoy = fecha.get(Calendar.YEAR)+"-"+(fecha.get(Calendar.MONTH)+1)+"-"+fecha.get(Calendar.DAY_OF_MONTH);
		
		int idCompra = 0;
		
		try {
			
			String query = "INSERT INTO compra (fecha,empleado) values (?,?)";
			preparedStmt = super.con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
			
			preparedStmt.setString(1,fechaDeHoy);
			preparedStmt.setInt(2,idEmpleado);
			
			preparedStmt.executeUpdate();
			
			rs = preparedStmt.getGeneratedKeys();
			
			rs.next();
			idCompra = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return idCompra;
	}
	
        
}
