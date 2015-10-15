package team018;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {
	private Direction dirToEnemHQ;
	public Soldier(RobotController rc) {
		super(rc);
		dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
	}

	public void run() {
		while (true) {
			MapLocation location = rc.getLocation();
			try {
				// sense and destroy mines
				if(rc.canMove(dirToEnemHQ) == false && location.distanceSquaredTo(enemyHQLoc) > 5){
					moveRandomly();
				} else if(rc.senseMine(location.add(dirToEnemHQ)) != null){
					rc.defuseMine(location.add(dirToEnemHQ));
				} else if (location.distanceSquaredTo(enemyHQLoc) > 5){
					rc.move(dirToEnemHQ);
				} else{
					rc.move(rc.getLocation().directionTo(enemyHQLoc));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	}
	
	private void moveRandomly() throws GameActionException{
		int randInt = rand.nextInt(8);
		MapLocation spot = rc.getLocation().add(directions[randInt]);
		if(rc.senseMine(spot) == null){
			rc.move(directions[randInt]);
		} else {
			rc.defuseMine(spot);
		}
	}
}
