package fer.progi.backend;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SeleniumAdminLogin {

    WebDriver driver;

    @BeforeClass
    public void setup() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("http://localhost:8080");
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test
    public void testAdminLogin() throws InterruptedException {
        driver.findElement(By.tagName("button")).click();
        Thread.sleep(2000);

        WebElement username = driver.findElement(By.name("loginfmt"));
        username.sendKeys("testadmin@eduxhub.onmicrosoft.com");

        driver.findElement(By.id("idSIButton9")).click();
        Thread.sleep(2000);

        WebElement password = driver.findElement(By.name("passwd"));
        password.sendKeys("Lozinka2");

        driver.findElement(By.id("idSIButton9")).click();
        Thread.sleep(2000);

        driver.findElement(By.id("idBtn_Back")).click();
        Thread.sleep(2000);

        String actual = driver.findElement(By.tagName("h3")).getText();
        String expected = "Popis svih admina";
        Assert.assertEquals(actual, expected);

    }

}
