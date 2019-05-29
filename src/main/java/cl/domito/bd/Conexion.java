package cl.domito.bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class Conexion {
    
    public static String HOST = "localhost";
    public static String PORT = "3306";
    public static String BD = "tarificador";
    public static String USER = "root";
    public static String PASS = "";

    public static Connection CONECCION_TARIFICADOR;
    public static Connection CONECCION_ESTADISTICA;
    
    private final static Logger LOG = Logger.getLogger(Conexion.class);

    
    public static void conectarTarificador() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            CONECCION_TARIFICADOR = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + BD,USER,PASS);
        } 
        catch (Exception e) {
            LOG.error("ERROR EN CONEXION A BASE DE DATOS: "+e.getMessage());
            System.exit(0);
        } 
    }
    public static void conectarEstadistica() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            CONECCION_ESTADISTICA = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + BD,USER,PASS);
        } 
        catch (Exception e) {
            LOG.error("ERROR EN CONEXION A BASE DE DATOS: "+e.getMessage());
            System.exit(0);
        } 
    }

    public static void desconectar(Connection connection) 
    {
        try {
            if(connection != null && !connection.isClosed())
            {
                connection.close();
            }
        }
        catch (Exception e) {
            LOG.error("ERROR EN DESCONEXION A BASE DE DATOS: "+e.getMessage());
        } 
    }
    
    public static void cerrar(PreparedStatement ps, ResultSet rs) 
    {
        if (ps != null) 
        {
            try 
            {
                ps.close();
            }
            catch (Exception e) 
            {
                LOG.error("ERROR EN CERRAR PS "+e.getMessage());
            }
        }
        if (rs != null) 
        {
            try 
            {
                rs.close();
            }
            catch (Exception e) 
            {
                LOG.error("ERROR EN CERRAR RS: "+e.getMessage());
            }
        }
    }
    
    
}
