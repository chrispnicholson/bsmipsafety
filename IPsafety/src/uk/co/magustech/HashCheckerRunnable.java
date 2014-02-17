package uk.co.magustech;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Set;

public class HashCheckerRunnable implements Runnable {

	private Hashtable<String, Integer> ipAddressHash;
	
	public HashCheckerRunnable(Hashtable<String, Integer> ipAddressHash) {
		this.ipAddressHash = ipAddressHash;
		
	}
	
	@Override
	public void run() {
		while (Thread.currentThread().isAlive()) {
			
			checkHash();
		}

	}
	
	private synchronized void checkHash() {
		Enumeration<String> keys = ipAddressHash.keys();
		
		while (keys.hasMoreElements()) {
			String ipAddress = keys.nextElement();
			
			Integer countOfIpAddress = ipAddressHash.get(ipAddress);
			if (countOfIpAddress.intValue() > 1) {
				System.out.println("Final decision is: " + ipAddress);
				printOutHashtable();
				System.exit(0);
			}
		}
	}
	
	private void printOutHashtable() {
		System.out.println(ipAddressHash);
		
	}
}
