package com.smart.sms;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/****************************************************************
 * Get SMS Delivery Status API class template
 * using Apache's HttpClient v4.3.3
 * Java Version 6.
 * Required libraries/jars:
 * 	httpcore-4.3.2.jar (and dependencies);
 *  httpclient-4.3.3.jar (and dependencies);
 *  log4j-1.2.17.jar.
 * NOTE: To use this API, you should already have a token
 * for Authorization.
 * @author MARebultan
 ***************************************************************/

public class GetSMSDeliveryStatus {
	
	public static void main(String[] args)   {


				/****************************************************************************************
				 * The following block of code only configures the OPTIONAL log4j. 
				 * This configuration displays the full WIRE (headers + content) of the HTTP transaction.
				 ***************************************************************************************/
		 		Logger log = Logger.getLogger(GetSMSDeliveryStatus.class.getName());
		 		PropertyConfigurator.configure("log4j.properties");
		
		 		
				/****************************************************
				 * Credentials:
				 * requestId = resource ID from the SendSMS response
				 * senderAddress = SMS Access Code
				 * apiURL = Endpoint URL for your request
				 * token = Token provided by RetrieveToken API
				*****************************************************/
				String requestId = "";
				String senderAddress = "";
				String token = "";
				String apiURL = "https://x.smart.com.ph/1/smsmessaging/outbound/" + senderAddress +"/requests/" + requestId + "/deliveryInfos";

				String authorization = "Bearer " + token; // This will be the value of your authorization header
				
				//Initialize Apache's HttpClient through an HttpClientBuilder.
				HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
				
				HttpGet get = new HttpGet(apiURL);
				//Set the following headers to your request
				get.addHeader("Content-Type", "application/json");
				get.addHeader("Accept","application/json");
				get.addHeader("Authorization", authorization);
				
				try {

					HttpResponse response = client.execute(get); // Execute the HttpGet
					  											// A successful response is HTTP 200 OK
					  											// which will include the delivery receipt.
					
				    if (response.getStatusLine().getStatusCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "
							     + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
				    }
				        
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					get.releaseConnection();
				}
	}
}
