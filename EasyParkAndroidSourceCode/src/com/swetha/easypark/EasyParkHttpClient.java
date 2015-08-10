/***************************************************************
Copyright (c) 2015 swethavs

[This program is licensed under the "MIT License"]

Please see the file COPYING in the source

distribution of this software for license terms.
****************************************************************/


package com.swetha.easypark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONObject;

import android.util.Log;


/**
 * 
 * @author Swetha Venkatachari Sundarajan
 * This class makes an HTTP post request to the URL with the post params
 *
 */


public class EasyParkHttpClient {
	
	public static final int HTTP_TIMEOUT = 100 * 1000; // milliseconds
	private static HttpClient mHttpClient; 
	static JSONObject jObj = null;
    static String json = "";
    static InputStream is = null;

	 private static HttpClient getHttpClient() {

	  if (mHttpClient == null) {
	   mHttpClient = new DefaultHttpClient();
	   
	   final HttpParams params = mHttpClient.getParams();
	   HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
	   HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
	   ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
	  }

	  return mHttpClient;
	 }
	 /** 
	  * @param url of the website and the post request
	  * @return http response in string */
	 public static String executeHttpPost(String url,ArrayList<NameValuePair> postParameters) throws Exception {

	 		BufferedReader in = null;

		  try {
		
		   HttpClient client = getHttpClient();
		
		   HttpPost request = new HttpPost(url);
		
		   UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(
		
		     postParameters);
		
		   request.setEntity(formEntity);
		Log.i("CustomHttpClient", "before sending request" +request);
		   HttpResponse response = client.execute(request);
			Log.i("CustomHttpClient", "after sending request" +response.toString());
			//Log.i("CustomHttpClient", "after sending request response in to string" +response.getEntity().getContent().toString());

			  in = new BufferedReader(new InputStreamReader(response.getEntity()
						
					     .getContent()));
					
					   
					
					   StringBuffer sb = new StringBuffer("");
					
					   String line = "";
					
					   String NL = System.getProperty("line.separator");
					
					   while ((line = in.readLine()) != null) {
					
					    sb.append(line + NL);
					
					   }
		   
		
		 in.close();

		   String result = sb.toString();
		
		   return result;
		
		   
		   }
		   
		
		  finally {

			  if (in != null) {

				    try {

				     in.close();

				    } catch (IOException e) {

				     Log.e("log_tag", "Error converting result "+e.toString()); 

				     e.printStackTrace();
	

	  }
	  
			  }
		  }
	   

	 }
	 
	
}
