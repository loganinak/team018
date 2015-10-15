package team018;

import battlecode.common.Direction;
import battlecode.common.GameActionException;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;
import battlecode.common.Upgrade;

public class Soldier extends DefaultRobot {
	private Direction dirToEnemHQ;
	public Soldier(RobotController rc) {
		super(rc);
		dirToEnemHQ = rc.getLocation().directionTo(enemyHQLoc);
	}

	public void run() 
	{
		while (true)
		{
			
			MapLocation location = rc.getLocation();
			Direction spiral = location.directionTo(HQloc).rotateLeft();
			Direction toEnem = location.directionTo(enemyHQLoc);
			try
			{
				if(location.distanceSquaredTo(HQloc)<=25)
				{
					if(rc.canMove(spiral))
						{
							if(rc.senseMine(location.add(spiral))!=null) rc.defuseMine(location.add(spiral));
							rc.move(spiral);
						}
					else
						{
							if(rc.senseMine(location.add(spiral.rotateLeft().rotateLeft()))!=null) rc.defuseMine(location.add(spiral.rotateLeft().rotateLeft()));
							rc.move(spiral.rotateLeft().rotateLeft());
						}
				}
				
				if(location.distanceSquaredTo(enemyHQLoc)<=1)
				{
					if(rc.canMove(toEnem))
						{
							rc.move(toEnem);
						}
					else
						{
						rc.move(toEnem.rotateLeft());
						}
				}
				else
				{
//					VERY basic pathfinding towards enemy base
//					!=null		means there is a mine
//					==null		means there is no mine
					if(rc.senseMine(location.add(toEnem))==null)
						{
							rc.move(toEnem);
						}
					else if(rc.senseMine(location.add(toEnem.rotateRight()))==null)
						{
							rc.move(toEnem.rotateRight());
						}
					else if(rc.senseMine(location.add(toEnem.rotateLeft()))==null)
						{
							rc.move(toEnem.rotateLeft());
						}
					else 
						{
							rc.defuseMine(location.add(toEnem));
						}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			rc.yield();
		}
	}
	
	private void moveRandomly() throws GameActionException{
		int randInt = rand.nextInt(8);
		MapLocation spot = rc.getLocation().add(directions[randInt]);
		if(rc.senseMine(spot) == null){
			rc.move(directions[randInt]);
		} else {
			rc.defuseMine(spot);
		}
	}
}
