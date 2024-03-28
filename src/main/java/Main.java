import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;


public class Main {
    public static void main(String[] args) {
        WebDriver driver = new EdgeDriver();

        // accessing the url
        driver.get("https://ru.wikipedia.org/");
        System.out.println(driver.getCurrentUrl());
        WebElement searchInput = driver.findElement(By.xpath("//input[@id='searchInput']"));
        WebElement searchButton = driver.findElement(By.xpath("//input[@id='searchButton']"));

        String[] queries = new String[]{"Ленин", "Сталин", "Кузнецов", "Лермонтов"};
        for (String query : queries) {
            StringBuilder by_symbol = new StringBuilder();
            for (int i = 0; i < query.length(); i++) {
                by_symbol.append(query.charAt(i));
                findSuggestions(driver, searchInput, by_symbol);
            }
//            findSuggestions(driver, searchInput, query);
        }

        // going to the URL
//        searchInput.submit();
//        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }

    private static void findSuggestions(WebDriver driver, WebElement searchInput, StringBuilder query) {
//        String xpath = "//div[@class='suggestions-results']//a[@class='mw-searchSuggest-link']";
        searchInput.sendKeys(query);
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            WebElement suggestions = wait.until(ExpectedConditions.elementToBeClickable(By
                            .xpath("//div[@class='suggestions']")));
            for (WebElement suggestion : suggestions.findElements(By.className("mw-searchSuggest-link"))) {
                System.out.println(suggestion.getText());
            }
        } catch (Exception RuntimeException) {
            System.out.println("No suggestions found for '" + query + "'");
        }
        searchInput.clear();
    }
}

