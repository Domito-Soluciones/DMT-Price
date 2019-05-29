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
    
}
