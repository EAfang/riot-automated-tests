package pom_android;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import utility.TestUtilities;

public class RiotRoomsListPageObjects extends TestUtilities {
	private AndroidDriver<MobileElement> driver;

	public RiotRoomsListPageObjects(AppiumDriver<MobileElement> myDriver) throws InterruptedException{
		driver=(AndroidDriver<MobileElement>) myDriver;
		PageFactory.initElements(new AppiumFieldDecorator(myDriver), this);
		//Thread.sleep(2000);
		//ExplicitWait(driver,this.roomsExpandableListView);
		try {
			waitUntilDisplayed((AndroidDriver<MobileElement>) driver,"im.vector.alpha:id/fragment_recents_list", true, 10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/*
	 * INVITES
	 */
	@AndroidFindBy(xpath="//android.widget.TextView[@resource-id='im.vector.alpha:id/heading' and @text='INVITES']/../..")//invites collapsing bar
	public MobileElement invitesHeadingLayout;

	public MobileElement getInvitationLayoutByName(String roomName) throws InterruptedException{
		if (waitUntilDisplayed(driver, "//android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_roomName' and@text='"+roomName+"']/../../../..", true, 20))
		{return driver.findElementByXPath("//android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_roomName' and@text='"+roomName+"']/../../../..");}
		else{
			return null;
		}
	}

	/**
	 * Check some properties on a room invitation layout.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void checkInvitationLayout(String roomName) throws InterruptedException{
		MobileElement roomInvitationLayout=getInvitationLayoutByName(roomName);
		//room's avatar is not empty
		org.openqa.selenium.Dimension roomAvatar=roomInvitationLayout.findElementById("im.vector.alpha:id/room_avatar_image_view").getSize();
		Assert.assertTrue(roomAvatar.height!=0 && roomAvatar.width!=0, "Riot logo has null dimension");
		//System.out.println(roomInvitationLayout.findElementById("im.vector.alpha:id/roomSummaryAdapter_roomName").getText());
		//! warning is present
		Assert.assertEquals(roomInvitationLayout.findElementById("im.vector.alpha:id/roomSummaryAdapter_unread_count").getText(), "!", "Unread count on the invitation layout isn't present");
		//last message received is not empty
		Assert.assertFalse(roomInvitationLayout.findElementById("im.vector.alpha:id/roomSummaryAdapter_roomMessage").getText().isEmpty(), "Last received message is empty");
		//the 2 buttons are enabled
		Assert.assertEquals(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_reject_button").getText(), "Reject");
		Assert.assertTrue(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_reject_button").isEnabled(), "Reject button isn't enabled");
		Assert.assertEquals(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_preview_button").getText(), "Preview");
		Assert.assertTrue(roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_preview_button").isEnabled(), "Preview button isn't enabled");
	}

	/**
	 * Hit the "preview" button on an invitation.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void previewInvitation(String roomName) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/recents_groups_invitation_group", true, 30);
		try {
			MobileElement roomInvitationLayout=getInvitationLayoutByName(roomName);
			roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_preview_button").click();
		} catch (Exception e) {
			Assert.fail("No invitation found with room "+roomName);
		}

	}

	/**
	 * Hit the "reject" button on an invitation.
	 * @param roomName
	 * @throws InterruptedException 
	 */
	public void rejectInvitation(String roomName) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/recents_groups_invitation_group", true, 30);
		try {
			MobileElement roomInvitationLayout=getInvitationLayoutByName(roomName);
			roomInvitationLayout.findElementById("im.vector.alpha:id/recents_invite_reject_button").click();
		} catch (Exception e) {
			Assert.fail("No invitation found with room "+roomName);
		}
	}

	/*
	 * ROOMS
	 */
	@AndroidFindBy(id="im.vector.alpha:id/fragment_recents_list")//expandable view containing all the rooms lists (favorites, rooms, low priority, etc).
	public MobileElement roomsExpandableListView;
	/**
	 * Contains only the rooms.
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/android.widget.LinearLayout")
	public List<WebElement> roomsList;
	/**
	 * Contains rooms and categories (favorites, people, rooms ...)
	 */
	@AndroidFindBy(xpath="//android.widget.ExpandableListView[@resource-id='im.vector.alpha:id/fragment_recents_list']/*")
	public List<MobileElement> roomsAndCategoriesList;

	/**
	 * Check that room is in a room category (favorites, people, rooms, etc). </br>
	 * TODO maybe scroll to the end of the list to check the room ?
	 * @param roomNameTest
	 * @param category
	 */
	public Boolean checkRoomInCategory(String roomNameTest, String category) {
		Boolean categoryFound=false, roomFound=false, otherCategoryFound=false;int indexCategory=0;
		System.out.println("Looking for room "+roomNameTest+" in the category "+category);
		//looking for the category
		int index=0;
		do {
			//is the mobilelement a category ?
			if(roomsAndCategoriesList.get(index).getTagName().equals("android.widget.RelativeLayout")){
				//is this the searched category ?
				if(roomsAndCategoriesList.get(index).findElementById("im.vector.alpha:id/heading").getText().equals(category)){
					indexCategory=index;categoryFound=true;
				}
			}
			index++;
		} while (index<=roomsAndCategoriesList.size()-1 && categoryFound==false);
		if(categoryFound==false)return false;
		//looking for room name
		index=indexCategory+1;
		do {
			//is the mobilelement a room ?
			if(roomsAndCategoriesList.get(index).getTagName().equals("android.widget.LinearLayout")){
				if(roomsAndCategoriesList.get(index).findElementById("im.vector.alpha:id/roomSummaryAdapter_roomName").getText().equals(roomNameTest)){
					roomFound=true;
				}
			}else if(roomsAndCategoriesList.get(index).getTagName().equals("android.widget.RelativeLayout")){//is this a category ?
				otherCategoryFound=true;
			}
			index++;
		} while (index<=roomsAndCategoriesList.size()-1 && roomFound==false && otherCategoryFound==false);
		return roomFound;
	}

	/**
	 * Return a room as a MobileElement. </br>
	 * Return null if not found.
	 * @param myRommName
	 * @return
	 */
	public MobileElement getRoomByName(String myRommName){
		try {
			return (MobileElement) driver.findElementByXPath("//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRommName+"']/../../..");	
		} catch (Exception e) {
			return null;
		}

	}

	/**
	 * Click on the context menu on a room, then choose one of the item : Notifications, Favourite, De-prioritize, Leave Conversation
	 * @param roomName
	 * @param item
	 * @throws InterruptedException 
	 */
	public void clickOnContextMenuOnRoom(String roomName, String item) throws InterruptedException{
		//open contxt menu on a room item
		MobileElement roomItem=getRoomByName(roomName);
		if(null!=roomItem){
			roomItem.findElementById("im.vector.alpha:id/roomSummaryAdapter_action_click_area").click();
			//hit the item on the options
			driver.findElementByXPath("//android.widget.ListView//android.widget.TextView[@text='"+item+"']/../..").click();
			Assert.assertFalse(waitUntilDisplayed(driver,"//android.widget.ListView[count(android.widget.LinearLayout)=4]", false, 0), "Option windows isn't closed");	
		}else{
			System.out.println("No room found with name "+roomName+", impossible to click on item "+item+" on context menu.");
		}
		
	}

	/**
	 * Leave a room from the rooms list.
	 * @param roomName
	 * @throws InterruptedException
	 */
	public void leaveRoom(String roomName) throws InterruptedException{
		clickOnContextMenuOnRoom(roomName, "Leave Conversation");
		alertDialogButton2.click();
	}

	/**
	 * Send back the badge number of a room by his name.</br>
	 * Return null if no badge.
	 * @param myRommName
	 * @return
	 */
	public Integer getBadgeNumberByRoomName(String myRommName){
		try {
			String badgeNumber= driver.findElement(By.xpath("//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRommName+"']/../android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_unread_count']")).getText();
			return Integer.parseInt(badgeNumber);
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * Return the last received message of a room by his name.</br>
	 * Message can be text or event. </br>
	 * Return null if no message found.
	 * @param myRoomName
	 * @return
	 */
	public String getReceivedMessageByRoomName(String myRoomName){
		try {
			String messageWithUsername =driver.findElement(By.xpath("//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRoomName+"']/../..//android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_roomMessage']")).getText();
			if(messageWithUsername.indexOf(":")!=-1){
				return messageWithUsername.substring(messageWithUsername.indexOf(":")+2, messageWithUsername.length());
			}else{
				return messageWithUsername;
			}

		} catch (Exception e) {
			return null;
		}
	}
	public Boolean isDirectMessageByRoomName(String myRommName){
		try {
			if(getRoomByName(myRommName).findElementById("im.vector.alpha:id/room_avatar_direct_chat_icon")!=null)return true;

		} catch (Exception e) {
			return false;
		}
		return false;
	}

	/**
	 * Wait until badge of the room is incremented.
	 * @param myRommName
	 * @param currentBadge
	 * @throws InterruptedException
	 */
	public void waitForRoomToReceiveNewMessage(String myRommName, int currentBadge) throws InterruptedException{
		waitUntilDisplayed(driver,"//android.widget.ExpandableListView//android.widget.TextView[@text='"+myRommName+"']/../android.widget.TextView[@resource-id='im.vector.alpha:id/roomSummaryAdapter_unread_count' and @text='"+Integer.sum(currentBadge, 1)+"']", true, 5);
	}

	/*
	 * TOOL BAR 
	 */
	@AndroidFindBy(id="im.vector.alpha:id/home_toolbar")
	public MobileElement toolBarView;
	@AndroidFindBy(xpath="//android.view.View/android.widget.ImageButton[@content-desc='Navigate up']")//Menu button (opens the lateral menu)
	public MobileElement contextMenuButton;
	@AndroidFindBy(id="im.vector.alpha:id/ic_action_search_room") //Open the search for rooms page
	public MobileElement searchButton;

	/*
	 * LATERAL MENU
	 */
	//Logout button

	@AndroidFindBy(id="im.vector.alpha:id/navigation_view")
	public MobileElement lateralMenuLayout;
	@AndroidFindBy(xpath="//android.widget.FrameLayout//android.widget.CheckedTextView[@text='Settings']")////android.widget.CheckedTextView[@text='Logout']
	public MobileElement settingsButton;
	@AndroidFindBy(xpath="//android.widget.FrameLayout//android.widget.CheckedTextView[@text='Sign out']")////android.widget.CheckedTextView[@text='Logout']
	public MobileElement signOutButton;
	@AndroidFindBy(id="im.vector.alpha:id/home_menu_main_displayname")
	public MobileElement displayedUserMain;

	@AndroidFindBy(xpath="//android.widget.FrameLayout//android.widget.CheckedTextView[@text='Copyright']")//copyright button
	public MobileElement openCopyrightButton;

	/*
	 * ALERT DIALOG
	 */
	@AndroidFindBy(id="android:id/parentPanel")
	public MobileElement signOutPopUpPanel;
	@AndroidFindBy(id="android:id/message")
	public MobileElement signOutTitleTextView;
	@AndroidFindBy(id="android:id/button1")
	public MobileElement alertDialogButton2;
	@AndroidFindBy(id="android:id/button2")
	public MobileElement alertDialogButton1;



	/**
	 * Get an item from the lateral menu.
	 * @param name
	 * @return
	 */
	public MobileElement getItemMenuByName(String name){
		return lateralMenuLayout.findElementByName(name);
	}

	/**
	 * '+' button at the bottom.</br> Open a listview with 'start chat' and 'create room'.
	 */
	@AndroidFindBy(id="im.vector.alpha:id/listView_create_room_view")
	public MobileElement plusRoomButton;

	/*
	 * START CHAT / CREATE ROOM
	 */
	@AndroidFindBy(id="android:id/select_dialog_listview")
	public MobileElement selectRoomTypeCreationListView;
	@AndroidFindBy(xpath="//android.widget.CheckedTextView[@text='Start chat']")
	public MobileElement startChatCheckedTextView;
	@AndroidFindBy(xpath="//android.widget.CheckedTextView[@text='Create room']")
	public MobileElement createRoomCheckedTextView;
	@AndroidFindBy(xpath="//android.widget.Button[@text='Cancel']")
	public MobileElement cancelCreationListButton;
	@AndroidFindBy(xpath="//android.widget.Button[@text='OK']")
	public MobileElement okCreationListButton;

	/**
	 * Create a new room : click on plus button, then create room item. </br>
	 * Return a RiotRoomPageObjects object.
	 * @return
	 */
	public RiotRoomPageObjects createRoom(){
		plusRoomButton.click();
		createRoomCheckedTextView.click();
		return new RiotRoomPageObjects(driver);
	}

	/**
	 * Log-out from Riot with the lateral menu.
	 */
	public void logOut(){
		this.contextMenuButton.click();
		this.signOutButton.click();
		//verifies that sign out pop up is displayed and correct
		Assert.assertEquals(signOutTitleTextView.getText(), "For security, logging out will delete any end-to-end encryption keys making previous encrypted chat history unreadable if you log back in.\nSelect export to backup them before signing out.");
		Assert.assertTrue(alertDialogButton1.isDisplayed(),"Cancel button isn't displayed");
		Assert.assertTrue(alertDialogButton2.isDisplayed(),"OK button isn't displayed");
		alertDialogButton2.click();
	}

	/**
	 * Log out from the rooms list, log in with the parameters.</br>
	 * Return a RiotRoomsListPageObjects POM.</br> Can be used to renew the encryption keys.
	 * @param username
	 * @param pwd
	 * @return new RiotRoomsListPageObjects
	 */
	public RiotRoomsListPageObjects logOutAndLogin(String username, String password) throws InterruptedException{
		this.logOut();
		RiotLoginAndRegisterPageObjects loginPage= new RiotLoginAndRegisterPageObjects(driver);
		loginPage.fillLoginForm(username, null,password);
		return new RiotRoomsListPageObjects(driver);
	}
	public RiotSettingsPageObjects openRiotSettingsFromLateralMenu() throws InterruptedException{
		this.contextMenuButton.click();
		this.settingsButton.click();
		return new RiotSettingsPageObjects(driver);
	}

	/**
	 * Wait until the spinner isn't displayed anymore.
	 * @param secondsToWait
	 * @throws InterruptedException 
	 */
	public void waitUntilSpinnerDone(int secondsToWait) throws InterruptedException{
		waitUntilDisplayed(driver, "im.vector.alpha:id/listView_spinner", false, secondsToWait);
	}
}
