package team018;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {
	public Soldier(RobotController rc){
		super(rc);
	}
	
	public void run(){
		while(true){
			MapLocation location = rc.getLocation();
			try {
					if (location.distanceSquaredTo(enemyHQLoc) <2) {
						rc.attackSquare(location.add(Direction.NORTH));
					} else if (rc.senseMine(location.add(rc.getLocation().directionTo(rc.senseEnemyHQLocation()))) != null) {
						rc.defuseMine(location.add(location.directionTo(rc.senseEnemyHQLocation())));
					} else {
						rc.move(location.directionTo(rc.senseEnemyHQLocation()));
						rc.setIndicatorString(0, "test");
					}
				
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	}
}
