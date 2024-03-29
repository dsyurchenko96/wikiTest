import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.support.PageFactory;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class WikiTest {

    private static WebDriver driver;

    @BeforeAll
    public static void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @AfterAll
    public static void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }


    @Test
    public void testValidHighlights() {
        runTestHighlights(new String[]{"пушкин", "ТОЛСТОЙ", "ЧеХоВ", "Лермонтов"}, true);
    }

    @Test
    public void testEmptyHighlights() {
        runTestHighlights(new String[]{"", "[", "}", "){(}", "<"}, false);
    }

    @Test
    public void testLatinAndSymbolHighlights() {
        runTestHighlights(new String[]{"///", ".NET", "/etc/passwd", "VK Музыка"}, true);
    }

    @Test
    public void testWebElementList() {
        WikiPage homePage = PageFactory.initElements(driver, WikiPage.class);
        List<WebElement> suggestions = homePage.getSuggestions("");
        assertTrue(suggestions.isEmpty());

        suggestions = homePage.getSuggestions("о");
        for (WebElement suggestion : suggestions) {
            System.out.println(suggestion.getText());
        }
        ArrayList<String> highlights = homePage.getHighlights("о");
        for (String highlight : highlights) {
            System.out.println(highlight);
        }
    }

    private void runTestHighlights(String[] queries, boolean valid) {
        WikiPage homePage = PageFactory.initElements(driver, WikiPage.class);
        for (String query : queries) {
            List<String> highlights = homePage.getHighlights(query);
            if (valid) {
                for (String highlight : highlights) {
                    assertEquals(highlight.toLowerCase(), query.toLowerCase());
                }
            } else {
                assertTrue(highlights.isEmpty());
            }
        }
    }
}