package Team2.robots;

import battlecode.common.*;


public class Muckraker extends AbstractRobot
{

	public static void run(RobotController rc) throws GameActionException
	{
		int detectRadius = rc.getType().detectionRadiusSquared;
		RobotInfo[] robots = rc.senseNearbyRobots(detectRadius);
		dealWithSlanderer(robots, rc);
		dealWithEnlightenmentCenters(robots, rc);
		//Move randomly if it can't see anything
		tryRandomMove(rc);
	}

	public static int dealWithSlanderer(RobotInfo[] robots, RobotController rc) throws GameActionException
	{
		int retVal = -1;
		int result;
		for (RobotInfo r : robots)
		{
			result = canExposeSlanderer(r, rc);
			if(result == 1)
				retVal = expose(r, rc);
			else
				retVal = chaseSlanderer(r, rc, result);
		}
		return retVal;
	}

	public static int dealWithEnlightenmentCenters(RobotInfo[] robots, RobotController rc)
	{
		int retVal = -1;
		/*
		for (RobotInfo r : robots)
		{
			if(ecm().addEc(r,rc))
			{
				retVal = 1;
			}
			else retVal = (retVal == 1) ? 1 : 2;
		}
		return retVal;

		*/
		return retVal;
	}
	
	/** Chase the slanderer. Return 1 if retval is 1 other wise return 2*/
	static int chaseSlanderer(RobotInfo robot, RobotController rc, int retval) throws GameActionException
	{
		chase(robot, rc);
		return (retval == 1) ? 1 : retval;
	}
	
	/** Exposes robot, returns 1*/
	static int expose(RobotInfo robot, RobotController rc)
	{
		try
		{
			rc.expose(robot.location);
			return 1;
		} catch (GameActionException e)
		{
			return -1;
		}
	}
	
	static int canExposeSlanderer(RobotInfo robot, RobotController rc) throws GameActionException
	{
		if(isEnemy(robot, rc))
		{
			if(exposable(robot, rc))
				return 1;
			return -2;
		}
		return -1;
	}
	
	/** Returns true if the robot type is exposable and the muckracker can expose them*/
	static boolean exposable(RobotInfo robot, RobotController rc)
	{
		return robot.type.canBeExposed() && rc.canExpose(robot.location);
	}
}
