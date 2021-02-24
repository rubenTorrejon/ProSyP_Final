package main;

/**
 * Clase Constant. Esta clase proporciona al servidor los datos de conexi�n de la BBDD,
 * a fin de poder consultar y modificar las tablas necesarias.
 * 
 * @author ruben
 *
 */
public class Constant {
	
	//Constantes para la conexi�n
	public static  String CONTROLADOR = "com.mysql.cj.jdbc.Driver";
	public static  String URL  = "jdbc:mysql://localhost:3306/bd_prosyp_final?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
	public static  String USUARIO = "root";
	public static  String CLAVE = "123456";
		
}