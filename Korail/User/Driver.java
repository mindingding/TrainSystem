package User;

import Accident.Accident;
import Train.*;

public class Driver extends User 
{
	int drivingTrain;
//	AccInfo acc;
	
	public Driver(){}

	@Override
	public void setDrivingTrain(int key){
		drivingTrain = key;
	}
	
	@Override
	public int getDrivingTrain(){
		return drivingTrain;
	}

	
}
