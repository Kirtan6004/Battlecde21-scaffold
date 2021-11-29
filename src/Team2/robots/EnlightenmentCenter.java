package Team2.robots;

import battlecode.common.*;

import java.util.HashSet;
import java.util.Set;


public class EnlightenmentCenter extends AbstractRobot {
  static int influence = 50;
  public static int lastRobot = 0;
  public static int switchSPM = 0;
  public static int numSP = 0;
  public static int muck = 0;

  public static RobotType makeFlag(RobotController rc, int flagValue, Direction d, RobotType r, int lastR) throws GameActionException {
    rc.buildRobot(r, d, influence);
    if (lastR == 3) {
      lastRobot = 0;
    } else {
      lastRobot++;
    }
    setFlag(flagValue, rc);
    return r;
  }

  private static void setFlag(int flagValue, RobotController rc) throws GameActionException
  {
    if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0) {
      rc.setFlag(flagValue);
    }
  }

  public static RobotType makePol(RobotController rc, int flagValue, int last, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.POLITICIAN, d, influence)) {
      return makeFlag(rc, 0, d, RobotType.POLITICIAN, last);
    }
    setFlag(flagValue, rc);
    return null;
  }

  public static RobotType makeSlan(RobotController rc, int flagValue, int last, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.SLANDERER, d, influence)) {
      return makeFlag(rc, 0, d, RobotType.SLANDERER, last);
    }
    setFlag(flagValue, rc);
    return null;
  }



  public static RobotType makeMuck(RobotController rc, int flagValue, int last, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.MUCKRAKER, d, influence)) {
      return makeFlag(rc, 0, d, RobotType.MUCKRAKER, last);
    }
    setFlag(flagValue, rc);
    return null;
  }

  public static void ecBid(RobotController rc, int inf) throws GameActionException {
    int bid_num = rc.getInfluence() - inf;
    if (bid_num >= 0) {
      rc.bid(bid_num);
    }
  }

  public static void ftMakeRobots(RobotController rc, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.POLITICIAN, d, influence) && switchSPM == 0) {
      makePol(rc, 0, 3, d);
      switchSPM = 1;
      numSP++;
      muck = 0;
    } if (rc.canBuildRobot(RobotType.SLANDERER, d, influence) && switchSPM == 1) {
      makeSlan(rc, 0, 1, d);
      switchSPM = 0;
      numSP++;
      muck = 0;
    }
  }

  public static void firstThird(RobotController rc) throws GameActionException {
    if (rc.getInfluence() >= 50) {
      for (Direction dir : directions) {
        ftMakeRobots(rc, dir);
      }
    }
    ecBid(rc, 50);
  }

  public static void sdMakeSlan (RobotController rc, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.SLANDERER, d, influence) && switchSPM < 10 && switchSPM > 2) {
      makeSlan(rc, 0, 1, d);
      switchSPM++;
    }
  }

  public static void sdMakeRobots(RobotController rc, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.MUCKRAKER, d, influence) && switchSPM < 3) {
      makeMuck(rc, 0, 2, d);
      switchSPM++;
    }
    sdMakeSlan(rc, d);
    if (rc.canBuildRobot(RobotType.POLITICIAN, d, influence) && switchSPM == 10) {
      makePol(rc, 0, 3, d);
      switchSPM = 0;
    }
  }

  public static void secondThird(RobotController rc) throws GameActionException {
    if (rc.getInfluence() > 100) {
      for (Direction dir : directions) {
        sdMakeRobots(rc, dir);
      }
    }
    ecBid(rc, 50);
  }

  public static void ltMakePol (RobotController rc, Direction d) throws GameActionException {
    if (rc.canBuildRobot(RobotType.POLITICIAN, d, influence) && (rc.getInfluence() >= 50)) {
      makePol(rc, 0, 3, d);
    }
  }

  public static void ltMakeRobots(RobotController rc, Direction d) throws GameActionException {
    ltMakePol(rc, d);
    if (rc.getInfluence() > 50) {
      if (rc.canBuildRobot(RobotType.SLANDERER, d, influence)) {
        makeSlan(rc, 0, 1, d);
      }
      if (rc.canBuildRobot(RobotType.MUCKRAKER, d, influence)) {
        makeMuck(rc, 0, 2, d);
      }
    }
  }

  public static void lastThird(RobotController rc) throws GameActionException {
    for (Direction dir : directions) {
      if (rc.getInfluence() > 90) {
        ltMakeRobots(rc, dir);
      }
    }
    ecBid(rc, 75);
  }

  public static void run(RobotController rc) throws GameActionException {
    if (rc.getRoundNum() < 501) {
      firstThird(rc);
    }
    else if (rc.getRoundNum() > 501 && rc.getRoundNum() < 1001) {
      secondThird(rc);
    }
    else {
      lastThird(rc);
    }
  }
}
