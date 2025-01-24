package fer.progi.backend.selenium;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class AdminFailFetchStudentMethod {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://eduhub-rfsg.onrender.com");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testAdminFetchPupilCertificate() throws InterruptedException {
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(1000);

        WebElement username = driver.findElement(By.name("loginfmt"));
        username.sendKeys("josip.jelavic@eduxhub.onmicrosoft.com");

        driver.findElement(By.id("idSIButton9")).click();
        Thread.sleep(1000);

        WebElement password = driver.findElement(By.name("passwd"));
        password.sendKeys("Lozinka2");

        driver.findElement(By.id("idSIButton9")).click();
        Thread.sleep(1000);

        //driver.findElement(By.id("btnAskLater")).click();
        //Thread.sleep(2000);

        driver.findElement(By.id("idBtn_Back")).click();
        Thread.sleep(2000);

        String url = "https://eduhub-rfsg.onrender.com/ucenik";
        driver.get(url);

        Thread.sleep(2000);

        List<WebElement> elements = driver.findElements(By.cssSelector("li.sidebar-item"));

        for (WebElement item : elements) {
            WebElement span = item.findElement(By.tagName("span"));
            if (span.getText().equals("Potvrde")) {
                item.click();
                break;
            }
        }
        Thread.sleep(2000);

        List<WebElement> buttons = driver.findElements(By.tagName("button"));

        for (WebElement button : buttons) {
            WebElement span = button.findElement(By.tagName("span"));
            if (span.getText().equals("Preuzmi potvrdu")) {
                button.click();
                break;
            }
        }
        Thread.sleep(2000);

        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();

            String expected = "Došlo je do greške prilikom generiranja potvrde.";

            Assert.assertEquals(alertText, expected);
        } catch (Exception e) {
            e.getMessage();
        }

    }

}
