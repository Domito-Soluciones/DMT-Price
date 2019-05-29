package cl.domito.thread;

import cl.domito.bd.Conexion;
import cl.domito.dominio.CDR;
import cl.domito.dominio.Extension;
import cl.domito.log.Log;
import cl.domito.query.ExtensionQuery;
import cl.domito.util.Util;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import org.apache.log4j.Logger;

public class Tarificador {
        
    private final int CANTIDAS_TITULOS = 2;
    
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
                CDR cdr = CDR.getInstance();
                for(File archivo : archivos){
                    try{
                        if(archivo.getName().endsWith(".cdr")){
                            BufferedReader buffer = new BufferedReader(new FileReader(archivo));
                            String linea;
                            int nLinea = 0;
                            while ((linea = buffer.readLine()) != null) {
                                nLinea++;
                                if (nLinea > CANTIDAS_TITULOS) {
                                    String[] camposCdr = linea.split(",");
                                    cdr.pkid = Util.reemplazarComillas(camposCdr[CDR.PKID]);
                                    cdr.origLegCallIdentifier = Util.convertirNumero(camposCdr[CDR.ORIG_LEG_CALL_IDENTIFIER]);
                                    cdr.destLegCallIdentifier = Util.convertirNumero(camposCdr[CDR.DEST_LEG_CALL_IDENTIFIER]);
                                    cdr.dateTimeOrigination = new Timestamp(Util.convertirLong(camposCdr[CDR.DATE_TIME_ORIGINATION]) * 1000L);
                                    cdr.dateTimeConnect = new Timestamp(Util.convertirLong(camposCdr[CDR.DATE_TIME_CONNECT]));
                                    cdr.dateTimeDisconnect = new Timestamp(Util.convertirLong(camposCdr[CDR.DATE_TIME_DISCONNECT]) * 1000L);
                                    cdr.origSpan = Util.convertirNumero(camposCdr[CDR.ORIG_SPAN]);
                                    cdr.destSpan = Util.convertirNumero(camposCdr[CDR.DEST_SPAN]);
                                    cdr.devicenameOrigen = Util.reemplazarComillas(camposCdr[CDR.ORIG_DEVICE_NAME]);
                                    cdr.devicenameDestino = Util.reemplazarComillas(camposCdr[CDR.DEST_DEVICE_NAME]);
                                    cdr.numeroLlamante = Util.reemplazarComillas(camposCdr[CDR.CALLING_PARTY_NUMBER]);
                                    cdr.numeroLlamado = Util.reemplazarComillas(camposCdr[CDR.ORIGINAL_CALLED_PARTY_NUMBER]);
                                    cdr.ultimaRed = Util.reemplazarComillas(camposCdr[CDR.LAST_REDIRECT_DN]);
                                    cdr.numFinal = Util.reemplazarComillas(camposCdr[CDR.FINAL_CALLED_PARTY_NUMBER]);
                                    cdr.particionLlamado = Util.reemplazarComillas(camposCdr[CDR.ORIGINAL_CALLED_PARTY_NUMBER_PARTITION]);
                                    cdr.particionLlamante = Util.reemplazarComillas(camposCdr[CDR.CALLING_PARTY_NUMBER_PARTITION]);
                                    cdr.particionFinal = Util.reemplazarComillas(camposCdr[CDR.FINAL_CALLED_PARTY_NUMBER_PARTITION]);
                                    cdr.particionLR = Util.reemplazarComillas(camposCdr[CDR.LAST_REDIRECT_DN_PARTITION]);
                                    
                                    if ((cdr.numeroLlamado.length() <= Extension.LARGO_ANEXO) && (cdr.numeroLlamante.length() > Extension.LARGO_ANEXO)) {
                                        cdr.tipoLlamada = Extension.LLAMADA_ENTRANTE;
                                        cdr.extension = cdr.numeroLlamado;
                                        cdr.contraparte = cdr.numeroLlamante;
                                        cdr.particionExtension = cdr.particionLlamado;
                                        cdr.particionContraparte = cdr.particionLlamante;
                                    }
                                    else if ((cdr.numeroLlamado.length() > Extension.LARGO_ANEXO) && (cdr.numeroLlamante.length() <= Extension.LARGO_ANEXO) || (cdr.ultimaRed.length() == Extension.LARGO_ANEXO) && (cdr.numFinal.length() > Extension.LARGO_ANEXO)) {
                                        cdr.tipoLlamada = Extension.LLAMADA_SALIENTE;
                                        cdr.contraparte = cdr.numFinal;
                                        cdr.particionContraparte = cdr.particionFinal;
                                        if ((cdr.ultimaRed.length() == Extension.LARGO_ANEXO) && (cdr.numFinal.length() > Extension.LARGO_ANEXO)) {
                                            cdr.extension = cdr.ultimaRed;
                                            cdr.particionExtension = cdr.particionLR;
                                        }
                                        else {
                                            cdr.extension = cdr.numeroLlamante;
                                            cdr.particionExtension = cdr.particionLlamante;
                                        }
                                    }
                                    else if ((cdr.numeroLlamado.length() <= Extension.LARGO_ANEXO) && (cdr.numeroLlamante.length() <= Extension.LARGO_ANEXO) && (cdr.ultimaRed.length() <= Extension.LARGO_ANEXO) && (cdr.numFinal.length() <= Extension.LARGO_ANEXO)) {
                                        cdr.tipoLlamada = Extension.LLAMADA_ANEXO;
                                        cdr.extension = cdr.numeroLlamante;
                                        cdr.contraparte = cdr.numeroLlamado;
                                        cdr.particionExtension = cdr.particionLlamante;
                                        cdr.particionContraparte =  cdr.particionLlamado;
                                    }
                                    else if ((cdr.origSpan == 0) && (cdr.destSpan != 0)) {
                                        cdr.tipoLlamada = Extension.LLAMADA_SALIENTE;
                                        cdr.contraparte = cdr.numFinal;
                                        cdr.extension =  cdr.numeroLlamante;
                                        cdr.particionContraparte = cdr.particionFinal;
                                        cdr.particionExtension = cdr.particionLlamante;
                                    }
                                    else if ((cdr.origSpan == 0) && (cdr.destSpan == 0)) {
                                        cdr.tipoLlamada = Extension.LLAMADA_ANEXO;
                                        cdr.extension = cdr.numeroLlamante;
                                        cdr.contraparte = cdr.numeroLlamado;
                                        cdr.particionExtension = cdr.particionLlamante;
                                        cdr.particionContraparte = cdr.particionLlamado;
                                    }
                                    else {
                                        cdr.tipoLlamada = Extension.LLAMADA_ENTRANTE;
                                        cdr.extension = cdr.numeroLlamado;
                                        cdr.contraparte = cdr.numeroLlamante;
                                        cdr.particionExtension = cdr.particionLlamado;
                                        cdr.particionContraparte = cdr.particionLlamante;
                                    }
                                }
                            }
                        }
                    }
                    catch(Exception e){
                        Log.getError(LOG,"ERROR GENERAL EN TARIFICAD0R "+e.getMessage());
                    }
                }
            }
        }
    }    
}
