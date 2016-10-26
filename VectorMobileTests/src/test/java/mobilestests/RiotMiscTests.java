//package mobilestests;
//
//import java.io.File;
//import java.io.IOException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.apache.commons.io.FileUtils;
//import org.openqa.selenium.By;
//import org.openqa.selenium.Dimension;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.OutputType;
//import org.openqa.selenium.ScreenOrientation;
//import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//import org.testng.Assert;
//import org.testng.annotations.AfterClass;
//import org.testng.annotations.AfterTest;
//import org.testng.annotations.BeforeClass;
//import org.testng.annotations.Listeners;
//import org.testng.annotations.Test;
//
//import io.appium.java_client.AppiumDriver;
//import io.appium.java_client.MobileElement;
//import io.appium.java_client.android.AndroidDeviceActionShortcuts;
//import io.appium.java_client.android.AndroidDriver;
//import io.appium.java_client.android.AndroidKeyCode;
//import pom.RiotLegalStuffView;
//import pom.RiotLoginPageObjects;
//import pom.RiotMainPageObjects;
//import pom.RiotRoomPageObjects;
//import pom.RiotSearchFromRoomPageObjects;
//import utility.Constant;
//import utility.DataproviderClass;
//import utility.ScreenshotUtility;
//import utility.AppiumFactory;
//import utility.testUtilities;
//
//@Listeners({ ScreenshotUtility.class })
//public class RiotMiscTests extends testUtilities{
//
//	public static AppiumDriver<MobileElement> driver;
//
//	Dimension size;
//	String destDir;
//	DateFormat dateFormat;
//
//	@BeforeClass
//	public void setUp() throws MalformedURLException{
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		capabilities.setCapability("deviceName","a71011c8");
//		capabilities.setCapability("platformName","Android");
//		capabilities.setCapability("platformVersion", "4.4.2");
//		capabilities.setCapability("appPackage", Constant.package_name);
//		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");
//
//		//Create RemoteWebDriver instance and connect to the Appium server
//		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
//		System.out.println("setUp() done");
//		AppiumFactory appiumFactory = AppiumFactory.getInstance();
//		if(appiumFactory.getAppiumDriver() == null) {
//			driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);                
//		}
//		else{
//			driver = appiumFactory.getAppiumDriver();
//		}
//	}
//
//	@AfterClass
//	public void tearDown(){
//		driver.quit();
//	}
//
//	public void restardDriver() throws MalformedURLException{
//		System.out.println("teardown after test");
//		DesiredCapabilities capabilities = new DesiredCapabilities();
//		driver.quit();
//		capabilities.setCapability("deviceName","a71011c8");
//		capabilities.setCapability("platformName","Android");
//		capabilities.setCapability("platformVersion", "4.4.2");
//
//		capabilities.setCapability("appPackage", Constant.package_name);
//		capabilities.setCapability("appActivity", "im.vector.activity.LoginActivity");
//
//		//Create RemoteWebDriver instance and connect to the Appium server
//		//It will launch the Riot application in Android Device using the configurations specified in Desired Capabilities
//		//driver = (AndroidDriver) new RemoteWebDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
//		driver = new AndroidDriver<MobileElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities); 
//	}
//
//
//	
//
//	@Test
//	public void switchOrientationMode() throws InterruptedException{
//		driver.rotate(ScreenOrientation.LANDSCAPE);//Thread.sleep(2000);
//		MobileElement expelement=(MobileElement) (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.FrameLayout")));
//		scrollWindowDown();
//		driver.rotate(ScreenOrientation.PORTRAIT);//Thread.sleep(2000);
//		expelement=(MobileElement) (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.FrameLayout")));
//		scrollWindowDown();
//		driver.rotate(ScreenOrientation.LANDSCAPE);//Thread.sleep(2000);
//		expelement=(MobileElement) (new WebDriverWait(driver, 15)).until(ExpectedConditions.presenceOfElementLocated(By.className("android.widget.FrameLayout")));
//		scrollWindowDown();
//	}
//	@Test
//	public void scrollRoomsList() throws Exception{
//		Dimension dimensions = driver.manage().window().getSize();
//		Double screenHeightStart = dimensions.getHeight() * 0.5;
//		int scrollStart = screenHeightStart.intValue();
//		System.out.println("s="+scrollStart);
//		Double screenHeightEnd = dimensions.getHeight() * 0.2;
//		int scrollEnd = screenHeightEnd.intValue();
//		driver.swipe(0,scrollStart,0,scrollEnd,2000);
//	}
//
//
//	/**
//	 * Pre-condition: being in the room list.\n
//	 * Detail: Enters in a room and post a message then come back in the main view.
//	 * @throws Exception 
//	 */
//	@Test
//	public void chatInTempRoom() throws Exception{
//		String testRoomName = "temp room";
//		String testMessage1 = "this is an automated test on 1 line";
//		String testMessage2 = "this is an automated test on 2 lines: \n here's the second line";
//
//		RiotMainPageObjects mainPage = new RiotMainPageObjects(driver);
//		mainPage.getRoomByName(testRoomName).click();
//		RiotRoomPageObjects roomPage = new RiotRoomPageObjects(driver);
//		roomPage.messageZoneEditText.sendKeys(testMessage1);
//		roomPage.sendMessageButton.click();
//		roomPage.messageZoneEditText.sendKeys(testMessage2);
//		roomPage.sendMessageButton.click();
//		roomPage.menuBackButton.click();
//	}
//
//	/**
//	 * Open all rooms context menus.
//	 * @throws Exception
//	 */
//	@Test
//	public void openRoomsMenuContexts() throws Exception {
//		RiotMainPageObjects mainPage = new RiotMainPageObjects(driver);
//
//		System.out.println("nb rooms"+ mainPage.roomsList.size());
//		for (WebElement room : mainPage.roomsList) {
//			MobileElement roomSummary=(MobileElement) room.findElement(By.id("im.vector.alpha:id/roomSummaryAdapter_action_image"));
//			roomSummary.click();
//			((AndroidDeviceActionShortcuts) driver).pressKeyCode(AndroidKeyCode.BACK);
//		}
//	}
//
//
//	/**
//	 * Search for a room, open it then come back to the main view.
//	 * @throws Exception
//	 */
//	@Test
//	public void searchForRoom() throws Exception{
//		String roomTestName="temp room";
//		//NEW WAY
//		RiotMainPageObjects mainPage = new RiotMainPageObjects(driver);
//		mainPage.searchButton.click();
//		RiotSearchFromRoomPageObjects searchPage= new RiotSearchFromRoomPageObjects(driver);
//		searchPage.searchEditText.sendKeys(roomTestName);
//		searchPage.getRoomByName(roomTestName).click();
//		RiotRoomPageObjects myRoom=new RiotRoomPageObjects(driver);
//		myRoom.menuBackButton.click();
//	}
//	/**
//	 * Open a room, then quote the last message entered
//	 * @throws Exception
//	 */
//	@Test	
//	public void quoteLastMessage() throws Exception{
//		String testRoomName = "temp room";
//
//		RiotMainPageObjects mainPage = new RiotMainPageObjects(driver);
//		mainPage.getRoomByName(testRoomName).click();
//		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(driver);
//		MobileElement lastMessage = myRoom.lastMessage;
//		lastMessage.click();
//		myRoom.getContextMenuByMessage(lastMessage).click();
//		myRoom.quoteItemFromMenu.click();
//	}
//
//	/**
//	 * Open a room, start a call, end it and come back to the main view.
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void voiceCallFromRoomTest() throws InterruptedException{
//		String testRoomName = "temp room";
//
//		RiotMainPageObjects mainPage = new RiotMainPageObjects(driver);
//		mainPage.getRoomByName(testRoomName).click();
//		RiotRoomPageObjects myRoom = new RiotRoomPageObjects(driver);
//		myRoom.startCallButton.click();
//		myRoom.voiceCallFromMenuButton.click();
//		Thread.sleep(3000);
//		//back button
//		((AndroidDeviceActionShortcuts) driver).pressKeyCode(AndroidKeyCode.BACK);
//		myRoom.endCallButton.click();
//		myRoom.menuBackButton.click();
//	}
//
//
//	@Test
//	public void takeScreenShot() throws Exception{
//		// Set folder name to store screenshots.
//		destDir = "screenshots";
//		// Capture screenshot.
//		File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
//		// Set date format to set It as screenshot file name.
//		dateFormat = new SimpleDateFormat("dd-MMM-yyyy__hh_mm_ssaa");
//		// Create folder under project with name "screenshots" provided to destDir.
//		new File(destDir).mkdirs();
//		// Set file name using current date time.
//		String destFile = dateFormat.format(new Date()) + ".png";
//
//		try {
//			FileUtils.copyFile(scrFile, new File(destDir + "/" + destFile));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//
//	/**
//	 * Test qui va planter intentionnelement.
//	 * @throws IOException
//	 * @throws InterruptedException 
//	 */
//	@Test
//	public void checkUserDisplayName() throws IOException, InterruptedException{
//		RiotMainPageObjects mainPage = new RiotMainPageObjects(driver);
//		mainPage.contextMenuButton.click();
//		Assert.assertEquals(mainPage.displayedUserMain.getText(), "Roger");
//	}
//
//	/**
//	 * Open all the legal stuff webiews
//	 * @throws InterruptedException 
//	 */
//	@Test(dataProvider="SearchProvider",dataProviderClass=DataproviderClass.class)
//	public void openLegalStuffFromPortraitAndLandscapeMode(String items, String expectedTitle) throws InterruptedException{
//		RiotMainPageObjects mainPage = new RiotMainPageObjects(driver);
//		driver.rotate(ScreenOrientation.PORTRAIT);
//		mainPage.contextMenuButton.click();
//		mainPage.getItemMenuByName(items).click();
//		RiotLegalStuffView copyrightPolicyView= new RiotLegalStuffView(driver);
//		Assert.assertTrue(copyrightPolicyView.isPresentTryAndCatch(), "Copyright Policy view isn't open");
//		Assert.assertEquals(copyrightPolicyView.secondTitle.getAttribute("name"), expectedTitle);
//		copyrightPolicyView.okButton.click();
//
//		//open it again and turn the device in landscape mode : the panel should be still displayed
//		mainPage.contextMenuButton.click();Thread.sleep(1000);
//		mainPage.openCopyrightButton.click();
//		copyrightPolicyView= new RiotLegalStuffView(driver);
//		driver.rotate(ScreenOrientation.LANDSCAPE);
//		Thread.sleep(1500);
//		Assert.assertTrue(copyrightPolicyView.isPresentTryAndCatch(), items+" view isn't open");
//		//copyrightPolicyView.okButton.click();
//		driver.rotate(ScreenOrientation.PORTRAIT);
//	}
//
//	@Test
//	public void testWith2Devices(){
//
//	}
//	private void scrollWindowDown(){
//		Dimension dimensions = driver.manage().window().getSize();
//		Double screenHeightStart = dimensions.getHeight() * 0.5;
//		int scrollStart = screenHeightStart.intValue();
//		System.out.println("s="+scrollStart);
//		Double screenHeightEnd = dimensions.getHeight() * 0.2;
//		int scrollEnd = screenHeightEnd.intValue();
//		driver.swipe(0,scrollStart,0,scrollEnd,2000);
//	}
//
//
//	public static AppiumDriver getDriver() {
//		return driver;
//	}
//
//}
