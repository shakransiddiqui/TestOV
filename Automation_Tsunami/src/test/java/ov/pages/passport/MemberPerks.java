package ov.pages.passport;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import ov.utilities.CommonMethods;
import ov.utilities.Driver;

import java.time.Duration;  
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;


public class MemberPerks extends CommonMethods{
	WebDriver driver = Driver.getDriver();
	WebDriverWait wait;
	

	public static final Logger logger = LogManager.getLogger(MemberPerks.class);
	public MemberPerks() { 
		PageFactory.initElements(Driver.getDriver(), this);
		wait=new WebDriverWait(Driver.getDriver(),Duration.ofSeconds(10));
	}
	
	

    @FindBy(css = "span.member-rewards-header-text")
    private WebElement memberPerksHeader;

    @FindBy(xpath = "//div[@class='rewards-perk-item']//input[@type='checkbox']")
     private List<WebElement> perkCheckboxes;
    
    @FindBy(xpath = "//button[contains(@class,'footer-action-btn-back')]")
    private WebElement saveCreateButton;
    
    @FindBy(xpath = "//h1")
    private WebElement pageHeader;
    

    

    // Method 1
    public boolean isMemberPerksHeaderDisplayed() {
        try {
            return memberPerksHeader.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    // Method 2
    public void selectFivePerkCards(int count) {
        for (int i = 0; i < count && i < perkCheckboxes.size(); i++) {
            WebElement checkbox = perkCheckboxes.get(i);
            if (!checkbox.isSelected()) {
                checkbox.click();
           
            }
        }
    }
            
    
       public void clickButton(String buttonName) {
           if(buttonName.equalsIgnoreCase("Save & Create Application")) {
              saveCreateButton.click();
            }
        }
        
              

    public String getPageHeaderText() {
         return pageHeader.getText().trim();
     }
    
    public boolean isCreateApplicationPageDisplayed(String pageName)
    {
    	wait.until(ExpectedConditions.urlContains(pageName));
    	return driver.getCurrentUrl().contains(pageName);
    }
}