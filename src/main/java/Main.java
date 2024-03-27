import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;


public class Main {
    public static void main(String[] args) {
        WebDriver driver = new EdgeDriver();
        System.out.format("Hello and welcome!");


        driver.get("https://ru.wikipedia.org/");
    }

}
