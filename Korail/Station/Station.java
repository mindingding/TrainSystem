package Station;

public class Station {
	String stationName;
	int stationNum;
	
	public Station(String stationName, int stationNum)
	{
		this.stationName = stationName;
		this.stationNum = stationNum;
	}
	public void setStationName(String _station){
		stationName = _station;
	}
	
	public void setStationNum(int _num){
		stationNum = _num;
	}
	
	public boolean CheckValidity()
	{
		return false;
	}

}
