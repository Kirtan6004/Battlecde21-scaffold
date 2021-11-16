package Team2.robots;

import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;


public class EnlightenmentCenter extends AbstractRobot {
  static int turnCount;
  static int influence = 100;
  public static int lastRobot = 0;

  public static void run(RobotController rc) throws GameActionException {
    int votes;

    votes = rc.getTeamVotes();
    for (Direction dir : directions) {
      makeRobots(rc, lastRobot, dir);
    }

    int leftoverInf = rc.getInfluence();
    if (leftoverInf > 100) {
      int bid_num = leftoverInf - 100;
      rc.bid(bid_num);
    }
    int byteCodeLeft = Clock.getBytecodesLeft();
  }

  public static RobotType makeFlag(RobotController rc, int flagValue, Direction d, RobotType r, int lastR) throws GameActionException {
    rc.buildRobot(r, d, influence);
    if (lastR == 3) {
      lastRobot = 0;
    } else {
      lastRobot++;
    }
    if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0) {
      rc.setFlag(flagValue);
    }
    return r;
  }

  public static RobotType makePol(RobotController rc, int last, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.POLITICIAN, d, influence)) {
      return makeFlag(rc, 0, d, RobotType.POLITICIAN, last);
    }
    return null;
  }

  public static RobotType makeSlan(RobotController rc, int last, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.SLANDERER, d, influence)) {
      return makeFlag(rc, 0, d, RobotType.SLANDERER, last);
    }
    return null;
  }

  public static RobotType makeMuck(RobotController rc, int last, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.MUCKRAKER, d, influence)) {
      return makeFlag(rc, 0, d, RobotType.MUCKRAKER, last);
    }
    return null;
  }

  public static RobotType makeRobots(RobotController rc, int last, Direction d) throws GameActionException {
    if (last == 1) {
      return makeSlan(rc, last, d);
    } else if (last == 2) {
      return makeMuck(rc, last, d);
    } else {
      return makePol(rc, last, d);
    }
  }
}
