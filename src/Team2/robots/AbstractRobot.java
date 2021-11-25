package Team2.robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import battlecode.common.*;




public abstract class AbstractRobot
{

    protected static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };


    //Copied these functions over here to use in your classes

    protected static boolean tryMove(Direction dir, RobotController rc) throws GameActionException
    {

        if (rc == null)
        {
            return false;
        }
        else
        {
            //System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
            if (rc.canMove(dir)) {
                rc.move(dir);
                return true;
            } else return false;
        }
    }
    protected static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }




	//Copied these functions over here to use in your classes
	protected static boolean chase(RobotInfo robot, RobotController rc) throws GameActionException
	{
		return tryMove(robot.location, rc);
	}

/**
 * Attempts to move in a given direction.
 *
 * @param dir The intended direction of movement
 * @param rc	The robot controller
 * @return true if a move was performed
 * @throws GameActionException
 */

	protected static boolean tryMove(MapLocation loc, RobotController rc) throws GameActionException
	{
		return tryMove(rc.getLocation().directionTo(loc), rc);
	}
	protected static boolean tryRandomMove(RobotController rc) throws GameActionException
	{
		return tryMove(randomDirection(), rc);
	}
	protected static boolean isEnemy(RobotInfo robot, RobotController rc)
	{
		return robot.team.equals(rc.getTeam().opponent());
	}
	

}
