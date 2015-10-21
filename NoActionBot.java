package team018;

import battlecode.common.RobotController;

public class NoActionBot extends DefaultRobot {

	protected NoActionBot(RobotController rc) {
		super(rc);
	}

	@Override
	public void run() {
		while(true){
			rc.yield();
		}
	}
}
