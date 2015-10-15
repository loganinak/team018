package team018;

import java.util.Random;
import battlecode.common.*;

public class RobotPlayer {
	static RobotController rcGlob;
	static MapLocation location;
	static MapLocation enemyHQ;

	public static void run(RobotController rc) {
		Random rand = new Random();
		DefaultRobot bot;
		switch (rc.getType()) {
		case HQ:
			bot = new HQ(rc);
			bot.run();
			break;
		case SOLDIER:
			bot = new Soldier(rc);
			bot.run();
			break;
		default:
			break;
		}
	}
}