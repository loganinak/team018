package team018;

import battlecode.common.*;

public class RobotPlayer {
	public static void run(RobotController rc) throws GameActionException {
		DefaultRobot bot;
		switch(rc.getType()){
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