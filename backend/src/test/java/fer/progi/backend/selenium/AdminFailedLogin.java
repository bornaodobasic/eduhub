package fer.progi.backend.selenium;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class AdminFailedLogin {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://eduhub-rfsg.onrender.com");
    }

    @AfterClass
    public void tearDown() {driver.quit();}

    @Test
    public void adminFailedLogin() throws InterruptedException {
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(2000);

        WebElement username = driver.findElement(By.name("loginfmt"));
        username.sendKeys("josip.jelavic@eduxhub.onmicrosoft.com");

        driver.findElement(By.id("idSIButton9")).click();
        Thread.sleep(2000);

        WebElement password = driver.findElement(By.name("passwd"));
        password.sendKeys("krivaLozinka");

        driver.findElement(By.id("idSIButton9")).click();
        Thread.sleep(2000);

        Boolean actual = driver.findElement(By.id("passwordError")).isDisplayed();
        Boolean expected = true;

        Assert.assertEquals(actual, expected);
    }


}
