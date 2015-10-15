package team018;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class HQ extends DefaultRobot{
	public HQ(RobotController rc){
		super(rc);
	}
	
	public void run(){
		while(true){
			MapLocation location = rc.getLocation();
			try{
				// spawn soldier
				Direction dir = location.directionTo(rc.senseEnemyHQLocation());
				if (rc.canMove(dir)){
					rc.spawn(dir);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			rc.yield();
		}
	}
}
