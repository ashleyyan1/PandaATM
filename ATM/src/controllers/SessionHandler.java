package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import database.Database;
import networking.Message;

public class SessionHandler implements Runnable {

	private Database db;
	private ObjectInputStream dataInput;
	private ObjectOutputStream dataOutput;

	public SessionHandler(Database db, Socket s) throws IOException {
		this.db = db;
		dataInput = new ObjectInputStream(s.getInputStream());
		dataOutput = new ObjectOutputStream(s.getOutputStream());
	}

	@Override
	public void run() {
		try {
			Message m = (Message) dataInput.readObject();
			if (m.flag() == 1) {
				long cardNumber = m.getCardNumber();
				int pin = m.getIntegerMessages().get(0);
				//INPROGRESS
			} else {
				// Client miscommunicating

			}
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			dataInput.close();
		} catch (IOException e) {

		}
	}
}// end SessionHandler
