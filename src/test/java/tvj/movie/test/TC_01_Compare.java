package tvj.movie.test;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.*;
import tvj.movie.imdb.po.IMDB_MovieDetails;
import tvj.movie.wiki.po.Wiki_MovieDetails;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Dictionary;
import java.util.concurrent.TimeUnit;

public class TC_01_Compare {
    WebDriver driver;
    Dictionary imdb_dict ;
    Dictionary wiki_dict;

    @BeforeClass
    public void setup(){
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080");
        options.addArguments("--start-maximized");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--allow-insecure-localhost");
        //options.addArguments("--headless");


        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test
    @Parameters("movieName")
    public void fetchFromIMDB(String movieName){

        try {
            IMDB_MovieDetails imdb = new IMDB_MovieDetails(driver, movieName);

            Dictionary imdb_dict = imdb.fetchFromIMDB();

            DateTimeFormatter imdb_format = DateTimeFormatter.ofPattern("MMMM d, yyyy");

            imdb_dict.put("ReleaseDate", LocalDate.parse((CharSequence) imdb_dict.get("ReleaseDate"),imdb_format));


        } catch (InterruptedException e) {

            e.printStackTrace();
        }

    }

    @Test
    @Parameters("movieName")
    public void fetchFromWiki(String movieName){

        try {
            Wiki_MovieDetails wiki = new Wiki_MovieDetails(driver, movieName);

            Dictionary wiki_dict = wiki.fetchFromWiki();

            DateTimeFormatter wiki_format = DateTimeFormatter.ofPattern("d MMMM yyyy");

            wiki_dict.put("ReleaseDate", LocalDate.parse((CharSequence) wiki_dict.get("ReleaseDate"),wiki_format));

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void compareData(){
        Assert.assertEquals(imdb_dict,wiki_dict,"The values from both Sites do not match.");
    }

    @AfterClass
    public void teardown(){
        driver.close();
        driver.quit();
    }
}
