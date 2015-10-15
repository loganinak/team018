package team018;

import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public abstract class DefaultRobot {
	
	protected final RobotController rc;
	protected final MapLocation enemyHQLoc;
	protected final MapLocation HQloc;
	
	DefaultRobot(RobotController rc){
		this.rc = rc;
		enemyHQLoc = rc.senseEnemyHQLocation();
		HQloc = rc.senseHQLocation();
	}
	
	void run() {
	}
}
