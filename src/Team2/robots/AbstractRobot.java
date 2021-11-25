package Team2.robots;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.RobotController;

import battlecode.common.*;


public abstract class AbstractRobot
{
    public static Direction pastDirection;
//    public static Object reflectedDirection;
    protected static MapLocation pastMapLocation;

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

    public static boolean tryMove(Direction dir, RobotController rc) throws GameActionException
    {

        if (rc == null)
        {
            return false;
        }
        else
        {
            MapLocation temp = rc.getLocation();
            //for the first time doesn't enter this if loop as pastdirection will be null.
            // But enters for the next consecutive times
            if (pastDirection != null && IsOnBorder(rc, null))
            {
                dir = reflectedDirection(rc);
            }
            //System.out.println("I am trying to move " + dir + "; " + rc.isReady() + " " + rc.getCooldownTurns() + " " + rc.canMove(dir));
            if (rc.canMove(dir)) {
                rc.move(dir);
                pastDirection = dir;
                pastMapLocation = temp;
                return true;
            }
            else return false;
        }
    }
    protected static Direction randomDirection() {
        return directions[(int) (Math.random() * directions.length)];
    }
    /*Get the reflection direction if rc is on the border*/
    /* reflection direction is determined by its "pastdirection" to the incidence of the wall
    * if incident angle is 90 degree to any wall of the map (direction = south/north/east/west)
    * we return the opposite direction
    * if incident angle is 45 degree to any wall of the map (direction = SE/SW/NE/NW)
    * we return the reflection direction
    * by turning right or left (whichever is inside the map) from the "pastdirection"
    * if rc is on the corner, we return the opposite of its ""pastdirection*/
    protected static Direction reflectedDirection(RobotController rc) throws GameActionException
    {

        MapLocation ownLocation = rc.getLocation();
        Direction reflectedDir = Direction.NORTH;
        int distance;
        if (pastDirection == Direction.NORTH || pastDirection == Direction.SOUTH || pastDirection == Direction.EAST || pastDirection == Direction.WEST)
        {
            reflectedDir = pastDirection.opposite();
        }
        else
        {
            Direction tempDir = pastDirection;
            if (rc.canMove((pastDirection.rotateRight())))
            {
                reflectedDir = tempDir.rotateRight();
            }
            else if (rc.canMove((pastDirection.rotateLeft())))
            {
                reflectedDir = tempDir.rotateLeft();
            }
            else
            {
                reflectedDir = tempDir.opposite();
            }
        }


        MapLocation next = ownLocation.add(reflectedDir);
        if (IsOnBorder(rc, next))
        {
            Direction tempDir = reflectedDir;
            if (rc.canMove((tempDir.rotateRight())))
            {
                reflectedDir = reflectedDir.rotateRight();
            }
            else if (rc.canMove((tempDir.rotateLeft())))
            {
                reflectedDir = reflectedDir.rotateLeft();
            }
            else
            {
                reflectedDir = reflectedDir.opposite();
            }
        }

        /*This logic is to find reflection point and direction based on distance formula and pythagoras hypotenuse length*/
//        else if (pastDirection == Direction.NORTHEAST)
//        {
//            distance = getDistanceToReflectedPoint(rc);
//            MapLocation nextPosSouthEast = ownLocation.translate(0 + distance, 0);
//            MapLocation nextPosNorthWest = ownLocation.translate(0, 0 + distance);
//
//            if (rc.onTheMap(nextPosSouthEast))
//            {
//                reflectedDir = Direction.SOUTHEAST;
//            }
//            else if (rc.onTheMap(nextPosNorthWest))
//            {
//                reflectedDir = Direction.NORTHWEST;
//            }
//            else //at the corner, so reverse
//            {
//                reflectedDir = Direction.SOUTHWEST;
//            }
//
//
//        }
//        else if (pastDirection == Direction.NORTHWEST)
//        {
//            distance = getDistanceToReflectedPoint(rc);
//            MapLocation nextPosSouthWest = ownLocation.translate(0 - distance, 0);
//            MapLocation nextPosNorthEast = ownLocation.translate(0, 0 + distance);
//
//            if (rc.onTheMap(nextPosSouthWest))
//            {
//                reflectedDir = Direction.SOUTHWEST;
//            }
//            else if (rc.onTheMap(nextPosNorthEast))
//            {
//                reflectedDir = Direction.NORTHEAST;
//            }
//            else //at the corner, so reverse
//            {
//                reflectedDir = Direction.SOUTHEAST;
//            }
//        }
//        else if (pastDirection == Direction.SOUTHEAST)
//        {
//            distance = getDistanceToReflectedPoint(rc);
//            MapLocation nextPosSouthWest = ownLocation.translate(0, 0 - distance);
//            MapLocation nextPosNorthEast = ownLocation.translate(0 + distance, 0);
//
//            if (rc.onTheMap(nextPosSouthWest))
//            {
//                reflectedDir = Direction.SOUTHWEST;
//            }
//            else if (rc.onTheMap(nextPosNorthEast))
//            {
//                reflectedDir = Direction.NORTHEAST;
//            }
//            else //at the corner, so reverse
//            {
//                reflectedDir = Direction.NORTHWEST;
//            }
//        }
//        else if (pastDirection == Direction.SOUTHWEST)
//        {
//            distance = getDistanceToReflectedPoint(rc);
//            MapLocation nextPosNorthWest = ownLocation.translate(0 - distance, 0);
//            MapLocation nextPosSouthEast = ownLocation.translate(0, 0 - distance);
//
//            if (rc.onTheMap(nextPosNorthWest))
//            {
//                reflectedDir = Direction.NORTHWEST;
//            }
//            else if (rc.onTheMap(nextPosSouthEast))
//            {
//                reflectedDir = Direction.SOUTHEAST;
//            }
//            else //at the corner, so reverse
//            {
//                reflectedDir = Direction.NORTHEAST;
//            }
//        }

        return reflectedDir;

    }

//    private static int getDistanceToReflectedPoint(RobotController rc)
//    {
//        MapLocation ownLocation = rc.getLocation();
//        int pastX = pastMapLocation.x;
//        int currentX = ownLocation.x;
//        int pastY = pastMapLocation.y;
//        int currentY = pastMapLocation.y;
//
//        double distance = Math.sqrt(Math.pow((currentX - pastX), 2) + Math.pow((currentY - pastY), 2));
//
//        return (int)(distance * Math.sqrt(2));
//    }

    //Copied these functions over here to use in your classes
    protected static boolean chase(RobotInfo robot, RobotController rc) throws GameActionException
    {
        return tryMove(robot.location, rc);
    }

    /**
     * Attempts to move in a given direction.
     *
     *
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


    /**
     * Checks if there is a out of map square which this robot can sense.
     *
     * @return MapLocation of the border if one is found, null otherwise.
     * If a corner could be inferred the corner location is returned.
     * The returned location is on the map.
     */

    /* check if robot(rc) is on the border of the map */
    //this method returns true if the coordinates is on the border else returns false
    protected static boolean IsOnBorder(RobotController rc, MapLocation next) throws GameActionException {
        MapLocation ownLocation = rc.getLocation();
        if (next != null)
        {
            ownLocation = next;
        }
        //assuming the robot is moving only by 1 unit
        int nextPos = 1;
        /*get transient location (+1 or -1 of current x and y) from current location */
        MapLocation borderXpos = ownLocation.translate(nextPos, 0);
        MapLocation borderXneg = ownLocation.translate(-nextPos, 0);
        MapLocation borderYpos = ownLocation.translate(0, nextPos);
        MapLocation borderYneg = ownLocation.translate(0, -nextPos);


        //Now determine the transient location coordinates is on the map
        //if not return true

        if (!rc.onTheMap(borderXpos))
        {
            return true;
        }
        else if (!rc.onTheMap(borderXneg))
        {
            return true;
        }


        if (!rc.onTheMap(borderYpos))
        {
            return true;
        }
        else if (!rc.onTheMap(borderYneg))
        {
            return true;
        }
        return false;
    }


}

