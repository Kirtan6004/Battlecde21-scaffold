package Team2;

import static Team2.RobotPlayer.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Rule;
import static org.mockito.Mockito.when;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;


public class RobotPlayerTest {


	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	RobotPlayer testplayer;

	@Test
	public void runMuckrakerTest() throws GameActionException
	{
		testplayer = mock(RobotPlayer.class);
		testplayer.rc = mock(RobotController.class);
		when(testplayer.rc.getType()).thenReturn(RobotType.MUCKRAKER);
		when(testplayer.rc.getTeam()).thenReturn(Team.A);
		when(testplayer.rc.getLocation()).thenReturn(new MapLocation(0,0));

		muckrackerSlanderTest();
		muckrackerECTest();
	}

	private void muckrackerSlanderTest() throws GameActionException
	{
		//testplayer.rc.buildRobot(RobotType.MUCKRAKER,Direction.CENTER,100);
		MapLocation near = new MapLocation(1, 1);
		MapLocation far = new MapLocation(10,10);
		RobotInfo[] robots = {
				new RobotInfo(2, Team.B, RobotType.SLANDERER,1,1, near)
		};
		RobotInfo[] robots2 = {
				new RobotInfo(3, Team.B, RobotType.SLANDERER,10,10, far)
		};
		RobotInfo[] invalid = {
				new RobotInfo(1, Team.B, RobotType.POLITICIAN, 10, 10, near)
		};
		RobotInfo[] empty = {};

		int reaction;
		//ignores non slanderers
		reaction = testplayer.dealWithSlanderer(invalid);
		assertEquals(-1, reaction);
		//nothing in range
		reaction = testplayer.dealWithSlanderer(empty);
		assertEquals(-1, reaction);
		//slanderer is close enough to convert
		when(testplayer.rc.canExpose(near)).thenReturn(true);
		reaction = testplayer.dealWithSlanderer(robots);
		assertEquals(1,reaction);
		//slanderer is too far to convert so follow.
		reaction = testplayer.dealWithSlanderer(robots2);
		assertEquals(2,reaction);
	}
	RobotController rc;
	//RobotPlayer testplayer;


	@Test
	public void testRobotCreation() throws GameActionException {
		rc = mock(RobotController.class);
		if (lastRobot == 3)
		{
			assertEquals(RobotType.POLITICIAN, rc.getType());
		}
		if (lastRobot == 1)
		{
			assertEquals(RobotType.SLANDERER, rc.getType());
		}
		if (lastRobot == 2)
		{
			assertEquals(RobotType.MUCKRAKER, rc.getType());
		}
	}

	@Test
	public void runTest() throws GameActionException {
		rc = mock(RobotController.class);
		if (rc.getType() == RobotType.ENLIGHTENMENT_CENTER)
		{
			assertEquals(RobotType.ENLIGHTENMENT_CENTER, rc.getType());
		}
		if (rc.getType() == RobotType.POLITICIAN)
		{
			assertEquals(RobotType.POLITICIAN, rc.getType());
		}
		if (rc.getType() == RobotType.SLANDERER)
		{
			assertEquals(RobotType.SLANDERER, rc.getType());
		}
		if (rc.getType() == RobotType.MUCKRAKER) {
			assertEquals(RobotType.MUCKRAKER, rc.getType());
		}
	}



	@Test
	public void runSlanderer() throws GameActionException
	{
		testplayer = mock(RobotPlayer.class);
		rc = mock(RobotController.class);
		int tempradius = -1;
		Team teamA = Team.A;
		int ID = 1;
		RobotType robottype = RobotType.MUCKRAKER;
		int influence = 50;
		int conviction = 10;
		MapLocation mapLocation = new MapLocation(2,2);
		MapLocation enemylocation = new MapLocation(10,10);
		RobotInfo[] enemiespresent = new RobotInfo[1];
		RobotInfo[] enemiesnotpresent = {};
		enemiespresent[0] = new RobotInfo(ID, teamA, robottype, influence, conviction, enemylocation);
		when(rc.senseNearbyRobots( tempradius, teamA)).thenReturn(enemiespresent);
		when(rc.getLocation()).thenReturn(new MapLocation(0, 0));
		int result = testplayer.WhenOpponentsAreFound(enemiespresent, mapLocation, rc);
		assertEquals(1, result);
		result = testplayer.WhenOpponentsAreFound(enemiesnotpresent, mapLocation, rc);
		assertEquals(-1, result);
	}

	private void muckrackerECTest() throws GameActionException
	{
		MapLocation near = new MapLocation(3, 3);
		RobotInfo[] neutEC = {
				new RobotInfo(1, Team.NEUTRAL, RobotType.ENLIGHTENMENT_CENTER, 1, 1, near)
		};
		RobotInfo[] nonNeutEC = {
				new RobotInfo(2, Team.A, RobotType.ENLIGHTENMENT_CENTER, 1, 1, near),
				new RobotInfo(3, Team.B, RobotType.ENLIGHTENMENT_CENTER, 1, 1, near)
		};
		RobotInfo[] invalid = {
				new RobotInfo(1, Team.B, RobotType.POLITICIAN, 10, 10, near)
		};
		RobotInfo[] empty = {};

		int response;
		//invalid type
		response = testplayer.dealWithEnlightenmentCenters(invalid);
		assertEquals(-1, response);
		//nothing in range
		response = testplayer.dealWithEnlightenmentCenters(empty);
		assertEquals(-1, response);
		//ignore non neutral ECs
		response = testplayer.dealWithEnlightenmentCenters(nonNeutEC);
		assertEquals(2,response);
		//add neutral EC
		response = testplayer.dealWithEnlightenmentCenters(neutEC);
		assertEquals(1, response);
	}
	


	@Test
	public void createPoliticianRobot() throws GameActionException {
		rc = mock(RobotController.class);
		if(this.lastRobot.exists == true)
		{
			if (lastRobot == 3)
			{
				assertEquals(RobotType.POLITICIAN, rc.getType());
			}
			if (lastRobot == 1)
			{
				assertEquals(RobotType.SLANDERER, rc.getType());
			}
			if (lastRobot == 2)
			{
				assertEquals(RobotType.MUCKRAKER, rc.getType());
			}
		}

	}
		@Test
		public void politicianBid() {
			rc = mock(RobotController.class);
			if (afterbid > 90) {
				assertEquals(90, afterbid);

			} else {
				assertEquals(after, infBeforeBid);
			}
		}

		@Test
		public void politicianTest() throws GameActionException
		{
			rc = mock(RobotController.class);
			int tempradius = -1;
			Team team = Team.B;
			int ID = 1;
			RobotType robottype = RobotType.POLITICIAN;
			int influence = 111;
			int conviction = 80;
			MapLocation mapLocation = new MapLocation(0,0);
			MapLocation enemylocation = new MapLocation(1,1);
			RobotInfo[] enemies = new RobotInfo[1];
			enemies[0] = new RobotInfo(ID, team, robottype, influence, conviction, enemylocation);
			when(rc.senseNearbyRobots( tempradius, team)).thenReturn(enemies);
			MapLocation maplocation = rc.getLocation();
			when(rc.getLocation()).thenReturn(new MapLocation(0, 0));
			assertTrue(enemies.length > 0);
			int dangerX = 0;
			int dangerY = 0;
			for (RobotInfo r : enemies)
			{
				assertTrue(r.getType() == RobotType.POLITICIAN);
				int temp = 0;
				assertEquals(r.getLocation(), enemylocation);
				assertTrue(r.getLocation().x > mapLocation.x);
				int newdangerX = dangerX-1;
				assertEquals(-1, dangerX-1);
				assertFalse(r.getLocation().x < mapLocation.x);
				newdangerX = dangerX+1;
				assertEquals(1, dangerX+1);
			}
		}
	}

