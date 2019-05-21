/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.adportas.dominio;

/**
 *
 * @author Jose
 */
public class Extension {
    
    public static int LARGO_ANEXO = 6;
    
    private int id;
    private String numero;
    private int idUsuario;
    private String nombreUsuario;
    private int idCentroCosto;
    private String nombreCentroCosto;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public int getIdCentroCosto() {
        return idCentroCosto;
    }

    public void setIdCentroCosto(int idCentroCosto) {
        this.idCentroCosto = idCentroCosto;
    }

    public String getNombreCentroCosto() {
        return nombreCentroCosto;
    }

    public void setNombreCentroCosto(String nombreCentroCosto) {
        this.nombreCentroCosto = nombreCentroCosto;
    }
    
    
    
}
