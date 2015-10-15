package team018;

import java.util.Random;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public abstract class DefaultRobot {
	
	protected final RobotController rc;
	protected final MapLocation enemyHQLoc;
	protected final MapLocation HQloc;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static Random rand;
	
	DefaultRobot(RobotController rc){
		this.rc = rc;
		enemyHQLoc = rc.senseEnemyHQLocation();
		HQloc = rc.senseHQLocation();
		rand = new Random(rc.getRobot().getID());
	}
	
	void run() {
	}
}
