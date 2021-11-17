package Team2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import Team2.robots.EnlightenmentCenter;
import Team2.robots.Muckraker;
import Team2.robots.Politician;
import Team2.robots.Slanderer;
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
	RobotController rc;
	Muckraker muckraker;
	EnlightenmentCenter ec;
	Slanderer slandererplayer;

	@Test
	public void runMuckrakerTest() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		testplayer.rc = mock(RobotController.class);
		when(testplayer.rc.getType()).thenReturn(RobotType.MUCKRAKER);
		when(testplayer.rc.getTeam()).thenReturn(Team.A);
		when(testplayer.rc.getLocation()).thenReturn(new MapLocation(0, 0));

		muckrackerSlanderTest();
		muckrackerECTest();
	}

	private void muckrackerSlanderTest() throws GameActionException {
		//testplayer.rc.buildRobot(RobotType.MUCKRAKER,Direction.CENTER,100);
		MapLocation near = new MapLocation(1, 1);
		MapLocation far = new MapLocation(10, 10);
		RobotInfo[] robots = {
				new RobotInfo(2, Team.B, RobotType.SLANDERER, 1, 1, near)
		};
		RobotInfo[] robots2 = {
				new RobotInfo(3, Team.B, RobotType.SLANDERER, 10, 10, far)
		};
		RobotInfo[] invalid = {
				new RobotInfo(1, Team.B, RobotType.POLITICIAN, 10, 10, near)
		};
		RobotInfo[] empty = {};

		int reaction;
		//ignores non slanderers
		reaction = muckraker.dealWithSlanderer(invalid, testplayer.rc);
		assertEquals(-2, reaction);
		//nothing in range
		reaction = muckraker.dealWithSlanderer(empty, testplayer.rc);
		assertEquals(-1, reaction);
		//slanderer is close enough to convert
		when(testplayer.rc.canExpose(near)).thenReturn(true);
		reaction = muckraker.dealWithSlanderer(robots, testplayer.rc);
		assertEquals(1, reaction);
		//Muckracker doesn't have enough energy to convert
		doThrow(new GameActionException(GameActionExceptionType.NOT_ENOUGH_RESOURCE, "")).when(testplayer.rc).expose(near);
		reaction = muckraker.dealWithSlanderer(robots, testplayer.rc);
		assertEquals(-1, reaction);
		//slanderer is too far to convert so follow.
		reaction = muckraker.dealWithSlanderer(robots2, testplayer.rc);
		assertEquals(-2, reaction);
	}

	private void muckrackerECTest() throws GameActionException {
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
		/**
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
		 */
		response = muckraker.dealWithEnlightenmentCenters(neutEC, testplayer.rc);
		assertEquals(-1, response);
	}

	@Test
	public void testRobotCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		rc = mock(RobotController.class);
		ec = mock(EnlightenmentCenter.class);

		if (rc.getInfluence() >= 100) {
			EnlightenmentCenter.run(rc);
			RobotType type0 = EnlightenmentCenter.makeRobots(rc, 0, Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type0);
			RobotType type1 = EnlightenmentCenter.makeRobots(rc, 1, Direction.NORTHEAST);
			assertEquals(RobotType.SLANDERER, type1);
			RobotType type2 = EnlightenmentCenter.makeRobots(rc, 2, Direction.NORTHEAST);
			assertEquals(RobotType.MUCKRAKER, type2);
			RobotType type3 = EnlightenmentCenter.makeRobots(rc, 3, Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type3);
		} else {
			EnlightenmentCenter.run(rc);
			RobotType type0 = EnlightenmentCenter.makeRobots(rc, 0, Direction.NORTHEAST);
			assertNull(type0);
			RobotType type1 = EnlightenmentCenter.makeRobots(rc, 1, Direction.NORTHEAST);
			assertNull(type1);
			RobotType type2 = EnlightenmentCenter.makeRobots(rc, 2, Direction.NORTHEAST);
			assertNull(type2);
			RobotType type3 = EnlightenmentCenter.makeRobots(rc, 3, Direction.NORTHEAST);
			assertNull(type3);
		}
	}

	@Test
	public void testPolCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		MapLocation mapLocation = new MapLocation(2, 2);
		RobotInfo ec = new RobotInfo(1, Team.A, RobotType.ENLIGHTENMENT_CENTER, 200, 100, mapLocation);
		assertEquals(200, ec.getInfluence());
		rc.buildRobot(RobotType.ENLIGHTENMENT_CENTER, Direction.NORTHEAST, 200);
		RobotType ret = EnlightenmentCenter.makePol(rc, 3, Direction.NORTHEAST);
		if (rc.getInfluence() >= 100) {
			assertEquals(RobotType.POLITICIAN, ret);
		} else {
			assertNull(ret);
		}
	}

	@Test
	public void testSlandCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		RobotType ret = EnlightenmentCenter.makeSlan(rc, 3, Direction.NORTHEAST);
		if (rc.getInfluence() >= 100) {
			assertEquals(RobotType.SLANDERER, ret);
		} else {
			assertNull(ret);
		}
	}

	@Test
	public void testMuckCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		RobotType ret = EnlightenmentCenter.makeMuck(rc, 3, Direction.NORTHEAST);
		if (rc.getInfluence() >= 100) {
			assertEquals(RobotType.MUCKRAKER, ret);
		} else {
			assertNull(ret);
		}
	}

	@Test
	public void runSlanderer() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		slandererplayer = mock(Slanderer.class);
		rc = mock(RobotController.class);
		int tempradius = -1;
		Team teamA = Team.A;
		int ID = 1;
		RobotType robottype = RobotType.MUCKRAKER;
		int influence = 50;
		int conviction = 10;
		MapLocation mapLocation = new MapLocation(2, 2);
		MapLocation enemylocation = new MapLocation(10, 10);
		RobotInfo[] enemiespresent = new RobotInfo[1];
		RobotInfo[] enemiesnotpresent = {};
		enemiespresent[0] = new RobotInfo(ID, teamA, robottype, influence, conviction, enemylocation);
		when(rc.senseNearbyRobots(tempradius, teamA)).thenReturn(enemiespresent);
		when(rc.getLocation()).thenReturn(new MapLocation(0, 0));
		int dangerX = ChangeXCoordinates(enemylocation, mapLocation);
		assertEquals(1, dangerX);
		int dangerY = ChangeYCoordinates(enemylocation, mapLocation);
		int result = slandererplayer.WhenOpponentsAreFound(enemiespresent, mapLocation, rc);
		assertEquals(1, dangerY);
		assertEquals(1, result);
		result = slandererplayer.WhenOpponentsAreFound(enemiesnotpresent, mapLocation, rc);
		assertEquals(-1, result);
	}

	private int ChangeXCoordinates(MapLocation enemyloc, MapLocation location) {
		int dangerX = 0;
		if (enemyloc.x > location.x) {
			dangerX--;
		} else {
			dangerX++;
		}
		return 1;
	}

	private int ChangeYCoordinates(MapLocation enemyloc, MapLocation location) {
		int dangerY = 0;
		if (enemyloc.y > location.y) {
			dangerY--;
		} else {
			dangerY++;
		}
		return 1;
	}


	@Test
	public void politicianTest() throws GameActionException {
		RobotInfo[] enemies = new RobotInfo[1];
		RobotInfo[] neutral = new RobotInfo[1];
		int reaction;
		reaction = Politician.pursueNeutralECs(rc, enemies, neutral);
		assertEquals(-1, reaction);
		RobotInfo[] enemies1 = new RobotInfo[800];
		neutral = new RobotInfo[800];
		reaction = Politician.pursueNeutralECs(rc, enemies1, neutral);
		assertEquals(1, reaction);
		enemies = new RobotInfo[12];
		neutral = new RobotInfo[12];
		reaction = Politician.pursueNeutralECs(rc, enemies, neutral);
		assertEquals(0, reaction);
	}


	@Test
	public void empowerTest() throws GameActionException {

		rc = mock(RobotController.class);
		try
		{
			System.out.println("Testing the value of RC in this function:- "+rc.getID());
			System.out.println("The value of the canEmpower is:- "+rc.canEmpower(25));
		}
		catch(Exception e)
		{
			System.out.println("e:- "+e.getMessage());
		}

		//condition -1 (actionRadius is such that canEmpower gives true, enemy>0 and neutral=0)
		RobotInfo[] enemies = new RobotInfo[10];
		RobotInfo[] neutral = new RobotInfo[0];
		int actionRadius = 25;  //Ask for which value of actionRadius does the rc.empower returns true==?
		int reaction = Politician.empower(rc,actionRadius,enemies,neutral);
		assertEquals(1, reaction);

		//Condition -2 (actionRadius is such that canEmpower gives true, enemy=0 and neutral>0)
		RobotInfo[] enemies1 = new RobotInfo[0];
		RobotInfo[] neutral1 = new RobotInfo[10];
//		actionRadius = 100;   //No need for this as already 100 is set
		reaction = Politician.empower(rc, actionRadius, enemies1, neutral1);
		assertEquals(1, reaction);

		//Condition -3 (actionRadius is such that canEmpower will return true but enemy=0 and neutral = 0)
		RobotInfo[] enemies2 = new RobotInfo[0];
		RobotInfo[] neutral2 = new RobotInfo[0];
//		actionRadius = 100;   //No need for this as already 100 is set
		reaction = Politician.empower(rc,actionRadius,enemies2,neutral2);
		assertEquals(0, reaction);

		//Condition-4 (actionRadius is such that canEmpower will return false)
		RobotInfo[] enemies3 = new RobotInfo[10];   //inconsequential
		RobotInfo[] neutral3 = new RobotInfo[10];   //inconsequential
		actionRadius = 10;  //Ask for the value of actionRadius which will give the value of canEmpower as false
		reaction = Politician.empower(rc,actionRadius,enemies3,neutral3);
		assertEquals(-1, reaction);


	}


	/*
				Team enemyTeam = new Team;
	//			public static int canattackanenemy(RobotController rc,  int actionRadius, Team enemyTeam)canattackanenemy
				int tempActionRadius = 1;
				int attackable = Politician.canattackanenemy(rc, tempActionRadius,enemyTeam);
				assertEquals(0, reaction);

	 */
	@Test
	public void canattackanenemytest() throws GameActionException {

		try {
			System.out.println("testplayer" + testplayer.rc);
		}
		catch(NullPointerException e)
		{
			System.err.println("ERROR");
		}
		int reaction=Politician.canattackanenemy(testplayer.rc , 1,Team.NEUTRAL);
		assertEquals(1,reaction);



	}
}
