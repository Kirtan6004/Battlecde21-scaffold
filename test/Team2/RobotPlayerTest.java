package Team2;

import static Team2.RobotPlayer.turnCount;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import junit.framework.TestCase;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;


public class RobotPlayerTest extends TestCase {
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	RobotPlayer testplayer;
	RobotController rc;
	@Test
	public void testSanity() {assertEquals(2, 1+1);}

	@org.junit.jupiter.api.Test
	public void runPoliticianTest() throws GameActionException{
		rc = mock(RobotController.class);
		testplayer.rc = mock(RobotController.class);
		when(testplayer.rc.getType()).thenReturn(RobotType.POLITICIAN);
		when(testplayer.rc.getTeam()).thenReturn(Team.A);
		when(testplayer.rc.getLocation()).thenReturn(new MapLocation(0,0));


		RobotInfo[] robots = {
				new RobotInfo(2, Team.B, RobotType.POLITICIAN,1,1, new MapLocation(1, 1))
		};
		//This Part is testing the number logic of Politician
		assertEquals(12, turnCount);
		assertEquals(800,turnCount);
		assertEquals(801,turnCount);
	}
}
