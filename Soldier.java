package team018;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {
	private Direction dirToEnemHQ;
	private int chance;
	private int soldier;
	private int defender;
	private int mineSetter;
	private int task;
	

	public Soldier(RobotController rc) {
		super(rc);
		dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
		chance = rand.nextInt(10);
	}
	//TASK 0 = SOLDIER
	//TASK 1 = DEFENDER
	//TASK 2 = MINESETTER
	public void run() {
		while (true) {
			MapLocation location = rc.getLocation();
			try {
				if (chance != 9 && chance != 8 && chance != 7){
					soldier = 0;
					task = 0;
				} else if (chance == 9 || chance == 8) {
					defender = 1;
					task = 1;
				} else if (chance == 7){
					mineSetter = 1;
					task = 2;
				}
				if (task == soldier) {
					// sense and destroy mines
					if (rc.canMove(dirToEnemHQ) == false && location.distanceSquaredTo(enemyHQLoc) > 5) {
						moveRandomly();
					} else if (rc.senseMine(location.add(dirToEnemHQ)) != null) {
						rc.defuseMine(location.add(dirToEnemHQ));
					} else if (location.distanceSquaredTo(enemyHQLoc) > 5) {
						rc.move(dirToEnemHQ);
					} else {
						rc.move(rc.getLocation().directionTo(enemyHQLoc));
					}
				} else if (task == defender){
						cyclone();
				} else if (task == mineSetter){
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	}

	private void moveRandomly() throws GameActionException {
		int randInt = rand.nextInt(8);
		MapLocation spot = rc.getLocation().add(directions[randInt]);
		if (rc.senseMine(spot) == null) {
			rc.move(directions[randInt]);
		} else {
			rc.defuseMine(spot);
		}
	}
	
	private void cyclone() throws GameActionException{
		
	}
}
