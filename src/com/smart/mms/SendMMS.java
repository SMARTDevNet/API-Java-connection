package com.smart.mms;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/****************************************************************
 * Send MMS API class template
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

public class SendMMS {
	
	public static void main(String[] args) throws IOException   {


				/************************************************************************************
				 * The following block of code only configures the log4j. This configuration displays
				 * the full WIRE (headers + content) of the HTTP transaction.
				 ***********************************************************************************/
		 		Logger log = Logger.getLogger(SendMMS.class.getName());
		 		PropertyConfigurator.configure("log4j.properties");

				/****************************************************
				 * Credentials:
				 * address = Nominated Mobile Number
				 * senderAddress = MMS Access Code
				 * apiURL = Endpoint URL for your request
				 * token = Token provided by RetrieveToken API
				 * Parameters:
				 * subject = subject of your MMS message
				 * attachmentJPG = your attachment
				 * jsonRequest = the text body of your request
				*****************************************************/
		 		
				String address = "tel:";
				String senderAddress = "";
				String subject = "";
				File attachmentJPG = new File("src\\Smart_Logo.jpg");
				String token = "tQg1GqXsjqWSGlaA7nbVMUkWcAlV";
				String apiURL = "https://x.smart.com.ph/1/messaging/outbound/" + senderAddress + "/requests";
				String jsonRequest = "{\"outboundMessageRequest\":{\"address\":\"" + address + "\",\"senderAddress\":\"" + senderAddress+ "\",\"outboundMMSMessage\":{\"subject\":\""+ subject +"\"}}}";			

				String authorization = "Bearer " + token; // This will be the value of your authorization header
				
				//Initialize Apache's HttpClient through an HttpClientBuilder.
				HttpClient client = HttpClientBuilder.create().disableRedirectHandling().build();
				//Note that my builder disables redirecting to the specified "Location" header of the response. 
				//This is because I want to manually load that location to the browser.
				
				HttpPost post = new HttpPost(apiURL);
				//Set the following headers to your request
				post.addHeader("Accept","application/json");
				post.addHeader("Authorization", authorization);
				post.addHeader("MIME-Version", "1.0");

				/**********************************************************
				 * Create the body and set it to your request.
				 * Since an MMS contains a message and an attachment,
				 * create a Multipart Entity and add the message 
				 * and the attachment as two separate parts:
				 * -JSON request
				 * 	--must have a part name of "root-fields:
				 *  --must have a content type of 'applicatin/json'
				 *  --must have a charset of UTF-8
				 * -Attachment
				 *  --must have a part name of "Attachments"
				 *  --must have a content type equivalent to its file type
				 *  --must set a valid file name
				 *********************************************************/
				
	            MultipartEntityBuilder reqEntityBuilder = MultipartEntityBuilder.create();
	            reqEntityBuilder.addBinaryBody("root-fields", new ByteArrayInputStream(jsonRequest.getBytes()), ContentType.create("application/json", Charset.forName("UTF-8")), null);
	            reqEntityBuilder.addBinaryBody("Attachments", new FileInputStream(attachmentJPG), ContentType.create("image/jpg"), attachmentJPG.getName());
	            HttpEntity reqEntity = reqEntityBuilder.build();
	            post.setEntity(reqEntity);
	            
				try {
					
					
				HttpResponse response = client.execute(post);   // Execute the HttpPost
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
