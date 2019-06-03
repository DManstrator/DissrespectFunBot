package tk.dmanstrator.discordbots.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.json.JSONObject;

public class Configuration {
    
    private final String token;
    
    public Configuration(String pathToConfig) {
        InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream(pathToConfig);
        
        if (resourceStream == null) {
            System.err.println("Couldn't get Configuration");
            token = null;
            return;
        }
        
        BufferedReader resReader = new BufferedReader(new InputStreamReader(resourceStream));
        String json = resReader.lines().collect(Collectors.joining());
        
        JSONObject jsonObject = new JSONObject(json);
        this.token = jsonObject.getString("token");
    }

    public String getToken() {
        return token;
    }
    
}