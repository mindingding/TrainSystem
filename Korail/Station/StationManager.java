package Station;

import java.util.ArrayList;

public class StationManager {
	ArrayList<Station> stationList = new ArrayList<Station>();
	
	public StationManager(){}
	public void AddStation(Station s)
	{
		stationList.add(s);
	}
	public void DeleteStation(Station s)
	{
		stationList.remove(s);
	}
	public int getStationNumber(String _StationName){ //역 이름을 고유번호로
		int num = -1;
		for(int i = 0; i< stationList.size(); i++){
			// System.out.println(stationList.get(i).stationName);
			if( stationList.get(i).stationName.compareTo(_StationName) == 0){
				num = i;
				break;
			}
		}
		
		return num;
	}
	public String getStationNameAt(int index){
		return stationList.get(index).stationName;
	}
}
