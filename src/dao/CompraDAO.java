package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;

import modelos.Compra;

/**
 * Clase CompraDAO, con todos los m�todos de consulta y modificaci�n necesarios
 * de la tabla Compra en la BBDD.
 * 
 * @author Rub�n Torrej�n
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
	 * M�todo que a�ade a la BBDD una compra. Este m�todo recibe como par�metro el ID del 
	 * empleado logado en la caja.
	 * <br><br>
	 * Adem�s, una vez creada la compra nueva, buscar� y guardar� en memoria el valor del ID
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
