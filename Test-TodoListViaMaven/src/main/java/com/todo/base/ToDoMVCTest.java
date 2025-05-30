package com.todo.base;

import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
//import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.List;

public class ToDoMVCTest {
	private WebDriver driver;

	@Before
	public void setUp() throws InterruptedException {
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
	public void testAddItem() throws InterruptedException {
		// Explicitly wait for the alert to be present.
		WebDriverWait wait = new WebDriverWait(driver, 20);
		WebElement inputBox = driver.findElement(By.xpath("//input[@placeholder='What needs to be done?']"));
		String[] listItm = { "Drink Water Every Hour", "Exercise Daily", "Clean the House", "Meditate Daily" };
		for (String li : listItm) {
			inputBox.sendKeys(li + Keys.ENTER);
		}

		List<WebElement> todoItems = driver.findElements(By.xpath("//ul[@class='todo-list']"));

		WebElement todoCount = driver.findElement(By.cssSelector(".todo-count"));
		System.out.println(todoCount.getText());
		Assert.assertEquals("4 items left", todoCount.getText());


		driver.findElement(By.xpath("(//input[@type='checkbox'])[2]")).click();

		driver.findElement(By.xpath("(//input[@type='checkbox'])[3]")).click();
		
		Thread.sleep(2000);
		driver.findElement(By.linkText("Completed")).click();
		Assert.assertEquals("2 items left", todoCount.getText());

		Thread.sleep(2000);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("All"))).click();
		List<WebElement> allItems = driver.findElements(By.cssSelector(".todo-list li"));
		Assert.assertEquals(4, allItems.size());

		driver.findElement(By.linkText("Active")).click();
		List<WebElement> activeItems = driver.findElements(By.cssSelector(".todo-list li"));
		Assert.assertEquals(2, activeItems.size());
		

		for (WebElement opt : todoItems) {
			System.out.println(opt.getText());
			if (opt.getText().equals("curd")) {
				opt.click();
				break;
			}

		}
		WebElement todoItem = driver.findElement(By.xpath("//app-todo-item[1]//li[1]"));
		Actions actions = new Actions(driver);
		actions.moveToElement(todoItem).perform();
		List<WebElement> todoItemss = driver.findElements(By.xpath("//app-todo-item[1]//li[1]"));
		Assert.assertEquals(1, todoItemss.size());
		
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.linkText("All"))).click();
		driver.findElement(By.xpath("(//input[@type='checkbox'])[4]")).click();
		
		
	}
}
		