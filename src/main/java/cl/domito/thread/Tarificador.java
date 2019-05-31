package cl.domito.thread;

import cl.domito.bd.Conexion;
import cl.domito.dominio.CDR;
import cl.domito.dominio.Extension;
import cl.domito.dominio.Llamada;
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
            Conexion.conectarTarificador();
            ArrayList<File> archivos = Util.obtenerArchivos(CDR.CARPETA_RESPALDO.toString());
            if(archivos != null){
                if (archivos.isEmpty()) {
                    Log.getInfo(LOG,"NO HAY ARCHIVOS EN EL DIRECTORIO "+CDR.CARPETA_RESPALDO);
                    return;
                }
                extensiones = ExtensionQuery.obtenerExtensiones();
                Llamada call = Llamada.getInstance();
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
                                    call.pkid = Util.reemplazarComillas(camposCdr[CDR.PKID]);
                                    call.origLegCallIdentifier = Util.convertirNumero(camposCdr[CDR.ORIG_LEG_CALL_IDENTIFIER]);
                                    call.destLegCallIdentifier = Util.convertirNumero(camposCdr[CDR.DEST_LEG_CALL_IDENTIFIER]);
                                    call.dateTimeOrigination = new Timestamp(Util.convertirLong(camposCdr[CDR.DATE_TIME_ORIGINATION]) * 1000L);
                                    call.dateTimeConnect = new Timestamp(Util.convertirLong(camposCdr[CDR.DATE_TIME_CONNECT]));
                                    call.dateTimeDisconnect = new Timestamp(Util.convertirLong(camposCdr[CDR.DATE_TIME_DISCONNECT]) * 1000L);
                                    call.origSpan = Util.convertirNumero(camposCdr[CDR.ORIG_SPAN]);
                                    call.destSpan = Util.convertirNumero(camposCdr[CDR.DEST_SPAN]);
                                    call.devicenameOrigen = Util.reemplazarComillas(camposCdr[CDR.ORIG_DEVICE_NAME]);
                                    call.devicenameDestino = Util.reemplazarComillas(camposCdr[CDR.DEST_DEVICE_NAME]);
                                    call.numeroLlamante = Util.reemplazarComillas(camposCdr[CDR.CALLING_PARTY_NUMBER]);
                                    call.numeroLlamado = Util.reemplazarComillas(camposCdr[CDR.ORIGINAL_CALLED_PARTY_NUMBER]);
                                    call.ultimaRed = Util.reemplazarComillas(camposCdr[CDR.LAST_REDIRECT_DN]);
                                    call.numFinal = Util.reemplazarComillas(camposCdr[CDR.FINAL_CALLED_PARTY_NUMBER]);
                                    call.particionLlamado = Util.reemplazarComillas(camposCdr[CDR.ORIGINAL_CALLED_PARTY_NUMBER_PARTITION]);
                                    call.particionLlamante = Util.reemplazarComillas(camposCdr[CDR.CALLING_PARTY_NUMBER_PARTITION]);
                                    call.particionFinal = Util.reemplazarComillas(camposCdr[CDR.FINAL_CALLED_PARTY_NUMBER_PARTITION]);
                                    call.particionLR = Util.reemplazarComillas(camposCdr[CDR.LAST_REDIRECT_DN_PARTITION]);                                    
                                    call.duracion = Util.convertirNumero(camposCdr[CDR.DURATION]);
                                    call.globalCallID = Util.convertirNumero(camposCdr[CDR.GLOBAL_CALL_ID]);
                                    call.destConversationId = Util.convertirNumero(camposCdr[CDR.DEST_CONVERSDATION_ID]);
                                    call.particionLR = Util.reemplazarComillas(camposCdr[CDR.LAST_REDIRECT_DN_PARTITION]);
                                    call.particionFinal = Util.reemplazarComillas(camposCdr[CDR.FINAL_CALLED_PARTY_NUMBER_PARTITION]);
                                    this.setValoresLlamada();
                                    
                                    if (call.extension != null && !call.extension.trim().equalsIgnoreCase("") && extensiones.get(call.extension) != null) {
                                        if(call.tipoLlamada == Extension.LLAMADA_SALIENTE && call.duracion > 0){
                                            call.costo = ExtensionQuery.obtenerCostoLlamada(call.contraparte, call.particionContraparte, call.duracion, call.dateTimeOrigination);
                                        }
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

    private void setValoresLlamada() {
        Llamada call = Llamada.getInstance();
        if ((call.numeroLlamado.length() <= Extension.LARGO_ANEXO) && (call.numeroLlamante.length() > Extension.LARGO_ANEXO)) {
            call.tipoLlamada = Extension.LLAMADA_ENTRANTE;
            call.extension = call.numeroLlamado.trim();
            call.contraparte = call.numeroLlamante;
            call.particionExtension = call.particionLlamado;
            call.particionContraparte = call.particionLlamante;
        }
        else if ((call.numeroLlamado.length() > Extension.LARGO_ANEXO) && (call.numeroLlamante.length() <= Extension.LARGO_ANEXO) || (call.ultimaRed.length() == Extension.LARGO_ANEXO) && (call.numFinal.length() > Extension.LARGO_ANEXO)) {
            call.tipoLlamada = Extension.LLAMADA_SALIENTE;
            call.contraparte = call.numFinal;
            call.particionContraparte = call.particionFinal;
            if ((call.ultimaRed.length() == Extension.LARGO_ANEXO) && (call.numFinal.length() > Extension.LARGO_ANEXO)) {
                call.extension = call.ultimaRed;
                call.particionExtension = call.particionLR;
            }
            else {
                call.extension = call.numeroLlamante;
                call.particionExtension = call.particionLlamante;
            }
        }
        else if ((call.numeroLlamado.length() <= Extension.LARGO_ANEXO) && (call.numeroLlamante.length() <= Extension.LARGO_ANEXO) && (call.ultimaRed.length() <= Extension.LARGO_ANEXO) && (call.numFinal.length() <= Extension.LARGO_ANEXO)) {
            call.tipoLlamada = Extension.LLAMADA_ANEXO;
            call.extension = call.numeroLlamante;
            call.contraparte = call.numeroLlamado;
            call.particionExtension = call.particionLlamante;
            call.particionContraparte =  call.particionLlamado;
        }
        else if ((call.origSpan == 0) && (call.destSpan != 0)) {
            call.tipoLlamada = Extension.LLAMADA_SALIENTE;
            call.contraparte = call.numFinal;
            call.extension =  call.numeroLlamante;
            call.particionContraparte = call.particionFinal;
            call.particionExtension = call.particionLlamante;
        }
        else if ((call.origSpan == 0) && (call.destSpan == 0)) {
            call.tipoLlamada = Extension.LLAMADA_ANEXO;
            call.extension = call.numeroLlamante;
            call.contraparte = call.numeroLlamado;
            call.particionExtension = call.particionLlamante;
            call.particionContraparte = call.particionLlamado;
        }
        else {
            call.tipoLlamada = Extension.LLAMADA_ENTRANTE;
            call.extension = call.numeroLlamado;
            call.contraparte = call.numeroLlamante;
            call.particionExtension = call.particionLlamado;
            call.particionContraparte = call.particionLlamante;
        }
    }
}
