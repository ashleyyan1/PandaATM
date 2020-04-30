package com.group7.pandaatm.controllers;

import java.util.Timer;
import java.util.TimerTask;

public class SessionTimer {

	private boolean sessionThreadActive;
	private long sessionTimeOut;
	private Timer sessionTimer;
	
	public SessionTimer(String name, boolean isDaemon, long sTO) {
		this.sessionThreadActive = true;
		this.sessionTimeOut = sTO;
		this.sessionTimer = new Timer(name, isDaemon);
	}//Constructor
	
	public void startTimer() {
		//Start the Timer
		this.sessionTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				sessionThreadActive = false;				
			}//end run
			
		}, this.sessionTimeOut);
	}//end startTimer	
	
	public boolean getSessionThreadActive() {
		return this.sessionThreadActive;
	}
	
	public void refreshTimer() {
		this.sessionTimer.cancel();
		this.sessionTimer = new Timer("timerThread", true);
		
		//Start the Timer
		sessionTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				sessionThreadActive = false;				
			}
			
		}, this.sessionTimeOut );
	}//end refreshTimer
}//end Timer
