package FirstBot;
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
    static MapLocation enemyBaseLoc = new MapLocation(0, 0);
    static int homeID;
    static MapLocation homeLoc;
    static Direction directionality = Direction.CENTER;
    static int handedness;
    static Set<Integer> flagsSeen = new HashSet<Integer>();
    static Set<Integer> seenMucks = new HashSet<Integer>();

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
                // You may rewrite this into your own control structure if you wish.
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
        RobotType toBuild = randomSpawnableRobotType();
        int influence = 50;
        for (Direction dir : directions) {
            if (rc.canBuildRobot(toBuild, dir, influence)) {
                rc.buildRobot(toBuild, dir, influence);
            } else {
                break;
            }
        }
    }
    static boolean trySetFlag(int flagNum) throws GameActionException {
        System.out.println("I am trying to set flag " + flagNum + ".");
        if (rc.canSetFlag(flagNum)) {
            rc.setFlag(flagNum);
            return true;
        } else return false;
    }
    static int codeFlag(MapLocation baseLoc, int extraInfo) throws GameActionException {
        return 128*(baseLoc.x % 128) + baseLoc.y % 128 + extraInfo * 128 * 128;
    }

    static MapLocation decodeFlag(int flag) throws GameActionException {
        int y = flag % 128;
        int x = (flag/128) % 128;
        int extrainfo = flag / 128 / 128;

        if (flag == 0 || flag == 128 * 128) {
            return new MapLocation(0, 0);
        }
//
//        if (trySetFlag(flag)) {
//            System.out.println("Decoded and Set flag: "+flag); //Set own Flag as the same
//        }

        MapLocation currentLocation = rc.getLocation();
        int offsetX128 = currentLocation.x / 128;
        int offsetY128 = currentLocation.y / 128;
        MapLocation actualLocation = new MapLocation(offsetX128*128 + x, offsetY128*128 + y);

        MapLocation altLocation = actualLocation.translate(-128, 0);
        if (currentLocation.distanceSquaredTo(altLocation) < currentLocation.distanceSquaredTo(actualLocation)) {
            actualLocation = altLocation;
        }
        altLocation = actualLocation.translate(128, 0);
        if (currentLocation.distanceSquaredTo(altLocation) < currentLocation.distanceSquaredTo(actualLocation)) {
            actualLocation = altLocation;
        }
        altLocation = actualLocation.translate(0, -128);
        if (currentLocation.distanceSquaredTo(altLocation) < currentLocation.distanceSquaredTo(actualLocation)) {
            actualLocation = altLocation;
        }
        altLocation = actualLocation.translate(0, 128);
        if (currentLocation.distanceSquaredTo(altLocation) < currentLocation.distanceSquaredTo(actualLocation)) {
            actualLocation = altLocation;
        }
        return actualLocation;
    }
    static void runPolitician() throws GameActionException {
        Team enemyTeam = rc.getTeam().opponent();
        Team allyTeam = rc.getTeam();
        MapLocation currentloc = rc.getLocation();
        int actionRadius = rc.getType().actionRadiusSquared;
        int senseRadius = rc.getType().sensorRadiusSquared;
        RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemyTeam);
        RobotInfo[] attackableNeutral = rc.senseNearbyRobots(actionRadius, Team.NEUTRAL);
        RobotInfo[] sensed = rc.senseNearbyRobots(senseRadius, enemyTeam);
        RobotInfo[] sensedNeutral = rc.senseNearbyRobots(senseRadius, Team.NEUTRAL);
        RobotInfo[] sensedAlly = rc.senseNearbyRobots(senseRadius, allyTeam);

        boolean seeMurkorPol = false;

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
            // finds a muckraker
            if (enemy.type == RobotType.MUCKRAKER || enemy.type == RobotType.POLITICIAN) {
                seeMurkorPol = true;
            }
        }

        // Move toward a sensed enemy base
        for (RobotInfo enemy : sensed) {
            if (enemy.type == RobotType.ENLIGHTENMENT_CENTER){
                MapLocation enemyLoc = enemy.getLocation();
                int flagNum = codeFlag(enemyLoc, 0); // Set a flag for enemy base location
                if (trySetFlag(flagNum)){
                    flagsSeen.add(flagNum);
                    System.out.println("Set Flag " + flagNum);
                }

                Direction dirMove = currentloc.directionTo(enemyLoc);
                if (rc.canMove(dirMove)) {
                    rc.move(dirMove);
                    return;
                }
            }
        }

        // Attack neutral base if < 100 influence
        for (RobotInfo neutral : attackableNeutral) {
            if (neutral.type == RobotType.ENLIGHTENMENT_CENTER && (neutral.getInfluence() <= 100 || Math.random() > .6)){
                if (rc.canEmpower(actionRadius)){
                    rc.empower(actionRadius);
                    return;
                }
            }
        }

        // Move toward a neutral base if < 100 influence
        for (RobotInfo neutral : sensedNeutral) {
            if (neutral.getType().canBid() && (neutral.getInfluence() <= 100 || Math.random() > .3)){
                Direction dirMove = currentloc.directionTo(neutral.getLocation());
                if (rc.canMove(dirMove)) {
                    rc.move(dirMove);
                    return;
                }
            }
        }

        //Check EC flag
        if (rc.canGetFlag(homeID)) {
            int ecFlag = rc.getFlag(homeID);
            if (ecFlag > 10 && enemyBaseLoc.x == 0 && !flagsSeen.contains(ecFlag)) {
                flagsSeen.add(ecFlag);
                enemyBaseLoc = decodeFlag(ecFlag);
                System.out.println("Got flag from EC. Target location: " + enemyBaseLoc);
            }
        }

        //Decode neighboring ally flags
        for (RobotInfo ally : sensedAlly) {
            int allyFlag = rc.getFlag(ally.getID());
            if (allyFlag > 10 && !flagsSeen.contains(allyFlag)) {
                flagsSeen.add(allyFlag);
                enemyBaseLoc = decodeFlag(allyFlag);
                System.out.println("Decoded a flag with location: " + enemyBaseLoc);
                break;
            }
        }

        //Check if enemybaseloc has already been captured, if so, set flag with extrainfo = 1.
        for (RobotInfo ally : sensedAlly) {
            if (ally.type == RobotType.ENLIGHTENMENT_CENTER && ally.getLocation().equals(enemyBaseLoc)) {
                int flagNum = codeFlag(ally.getLocation(), 1); // Set a flag for enemy base location
                if (trySetFlag(flagNum)) {
                    flagsSeen.add(flagNum);
                    System.out.println("Set Flag " + flagNum + "(Captured)");
                    enemyBaseLoc = new MapLocation(0,0);
                }
                break;
            }
        }

        // If we saw a murk, empower w 30% chance
        if (seeMurkorPol && Math.random() >= .7){
            if (rc.canEmpower(actionRadius)){
                rc.empower(actionRadius);
                return;
            }
        }

        // Empower if 3+ enemies around it
        if (attackable.length >= 3 && rc.canEmpower(actionRadius)) {
            System.out.println("empowering...");
            rc.empower(actionRadius);
            System.out.println("empowered");
            return;
        }
    }

    static void runSlanderer() throws GameActionException {
        if (tryMove(randomDirection()))
            System.out.println("I moved!");
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
