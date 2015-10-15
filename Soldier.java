package team018;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {
	public Soldier(RobotController rc) {
		super(rc);
	}

	public void run() {
		while (true) {
			MapLocation location = rc.getLocation();
			try {
				// sense and destroy mines
				if(rc.canMove(location.directionTo(enemyHQLoc)) == false && location.distanceSquaredTo(enemyHQLoc) > 5){
					if(rc.senseMine(location.add(Direction.NORTH)) == null){
						rc.move(Direction.NORTH);
					} else {
						rc.defuseMine(location.add(Direction.NORTH));
					}
				

				} else if (rc.senseMine(location.add(location.directionTo(rc.senseEnemyHQLocation()))) != null) {
					rc.defuseMine(location.add(location.directionTo(rc.senseEnemyHQLocation())));
				} else {
					rc.move(location.directionTo(rc.senseEnemyHQLocation()));
					rc.setIndicatorString(0, "test");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	}
}
