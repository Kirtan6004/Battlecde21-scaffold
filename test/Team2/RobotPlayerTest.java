package Team2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import battlecode.common.*;
import org.junit.Test;

public class RobotPlayerTest {
	RobotPlayer testplayer;
	@Test
	public void runMuckraker() throws GameActionException
	{
		testplayer = mock(RobotPlayer.class);
		testplayer.rc = mock(RobotController.class);
		when(testplayer.rc.getTeam()).thenReturn(Team.A);
		when(testplayer.rc.getLocation()).thenReturn(new MapLocation(0,0));
		muckrackerSlanderTest();
	}

	private void muckrackerSlanderTest() throws GameActionException
	{
		testplayer.rc.buildRobot(RobotType.MUCKRAKER,Direction.CENTER,100);
		RobotInfo muckraker = new RobotInfo(2,Team.A,RobotType.MUCKRAKER, 10,10,new MapLocation(0,0));
		MapLocation near = new MapLocation(5, 5);
		MapLocation far = new MapLocation(40,40);
		RobotInfo[] robots = {
				  new RobotInfo(1, Team.B, RobotType.SLANDERER,10,10, near)
		};
		RobotInfo[] robots2 = {
				  new RobotInfo(1, Team.B, RobotType.SLANDERER,10,10, near)
		};
		RobotInfo[] invalid = {
				  new RobotInfo(1, Team.B, RobotType.POLITICIAN, 10, 10, near)
		};
		RobotInfo[] empty = {};

		int reaction;
		//ignore non slanderers test
		reaction = testplayer.dealWithSlanderer(invalid);
		//nothing in range test
		assertEquals(reaction, -1);
		reaction = testplayer.dealWithSlanderer(empty);
		//slanderer
		assertEquals(reaction, -1);
		reaction = testplayer.dealWithSlanderer(robots);
		assertEquals(reaction, 1);
		reaction = testplayer.dealWithSlanderer(robots2);
		assertEquals(reaction, 2);
		//System.out.println("Result: " + result);
	}
}
