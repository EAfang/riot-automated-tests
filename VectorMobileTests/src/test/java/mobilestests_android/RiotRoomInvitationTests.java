package mobilestests_android;

import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import pom_android.RiotLoginAndRegisterPageObjects;
import pom_android.RiotRoomPageObjects;
import pom_android.RiotRoomsListPageObjects;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotRoomInvitationTests extends RiotParentTest{
	String inviterUserAccessToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI1Y2lkIHVzZXJfaWQgPSBAamVhbmdiOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMjFjaWQgbm9uY2UgPSAqaUcwOVFzc2w4PUB0OixkCjAwMmZzaWduYXR1cmUgTz8fR2UypyIHa-uKum3e60I7oxIg087S4LQw4kM_R9kK";  
	String invitedUserAccessToken="MDAxOGxvY2F0aW9uIG1hdHJpeC5vcmcKMDAxM2lkZW50aWZpZXIga2V5CjAwMTBjaWQgZ2VuID0gMQowMDI4Y2lkIHVzZXJfaWQgPSBAcmlvdHVzZXIyOm1hdHJpeC5vcmcKMDAxNmNpZCB0eXBlID0gYWNjZXNzCjAwMjFjaWQgbm9uY2UgPSA2bSNGLHM3dFcwMlA1JlFCCjAwMmZzaWduYXR1cmUgiOIVBX5WRFIpop8OHyq2uN8601NHMqG1e9eg1txWkqwK";
	private String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
	
	/**
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Check that the invitation is closed when accepted.</br>
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android"})
	public void rejectInvitationToARoom() throws IOException, InterruptedException{
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		
		HttpsRequestsToMatrix.leaveRoom(invitedUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(inviterUserAccessToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		ExplicitWait(appiumFactory.getAndroidDriver1(),riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//reject invitation
		riotRoomsList.rejectInvitation(roomName);
		//check that the invite bar is closed
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getAndroidDriver1(),"//android.widget.TextView[@resource-id='im.vector.alpha:id/heading' and @text='INVITES']/../..", false, 5),"The INVITES bar isn't closed after rejecting the invitation");
	}
	
	/**
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Click preview to preview the room.</br>
	 * Check the preview layout.</br>
	 * Cancel the invitation.</br>
	 * Check that the invitation is closed.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android"})
	public void cancelInvitationToARoom() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		
		HttpsRequestsToMatrix.leaveRoom(invitedUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(inviterUserAccessToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		ExplicitWait(appiumFactory.getAndroidDriver1(),riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//preview invitation
		riotRoomsList.previewInvitation(roomName);
		//check the preview layout
		RiotRoomPageObjects newRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		newRoom.checkPreviewRoomLayout(roomName);
		//cancel invitation
		newRoom.cancelInvitationButton.click();
		riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//check that the invite bar is closed
		Assert.assertFalse(waitUntilDisplayed(appiumFactory.getAndroidDriver1(),"//android.widget.TextView[@resource-id='im.vector.alpha:id/heading' and @text='INVITES']/../..", false, 5),"The INVITES bar isn't closed after rejecting the invitation");
	}

	/**
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Click preview to preview the room.</br>
	 * Check the preview layout.</br>
	 * Join the room</br>
	 * Check that the room is opened. </br>
	 * Came back in the list and leave room.</br>
	 * Check that room page is closed and not present in the rooms list.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android"})
	public void acceptInvitationToARoom() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		
		HttpsRequestsToMatrix.leaveRoom(invitedUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(inviterUserAccessToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		ExplicitWait(appiumFactory.getAndroidDriver1(),riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//preview invitation
		riotRoomsList.previewInvitation(roomName);
		//check the preview layout
		RiotRoomPageObjects newRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		newRoom.checkPreviewRoomLayout(roomName);
		newRoom.joinRoomButton.click();
		newRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		newRoom.checkRoomLayout(roomName);
		//come back in roomslist
		newRoom.menuBackButton.click();
		riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		//leave the room
		riotRoomsList.clickOnContextMenuOnRoom(roomName, "Leave Conversation");
		//check that room is closed and isn't in the rooms list page
		newRoom.isDisplayed(false);
		riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		Assert.assertNull(riotRoomsList.getRoomByName(roomName), "Room "+roomName+" is still in the rooms list after leaving it.");
	}
	
	/**
	 * Required : the test user hasn't received any invitation.
	 * Receive an invitation to a room. </br>
	 * Check that riot allows the user to accept or decline the invitation.</br>
	 * Click preview to preview the room.</br>
	 * Check the preview layout.</br>
	 * Join the room</br>
	 * Check that the room is opened. </br>
	 * Came back in the list and leave room from the room menu.</br>
	 * Check that room page is closed and not present in the rooms list.
	 * @throws IOException 
	 * @throws InterruptedException 
	 */
	@Test(groups={"1driver_android"})
	public void acceptInvitationAndLeaveFromMenu() throws IOException, InterruptedException{
		String roomId="!ECguyzzDCnAZarUOSW%3Amatrix.org";
		String invitedUserAdress=Constant.DEFAULT_USERADRESS;
		String roomName="room tests Jean";
		String leavingUserAdress=Constant.DEFAULT_USERADRESS;
		
		HttpsRequestsToMatrix.leaveRoom(invitedUserAccessToken, roomId, leavingUserAdress);
		HttpsRequestsToMatrix.sendInvitationToUser(inviterUserAccessToken, roomId, invitedUserAdress);
		
		RiotRoomsListPageObjects riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		ExplicitWait(appiumFactory.getAndroidDriver1(),riotRoomsList.invitesHeadingLayout);
		Assert.assertTrue(riotRoomsList.invitesHeadingLayout.isDisplayed(), "The invites collapsing bar isn't displayed");
		//TODO check that invites layout is above rooms list
		//check invite layout
		riotRoomsList.checkInvitationLayout(roomName);
		//preview invitation
		riotRoomsList.previewInvitation(roomName);
		//check the preview layout
		RiotRoomPageObjects newRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		newRoom.checkPreviewRoomLayout(roomName);
		newRoom.joinRoomButton.click();
		newRoom = new RiotRoomPageObjects(appiumFactory.getAndroidDriver1());
		newRoom.checkRoomLayout(roomName);
		//leave room from room menu
		newRoom.moreOptionsButton.click();
		newRoom.leaveRoomMenuItem.click();
		//check that room is closed and isn't in the rooms list page
		newRoom.isDisplayed(false);
		riotRoomsList = new RiotRoomsListPageObjects(appiumFactory.getAndroidDriver1());
		Assert.assertNull(riotRoomsList.getRoomByName(roomName), "Room "+roomName+" is still in the rooms list after leaving it.");
	}
	
	/**
	 * Stress test on invitations.
	 * Receive multiple (10) invitations from an other user. </br>
	 * TODO write this test
	 */
	@Test(enabled=false)
	public void receiveMultipleInvitations(){
		
	}
	/**
	 * TODO write this test
	 */
	@Test(enabled=false)
	public void sendInvitationOnRestrictedRoom(){
		
	}
	
	/**
	 * TODO write this test
	 */
	@Test(enabled=false)
	public void leaveRoom(){
		
	}
	/**
	 * Log-in the user if it can't see the login page.
	 * @throws InterruptedException
	 */
	@BeforeMethod
	public void loginForSetup() throws InterruptedException{
		if(false==waitUntilDisplayed(appiumFactory.getAndroidDriver1(),"im.vector.alpha:id/fragment_recents_list", true, 5)){
			System.out.println("Can't access to the rooms list page, none user must be logged. Forcing the log-in.");
			forceWifiOnIfNeeded(appiumFactory.getAndroidDriver1());
			RiotLoginAndRegisterPageObjects loginPage = new RiotLoginAndRegisterPageObjects(appiumFactory.getAndroidDriver1());
			loginPage.emailOrUserNameEditText.setValue(Constant.DEFAULT_USERNAME);
			loginPage.passwordEditText.setValue(Constant.DEFAULT_USERPWD);
			//Forcing the login button to be enabled : this bug should be corrected.
			if(loginPage.loginButton.isEnabled()==false){
				loginPage.registerButton.click();
				loginPage.loginButton.click();
			}
			loginPage.loginButton.click();
		}
	}
}
