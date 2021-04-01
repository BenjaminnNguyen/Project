package constant;

import org.openqa.selenium.By;

public class CT_Account {
	public static By ELEMENT_USERNAME_TEXTBOX = By.xpath("//*[@id='UserName']|//*[@id='username']|//*[@class='user-name']/input");
	public static By ELEMENT_PASSWORD_TEXTBOX = By.xpath("//*[@id='password']|//*[@class='password']/input|//*[@id='PassWord']");
	public static By ELEMENT_LOGIN_BUTTON = By.xpath("//*[@value='ĐĂNG NHẬP' or @value='Log in' or @value='Login']|//button[contains(text(),'Đăng nhập') or text()='Login']");
	public static By ELEMENT_LOGOUT_BUTTON = By.xpath("//*[text()='Logout']/..");


}
