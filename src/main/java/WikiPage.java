import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.FindBy;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class WikiPage {
    public final WebDriver driver;
    private final String homeUrl = "https://ru.wikipedia.org/";
    @FindBy(xpath = "//input[@id='searchInput']")
    public WebElement searchInput;


    private final By suggestPath =
            By.xpath("//a[@class='mw-searchSuggest-link']");
    private final By highlightPath = By.xpath("//span[@class='highlight']");

    public WikiPage(WebDriver driver) {
        this.driver = driver;
        this.driver.get(homeUrl);
    }

    public void goHome() {
        driver.navigate().to(homeUrl);
    }

    /**
     * Types the given query into the search input field, adding a space and backspace
     * to evoke all possible suggestions, including the special suggestion link.
     * Sleeps for 100 milliseconds between each key event to simulate human typing.
     *
     * @param  query  The query to be typed into the search input field
     */
    public void typeQuery(String query) {
        String[] keys = new String[]{query, " ", String.valueOf(Keys.BACK_SPACE)};
        for (String key : keys) {
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                System.out.println("Interrupted execution of the thread");
            }
            searchInput.sendKeys(key);
        }
    }

    /**
     * Retrieves a list of suggestions based on the given query.
     *
     * @param  query  the query string used to search for suggestions
     * @return        a list of WebElement objects representing the suggestions
     */
    public List<WebElement> getSuggestions(String query) {
        if (query.isEmpty()) {
            return new ArrayList<>();
        }
        typeQuery(query);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
            return wait.until(ExpectedConditions
                    .visibilityOfAllElementsLocatedBy(suggestPath));
        } catch (NoSuchElementException | TimeoutException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Retrieves a list of highlighted suggestions based on a given query.
     *
     * @param  query  the search query to retrieve highlights for
     * @return        an ArrayList of strings containing the highlights
     */
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

    /**
     * Clicks on the suggestion link if it exists and returns the URL.
     * Whether the first or the last suggestion is clicked is determined
     * by the specialSearch parameter. null is returned if no suggestions
     * are found at all.
     *
     * @param  query         the search query to retrieve suggestions for
     * @param  specialSearch identifies whether the query is a special search
     * @return               the URL of the clicked suggestion
     */
    public String getSuggestionLink(String query, boolean specialSearch) {
        List<WebElement> suggestions = getSuggestions(query);
        String url;
        if (!suggestions.isEmpty()) {
            if (!specialSearch) {
                suggestions.getFirst().click();
            } else {
                suggestions.getLast().click();
            }
            url = driver.getCurrentUrl();
        } else {
            url = null;
        }
        searchInput.clear();
        return url;
    }

    /**
     * Decodes a URL by using UTF-8 encoding and
     * replacing any underscores with spaces to be used later
     * for comparison with the original query.
     *
     * @param  url  the URL to be decoded
     * @return      the decoded URL
     */
    public String decodeUrl(String url) {
        if (url == null) {
            return null;
        }
        return URLDecoder.decode(url, StandardCharsets.UTF_8).replace('_', ' ');
    }

    /**
     * Finds the last part of the URL and decodes it.
     *
     * @param   url  the URL the last part of which should be decoded
     * @return       the decoded last part of the URL
     */
    public String decodeLastPartOfUrl(String url) {
        String lastPartOfUrl = url.substring(url.lastIndexOf("/") + 1);
        return decodeUrl(lastPartOfUrl);
    }
}