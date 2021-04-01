package excute;

import common.CommonBase;

import java.awt.AWTException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public final class Question2 extends CommonBase {

	@BeforeMethod
	public void beforeMethod() {
		driver = initDriverTest("https://tiki.vn", "firefox");
	}

	@AfterMethod
	public void afterMethod() {
		quitDriver(driver);
	}

	@Test
	public void tc() {
		waitForPageLoaded(driver);
		click(By.xpath("//input[@data-view-id='main_search_form_input']"));
		type(By.xpath("//input[@data-view-id='main_search_form_input']"), "iPhone 11", true);
		click(By.xpath("//button[@data-view-id='main_search_form_button']"));
		waitForPageLoaded(driver);
		info("is display iPhone 11");
		click(By.xpath("//*[@class='product-item']//span[contains(text(),'iPhone 11')][1]"));
		waitForPageLoaded(driver);
		String Price = trimCharactor(
				trimCharactor(getText(By.xpath("//*[@class='product-price__current-price']")), "₫"), ".");
		String url = getUrl();
		openPage("https://www.ebay.com/");
		click(By.id("gh-ac"));
		type(By.id("gh-ac"), "iPhone 11", true);
		click(By.id("gh-btn"));
		info("is display iPhone 11");
		waitForPageLoaded(driver);
		WebElement p = getElement(By.xpath(
				"//*[text()='Apple iPhone 11 A2111 64/128/256GB Unlocked Brand New']/../..//div[1][@class='s-item__detail s-item__detail--primary']/span"));
		String PriceEbay = trimCharactor(trimCharactor(getText(p), ","), "VND");
		String urlEbay = getUrl();
		try {
			double price = Double.parseDouble(Price);
			double priceEbay = Double.parseDouble(PriceEbay);
			if (price < priceEbay) {
				info("Tiki.vn \t" + "IPhone 11 +\t " + Price + "\t" + url);
				info("Ebay.com \t" + "IPhone 11 +\t " + PriceEbay + "\t" + urlEbay);
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		pause(30000);
	}

}
