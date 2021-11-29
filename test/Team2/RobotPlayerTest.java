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
		when(testplayer.rc.getType()).thenReturn(null);
		when(testplayer.rc.getTeam()).thenReturn(Team.A);
		when(testplayer.rc.getLocation()).thenReturn(new MapLocation(0,0));
		testMuckRun(testplayer);
		muckrackerSlanderTest();
		muckrackerECTest();
	}

	/**test the run(rc) function specifically*/
	private void testMuckRun(RobotPlayer rp) throws GameActionException
	{
		MapLocation far = new MapLocation(10,10);
		RobotInfo[] invalid = {
				  new RobotInfo(1, Team.B, RobotType.POLITICIAN, 10, 10, far)
		};
		when(rp.rc.senseNearbyRobots(RobotType.MUCKRAKER.detectionRadiusSquared)).thenReturn(invalid);
		when(rp.rc.getType()).thenReturn(RobotType.MUCKRAKER);
		Muckraker.run(rp.rc);
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
		RobotInfo[] friendly = {
				  new RobotInfo(4, Team.A, RobotType.SLANDERER,10,10, far)
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
		//friendly units
		reaction = muckraker.dealWithSlanderer(friendly, testplayer.rc);
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
	public void ecFtMakeRobots() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		Direction d = Direction.NORTHEAST;
		ec.ftMakeRobots(rc, d, 0, true, true);
		ec.ftMakeRobots(rc, d, 1, true, true);
		ec.ftMakeMuck(rc, d, 20, 0, true);
	}

	@Test
	public void ecSdMakeRobots() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		Direction d = Direction.NORTHEAST;
		ec.sdMakeRobots(rc, d, true, true, 2);
		ec.sdMakeRobots(rc, d, true, true, 10);
		ec.sdMakeSlan(rc, d, true, 5);
	}

	@Test
	public void ecLtMakeRobotsTest() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		ec.ltMakeRobots(rc, Direction.NORTHEAST, 200, true, true);
		ec.ltMakePol(rc, Direction.NORTHEAST, 200, true);
	}

	@Test
	public void ecBidTest() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		ec.ecBid(rc, 50, 100);
	}

	@Test
	public void ecMakeRobotsTest() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		Direction d = Direction.NORTHEAST;
		assertEquals(RobotType.SLANDERER, ec.makeSlan(rc, 0, 2, d, true));
		assertEquals(RobotType.MUCKRAKER, ec.makeMuck(rc, 0, 1, d, true));
		assertEquals(RobotType.POLITICIAN, ec.makePol(rc, 0, 3, d, true));
	}

	@Test
	public void ecFlagTest() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		RobotType r = RobotType.POLITICIAN;
		assertEquals(r, ec.makeFlag(rc, 0, Direction.NORTHEAST, r, 3));
		RobotType r2 = RobotType.SLANDERER;
		assertEquals(r2, ec.makeFlag(rc, 0, Direction.NORTHEAST, r2, 2));
		ec.justMakeFlag(rc, 0, true, 2);
	}

	@Test
	public void ecThirdsTest() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		ec.firstThird(rc, 51);
		ec.secondThird(rc, 110);
		ec.lastThird(rc, 95);
	}

	@Test
	public void ecRunTest() throws GameActionException
	{
		ec = mock(EnlightenmentCenter.class);
		rc = mock(RobotController.class);
		ec.run(rc, 300);
		ec.run(rc, 700);
		ec.run(rc, 1300);
	}

	@Test
	public void runSlanderer() throws GameActionException
	{
		testplayer = mock(RobotPlayer.class);
		slandererplayer = mock(Slanderer.class);
		rc = mock(RobotController.class);
		Team teamA = Team.A;
		RobotType robottype = RobotType.MUCKRAKER;
		MapLocation mapLocation = new MapLocation(2,2);
		MapLocation loc1 = new MapLocation(10,10);
		MapLocation loc2 = new MapLocation(1,1);
		RobotInfo[] enemiespresent = new RobotInfo[2];
		enemiespresent[0] = new RobotInfo(1, teamA, robottype, 1,1, loc1);
		enemiespresent[1] = new RobotInfo(2, teamA, robottype, 1, 1, loc2);
		RobotInfo[] enemiesnotpresent = {};
		when(rc.senseNearbyRobots( -1, teamA)).thenReturn(enemiespresent);
		when(rc.getLocation()).thenReturn(new MapLocation(0, 0));
		int result = slandererplayer.WhenOpponentsAreFound(enemiespresent, mapLocation, rc);
		assertEquals(1, result);
		result = slandererplayer.WhenOpponentsAreFound(enemiesnotpresent, mapLocation, rc);
		assertEquals(-1, result);

		testSlandererRun();
	}

	private void testSlandererRun() throws GameActionException
	{
		testplayer = mock(RobotPlayer.class);
		rc = mock(RobotController.class);
		when(rc.getTeam()).thenReturn(Team.A);
		when(rc.getLocation()).thenReturn(new MapLocation(0,0));
		when(rc.senseNearbyRobots(-1,rc.getTeam().opponent()))
				  .thenReturn(new RobotInfo[]{});
		Slanderer.runSlanderer(rc);
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
