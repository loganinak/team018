package team018;

import java.util.Random;
import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public abstract class DefaultRobot {
	
	protected final RobotController rc;
	protected final MapLocation enemyHQLoc;
	protected final MapLocation HQloc;
	static Direction[] directions = {Direction.NORTH, Direction.NORTH_EAST, Direction.EAST, Direction.SOUTH_EAST, Direction.SOUTH, Direction.SOUTH_WEST, Direction.WEST, Direction.NORTH_WEST};
	static Random rand;
	
	DefaultRobot(RobotController rc){
		this.rc = rc;
		enemyHQLoc = rc.senseEnemyHQLocation();
		HQloc = rc.senseHQLocation();
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
}
