package com.smart.sms;

import org.apache.http.HttpResponse;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/****************************************************************
 * Send SMS API class template
 * using Apache's HttpClient v4.3.3
 * Java Version 6.
 * Required libraries/jars:
 * 	httpcore-4.3.2.jar (and dependencies);
 *  httpclient-4.3.3.jar (and dependencies);
 *  log4j-1.2.17.jar.
 * NOTE: To use this API, you should already have a token
 * for Authorization
 * @author MARebultan
 ***************************************************************/

public class SendSms {
	
	public static void main(String[] args)   {

				/************************************************************************************
				 * The following block of code only configures the log4j. This configuration displays
				 * the full WIRE (headers + content) of the HTTP transaction.
				 ***********************************************************************************/
		 		Logger log = Logger.getLogger(SendSms.class.getName());
		 		PropertyConfigurator.configure("log4j.properties");

				/****************************************************
				 * Credentials:
				 * address = Nominated Mobile Number
				 * senderAddress = SMS Access Code
				 * apiURL = Endpoint URL for your request
				 * token = Token provided by RetrieveToken API
				 * Parameters
				 * message = Your test message
				*****************************************************/
				String address = "tel:";
				String senderAddress = "";
				String message = "";
				String token = "";
				
				String authorization = "Bearer " + token; // This will be the value of your authorization header
				
				String apiURL = "https://x.smart.com.ph/1/smsmessaging/outbound/" + senderAddress + "/requests";
					
				//Initialize Apache's HttpClient through an HttpClientBuilder.
				HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
				
				HttpPost post = new HttpPost(apiURL);
				//Set the following headers to your request
				post.addHeader("Content-Type", "application/json");
				post.addHeader("Accept","application/json");
				post.addHeader("Authorization", authorization);
				
				//Create the body and set it to your request
				StringEntity jsonrequest = new StringEntity("{\"outboundSMSMessageRequest\":{\"address\":\"" + address + "\",\"senderAddress\": \"" + senderAddress + "\",\"outboundSMSTextMessage\":{\"message\":\"" + message + "\"}}}", "UTF-8");
				post.setEntity(jsonrequest);
				
				try {
					HttpResponse response = client.execute(post); // Execute the HttpPost
																  // A successful response is HTTP 201 Created
																  // which will include a response body.
																  // Use the resource reference ID to
																  // to retrieve the delivery receipt.
					
				    if (response.getStatusLine().getStatusCode() != 201) {
						throw new RuntimeException("Failed : HTTP error code : "
							     + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
				    }		    
				    
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					post.releaseConnection();
				}
				
	} 
	
}
