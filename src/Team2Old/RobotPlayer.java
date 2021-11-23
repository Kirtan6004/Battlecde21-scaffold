package Team2Old;

import Team2Old.robots.EnlightenmentCenter;
import Team2Old.robots.Muckraker;
import Team2Old.robots.Politician;
import Team2Old.robots.Slanderer;
import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;


public strictfp class RobotPlayer {
     static RobotController rc;

    static final RobotType[] spawnableRobot = {
            RobotType.POLITICIAN,
            RobotType.SLANDERER,
            RobotType.MUCKRAKER,
    };

    static final Direction[] directions = {
            Direction.NORTH,
            Direction.NORTHEAST,
            Direction.EAST,
            Direction.SOUTHEAST,
            Direction.SOUTH,
            Direction.SOUTHWEST,
            Direction.WEST,
            Direction.NORTHWEST,
    };

    static int turnCount;
    static int influence = 100;
    // Add New Var on here

    static Set<Integer> flagsSeen = new HashSet<Integer>();

    public static int lastRobot = 0;

    /**
     * run() is the method that is called when a robot is instantiated in the Battlecode world.
     * If this method returns, the robot dies!
     **/
    @SuppressWarnings("unused")
    public static void run(RobotController rc) throws GameActionException {

        // This is the RobotController object. You use it to perform actions from this robot,
        // and to get information on its current status.
        RobotPlayer.rc = rc;

        turnCount = 0;

        //System.out.println("I'm a " + rc.getType() + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                // You may rewrite this into your own control structure if you wish.
                //System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER: EnlightenmentCenter.run(rc); break;
                    case POLITICIAN:           Politician.run(rc);       break;
                    case SLANDERER:            Slanderer.runSlanderer(rc);break;
                    case MUCKRAKER:            Muckraker.run(rc);        break;
                }

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    static int WhenOpponentsAreFound(RobotInfo[] enemies, MapLocation location, RobotController rctemp) throws GameActionException
    {
        rc = rctemp;
        int dangerX = 0;
        int dangerY = 0;
        if (enemies.length > 0)
        {
            for(RobotInfo r : enemies)
            {
                if(r.getType() == RobotType.MUCKRAKER){
                    //FLY YOU FOOLS!
                    MapLocation enemyloc = r.getLocation();
                    if(enemyloc.x > location.x)
                        dangerX--;
                    else
                        dangerX++;

                    if(enemyloc.y > location.y)
                        dangerY--;
                    else
                        dangerY++;
                }
            }
            MapLocation safety = location.translate(Integer.signum(dangerX),
                    Integer.signum(dangerY));
            tryMove(location.directionTo(safety));
            return 1;
        }
        else
        {
            tryMove(randomDirection());
            return -1;
        }
    }

    /**
     * Returns a random Direction.
     *
     * @return a random Direction
     */
    static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }

    /**
     * Returns a random spawnable RobotType
     *
     * @return a random RobotType
     */
    static RobotType randomSpawnableRobotType() {
        return spawnableRobot[(int) (Math.random() * spawnableRobot.length)];
    }

    /**
     * Attempts to move in a given direction.
     *
     * @param dir The intended direction of movement
     * @return true if a move was performed
     * @throws GameActionException
     */
    static boolean tryMove(Direction dir) throws GameActionException {

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
}
