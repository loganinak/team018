package team018;

import battlecode.common.Clock;
import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.Robot;
import battlecode.common.RobotController;
import battlecode.common.RobotInfo;
import battlecode.common.RobotType;

public class Soldier extends DefaultRobot {

	private Direction dirToEnemHQ;
	private Direction movingDir;
	private Direction lastMovingDir;
	boolean attacking = false;
	private MapLocation attackSwarmLoc;
	
	private int defenseReq = 18;
	
	private int task;
	private final int attacker = 0;
	private int attackReq = 12;
	
	private final int defender = 1;
	
	private final int builder = 2;
	private MapLocation[] encampments;
	private MapLocation minEncamp;

	// what will spawn if you get to the end of the buildOrder array
	private int defaultUnit = 0;
	// Change this to change build order
	private int[] buildOrder = { 0, 0 };

	public Soldier(RobotController rc) throws GameActionException {
		super(rc);
		int numDefenseBots = readDataScram(defenseNeedChan);

		if (numDefenseBots < defenseReq) {
			task = defender;
		} else {
			int buildIndex = readDataScram(spawnChannel) - 1;
			if (buildIndex > buildOrder.length - 1) {
				task = defaultUnit;
			} else {
				task = buildOrder[buildIndex];
			}
		}
		
		if(readDataScram(spawnChannel) == 1){
			task = builder;
		}
		
		String role;
		switch (task) {
		case attacker:
			role = "Attacker";
			attackSwarmLoc = midLoc(enemyHQLoc, HQLoc);
//			if(HQLoc.x != enemyHQLoc.x && HQLoc.y != enemyHQLoc.y){
//				attackSwarmLoc = new MapLocation(attackSwarmLoc.x + enemyHQLoc.x, attackSwarmLoc.y);
//			} else if(rc.getMapHeight()/2 < HQLoc.y){
//				attackSwarmLoc = new MapLocation(attackSwarmLoc.x, attackSwarmLoc.y - rc.getMapHeight()/2);
//			} else {
//				attackSwarmLoc = new MapLocation(attackSwarmLoc.x, attackSwarmLoc.y + rc.getMapHeight()/2);
//			}
			break;
		case defender:
			role = "Defender";
			break;
		case builder:
			role = "Builder";
			encampments = rc.senseAllEncampmentSquares();
			int dist = 10000000;
			for(int i = 0; i < encampments.length; i++){
				int tempDist = rc.getLocation().distanceSquaredTo(encampments[i]);
				if(tempDist < dist){
					dist = tempDist;
					minEncamp = encampments[i];
				}
			}
			break;
		default:
			role = "None";
			break;
		}
		rc.setIndicatorString(0, "Role: " + role);
	}

	public void run() {
		while (true) {
			dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
			try {
				if (task == attacker) {
					if (rc.isActive()) {
						attacker();
					}
				} else if (task == defender) {
					if (rc.isActive()) {
						defender();
					}
				} else if (task == builder) {
					if (rc.isActive()) {
						builder();
					}
				}
			} catch (Exception e) {
				rc.setIndicatorString(1, e.getMessage());
				
				e.printStackTrace();
			}
			rc.yield();
		}
	}

	private void attacker() throws GameActionException {
		lastMovingDir = movingDir;
		movingDir = getDirTowTarAvoidMines(dirToEnemHQ);
		MapLocation movingLoc = rc.getLocation().add(movingDir);
		int numFriends = 0;

		if (attacking == false) {
			attacking = readDataScram(attackChan) == 1;
			numFriends = senseNumBotsAtLoc(attackSwarmLoc);
		}
		boolean mine = rc.senseMine(movingLoc) != null;
		if (attacking == false && numFriends < attackReq) {
				spiralLoc(attackSwarmLoc);	
		} else {
			if (readDataScram(attackChan) == 0) {
				attacking = true;
				broadcastDataScram(attackChan, 1);
			}
			if (rc.getLocation().distanceSquaredTo(enemyHQLoc) > 4) {
				if (mine) {
					rc.defuseMine(movingLoc);
					lastMovingDir = movingDir;
				} else if (rc.canMove(movingDir) && !mine) {
					rc.move(movingDir);
					lastMovingDir = movingDir;
				} else {// does this if it can't move towards the base
					movingLoc = rc.getLocation().add(lastMovingDir);
					mine = rc.senseMine(movingLoc) != null;
					if (mine) {
						rc.defuseMine(movingLoc);
					} else if (rc.canMove(lastMovingDir) && !mine) {
						rc.move(lastMovingDir);
					}
				}
			} else {
				spiralLoc(enemyHQLoc);
			}

		}
	}

	private void defender() throws GameActionException {
		Robot[] enemies = rc.senseNearbyGameObjects(Robot.class, 25, enemy);
		if (enemies.length > 0) {
			Direction enemDir = rc.getLocation().directionTo(rc.senseRobotInfo(enemies[0]).location);
			if(rc.canMove(enemDir) && rc.senseMine(rc.getLocation().add(enemDir)) == null){
				rc.move(enemDir);
			}
		} else {
			spiralLoc(HQLoc.add(dirToEnemHQ).add(dirToEnemHQ));
		}
	}

	private void builder() throws GameActionException{
		if(!rc.getLocation().equals(minEncamp)){
			moveToTarg(minEncamp);
		} else{
			rc.captureEncampment(RobotType.GENERATOR);
		}
	}
	
	private void spiralLoc(MapLocation target) throws GameActionException {
		if(rc.getLocation().equals(target)){
			return;
		}
		Direction spiral = rc.getLocation().directionTo(target).rotateLeft();
		if (rc.canMove(spiral)) {
			if (rc.senseMine(rc.getLocation().add(spiral)) != null) {
				rc.defuseMine(rc.getLocation().add(spiral));
			} else{
				rc.move(spiral);
			}
		} else if (rc.canMove(spiral.rotateLeft())) {
			if (rc.senseMine(rc.getLocation().add(spiral.rotateLeft())) != null) {
				rc.defuseMine(rc.getLocation().add(spiral.rotateLeft()));
			} else {
				rc.move(spiral.rotateLeft());
			}

		} else {
			Direction randDir = randDirNoMines();
			if(rc.canMove(randDir)){
				rc.move(randDir);
			}
		}
	}
	
	private void moveToTarg(MapLocation target) throws GameActionException{
		Direction dirToTar = getDirTowTarAvoidMines(rc.getLocation().directionTo(target));
		if(dirToTar == null && rc.canMove(lastMovingDir)){
			if(rc.senseMine(rc.getLocation().add(lastMovingDir)) == null){
				rc.move(lastMovingDir);
			} else {
				rc.defuseMine(rc.getLocation().add(lastMovingDir));
			}
		}else if(rc.senseMine(rc.getLocation().add(dirToTar)) == null){
			rc.move(dirToTar);
		} else {
			rc.defuseMine(rc.getLocation().add(dirToTar));
		}
	}
}

