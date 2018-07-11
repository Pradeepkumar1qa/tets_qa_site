package com.qait.automation.RobotclassDemo;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Loginqait {
	WebDriver driver;

	@Test
	public void launchbrowser() {
		System.out.println("launching brwoser");
		System.setProperty("webdriver.chrome.driver",
				"C:\\Users\\pradeepkumar1\\Downloads\\chromedriver\\chromedriver.exe");

		driver = new ChromeDriver();
		String url = "http://10.0.31.161:9292/";
		driver.get(url);
		driver.manage().window().maximize();
		System.out.println(driver.getTitle());
		String ExceptedTitle = "QA InfoTech Selenium Test Site";
		Assert.assertTrue(driver.getTitle().equals(ExceptedTitle), "unable to go to the site");

	}

	// @Test(dependsOnMethods= {"launchbrowser"},description="basic authorization
	// test")
	public void checkbasicauthorization() throws AWTException, InterruptedException {

	}

	@Test(dependsOnMethods = "launchbrowser")
	public void checkBrokenimage() throws InterruptedException {
		driver.findElement(By.linkText("Broken Images")).click();
		List<WebElement> s = driver.findElements(By.cssSelector(".example img"));

		ArrayList<String> notworkingurls = new ArrayList<String>();
		Iterator<WebElement> it = s.iterator();
		int respcode = 200;
		HttpURLConnection huc = null;
		while (it.hasNext()) {
			String url = ((WebElement) it.next()).getAttribute("src").toString();

			try {
				huc = (HttpURLConnection) (new URL(url).openConnection());
				huc.setRequestMethod("HEAD");
				huc.connect();
				respcode = huc.getResponseCode();
				if (respcode > 400) {

					notworkingurls.add(url);
				} else {
					System.out.println("url is working porperly");

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println(notworkingurls + "total number=" + notworkingurls.size());

		driver.navigate().back();
		Thread.sleep(5000);

	}

	@Test(dependsOnMethods = { "checkBrokenimage" })
	public void check_exit_intent() throws AWTException, InterruptedException {
		driver.findElement(By.linkText("Exit Intent")).click();

		Robot robot = new Robot();
		robot.mouseMove(400, 400);
		robot.keyPress(KeyEvent.VK_ENTER);
		robot.keyRelease(KeyEvent.VK_ENTER);
		Thread.sleep(1000);
		robot.mouseMove(20, 20);
		String text = driver.findElement(By.cssSelector(".modal .modal-title h3")).getText();
		String Excepted = "THIS IS A MODAL WINDOW";
		System.out.println(text);
		Assert.assertTrue(text.equals(Excepted), "modal is not displayed on this page");
		// robot.mouseMove(100, 200);

		WebElement element = driver.findElement(By.cssSelector(".modal-footer p"));
		JavascriptExecutor js = (JavascriptExecutor) driver;
		Thread.sleep(1000);
		js.executeScript("arguments[0].click()", element);
		element = driver.findElement(By.id("ouibounce-modal"));
		String s = element.getCssValue("display");// System.out.println("debuggin-"+s);
		Assert.assertTrue(s.equals("none"), "modal is continued to displayed");
		driver.navigate().back();

	}

	@Test(dependsOnMethods = { "check_exit_intent" }, description = "chcking sorted tables")
	public void checksortedtables() {
		driver.findElement(By.linkText("Sortable Data Tables")).click();
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		List<WebElement> rows = new ArrayList<WebElement>();
		rows = driver.findElement(By.xpath("//*[@id=\"table1\"]/tbody")).findElements(By.tagName("tr"));
		rows.sort(new Comparator<WebElement>() {

			@Override
			public int compare(WebElement o1, WebElement o2) {
				float a = Float
						.parseFloat(o1.findElement(By.cssSelector("td:nth-child(4)")).getText().replace("$", ""));
				float b = Float
						.parseFloat(o2.findElement(By.cssSelector("td:nth-child(4)")).getText().replace("$", ""));
				int res = a > b ? -1 : a < b ? 1 : 0;
				return res;

			}
		});
		driver.findElement(By.xpath("//*[@id=\"table1\"]/thead/tr/th[4]/span")).click();
		driver.findElement(By.xpath("//*[@id=\"table1\"]/thead/tr/th[4]/span")).click();
		List<WebElement> afterclickingrows = new ArrayList<WebElement>();
		afterclickingrows = driver.findElement(By.xpath("//*[@id=\"table1\"]/tbody")).findElements(By.tagName("tr"));
		boolean finalassertionresult = false;
		if (rows.size() == afterclickingrows.size()) {
			for (int i = 0; i < rows.size(); i++) {
				String priorclickingdue = ((WebElement) rows.get(i)).findElement(By.cssSelector("td:nth-child(4)"))
						.getText();
				String afterclickingdue = ((WebElement) afterclickingrows.get(i))
						.findElement(By.cssSelector("td:nth-child(4)")).getText();
				System.out.println(priorclickingdue + "   " + afterclickingdue);
				finalassertionresult = priorclickingdue.equals(afterclickingdue);
				if (!finalassertionresult) {
					System.out.println("both of them are not equal order wise at +" + i);
					break;
				}
			}
		} else {
			System.out.println("size of sorted arrat are not same after clicking due button");
		}
		Assert.assertTrue(finalassertionresult, "unable to proceed further");
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		driver.navigate().back();
	}

	@Test(dependsOnMethods = "checksortedtables", description = "checking login page")
	public void verifyLoginpage() throws InterruptedException {
		driver.findElement(By.linkText("Form Authentication")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"content\"]/div/h2")).isDisplayed(),
				"from authentication page is not displayed");
		driver.findElement(By.id("username")).sendKeys("pradeep");
		driver.findElement(By.name("password")).sendKeys("nothing");
		driver.findElement(By.cssSelector("#login > button")).click();
		String found = driver.findElement(By.id("flash")).getText().replace("\n×", "").trim();

		Assert.assertTrue(found.equals("Your username is invalid!"), "not at the error page");
		Thread.sleep(1000);
		driver.navigate().back();
		driver.navigate().back();

	}

	@Test(dependsOnMethods = "verifyLoginpage", description = "checking login page")
	public void verifyloginsuccessfully() {
		driver.findElement(By.linkText("Form Authentication")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"content\"]/div/h2")).isDisplayed(),
				"from authentication page is not displayed");
		driver.findElement(By.id("username")).sendKeys("tomsmith");
		driver.findElement(By.name("password")).sendKeys("SuperSecretPassword!");
		driver.findElement(By.cssSelector("#login > button")).click();
		String found = driver.findElement(By.id("flash")).getText().replace("\n×", "").trim();
		String expected = "You logged into a secure area!";
		System.out.println(found + "debugging");
		Assert.assertTrue(found.equals(expected), "unable to show login");
		Utility.chill();
		driver.findElement(By.cssSelector("#content > div > a > i")).click();
		Assert.assertTrue(driver.findElement(By.xpath("//*[@id=\"content\"]/div/h2")).isDisplayed(),
				"from authentication page is not displayed");
		Utility.chill();
		driver.navigate().to("http://10.0.31.161:9292/");
	}

	@Test(dependsOnMethods = "verifyloginsuccessfully", description = "checking mouse hover")
	public void checkmousehover() {
		driver.findElement(By.linkText("Hovers")).click();
		int totalimg = driver.findElements(By.cssSelector("#content > div> div> img")).size();
		Assert.assertTrue(totalimg == 3, "number of image is not equal to 3");
		// System.out.println(totalimg);

		WebElement web_Element_To_Be_Hovered = driver
				.findElement(By.cssSelector("#content > div > div:nth-child(3) > img"));
		Actions builder = new Actions(driver);
		builder.moveToElement(web_Element_To_Be_Hovered).build().perform();
		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/div/h5")).isDisplayed(), true);
		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/div/a")).isDisplayed(), true);

		driver.navigate().refresh();
		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/div/h5")).isDisplayed(),
				false);
		Assert.assertEquals(driver.findElement(By.xpath("//*[@id=\"content\"]/div/div[1]/div/a")).isDisplayed(), false);

	}

	@Test(dependsOnMethods = "checkmousehover", description = "checking editor functionality")
	public void checkeditor() {
		driver.navigate().to("http://10.0.31.161:9292/");
		driver.findElement(By.linkText("WYSIWYG Editor")).click();

		WebElement editor = driver.findElement(By.xpath("//*[@id=\"mceu_13-body\"]"));
		boolean ActualResult = editor.isDisplayed();
		Assert.assertTrue(ActualResult);

		driver.switchTo().frame("mce_0_ifr");
		driver.findElement(By.xpath("//*[@id=\"tinymce\"]/p")).clear();
		driver.findElement(By.id("tinymce")).sendKeys("QA Infotech");
		driver.findElement(By.id("tinymce")).sendKeys(Keys.chord(Keys.CONTROL, "a"));
        Utility.chill();
		driver.switchTo().defaultContent();
		driver.findElement(By.xpath("//*[@id=\"mceu_3\"]/button/i")).click();
		driver.switchTo().frame("mce_0_ifr");
		String Expected = "QA Infotech";
		String Actual = driver.findElement(By.xpath("//*[@id=\"tinymce\"]/p/strong")).getText();
		System.out.println(Actual);
		Assert.assertEquals(Actual, Expected);
		Utility.chill();

	}
	@Test(dependsOnMethods= {"checkeditor"},description="checking status code page")
	public void  checkstatuscode() {
		driver.navigate().to("http://10.0.31.161:9292/");
		driver.findElement(By.linkText("Status Codes")).click();
		String text=driver.findElement(By.xpath("//*[@id=\"content\"]/div/h3")).getText();
		Assert.assertEquals(text,"Status Codes");
	
	    //checking error code 404
		driver.findElement(By.xpath("//*[@id=\"content\"]/div/ul/li[3]/a")).click();
		String s=driver.findElement(By.tagName("p")).getText();
		String s2=s.substring(0,37);
		String s1="This page returned a 404 status code.";
		//System.out.println(s2);
		Assert.assertEquals(s2, s1);
		System.out.println("finally completed all test cases");
	}
	
}
