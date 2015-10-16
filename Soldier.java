package team018;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {

	private Direction dirToEnemHQ;
	private Direction dirToHQ;
	private Direction movingDir;
	private Direction lastMovingDir;

	private final int soldier = 0;
	private final int defender = 1;
	private final int builder = 2;
	private int task;

	// what will spawn if you get to the end of the buildOrder array
	private int defaultUnit = 0;
	// Change this to change build order
	private int[] buildOrder = { 0, 0 };

	public Soldier(RobotController rc) throws GameActionException {
		super(rc);
		roundCount = rc.readBroadcast(roundCountChan);
		int needDefense = readDataScram(defenseNeedChan);

		if (needDefense == 1) {
			task = 1;
		} else {
			int buildIndex = readDataScram(spawnChannel) - 1;
			if (buildIndex > buildOrder.length - 1) {
				task = defaultUnit;
			} else {
				task = buildOrder[buildIndex];
			}
		}

		String role;
		switch (task) {
		case 0:
			role = "Soldier";
			break;
		case 1:
			role = "Defender";
			break;
		case 2:
			role = "builder";
			break;
		default:
			role = "None";
			break;
		}
		rc.setIndicatorString(0, "Role: " + role);
	}

	public void run() {
		while (true) {
			dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
			dirToHQ = rc.getLocation().directionTo(HQLoc);
			try {
				if (task == soldier) {
					soldier();
				} else if (task == defender) {
					spiralLoc(HQLoc);
				} else if (task == builder) {
					rc.move(Direction.NORTH);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.setIndicatorString(1, "round: " + Integer.toString(roundCount));
			roundCount++;
			rc.yield();
		}
	}

	private void soldier() throws GameActionException {
		lastMovingDir = movingDir;
		movingDir = getDirTowTarAvoidMines(dirToEnemHQ);
		MapLocation movingLoc = rc.getLocation().add(movingDir);

		boolean mine = rc.senseMine(movingLoc) != null;
		if (readDataScram(attackCntChan) < 11) {
			spiralLoc(midLoc(HQLoc, enemyHQLoc));
		} else {
			if (rc.getLocation().distanceSquaredTo(enemyHQLoc) > 4) {
				if (mine) {
					rc.defuseMine(movingLoc);
					lastMovingDir = movingDir;
				} else if (rc.canMove(movingDir) && !mine) {
					rc.move(movingDir);
					lastMovingDir = movingDir;
				} else {// does this if it can't move towards the base
					movingLoc = rc.getLocation().add(lastMovingDir);
					mine = rc.senseMine(movingLoc) != null;
					if (mine) {
						rc.defuseMine(movingLoc);
					} else if (rc.canMove(movingDir) && !mine) {
						rc.move(lastMovingDir);
					}
				}
			} else {
				spiralLoc(enemyHQLoc);
			}
		}
	}

	private void spiralLoc(MapLocation target) throws GameActionException {
		Direction spiral = rc.getLocation().directionTo(target).rotateLeft();
		if (rc.canMove(spiral)) {
			if (rc.senseMine(rc.getLocation().add(spiral)) != null) {
				rc.defuseMine(rc.getLocation().add(spiral));
			} else {
				rc.move(spiral);
			}
		} else {
			if (rc.senseMine(rc.getLocation().add(spiral.rotateLeft().rotateLeft())) != null) {
				rc.defuseMine(rc.getLocation().add(spiral.rotateLeft()));
			} else {
				rc.move(spiral.rotateLeft().rotateLeft());
			}

		}
	}
}
