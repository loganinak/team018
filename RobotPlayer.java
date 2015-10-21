package team018;

import battlecode.common.*;

public class RobotPlayer {
	public static void run(RobotController rc) throws GameActionException {
		DefaultRobot bot = null;
		switch(rc.getType()){
		case HQ:
			bot = new HQ(rc);
			break;
		case MEDBAY:
			bot = new NoActionBot(rc);
			break;
		case GENERATOR:
			bot = new NoActionBot(rc);
			break;
		case SOLDIER:
			bot = new Soldier(rc);
			break;
		default:
			break;
		}
		bot.run();
	}
}