package com.latico.commons.net.trap;


import com.latico.commons.net.trap.bean.TrapResult;

public class HandleThread extends Thread {
	TrapReceiver tr = null;
	public HandleThread(TrapReceiver tr){
		this.tr = tr;
	}
	public void run() {
		while(true){
			TrapResult result = tr.getOneResult(3000);
			if(result != null){
				System.out.println(result);
			}
		}
	}
} 
