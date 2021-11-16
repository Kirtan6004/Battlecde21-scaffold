package Team2;

import static Team2.RobotPlayer.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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
	Slanderer slandererplayer;

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

		if (testplayer.rc.getInfluence() >= 50) {
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
			assertEquals(null, type0);
			RobotType type1 = testplayer.makeRobots(1, Direction.NORTHEAST);
			assertEquals(null, type1);
			RobotType type2 = testplayer.makeRobots(2, Direction.NORTHEAST);
			assertEquals(null, type2);
			RobotType type3 = testplayer.makeRobots(3, Direction.NORTHEAST);
			assertEquals(null, type3);
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
		slandererplayer = mock(Slanderer.class);
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
		int dangerX = ChangeXCoordinates(enemylocation, mapLocation);
		assertEquals(1, dangerX);
		int dangerY = ChangeYCoordinates(enemylocation, mapLocation);
		int result = slandererplayer.WhenOpponentsAreFound(enemiespresent, mapLocation, rc);
		assertEquals(1, dangerY);
		assertEquals(1, result);
		result = slandererplayer.WhenOpponentsAreFound(enemiesnotpresent, mapLocation, rc);
		assertEquals(-1, result);
	}
	private int ChangeXCoordinates(MapLocation enemyloc, MapLocation location)
	{
		int dangerX = 0;
		if(enemyloc.x > location.x)
		{
			dangerX--;
		}
		else
		{
			dangerX++;
		}
		return 1;
	}
	private int ChangeYCoordinates(MapLocation enemyloc, MapLocation location)
	{
		int dangerY = 0;
		if (enemyloc.y > location.y)
		{
			dangerY--;
		}
		else
		{
			dangerY++;
		}
		return 1;
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
}
