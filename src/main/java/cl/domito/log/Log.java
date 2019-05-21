/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.adportas.log;

import org.apache.log4j.Logger;

/**
 *
 * @author Jose
 */
public class Log {
        
    public static void getInfo(Logger log,String mensaje){
        if(log.isInfoEnabled()){
            log.info(mensaje);
        }
    }
    public static void getError(Logger log,String mensaje){
        if(log.isTraceEnabled()){
            log.error(mensaje);
        }
    }
    public static void getDebugError(Logger log,String mensaje){
        if(log.isDebugEnabled()){
            log.error(mensaje);
        }
    }
}
