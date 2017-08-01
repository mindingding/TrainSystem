package Train;

import java.util.ArrayList;

import Accident.AccInfo;
import Station.Station;
import UI.AdministerUI;
import UI.CusTime;

public class TrainList {
	ArrayList<Train> trainList = new ArrayList<Train>();
	Train train;
	
	public TrainList(){	}
	public Train Search(int key)
	{
		for(int i=0; i<trainList.size(); i++){
			if(trainList.get(i).getKey()==key)
				return trainList.get(i);
		}
		return null;
	}
	
	public int getIndexToKey(int index){
		return trainList.get(index).getKey();
	}
	public void renewalAll(){
		for(int i = 0; i < trainList.size(); i++){
			trainList.get(i).MakeReport();
		}
	}
	
	public int getListsize(){
		return trainList.size();
	}
	public int RetKeyFormIndex(int i) {
		int key;
		if (i == trainList.size())
			return -1;

		key = trainList.get(i).getKey();

		return key;

	}
	public double getDistance(int index1, int index2){
		double sum = trainList.get(index1).getPosition() - trainList.get(index2).getPosition();
		return sum;
	}
	public String ExcuteMakereport(int key) {
		for (int i = 0; i < trainList.size(); i++){
			if(trainList.get(i).getKey()==key)
				return trainList.get(i).MakeReport();
		}
		return null;
	}
	public void SetDelayTime(int key,String delay)
	{
		for(int i=0; i<trainList.size(); i++){
			if(trainList.get(i).getKey()==key)
				trainList.get(i).setDelay(Integer.parseInt(delay));;
		}
	}
	
	public int getTrainIndex(int key){
		for( int i = 0; i < trainList.size();i++){
			if( trainList.get(i).getKey() == key){
				return i;
			}
		}
		return 0;
	}
	
	public boolean Is_available_Time()
	{
		return false;
	}
	public void AddTrain(Train t)
	{
		trainList.add(t);
	}
	public void DeleteTrain(Train t)
	{
		trainList.remove(t);
	}
	public void GetTrainInfo(Train t)
	{
		System.out.printf("type:%d, key:%d ...",t.getType(),t.getKey());
	}
	public String retTrainlist(String stS,String fnS){
		String msg="";
		int st=0, fn=0, dir;
		st = AdministerUI.SM[0].getStationNumber( stS) ; //하행선을 기준으로 고유번호를 얻어온 뒤
		fn = AdministerUI.SM[0].getStationNumber( fnS) ;
		if( st == -1) return "해당하는 출발역이 존재하지 않습니다.\n";
		else if( fn == -1) return "해당하는 도착역이 존재하지 않습니다.\n";
		
		if( st < fn) dir = 0;
		else {
			st = AdministerUI.SM[1].getStationNumber( stS) ; //출발역과 도착역의 고유번호를 얻어오고
			fn = AdministerUI.SM[1].getStationNumber( fnS) ;
			dir = 1;
		}
		System.out.println(st + " " + fn + " " + dir);
		double currentTime = CusTime.getD_currentTime(), stTime, fnTime;
		//System.out.println(currentTime);
		for(int i = 0; i < trainList.size(); i++){
			// System.out.println(trainList.get(i).getTimeAtStation(st));
			if( trainList.get(i).getDir() == dir && trainList.get(i).getTimeAtStation(st) >= currentTime
					&& trainList.get(i).getTimeAtStation(fn) != 0){
				stTime = trainList.get(i).getTimeAtStation(st);
				fnTime = trainList.get(i).getTimeAtStation(fn);
				msg = String.format("%s(%s/%d)-%s / %s(지연+%s분)\n", msg, trainList.get(i).getType(),trainList.get(i).getKey(),
						CusTime.getS_Time(stTime), CusTime.getS_Time(fnTime), trainList.get(i).getDelay());
			}
		}

		return msg;
	}
}
