package recursos;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

/**
 * Clase Correo. Esta clase nos permitir� enviar un Email con nuestra aplicaci�n Java
 * 
 * @author Rub�n Torrej�n
 *
 */
public class Correo {
	
	// Estado
	final String username = "finalbosspsp@gmail.com";
	final String password = "cosillas";


	
	/**
	 * Constructor por defecto
	 */
	public Correo() {

	}


	
	/**
	 * M�todo para enviarle al proveedor un email indicando que se ha agotado un producto
	 * <br><br>
	 * Este m�todo recibe como par�metro el nombre del producto, y lo incluir� en el texto
	 * del email que enviar�.
	 * 
	 * @param producto
	 */
	public void enviarEmail(String producto) {
		
		final String username = "finalbosspsp@gmail.com";
		final String password = "cosillas";

		Properties prop = new Properties();
		prop.put("mail.smtp.host", "smtp.gmail.com");
		prop.put("mail.smtp.port", "465");
		prop.put("mail.smtp.auth", "true");
		prop.put("mail.smtp.socketFactory.port", "465");
		prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		
		Session session = Session.getInstance(prop, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			}
		);

		
		try {

			Message message = new MimeMessage(session);
			
			message.setFrom(new InternetAddress("finalbosspsp@gmail.com"));
			message.setRecipients(Message.RecipientType.TO,	InternetAddress.parse("finalbosspsp@gmail.com"));
			message.setSubject("Pedido a proveedor");
			message.setText("Hola proveedor, necesitamos 50 unidades del producto " + producto);

			Transport.send(message);

		} catch (MessagingException e) {
			e.printStackTrace();
		}

	}

}