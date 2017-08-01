package UI;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.UnknownHostException;
import java.util.Scanner;

import Accident.AccInfo;
import Accident.Accident;
import Station.Station;
import Station.StationManager;
import Train.Train;
import Train.TrainList;
import User.*;

public class AdministerUI extends EchoServer{
	static public UserManager usermanager;
	static public TrainList TL;
	static public StationManager[] SM = new StationManager[2];
	
	public AdministerUI(int port) {
	      super(port);
	      
	      try {
	         FileInputStream fileStream = new FileInputStream("UserList.ser");
	         ObjectInputStream os= new ObjectInputStream(fileStream);
	         usermanager = (UserManager)(os.readObject());
	         os.close();
	      } catch (IOException | ClassNotFoundException e) {
	         // TODO Auto-generated catch block
	         e.printStackTrace();
	      }
	      // TODO Auto-generated constructor stub
	   }
	
	public static void main(String args[]) throws UnknownHostException{
		usermanager = new UserManager();
		TL = new TrainList();
		SM[0] = new StationManager(); SM[1] = new StationManager();
		
		ReadFile R = new ReadFile();
		
		Scanner input = new Scanner(System.in);
		int port = 0; // Port to listen on
		int sel;// Port to listen on

		/*MakeReport Testing
		String msg = TL.Search(115).MakeReport();
		System.out.println(msg);
		
		String msg = TL.retTrainlist("동대구", "서울");
		System.out.println(msg);
		AccInfo test = new AccInfo();
		test.setAccident("홍수", 216, "10");
		analysis_Accident(test);
		*/
		String svIP = java.net.InetAddress.getLocalHost().getHostAddress();
		System.out.printf("IP: %s\n", svIP);
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			System.out.print("사용할 포트번호를 입력하세요: ");
			
			port = input.nextInt();
			// port = DEFAULT_PORT; // Set port to 5555
		}
	
		EchoServer sv = new AdministerUI(port);
		while(true)//서버 닫고 수행하는 메뉴들
		{
			System.out.println("옵션을 선택하세요\n"
					+ "1.서버열기\n"
					+ "2.기관사 추가\n"
					+ "3.기관사에 열차 할당\n"
					+ "4.종료");
			
			sel = input.nextInt();
			if(sel==1)
				break;
				
			switch(sel)
			{
			case 2:
				AddDriver();
				break;
			case 3:
				assignTrain();
				break;
			case 4:
				System.exit(0);
				break;
			}
		}
		try {
			sv.listen(); // Start listening for connections
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
		while (true)// 서버 돌아가고있을때의 서버 명령들
		{
			System.out.println("서버 명령\n" + "1.Delay 할당\n" + "2.지연 전달\n" + "3.서버 종료");

			int s = input.nextInt();

			switch (s) {
			case 1:
				System.out.println("기차 번호:");
				int key = input.nextInt();
				input.nextLine();
				System.out.println("딜레이할 시간(분):");
				String delay = input.nextLine();
				sv.sendDelayToDriver(key, delay);
				SetTimeOfTrain(key, delay);
				break;
			case 2:
				for (int i = 0;; i++) {
					int key1 = TL.RetKeyFormIndex(i);
					if (key1 == -1)
						break;
					sv.sendDelayToDriver(key1, Integer.toString(TL.Search(key1).getDelay()));
				}
				break;
			case 3:
				System.exit(0);
				break;

			}
		}
	}

	private static void SetTimeOfTrain(int key, String delay) {
		TL.SetDelayTime(key, delay);
		
	}


	private static void assignTrain() //기관사에게 기차를 할당시켜줌
	{

		Scanner input = new Scanner(System.in);
		System.out.println("기관사의 이름을 입력하세요:");
		String name = input.nextLine();
		System.out.println("추가할 기차의 번호를 입력하세요:");
		int key = input.nextInt();
		
		usermanager.FindUserWithName(name).setDrivingTrain(key);
		
		try 
        { 
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("UserList.ser")); 
            output.writeObject(usermanager); 
            output.close(); 
        } 
        catch(IOException ioException) 
        { 
            System.out.println("Error opening file."); 
        } 		
	}


	public static void AddDriver()//기관사 입력받아서 추가하는곳
	{
		Scanner input = new Scanner(System.in);
		System.out.println("기관사의 이름을 입력하세요:");
		String name = input.nextLine();

		System.out.println("기관사의 ID를 입력하세요:");
		String ID = input.nextLine();

		System.out.println("기관사의 Password를 입력하세요:");
		String Password = input.nextLine();
		
		System.out.println("추가할 기차의 번호를 입력하세요: (입력하지 않으려면 0 입력)");
		
		int key = input.nextInt();//추가할 기차번호
		boolean check=usermanager.Existuser(ID);//아이디가 중복인지 확인
		if(check==false)//아이디 중복되지않음
		{
			usermanager.AddDriver(name, ID, Password, key);
		/*	if(key!=0)
				usermanager.FindUserWithName(name).setDrivingTrain(key);*/
			
			try 
	        { 
	            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("./UserList.ser")); 
	            output.writeObject(usermanager); 
	            output.close(); 
	        } 
	        catch(IOException ioException) 
	        { 
	            System.out.println("Error opening file."); 
	        } 
		}
		else if(check==true)//아이디 중복됨
		{
			System.out.println("이미 동일한 ID가 존재합니다. 다시 진행해주십시오."); 
		}
			
	}
	
	@Override
	public void receive_Command(String command,ConnectionToClient client){
		
		// Is_valid(command);
		comHandle(command,client);
	}
	public void comHandle(String com,ConnectionToClient client)
	{
		String returnString = null;
		/*
		*/
		if(com.contains("1")&&client.state==1)//login 선택
		{
			returnString = "ID:";
			client.state = 2;
		}
		else if(com.contains("2")&&client.state==1)//회원가입 선택
		{
			client.state=6;
			returnString ="Name:";
		}
		else if(client.state==6)
		{
			client.Name = com;
			returnString = "ID:";
			client.state = 7;
		}
		else if(client.state==7)// id입력받기(회원가입)
		{
			client.ID = com;
			returnString = "Password:";
			client.state=8;	
		}
		else if(client.state==8)//password까지 받고 회원가입 진행
		{
			boolean check;
			client.Password = com;
			check=usermanager.Existuser(client.ID);//존재하는 아이디인지 확인
			if(check==false)//ID가 이용 가능할때
			{
				usermanager.AddCustomer(client.Name,client.ID,client.Password);
				try 
		        { 
					ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("./UserList.ser")); 
		            output.writeObject(usermanager); 
		            output.close(); 
		        } 
		        catch(IOException ioException) 
		        { 
		            System.out.println("Error opening file."); 
		        } 
				returnString = "회원가입이 완료 되었습니다.";
				client.state = 1;
			}
			else if(check==true)//아이디가 중복됐을때.
			{
				returnString ="이미 동일한 ID가 존재합니다. 다시 진행해주십시오.";
				client.state =1;
						
			}
		}
		else if(client.state==2)//Id 입력받기(로그인)
		{
			client.ID = com;
			returnString = "Passward:";
			client.state = 3;
		}
		else if(client.state==3)//패스워드 입력받기.
		{
			int check;
			client.Password = com;
			client.user = usermanager.AllowLogin(client.ID,client.Password);//로그인 하는 함수. 결과에따라 return
			if(client.user==null)//매치되는 ID,Password가 없을때
			{
				returnString="잘못된 ID 혹은 Password 입니다.";
				client.state =1;
			}
			else if(client.user.getPermission() == 1)//passenger로 로그인. (permission = 1)
			{
				returnString="안녕하세요.\n"
						+ "1.열차조회\n"
						+ "2.로그아웃\n";
				client.state=4;//승객
			}
			else if(client.user.getPermission() == 2)//driver로 로그인.  (permission = 2)
			{
				returnString="기관사 메뉴.\n"
						+ "1.기차 위치 보고\n"
						+ "2.사고 정보 알림\n"
						+ "3.로그아웃\n";
				client.key = usermanager.FindUser(client.ID).getDrivingTrain();//기관사일때, key값 받아옴
				client.state = 5;//기관사
			}
		}
		else if(client.state ==4)//승객
		{
			if(com.contains("1"))//열차조회
			{
				returnString = "열차 조회\n"
						+ "출발역을 입력하세요:";
				client.state = 9;
			}
			else if(com.contains("2"))//종료
			{
				client.state=1;
				returnString = "";
			}
			else
				returnString ="잘못 입력하셨습니다.\n"
						+ "1.열차조회\n"
						+ "2.종료\n";
		}
		else if(client.state==5)//기관사
		{
			if(com.contains("1"))//기차 위치보고 (함수 만들어야함!! 동기리 부탁해)
			{
				returnString = TL.ExcuteMakereport(client.key);
				System.out.println(returnString);
				client.state=5;
			}
			else if(com.contains("2"))//사고 났음.
			{
				if( TL.Search(client.key).getState() == Train.State.NORMAL ){
					returnString = "사고 정보:";
					client.state=11;
				}
				else{
					returnString = "현재 기차가 주행중인 상태가 아닙니다.\n"
							+ "기관사 메뉴.\n"
							+ "1.기차 위치 보고\n"
							+ "2.사고 정보 알림\n"
							+ "3.로그아웃\n";;
					client.state = 5;
				}
					
			}
			else if(com.contains("3"))//로그아웃
			{
				returnString = "";
				client.state=1;
			}
			else//잘못 입력했을때.
				returnString ="잘못 입력하셨습니다.\n"
						+ "기관사 메뉴.\n"
						+ "1.기차 위치 보고\n"
						+ "2.사고 정보 알림\n"
						+ "3.로그아웃\n";
		}
		else if(client.state==9)
		{
			client.StartStation=com;
			returnString = "도착역을 입력하세요:";
			client.state=10;
		}
		else if(client.state==10)
		{
			client.DstStation=com;
			System.out.println(client.StartStation+client.DstStation);
			returnString = TL.retTrainlist(client.StartStation, client.DstStation);
			System.out.println(returnString);
			client.state=4;
			returnString +="\n"
					+ "1.열차조회\n"
					+ "2.종료\n";
			
		}
		else if(client.state==11)//기관사의 사고정보 입력받음
		{
			client.Acctmsg=com;
			returnString = "예상 복구 시간:";
			client.state=12;
		}
		else if(client.state==12)//기관사의 예상복구시간까지 입력 받음
	      {
			client.ExTime = com;
			client.accident = new AccInfo();
			client.accident.setAccident(client.Acctmsg, client.key, com);
			System.out.println("asdf");
			analysis_Accident(client.accident);
			System.out.println("qwer");
			System.out.println(TL.ExcuteMakereport(client.key));
			returnString = "빠른 복구 바람!";
			client.state = 5;
		}
	      
		send_Command(returnString,client);
	}
	
	public static void analysis_Accident(AccInfo acc){
		// 상하행선 구분
		int trainIndex, prev = 0, delay=0, crashTA, crashTP;
		double distanceFA, distanceFP; // 시간개념으로간다. 사고보고는 기관사가 어떤 행동을 수행할 때 마다 기본적으로 수행하게끔 만들어야함.
		// distance from accident, distance from previous
		TL.renewalAll();
		TL.Search(acc.getTrainKey()).setDelay( acc.getExtime() ); // 사고난 기차에 딜레이 부여
		
		// 사고난 기차는 acc.delay와 같고.
		// tinfo.positionP = (double)current + currentTime - cur; //현재역(int) 이전역으로부터 지난시간(double)
		// tinfo.positionN = (double)current + next - currentTime; //현재역(int) 다음역까지 남은시간(double)
		for( int road = 0; road <= 1; road++){
			trainIndex = TL.getTrainIndex( acc.getTrainKey() );
			prev = trainIndex;
			trainIndex++;
			while(trainIndex < TL.getListsize() - 1 && TL.Search( TL.getIndexToKey(trainIndex) ).getDir() == road){
				// distanceFA = TL.getDistance(trainIndex, TL.getTrainIndex( acc.getTrainKey() ));
				System.out.println(trainIndex + " " + prev);
				
				distanceFP = TL.getDistance(trainIndex, prev);
				System.out.println(distanceFP);
				// crashTA = CusTime.getI_Time( distanceFA);
				crashTP = CusTime.getI_Time( distanceFP);
				System.out.println(crashTP);
				delay = TL.Search( TL.getIndexToKey(prev)).getDelay() + 5 - crashTP;
				if( delay < 0){
					delay = 0;
					break;
				}
				
				TL.Search( TL.getIndexToKey(trainIndex)).setDelay(delay);
				prev = trainIndex; trainIndex++;
			}
		}
	}
	
	
	public int ExistUser(String Name,String Id,String Password)
	{
		if(usermanager.FindUser(Id)==null)
		{
			return 1;
		}
		return 2;
	}

	public void send_Command(String com,ConnectionToClient client){
		Object o = com;
		try {
			client.sendToClient(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
	
	/*
	public void addInfo(String s){

		
		Scanner scan = new Scanner(System.in);
		if(s=="trainAdd"){ //기차등록 및 수정?? //지연기록은 ???
			
			Train t = new Train("",0,0);
			
			String type;
			int key;
			
			System.out.println("type과 key를 입력하세요");
			type = scan.nextLine(); //수정필요 String or file
			key = scan.nextInt();
			if(TL.Search(key)==null){
				t.setType(type);
				t.setKey(key);
				
				TL.AddTrain(t);
				System.out.println("기차 등록이 완료되었습니다.");
			}
			else
				System.out.println("해당기차가 이미 존재합니다.");
		}
		if(s=="trainDelete"){ //기차삭제 
			
			int key;
			Train t = null;
			
			System.out.println("삭제할 기차의 key를 입력하세요");
			key = scan.nextInt(); //수정필요, 파일로 입력받을수도 있어서
			t = TL.Search(key);
			if(t==null)
				System.out.println("해당 기차는 존재하지 않습니다.");
			else{
				TL.DeleteTrain(t);
				System.out.println("기차 삭제가 완료되었습니다.");
			}
		}

		if(s=="stationAdd"){ //역등록
			String data;
			Station st =null;
			String name,num;
			
			System.out.println("역이름과 역번호를 입력하세요");
			data = scan.nextLine();
			
			String[] msg = data.split(" "); //수정필요?
			name = msg[0];
			num = msg[1];
	
			if(SM.CheckValidity(num)==null){
				st.setStationName(name);
				st.setStaionNumber(num);
				SM.AddStation(st);
				System.out.println("역 등록이 완료되었습니다.");
				
			}
			else{
				System.out.println("해당 역이 이미 존재합니다.");
			}
			
		}
		if(s=="stationDelete"){ //역 삭제
			String num;
			Station st;
			
			System.out.println("삭제할 역번호를 입력하세요 ");
			num = scan.nextLine();
			st = SM.CheckValidity(num);
			if(st==null){
				System.out.println("해당 역은 존재하지 않는 역입니다. ");
			}
			else{
				SM.DeleteStation(st);
				System.out.println("역 삭제가 완료되었습니다.");
			}
		}
		if(s=="railAdd"){ //선로 등록
			System.out.println("등록할 레일의 번호와 길이를 입력하세요");
		}
		if(s=="railDelete"){ //선로 삭제 
			System.out.println("삭제할 레일의 번호를 입력하세요");
		}
	}
	*/
}
