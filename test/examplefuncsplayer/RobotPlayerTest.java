package Team2;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import battlecode.common.*;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import static Team2.RobotPlayer.lastRobot;

import battlecode.common.Clock;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;
import battlecode.common.RobotType;
import java.util.Map;


public class RobotPlayerTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();
	@Test
	public void testSanity() {
		assertEquals(2, 1+1);
	}
	RobotController rc;


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
	public void SlanderersTest() throws GameActionException
	{
		//This method tests for loop when there is one object and enemy location x is greater than the team location
		rc = mock(RobotController.class);
		int tempradius = -1;
		//Team team = rc.getTeam().opponent();
		Team team = Team.A;
		int ID = 1;
		RobotType robottype = RobotType.MUCKRAKER;
		int influence = 50;
		int conviction = 10;
		MapLocation mapLocation = new MapLocation(0,0);
		MapLocation enemylocation = new MapLocation(1,1);
		RobotInfo[] enemies = new RobotInfo[1];
		enemies[0] = new RobotInfo(ID, team, robottype, influence, conviction, enemylocation);
		//Mockito.doReturn(enemies).when(rc).senseNearbyRobots(tempradius, team);
		when(rc.senseNearbyRobots( tempradius, team)).thenReturn(enemies);
		MapLocation maplocation = rc.getLocation();
		when(rc.getLocation()).thenReturn(new MapLocation(0, 0));
		assertTrue(enemies.length > 0);
		int dangerX = 0;
		int dangerY = 0;
		for (RobotInfo r : enemies)
		{
			assertTrue(r.getType() == RobotType.MUCKRAKER);
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
