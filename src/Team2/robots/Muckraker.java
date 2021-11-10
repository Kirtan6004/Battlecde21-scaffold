package Team2.robots;

import Team2.RobotPlayer;
import battlecode.common.*;


public class Muckraker extends AbstractRobot
{


	public static void run(RobotController rc) throws GameActionException
	{
		Team enemy = rc.getTeam().opponent();
		int detectRadius = rc.getType().detectionRadiusSquared;
		RobotInfo[] robots = rc.senseNearbyRobots(detectRadius);
		if(dealWithSlanderer(robots,rc) != -1)
			return;
		else
			dealWithEnlightenmentCenters(robots, rc);
		//Move randomly if it can't see anything
		tryMove(randomDirection(), rc);
	}

	static int dealWithSlanderer(RobotInfo[] robots, RobotController rc) throws GameActionException
	{
		int retVal = -1;
		for(RobotInfo r : robots){
			if(!r.type.equals(RobotType.SLANDERER))
				continue;
			Team enemy = rc.getTeam().opponent();
			//expose it if its in range
			if(r.team.equals(enemy)){
				if (r.type.canBeExposed())
				{
					if (rc.canExpose(r.location))
					{
						rc.expose(r.location);
						//System.out.println("Exposed you!");
						retVal = 1;
					}
				}
				//otherwise chase slanderer
				tryMove(rc.getLocation().directionTo(r.getLocation()),rc);
				retVal = (retVal == 1) ? 1 : 2;
			}
		}
		return retVal;
	}

	static int dealWithEnlightenmentCenters(RobotInfo[] robots, RobotController rc)
	{
		int retVal = -1;
		for(RobotInfo r : robots)
		{
			if (!r.type.equals(RobotType.ENLIGHTENMENT_CENTER))
				continue;
			if (r.team.equals(Team.NEUTRAL))
			{
				if (addNeutralEC(r.location))
				{
					//System.out.println("I'm Helping! " + neutralECs.size());
					retVal = 1;
				}
			}
			retVal = (retVal == 1) ? 1 : 2;
		}
		return retVal;
	}

	static boolean addNeutralEC(MapLocation ml){
		return RobotPlayer.addNeutralEC(ml);
	}

	static void removeNeutralEC(MapLocation ml){
		RobotPlayer.removeNeutralEC(ml);
	}
}
