package Team2;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static Team2.RobotPlayer.*;

import battlecode.common.RobotController;
import battlecode.common.RobotType;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.junit.MockitoRule;
import org.mockito.junit.MockitoJUnit;

public class RobotPlayerTest {

	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	RobotController rc;

	@Test
	public void testSanity() {
		assertEquals(2, 1 + 1);
	}

	@Test
	public void testRobotCreation() {
		rc = mock(RobotController.class);
		if (lastRobot == 3) {
			assertEquals(RobotType.POLITICIAN, rc.getType());
		}
		if (lastRobot == 1) {
			assertEquals(RobotType.SLANDERER, rc.getType());
		}
		if (lastRobot == 2) {
			assertEquals(RobotType.MUCKRAKER, rc.getType());
		}
	}

	@Test
	public void runTest() {
		rc = mock(RobotController.class);
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

	@Test
	public void testBid() {
		rc = mock(RobotController.class);
		if (infBeforeBid > 50) {
			assertEquals(50, infAfterBid);
		} else {
			assertEquals(infBeforeBid, infAfterBid);
		}
	}
}
