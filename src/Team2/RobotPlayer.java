package Team2;
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
    // Add New Var on here
    static MapLocation enemyBaseLoc = new MapLocation(0, 0);
    static int homeID;
    static MapLocation homeLoc;
    static Direction directionality = Direction.CENTER;

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

        System.out.println("I'm a " + rc.getType() + " and I just got created!");
        while (true) {
            turnCount += 1;
            // Try/catch blocks stop unhandled exceptions, which cause your robot to freeze
            try {
                // Here, we've separated the controls into a different method for each RobotType.
                //                // You may rewrite this into your own control structure if you wish.
                System.out.println("I'm a " + rc.getType() + "! Location " + rc.getLocation());
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

    static void runEnlightenmentCenter() throws GameActionException {
        RobotType toBuild1 = RobotType.POLITICIAN;
        RobotType toBuild2 = RobotType.SLANDERER;
        RobotType toBuild3 = RobotType.MUCKRAKER;

        int votes;
        int influence = 50;
        int flagValue = 0;

        votes = rc.getTeamVotes();
        for (Direction dir : directions) {
            if ((lastRobot == 0 || lastRobot == 3) && rc.canBuildRobot(toBuild1, dir, influence)) {
                rc.buildRobot(toBuild1, dir, influence);
                lastRobot = 1;
                if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0)
                {
                    rc.setFlag(flagValue);
                }
            } else if ((lastRobot == 1) && rc.canBuildRobot(toBuild2, dir, influence)) {
                rc.buildRobot(toBuild2, dir, influence);
                lastRobot = 2;
                if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0)
                {
                    rc.setFlag(flagValue);
                }
            } else if ((lastRobot == 2) && rc.canBuildRobot(toBuild3, dir, influence)) {
                rc.buildRobot(toBuild3, dir, influence);
                lastRobot = 3;
                if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0)
                {
                    rc.setFlag(flagValue);
                }
            } else {
                break;
            }
        }

        int leftoverInfluence = rc.getInfluence();
        if (leftoverInfluence > 50)
        {
            int bid_num = leftoverInfluence - 50;
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
        if(enemies.length > 0)
        {
            int dangerX = 0;
            int dangerY = 0;
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

        }
        else
            tryMove(randomDirection());
    }

    static void runMuckraker() throws GameActionException {
        Team enemy = rc.getTeam().opponent();
        int actionRadius = rc.getType().actionRadiusSquared;
        for (RobotInfo robot : rc.senseNearbyRobots(actionRadius, enemy)) {
            if (robot.type.canBeExposed()) {
                // It's a slanderer... go get them!
                if (rc.canExpose(robot.location)) {
                    System.out.println("e x p o s e d");
                    rc.expose(robot.location);
                    return;
                }
            }
        }
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
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
        System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
        if (rc.canMove(dir)) {
            rc.move(dir);
            return true;
        } else return false;
    }
}
