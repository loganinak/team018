package team018;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Upgrade;

public class HQ extends DefaultRobot {
	private int soldierCount = 0;

	public HQ(RobotController rc) {
		super(rc);
	}

	public void run() {
		while (true) {
			MapLocation location = rc.getLocation();
			try {
				if (rc.isActive()) {
					if (roundCount < 45 && roundCount != 0 && roundCount != 30) {
						rc.researchUpgrade(Upgrade.DEFUSION);
					} else {
						// spawn soldier
						Direction dir = location.directionTo(enemyHQLoc);

						dir = getDirTowTarAvoidMines(dir);
						if (dir == null) {
							dir = randDirNoMines();
						}
						if (dir == null) {
							dir = directions[rand.nextInt(8)];
						}
						rc.spawn(dir);
						soldierCount++;
					}
					rc.broadcast(roundCountChan, roundCount);
					broadcastDataScram(spawnChannel, soldierCount);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			rc.setIndicatorString(1, "round: " + Integer.toString(roundCount));
			roundCount++;
			rc.yield();
		}
	}
}
