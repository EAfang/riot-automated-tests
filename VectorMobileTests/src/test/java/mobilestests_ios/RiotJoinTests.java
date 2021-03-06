package mobilestests_ios;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Method;

import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeGroups;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.esotericsoftware.yamlbeans.YamlException;

import pom_ios.main_tabs.RiotHomePageTabObjects;
import pom_ios.main_tabs.RiotRoomsTabPageObjects;
import utility.Constant;
import utility.HttpsRequestsToMatrix;
import utility.MatrixUtilities;
import utility.RiotParentTest;
import utility.ScreenshotUtility;

@Listeners({ ScreenshotUtility.class })
public class RiotJoinTests extends RiotParentTest{
	private String riotUserADisplayName="riotuser1";
	private String riotUserBDisplayName="riotuser2";
	private String riotUserAAccessToken;
	private String riotUserBAccessToken;
	private String testRoomId;
	
	/**
	 * 1. Create a room with user B </br>
	 * 2. Invite user A. </br>
	 * 3. With user A, join the room from the home page.  </br>
	 * Check that the joined room is opened.  </br>
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_ios","1checkuser"}, priority=0)
	public void joinRoomByIdFromHomePage() throws IOException, InterruptedException{
		String roomName="joinroomtest";
		//1. Create room A from home page & 2. Invite user A.
		createRoomWithByRequestsToMatrix(roomName);
		
		//2. With user A, join the room from the home page.
		RiotHomePageTabObjects homePageA = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		//Check that the joined room is opened. 
		Assert.assertNotNull(homePageA.joinRoom(testRoomId, true), "No room opened in riot after joining it");
	}
	
	/**
	 * 1. Create a room with user B </br>
	 * 2. Invite user A. </br>
	 * 3. With user A, join the room from the rooms page.  </br>
	 * Check that the joined room is opened.  </br>
	 * @throws IOException
	 * @throws InterruptedException
	 */
	@Test(groups={"1driver_ios","1checkuser"}, priority=0)
	public void joinRoomByIdFromRoomsPage() throws IOException, InterruptedException{
		String roomName="joinroomtest";
		//1. Create room A from home page & 2. Invite user A.
		createRoomWithByRequestsToMatrix(roomName);
		
		//2. With user A, join the room from the home page.
		RiotHomePageTabObjects homePageA = new RiotHomePageTabObjects(appiumFactory.getiOsDriver1());
		RiotRoomsTabPageObjects roomsPageA=homePageA.openRoomsTab();
		//Check that the joined room is opened. 
		Assert.assertNotNull(roomsPageA.joinRoom(testRoomId, true), "No room opened in riot after joining it");
	}
	
	/**
	 * Create a room by using https requests to home server.
	 * @param roomName
	 * @throws IOException 
	 */
	private void createRoomWithByRequestsToMatrix(String roomName) throws IOException{
		//1. Create room R with user B.
		riotUserBAccessToken=HttpsRequestsToMatrix.login(riotUserBDisplayName, Constant.DEFAULT_USERPWD);
		testRoomId=HttpsRequestsToMatrix.createRoom(riotUserBAccessToken, roomName);
		//2. Invite user A. 
		HttpsRequestsToMatrix.sendInvitationToUser(riotUserBAccessToken, testRoomId, MatrixUtilities.getMatrixIdFromDisplayName(riotUserADisplayName));
	}
	
	@AfterMethod(alwaysRun=true)
	private void leaveRoomAfterTest(Method m) throws InterruptedException, IOException{
		switch (m.getName()) {
		case "joinRoomByIdFromHomePage":
			leaveAndForgetRoomWithBothUsers();
			break;
		case "joinRoomByIdFromRoomsPage":
			leaveAndForgetRoomWithBothUsers();
			break;
		default:
			break;
		}
	}
	
	@AfterMethod(alwaysRun=true,groups={"1driver_ios"})
	private void restart1ApplicationAfterTest(Method m) throws InterruptedException{
		restartApplication(appiumFactory.getiOsDriver1());
	}
	
	private void leaveAndForgetRoomWithBothUsers() throws IOException{
		//leave room user B
		HttpsRequestsToMatrix.leaveRoom(riotUserBAccessToken, testRoomId);
		//forget room user B
		HttpsRequestsToMatrix.forgetRoom(riotUserBAccessToken, testRoomId);
		
		riotUserAAccessToken=HttpsRequestsToMatrix.login(riotUserADisplayName, Constant.DEFAULT_USERPWD);
		//leave room user A
		HttpsRequestsToMatrix.leaveRoom(riotUserAAccessToken, testRoomId);
		//forget room user A
		HttpsRequestsToMatrix.forgetRoom(riotUserAAccessToken, testRoomId);
	}
	
	/**
	 * Log the good user if not.</br> Secure the test.
	 * @param myDriver
	 * @param username
	 * @param pwd
	 */
	@BeforeGroups("1checkuser")
	private void checkIfUserLogged() throws InterruptedException, FileNotFoundException, YamlException{
		super.checkIfUserLoggedAndHomeServerSetUpIos(appiumFactory.getiOsDriver1(), riotUserADisplayName, Constant.DEFAULT_USERPWD);
	}
}
