package recursos;

import java.io.*;
import java.net.*;

import threads.ServidorHilo;

/**
 * Clase Servidor. Clase intermedia que actúa entre la clase Caja y la clase ServidorHilo.
 * Cuando una caja se conecte, la clase Servidor comprobará que la conexión a través del
 * puerto es correcta.
 * 
 * @author Rubén Torrejón
 *
 */
public class Servidor {
		
    public static void main(String[] arg) throws IOException {
    	
    	System.out.println("SERVIDOR CONECTADO CON LA BASE DE DATOS. ESPERANDO CAJA.");

    	// Datos de conexión
    	int numeroPuerto = 7500;
    	@SuppressWarnings("resource")
		ServerSocket servidor = new ServerSocket(numeroPuerto);

    	while(true) {
    		Socket cajaConectada = servidor.accept();
    		ServidorHilo server = new ServidorHilo(cajaConectada);
    		server.start();
    	}

    }

}