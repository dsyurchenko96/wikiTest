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

        searchInput.sendKeys("hi");
        try {
            WebElement suggestions = (new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(By
                            .xpath("//div[@class='suggestions']"))));
            for (WebElement suggestion : suggestions.findElements(By.className("mw-searchSuggest-link"))) {
                System.out.println(suggestion.getText());
            }
        } catch (Exception RuntimeException) {
            System.out.println("No suggestions found");
        }

        // going to the URL
//        searchInput.submit();
//        System.out.println(driver.getCurrentUrl());
        driver.quit();
    }

}
