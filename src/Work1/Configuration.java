package Work1;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {
    public static String loadProperty(String pName) {
        String ans = null;
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = ClassLoader.getSystemResourceAsStream("config.properties");///////////////

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            ans = prop.getProperty(pName);

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return ans;
    }
}
