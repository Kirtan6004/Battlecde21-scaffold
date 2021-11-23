package Team2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import Team2.robots.EnlightenmentCenter;
import Team2.robots.Muckraker;
import Team2.robots.Slanderer;
import Team2.robots.Politician;
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
	Politician politicianplayer;

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
		reaction = muckraker.dealWithSlanderer(invalid, testplayer.rc);
		assertEquals(-2, reaction);
		//nothing in range
		reaction = muckraker.dealWithSlanderer(empty, testplayer.rc);
		assertEquals(-1, reaction);
		//slanderer is close enough to convert
		when(testplayer.rc.canExpose(near)).thenReturn(true);
		reaction = muckraker.dealWithSlanderer(robots, testplayer.rc);
		assertEquals(1,reaction);
		//Muckracker doesn't have enough energy to convert
		doThrow(new GameActionException(GameActionExceptionType.NOT_ENOUGH_RESOURCE, "")).when(testplayer.rc).expose(near);
		reaction = muckraker.dealWithSlanderer(robots, testplayer.rc);
		assertEquals(-1, reaction);
		//slanderer is too far to convert so follow.
		reaction = muckraker.dealWithSlanderer(robots2, testplayer.rc);
		assertEquals(-2,reaction);
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
			ec.run(rc);
			RobotType type0 = ec.makePol(rc,0, 0,Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type0);
			RobotType type1 = ec.makeSlan(rc, 0, 1, Direction.NORTHEAST);
			assertEquals(RobotType.SLANDERER, type1);
			RobotType type2 = ec.makeMuck(rc, 0, 2, Direction.NORTHEAST);
			assertEquals(RobotType.MUCKRAKER, type2);
			RobotType type3 = ec.makePol(rc, 0, 3, Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type3);
		} else {
			EnlightenmentCenter.run(rc);
			RobotType type0 = ec.makePol(rc, 0, 0, Direction.NORTHEAST);
			assertNull(type0);
			RobotType type1 = ec.makeSlan(rc, 0, 1, Direction.NORTHEAST);
			assertNull(type1);
			RobotType type2 = ec.makeMuck(rc, 0, 2, Direction.NORTHEAST);
			assertNull(type2);
			RobotType type3 = ec.makePol(rc,0,  3, Direction.NORTHEAST);
			assertNull(type3);
		}
	}

	@Test
	public void testPolCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		MapLocation mapLocation = new MapLocation(2,2);
		RobotInfo ec = new RobotInfo(1, Team.A, RobotType.ENLIGHTENMENT_CENTER,200, 100, mapLocation);
		assertEquals(200, ec.getInfluence());
		rc.buildRobot(RobotType.ENLIGHTENMENT_CENTER, Direction.NORTHEAST, 200);
		RobotType ret = EnlightenmentCenter.makePol(rc,0,  3, Direction.NORTHEAST);
		if (rc.getInfluence() >= 100) {
			assertEquals(RobotType.POLITICIAN, ret);
		}
		else {
			assertNull(ret);
		}
	}

	@Test
	public void testSlandCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		RobotType ret = EnlightenmentCenter.makeSlan(rc, 0, 3, Direction.NORTHEAST);
		if (rc.getInfluence() >= 100) {
			assertEquals(RobotType.SLANDERER, ret);
		}
		else {
			assertNull(ret);
		}
	}

	@Test
	public void testMuckCreation() throws GameActionException {
		testplayer = mock(RobotPlayer.class);
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		RobotType ret = EnlightenmentCenter.makeMuck(rc, 0, 3, Direction.NORTHEAST);
		if (rc.getInfluence() >= 100) {
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


		@Test
		public void politicianTest() throws GameActionException
		{

			rc = mock(RobotController.class);
			politicianplayer = mock(Politician.class);
			Team teamB = Team.B;
			Team neutralteam = Team.NEUTRAL;
			int ID = 1;
			int tempradius = -1;
			RobotType robottype = RobotType.POLITICIAN;
			int influence = 111;
			int conviction = 80;
			MapLocation mapLocation = new MapLocation(0,0);
			MapLocation enemylocation = new MapLocation(1,1);
			RobotInfo[] enemies = new RobotInfo[1];
			RobotInfo[] neutral = new RobotInfo[1];
			RobotInfo[] neutralivalid = new RobotInfo[0];
			RobotInfo[] enemyinvalid = new RobotInfo[0];
			neutral[0] = new RobotInfo(2, neutralteam, robottype, 100, 100, enemylocation);
			enemies[0] = new RobotInfo(ID, teamB, robottype, influence, conviction, enemylocation);
			RobotInfo[] attackableEC = new RobotInfo[1];
			RobotInfo[] attackableNonEC = new RobotInfo[1];
			RobotInfo[] Invalidattackable= new RobotInfo[0];
			attackableEC[0] = new RobotInfo(ID, teamB, RobotType.ENLIGHTENMENT_CENTER, influence, conviction, enemylocation);
			boolean canempowerreturnvalue = true;
			when(rc.canEmpower(10)).thenReturn(canempowerreturnvalue);
			when(rc.senseNearbyRobots(10, teamB)).thenReturn(attackableEC);

			//Testing for canattackanenemy method
			int resultCanAttackEnemy = politicianplayer.canattackanenemy(rc, 10, teamB);
			assertEquals(1, resultCanAttackEnemy);
			attackableNonEC[0] = new RobotInfo(ID, teamB, robottype, influence, conviction, enemylocation);
			when(rc.senseNearbyRobots(10, teamB)).thenReturn(attackableNonEC);
			resultCanAttackEnemy = politicianplayer.canattackanenemy(rc, 10, teamB);
			assertEquals(0, resultCanAttackEnemy);
			when(rc.senseNearbyRobots(10, teamB)).thenReturn(Invalidattackable);
			resultCanAttackEnemy = politicianplayer.canattackanenemy(rc, 10, teamB);
			assertEquals(-1, resultCanAttackEnemy);

			//Testing for canempower method
			boolean canempowertrue = true;
			boolean canempowerfalse = false;
			when(rc.canEmpower(10)).thenReturn(canempowertrue);
			int empowerresult = politicianplayer.empower(rc, 10, enemies, neutral);
			assertEquals(1, empowerresult);
			empowerresult = politicianplayer.empower(rc, 10, enemyinvalid, neutralivalid);
			assertEquals(0, empowerresult);
			when(rc.canEmpower(10)).thenReturn(canempowerfalse);
			empowerresult = politicianplayer.empower(rc, 10, enemyinvalid, neutralivalid);
			assertEquals(-1, empowerresult);

			//Testing for pursueNeutralECs
			int turnCount = 10;
//			int turnCount2 = 900;
//			int turncount3 = 0;
			RobotInfo[] validenemy = new RobotInfo[10];
			RobotInfo[] validneutral = new RobotInfo[10];
			RobotInfo[] Invalidenemy = new RobotInfo[500];
			RobotInfo[] Invalidneutral = new RobotInfo[500];
			int pursueNeutralECResult = politicianplayer.pursueNeutralECs(rc, enemies, neutral);
			assertEquals(-1, pursueNeutralECResult);
			pursueNeutralECResult = politicianplayer.pursueNeutralECs(rc, validenemy, validneutral);
			assertEquals(0, pursueNeutralECResult);
			pursueNeutralECResult = politicianplayer.pursueNeutralECs(rc, Invalidenemy, Invalidneutral);
			assertEquals(1, pursueNeutralECResult);

			//Testing run() method
//			//Team opponent = Team.A;
//			int actionradiussquared = 10;
//			when(rc.getTeam().opponent()).thenReturn((teamB));
			//when(rc.getType().actionRadiusSquared).thenReturn(actionradiussquared);
			when(rc.senseNearbyRobots(10, neutralteam)).thenReturn(neutral);
			when(rc.senseNearbyRobots(10, teamB)).thenReturn(attackableNonEC);
//			int turncountcheckinvalid = -10;
//			when(politicianplayer.pursueNeutralECs(rc, attackableNonEC, neutral)).thenReturn(turncountcheckinvalid);
			int turncountcheckvalid = 10;
			when(rc.senseNearbyRobots(10,teamB)).thenReturn(attackableEC);
			when(rc.senseNearbyRobots(10, neutralteam)).thenReturn(attackableNonEC);
//			when(politicianplayer.pursueNeutralECs(rc, value1, value1)).thenReturn(turncountcheckvalid);


		}


}
