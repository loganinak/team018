package team018;

import battlecode.common.Direction;
import battlecode.common.MapLocation;
import battlecode.common.RobotController;

public class Soldier extends DefaultRobot {
	public Soldier(RobotController rc) {
		super(rc);
	}

	public void run() 
	{
		while (true)
		{
			
			MapLocation location = rc.getLocation();
			try
			{
				Direction toHome=location.directionTo(HQloc);
				Direction toEnem=location.directionTo(enemyHQLoc);
				
				if(location.distanceSquaredTo(enemyHQLoc)<4)
				{
					rc.move(directions[rand.nextInt(8)]);	
				}
//				rand.nextInt(8).directions[];
				
//				VERY basic pathfinding towards enemy base
//				!=null		means there is a mine
//				==null		means there is no mine
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
			catch (Exception e)
			{
				e.printStackTrace();
			}
			rc.yield();
		}
	}
}
