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
				//RESEARCHES A NUKE
				rc.researchUpgrade(Upgrade.NUKE);
				rc.yield();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}