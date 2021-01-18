/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpos.newthree.utils;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Access-solutions
 */
public class SimpleLogger {
    Calendar cal = Calendar.getInstance();
    //cal.get(Calendar.HOUR_OF_DAY);
    DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //Date for File Name
    DateFormat dateFormatfile = new SimpleDateFormat("yyyy/MM/dd");
    Date date = new Date();
    
    
    public void writeToFile(){
        System.out.println(dateFormatfile.format(date)+".log");
        
        cal.get(Calendar.DATE);
        cal.get(Calendar.MONTH);
        cal.get(Calendar.YEAR);
        
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;
          
        try {  
              
            // This block configure the logger with handler and formatter
            
            fh = new FileHandler("C:/temp/test/MyLogFile.log");
            logger.addHandler(fh);  
            //logger.setLevel(Level.ALL);  
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);  
              
            // the following statement is used to log any messages  
            logger.info("My first log");  
              
        } catch (SecurityException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }  
          
        logger.info("Hi How r u?");  
        
        System.out.println(new File(".").getAbsoluteFile());
        String s = "log/" + dateFormatfile.format(date) + ".log";
    }
}
