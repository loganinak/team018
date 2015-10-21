package team018;

import java.util.Random;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.Team;

public abstract class DefaultRobot {
	
	protected int spawnBrodcastInt = 0;
	protected static int spawnChannel[] = {50, 1000, 999, 345, 6000, 10000, 986, 666, 999, 55404, 9930, 58483, 23454};
	protected static int defenseNeedChan[] = {10, 5625, 9561, 8489, 7512};
	protected static int attackChan[] = {5564, 9844, 25845, 36548, 8455, 45593};
	protected static Team enemy;
	
	protected final RobotController rc;
	protected final MapLocation enemyHQLoc;
	protected final MapLocation HQLoc;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static Random rand;
	
	protected DefaultRobot(RobotController rc){
		this.rc = rc;
		enemyHQLoc = rc.senseEnemyHQLocation();
		HQLoc = rc.senseHQLocation();
		rand = new Random(rc.getRobot().getID());
		enemy = rc.getTeam().opponent();
	}
	
	public abstract void run();
	
	protected Direction getDirTowTarAvoidMines(Direction target){
		if(rc.canMove(target) && rc.senseMine(rc.getLocation().add(target)) == null){
			return target;
		} else if(rc.canMove(target.rotateLeft()) && rc.senseMine(rc.getLocation().add(target.rotateLeft())) == null){
			return target.rotateLeft();
		} else if(rc.canMove(target.rotateRight()) && rc.senseMine(rc.getLocation().add(target.rotateRight())) == null){
			return target.rotateRight();
		}
		return null;
	}
	
	protected Direction randDirNoMines(){
		int  i = 0;
		while(i < 8){
			if(rc.senseMine(rc.getLocation().add(directions[i])) == null && rc.canMove(rc.getLocation().directionTo(rc.getLocation().add(directions[i])))){
				return directions[i];
			}
			i++;
		}
		return null;
	}
	
	protected void broadcastDataScram(int[] channel, int data) throws GameActionException{
		int broadcastChan = Clock.getRoundNum();
		while(broadcastChan > channel.length - 1){
			broadcastChan = broadcastChan - (channel.length);
		}
		rc.broadcast(channel[broadcastChan], data);
	}
	
	protected int readDataScram(int[] channel) throws GameActionException{
		int broadcastChan = Clock.getRoundNum();
		while(broadcastChan > channel.length - 1){
			broadcastChan = broadcastChan - (channel.length);
		}
		return rc.readBroadcast(channel[broadcastChan]);
	}
	
	protected MapLocation midLoc(MapLocation loc1, MapLocation loc2){
		return new MapLocation((loc1.x + loc2.x)/2, (loc1.y + loc2.y)/2);
	}
	
	protected int senseNumBotsAtLoc(MapLocation location){
		if(rc.getLocation().distanceSquaredTo(location) <= 9){
			return rc.senseNearbyGameObjects(Robot.class, location, 9, rc.getTeam()).length;
		}
		return -1;
	}
}
