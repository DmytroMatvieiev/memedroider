import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.System.in;

public class MemedroiderApplication {
    public static void main(String[] args) {
        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver();

        int curr = 0;
        Map<String, String> map = new HashMap<>();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        File saveDir = new File("F:\\memes");
        if (!saveDir.exists()) saveDir.mkdirs();

        try {
            driver.get("https://www.memedroid.com/memes/random");

            //Thread.currentThread().sleep(5000);
            while (true) {

                List<WebElement> articles = driver.findElements(By.cssSelector("article"));

                for (; curr < articles.size(); curr++) {

                    // Get image inside article
                    WebElement imgElement = null;
                    try {
                        imgElement = articles.get(curr).findElement(By.cssSelector("picture img"));
                    } catch (NoSuchElementException e) {
                        continue;
                    }

                    String imgSrc = imgElement.getAttribute("src");
                    System.out.println(curr);
                    // Get rating percentage (green-1 or white-gray)
                    String percentage = "";
                    try {
                        percentage = articles.get(curr).findElement(By.cssSelector(".item-rating-container span.green-1")).getText();
                    } catch (NoSuchElementException e) {
                        try {
                            percentage = articles.get(curr).findElement(By.cssSelector(".item-rating-container span.white-grey")).getText();
                        } catch (NoSuchElementException e2) {
                            continue;
                        }
                    }
                    // Get vote count
                    String votes = articles.get(curr).findElement(By.cssSelector(".item-rating-container .item-rating-vote-count")).getText();


                    String rawFilename = curr + " " + percentage.replaceAll("[^\\d]", "") + "-" + votes;
                    String filename = rawFilename.replaceAll("[\\\\/:*?\"<>|]", "").trim();
                    File outputFile = new File(saveDir, filename + ".jpeg");

                    System.out.println("Image: " + imgSrc + " - " + filename);
                    map.put(imgSrc, saveDir + "/" + filename + ".jpeg");
                    //ImageSaver.saveImage(imgSrc,saveDir + "/" + filename + ".jpeg");

                }
                js.executeScript("arguments[0].scrollIntoView({behavior: 'smooth', block: 'center'});", articles.get(curr - 1));
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    String imgSrc = entry.getKey();
                    String fileOutput = entry.getValue();
                    ImageSaver.saveImage(imgSrc, fileOutput);
                }
                map.clear();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
        } finally {
            System.out.println("Closing WebDriver");
            driver.quit();
        }
    }


    private static void downloadImage(String imageUrl, String destinationFile) throws IOException {
        try (InputStream in = new URL(imageUrl).openStream()) {
            Files.copy(in, Paths.get(destinationFile));
            System.out.println("Image saved as: " + destinationFile);
        }
    }
}
