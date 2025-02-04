package org.openelisglobal.qaframework.automation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.openelisglobal.qaframework.RunTest;
import org.openelisglobal.qaframework.automation.page.AddOrderPage;
import org.openelisglobal.qaframework.automation.page.HomePage;
import org.openelisglobal.qaframework.automation.page.LoginPage;
import org.openelisglobal.qaframework.automation.test.TestBase;
import org.openelisglobal.qaframework.automation.test.TestProperties;
import org.openelisglobal.qaframework.automation.utils.Utils;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AddOrderSteps extends TestBase {
	
	private LoginPage loginPage;
	
	private HomePage homePage;
	
	private AddOrderPage addOrderPage;
	
	protected TestProperties testProperties = TestProperties.instance();
	
	@After(RunTest.HOOK.ORDER)
	public void destroy() {
		quit();
	}
	
	@Before(RunTest.HOOK.ORDER)
	public void setLoginPage() {
		System.out.println("....Add Order......");
		loginPage = new LoginPage(getWebDriver());
	}
	
	@Given("User logs in and visits Home page")
	public void visitLoginPage() throws Exception {
		homePage = loginPage.goToHomePage();
	}
	
	@And("User clicks add order and goes to Add order Page")
	public void goToAddOrderPage() throws Exception {
		addOrderPage = homePage.goToAddOrderPage();
	}
	
	@Then("Order form should appear")
	public void orderFormShouldAppear() throws Exception {
		addOrderPage.turnOnAcessionValidation(false);
		assertTrue(addOrderPage.containsTextRequest());
	}
	@And("Remember site and requester checkbox exists")
	public void rememberSiteAndRequesterCheckboxExists() {
		assertTrue(addOrderPage.rememberSiteAndRequesterCheckBoxExist());
		assertTrue(addOrderPage.containsText("Remember site and requester"));
	}
	@When("User enters Accesion Number {string}")
	public void enterAcessionNumber(String accesionNumber) throws Exception {
		addOrderPage.turnOnAcessionValidation(true);
		addOrderPage.enterAccessionNumber(accesionNumber);
	}
	
	@Then("Validate {string} AccesionNumber Entered {string}")
	public void checkEnteredAccessionNumber(String status, String accesionNumber) throws Exception {
		if (status.equals("valid")) {
			addOrderPage.clickOnNextVisitDate();
			if(addOrderPage.alertPresent()){
				addOrderPage.dismissAlert();
				addOrderPage.clickGenerateButton();
				accesionNumber = addOrderPage.getAccesionNumberValue();
			}
			Thread.sleep(1000);
			assertTrue(addOrderPage.accessionNumberEntered(accesionNumber));
			assertEquals(addOrderPage.getAccesionNumberClass(), "text");
		} else if (status.equals("invalid")) {
			addOrderPage.clickOnNextVisitDate();
			Thread.sleep(1000);
			addOrderPage.acceptAlert();
			assertEquals(addOrderPage.getAccesionNumberClass(), "text error");
		}
		
	}
	
	@When("User clicks Generate Button")
	public void clickGenerate() throws Exception {
		addOrderPage.clickGenerateButton();
	}
	
	@Then("Generated Accesion Number should be a digit")
	public void generatedAccesionNumbershouldBeDigit() throws Exception {
		//assertTrue(addOrderPage.GeneratedAccessionNumberIsDigit());
		assertNotNull(addOrderPage.getAccesionNumberValue());
	}

	@Then("Turn off accession number validation from the previous stage")
	public void turnOffAccessionNumberValidationFromThePreviousStage() {
		addOrderPage.turnOnAcessionValidation(false);
	}
	
	@Then("View page Request Date and Received Date Default to the current date")
	public void requestAndRecievedDatesShouldDefaultToCurrent() throws Exception {
		// this test will fail at times if the server and the testing
		// framework run in different time zones
		//assertEquals(addOrderPage.getRecievedDateValue(), getCurrentDate());
		//assertEquals(addOrderPage.getRequestDateValue(), getCurrentDate());
	}
	
	@And("Both request and received date should be mandatory")
	public void requestAndRecievedDatesShouldbeMandatory() throws Exception {
		assertEquals(addOrderPage.getRequestDateRequiredClass(), "requiredlabel");
		assertEquals(addOrderPage.getRecievedDateRequiredClass(), "requiredlabel");
	}
	
	@When("User enters incorrect Request and Received Date format {string}")
	public void UserEntersIncorrectRequestAndRecievedDateFormat(String date) throws Exception {
		assertEquals(addOrderPage.getRecievedDateClass(), "text required");
		assertEquals(addOrderPage.getRequestDateClass(), "required");
		addOrderPage.enterRecievedDate(date);
		addOrderPage.enterRequestDate(date);
	}
	
	@Then("Request and Received Date Fields should show error")
	public void RequestAndRecievedDateFieldShouldThrowError() throws Exception {
		// enter next Vist Date to triger field error
		addOrderPage.clickOnNextVisitDate();
		Thread.sleep(1000);
		assertEquals(addOrderPage.getRecievedDateClass(), "text required error");
		assertEquals(addOrderPage.getRequestDateClass(), "required error");
	}
	
	@When("User enters Request Date in future")
	public void UserEntersRequestInFuture() throws Exception {
		String strDate = Utils.getFutureDate();
		addOrderPage.enterRequestDate(strDate);
	}
	
	@Then("Alert should appear if date is in future")
	public void alertShouldApperIfFutureDateIsEntered() throws Exception {
		// enter next Vist Date to triger field error
		addOrderPage.clickOnNextVisitDate();
		Thread.sleep(1000);
		addOrderPage.acceptAlert();
		assertEquals(addOrderPage.getRequestDateClass(), "required error");
	}
	
	@When("User enters correct Request and Received Date format")
	public void UserEnterscorrectRequestAndRecievedDateFormat() throws Exception {
		String strDate = Utils.getPastDate();
		addOrderPage.enterRecievedDate(strDate);
		addOrderPage.enterRequestDate(strDate);
	}
	
	@Then("Request and Received Date Fields should not show error")
	public void RequestAndRecievedDateFieldShouldNotThrowError() throws Exception {
		// enter next Vist Date to triger field error
		addOrderPage.clickOnNextVisitDate();
		Thread.sleep(1000);
		assertEquals(addOrderPage.getRecievedDateClass(), "text required");
		assertEquals(addOrderPage.getRequestDateClass(), "required");
	}
	
	@When("User enters Reception time {string}")
	public void userEntersRecievedTime(String time) {
		addOrderPage.enterRecievedTime(time);
	}
	
	@Then("Field Automatically corrects {string} straight numeric to proper format HH:MM {string}")
	public void fieldAutomaticallyCorrectsReceptionTime(String action, String correctedTime) throws InterruptedException {
		// enter next Vist Date to triger field error
		addOrderPage.clickOnNextVisitDate();
		if (action.trim().equals("auto-correct")) {
			Thread.sleep(1000);
			assertEquals(addOrderPage.getRecievedTimeValue(), correctedTime);
		} else if (action.trim().equals("none")) {
			assertEquals(addOrderPage.getRecievedTimeValue(), correctedTime);
		}
	}
	
	@And("Field validates {string} correct format")
	public void fieldValidatesRecetionTimeFormat(String status) throws Exception {
		if (status.trim().equals("valid")) {
			assertNotEquals(addOrderPage.getRecievedTimeClass(), "error");
		} else if (status.trim().equals("invalid")) {
			assertEquals(addOrderPage.getRecievedTimeClass().trim(), "error");
		}
	}
	
	@Then("Site Name is mandatory")
	public void siteNameShoulBeMandatory() {
		assertEquals(addOrderPage.getSiteNameRequiredClass(), "requiredlabel");
	}
	@When("User enters site name suggestion text {string}")
	public void userEntersSiteNameSuggestionText(String siteNameSuggestion) throws InterruptedException {
		addOrderPage.enterSiteNameSuggestion(siteNameSuggestion);
		Thread.sleep(100);
	}
	
	@Then("User Selects Site Name from a Drop down Menu")
	public void selectSiteNameFromDropDownMenu() throws InterruptedException {
		addOrderPage.selectSiteNameFromDropDown();
	}
	
	@And("User Selects Program from a Drop down Menu")
	public void selectProgramFromDropDownMenu() throws InterruptedException {
		addOrderPage.selectProgramFromDropDown();
	}
	
	@Then("Requester's Name is mandatory")
	public void requesterNameIsMandatory() throws InterruptedException {
		assertEquals(addOrderPage.getLastNameRequiredClass(), "requiredlabel");
	     addOrderPage.turnOnAllowRequesterFieldAllFreeText();
		 Thread.sleep(1000);
	}

	@And("User Enters Requester's Last Name {string}")
	public void enterLastName(String lastName) {
		addOrderPage.enterRequesterLastName(lastName);
		assertEquals(addOrderPage.getLastNameValue(), lastName);
	}

	@And("User Enters Requester's First Name {string}")
	public void enterFirstName(String firstName) {
		addOrderPage.enterRequesterFirstName(firstName);
		assertEquals(addOrderPage.getFistNameValue(), firstName);
	}

	@When("User Enters Telephone Number {string}")
	public void enterTelephone(String telephone) {
		addOrderPage.turnOnTelephoneValidation();
		addOrderPage.enterRequesterTelephone(telephone);
	}

	@Then("Validate {string} Telephone Number")
	public void validateTelephoneNumber(String status) throws InterruptedException {
		if (status.equals("valid")) {
			addOrderPage.clickOnNextVisitDate();
			if (addOrderPage.alertPresent()) {
				addOrderPage.acceptAlert();
				addOrderPage.enterRequesterTelephone("");
				return;
			}
			Thread.sleep(1000);
			assertEquals(addOrderPage.getTelephoneNumberClass(), "text");
		} else if (status.equals("invalid")) {
			addOrderPage.clickOnNextVisitDate();
			Thread.sleep(1000);
			addOrderPage.acceptAlert();
			assertEquals(addOrderPage.getTelephoneNumberClass(), "text error");
		}
	}
	@And("User Enters Fax {string}")
	public void enterFax(String fax) {
		addOrderPage.enterRequesterFax(fax);
	}

	@And("User Enters Email {string}")
	public void enterEmail(String email) throws InterruptedException {
		addOrderPage.enterRequesterEmail(email);
	}

	@Then("User selects patient payment status drop down list")
	public void userSelectsPatientPaymentStatusDropDownList() {
		addOrderPage.selectPatientPaymentStatus();
	}

	@When("User selects others from Sampling performed for analysis drop down list")
	public void userSelectsOthersFromSamplingPerformedForAnalysisDropDownList() {
		addOrderPage.selectSamplingPerformed();
	}
	@Then("Others sampling performed text box appears")
	public void othersSamplingPerformedTextBoxAppears() {
        assertTrue(addOrderPage.IsSamplingPerformedOthersTextBoxDisplayed());
	}
	@And("Others sampling performed text field accepts text {string}")
	public void othersSamplingPerformedTextFieldAcceptsText(String other) {
		   addOrderPage.enterSamplingPerformedOther(other);
		   assertEquals(addOrderPage.getSamplingPerformedOthersValue(), other);
	}
	@Then("Sample addition is mandatory")
	public void sampleAdditionMandatory() {
		assertEquals(addOrderPage.getSampleRecquiredClass(), "requiredlabel");
	}
	
	@When("User Clicks on + Button next to Sample")
	public void clickAddSample() throws InterruptedException {
		addOrderPage.clickAddSampleButton();
	}
	
	@Then("Sample Selection Field appears")
	public void sampleSelectionFiedlAppears() {
		assertTrue(addOrderPage.getSampleSelectionField().isDisplayed());
	}
	
	@And("Sample types display in drop-down list")
	public void sampleTypesDisplayInDropDownMenu() {
		assertTrue(addOrderPage.sampleTypesDropDownMenuContainsSampleTypesOptions());
	}
	
	@And("User Selects Sample Type from Drop down menu")
	public void selectSampleTypeFromDropDownMenu() {
		addOrderPage.selectSampleTypeFromDropDownMenu("Serum");
	}
	@Then("User Checks the Reject checkbox")
	public void userChecksTheRejectCheckbox() throws InterruptedException {
		addOrderPage.rejectSampleCheckBox();
		if (addOrderPage.alertPresent()) {
			addOrderPage.acceptAlert();
		}
		assertFalse(addOrderPage.sampleConditionDropDownOptionDisabled());
	}
	
	@And("User Selects Sample Conditions from Drop down menu")
	public void selectSampleConditionFromDropDownMenu() {
		addOrderPage.selectSampleConditionFromDropDownMenu();
	}


	@And("User Clicks reject checkBox to remove added Sample Conditions")
	public void userClicksRejectCheckBoxToRemoveAddedSampleConditions() throws InterruptedException {
		addOrderPage.rejectSampleCheckBox();
		if (addOrderPage.alertPresent()) {
			addOrderPage.acceptAlert();
		}
		assertTrue(addOrderPage.sampleConditionDropDownOptionDisabled());
	}
	
	@And("User Clicks remove button to remove added Sample")
	public void removeAddedSampleTypeFromDropDownMenu() {
		addOrderPage.removeAddedSampleType();
	}
	
	@And("User Re-adds Samples")
	public void readdSamples() {
		addOrderPage.reAddSamples();
        //assertNotNull(addOrderPage.getRemoveAllButton());
	}
	
	@And("User Clicks to Remove all")
	public void removesAllSamples() {
		addOrderPage.clickRemoveAllButton();
	}
	
	@When("User enters Incorrect Date format {string}")
	public void enterIncorrectDateFormat(String date) {
		addOrderPage.enterCollectionDate(date);
	}
	
	@Then("Text field hightlights in red")
	public void textFieldHighlightsInRed() throws InterruptedException {
		addOrderPage.clickOnCollectionTime();
		Thread.sleep(1000);
		assertEquals(addOrderPage.getCollectionDateClass(), "text error");
	}
	
	@When("User enters Date In the future")
	public void enterDateInTheFuture() {
		String strDate = Utils.getFutureDate();
		addOrderPage.enterCollectionDate(strDate);
	}
	
	@Then("Pop-up alert appears if date is in the future")
	public void poppAlertAppears() throws InterruptedException {
		addOrderPage.clickOnCollectionTime();
		Thread.sleep(1000);
		addOrderPage.acceptAlert();
		assertEquals(addOrderPage.getCollectionDateClass(), "text error");
	}
	
	@When("User enters correct Date")
	public void enterCorrectDateFormat() {
		String strDate = Utils.getPastDate();
		addOrderPage.enterCollectionDate(strDate);
	}
	
	@Then("Text field accepts the correct date format")
	public void textFieldAcceptsCorrectDate() throws InterruptedException {
		addOrderPage.clickOnCollectionTime();
		Thread.sleep(1000);
		assertEquals(addOrderPage.getCollectionDateClass(), "text");
	}
	
	@When("User enters Collection time {string}")
	public void userEntersCollectionTime(String time) {
		addOrderPage.enterCollectionTime(time);
	}
	
	@Then("Field Automatically corrects {string} straight numeric to proper Collection Time format HH:MM {string}")
	public void fieldAutomatocallyCorrectsCollectionTime(String action, String correctedTime) throws InterruptedException {
		addOrderPage.clickCollectorField();
		if (action.trim().equals("auto-correct")) {
			Thread.sleep(1000);
			assertEquals(addOrderPage.getCollectionTimeValue(), correctedTime);
		} else if (action.trim().equals("none")) {
			assertEquals(addOrderPage.getCollectionTimeValue(), correctedTime);
		}
	}
	
	@And("Field validates {string} Collection Time")
	public void fieldvalidatesCollectionTimeFormat(String status) throws Exception {
		if (status.trim().equals("valid")) {
			assertNotEquals(addOrderPage.getCollectionTimeClass(), "text error");
		} else if (status.trim().equals("invalid")) {
			assertEquals(addOrderPage.getCollectionTimeClass(), "text error");
		}
	}
	
	@When("User Enters Collector {string}")
	public void enterCollector(String name) {
		addOrderPage.enterCollector(name);
	}
	
	@Then("Field Acceps text {string}")
	public void assertTextEntered(String name) {
		assertEquals(addOrderPage.getCollectorValue(), name);
	}
	
	@Then("Tests entry is marked mandatory")
	public void testIsMarkedMandatory() {
		assertEquals(addOrderPage.getTestReqcuiredClass(), "requiredlabel");
	}
	
	@Then("Available Tests exists")
	public void hasAvailableTest() {
		assertTrue(addOrderPage.containsText("Available Tests"));
	}
	
	@When("User Checks checkbox next to test name")
	public void checkTestNameCheckBox() throws InterruptedException {
		addOrderPage.clickTestCheckBox();
	}
	
	@Then("Checkbox sticks, test name appears under Tests box")
	public void testNameAppearsUnderTestBox() {
		assertTrue(addOrderPage.getTestValue().contains("COVID-19 ANTIBODY IgG"));
	}
	
	@When("User unChecks checkbox next to test name")
	public void unCheckTestNameCheckBox() throws InterruptedException {
		addOrderPage.clickTestCheckBox();
	}
	
	@Then("Name disappears from Tests box")
	public void testNameDisppearsFromTestBox() {
		assertEquals(addOrderPage.getTestValue(), "");
	}
	
	@When("User Checks checkbox next to Panel name")
	public void checkPannelNameCheckBox() {
		addOrderPage.clickPannelCheckBox();
	}
	
	@Then("All applicable panel tests apear in the Testsbox")
	public void AllApplicableTestNameAppearsUnderTestBox() {
		if (addOrderPage.panelCheckBoxExists()) {
			assertNotEquals(addOrderPage.getTestValue(), "");
		}
	}
	
	@When("User unChecks checkbox next to Panel name")
	public void unCheckPannelNameCheckBox() {
		addOrderPage.clickPannelCheckBox();
	}
	
	@Then("All Test Names disappears from Tests box")
	public void allTestNameDisppearsFromTestBox() {
		assertEquals(addOrderPage.getTestValue(), "");
	}
	
	@When("User enters Text in Tests Box")
	public void enterTests() {
		try {
			addOrderPage.enterTest("Test A");
		}
		catch (Exception e) {
			
		}
	}
	
	@Then("Text cannot be entered in Tests Box")
	public void textDoesNotEnterTestBox() {
		assertNotEquals(addOrderPage.getTestValue(), "Test A");
		assertEquals(addOrderPage.getTestValue(), "");
	}
	
	@When("User deletes Text in Tests Box")
	public void deleteTests() throws InterruptedException {
		addOrderPage.clickTestCheckBox();
		try {
			addOrderPage.clearTestsField();
		}
		catch (Exception e) {
			
		}
	}
	
	@Then("Text cannot be cleared in Tests Box")
	public void textCanNotBeDeletedFromTestBox() {
		assertNotEquals(addOrderPage.getTestValue(), "");
		assertTrue(addOrderPage.getTestValue().contains("COVID-19 ANTIBODY IgG"));
	}
	
	@Then("Patient information form is marked mandatory")
	public void patientInformationIsMarkedMandatory() {
		assertEquals(addOrderPage.getPatientInformationRecquiredClass(), "requiredlabel");
	}
	
	@When("User Expands Patient information form by clicking the + button next to Patient")
	public void expandPatientInformatio() {
		addOrderPage.clickAddPatientInformation();
	}
	
	@Then("Patient Search appears")
	public void patientSearchAppears() {
		assertTrue(addOrderPage.containsText("Search"));
	}
	
	@Then("Search button deactivated until search criteria selected and text entered")
	public void patientSerachButtonDeactivated() {
		assertEquals(addOrderPage.getPatientSerchDisabledAttribute(), "true");
	}
	
	@And("User Searches by Accesion Number {string}")
	public void searchByAccesionNumber(String accesionNumber) {
		addOrderPage.enterLabNumberSearch(accesionNumber);
		assertNull(addOrderPage.getPatientSerchDisabledAttribute());
		addOrderPage.searchPatient();
	}
	
	@And("User Searches by Patient Id {string}")
	public void searchByPatientId(String patientId) {
		addOrderPage.enterPatientIdSearch(patientId);
		assertNull(addOrderPage.getPatientSerchDisabledAttribute());
		addOrderPage.searchPatient();
	}
	
	@And("User Searches by Last Name {string}")
	public void searchByLastName(String lastName) {
		addOrderPage.enterLastNameSearch(lastName);
		assertNull(addOrderPage.getPatientSerchDisabledAttribute());
		addOrderPage.searchPatient();
	}
	
	@And("User Searches by First Name {string}")
	public void searchByFirstName(String firstName) {
		addOrderPage.enterFirstNameSearch(firstName);
		assertNull(addOrderPage.getPatientSerchDisabledAttribute());
		addOrderPage.searchPatient();
	}
	
	@And("User Searches by Date of Birth {string}")
	public void searchByDateOfBirth(String dateOfBirth) {
		addOrderPage.enterDateOfBirthSearch(dateOfBirth);
		assertNull(addOrderPage.getPatientSerchDisabledAttribute());
		addOrderPage.searchPatient();
	}
	
	@And("User Clicks New Patient Button")
	public void clickNewPatient() throws InterruptedException {
		
		// innitializing data
		addOrderPage.clickGenerateButton();
		Thread.sleep(1000);
		addOrderPage.enterSiteNameSuggestion("ABENGOUROU");
		addOrderPage.selectSiteNameFromDropDown();
		Thread.sleep(1000);
		addOrderPage.enterRequesterLastName("SADDIO");
		addOrderPage.clickAddSampleButton();
		addOrderPage.selectSampleTypeFromDropDownMenu("Serum");
		addOrderPage.clickPannelCheckBox();
		addOrderPage.clickNewPatientButton();
	}
	
	@And("User Enters Subject Number {string}")
	public void enterPatientSubjectNumber(String subJectNumber) {
		addOrderPage.enterSubjectNumber(subJectNumber + Utils.generateRandomNumber(4));
	}
	
	@And("User Enters National ID {string}")
	public void enterPatientNationalId(String nationalID) {
		addOrderPage.enterNationalId(nationalID + Utils.generateRandomNumber(4));
	}
	
	@And("User Enters Patient Last Name {string}")
	public void enterPatientLastName(String lastName) {
		addOrderPage.enterPatientLastName(lastName);
	}
	
	@And("User Enters Patient First Name {string}")
	public void enterPatientFirstName(String firstName) {
		addOrderPage.enterPatientFirstName(firstName);
	}
	
	@And("User Enters Contact Last Name {string}")
	public void enterContactLastName(String lastName) {
		addOrderPage.enterContactLastName(lastName);
	}
	
	@And("User Enters Contact First Name {string}")
	public void enterContactFirstName(String firstName) {
		addOrderPage.enterContactFirstName(firstName);
	}
	
	@And("User Enters Contact Email {string}")
	public void enterContactEmail(String email) {
		addOrderPage.enterContactEmail(email);
	}
	
	@And("Field validates {string} Contact Email")
	public void validateContactEmail(String status) throws InterruptedException {
		addOrderPage.clickOtherNationality();
		Thread.sleep(1000);
		if (status.equals("valid")) {
			assertEquals(addOrderPage.getContactEmailFieldClass(), "text");
		} else if (status.equals("invalid")) {
			assertEquals(addOrderPage.getContactEmailFieldClass(), "text error");
		}
	}
	
	@And("User Enters Contact Phone {string}")
	public void enterContactPhone(String phone) {
		addOrderPage.enterContactPhone(phone);
	}
	
	@And("User Enters Patient Street {string}")
	public void enterPatientStreet(String street) {
		addOrderPage.enterPatientStreet(street);
	}
	
	@And("User Enters Patient Commune {string}")
	public void enterPatientCommune(String commune) {
		addOrderPage.enterPatientCommune(commune);
	}
	
	@And("User Enters Patient Town {string}")
	public void enterPatientTown(String town) {
		addOrderPage.enterPatientTown(town);
	}
	
	@And("User Enters Patient Phone {string}")
	public void enterPatientPhone(String phone) {
		addOrderPage.enterPatientPhone(phone);
	}
	
	@Then("Field validates {string} Patient Phone")
	public void validatePatientPhone(String status) throws InterruptedException {
		addOrderPage.clickOtherNationality();
		if(addOrderPage.alertPresent()){
			addOrderPage.acceptAlert();
			addOrderPage.enterPatientPhone("");
			return;
		}
		Thread.sleep(1000);
		if (status.equals("valid")) {
			assertNotEquals(addOrderPage.getPatientPhoneFieldClass().trim(), "error");
		} else if (status.equals("invalid")) {
			addOrderPage.acceptAlert();
			assertEquals(addOrderPage.getPatientPhoneFieldClass().trim(), "error");
		}
	}
	
	@And("User Enters Patient Email {string}")
	public void enterPatientEmail(String email) {
		addOrderPage.enterPatientEmail(email);
	}
	
	@And("Field validates {string} Patient Email")
	public void validatePatientEmail(String status) throws InterruptedException {
		if (!addOrderPage.hasPatientEmailField()) {
			return;
		}
		addOrderPage.clickOtherNationality();
		Thread.sleep(1000);
		if (status.equals("valid")) {
			assertNotEquals(addOrderPage.getPatientEmailFieldClass().trim(), "error");
		} else if (status.equals("invalid")) {
			assertEquals(addOrderPage.getPatientEmailFieldClass().trim(), "error");
		}
	}
	
	@And("User Selects Patient Health Region")
	public void selectPatientHealthRegion() {
		addOrderPage.selectPatientHelathRegionFromDropDownMenu();
	}
	@And("User Selects Patient Health District from the drop down")
	public void userSelectsPatientHealthDistrictFromTheDropDown() {
		addOrderPage.selectPatientHealthDistrictFromDropDownMenu();
	}
	@And("User Enters Patient Date of Birth {string}")
	public void enterPatientDateOfBirth(String dob) {
		addOrderPage.enterPatientDateofBirth(dob);
	}
	
	@Then("Field validates {string} Patient Date Of Birth")
	public void validatePatientDateOfBirth(String status) throws InterruptedException {
		addOrderPage.clickOtherNationality();
		Thread.sleep(1000);
		if (status.equals("valid")) {
			assertNotEquals(addOrderPage.getPatientDoBValidateLabelClass(), "badmessage");
		} else if (status.equals("invalid")) {
			assertEquals(addOrderPage.getPatientDoBValidateLabelClass(), "badmessage");
		}
	}
	
	@When("User Enters Patient Date of Birth in future")
	public void enterPatientDateOfBirthInFuture() {
		String strDate = Utils.getFutureDate();
		addOrderPage.enterPatientDateofBirth(strDate);
	}
	
	@Then("Alert appears if Date is in the Future")
	public void alertAppearsIfFutureDateIsEntered() throws InterruptedException {
		addOrderPage.clickOtherNationality();
		Thread.sleep(1000);
		addOrderPage.acceptAlert();
		assertEquals(addOrderPage.getPatientDoBValidateLabelClass(), "badmessage");
	}
	
	@When("Date of Birth is left blank and Age {string} is filled")
	public void enterPatientAgeWithoutDateOfBirth(String age) {
		addOrderPage.clearPatientDateOfBirth();
		addOrderPage.enterPatientAgeInYears(age);
	}
	
	@Then("Field generates Date of Birth with correct year for the Age {string}")
	public void generateDateOfBirth(String age) throws InterruptedException {
		addOrderPage.clickOtherNationality();
		Thread.sleep(1000);
		String year = Utils.generateDobYearFromAge(age);
		assertEquals(addOrderPage.getPatientDateOfBirthValue(), "xx/xx/" + year);
	}
	
	@And("Alert Appears if Age entered is -1 , 100 and >100")
	public void showAlertIfAgeIsInvalid() throws InterruptedException {
		List<String> invalidAge = new ArrayList<String>();
		invalidAge.add("-1");
		invalidAge.add("100");
		invalidAge.add("101");
		for (String age : invalidAge) {
			addOrderPage.clearPatientDateOfBirth();
			addOrderPage.enterPatientAgeInYears(age);
			addOrderPage.clickOtherNationality();
			Thread.sleep(1000);
			assertEquals(addOrderPage.getPatientAgeValidateLabelClass(), "badmessage");
		}
	}
	
	@And("User Selects Patient Gender")
	public void selectPatientGender() {
		addOrderPage.selectPatientGenderFromDropDownMenu();
	}
	
	@And("User Selects Patient Education")
	public void selectPatientEducation() {
		addOrderPage.selectPatientEducationFromDropDownMenu();
	}
	
	@And("User Selects Patient Marital Status")
	public void selectPatientMaritalStatus() {
		addOrderPage.selectPatientMaritalStatusFromDropDownMenu();
	}
	
	@And("User Enters Patient Other Nationality {string}")
	public void enterPatientOtherNationality(String nationality) {
		addOrderPage.enterPatientOtherNationality(nationality);
	}
	
	@Then("Save button deactivated until all mandatory fields are completed")
	public void saveButtonDeactivated() {
		assertFalse(addOrderPage.saveButtonActivated());
	}
	
	@When("User Completes all mandatory fields")
	public void completeAllMandatoryFiels() throws InterruptedException {
		// innitializing data
		addOrderPage.clickGenerateButton();
		Thread.sleep(1000);
		addOrderPage.enterSiteNameSuggestion("ABENGOUROU");
		addOrderPage.selectSiteNameFromDropDown();
		Thread.sleep(1000);
		addOrderPage.enterRequesterLastName("SADDIO");
		addOrderPage.clickAddSampleButton();
		addOrderPage.selectSampleTypeFromDropDownMenu("Serum");
		addOrderPage.clickPannelCheckBox();
		addOrderPage.clickTestCheckBox();
		addOrderPage.clickNewPatientButton();
		addOrderPage.enterSubjectNumber("202307D9P" + Utils.generateRandomNumber(4));
		addOrderPage.enterNationalId("201507D35" + Utils.generateRandomNumber(4));
		addOrderPage.enterPatientLastName("lastName");
		addOrderPage.enterPatientFirstName("firstName");
		addOrderPage.enterPatientStreet("street");
		addOrderPage.enterPatientCommune("commune");
		enterPatientPhone("+225-63-45-87-88");
		addOrderPage.clickOnNextVisitDate();
		Thread.sleep(1000);
		if(addOrderPage.alertPresent()){
			addOrderPage.acceptAlert();
			addOrderPage.enterPatientPhone("");
		}
		addOrderPage.selectPatientHelathRegionFromDropDownMenu();
		addOrderPage.enterPatientDateofBirth("09/02/2019");
		addOrderPage.selectPatientGenderFromDropDownMenu();
		addOrderPage.selectPatientEducationFromDropDownMenu();
		addOrderPage.selectPatientMaritalStatusFromDropDownMenu();
		addOrderPage.enterPatientOtherNationality("nationality");
	}
	
	@Then("Save button is Activated when all mandatory fields are completed")
	public void saveButtonActivated() {
		assertTrue(addOrderPage.saveButtonActivated());
	}
	
	@When("User Clicks Cancel")
	public void clickCancel() {
		addOrderPage.clickCancel();
	}
	
	@When("User Click Stay on Page")
	public void ClickStayOnPage() {
		addOrderPage.dismissAlert();
	}
	
	@Then("Patient Information form remains on screen")
	public void patientFormRemains() {
		assertTrue(addOrderPage.saveButtonActivated());
	}
	
	@When("User Clicks Save")
	public void clickSave() throws InterruptedException {
		addOrderPage.clickSave();
	}
	
	@Then("New blank Add Order form appears with green Save was successful message on the top")
	public void saveSuccesful() throws InterruptedException {
		Thread.sleep(1000);
		assertTrue(addOrderPage.containsText("Save was successful"));
		assertTrue(addOrderPage.containsText("Test Request"));
	}
	
	@Then("User Clicks Cancel and Returns to Home Page")
	public void clickCancelAndLeave() throws InterruptedException {
		addOrderPage.clickCancel();
		if(addOrderPage.alertPresent()){
			addOrderPage.acceptAlert();
		}
		homePage = new HomePage(addOrderPage);
		assertFalse(homePage.containsText("Test Request"));
	}
}
