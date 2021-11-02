package Team2;
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
    static MapLocation enemyBaseLoc = new MapLocation(0, 0);
    static int homeID;
    static MapLocation homeLoc;
    static Direction directionality = Direction.CENTER;

    static Set<Integer> flagsSeen = new HashSet<Integer>();

    public static int lastRobot = 0;

    //keep track of the known neutralECs
    static Set<MapLocation> neutralECs = new HashSet<MapLocation>();


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
                //                // You may rewrite this into your own control structure if you wish.
                //System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
                switch (rc.getType()) {
                    case ENLIGHTENMENT_CENTER: runEnlightenmentCenter(); break;
                    case POLITICIAN:           runPolitician();          break;
                    case SLANDERER:            runSlanderer();           break;
                    case MUCKRAKER:            runMuckraker();           break;
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
        Team enemyTeam = rc.getTeam().opponent();
        Team allyTeam = rc.getTeam();


        int actionRadius = rc.getType().actionRadiusSquared;
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemyTeam);

        if (turnCount <= 12) {
            for (RobotInfo ally :rc.senseNearbyRobots(2, allyTeam)) {
                if (ally.getType().canBid()){
                    homeID = ally.getID();
                    homeLoc = ally.getLocation();
                }
            }
        } else if (turnCount > 800) {
            directionality = Direction.CENTER;
        }

        // Can attack an enemy base or muckraker
        for (RobotInfo enemy : attackable) {
            if (enemy.type == RobotType.ENLIGHTENMENT_CENTER){
                if (rc.canEmpower(actionRadius)){
                    rc.empower(actionRadius);
                    return;
                }
            }
        }

        if (attackable.length != 3 && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            rc.empower(actionRadius);
            System.out.println("empowered");
            return;
        }
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
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

    static void runMuckraker() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int detectRadius = rc.getType().detectionRadiusSquared;
        RobotInfo[] robots = rc.senseNearbyRobots(detectRadius);
            if(dealWithSlanderer(robots) != -1)
                return;
            else
                dealWithEnlightenmentCenters(robots);
        //Move randomly if it can't see anything
        tryMove(randomDirection());
    }
    static int dealWithSlanderer(RobotInfo[] robots) throws GameActionException
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
                tryMove(rc.getLocation().directionTo(r.getLocation()));
                retVal = (retVal == 1) ? 1 : 2;
            }
        }
        return retVal;
    }

    static int dealWithEnlightenmentCenters(RobotInfo[] robots)
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


    static boolean addNeutralEC(MapLocation ml){
        return neutralECs.add(ml);
    }

    static void removeNeutralEC(MapLocation ml){
        neutralECs.remove(ml);
    }
}
