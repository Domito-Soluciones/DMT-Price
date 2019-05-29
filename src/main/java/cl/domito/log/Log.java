package cl.domito.log;

import org.apache.log4j.Logger;

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
