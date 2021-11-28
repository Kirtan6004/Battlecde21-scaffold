package Old.robots;

import battlecode.common.*;


public class EnlightenmentCenter extends AbstractRobot {
    static int turnCount;
    static int influence = 50;
    public static int lastRobot = 0;
    public static int switchSPM = 0;
    public static int numSP = 0;
    public static int numPM = 0;
    public static int muck = 0;
    public static int slan = 0;

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

    public static RobotType makePol(RobotController rc, int flagValue, int last, Direction d) throws GameActionException {
        if (rc.canBuildRobot(RobotType.POLITICIAN, d, influence)) {
            return makeFlag(rc, 0, d, RobotType.POLITICIAN, last);
        }
        if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0) {
            rc.setFlag(flagValue);
        }
        return null;
    }

    public static RobotType makeSlan(RobotController rc, int flagValue, int last, Direction d) throws GameActionException {
        if (rc.canBuildRobot(RobotType.SLANDERER, d, influence)) {
            return makeFlag(rc, 0, d, RobotType.SLANDERER, last);
        }
        if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0) {
            rc.setFlag(flagValue);
        }
        return null;
    }

    public static RobotType makeMuck(RobotController rc, int flagValue, int last, Direction d) throws GameActionException {
        if (rc.canBuildRobot(RobotType.MUCKRAKER, d, influence)) {
            return makeFlag(rc, 0, d, RobotType.MUCKRAKER, last);
        }
        if (rc.canSetFlag(flagValue++) && Clock.getBytecodesLeft() > 0) {
            rc.setFlag(flagValue);
        }
        return null;
    }

    public static void run(RobotController rc) throws GameActionException {
        int votes;
        votes = rc.getTeamVotes();

        if (rc.getRoundNum() < 501) {
            if (rc.getInfluence() >= 50) {
                for (Direction dir : directions) {
                    System.out.println("TC: " + rc.getRoundNum());
                    if (rc.canBuildRobot(RobotType.MUCKRAKER, dir, influence) && ((numSP % 20) == 0) && (numSP != 0) && (muck != 1)) {
                        makeMuck(rc, 0, 2, dir);
                        muck = 1;
                    } if (rc.canBuildRobot(RobotType.POLITICIAN, dir, influence) && switchSPM == 0) {
                        makePol(rc, 0, 3, dir);
                        switchSPM = 1;
                        numSP++;
                        muck = 0;
                    } if (rc.canBuildRobot(RobotType.SLANDERER, dir, influence) && switchSPM == 1) {
                        makeSlan(rc, 0, 1, dir);
                        switchSPM = 0;
                        numSP++;
                        muck = 0;
                    }
                }
            }
            int leftoverInf = rc.getInfluence();
            if (leftoverInf > influence) {
                int bid_num = leftoverInf - influence;
                rc.bid(bid_num);
            }
            int byteCodeLeft = Clock.getBytecodesLeft();
        }
        else if (rc.getRoundNum() > 501 && rc.getRoundNum() < 1001) {
            if (rc.getInfluence() > 100) {
                for (Direction dir : directions) {
                    if (rc.canBuildRobot(RobotType.MUCKRAKER, dir, influence) && switchSPM < 4) {
                        makeMuck(rc, 0, 2, dir);
                        switchSPM++;
                    } if (rc.canBuildRobot(RobotType.SLANDERER, dir, influence) && switchSPM < 8 && switchSPM > 3) {
                        makeSlan(rc, 0, 1, dir);
                        switchSPM++;
                    } if (rc.canBuildRobot(RobotType.POLITICIAN, dir, influence) && switchSPM == 8) {
                        makePol(rc, 0, 3, dir);
                        switchSPM = 0;
                    }
                }
            }
            int leftoverInf = rc.getInfluence();
            if (leftoverInf > influence) {
                int bid_num = leftoverInf - influence;
                rc.bid(bid_num);
            }
            int byteCodeLeft = Clock.getBytecodesLeft();
        }
        else if (rc.getRoundNum() >= 1001) {
            for (Direction dir : directions) {
                if (rc.canBuildRobot(RobotType.POLITICIAN, dir, influence) && (rc.getInfluence() >= 50)) {
                    makePol(rc, 0, 3, dir);
                }
                if (rc.canBuildRobot(RobotType.SLANDERER, dir, influence) && (rc.getInfluence() > 50)) {
                    makeSlan(rc, 0, 1, dir);
                }
                if (rc.canBuildRobot(RobotType.MUCKRAKER, dir, influence) && (rc.getInfluence() > 50)) {
                    makeMuck(rc, 0, 2, dir);
                }
            }
            int leftoverInf = rc.getInfluence();
            int bid_num = leftoverInf - influence;
            if (bid_num >= 0) {
                rc.bid(bid_num);
            }
            int byteCodeLeft = Clock.getBytecodesLeft();
        }
    }
}