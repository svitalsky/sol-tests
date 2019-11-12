package sol.ace.t.one.support;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
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

    public long getLongProperty(String key) {
        try {
            return Long.parseLong(properties.getProperty(key));
        }
        catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("Not a long property: '%s'", key));
        }
    }

    public boolean getBoolProperty(String key) {
        return Optional.ofNullable(properties.getProperty(key))
                .map(s -> {
                    boolean res;
                    switch (s.trim().toLowerCase()) {
                        case "0":
                        case "n":
                        case "no":
                        case "f":
                        case "false": res = false; break;
                        case "1":
                        case "y":
                        case "yes":
                        case "t":
                        case "true": res = true; break;
                        default: throw new IllegalArgumentException(String.format("Not a boolean property: '%s'", key));
                    }
                    return res;

                })
                .orElseThrow(() -> new IllegalArgumentException(String.format("Not a boolean property: '%s'", key)));
    }
}
