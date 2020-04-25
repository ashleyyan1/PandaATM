package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;

import data_access_atm.ATMDA;
import data_access_atm.ATMSessionDA;
import data_access_atm.DebitCardDA;
import database.Database;
import entity_atm.ATM;
import entity_atm.DebitCard;
import networking.Message;

public class SessionHandler implements Runnable {

	private Database db;
	private ATMDA atmData;
	private ObjectInputStream dataInput;
	private ObjectOutputStream dataOutput;

	public SessionHandler(Database db, Socket s) throws IOException {
		this.db = db;
		try {
			atmData = new ATMDA(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dataInput = new ObjectInputStream(s.getInputStream());
		dataOutput = new ObjectOutputStream(s.getOutputStream());
	}

	@Override
	public void run() {
		
	}//end run
	
	public boolean verifyLogin() {
		try {
			Message m = (Message) dataInput.readObject();
			if (m.flag() == 2) {//Login Request
				
				long cardNumber = m.getCardNumber();
				int pin = m.getIntegerMessages().get(0);
				int atmID = m.getIntegerMessages().get(1);
				int pinAttemptCount = 1;
				
				ATM atm = atmData.getATMInfo(atmID);
				DebitCardDA debitCardData = new DebitCardDA(db);
				DebitCard debitCard = debitCardData.getDebitCardInfo(cardNumber);
				
				//Checks to see if debit card with provided card number exists
				if(debitCard == null) {
					Message cardDNE = new Message(3);
					cardDNE.addStringM("Entered Card Number Not Found.");
					dataOutput.writeObject(cardDNE);
				}
				//Card exists but has been previously locked
				else if(debitCard.isLocked()) {
					Message cardLocked = new Message(4);
					cardLocked.addStringM("Card is Locked. Access Denied.");
					dataOutput.writeObject(cardLocked);
				}
				//Card Number does exist in Database and is not locked
				else {
					while(pinAttemptCount < atm.getMaxPinEntryAttempts())
					{
						if(debitCard.getPinNumber() == pin) {
							Message mainMenuInfo = new Message(5);
							
							//Attach Menu Info ATM
							
							return true;
						}
						else {
							Message invalidPin = new Message(3);
							invalidPin.addStringM("Invalid Pin Entered. Try Again.");
							dataOutput.writeObject(invalidPin);
							pinAttemptCount += 1;
							m = (Message) dataInput.readObject();
							pin = m.getIntegerMessages().get(0);
						}
					}//end while
					//This code is reached if user ran out of attempts
					debitCardData.lockDebitCard(cardNumber);
					Message cardLocked = new Message(4);
					cardLocked.addStringM("Maximum Attempts Reached. Card is now locked.");
					dataOutput.writeObject(cardLocked);
				}
			} 
			else {//Communication Error
				Message unexpectedRequest = new Message(7);
				unexpectedRequest.addStringM("Expected Login Request. " + "Received Unexpected Request Type.");
				dataOutput.writeObject(unexpectedRequest);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();	
		} catch (SQLException e) {
			e.printStackTrace();
		}	
		
		try {
			dataInput.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}//end verifyLogin
	
	public boolean requestATM() {
		try {
			Message m = (Message) dataInput.readObject();
			if (m.flag() == 6) {//ATM Access Request
				
				try {
					ATMSessionDA atmSessionData = new ATMSessionDA(db);
					
					//Need to get info from atmDA class field
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			}
			else {//Communication Error
				Message unexpectedRequest = new Message(7);
				unexpectedRequest.addStringM("Expected ATM Access Request. " + "Received Unexpected Request Type.");
				dataOutput.writeObject(unexpectedRequest);
			}
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return false;
	}
}// end SessionHandler