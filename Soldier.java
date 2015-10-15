package team018;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {

	private Direction dirToEnemHQ;
	private Direction movingDir;
	private Direction lastMovingDir;
	int patrolCount = 0;
	private Direction[] patrolPat = {Direction.NORTH,Direction.EAST,Direction.EAST, Direction.SOUTH, Direction.SOUTH, Direction.SOUTH, Direction.SOUTH, Direction.WEST, Direction.WEST, Direction.WEST, Direction.WEST, Direction.NORTH, Direction.NORTH, Direction.NORTH, Direction.NORTH, Direction.EAST, Direction.EAST};

	private final int soldier = 0;
	private final int defender = 1;
	private final int builder = 2;
	private int task;

	public Soldier(RobotController rc) {
		super(rc);
		int temp = rand.nextInt(5);
		if (temp == 0) {
			temp = rand.nextInt(2);
			if (temp == 0) {
				task = defender;
			} else {
				task = defender;
			}
		} else {
			task = soldier;
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
			MapLocation location = rc.getLocation();
			try {
				if (task == soldier) {
					dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
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
				} else if (task == defender) {
					patrol(HQLoc);
				} else if (task == builder) {
					rc.move(Direction.NORTH);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	}

	private void patrol(MapLocation target) throws GameActionException {
		rc.move(patrolPat[patrolCount]);
		patrolCount++;
		
		if(patrolCount > patrolPat.length -1){
			patrolCount = 1;
		}
	}
}
