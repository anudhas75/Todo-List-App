package com.todo.base;


import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.testng.Assert;
import org.testng.annotations.*;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Todo {
    private WebDriver driver;
     
    @BeforeMethod
    public void setUp() {
    	WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://todomvc.com/examples/angular/dist/browser/#/all");
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }



    @Test(priority = 0)
    public void testAddItem() throws InterruptedException {
        Thread.sleep(5000);
        WebElement inputBox = driver.findElement(By.xpath("//input[@placeholder=\"What needs to be done?\"]"));
        inputBox.sendKeys("Buy milk" + Keys.ENTER);

        List<WebElement> todoItems = driver.findElements(By.xpath("(//*[@class=\"todo-list\"]/app-todo-item/li)"));
        Assert.assertTrue(todoItems.size() > 0);
        Assert.assertEquals("Buy milk", todoItems.get(0).getText());

    }

    @Test (priority = 1)
    public void testDeleteItem() throws InterruptedException {
        testAddItem(); // Reuse add logic

        WebElement todoItem = driver.findElement(By.xpath("(//*[@class=\"todo-list\"]/app-todo-item/li)"));
        Actions actions = new Actions(driver);
        actions.moveToElement(todoItem).perform();

        WebElement destroyButton = todoItem.findElement(By.xpath("//button[@class=\"destroy\"]"));
        destroyButton.click();

        List<WebElement> todoItems = driver.findElements(By.xpath("(//*[@class=\"todo-list\"]/app-todo-item/li)"));
        Assert.assertEquals(0, todoItems.size());
    }

    @Test(priority = 2)
    public void testEditItem() throws InterruptedException, AWTException {
        testAddItem();

        WebElement label = driver.findElement(By.cssSelector(".todo-list li label"));
        Actions actions = new Actions(driver);
        actions.doubleClick(label).perform();
        WebElement editInput = driver.findElement(By.xpath("//li[@class='editing']"));
        System.out.println(editInput.isDisplayed());
        boolean iseditable = editInput.getAttribute("class").equals("editing");
        Assert.assertTrue(iseditable);
    }

    @Test(priority = 3)
    public void testRemainingItemsCount() throws InterruptedException {
        testAddItem();

        WebElement count = driver.findElement(By.cssSelector("footer .todo-count"));
        Assert.assertTrue(count.getText().contains("1 item left"));
    }

    @Test(priority = 4)
    public void testFilterOptions() throws InterruptedException {
        testAddItem();

        WebElement checkbox = driver.findElement(By.cssSelector(".todo-list li .toggle"));
       checkbox.click(); // Mark item as complete
       WebElement  completeItem = driver.findElement(By.xpath("//li[@class=\"completed\"]"));
       Assert.assertTrue(completeItem.isDisplayed());

        driver.findElement(By.linkText("Active")).click();
        List<WebElement> activeItems = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(0, activeItems.size());

        driver.findElement(By.linkText("Completed")).click();
        List<WebElement> completedItems = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(1, completedItems.size());

        driver.findElement(By.linkText("All")).click();
        List<WebElement> allItems = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(1, allItems.size());
    }
    @AfterMethod
    public void tearDown() {

        if (driver != null) {
            driver.quit(); // closes the browser after each test
        }
    }
}


