/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cl.adportas.bd;

import cl.adportas.main.Main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.apache.log4j.Logger;

public class Conexion {
    
    public static String HOST = "localhost";
    public static String PORT = "3306";
    public static String BD = "tarifikall";
    public static String USER = "root";
    public static String PASS = "123456";
    
    private final static Logger LOG = Logger.getLogger(Conexion.class);

    
    public static Connection conectar() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + BD,USER,PASS);
        } 
        catch (Exception e) {
            LOG.error("ERROR EN CONEXION A BASE DE DATOS: "+e.getMessage());
            System.exit(0);
        } 
        return connection;
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
