package pe.edu.unasam.activos.common.util;

import java.security.SecureRandom;
import java.util.UUID;

public class CodigoGenerator {
    
    private static final SecureRandom randon = new SecureRandom();

    public static final SecureRandom getRandon() {
        return randon;
    }
    
    public static String generarCodigoActivo(String prefijo, int anio) {
        String uuidPart = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        return String.format("%s-%d-%s", prefijo, anio, uuidPart);
    }
}
