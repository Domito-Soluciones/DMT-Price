package cl.domito.main;

import cl.domito.thread.Configuracion;
import cl.domito.thread.Estadistica;
import cl.domito.thread.Tarificador;

public class Main {
    
    public static void main(String[] args) {
        Configuracion configuracion = new Configuracion();
        configuracion.start();
        Estadistica estadistica = new Estadistica();
        estadistica.start();
        Tarificador tarificador = new Tarificador();
        tarificador.tarificar();
    }
    
}
