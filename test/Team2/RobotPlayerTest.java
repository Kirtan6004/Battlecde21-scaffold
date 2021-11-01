package Team2;

import static Team2.RobotPlayer.turnCount;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import battlecode.common.*;
import org.junit.Rule;
import org.junit.Test;

import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static Team2.RobotPlayer.lastRobot;


public class RobotPlayerTest {
	@Rule
	public MockitoRule mockitoRule = MockitoJUnit.rule();

	RobotController rc;
	@Test
	public void testSanity() {assertEquals(2, 1+1);}
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
//	Politician
	public void PoliticianTest() throws GameActionException{
		rc = mock(RobotController.class);
		//This Part is testing the number logic of Politician
		if(turnCount <= 12){
			assertEquals(12, turnCount);
		}
		else if(turnCount <= 800){
			assertEquals(800,turnCount);
		}
		else{
			assertEquals(801,turnCount);
		}
		//This Part will test Enpower Part and Attack Part
//		Team team = Team.A;
//		int ID = 1;
//		MapLocation enemylocation = new MapLocation(1,1);
//		RobotInfo[] enemies = new RobotInfo[1];
//		enemies[0] = new RobotInfo(ID, team, robottype, influence, conviction, enemylocation);
//		for (RobotInfo enemy : enemies){
////			actionRadius
//
//		}
	}
}
