package team018;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {

	private Direction dirToEnemHQ;
	private Direction movingDir;
	private Direction lastMovingDir;

	public Soldier(RobotController rc) throws GameActionException {
		super(rc);
		roundCount = rc.readBroadcast(roundCountChan);
	}

	public void run() {
		while (true) {
			dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
			MapLocation location = rc.getLocation();
			try {
				lastMovingDir = movingDir;
				movingDir = getDirTowTarAvoidMines(dirToEnemHQ);
				MapLocation movingLoc = location.add(movingDir);

				boolean mine = rc.senseMine(movingLoc) != null;
				if (mine) {
					rc.defuseMine(movingLoc);
					lastMovingDir = movingDir;
				} else if (rc.canMove(movingDir) && !mine) {
					rc.move(movingDir);
					lastMovingDir = movingDir;
				} else {// does this if it can't move towards the base
					movingLoc = location.add(lastMovingDir);
					mine = rc.senseMine(movingLoc) != null;
					if (mine) {
						rc.defuseMine(movingLoc);
					} else if (rc.canMove(movingDir) && !mine) {
						rc.move(lastMovingDir);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.setIndicatorString(1, "round: " + Integer.toString(roundCount));
			roundCount++;
			rc.yield();
		}
	}
}
