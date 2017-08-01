package Train;

import UI.AdministerUI;
import UI.CusTime;
import java.util.ArrayList;

class TrainInfo {
	int delay;
	double position;
	double renewalTime;
	TrainInfo(){}
}
public class Train {
	public enum State{START, NORMAL, ABNORMAL, REPAIR, STOP, FINAL};
	
	ArrayList<Double> timeAtStation = new ArrayList<Double>();
	private String type;
	private int key;
	private int dir; // dir = 0 하행선/ 1 상행선
	TrainInfo tinfo = new TrainInfo();
	State state;
	
	public Train(){}
	public Train(String type, int key, int dir)
	{
		this.type = type;
		this.key = key;
		this.dir = dir;
		tinfo.delay = 0;
		tinfo.position = 0;
		tinfo.renewalTime = CusTime.getD_currentTime();
		state = State.START;
	}
	public State getState(){
		return state;
	}
	public String MakeReport(){
		double currentTime, cur=0.0, next=0.0;
		String currentStation = "", nextStation="",msg = "";
		int i, current = 0;
		
		currentTime = CusTime.getD_currentTime();
		for(i = 0; i < 18;i++){
			cur = timeAtStation.get(i); current = i;
			
			if( cur == 0.0) continue;
			
			i++;
			while(i < 19){
				if( timeAtStation.get(i) != 0.0)
					break;
				i++;
			}
			
			if( i == 19) {state = State.FINAL; break;}
			
			next = timeAtStation.get(i);
			if( cur > next && next != 0.0) {
				currentTime += 1;
				next += 1; // 자정이 넘어가는 경우
			}
			// System.out.println(cur + " " + currentTime + " " + next);
			if( (cur != 0.0 && next != 0.0 ) && cur <= currentTime && next >= currentTime){ // 시간순으로 정렬되어 있기 때문에.
				currentStation = AdministerUI.SM[dir].getStationNameAt(current);
				nextStation = AdministerUI.SM[dir].getStationNameAt(i);
				state = State.NORMAL;
				break;
			}
			i--;
		}
		
		if( currentTime > 1) currentTime -= 1;
		
		
		//position은 대전역으로 부터 남은시간이 얼마인지(음수이면 그만큼 남은거고 양수이면 대전을 지난지 얼마나 되었다./ 대전이 모든 기차가 경유하는 유일한 역이기 때문에)
		tinfo.position = (dir == 0 ? timeAtStation.get(10) - currentTime : timeAtStation.get(8) - currentTime);
		tinfo.renewalTime = currentTime;
		
		// System.out.println(currentStation + " " + nextStation); 
		if( state == State.FINAL || state == State.START) msg = "현재 기차가 주행상태가 아닙니다.\n";
		else if( state == State.NORMAL) msg = String.format("Report: %s(으)로부터 출발 %s경과 / 다음 정차역: %s\n", currentStation, CusTime.getS_Time(currentTime-cur), nextStation);
		return msg;
	}
	
	public void addTime(double time ){
		timeAtStation.add( time);
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getKey() {
		return key;
	}
	public void setKey(int key) {
		this.key = key;
	}
	public void setDir(int dir){
		this.dir = dir;
	}
	public int getDir(){
		return dir;
	}
	
	public int getDelay() {
		return tinfo.delay;
	}
	public void setDelay(int delay) {
		this.tinfo.delay = delay;
	}
	public double getPosition() {
		return tinfo.position;
	}
	public double getRenewalTime() {
		return tinfo.renewalTime;
	}
	public void setRenewalTime(double renewalTime) {
		this.tinfo.renewalTime = renewalTime;
	}
	public double getTimeAtStation(int StationNum){
		double result;
		result = timeAtStation.get(StationNum);
		return result;
	}

}
