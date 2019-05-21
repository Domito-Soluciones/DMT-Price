package cl.adportas.main;

import cl.adportas.thread.Configuracion;
import cl.adportas.thread.Estadistica;
import cl.adportas.thread.Tarificador;

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
