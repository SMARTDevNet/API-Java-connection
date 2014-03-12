package com.smart.oauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/****************************************************************
 * Retrieve App API class template
 * using Apache's HttpClient v4.3.3
 * Java Version 6.
 * Required libraries/jars:
 * 	httpcore-4.3.2.jar (and dependencies);
 *  httpclient-4.3.3.jar (and dependencies);
 *  log4j-1.2.17.jar.
 * NOTE: This is the SECOND step to acquiring a token using OAuth.
 * @author MARebultan
 ***************************************************************/

public class RetrieveToken {

	public static void main(String[] args) throws ParseException, IOException  {

				/************************************************************************************
				 * The following block of code only configures the log4j. This configuration displays
				 * the full WIRE (headers + content) of the HTTP transaction.
				 ***********************************************************************************/
		 		Logger log = Logger.getLogger(RetrieveToken.class.getName());
		 		PropertyConfigurator.configure("log4j.properties");
		
		 		
				/***********************************************************************************
				 * Credentials:
				 * clientId = Consumer Key
				 * clientSecret = Consumer Secret Key
				 * callbackURL = Callback URL
				 * apiURL = Endpoint URL for your request
				 * auth_code = {code} parameter from
				 * 				https://(callback_URL)/?scope=SMS+MMS&state=null&code=(auth_code)
				***********************************************************************************/
				
				String clientId = "";
				String clientSecret = "";
				String callbackURL = "http://www.smart.com.ph";
				String apiURL = "https://x.smart.com.ph/oauth/token";
				String auth_code = ""; // Important: The auth_code is retrieved once you "Allow" your app to use
											   // the credentials. In order to allow your app, you must follow the Location
											   // link on your AuthorizeApp response.
				
				
				//Initialize Apache's HttpClient through an HttpClientBuilder.
				HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();

				//Create the "body" and set it as a URL Encoded entity.
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				parameters.add((new BasicNameValuePair("client_id", clientId)));
				parameters.add((new BasicNameValuePair("client_secret", clientSecret)));
				parameters.add((new BasicNameValuePair("redirect_uri", callbackURL)));
				parameters.add((new BasicNameValuePair("scope", "SMS+MMS"))); // scope = the allowed function(s) provided to your credential
				parameters.add((new BasicNameValuePair("grant_type", "authorization_code"))); // grant_type = whether you are requesting for an auth_code token
																							  // or a refresh_token if your auth_code has expired.
				parameters.add((new BasicNameValuePair("code", auth_code)));
				
				HttpPost method = new HttpPost(apiURL);
				method.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				UrlEncodedFormEntity body = new UrlEncodedFormEntity(parameters, "UTF-8");
				method.setEntity(body);
			
				try {
					
					HttpResponse response = client.execute(method); // Execute the HttpGet
																	// A successful response is HTTP 200 OK
																	// which will include a header named "Location"
																	// Use the value of that header to allow your app
																	// and retrieve the AuthCode.
					
				    if (response.getStatusLine().getStatusCode() != 200) {
						throw new RuntimeException("Failed : HTTP error code : "
							     + response.getStatusLine().getStatusCode() + " " + response.getStatusLine().getReasonPhrase());
				    }
				    
				} catch (Exception e) {
					System.out.println(e);
				} finally {
				    method.releaseConnection();
				    
				}
	}
}
