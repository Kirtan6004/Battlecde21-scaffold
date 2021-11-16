package Team2.robots;

import battlecode.common.*;

public class Politician extends AbstractRobot
{
  public static void run(RobotController rc) throws GameActionException
  {
    Team enemy = rc.getTeam().opponent();
    int actionRadius = rc.getType().actionRadiusSquared;
  
    empower(actionRadius, rc.senseNearbyRobots(actionRadius,enemy),
          rc.senseNearbyRobots(actionRadius, Team.NEUTRAL));
    if(pursueNeutralECs() < 0)
      tryRandomMove(rc);
  }

  /**
   * @return -1 if no neutral ECs left, 1 if moving towards a neutral EC
   * @throws GameActionException
   */
  static int pursueNeutralECs(RobotController rc) throws GameActionException
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
}
