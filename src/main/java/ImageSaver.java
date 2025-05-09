import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageSaver {


    public static void saveImage(String imgSrc, String fileName) {
        try {
            URL url = new URL(imgSrc);
            URLConnection conn = url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 Chrome/122.0.0.0 Safari/537.36");

            try (InputStream in = conn.getInputStream()) {
                Files.copy(in, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
                System.out.println("Saved image to " + fileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
