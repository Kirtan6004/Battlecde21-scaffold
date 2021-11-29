package Team2;
import Team2.robots.EnlightenmentCenter;
import Team2.robots.Muckraker;
import Team2.robots.Politician;
import Team2.robots.Slanderer;
import battlecode.common.*;

import java.util.*;


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
                switchType();

                // Clock.yield() makes the robot wait until the next turn, then it will perform this loop again
                Clock.yield();

            } catch (Exception e) {
                System.out.println(rc.getType() + " Exception");
                e.printStackTrace();
            }
        }
    }

    public static int switchType() throws GameActionException
    {
        switch (rc.getType()) {
            case ENLIGHTENMENT_CENTER: EnlightenmentCenter.run(rc, rc.getRoundNum()); return 0;
            case POLITICIAN:           Politician.run(rc);          return 1;
            case SLANDERER:            Slanderer.runSlanderer(rc);  return 2;
            case MUCKRAKER:            Muckraker.run(rc);           return 3;
        }
        return -1;
    }
}
