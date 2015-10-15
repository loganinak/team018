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
				
//				VERY basic pathfinding towards enemy base
//				!=null		means there is a mine
				if(rc.senseMine(location.add(toEnem))!=null)
				{
					if(rc.senseMine(location.add(toEnem.rotateLeft()))!=null)
					{
						if(rc.senseMine(location.add(toEnem.rotateRight()))!=null)
						{
							rc.defuseMine(location.add(toEnem));
							rc.move(toEnem);
						}
						else
						{
							rc.move(toEnem.rotateRight());
						}
					}
					else
					{
						rc.move(toEnem.rotateLeft());
					}
				}
				
				if(rc.canMove(toEnem))
				{
					rc.move(toEnem);
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
