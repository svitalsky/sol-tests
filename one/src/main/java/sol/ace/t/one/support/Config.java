package sol.ace.t.one.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public enum Config {
    CONFIG;

    private final Properties properties = new Properties();

    Config() {
        try (InputStream stream = Thread.currentThread()
                                        .getContextClassLoader()
                                        .getResourceAsStream("solace.properties"))
        {
            properties.load(stream);
        }
        catch (IOException e) {
            throw new IllegalStateException("Failed to load config!", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public int getIntProperty(String key) {
        try {
            return Integer.parseInt(properties.getProperty(key));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Not an integer property: '%s'", key));
        }
    }
}
