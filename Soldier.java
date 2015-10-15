package team018;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {

	private Direction dirToEnemHQ;
	private Direction movingDir;
	private Direction lastMovingDir;

	private final int soldier = 0;
	private final int defender = 1;
	private final int builder = 2;
	private int task;

	public Soldier(RobotController rc) {
		super(rc);
		roundCount = rc.readBroadcast(roundCountChan);
		int temp = rand.nextInt(5);
		if (temp == 0) {
			temp = rand.nextInt(2);
			if (temp == 0) {
				task = defender;
			} else {
				task = builder;
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
					rc.move(Direction.SOUTH);
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

}
