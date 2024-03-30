import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.PageFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class WikiTest {

    private static WebDriver driver;
    public static WikiPage homePage;

    @BeforeAll
    public static void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        homePage = PageFactory.initElements(driver, WikiPage.class);
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
    public void testInvalidHighlights() {
        runTestHighlights(new String[]{"", "[", "}", "){(}", "<"}, false);
    }

    @Test
    public void testNoHighlights() {
        runTestHighlights(new String[]{"fgd", "koxwe", "ащм"}, false);
    }

    @Test
    public void testLatinAndSymbolHighlights() {
        runTestHighlights(new String[]{"///", ".NET", "/etc/passwd", "VK Музыка"}, true);
    }

    @Test
    public void testGoToFirstExactSuggestion() {
        runTestSuggestions(new String[]{
                "Пушкин, Александр Сергеевич",
                "Толстой, Лев Николаевич",
                "Чехов, Антон Павлович",
                "Лермонтов, Михаил Юрьевич"
        }, false);
    }


    @Test
    public void testInvalidGoToSpecialSearch() {
        runTestSuggestions(new String[]{"Иванннннн", "gjfodogji", "0489571098346", "[];,-)(*&^%$@!@#"}, true);
    }

    @Test
    public void testValidGoToSpecialSearch() {
        runTestSuggestions(new String[]{"VK", "Одноклассники", "Толстой"}, true);
    }

    private void runTestHighlights(String[] queries, boolean valid) {
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

    private void runTestSuggestions(String[] queries, boolean specialSearch) {
        String searchPageStart = "https://ru.wikipedia.org/w/index.php?fulltext=1&search=";
        String searchPageEnd = "&title=Служебная:Поиск&ns0=1";
        for (String query: queries) {
            String url = homePage.getSuggestionLink(query, specialSearch);
            if (specialSearch) {
                assertEquals(searchPageStart + query + searchPageEnd, homePage.decodeUrl(url));
            } else {
                assertEquals(query, homePage.decodeLastPartOfUrl(url));
            }
        }
        homePage.goHome();
    }
}