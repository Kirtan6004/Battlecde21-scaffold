package Team2.robots;

import Team2.RobotPlayer;
import battlecode.common.*;

public class Politician extends AbstractRobot
{
  public static void run(RobotController rc) throws GameActionException
  {
    Team enemy = rc.getTeam().opponent();
    int actionRadius = rc.getType().actionRadiusSquared;

    canattackanenemy(rc, actionRadius,enemy);
    empower(rc, actionRadius, rc.senseNearbyRobots(actionRadius,enemy),
          rc.senseNearbyRobots(actionRadius, Team.NEUTRAL));
    int turnCount = pursueNeutralECs(rc, rc.senseNearbyRobots(actionRadius,enemy), rc.senseNearbyRobots(actionRadius, Team.NEUTRAL));
    if( turnCount < 0)
    {
      tryRandomMove(rc);
    }
  }

  /**
   * @return -1 if the total number of elements is smaller than 12, 0 is [12,800], if its bigger than 800 return 1
   * @throws GameActionException
   */
  public static int pursueNeutralECs(RobotController rc,RobotInfo[] enemy, RobotInfo[] neutral) throws GameActionException
  {
      int turnCount = enemy.length + neutral.length;
      if(turnCount <=12)
        return -1;
      else if(turnCount >800)
        return 1;
      return 0;
//    if (turnCount <= 12) {
//      for (RobotInfo ally :rc.senseNearbyRobots(2, allyTeam)) {
//        if (ally.getType().canBid()){
//          homeID = ally.getID();
//          homeLoc = ally.getLocation();
//        }
//      }
//    } else if (turnCount > 800) {
//      directionality = Direction.CENTER;
//    }
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

  }

  // Can attack an enemy base or muckraker
  public static int canattackanenemy(RobotController rc,  int actionRadius, Team enemyTeam) throws GameActionException {
    RobotInfo[] attackable = rc.senseNearbyRobots(actionRadius, enemyTeam);
    for (RobotInfo enemy : attackable) {
      if (enemy.type == RobotType.ENLIGHTENMENT_CENTER){
        if (rc.canEmpower(actionRadius)){
          rc.empower(actionRadius);
          return 1;
        }
      }
      return 0;
    }
    return -1;
  }
  public static int empower(RobotController rc, int actionRadius, RobotInfo[] enemy, RobotInfo[] neutral) throws GameActionException
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
