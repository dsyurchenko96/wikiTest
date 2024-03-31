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

    public static WebDriver driver;
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


    /**
     * Tests the highlighting of search results
     * for the given valid queries that have highlights.
     */
    @Test
    public void testValidHighlights() {
        runTestHighlights(new String[]{"пушкин", "ТОЛСТОЙ", "ЧеХоВ", "Лермонтов"}, true);
    }

    /**
     * Tests not having any highlights with the given invalid queries
     * that don't have any suggestions at all.
     */
    @Test
    public void testNoHighlightsWithoutSuggestions() {
        runTestHighlights(new String[]{"", "[", "}", "){(}", "<"}, false);
    }

    /**
     * Tests not having any highlights with the given invalid queries
     * that have at least one suggestion.
     */
    @Test
    public void testNoHighlightsWithSuggestions() {
        runTestHighlights(new String[]{"fgd", "koxwe", "ащм"}, false);
    }

    /**
     * Tests the highlighting of search results for the given valid queries
     * with Latin characters and special symbols.
     */
    @Test
    public void testLatinAndSymbolHighlights() {
        runTestHighlights(new String[]{"///", ".NET", "/etc/passwd", "VK Музыка"}, true);
    }

    /**
     * Tests the exact match of the query and the last part of the URL
     * going to the first highlighted suggestion.
     */
    @Test
    public void testGoToFirstExactSuggestion() {
        runTestSuggestions(new String[]{
                "Пушкин, Александр Сергеевич",
                "Толстой, Лев Николаевич",
                "Чехов, Антон Павлович",
                "Лермонтов, Михаил Юрьевич"
        }, false);
    }

    /**
     * Tests going to the special search page for the given queries
     * that don't have any suggestions.
     */
    @Test
    public void testInvalidGoToSpecialSearch() {
        runTestSuggestions(new String[]{"Иванннннн", "gjfodogji", "0489571098346", "[];,-)(*&^%$@!@#"}, true);
    }

    /**
     * Tests going to the special search page for the given queries
     * that have at least one suggestion and can be disambiguated.
     */
    @Test
    public void testValidGoToSpecialSearch() {
        runTestSuggestions(new String[]{"VK", "Одноклассники", "Толстой"}, true);
    }

    /**
     * Runs a test for the highlights of a given set of queries.
     *
     * @param  queries  an array of queries to test the highlights
     * @param  valid    a boolean indicating whether the highlights should be valid or empty
     */
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

    /**
     * Runs a test for suggestions based on the given queries.
     *
     * @param  queries       an array of queries to search for suggestions
     * @param  specialSearch a boolean indicating whether to use special search or not
     */
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