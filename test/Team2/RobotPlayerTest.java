package Team2;

import static Team2.RobotPlayer.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;


public class RobotPlayerTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	@Test
	public void testSanity() {
		assertEquals(2, 1+1);
	}
	RobotController rc;
	RobotPlayer testplayer;


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

}
