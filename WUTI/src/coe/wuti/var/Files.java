package coe.wuti.var;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Creado por Jesus el 13/6/15.
 */
public class Files {
    public static List<String> getLines(File file) throws Exception {

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String linea;
            while ((linea = br.readLine()) != null) lines.add(linea);
        }

        return lines;
    }
}
