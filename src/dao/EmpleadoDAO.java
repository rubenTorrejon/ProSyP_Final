package dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;

import modelos.Empleado;

/**
 * Clase EmpleadoDAO, con todos los métodos de consulta y modificación necesarios
 * de la tabla Empleado en la BBDD.
 * 
 * @author Rubén Torrejón
 *
 */
public class EmpleadoDAO extends AbstractDAO {

	//ESTADO
	protected Statement st;
	protected Empleado miEmpleado;
	
	
	/**
	 * Constructor por defecto
	 */
	public EmpleadoDAO() {
		super();
	}
	
	
	
	/**
	 * Método que, recibido como parámetro el ID de un empleado, lo localiza en la BBDD
	 * y devuelve el objeto Empleado con todos los datos seteados. Si el ID recibido no
	 * se correspone con ningún empleado, devolverá un objeto empleado vacío.
	 * 
	 * @param idEmpleado
	 * @return Empleado
	 */
	public Empleado buscarEmpleadoById(int idEmpleado) {
		
		Empleado miEmpleado= null;
		
		PreparedStatement preparedStmt;
		
		try {
			String query = "SELECT * FROM empleado WHERE id_empleado = ?";
			preparedStmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			preparedStmt.setInt(1,idEmpleado);
			rs = preparedStmt.executeQuery();
										
			//mientras el resultset tenga filas creará clientes, les setea el estado y lo añade a la lista 
			if(rs.next()) {
				miEmpleado = new Empleado();
				miEmpleado.setIdEmpleado(rs.getInt(1));
				miEmpleado.setNombre(rs.getString(2));
				miEmpleado.setUltimaSesion(rs.getString(3));
				miEmpleado.setFechaContratacion(rs.getString(4));	
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return miEmpleado;
	}
	
	
	
	/**
	 * Método que realiza una consulta a la BBDD para obtener una tabla con los productos
	 * vendidos y el total recaudado con cada producto.
	 * <br><br>
	 * Además, suma la recaudación de todos los productos y nos devuelve un valor
	 * de recaudación total.
	 */
	public ArrayList<String> consultarCaja() {
		
		PreparedStatement preparedStmt;
		
		// Objeto Calendar, con el que objener la fecha del día de la consulta
		Calendar fecha = Calendar.getInstance();
		int year = fecha.get(Calendar.YEAR);
		int month = fecha.get(Calendar.MONTH)+1;
		int day = fecha.get(Calendar.DAY_OF_MONTH);
		String fechaDeHoy = year+"-"+month+"-"+day;
		
		// Valores totales de recaudación
		int total = 0;
		int recaudacionPorProducto = 0;
		String nombreProducto = "";
		
		ArrayList<String> totalesVendidos = new ArrayList<String>();
		
		// Consulta a la BBDD
		try {
			 String query = "select nombre_producto,sum(cantidad*precio_venta) from cantidad,compra,producto "
			 		+ "where cantidad.compra=compra.id_compra "
			 		+ "and cantidad.producto=producto.id_producto "
			 		+ "and fecha = ? group by nombre_producto";
			 
			 preparedStmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			 preparedStmt.setString(1,fechaDeHoy);
			 rs = preparedStmt.executeQuery();
	         
			 totalesVendidos.add("");
			 
			 while(rs.next()) {
	        	 nombreProducto = rs.getString(1);
	        	 recaudacionPorProducto = rs.getInt(2);
	        	 total += recaudacionPorProducto;
        	 
	        	 totalesVendidos.add("Producto: "+nombreProducto+"\nTotal vendido: "+
	        			 recaudacionPorProducto+"€\n-----------------------------------------");
	       
			 }
	         
	         totalesVendidos.add("\nLa caja total del día ha sido: "+total+" €\n\n-----------------------------------------");
	         
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return totalesVendidos;
	}
	
	
	
	/**
	 * Método que actualiza el valor "ultima_sesion" de la BBDD y lo actualiza con la fecha
	 * del día de ejecución, tomada directamente de la máquina.
	 * @param miIdEmpleado
	 */
	public void modificarUltimaSesion(int miIdEmpleado) {
		
		PreparedStatement preparedStmt;
		
		// Objeto Calendar, que nos permite obtener la fecha de hoy
		Calendar fecha = Calendar.getInstance();
		int year = fecha.get(Calendar.YEAR);
		int month = fecha.get(Calendar.MONTH)+1;
		int day = fecha.get(Calendar.DAY_OF_MONTH);
		String fechaDeHoy = year+"-"+month+"-"+day;
		
		// Orden de actualización de la fecha de última sesión
		try {
			String query = "UPDATE empleado SET Ultima_Sesion = ? WHERE (ID_Empleado = ?)";
			preparedStmt = con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			preparedStmt.setString(1,fechaDeHoy);
			preparedStmt.setInt(2, miIdEmpleado);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	
}