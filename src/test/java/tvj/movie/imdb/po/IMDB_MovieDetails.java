package tvj.movie.imdb.po;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class IMDB_MovieDetails {

    WebDriver driver;
    String movieName;
    static Dictionary imdbMovieDetails =  new Hashtable();

    public IMDB_MovieDetails(WebDriver driver, String movieName){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.movieName = movieName;
    }

    @FindBy(xpath = "//*[@id=\"suggestion-search\"]" )
    public WebElement search_box;

    @FindBy(id = "suggestion-search-button")
    public WebElement searchButton;

    @FindBy(how=How.XPATH, using = "//div[contains(text(),'Exact')]/parent::a")
    public WebElement exactMatches;

    @FindBy(xpath = "//span[contains(text(),'Details')]")
    public WebElement details;

    @FindBy(xpath = "//*[contains(text(),'Release date')]/parent::li//li/a")
    public WebElement releaseDate;

    @FindBy(xpath = "//*[contains(text(),'Country of origin')]/parent::li//li/a")
    public WebElement countryOfOrigin;


    public Dictionary fetchFromIMDB() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("https://www.imdb.com/");
        Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofSeconds(1))
                .ignoring(NoSuchElementException.class);

        wait.until(ExpectedConditions.elementToBeClickable(search_box));
        search_box.click();
        search_box.sendKeys(movieName);
        searchButton.click();

        wait.until(ExpectedConditions.
                elementToBeClickable(exactMatches));
        exactMatches.click();

        WebElement section =  driver.findElement(By.xpath("//section[@data-testid='find-results-section-title']"));
        List<WebElement> elements = section.findElements(By.xpath("//ul[@role='presentation']//a"));
        for(WebElement link : elements){
            if (link.getText().equalsIgnoreCase(movieName)){
                link.click();
                break;
            }
        }


       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", details);
       Thread.sleep(500);

        imdbMovieDetails.put("ReleaseDate",releaseDate.getText().split("(?<=\\w{1,9} \\d{1,2}, \\d{4})\\s")[0]);
        imdbMovieDetails.put("Origin",countryOfOrigin.getText());

        return imdbMovieDetails;
    }
}
