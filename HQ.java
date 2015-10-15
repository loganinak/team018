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
				boolean spawned = false;
				Direction dir = location.directionTo(enemyHQLoc);
				if (rc.canMove(dir) && rc.senseMine(location.add(dir)) == null){
					rc.spawn(dir);
					spawned = true;
				} else{
					int i = 0;
					while(i < 7 && spawned == false){
						if(rc.senseMine(location.add(directions[i])) == null){
							rc.spawn(directions[i]);
							spawned = true;
						} 
					}
				}
				if(spawned == false){
					rc.spawn(dir);
				}
			} catch(Exception e){
				e.printStackTrace();
			}
			rc.yield();
		}
	}
}
