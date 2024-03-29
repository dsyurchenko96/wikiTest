import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.codeborne.selenide.Selenide;
//import com.codeborne.selenide.WebDriverRunner;

import java.time.Duration;
import java.util.List;


public class Main {
    public static void main(String[] args) {

        WebDriver driver;
        try {
            WebDriverManager.chromedriver().setup();
            driver = new ChromeDriver();
        } catch (Exception e) {
            WebDriverManager.edgedriver().setup();
            driver = new EdgeDriver();
        }


        JavascriptExecutor js = (JavascriptExecutor) driver;

        // accessing the url
        driver.get("https://ru.wikipedia.org/");
        System.out.println(driver.getCurrentUrl());
        WebElement searchInput = driver.findElement(By.xpath("//input[@id='searchInput']"));
        WebElement searchButton = driver.findElement(By.xpath("//input[@id='searchButton']"));

        String[] queries = new String[]{"Кузнецов", "Ленин", "Сталин", "Лермонтов"};
        for (String query : queries) {
            js.executeScript("window.focus()");
//            StringBuilder by_symbol = new StringBuilder();
//            for (int i = 0; i < query.length(); i++) {
//                by_symbol.append(query.charAt(i));
//                findSuggestions(driver, searchInput, by_symbol.toString());
//            }
            List<WebElement> suggestions = findSuggestions(driver, searchInput, query);

//            if (checkSuggestions(suggestions, query) == 1) {
//                System.out.println("MISMATCH HERE WITH QUERY: " + query);
//            }
            printSuggestions(suggestions);
        }

        // going to the URL
//        searchInput.submit();
//        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }

    private static List<WebElement> findSuggestions(WebDriver driver, WebElement searchInput, String query) {
        List<WebElement> suggestions;
        String xpath = "//div[@class='suggestions-results']//a[@class='mw-searchSuggest-link']";
        searchInput.sendKeys(query);
//        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
            suggestions = wait.until(ExpectedConditions.
                    visibilityOfAllElementsLocatedBy(By.xpath(xpath)));
//            for (WebElement suggestion : suggestions) {
//                System.out.println(suggestion.getText());
//                System.out.println(suggestion.findElement(By.tagName("span")).getText());
//
//            }
//        } catch (RuntimeException re) {
//            suggestions = null;
//            System.out.println("No suggestions found for '" + query + "'");
//        }
        searchInput.clear();
        return suggestions;
    }

    private static void printSuggestions(List<WebElement> suggestions) {
        System.out.println(suggestions.size());
        for (WebElement suggestion : suggestions) {
            System.out.println(suggestion.getText());
            System.out.println(suggestion.findElement(By.tagName("span")).getText());

        }
    }

    private static int checkSuggestions(List<WebElement> suggestions, String query) {
        for (WebElement suggestion : suggestions) {
            System.out.println(suggestion.getText());
            System.out.println(suggestion.findElement(By.tagName("span")).getText());
            String highlight = suggestion.findElement(By.tagName("span")).getText();
            if (!highlight.equals(query)) {
                return 1;
            }
        }
        return 0;
    }
}
