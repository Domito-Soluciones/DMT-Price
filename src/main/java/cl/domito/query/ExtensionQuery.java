package cl.domito.query;

import cl.domito.bd.Conexion;
import cl.domito.dominio.Extension;
import cl.domito.log.Log;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class ExtensionQuery {
    
    private final static Logger LOG = Logger.getLogger(ExtensionQuery.class);
    
    public static Map<String,Extension> obtenerExtensiones() {
        Map<String,Extension> extensiones = new HashMap();
        String sql = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            ps = Conexion.CONECCION_TARIFICADOR.prepareStatement(sql);
            rs = ps.executeQuery();
            while (rs.next()) {
                Extension extension = new Extension();
                extension.setId(rs.getInt("extension_id"));
                extension.setNumero(rs.getString("extension_numero").trim());
                extension.setIdUsuario(rs.getInt("usuario_id"));
                extension.setNombreUsuario(rs.getString("usuario_nombre").trim());
                extension.setIdCentroCosto(rs.getInt("centrocosto_id"));
                extension.setNombreCentroCosto(rs.getString("centrocosto_nombre").trim());
                extensiones.put(extension.getNumero(),extension);
            }
        }
        catch(SQLException e){
            Log.getError(LOG,"ERROR SQL EN obtenerExtensiones "+e.getMessage());
        }
        catch(Exception e){
            Log.getError(LOG,"ERROR GE EN obtenerExtensiones "+e.getMessage());
        }
        finally{
            Conexion.cerrar(ps, rs);
        }
        return extensiones;
    }
}
