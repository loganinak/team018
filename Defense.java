package team018;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Defense extends DefaultRobot {
	private Direction dirToEnemHQ;
	public Defense(RobotController rc){
		super(rc);
		dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
	}
	
	public void run(){
		while (true){
			MapLocation location = rc.getLocation();
			try{
				if (location.distanceSquaredTo(HQloc) < 10){
					rc.move(Direction.NORTH);
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.yield();
		}
	}
}