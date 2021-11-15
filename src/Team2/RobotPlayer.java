package Team2;
import Team2.robots.Muckraker;
import Team2.utils.ECManager;
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
            ECManager.updateLists(rc);
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                //                // You may rewrite this into your own control structure if you wish.
                //System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER: runEnlightenmentCenter(); break;
                    case POLITICIAN:           runPolitician();          break;
                    case SLANDERER:            runSlanderer();           break;
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

    static RobotType makeRobots(int last, Direction d) throws GameActionException {
        RobotType toBuild1 = RobotType.POLITICIAN;
        RobotType toBuild2 = RobotType.SLANDERER;
        RobotType toBuild3 = RobotType.MUCKRAKER;

        int influence = 50;
        int flagValue = 0;

        if ((last == 0 || last == 3) && rc.canBuildRobot(toBuild1, d, influence)) {
            rc.buildRobot(toBuild1, d, influence);
            lastRobot = 1;
            if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0)
            {
                rc.setFlag(flagValue);
            }
            return toBuild1;
        } else if ((last == 1) && rc.canBuildRobot(toBuild2, d, influence)) {
            rc.buildRobot(toBuild2, d, influence);
            lastRobot = 2;
            if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0)
            {
                rc.setFlag(flagValue);
            }
            return toBuild2;
        } else if ((last == 2) && rc.canBuildRobot(toBuild3, d, influence)) {
            rc.buildRobot(toBuild3, d, influence);
            lastRobot = 3;
            if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0)
            {
                rc.setFlag(flagValue);
            }
            return toBuild3;
        }
        return null;
    }

    static void runEnlightenmentCenter() throws GameActionException {
        int votes;

        votes = rc.getTeamVotes();
        for (Direction dir : directions) {
            makeRobots(lastRobot, dir);
        }

        int leftoverInf = rc.getInfluence();
        if (leftoverInf > 50)
        {
            int bid_num = leftoverInf - 50;
            rc.bid(bid_num);
        }
        int byteCodeLeft = Clock.getBytecodesLeft();
    }

    static void runPolitician() throws GameActionException {
        Team me = rc.getTeam();
        Team enemy = rc.getTeam().opponent();
        int sensorRadius = rc.getType().sensorRadiusSquared;
        int actionRadius = rc.getType().actionRadiusSquared;
        int backing = rc.senseNearbyRobots(actionRadius, me).length;

        empower(actionRadius, rc.senseNearbyRobots(actionRadius,enemy),
                rc.senseNearbyRobots(actionRadius, Team.NEUTRAL));
        if(pursueNeutralECs() < 0)
            tryMove(randomDirection());

    }

    static int empower(int actionRadius, RobotInfo[] enemy, RobotInfo[] neutral) throws GameActionException
    {
        if(rc.canEmpower(actionRadius)){
            if(enemy.length > 0 || neutral.length > 0){
                rc.empower(actionRadius);
                System.out.println("BOOM!");
                return 1;
            }
            return 0;
        }
        return -1;

    }

    /**
     * @return -1 if no neutral ECs left, 1 if moving towards a neutral EC
     * @throws GameActionException
     */
    static int pursueNeutralECs() throws GameActionException
    {
        if(neutralECs.size() == 0)
            return -1;

        Direction toClosest = Direction.CENTER;
        MapLocation me = rc.getLocation();
        int best = 999999;
        int dist = 0;

        for(MapLocation ml : neutralECs)
        {
            dist = me.distanceSquaredTo(ml);
            if(dist < best)
            {
                best = dist;
                toClosest = me.directionTo(ml);
            }

        }
        System.out.println("I'm in pursuit!");
        tryMove(toClosest);
        return 1;
    }

    static void runSlanderer() throws GameActionException {
        RobotInfo[] enemies = rc.senseNearbyRobots(-1,rc.getTeam().opponent());
        MapLocation location = rc.getLocation();
        int result = WhenOpponentsAreFound(enemies, location, rc);
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
