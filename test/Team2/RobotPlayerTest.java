package Team2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static Team2.RobotPlayer.*;

import battlecode.common.*;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.MockitoJUnit;

public class RobotPlayerTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	RobotPlayer testPlayer;
	RobotController rc;

	@Test
	public void testSanity() {
		assertEquals(2, 1 + 1);
	}

	@Test
	public void testRobotCreation() throws GameActionException {
		testPlayer = mock(RobotPlayer.class);
		testPlayer.rc = mock(RobotController.class);

		if (testPlayer.rc.getInfluence() >= 50) {
			testPlayer.runEnlightenmentCenter();
			RobotType type0 = testPlayer.makeRobots(0, Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type0);
			RobotType type1 = testPlayer.makeRobots(1, Direction.NORTHEAST);
			assertEquals(RobotType.SLANDERER, type1);
			RobotType type2 = testPlayer.makeRobots(2, Direction.NORTHEAST);
			assertEquals(RobotType.MUCKRAKER, type2);
			RobotType type3 = testPlayer.makeRobots(3, Direction.NORTHEAST);
			assertEquals(RobotType.POLITICIAN, type3);
		}
		else {
			testPlayer.runEnlightenmentCenter();
			RobotType type0 = testPlayer.makeRobots(0, Direction.NORTHEAST);
			assertEquals(null, type0);
			RobotType type1 = testPlayer.makeRobots(1, Direction.NORTHEAST);
			assertEquals(null, type1);
			RobotType type2 = testPlayer.makeRobots(2, Direction.NORTHEAST);
			assertEquals(null, type2);
			RobotType type3 = testPlayer.makeRobots(3, Direction.NORTHEAST);
			assertEquals(null, type3);
		}
	}

	@Test
	public void runTest() {
		rc = mock(RobotController.class);
		RobotType ec = RobotType.ENLIGHTENMENT_CENTER;
		if (rc.getType() == RobotType.ENLIGHTENMENT_CENTER) {
			assertEquals(RobotType.ENLIGHTENMENT_CENTER, rc.getType());
		}
		if (rc.getType() == RobotType.POLITICIAN) {
			assertEquals(RobotType.POLITICIAN, rc.getType());
		}
		if (rc.getType() == RobotType.SLANDERER) {
			assertEquals(RobotType.SLANDERER, rc.getType());
		}
		if (rc.getType() == RobotType.MUCKRAKER) {
			assertEquals(RobotType.MUCKRAKER, rc.getType());
		}
	}

//	@Test
//	public void testBid() {
//		rc = mock(RobotController.class);
//		if (infBeforeBid > 50) {
//			assertEquals(50, infAfterBid);
//		} else {
//			assertEquals(infBeforeBid, infAfterBid);
//		}
//	}
}
