package cl.domito.query;

import cl.domito.bd.Conexion;
import cl.domito.dominio.Extension;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Map;

public class ExtensionQuery {
    
    
    public static Map<String,Extension> getExtensiones(){
        Connection con = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT * FROM tbl_extension JOIN ";
            con = Conexion.conectar();
            st = con.createStatement();
            rs = st.executeQuery(sql);
        }
        catch(Exception e){
        }
        finally{
        }
        return null;
    }
}
