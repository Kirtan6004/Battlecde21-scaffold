package Team2.robots;

import Team2.RobotPlayer;
import battlecode.common.*;

public class Politician extends AbstractRobot
{
  public static void run(RobotController rc) throws GameActionException
  {
    Team enemy = rc.getTeam().opponent();
    int actionRadius = rc.getType().actionRadiusSquared;
  
    empower(rc, actionRadius, rc.senseNearbyRobots(actionRadius,enemy),
          rc.senseNearbyRobots(actionRadius, Team.NEUTRAL));
    if(pursueNeutralECs(rc) < 0)
      tryRandomMove(rc);
  }

  /**
   * @return -1 if no neutral ECs left, 1 if moving towards a neutral EC
   * @throws GameActionException
   */
  static int pursueNeutralECs(RobotController rc) throws GameActionException
  {
    /*
    if(!ecm().haveKnownNeutralECs())
      return -1;
    System.out.println("WE made it past the first check");
    Direction toClosest = Direction.CENTER;
    MapLocation me = rc.getLocation();
    int best = 999999;
    int dist = 0;
    
    for(RobotInfo robot : ecm().getNeutralECs())
    {
      dist = me.distanceSquaredTo(robot.location);
      if(dist < best)
      {
        best = dist;
        toClosest = me.directionTo(robot.location);
      }
    }
    //System.out.println("I'm in pursuit!");
    if(tryMove(toClosest, rc))
      System.out.println("Moving");
    else
      System.out.println("Something wrong with movement");
    return 1;
     */
    return -1;
  }

  static int empower(RobotController rc, int actionRadius, RobotInfo[] enemy, RobotInfo[] neutral) throws GameActionException
  {
//    if (attackable.length != 3 && rc.canEmpower(actionRadius)) {
//      System.out.println("empowering...");
//      rc.empower(actionRadius);
//      System.out.println("empowered");
//      return;
//    }
    if(rc.canEmpower(actionRadius)){
      if(enemy.length > 0 || neutral.length > 0){
        rc.empower(actionRadius);
        return 1;
      }
      return 0;
    }
    return -1;
  }
}
