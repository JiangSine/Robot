package sample;


import java.util.HashMap;
import java.util.Map.Entry;
import java.util.function.DoubleToLongFunction;

import robocode.AdvancedRobot;
import robocode.Rules;
import robocode.ScannedRobotEvent;

public class MySecondRobot extends AdvancedRobot  {

	private double SAFE_DISTANCE = 30;
	private final Integer U = 0, L = 1, D = 2, R = 3;
	public MySecondRobot() {
		// TODO Auto-generated constructor stub
	}

	public void run() {				
		setAdjustGunForRobotTurn(true);
		Integer drt = goBoundary();
        while (true) {
      	drt = proceed(drt);       	
   		//execute();
        }
        
    }
	
	public Integer proceed(Integer drt) {
		Integer nextDrt = getNextDirection(drt);
		turnLeft(90);
		double distance  = 0;
		switch (nextDrt) {
		case 0:
			distance = getBattleFieldHeight()-getY()-SAFE_DISTANCE;
			break;
		case 1:
			distance = getX() - SAFE_DISTANCE;
			break;
		case 2:
			distance = getY() - SAFE_DISTANCE;
			break;
		case 3:
			distance = getBattleFieldWidth() - getX() - SAFE_DISTANCE;
			break;
		default:
			break;
		}
		ahead(distance);
		return nextDrt;
	}
	
	private Integer getNextDirection(Integer drt) {
		return (drt+1)%4;		
	}
	
	public Integer goBoundary() {		
		double x = this.getX() - SAFE_DISTANCE;
		double distanceX = this.getBattleFieldWidth() - x - SAFE_DISTANCE;
		double y = this.getY() - SAFE_DISTANCE;
		double distanceY = this.getBattleFieldHeight() - y - SAFE_DISTANCE;
		double heading = this.getHeading();
		HashMap<Integer,Double> direction = new HashMap<Integer,Double>();
		direction.put(U,distanceY);
		direction.put(D,y);
		direction.put(L,x);
		direction.put(R,distanceX);
		double min = Double.MAX_VALUE;
		Integer drt = null;
		for (Entry<Integer, Double> entry : direction.entrySet()) {
			if (drt == null || min > entry.getValue()) {
				min = entry.getValue();
				drt = entry.getKey();
			}			
		}
		switch (drt) {
		case 1:
			if (heading >= 90 && heading <= 270){
				turnRight(270-heading);
			} else {
				turnLeft((90 +heading)%360);
			}
			ahead(x);
			break;
		case 3:
			if (heading >= 90 && heading <= 270) {
				turnLeft(heading-90);
			} else {
				turnRight((450-heading)%360);
			}
			ahead(distanceX);
			break;
		case 0:
			if (heading >= 0 && heading <= 180) {
				turnLeft(heading);
			} else {
				turnRight(360-heading);
			}
			ahead(distanceY);
			break;
		case 2:
			if (heading >= 0 && heading <= 180) {
				turnRight(180-heading);
			} else {
				turnLeft(heading-180);
			}
			ahead(y);
			break;
		default:
			break;
		}
		return drt;		
	}
	
	public void round() {
		
	}
		  	
    public void onScannedRobot(ScannedRobotEvent e) {
    	double dy = e.getDistance() * Math.cos(e.getBearing()+this.getHeading());
    	double dx = e.getDistance() * Math.sin(e.getBearing()+this.getHeading());    	
    	double t  = e.getDistance()/Rules.getBulletSpeed(getPower(e));
    	double rDistance = e.getVelocity() * t;
    	/*double sina = (e.getVelocity() * t * e.getHeading() + dx - this.getX()*t)/this.getVelocity()/t;
    	Math.toDegrees(Math.asin(sina));*/

    	
        fire(1);
    }
    
    private double getPower(ScannedRobotEvent e) {
		return Math.min(Math.min(this.getEnergy() / 6, 400 / e.getDistance()), e.getEnergy()/ 3);
	}
}
