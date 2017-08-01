package Accident;

public class Accident {
		private String type;
		Accident()
		{}
		Accident(String _type)
		{
			type = _type;
		}
		
		public void setAccType(String _type){
			type = _type;
		}
		
		public String getAccType(){
			return type;
		}

}
