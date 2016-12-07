package coe.wuti.hash;

import java.security.MessageDigest;

/**
 * Creado por Jesus el 25/6/15.
 */
public class BasicCypher {
    public static String encripta(String claveEnClaro) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        return byteArrayToHexString(md.digest(claveEnClaro.getBytes()));
    }

    private static String byteArrayToHexString(byte[] bytes) {
        String resultado = "";
        if (bytes != null)
            for (byte b : bytes) resultado += Integer.toString((b & 0xff) + 0x100, 16).substring(1);
        return resultado;
    }

    public static void main(String[] args) throws Exception{
        String claveEnClaro = "cartagon" ;
        String res = encripta(claveEnClaro) ;
        System.out.println("res = " + res);
    }
}
