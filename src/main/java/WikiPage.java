import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


public class WikiPage {
    private final WebDriver driver;
    @FindBy(xpath = "//input[@id='searchInput']")
    public WebElement searchInput;
    @FindBy(xpath = "//input[@id='searchButton']")
    public WebElement searchButton;

    private final By suggestPath = By.xpath("//div[@class='suggestions-results']//a[@class='mw-searchSuggest-link']");
    private final By highlightPath = By.xpath("//span[@class='highlight']");

    public WikiPage(WebDriver driver) {
        this.driver = driver;
        this.driver.get("https://ru.wikipedia.org/");
    }

    public List<WebElement> getSuggestions(String query) {
        searchInput.sendKeys(query);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1));
            return wait.until(ExpectedConditions
                    .visibilityOfAllElementsLocatedBy(suggestPath));
        } catch (NoSuchElementException | TimeoutException e) {
            return new ArrayList<WebElement>();
        }
    }

    public ArrayList<String> getHighlights(String query) {
        ArrayList<String> highlights = new ArrayList<>();
        List<WebElement> suggestions = getSuggestions(query);
        if (!suggestions.isEmpty()) {
            for (WebElement suggestion : suggestions) {
                String highlight = suggestion.findElement(highlightPath).getText();
                if (!highlight.isEmpty()) {
                    highlights.add(highlight);
                }
            }
        }
        searchInput.clear();
        return highlights;
    }
}