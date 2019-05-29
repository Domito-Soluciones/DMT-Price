package cl.domito.util;

import java.io.File;
import java.util.ArrayList;

public class Util {
    
    public static ArrayList<File> obtenerArchivos(String nombre_carpeta) {
        ArrayList archivos = new ArrayList();
        File[] carpeta = new File(nombre_carpeta).listFiles();
        for (File f : carpeta) {
            if (f.isDirectory()) {
                archivos.addAll(obtenerArchivos(f.toString()));
            } else if (f.isFile()) {
                archivos.add(f);
            }
        }
        return archivos;
    }
    
    public static String reemplazarComillas(String cadena){
        return cadena.replaceAll("\"", "").trim();
    }
    
    public static int convertirNumero(String cadena){
        int numero = 0;
        try{
            numero = Integer.parseInt(cadena);
        }
        catch(NumberFormatException e){
            numero = 0;
        }
        return numero;
    }
    
    public static long convertirLong(String cadena){
        long numero = 0;
        try{
            numero = Long.parseLong(cadena);
        }
        catch(NumberFormatException e){
            numero = 0;
        }
        return numero;
    }
    
}
