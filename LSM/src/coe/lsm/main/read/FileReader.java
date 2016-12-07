package coe.lsm.main.read;

import coe.lsm.main.Configuration;
import coe.lsm.main.store.Store;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

/**
 * Created by jesus on 2/19/16.
 * <p>
 * Reads contents from an input content file
 */
public class FileReader {

    public interface Listener {
        /**
         * Gives the reading as they are read
         *
         * @param reading the reading
         */
        void newLine(Reading reading);

        /**
         * Some error happened while reading some line
         *
         * @param msg the cause of the error
         */
        void error(String msg);

        /**
         * The reading will stop (probably due to an interrupted exception)
         *
         * @param msg cause of ending with the reading process
         */

        void end(String msg);

        /**
         * The Reader is waiting for more data
         */
        void idle(int seconds);
    }

    private BufferedReader bufferedReader;

    public FileReader(Configuration configuration) throws Exception {
        File dataSource = configuration.getDataSource() ;

        try {
            bufferedReader = new BufferedReader(new java.io.FileReader(dataSource));
        } catch (FileNotFoundException e) {
            throw new Exception("File not found: " + dataSource.getAbsolutePath());
        }
    }

    public void readContinuous(final Listener listener, final int readEverySeconds) {
        new Thread("File reading thread") {
            @Override
            public void run() {
                String rawLine = null;

                while (true) {

                    // Read new raw line
                    try {
                        rawLine = bufferedReader.readLine();
                    } catch (IOException e) {
                        listener.error("Could not read line: " + e.getMessage());
                    }

                    // Wait until there are more lines to read
                    if (rawLine == null)
                        try {
                            new Thread(() -> listener.idle(readEverySeconds)).start();
                            Thread.sleep(readEverySeconds * 1000);
                        } catch (InterruptedException e) {
                            listener.end("Reading must be interrupted: " + e.getMessage());
                            Thread.currentThread().interrupt();
                            break;
                        }

                        // From raw to reading
                    else {
                        Reading reading = Reading.getFromRawData(rawLine);
                        if (reading != null)
                            listener.newLine(reading);
                    }
                }
            }
        }.start();
    }

    public static void main(String[] args) throws Exception {

        Configuration configuration = new Configuration() ;
        Store store = new Store(configuration) ;

        Listener listener = new Listener() {
            @Override
            public void newLine(Reading reading) {
                store.addReading(reading);
            }

            @Override
            public void error(String msg) {
                System.out.println("Error : " + msg);
            }

            @Override
            public void end(String msg) {
                System.out.println("Ending readings ... :" + msg);
            }

            @Override
            public void idle(int seconds) {
            }
        };

        System.out.println(new Date());
        new FileReader(configuration).readContinuous(listener, configuration.getReadEverySeconds());
        System.out.println(new Date());
    }
}
