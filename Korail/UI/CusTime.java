package UI;
import java.util.Date;

public class CusTime{
	private static int month, day, hour, min;
	
	public CusTime(){
		set_Date();
	}
	
	@SuppressWarnings("deprecation")
	public void set_Date(){
		Date currentTime  = new Date();
		month = currentTime.getMonth() + 1;
        day = currentTime.getDate();
        hour = currentTime.getHours();
        min = currentTime.getMinutes();
	}
	public String get_Date(){
		String msg;
		msg = String.format("%d/%d-%d:%d", month, day, hour, min);
		return msg;
	}
	public static Double getD_currentTime(){
		Date currentTime = new Date();
		double result;
		result = currentTime.getHours() / 24.0 + currentTime.getMinutes() / 60.0 / 24.0;
		return result;
	}
	public static int getI_Time(double _time){
		int result;
		result = (int)(_time * 24 * 60);
		return result;
	}
	public static String getS_Time(double _time){
		int minute, second;
		minute = (int)(_time * 24.0);
		second = (int)((_time * 24.0 - (int)(_time * 24.0)) * 60.0);
		String msg = String.format("%2d:%2d", minute, second );
		return msg;
	}
	@SuppressWarnings("deprecation")
	public  void uptodate(){
		set_Date();
	}
}