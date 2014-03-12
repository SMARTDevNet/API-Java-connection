package com.smart.oauth;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/****************************************************************
 * Authorize App API class template
 * using Apache's HttpClient v4.3.3.
 * Java Version 6.
 * Required libraries/jars:
 * 	httpcore-4.3.2.jar (and dependencies);
 *  httpclient-4.3.3.jar (and dependencies);
 *  log4j-1.2.17.jar.
 * NOTE: This is the FIRST step to acquiring a token using OAuth
 *  @author MARebultan
 ***************************************************************/

public class AuthorizeApp {
	
	public static void main(String[] args)  { 

		
				/************************************************************************************
				 * The following block of code only configures the log4j. This configuration displays
				 * the full WIRE (headers + content) of the HTTP transaction.
				 ***********************************************************************************/
		 		Logger log = Logger.getLogger(AuthorizeApp.class.getName());
		 		PropertyConfigurator.configure("log4j.properties");

			
				/*********************************************
				 * Credentials:
				 * clientId = Consumer Key
				 * clientSecret = Consumer Secret Key
				 * callbackURL = Callback URL
				 * apiURL = Endpoint URL for your request
				*********************************************/

				String clientId = "";
				String clientSecret = "";
				String callbackURL = "http://www.smart.com.ph";
				String apiURL = "https://x.smart.com.ph/oauth/authorize?response_type=code&client_id=" + clientId + "&client_secret=" + clientSecret + "&redirect_uri=" + callbackURL+ "&scope=SMS+MMS";

				//Initialize Apache's HttpClient through an HttpClientBuilder.
				HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
				//Note that my builder disables redirecting to the specified "Location" header of the response. 
				//This is because I want to manually load that location to the browser.

				HttpGet method = new HttpGet(apiURL);

				try {

				HttpResponse response = client.execute(method); // Execute the HttpGet
																// A successful response is HTTP 302 Found
																// which will include a header named "Location"
																// Use the value of that header to allow your app
																// and retrieve the AuthCode.
					
				    if (response.getStatusLine().getStatusCode() != 302) {
						throw new RuntimeException("Failed : HTTP error code : "
							     + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
				    }
	    
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					method.releaseConnection();
				}


		}
}
