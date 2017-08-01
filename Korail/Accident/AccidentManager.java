package Accident;

import java.util.ArrayList;

public class AccidentManager {
	ArrayList<AccInfo>accInfoList;
	
	AccidentManager(){
		accInfoList = new ArrayList<AccInfo>();
	}
	
	public void add_accInfo(AccInfo accinfo){
		accInfoList.add( accinfo);
	}
	
	public void delete_accInfo(AccInfo accinfo){
		accInfoList.remove(accinfo);
	}
}
