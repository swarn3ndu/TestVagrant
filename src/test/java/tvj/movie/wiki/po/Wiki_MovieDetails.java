package tvj.movie.wiki.po;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Wait;

import java.time.Duration;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;



public class Wiki_MovieDetails {
    WebDriver driver;
    String movieName;
    static Dictionary wikiMovieDetails =  new Hashtable();

    public Wiki_MovieDetails(WebDriver driver, String movieName){
        this.driver = driver;
        PageFactory.initElements(driver, this);
        this.movieName = movieName;
    }
    @FindBy(xpath = "//*[@id='p-search']/a")
    public WebElement looking_glass;

    @FindBy(id = "searchInput")
    public WebElement search_box;

    @FindBy(xpath = "//*[@id='searchform']/div/button")
    public WebElement search_button;

    @FindBy(xpath = "//div[contains(text(),'Release date')]/parent::th/following-sibling::td//li")
    public WebElement release_date;

    @FindBy(xpath = "//th[contains(text(),'Country')]/following-sibling::td")
    public WebElement country;

    public Dictionary fetchFromWiki() throws InterruptedException {
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.get("https://en.wikipedia.org/");
        driver.manage().window().maximize();

        search_box.click();
        search_box.sendKeys(movieName);
        search_button.click();

        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", release_date);
        Thread.sleep(500);

        wikiMovieDetails.put("ReleaseDate",release_date.getText().replace("&nbsp;"," "));
        wikiMovieDetails.put("Origin",country.getText());

        return wikiMovieDetails;
    }
}
