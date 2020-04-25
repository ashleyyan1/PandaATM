package networking;

import java.util.ArrayList;

public class Message {

	/*
	 * 0 = Logout Cancel 
	 * 1 = Logout Timeout 
	 * 2 = Login Request 
	 * 3 = Session Access Denied 
	 * 4 = Card Locked
	 * 5 = Main Menu Info
	 * 6 = ATM Access Request
	 * 7 = Communication Error
	 */
	private int flag;
	private ArrayList<Integer> intMessages;
	private ArrayList<String> textMessages;
	private long cardNumber;

	public Message(int flag) {
		this.flag = flag;
		intMessages = new ArrayList<Integer>();
		textMessages = new ArrayList<String>();
	}

	public int addintegerM(int data) {
		intMessages.add(data);
		return intMessages.size() - 1;
	}

	public int addStringM(String data) {
		textMessages.add(data);
		return textMessages.size() - 1;
	}

	public void setCardNumber(long cardNumber) {
		this.cardNumber = cardNumber;
	}

	public int flag() {
		return flag;
	}

	public ArrayList<Integer> getIntegerMessages() {
		return intMessages;
	}

	public ArrayList<String> getTextMessages() {
		return textMessages;
	}

	public long getCardNumber() {
		return cardNumber;
	}
}//end Message
