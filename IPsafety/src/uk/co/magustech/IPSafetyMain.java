/**
 * 
 */
package uk.co.magustech;

import java.util.Hashtable;

/**
 * @author Chris
 *
 */
public class IPSafetyMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		Hashtable<String, Integer> ipAddressHash = new Hashtable<String, Integer>();
		
		try {
			IPSafetyRunnable ipsr1 = new IPSafetyRunnable("http://checkip.amazonaws.com/", ipAddressHash);
			IPSafetyRunnable ipsr2 = new IPSafetyRunnable("http://checkip.dyndns.org/", ipAddressHash);
			IPSafetyRunnable ipsr3 = new IPSafetyRunnable("http://ifconfig.me/ip", ipAddressHash);
			IPSafetyRunnable ipsr4 = new IPSafetyRunnable("http://corz.org/ip", ipAddressHash);
			HashCheckerRunnable hc = new HashCheckerRunnable(ipAddressHash);
			
			Thread thread1 = new Thread(ipsr1);
			Thread thread2 = new Thread(ipsr2);
			Thread thread3 = new Thread(ipsr3);
			Thread thread4 = new Thread(ipsr4);
			Thread thread5 = new Thread(hc);
			
			thread1.start();
			thread2.start();
			thread3.start();
			thread4.start();
			thread5.start();
			
		}
		catch (RuntimeException re) {
			re.printStackTrace();
		}

	}

}
