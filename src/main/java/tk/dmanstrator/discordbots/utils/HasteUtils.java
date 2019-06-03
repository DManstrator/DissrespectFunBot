package tk.dmanstrator.discordbots.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.json.JSONObject;
import org.json.JSONTokener;

public class HasteUtils {
    
    private HasteUtils()  {} // prohibit instancing
    
    private final static List<String> MAIN_URLS = Arrays.asList("hasteb.in", "hastebin.com");
    private static final String API_CALL = "documents";
    
    public static String paste(String data) throws IOException {
        String finalUrl = null;
        for (String mainUrl : MAIN_URLS)  {
            try  {
                String url = String.format("https://%s/%s", mainUrl, API_CALL);
                HttpsURLConnection connection;
                URL apiUrl = new URL(url);
                connection = (HttpsURLConnection) apiUrl.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("User-Agent", "Mozilla/5.0");
                
                connection.setDoOutput(true);
                connection.setReadTimeout(5*1000);  // In case Hastebin is down
                
                final DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.write(data.getBytes("UTF-8"));
                wr.flush();
                wr.close();
        
                finalUrl = String.format("https://%s/%s", mainUrl, new JSONObject(new JSONTokener(connection.getInputStream())).getString("key"));
                break;
            } catch (SocketTimeoutException ex)  {
                continue;  // try next url
            }
        }
        if (finalUrl == null)  {
            throw new IOException("Could not send data to any website!");
        }
        return finalUrl;
    }
}