package controllers;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.time.LocalDateTime;

import data_access_atm.ATMDA;
import data_access_atm.ATMSessionDA;
import data_access_atm.AccountDA;
import data_access_atm.DebitCardDA;
import database.Database;
import entity_atm.ATM;
import entity_atm.ATMSession;
import entity_atm.DebitCard;
import networking.Message;

public class SessionHandler implements Runnable {

	private Database db;
	private ATMDA atmData;
	private ATMSessionDA sessionData;
	private DebitCardDA debitCardData;
	private DebitCard debitCard;
	private AccountDA accountData;
	private SessionTimer sessionTimer;
	private ObjectInputStream dataInput;
	private ObjectOutputStream dataOutput;

	public SessionHandler(Database db, Socket s) throws IOException {
		this.db = db;
		try {
			atmData = new ATMDA(db);
			sessionData = new ATMSessionDA(db);
			accountData = new AccountDA(db);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		dataInput = new ObjectInputStream(s.getInputStream());
		dataOutput = new ObjectOutputStream(s.getOutputStream());
	}

	@Override
	public void run() {

		try {
			ATM atm = requestATMAccess();
			// Access Granted, ATM is available for use
			if (atm != null) {

				// Timer to Terminate Session if User is Idle
				sessionTimer = new SessionTimer("timerThread", true, (long) atm.getSessionTimeOut());
				sessionTimer.startTimer();

				ATMSession atmSession = verifyLogin(atm);
				// Valid PIN Provided, Session Access Granted
				if (atmSession != null) {

					try {
						boolean sessionActive = true;
						TransactionHandler transHandler = new TransactionHandler(db, accountData, atmData, 
								atmSession, sessionTimer, dataInput, dataOutput, debitCard, atm);
						
						while (sessionActive) {
							
							Message sessionAction = readMessage((long) atm.getSessionTimeOut());
							if (!sessionTimer.getSessionThreadActive()) {
								throw new InterruptedException();
							}
							// Resets Timer
							sessionTimer.refreshTimer();

							switch (sessionAction.flag()) {

							case 0:// Logout Request (Cancel Button)
								sessionActive = false;
								break;

							case 9:// Initiate Withdrawal Transaction
								transHandler.initiateWithdrawal();
								break;

							case 10:// Initiate Deposit Transaction
								transHandler.initiateDeposit();
								break;

							case 11:// Initiate Transfer Transaction
								transHandler.initiateTransfer();
								break;

							case 14:// View Balance (Account Inquiry)
								transHandler.initiateBalanceInquiry();
								break;
								
							default:// Communication Error
								Message unexpectedRequest = new Message(7);
								unexpectedRequest
										.addStringM("Expected Transaction Request. Received Unexpected Request Type.");
								dataOutput.writeObject(unexpectedRequest);
								sessionActive = false;
								break;
							}// end switch
						} // end while
							// Terminates Session on User Cancel (Cancel Button)
						sessionData.terminateSession(atmSession.getSessionID());

					} catch (InterruptedException e) {
						// Terminates Session on Session Timeout
						sessionData.terminateSession(atmSession.getSessionID());
						Message sessionTimedOut = new Message(1);
						dataOutput.writeObject(sessionTimedOut);
					} // end if(verifyLogin())
				} // end if(accessGranted)
			}
		} catch (ClassNotFoundException | IOException | InterruptedException | SQLException e) {
			e.printStackTrace();
		}
	}// end run

	public ATMSession verifyLogin(ATM atm) throws InterruptedException {
		try {
			Message m = readMessage((long) atm.getSessionTimeOut());
			if (m.flag() == 2) {// Login Request

				long cardNumber = m.getCardNumber();
				int pin = m.getIntegerMessages().get(0);
				int pinAttemptCount = 1;

				debitCardData = new DebitCardDA(db);
				debitCard = debitCardData.getDebitCardInfo(cardNumber);

				// Checks to see if debit card with provided card number exists
				if (debitCard == null) {
					Message cardDNE = new Message(3);
					cardDNE.addStringM("Entered Card Number Not Found.");
					dataOutput.writeObject(cardDNE);
				}
				// Card exists but has been previously locked
				else if (debitCard.isLocked()) {
					Message cardLocked = new Message(4);
					cardLocked.addStringM("Card is Locked. Access Denied.");
					dataOutput.writeObject(cardLocked);
				}
				//Card Expiration Date has passed (Expiration is before current time of access)
				else if (debitCard.getCardExpDate().isBefore(LocalDateTime.now())) {
					Message cardExpired = new Message(4);
					cardExpired.addStringM("Card is Expired. Access Denied.");
					dataOutput.writeObject(cardExpired);
				}
				// Card Number does exist in Database and is not locked
				else {
					while (pinAttemptCount < atm.getMaxPinEntryAttempts()) {
						/*
						 * Main Menu Info Message Consists of the following: 1. ATM's Physical Location
						 * (Address) 2. Timer Limit to time out session (milliseconds)
						 */
						if (debitCard.getPinNumber() == pin) {
							Message mainMenuInfo = new Message(5);
							mainMenuInfo.addStringM(atm.getMachineAddress());
							mainMenuInfo.addIntegerM(atm.getSessionTimeOut());
							dataOutput.writeObject(mainMenuInfo);

							// Creates Session for Valid User Login and returns it to run()
							int sessionID = sessionData.insertATMSession(atm.getMachineID(), cardNumber);
							ATMSession atmSession = sessionData.getATMSessionInfo(sessionID);
							return atmSession;
						} else {
							Message invalidPin = readMessage((long) atm.getSessionTimeOut());
							invalidPin.addStringM("Invalid Pin Entered. Try Again.");
							dataOutput.writeObject(invalidPin);
							pinAttemptCount += 1;
							m = (Message) dataInput.readObject();
							pin = m.getIntegerMessages().get(0);
						}
					} // end while
						// This code is reached if user ran out of attempts
					debitCardData.lockDebitCard(cardNumber);
					Message cardLocked = new Message(4);
					cardLocked.addStringM("Maximum Attempts Reached. Card is now locked.");
					dataOutput.writeObject(cardLocked);
				}
			} else {// Communication Error
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
		return null;
	}// end verifyLogin

	public ATM requestATMAccess() {
		try {
			Message m = (Message) dataInput.readObject();
			if (m.flag() == 6) {// ATM Access Request

				try {
					atmData = new ATMDA(db);
					ATM atm = atmData.getATMInfo(m.getIntegerMessages().get(0));
					// Restrict access to ATM if it is already in use
					if (atm.isSessionActive()) {
						Message accessDenied = new Message(8);
						accessDenied.addStringM("Access Denied. ATM is currently in use.");
						dataOutput.writeObject(accessDenied);
					} else {// Grants access to atm (meaning the given ATM is not currently in use)
						return atm;
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else {// Communication Error
				Message unexpectedRequest = new Message(7);
				unexpectedRequest.addStringM("Expected ATM Access Request. " + "Received Unexpected Request Type.");
				dataOutput.writeObject(unexpectedRequest);
			}
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return null;
	}// end requestATMAccess

	public Message readMessage(long sessionTimeout) throws ClassNotFoundException, IOException, InterruptedException {
		Message m = (Message) dataInput.readObject();
		if (!sessionTimer.getSessionThreadActive()) {
			throw new InterruptedException();
		}
		// Resets Timer
		sessionTimer.refreshTimer();
		return m;
	}// end readMessage
}// end SessionHandler