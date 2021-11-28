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

  public static void justMakeFlag(RobotController rc, int flagValue, boolean can, int bytes) throws GameActionException {
    if (can && bytes > 0) {
      rc.setFlag(flagValue);
    }
  }

  public static RobotType makeFlag(RobotController rc, int flagValue, Direction d, RobotType r, int lastR) throws GameActionException {
    rc.buildRobot(r, d, influence);
    if (lastR == 3) {
      lastRobot = 0;
    } else {
      lastRobot++;
    }
    justMakeFlag(rc, flagValue, rc.canSetFlag(flagValue++), Clock.getBytecodesLeft());
    return r;
  }

  public static RobotType makePol(RobotController rc, int flagValue, int last, Direction d, boolean canPol) throws GameActionException {
    if (canPol) {
      return makeFlag(rc, 0, d, RobotType.POLITICIAN, last);
    }
    justMakeFlag(rc, flagValue, rc.canSetFlag(flagValue++), Clock.getBytecodesLeft());
    return null;
  }

  public static RobotType makeSlan(RobotController rc, int flagValue, int last, Direction d, boolean canSlan) throws GameActionException {
    if (canSlan) {
      return makeFlag(rc, 0, d, RobotType.SLANDERER, last);
    }
    justMakeFlag(rc, flagValue, rc.canSetFlag(flagValue++), Clock.getBytecodesLeft());
    return null;
  }

  public static RobotType makeMuck(RobotController rc, int flagValue, int last, Direction d, boolean canMuck) throws GameActionException {
    if (canMuck) {
      return makeFlag(rc, 0, d, RobotType.MUCKRAKER, last);
    }
    justMakeFlag(rc, flagValue, rc.canSetFlag(flagValue++), Clock.getBytecodesLeft());
    return null;
  }

  public static void ecBid(RobotController rc, int inf, int curr_inf) throws GameActionException {
    int bid_num = curr_inf - inf;
    if (bid_num >= 0) {
      rc.bid(bid_num);
    }
  }

  public static void ftMakeMuck(RobotController rc, Direction d, int sp, int m, boolean canMuck) throws GameActionException {
    if (canMuck && ((sp % 20) == 0) && (sp != 0) && (m != 1)) {
      makeMuck(rc, 0, 2, d, rc.canBuildRobot(RobotType.MUCKRAKER, d, influence));
      muck = 1;
    }
  }

  public static void ftMakeRobots(RobotController rc, Direction d, int swit, boolean canPol, boolean canSlan) throws GameActionException {
    ftMakeMuck(rc, d, numSP, muck, rc.canBuildRobot(RobotType.MUCKRAKER, d, influence));
    if (canPol && swit == 0) {
      makePol(rc, 0, 3, d, rc.canBuildRobot(RobotType.POLITICIAN, d, influence));
      switchSPM = 1;
      numSP++;
      muck = 0;
    } if (canSlan && swit == 1) {
      makeSlan(rc, 0, 1, d, rc.canBuildRobot(RobotType.SLANDERER, d, influence));
      switchSPM = 0;
      numSP++;
      muck = 0;
    }
  }

  public static void firstThird(RobotController rc, int inf) throws GameActionException {
    if (inf >= 50) {
      for (Direction dir : directions) {
        ftMakeRobots(rc, dir, switchSPM, rc.canBuildRobot(RobotType.POLITICIAN, dir, influence), rc.canBuildRobot(RobotType.SLANDERER, dir, influence));
      }
    }
    ecBid(rc, 50, rc.getInfluence());
  }

  public static void sdMakeSlan (RobotController rc, Direction d, boolean canSlan, int swit) throws GameActionException {
    if (canSlan && swit < 10 && swit > 2) {
      makeSlan(rc, 0, 1, d, rc.canBuildRobot(RobotType.SLANDERER, d, influence));
      switchSPM++;
    }
  }

  public static void sdMakeRobots(RobotController rc, Direction d, boolean canMuck, boolean canPol, int swit) throws GameActionException {
    if (canMuck && swit < 3) {
      makeMuck(rc, 0, 2, d, rc.canBuildRobot(RobotType.MUCKRAKER, d, influence));
      switchSPM++;
    }
    sdMakeSlan(rc, d, rc.canBuildRobot(RobotType.SLANDERER, d, influence), switchSPM);
    if (canPol && swit == 10) {
      makePol(rc, 0, 3, d, rc.canBuildRobot(RobotType.POLITICIAN, d, influence));
      switchSPM = 0;
    }
  }

  public static void secondThird(RobotController rc, int inf) throws GameActionException {
    if (inf > 100) {
      for (Direction dir : directions) {
        sdMakeRobots(rc, dir, rc.canBuildRobot(RobotType.MUCKRAKER, dir, influence), rc.canBuildRobot(RobotType.POLITICIAN, dir, influence), switchSPM);
      }
    }
    ecBid(rc, 50, rc.getInfluence());
  }

  public static void ltMakePol (RobotController rc, Direction d, int inf, boolean canPol) throws GameActionException {
    if (canPol && (inf >= 50)) {
      makePol(rc, 0, 3, d, rc.canBuildRobot(RobotType.POLITICIAN, d, influence));
    }
  }

  public static void ltMakeRobots(RobotController rc, Direction d, int inf, boolean canSlan, boolean canMuck) throws GameActionException {
    ltMakePol(rc, d, rc.getInfluence(), rc.canBuildRobot(RobotType.POLITICIAN, d, influence));
    if (inf > 50) {
      if (canSlan) {
        makeSlan(rc, 0, 1, d, rc.canBuildRobot(RobotType.SLANDERER, d, influence));
      }
      if (canMuck) {
        makeMuck(rc, 0, 2, d, rc.canBuildRobot(RobotType.MUCKRAKER, d, influence));
      }
    }
  }

  public static void lastThird(RobotController rc, int inf) throws GameActionException {
    for (Direction dir : directions) {
      if (inf > 90) {
        ltMakeRobots(rc, dir, rc.getInfluence(), rc.canBuildRobot(RobotType.SLANDERER, dir, influence), rc.canBuildRobot(RobotType.MUCKRAKER, dir, influence));
      }
    }
    ecBid(rc, 75, rc.getInfluence());
  }

  public static void run(RobotController rc, int round) throws GameActionException {
    if (round < 501) {
      firstThird(rc, rc.getInfluence());
    }
    else if (round > 501 && round < 1001) {
      secondThird(rc, rc.getInfluence());
    }
    else {
      lastThird(rc, rc.getInfluence());
    }
  }
}