package team018;

import battlecode.common.*;

public class RobotPlayer {
	static RobotController rcGlob;
	static MapLocation location;
	static MapLocation enemyHQ;

	public static void run(RobotController rc) {
		rcGlob = rc;
		enemyHQ = rc.senseEnemyHQLocation();
		
		while (true) {
			location = rc.getLocation();
			try {
				if (rc.getType() == RobotType.HQ) {
					// spawn soldier
					Direction dir = location.directionTo(rc.senseEnemyHQLocation());
					if (rc.canMove(dir))
						rc.spawn(dir);
				} else if (rc.getType() == RobotType.SOLDIER) {
					// sense and destroy mines
					if(rc.canMove(location.directionTo(enemyHQ)) == false && location.distanceSquaredTo(enemyHQ) > 5){
						if(rc.senseMine(location.add(Direction.NORTH)) == null){
							rc.move(Direction.NORTH);
						} else {
							rc.defuseMine(location.add(Direction.NORTH));
						}
					
//						Trent was here
					} else if (rc.senseMine(location.add(location.directionTo(rc.senseEnemyHQLocation()))) != null) {
						rc.defuseMine(location.add(location.directionTo(rc.senseEnemyHQLocation())));
					} else {
						rc.move(location.directionTo(rc.senseEnemyHQLocation()));
						rc.setIndicatorString(0, "test");
					}
				}
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}