package Accident;

import Train.*;
import UI.CusTime;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AccInfo {
	
	Accident acc = null;
	int train = 0;
	double time = 0;
	int Extime;
	double DExtime;
	
	public AccInfo(){
		acc = new Accident();
	}

	
	public void setAccident(String accType, int trainkey,String extime)
	{
		acc.setAccType( accType);
		this.train = trainkey;
		time = CusTime.getD_currentTime();
		Extime = Integer.parseInt(extime);
		DExtime = (int) ((double)Extime / 24.0 / 60.0);
	}
	
	public void setTrainInfo(int _train)
	{
		train = _train;
	}
	
	public int getTrainKey(){
		return train;
	}
	
	public int getExtime(){
		return Extime;
	}
}
