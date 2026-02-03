package eal.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import eal.Utilities.CommonMethods;
import eal.Utilities.LogColor;

public class DashBoard_POM extends CommonMethods {

	public static final Logger logger = LogManager.getLogger(DashBoard_POM.class);

	//PageFactory Constructor:
	public DashBoard_POM() {
		PageFactory.initElements(driver, this);
	}
	
	//Controllable Xpath Strings:
	
	private static final String DashBoard_Link_Elements= "//a[text()='%s']";
	
//	**********************************************************************************************************************************************************
	public boolean verify_LinkButtons_inDashBoard(String buttonName_fromSD) {
		
		try {
			
			String formattedStringXpathOf_LinkButtons_DB =String.format(DashBoard_Link_Elements, buttonName_fromSD);
			logger.info(formattedStringXpathOf_LinkButtons_DB);
			
			boolean presence=isElementPresent(By.xpath(formattedStringXpathOf_LinkButtons_DB));
			if(presence) {
				
				WebElement LinkButton = driver.findElement(By.xpath(formattedStringXpathOf_LinkButtons_DB));
				logger.info("Clicking on the Element LinkButton: "+LinkButton);
				
				clickAndDraw(LinkButton);
				
				return true;
			}
			else {
				return false;
			}
		
		} catch (Exception e) {
			logger.error(LogColor.RED + e + LogColor.RESET);	
			return false;
		}
	}
	


}
