package cl.domito.thread;

import cl.domito.dominio.Extension;
import cl.domito.query.ExtensionQuery;
import java.util.Map;

public class Tarificador {
        
    public void tarificar(){
        Map<String, Extension> extensiones = null;
        while(true){
            extensiones = ExtensionQuery.getExtensiones();
            
        }
    }
    
}
