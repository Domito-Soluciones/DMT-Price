package cl.domito.dominio;

import java.io.File;
import java.sql.Timestamp;

public class CDR {
        
    public final static int GLOBAL_CALL_ID = 2;
    public final static int ORIG_LEG_CALL_IDENTIFIER = 3;
    public final static int DEST_LEG_CALL_IDENTIFIER = 25;
    public final static int DATE_TIME_ORIGINATION = 4;
    public final static int ORIG_NODE_ID = 5;
    public final static int ORIG_SPAN = 6;
    public final static int ORIG_IP_ADDR = 7;
    public final static int CALLING_PARTY_NUMBER = 8;
    public final static int DEST_NODE_ID = 26;
    public final static int DEST_SPAN = 27;
    public final static int DEST_IP_ADDR = 28;
    public final static int ORIGINAL_CALLED_PARTY_NUMBER = 29;
    public final static int FINAL_CALLED_PARTY_NUMBER = 30;
    public final static int DATE_TIME_CONNECT = 47;
    public final static int DATE_TIME_DISCONNECT = 48;
    public final static int LAST_REDIRECT_DN = 49;
    public final static int PKID = 50;
    public final static int ORIGINAL_CALLED_PARTY_NUMBER_PARTITION = 51;
    public final static int CALLING_PARTY_NUMBER_PARTITION = 52;
    public final static int FINAL_CALLED_PARTY_NUMBER_PARTITION  = 53;
    public final static int LAST_REDIRECT_DN_PARTITION = 54;
    public final static int DURATION = 55;
    public final static int ORIG_DEVICE_NAME = 56;
    public final static int DEST_DEVICE_NAME = 57;
    public final static int DEST_CONVERSDATION_ID = 64;
    public final static int JOIN_ON_BEHALF_OF = 66;
    public final static int COMMENT = 67;
    public final static int AUTH_CODE_DESCRIPTION = 68;
    public final static int AUTHORIZATION_LEVEL = 69;
    public final static int AUTHORIZATION_CODE_VALUE = 77;    
    
    public static File CARPETA_CDR = new File("CDR/");
    public static File CARPETA_RESPALDO = new File("RESPALDO/");
    public static boolean RESPALDAR = true;
    
}
