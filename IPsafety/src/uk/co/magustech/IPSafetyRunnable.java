package uk.co.magustech;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IPSafetyRunnable implements Runnable {

	private final static String IPADDRESS_PATTERN = 
	        "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	private String serviceProvider;
	private Hashtable<String, Integer> ipAddressHash;

	public IPSafetyRunnable(String serviceProvider, Hashtable<String, Integer> ipAddressHash) {

		this.serviceProvider = serviceProvider;
		this.ipAddressHash = ipAddressHash;
	}

	private synchronized void addToHash(String ipAddress) {
		if (ipAddress.isEmpty())
			return;
		else {
			if (ipAddressHash.containsKey(ipAddress)) {
				Integer countOfIpAddress = ipAddressHash.get(ipAddress);
				int count = countOfIpAddress.intValue();
				count++;
				countOfIpAddress = new Integer(count);
				ipAddressHash.put(ipAddress, countOfIpAddress);
			} else {
				ipAddressHash.put(ipAddress, new Integer(1));
			}
		}
	}

	@Override
	public void run() {
		BufferedReader reader = null;
		HttpURLConnection connection = null;
		BufferedReader rd  = null;
		StringBuffer buffer = new StringBuffer("");
		String line = null;
		URL urlService = null;

		try {

			urlService = new URL(serviceProvider);

			//Set up the initial connection
			connection = (HttpURLConnection)urlService.openConnection();
			connection.setRequestMethod("GET");
			connection.setDoOutput(true);
			connection.setReadTimeout(10000);

			connection.connect();

			System.out.println("Response code: " + connection.getResponseCode());
			if (connection.getResponseCode() == 200) {
				//read the result from the server
				rd  = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				while ((line = rd.readLine()) != null) {
					buffer.append(line);
				}

				addToHash(extractIp4Address(buffer.toString().trim()));
			}

		} catch (UnsupportedEncodingException e) {
			//e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//e.printStackTrace();
		}
		finally {
			if (reader != null) try { reader.close(); } catch (IOException ignore) {}
		}
	}
	
	private String extractIp4Address(String ipAddressRawText) {
		Pattern pattern = Pattern.compile(IPADDRESS_PATTERN);
		Matcher matcher = pattern.matcher(ipAddressRawText);
		String extractedIpAddress = "0.0.0.0";
		if (matcher.find()) {
			extractedIpAddress = matcher.group();
		}
		
		return extractedIpAddress;
	}

}
