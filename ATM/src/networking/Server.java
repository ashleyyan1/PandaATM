package networking;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import controllers.SessionHandler;
import database.Database;

public class Server {
	
	private ServerSocket mainSocket;
	
	public Server(Database db, int port) {
		try {
			mainSocket = new ServerSocket(port);
			while(true) {
				Socket clientConnection = mainSocket.accept();
				Thread clientWorker = new Thread(new SessionHandler(db, clientConnection));
				clientWorker.start();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
