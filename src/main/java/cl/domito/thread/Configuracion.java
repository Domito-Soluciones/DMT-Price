package cl.domito.thread;

import cl.domito.bd.Conexion;
import cl.domito.dominio.CDR;
import cl.domito.dominio.Extension;
import cl.domito.log.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Configuracion extends Thread{
        
    private final static Logger LOG = Logger.getLogger(Configuracion.class);
    private static final Properties PROPIEDADES = new Properties();
    
    @Override
    public void run() {
        Log.getInfo(LOG,"INICIANDO THREAD DE CONFIGURACIONES...");
        while(true){
            try{
                cargarParamentros();
            }
            catch(Exception e){
                Log.getError(LOG,"ERROR EN HILO DE CONFIGURACIONES "+e.getMessage());
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException ie){
                    Log.getError(LOG,"ERROR EN HILO DE CONFIGURACIONES "+e.getMessage());
                }
            }
            finally{
                try{
                    Thread.sleep(30000);
                }
                catch(InterruptedException e){
                    Log.getError(LOG,"ERROR EN SLEEP DE 30 SEGUNDOS "+e.getMessage());
                }
            }
        }
    }
    private void cargarParamentros(){
        Log.getInfo(LOG,"CARGANDO PARAMETROS...");
        FileInputStream properties = null;
        try{
            String config = Configuracion.class.getClassLoader().getResource("config.properties").getPath();
            properties = new FileInputStream(config);
            PROPIEDADES.load(properties);
            CDR.CARPETA_CDR = new File(validarParametro("carpeta.cdr"));
            CDR.CARPETA_RESPALDO = new File(validarParametro("carpeta.respaldo"));
            Extension.LARGO_ANEXO = Integer.parseInt(validarParametro("largo.anexo"));
            CDR.RESPALDAR = Boolean.parseBoolean(validarParametro("respaldar.cdr"));
            Conexion.HOST = validarParametro("basedato.ip");
            Conexion.PORT = validarParametro("basedato.puerto");
            Conexion.BD = validarParametro("basedato.nombre");
            Conexion.USER = validarParametro("basedato.usuario");
            Conexion.PASS = validarParametro("basedato.clave");
            Log.getInfo(LOG,"PARAMETRO CARGADO: carpeta.cdr: "+CDR.CARPETA_CDR);
            Log.getInfo(LOG,"PARAMETRO CARGADO: carpeta.respaldo: "+CDR.CARPETA_RESPALDO);
            Log.getInfo(LOG,"PARAMETRO CARGADO: largo.anexo: "+Extension.LARGO_ANEXO);
            Log.getInfo(LOG,"PARAMETRO CARGADO: respaldar.cdr: "+CDR.RESPALDAR);
            Log.getInfo(LOG,"PARAMETRO CARGADO: basedato.ip: "+Conexion.HOST);
            Log.getInfo(LOG,"PARAMETRO CARGADO: basedato.puerto: "+Conexion.PORT);
            Log.getInfo(LOG,"PARAMETRO CARGADO: basedato.nombre: "+Conexion.BD);
            Log.getInfo(LOG,"PARAMETRO CARGADO: basedato.usuario: "+Conexion.USER);
            Log.getInfo(LOG,"PARAMETRO CARGADO: basedato.clave: "+Conexion.PASS);            
        }
        catch(IOException ioe){
            Log.getError(LOG, "ERROR AL CARGAR PARAMETROS VERIFIQUE SI EL ARCHIVO config.properties ESTA EN LA CARPETA DE RECURSOS "+ioe);
            System.exit(0);
        }
        catch(NumberFormatException nfe){
            Log.getError(LOG, "ERROR AL CARGAR PARAMETRO LARGO ANEXO, DEBE SER NUMERICO "+nfe);
        }
        catch(Exception e){
            Log.getError(LOG, "ERROR GENERAL EN CARGAR PARAMETROS "+e);
        }
        finally{
            try{
                if(properties != null){
                    properties.close();
                }
            }
            catch(IOException e){
                Log.getError(LOG, "ERROR AL CERRAR FileInputStream CARGAR PARAMETROS "+e);
            }
        }
    }
    
    private String validarParametro(String nombreParametro){
        String variable = null;
        try{
                variable = PROPIEDADES.getProperty(nombreParametro);
        }
        catch(Exception e){
            Log.getError(LOG, "ERROR AL ASIGNAR VALOR PARAMETRO "+nombreParametro+" "+e.getMessage());
            System.exit(0);
        }
        return variable;
    }

}
