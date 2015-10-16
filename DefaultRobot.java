package team018;

import java.util.Random;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public abstract class DefaultRobot {
	protected int roundCountChan = 32566;
	
	protected int roundCount = 0;
	protected int spawnBrodcastInt = 0;
	protected int spawnChannel[] = {50, 1000, 999, 345, 6000, 10000, 986, 666, 999, 55404, 9930, 58483, 23454};
	
	protected final RobotController rc;
	protected final MapLocation enemyHQLoc;
	protected final MapLocation HQLoc;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static Random rand;
	
	DefaultRobot(RobotController rc){
		this.rc = rc;
		enemyHQLoc = rc.senseEnemyHQLocation();
		HQLoc = rc.senseHQLocation();
		rand = new Random(rc.getRobot().getID());
	}
	
	void run() {
	}
	
	protected Direction getDirTowTarAvoidMines(Direction target){
		if(rc.canMove(target) && rc.senseMine(rc.getLocation().add(target)) == null){
			return target;
		} else if(rc.canMove(target.rotateLeft()) && rc.senseMine(rc.getLocation().add(target.rotateLeft())) == null){
			return target.rotateLeft();
		} else if(rc.canMove(target.rotateRight()) && rc.senseMine(rc.getLocation().add(target.rotateRight())) == null){
			return target.rotateRight();
		}
		return target;
	}
	
	protected Direction randDirNoMines(){
		boolean spotFound = false;
		int  i = 0;
		while(i < 8 && !spotFound){
			if(rc.senseMine(rc.getLocation().add(directions[i])) == null){
				return directions[i];
			}
		}
		return null;
	}
	
	protected void broadcastDataScram(int[] channel, int data) throws GameActionException{
		int broadcastChan = roundCount;
		while(broadcastChan > channel.length - 1){
			broadcastChan = broadcastChan - (channel.length);
		}
		rc.broadcast(channel[broadcastChan], data);
	}
	
	protected int readDataScram(int[] channel) throws GameActionException{
		int broadcastChan = roundCount;
		while(broadcastChan > channel.length - 1){
			broadcastChan = broadcastChan - (channel.length);
		}
		return rc.readBroadcast(channel[broadcastChan]);
	}
}
