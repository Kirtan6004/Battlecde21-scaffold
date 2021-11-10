package Team2.robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;


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
}
