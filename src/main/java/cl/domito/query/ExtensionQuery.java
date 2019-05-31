package cl.domito.query;

import cl.domito.bd.Conexion;
import cl.domito.dominio.Extension;
import cl.domito.log.Log;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    
    public static int obtenerCostoLlamada(String contraparte,String particion,int duracion,Timestamp inicio) {
        float costo = 0.0F;
        PreparedStatement pst = null;
        ResultSet rs = null;
        try {
            String sql = "SELECT * FROM route_pattern WHERE ? ~ pattern AND route NOT LIKE '%.!%' AND particion = ? ORDER BY pattern DESC";
            pst = Conexion.CONECCION_TARIFICADOR.prepareStatement(sql);
            pst.setString(1, contraparte);
            pst.setString(2, particion.trim());
            rs = pst.executeQuery();
            String tipoLlamada = "";
            int cont = 0;
            while (rs.next()) {
                cont = cont + 1;
                if (rs.getString("route") != null && rs.getString("route").trim().startsWith(contraparte)) {
                    tipoLlamada = rs.getString("tipo") != null ? rs.getString("tipo").trim() : "";
                    if (!tipoLlamada.equalsIgnoreCase("ldi")) {
                        String tipoHorario = obtenerHorario(rs.getInt("identificador_horario"), registroOrigenLlamada, horarios);
                        if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                            costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                        } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                            costo = Float.valueOf(rs.getString("valor_economico") != null ? rs.getFloat("valor_economico") : 0.0F);
                        } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                            costo = Float.valueOf(rs.getString("valor_reducido") != null ? rs.getFloat("valor_reducido") : 0.0F);
                        } else {
                            costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                        }
                    } else {
                        //Obteniendo costo LDI 9.1230[1-9]!   
                        System.out.println("Llamada LDI===========   " + contraparte);
                        String patron = rs.getString("route") != null ? rs.getString("route").trim() : "";
                        PreparedStatement pstLDI = con.prepareStatement("select * from carriers order by numero_carrier desc;");
                        ResultSet rsLDI = pstLDI.executeQuery();
                        List<String> carriers = new ArrayList<String>();

                        while (rsLDI.next()) {
                            String llave = rsLDI.getString("numero_carrier") != null ? rsLDI.getString("numero_carrier").trim() : "";
                            if (!llave.equals("") && !carriers.contains(llave)) {
//                            mapaLDI.put(llave, llave);
                                carriers.add(llave);
                            }
                        }
                        if (rsLDI != null) {
                            rsLDI.close();
                        }
                        if (pstLDI != null) {
                            pstLDI.close();
                        }
                        String carrierAux = Utilidades.esLargaDistanciaMario(patron, carriers);
                        if (!carrierAux.equals("")) {
                            System.out.println("Contraparte LDI Encontrada, buscando costo.....= " + contraparte);

                            String cadenaAux = contraparte.substring(((contraparte.indexOf(carrierAux) + carrierAux.length()) - 1), contraparte.length());
                            for (int x = cadenaAux.length(); x >= 0; x--) {
                                if (prefijosInternacional.containsKey(cadenaAux)) {
                                    Particion partiInter = (Particion) prefijosInternacional.get(cadenaAux);
                                    String tipoHorario = obtenerHorario(partiInter.getIdentificadorHorario(), registroOrigenLlamada, horarios);
                                    if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                                        costo = partiInter.getValorPrefijoNormal().floatValue();
                                    } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                                        costo = partiInter.getValorPrefijoEconomico().floatValue();
                                    } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                                        costo = partiInter.getValorPrefijoEconomico().floatValue();
                                    } else {
                                        costo = partiInter.getValorPrefijo().floatValue();
                                    }
                                    break;
                                } else {
                                    cadenaAux = cadenaAux.substring(0, (cadenaAux.length() - 1));
                                }
                            }
                        } else {
                            //Si no encentra el carrier
                            String tipoHorario = obtenerHorario(rs.getInt("identificador_horario"), registroOrigenLlamada, horarios);
                            if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                                costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                            } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                                costo = Float.valueOf(rs.getString("valor_economico") != null ? rs.getFloat("valor_economico") : 0.0F);
                            } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                                costo = Float.valueOf(rs.getString("valor_reducido") != null ? rs.getFloat("valor_reducido") : 0.0F);
                            } else {
                                costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                            }
                        }
                    }
                    tipoLlamada = "";
                    while (!rs.isLast()) {
                        rs.next();
                    }
                } else if (rs.getString("particion") != null && rs.getString("particion").trim().equals(particion.trim())) {
                    tipoLlamada = rs.getString("tipo") != null ? rs.getString("tipo").trim() : "";
                    if (!tipoLlamada.equalsIgnoreCase("ldi")) {
                        String tipoHorario = obtenerHorario(rs.getInt("identificador_horario"), registroOrigenLlamada, horarios);
                        if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                            costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                        } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                            costo = Float.valueOf(rs.getString("valor_economico") != null ? rs.getFloat("valor_economico") : 0.0F);
                        } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                            costo = Float.valueOf(rs.getString("valor_reducido") != null ? rs.getFloat("valor_reducido") : 0.0F);
                        } else {
                            costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                        }
                    } else {
                        //Obteniendo costo LDI 9.1230[1-9]!   
                        System.out.println("Contraparte===========   " + contraparte);
                        String patron = rs.getString("route") != null ? rs.getString("route").trim() : "";
                        PreparedStatement pstLDI = con.prepareStatement("select * from carriers order by numero_carrier desc;");
                        ResultSet rsLDI = pstLDI.executeQuery();
                        List<String> carriers = new ArrayList<String>();

                        while (rsLDI.next()) {
                            String llave = rsLDI.getString("numero_carrier") != null ? rsLDI.getString("numero_carrier").trim() : "";
                            if (!llave.equals("") && !carriers.contains(llave)) {
//                            mapaLDI.put(llave, llave);
                                carriers.add(llave);
                            }
                        }
                        if (rsLDI != null) {
                            rsLDI.close();
                        }
                        if (pstLDI != null) {
                            pstLDI.close();
                        }
                        String carrierAux = Utilidades.esLargaDistanciaMario(patron, carriers);
                        if (!carrierAux.equals("")) {
                            System.out.println("Contraparte LDI Encontrada, buscando costo.....= " + contraparte);

                            String cadenaAux = contraparte.substring(((contraparte.indexOf(carrierAux) + carrierAux.length()) - 1), contraparte.length());
                            for (int x = cadenaAux.length(); x >= 0; x--) {
                                if (prefijosInternacional.containsKey(cadenaAux)) {
                                    Particion partiInter = (Particion) prefijosInternacional.get(cadenaAux);
                                    String tipoHorario = obtenerHorario(partiInter.getIdentificadorHorario(), registroOrigenLlamada, horarios);
                                    if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                                        costo = partiInter.getValorPrefijoNormal().floatValue();
                                    } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                                        costo = partiInter.getValorPrefijoEconomico().floatValue();
                                    } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                                        costo = partiInter.getValorPrefijoEconomico().floatValue();
                                    } else {
                                        costo = partiInter.getValorPrefijo().floatValue();
                                    }
                                    break;
                                } else {
                                    cadenaAux = cadenaAux.substring(0, (cadenaAux.length() - 1));
                                }
                            }
                        } else {
                            //Si no encentra el carrier
                            String tipoHorario = obtenerHorario(rs.getInt("identificador_horario"), registroOrigenLlamada, horarios);
                            if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                                costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                            } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                                costo = Float.valueOf(rs.getString("valor_economico") != null ? rs.getFloat("valor_economico") : 0.0F);
                            } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                                costo = Float.valueOf(rs.getString("valor_reducido") != null ? rs.getFloat("valor_reducido") : 0.0F);
                            } else {
                                costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                            }
                        }
                    }
                    tipoLlamada = "";
                }
                if (cont <= 1) {
                    tipoLlamada = rs.getString("tipo") != null ? rs.getString("tipo").trim() : "";
                    if (!tipoLlamada.equalsIgnoreCase("ldi")) {
                        String tipoHorario = obtenerHorario(rs.getInt("identificador_horario"), registroOrigenLlamada, horarios);
                        if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                            costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                        } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                            costo = Float.valueOf(rs.getString("valor_economico") != null ? rs.getFloat("valor_economico") : 0.0F);
                        } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                            costo = Float.valueOf(rs.getString("valor_reducido") != null ? rs.getFloat("valor_reducido") : 0.0F);
                        } else {
                            costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                        }
                    } else {
                        //Obteniendo costo LDI 9.1230[1-9]!   
                        System.out.println("Contraparte===========   " + contraparte);
                        String patron = rs.getString("route") != null ? rs.getString("route").trim() : "";
                        PreparedStatement pstLDI = con.prepareStatement("select * from carriers order by numero_carrier desc;");
                        ResultSet rsLDI = pstLDI.executeQuery();
                        List<String> carriers = new ArrayList<String>();

                        while (rsLDI.next()) {
                            String llave = rsLDI.getString("numero_carrier") != null ? rsLDI.getString("numero_carrier").trim() : "";
                            if (!llave.equals("") && !carriers.contains(llave)) {
//                            mapaLDI.put(llave, llave);
                                carriers.add(llave);
                            }
                        }
                        if (rsLDI != null) {
                            rsLDI.close();
                        }
                        if (pstLDI != null) {
                            pstLDI.close();
                        }
                        String carrierAux = Utilidades.esLargaDistanciaMario(patron, carriers);
                        if (!carrierAux.equals("")) {
                            System.out.println("Contraparte LDI Encontrada, buscando costo.....= " + contraparte);

                            String cadenaAux = contraparte.substring(((contraparte.indexOf(carrierAux) + carrierAux.length()) - 1), contraparte.length());
                            for (int x = cadenaAux.length(); x >= 0; x--) {
                                if (prefijosInternacional.containsKey(cadenaAux)) {
                                    Particion partiInter = (Particion) prefijosInternacional.get(cadenaAux);
                                    String tipoHorario = obtenerHorario(partiInter.getIdentificadorHorario(), registroOrigenLlamada, horarios);
                                    if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                                        costo = partiInter.getValorPrefijoNormal().floatValue();
                                    } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                                        costo = partiInter.getValorPrefijoEconomico().floatValue();
                                    } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                                        costo = partiInter.getValorPrefijoEconomico().floatValue();
                                    } else {
                                        costo = partiInter.getValorPrefijo().floatValue();
                                    }
                                    break;
                                } else {
                                    cadenaAux = cadenaAux.substring(0, (cadenaAux.length() - 1));
                                }
                            }
                        } else {
                            //Si no encentra el carrier
                            String tipoHorario = obtenerHorario(rs.getInt("identificador_horario"), registroOrigenLlamada, horarios);
                            if (tipoHorario.trim().equalsIgnoreCase("normal")) {
                                costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                            } else if (tipoHorario.trim().equalsIgnoreCase("economico")) {
                                costo = Float.valueOf(rs.getString("valor_economico") != null ? rs.getFloat("valor_economico") : 0.0F);
                            } else if (tipoHorario.trim().equalsIgnoreCase("reducido")) {
                                costo = Float.valueOf(rs.getString("valor_reducido") != null ? rs.getFloat("valor_reducido") : 0.0F);
                            } else {
                                costo = Float.valueOf(rs.getString("valor_normal") != null ? rs.getFloat("valor_normal") : 0.0F);
                            }
                        }
                    }
                    tipoLlamada = "";
                }
            }

        } catch (Exception e) {
            e.printStackTrace(System.out);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }

            } catch (Exception e) {
                e.printStackTrace(System.out);
            }
        }
        costo = costo * dur;
        return tratarCosto(costo);
    }
}
