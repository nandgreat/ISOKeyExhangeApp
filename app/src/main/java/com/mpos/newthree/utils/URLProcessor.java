/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpos.newthree.utils;

/**
 *
 * @author samycorps
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
//* import org.apache.log4j.Logger;

public class URLProcessor implements Runnable {
    //* private final Logger logger = Logger.getLogger(URLProcessor.class);
    private HttpURLConnection connection;
    private URL urlObject;
    private BufferedReader in;
    private StringBuilder response = null;
    private String opurl = "";
        
    public URLProcessor() {
        
    }
    
    public URLProcessor(String url) {
        this.opurl = url;
    }

    public URLProcessor(String host, String data) {
        this.opurl = host+"?"+data;
    }
    
    public void setOpUrl(String opurl) {
        this.opurl = opurl;
    }
    
    public String submit()
    {        
        int responseCode = -1;
        try
        {
            // Added to cater for HTTPS
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            //*    Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            //logger.info("URL Processor::submit() - " + opurl);
            urlObject = new URL(opurl);
            connection = (HttpURLConnection)urlObject.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            //connection.setRequestProperty("Content-Length", Integer.toString(opurl.length()));
            connection.connect();
            responseCode = connection.getResponseCode();
            if(responseCode == 200)
            {                
                //logger.info("URL Processor - Connected to Server: " + getUrl());
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String out = "";
                response = new StringBuilder();
                while((out = in.readLine()) != null) {
                    response.append(out);
                }
                //*logger.info("Response: " + response.toString());
                 
            } else
            {
                //* logger.info("URLProcessor:: - Connection Failed: "+responseCode+" - " + opurl);
                //submitted = 1;
                
                response = new StringBuilder("Connection Failed: Code = ");
                response.append(responseCode);
            }
        }
        catch(Exception ex)
        {
            //logger.info("SMS Delivery::" + eee);
            //logger.info("Exception Error: "+ex);
            response = new StringBuilder("Exception: ");
            response.append(ex.getMessage());
            
        }
        return response.toString();
    }
    
    public static void main(String[] args) throws UnsupportedEncodingException {
        //URLProcessor url = null;
        String stan = "000345";
        String phoneNo = "08024035037";
        String meterNo = "0415462838";
        String pinNo = "1234 5677 8890 9097 7654";
        
        StringBuilder sb = new StringBuilder("http://www.mobbow.com/vuvaa_post_sms.php?sms_id=");
        sb.append("");        
        sb.append(stan);
        sb.append("&sms_to=");
        sb.append("234");
        sb.append(phoneNo.substring(1));
        //sb.append("&sms_from=FIDELITYKSK");
        sb.append("&sms_message=");
        sb.append(URLEncoder.encode("Thank you for using Fidelity Bank Plc Self Service Kiosk. Your Meter No is: ", "UTF-8"));
        sb.append(meterNo);
        sb.append(URLEncoder.encode(". Your PIN is: ", "UTF-8"));
        sb.append(URLEncoder.encode(pinNo, "UTF-8"));
        sb.append("&sender_id=sunnyben&username=sunnyben&password=goodies");            
            
        // 1 = successful
        // 2 = failure
            
            Thread t = new Thread(new URLProcessor(sb.toString()));
            t.start();
        //String val = "/audittrail2?pan=5021920768900020165&procCode=531000&amount=5000&stan=001263&respCode=00&reverse=0&termId=23200008&merchId=2329000024%20%20%20%20%20&provider=MTN&phoneNo=08060223271&curBalance=253400&transDate=12112012115625&pin=";
        //logger.info(val);
        //url = new URLProcessor("http://www.vixae.com:9000"+val);        
    }

    @Override
    public void run() {
        int responseCode = -1;
        try
        {
            // Added to cater for HTTPS
            System.setProperty("java.protocol.handler.pkgs", "com.sun.net.ssl.internal.www.protocol");
            //*    Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
            //logger.info("URL Processor::submit() - " + opurl);
            urlObject = new URL(opurl);
            connection = (HttpURLConnection)urlObject.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            //connection.setRequestProperty("Content-Length", Integer.toString(opurl.length()));
            connection.connect();
            responseCode = connection.getResponseCode();
            if(responseCode == 200)
            {                
                //logger.info("URL Processor - Connected to Server: " + opurl);
                in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String out = "";
                response = new StringBuilder();
                while((out = in.readLine()) != null) {
                    response.append(out);
                }
                //*logger.info("HTTP Response: " + response.toString());
                 
            } else
            {
                //*  logger.info("URLProcessor:: - Connection Failed: "+responseCode+" - " + opurl);
                //submitted = 1;
                
                response = new StringBuilder("Connection Failed: Code = ");
                response.append(responseCode);
            }
        }
        catch(Exception ex)
        {
            //logger.info("SMS Delivery::" + eee);
            //logger.info("Exception Error: "+ex);
            response = new StringBuilder("Exception: ");
            response.append(ex.getMessage());
            
        }        
    }
}
