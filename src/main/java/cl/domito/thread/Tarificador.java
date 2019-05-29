package cl.domito.thread;

import cl.domito.bd.Conexion;
import cl.domito.dominio.CDR;
import cl.domito.dominio.Extension;
import cl.domito.log.Log;
import cl.domito.query.ExtensionQuery;
import cl.domito.util.Util;
import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;

public class Tarificador {
        
    private final static Logger LOG = Logger.getLogger(Tarificador.class);
    
    public void tarificar(){
        Map<String, Extension> extensiones = null;
        while(true){
            extensiones = ExtensionQuery.obtenerExtensiones();
            Conexion.conectarTarificador();
            ArrayList<File> archivos = Util.obtenerArchivos(CDR.CARPETA_RESPALDO.toString());
            if(archivos != null){
                if (archivos.isEmpty()) {
                    Log.getInfo(LOG,"NO HAY ARCHIVOS EN EL DIRECTORIO "+CDR.CARPETA_RESPALDO);
                    return;
                }
                extensiones = ExtensionQuery.obtenerExtensiones();
                for(File archivo : archivos){
                    if(archivo.getName().endsWith(".cdr")){
                        
                    }
                }
            }
        }
    }    
}
