/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mpos.newthree.utils;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 *
 * @author Olaniyi
 */
public class Loggin {
   public static void logTransactioninfo(String log) throws IOException {

    Logger logger = Logger.getLogger("MyLog");
               FileHandler fh;
                try {  
        // This block configure the logger with handler and formatter 
                    
        fh = new FileHandler("log/sterlingTransactionLog.xml", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter); 
        logger.info(log);  
        System.out.println("Log has been generated");
    
}catch(Exception e){
e.printStackTrace();}
    
    }

public static void vuvaa(String vulog) throws IOException {

    Logger logger = Logger.getLogger("MyLog");
               FileHandler fh;
                try {  
        // This block configure the logger with handler and formatter 
                    
        fh = new FileHandler("log/vuvaaTransactionlog.xml", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter); 
        logger.info(vulog);  
        System.out.println("Log has been generated");
    
}catch(Exception e){
e.printStackTrace();}
    
    }
}
