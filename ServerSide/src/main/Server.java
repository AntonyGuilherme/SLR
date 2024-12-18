package main;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

import fr.tp.inf112.projects.robotsim.infrasturcture.FactoryPersistenceManager;

public class Server {
	private static final Logger LOGGER = Logger.getLogger(Server.class.getName());

	public static void main(String args[]) {
		try (ServerSocket serverSocket = new ServerSocket(80)) {
			LOGGER.info("SERVER LISTENING ON 80");
			
			while(true) {
				// awaiting for the client connection
				Socket socket = serverSocket.accept();
				
				// running the request in another thread to not block the server
				Runnable reqProcessor = new RequestProcessor(socket, new FactoryPersistenceManager(null), LOGGER);
				new Thread(reqProcessor).start();
			}	
		}
		catch (IOException e) {
			LOGGER.warning(e.getMessage());
		}
	}
}