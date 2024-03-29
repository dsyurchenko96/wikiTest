import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class WikiPage {
    private final WebDriver driver;
    private final String homeUrl = "https://ru.wikipedia.org/";
    @FindBy(xpath = "//input[@id='searchInput']")
    public WebElement searchInput;
    @FindBy(xpath = "//input[@id='searchButton']")
    public WebElement searchButton;


    private final By suggestPath =
            By.xpath("//a[@class='mw-searchSuggest-link']");
    private final By highlightPath = By.xpath("//span[@class='highlight']");
    private final By specialSuggestPath = By.xpath("/../following-sibling::a");

    public WikiPage(WebDriver driver) {
        this.driver = driver;
        this.driver.get(homeUrl);
    }

    public void goHome() {
        driver.navigate().to(homeUrl);
    }

    public List<WebElement> getSuggestions(String query) {
        searchInput.sendKeys(query);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
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
                try {
                    String highlight = suggestion.findElement(highlightPath).getText();
                    highlights.add(highlight);
                } catch (NoSuchElementException e) {
                    break;
                }
            }
        }
        searchInput.clear();
        return highlights;
    }

    public String getSuggestionLink(String query, boolean specialSearch) {
        List<WebElement> suggestions = getSuggestions(query);
        if (!suggestions.isEmpty()) {
            if (!specialSearch) {
                suggestions.getFirst().click();
            } else {
                suggestions.getLast().click();
            }
        } else {
            System.out.println("NOTHING FOUND!!!!");
        }
        String url = driver.getCurrentUrl();
        searchInput.clear();
        return url;
    }

    public String decodeUrl(String url) {
        return URLDecoder.decode(url, StandardCharsets.UTF_8).replace('_', ' ');
    }

    public String decodeLastPartOfUrl(String url) {
        String lastPartOfUrl = url.substring(url.lastIndexOf("/") + 1);
        return decodeUrl(lastPartOfUrl);
    }
}