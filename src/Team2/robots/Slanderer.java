package Team2.robots;
import Team2.RobotPlayer;
import Team2.robots.AbstractRobot;
import battlecode.common.*;

public class Slanderer extends AbstractRobot{

    public static int dangerX = 0;
    public static int dangerY = 0;

    public static void runSlanderer(RobotController rc) throws GameActionException {

        RobotInfo[] enemies = rc.senseNearbyRobots(-1,rc.getTeam().opponent());
        MapLocation location = rc.getLocation();
        int result = WhenOpponentsAreFound(enemies, location, rc);

    }

    public static int WhenOpponentsAreFound(RobotInfo[] enemies, MapLocation location, RobotController rctemp) throws GameActionException
    {
        if (enemies.length > 0)
        {
            for(RobotInfo r : enemies)
            {
                if(r.getType() == RobotType.MUCKRAKER){
                    //FLY YOU FOOLS!
                    MapLocation enemyloc = r.getLocation();
                    dangerX = ChangeXCoordinates(enemyloc, location);
                    dangerY = ChangeYCoordinates(enemyloc, location);
                }
            }
            MapLocation safety = location.translate(Integer.signum(dangerX),
                    Integer.signum(dangerY));
            tryMove(location.directionTo(safety), rctemp);
            return 1;
        }
        else
        {
            tryMove(randomDirection(), rctemp);
            return -1;
        }
    }

    public static int ChangeXCoordinates(MapLocation enemyloc, MapLocation location)
    {

        if(enemyloc.x > location.x)
        {
            dangerX--;
        }
        else
        {
            dangerX++;
        }
        return dangerX;
    }

    public static int ChangeYCoordinates(MapLocation enemyloc, MapLocation location)
    {

        if (enemyloc.y > location.y)
        {
            dangerY--;
        }
        else
        {
            dangerY++;
        }
        return dangerY;
    }
}
