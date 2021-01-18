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
public class Exceptionlog {
    public static void info(String log) throws IOException {

    Logger logger = Logger.getLogger("MyLog");
               FileHandler fh;
                try {  
        // This block configure the logger with handler and formatter 
                    
        fh = new FileHandler("log/Exception.xml", true);
        logger.addHandler(fh);
        SimpleFormatter formatter = new SimpleFormatter();
        fh.setFormatter(formatter); 
        logger.info(log);  
        System.out.println("Log has been generated");
    
}catch(Exception e){
e.printStackTrace();}
    
    }}
