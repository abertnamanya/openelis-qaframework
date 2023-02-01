package org.openelisglobal.qaframework.automation.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * This class represents the Work Plan By Test Type Page
 */
public class WorkPlanByTestTypePage extends Page {
	
	private static final String PAGE_PATH = "/WorkPlanByTest";
	
	private static final String PATH_HOME = "/Dashboard";
	
	private static final By DROP_DOWN_TEST_TYPES = By.id("testName");
	
	private static final By BUTTON_PRINT = By.id("print");
	
	public WorkPlanByTestTypePage(Page parent) {
		super(parent);
	}
	
	@Override
	public String getPageUrl() {
		return PAGE_PATH;
	}
	
	public HomePage goToHomePage() {
		this.goToPage(PATH_HOME);
		return new HomePage(this);
	}
	
	public void selctTestType(String testType) {
		By FIELD_OPTION = By.tagName("option");
		clickOn(DROP_DOWN_TEST_TYPES);
		List<WebElement> options = findElement(DROP_DOWN_TEST_TYPES).findElements(FIELD_OPTION);

		for (WebElement option : options) {
			if (option.getText().equals(testType)){
				option.click();
				break;
			}
		}
	}
	
	public void clickPrintWorkPlan() {
		clickOn(BUTTON_PRINT);
	}
}
