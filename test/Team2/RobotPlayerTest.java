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

	@Test
	public void testRobotCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		testplayer.rc = mock(RobotController.class);

		if (testplayer.rc.getInfluence() >= 100) {
			testplayer.runEnlightenmentCenter();
			RobotType type0 = testplayer.makeRobots(0, Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type0);
			RobotType type1 = testplayer.makeRobots(1, Direction.NORTHEAST);
			assertEquals(RobotType.SLANDERER, type1);
			RobotType type2 = testplayer.makeRobots(2, Direction.NORTHEAST);
			assertEquals(RobotType.MUCKRAKER, type2);
			RobotType type3 = testplayer.makeRobots(3, Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type3);
		} else {
			testplayer.runEnlightenmentCenter();
			RobotType type0 = testplayer.makeRobots(0, Direction.NORTHEAST);
			assertNull(type0);
			RobotType type1 = testplayer.makeRobots(1, Direction.NORTHEAST);
			assertNull(type1);
			RobotType type2 = testplayer.makeRobots(2, Direction.NORTHEAST);
			assertNull(type2);
			RobotType type3 = testplayer.makeRobots(3, Direction.NORTHEAST);
			assertNull(type3);
		}
	}

	@Test
	public void testPolCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		testplayer.rc = mock(RobotController.class);
		//RobotInfo ri = mock(RobotInfo.class);
		MapLocation mapLocation = new MapLocation(2,2);
		RobotInfo ec = new RobotInfo(1, Team.A, RobotType.ENLIGHTENMENT_CENTER,200, 100, mapLocation);
		assertEquals(200, ec.getInfluence());
		testplayer.rc.buildRobot(RobotType.ENLIGHTENMENT_CENTER, Direction.NORTHEAST, 200);
		RobotType ret = testplayer.makePol(3, Direction.NORTHEAST);
		if (testplayer.rc.getInfluence() >= 100) {
			assertEquals(RobotType.POLITICIAN, ret);
		}
		else {
			assertNull(ret);
		}
	}

	@Test
	public void testSlandCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		testplayer.rc = mock(RobotController.class);
		RobotType ret = testplayer.makeSlan(3, Direction.NORTHEAST);
		if (testplayer.rc.getInfluence() >= 100) {
			assertEquals(RobotType.SLANDERER, ret);
		}
		else {
			assertNull(ret);
		}
	}

	@Test
	public void testMuckCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		testplayer.rc = mock(RobotController.class);
		RobotType ret = testplayer.makeMuck(3, Direction.NORTHEAST);
		if (testplayer.rc.getInfluence() >= 100) {
			assertEquals(RobotType.MUCKRAKER, ret);
		}
		else {
			assertNull(ret);
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
		public void politicianTest() throws GameActionException
		{
			testplayer = mock(RobotPlayer.class);
			testplayer.rc = mock(RobotController.class);
			when(testplayer.rc.getType()).thenReturn(RobotType.POLITICIAN);
			when(testplayer.rc.getTeam()).thenReturn(Team.A);
			when(testplayer.rc.getLocation()).thenReturn(new MapLocation(0,0));

			rc = mock(RobotController.class);
			Team teamB = Team.B;
			int ID = 1;
			int tempradius = -1;
			RobotType robottype = RobotType.POLITICIAN;
			int influence = 111;
			int conviction = 80;
			MapLocation mapLocation = new MapLocation(0,0);
			MapLocation enemylocation = new MapLocation(1,1);
			RobotInfo[] enemies = new RobotInfo[1];
			enemies[0] = new RobotInfo(ID, teamB, robottype, influence, conviction, enemylocation);
			when(rc.senseNearbyRobots( tempradius, teamB)).thenReturn(enemies);
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

			if (turnCount <= 12) {
					directionality = Direction.EAST;
				}
			 else if (turnCount > 800) {
				directionality = Direction.WEST;
			}

			if(turnCount >12 && turnCount <=800)
			{
				directionality = Direction.CENTER;
				if(teamB.isPlayer())
				{
					return;
				}
			}
		
		}
}
