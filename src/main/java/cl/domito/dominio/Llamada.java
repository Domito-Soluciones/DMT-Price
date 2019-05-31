/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.domito.dominio;

import java.sql.Timestamp;

/**
 *
 * @author Jose
 */
public class Llamada {
        
    private static Llamada LLAMADA = new Llamada();
    
    
    public static Llamada getInstance(){
        return LLAMADA;
    }
    
    public String pkid;
    public int globalCallID;
    public int origLegCallIdentifier;
    public int destLegCallIdentifier;
    public Timestamp dateTimeOrigination;
    public Timestamp dateTimeConnect;
    public Timestamp dateTimeDisconnect;
    public int origSpan;
    public int destSpan;
    public String devicenameOrigen;
    public String devicenameDestino;
    public String numeroLlamante;
    public String numeroLlamado;
    public String ultimaRed;
    public String numFinal;
    public int tipoLlamada;
    public String particionLlamado;
    public String particionLlamante;
    public String particionFinal;
    public String particionLR;
    public int destConversationId;
    public String extension;
    public String contraparte;
    public String particionExtension;
    public String particionContraparte;    
    public int duracion;
    public int costo;
}
