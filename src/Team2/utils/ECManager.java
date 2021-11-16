package Team2.utils;

import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;

/**
 * Class to manage list of neutral and hostile EC's
 */
public class ECManager
{
  //keep track of the known neutralECs
  static Set<RobotInfo> neutralECs = new HashSet<RobotInfo>();
  static Set<RobotInfo> enemyECs = new HashSet<RobotInfo>();
  
  public static boolean addEc(RobotInfo robot, RobotController rc)
  {
    return isEC(robot) && addToList(robot, rc);
  }
  
  /**Make sure that all the lists are up-to-date!*/
  public static boolean updateLists(RobotController rc)
  {
    removedChangedTeamECs(neutralECs, Team.NEUTRAL);
    removedChangedTeamECs(enemyECs, rc.getTeam().opponent());
    return true;
  }
  /** Returns true if robot is an EC*/
  private static boolean isEC(RobotInfo robot)
  {
    return robot.getType().equals(RobotType.ENLIGHTENMENT_CENTER);
  }
  
  /**If EC isn't friendly, add it to the neutral or enemy EC list */
  private static boolean addToList(RobotInfo robot, RobotController rc)
  {
      return addToList(robot, Team.NEUTRAL, neutralECs) ||
        addToList(robot, rc.getTeam().opponent(), enemyECs);
  }
  
  /** Returns true if Robot was sucessfully added to the list*/
  private static boolean addToList(RobotInfo robot, Team team, Set<RobotInfo> list)
  {
    if(robot.getTeam() == team)
    {
      list.add(robot);
      return true;
    }
    return false;
  }
  
  /**Remove EC's who are no longer on the list's team*/
  private static boolean removedChangedTeamECs(Set<RobotInfo> list, Team team)
  {
    list.removeIf(robot -> !robot.getTeam().equals(team));
    return true;
  }
}
