package Team2.robots;

import Team2.utils.ECManager;
import battlecode.common.*;


public class Muckraker extends AbstractRobot
{

	public static void run(RobotController rc) throws GameActionException
	{
		Team enemy = rc.getTeam().opponent();
		int detectRadius = rc.getType().detectionRadiusSquared;
		RobotInfo[] robots = rc.senseNearbyRobots(detectRadius);
		if (dealWithSlanderer(robots, rc) != -1)
			return;
		else
			dealWithEnlightenmentCenters(robots, rc);
		//Move randomly if it can't see anything
		tryMove(randomDirection(), rc);
	}

	static int dealWithSlanderer(RobotInfo[] robots, RobotController rc) throws GameActionException
	{
		int retVal = -1;
		for (RobotInfo r : robots)
		{
			if(canExposeSlanderer(r,rc))
				retVal = expose(r, rc);
			else
				retVal = chaseSlanderer(r, rc, retVal);
		}
		return retVal;
	}

	static int dealWithEnlightenmentCenters(RobotInfo[] robots, RobotController rc)
	{
		int retVal = -1;
		for (RobotInfo r : robots)
		{
			if(ECManager.addEc(r,rc))
				retVal = 1;
			else retVal = (retVal == 1) ? 1 : 2;
		}
		return retVal;
	}
	
	/** Chase the slanderer. Return 1 if retval is 1 other wise return 2*/
	static int chaseSlanderer(RobotInfo robot, RobotController rc, int retval) throws GameActionException
	{
		chase(robot, rc);
		return (retval == 1) ? 1 : 2;
	}
	
	/** Exposes robot, returns 1*/
	static int expose(RobotInfo robot, RobotController rc) throws GameActionException
	{
		rc.expose(robot.location);
		return 1;
	}
	
	static boolean canExposeSlanderer(RobotInfo robot, RobotController rc) throws GameActionException
	{
		if(isEnemy(robot, rc) && exposable(robot, rc))
		{
			rc.expose(robot.location);
			return true;
		}
		return false;
	}
	
	/** Returns true if the robot type is exposable and the muckracker can expose them*/
	static boolean exposable(RobotInfo robot, RobotController rc)
{
	return robot.type.canBeExposed() && rc.canExpose(robot.location);
}
}
