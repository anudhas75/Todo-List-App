package com.todo.base;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import com.sun.tools.javac.util.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.List;

public class Todo {
    private WebDriver driver;
     
    @Before
    public void setUp() {
    	WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.get("https://todomvc.com/examples/angular/dist/browser/#/all");
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void testAddItem() {
        WebElement inputBox = driver.findElement(By.className("new-todo ng-pristine ng-valid ng-touched"));
        inputBox.sendKeys("Buy milk" + Keys.ENTER);

        List<WebElement> todoItems = driver.findElements(By.cssSelector("ul[class='todo-list'] app-todo-item li"));
        Assert.assertTrue(todoItems.size() > 0);
        Assert.assertEquals("Buy milk", todoItems.get(0).getText());
    }

    @Test
    public void testDeleteItem() {
        testAddItem(); // Reuse add logic

        WebElement todoItem = driver.findElement(By.cssSelector(".todo-list li"));
        Actions actions = new Actions(driver);
        actions.moveToElement(todoItem).perform();

        WebElement destroyButton = todoItem.findElement(By.cssSelector("button.destroy"));
        destroyButton.click();

        List<WebElement> todoItems = driver.findElements(By.cssSelector(".todo-list li"));
        Assert.assertEquals(0, todoItems.size());
    }

    @Test
    public void testEditItem() {
        testAddItem();

        WebElement label = driver.findElement(By.cssSelector(".todo-list li label"));
        Actions actions = new Actions(driver);
        actions.doubleClick(label).perform();

        WebElement editInput = driver.findElement(By.cssSelector(".todo-list li.editing .edit"));
        editInput.clear();
        editInput.sendKeys("Buy eggs" + Keys.ENTER);

        WebElement updatedLabel = driver.findElement(By.cssSelector(".todo-list li label"));
        Assert.assertEquals("Buy eggs", updatedLabel.getText());
    }

    @Test
    public void testRemainingItemsCount() {
        testAddItem();

        WebElement count = driver.findElement(By.cssSelector("footer .todo-count"));
        Assert.assertTrue(count.getText().contains("1 item left"));
    }

    @Test
    public void testFilterOptions() {
        testAddItem();

        WebElement checkbox = driver.findElement(By.cssSelector(".todo-list li .toggle"));
        checkbox.click(); // Mark item as complete

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
}


