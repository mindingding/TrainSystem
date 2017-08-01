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
		
		String msg = TL.retTrainlist("���뱸", "����");
		System.out.println(msg);
		AccInfo test = new AccInfo();
		test.setAccident("ȫ��", 216, "10");
		analysis_Accident(test);
		*/
		String svIP = java.net.InetAddress.getLocalHost().getHostAddress();
		System.out.printf("IP: %s\n", svIP);
		try {
			port = Integer.parseInt(args[0]); // Get port from command line
		} catch (Throwable t) {
			System.out.print("����� ��Ʈ��ȣ�� �Է��ϼ���: ");
			
			port = input.nextInt();
			// port = DEFAULT_PORT; // Set port to 5555
		}
	
		EchoServer sv = new AdministerUI(port);
		while(true)//���� �ݰ� �����ϴ� �޴���
		{
			System.out.println("�ɼ��� �����ϼ���\n"
					+ "1.��������\n"
					+ "2.����� �߰�\n"
					+ "3.����翡 ���� �Ҵ�\n"
					+ "4.����");
			
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
		while (true)// ���� ���ư����������� ���� ��ɵ�
		{
			System.out.println("���� ���\n" + "1.Delay �Ҵ�\n" + "2.���� ����\n" + "3.���� ����");

			int s = input.nextInt();

			switch (s) {
			case 1:
				System.out.println("���� ��ȣ:");
				int key = input.nextInt();
				input.nextLine();
				System.out.println("�������� �ð�(��):");
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


	private static void assignTrain() //����翡�� ������ �Ҵ������
	{

		Scanner input = new Scanner(System.in);
		System.out.println("������� �̸��� �Է��ϼ���:");
		String name = input.nextLine();
		System.out.println("�߰��� ������ ��ȣ�� �Է��ϼ���:");
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


	public static void AddDriver()//����� �Է¹޾Ƽ� �߰��ϴ°�
	{
		Scanner input = new Scanner(System.in);
		System.out.println("������� �̸��� �Է��ϼ���:");
		String name = input.nextLine();

		System.out.println("������� ID�� �Է��ϼ���:");
		String ID = input.nextLine();

		System.out.println("������� Password�� �Է��ϼ���:");
		String Password = input.nextLine();
		
		System.out.println("�߰��� ������ ��ȣ�� �Է��ϼ���: (�Է����� �������� 0 �Է�)");
		
		int key = input.nextInt();//�߰��� ������ȣ
		boolean check=usermanager.Existuser(ID);//���̵� �ߺ����� Ȯ��
		if(check==false)//���̵� �ߺ���������
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
		else if(check==true)//���̵� �ߺ���
		{
			System.out.println("�̹� ������ ID�� �����մϴ�. �ٽ� �������ֽʽÿ�."); 
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
		if(com.contains("1")&&client.state==1)//login ����
		{
			returnString = "ID:";
			client.state = 2;
		}
		else if(com.contains("2")&&client.state==1)//ȸ������ ����
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
		else if(client.state==7)// id�Է¹ޱ�(ȸ������)
		{
			client.ID = com;
			returnString = "Password:";
			client.state=8;	
		}
		else if(client.state==8)//password���� �ް� ȸ������ ����
		{
			boolean check;
			client.Password = com;
			check=usermanager.Existuser(client.ID);//�����ϴ� ���̵����� Ȯ��
			if(check==false)//ID�� �̿� �����Ҷ�
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
				returnString = "ȸ�������� �Ϸ� �Ǿ����ϴ�.";
				client.state = 1;
			}
			else if(check==true)//���̵� �ߺ�������.
			{
				returnString ="�̹� ������ ID�� �����մϴ�. �ٽ� �������ֽʽÿ�.";
				client.state =1;
						
			}
		}
		else if(client.state==2)//Id �Է¹ޱ�(�α���)
		{
			client.ID = com;
			returnString = "Passward:";
			client.state = 3;
		}
		else if(client.state==3)//�н����� �Է¹ޱ�.
		{
			int check;
			client.Password = com;
			client.user = usermanager.AllowLogin(client.ID,client.Password);//�α��� �ϴ� �Լ�. ��������� return
			if(client.user==null)//��ġ�Ǵ� ID,Password�� ������
			{
				returnString="�߸��� ID Ȥ�� Password �Դϴ�.";
				client.state =1;
			}
			else if(client.user.getPermission() == 1)//passenger�� �α���. (permission = 1)
			{
				returnString="�ȳ��ϼ���.\n"
						+ "1.������ȸ\n"
						+ "2.�α׾ƿ�\n";
				client.state=4;//�°�
			}
			else if(client.user.getPermission() == 2)//driver�� �α���.  (permission = 2)
			{
				returnString="����� �޴�.\n"
						+ "1.���� ��ġ ����\n"
						+ "2.��� ���� �˸�\n"
						+ "3.�α׾ƿ�\n";
				client.key = usermanager.FindUser(client.ID).getDrivingTrain();//������϶�, key�� �޾ƿ�
				client.state = 5;//�����
			}
		}
		else if(client.state ==4)//�°�
		{
			if(com.contains("1"))//������ȸ
			{
				returnString = "���� ��ȸ\n"
						+ "��߿��� �Է��ϼ���:";
				client.state = 9;
			}
			else if(com.contains("2"))//����
			{
				client.state=1;
				returnString = "";
			}
			else
				returnString ="�߸� �Է��ϼ̽��ϴ�.\n"
						+ "1.������ȸ\n"
						+ "2.����\n";
		}
		else if(client.state==5)//�����
		{
			if(com.contains("1"))//���� ��ġ���� (�Լ� ��������!! ���⸮ ��Ź��)
			{
				returnString = TL.ExcuteMakereport(client.key);
				System.out.println(returnString);
				client.state=5;
			}
			else if(com.contains("2"))//��� ����.
			{
				if( TL.Search(client.key).getState() == Train.State.NORMAL ){
					returnString = "��� ����:";
					client.state=11;
				}
				else{
					returnString = "���� ������ �������� ���°� �ƴմϴ�.\n"
							+ "����� �޴�.\n"
							+ "1.���� ��ġ ����\n"
							+ "2.��� ���� �˸�\n"
							+ "3.�α׾ƿ�\n";;
					client.state = 5;
				}
					
			}
			else if(com.contains("3"))//�α׾ƿ�
			{
				returnString = "";
				client.state=1;
			}
			else//�߸� �Է�������.
				returnString ="�߸� �Է��ϼ̽��ϴ�.\n"
						+ "����� �޴�.\n"
						+ "1.���� ��ġ ����\n"
						+ "2.��� ���� �˸�\n"
						+ "3.�α׾ƿ�\n";
		}
		else if(client.state==9)
		{
			client.StartStation=com;
			returnString = "�������� �Է��ϼ���:";
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
					+ "1.������ȸ\n"
					+ "2.����\n";
			
		}
		else if(client.state==11)//������� ������� �Է¹���
		{
			client.Acctmsg=com;
			returnString = "���� ���� �ð�:";
			client.state=12;
		}
		else if(client.state==12)//������� ���󺹱��ð����� �Է� ����
	      {
			client.ExTime = com;
			client.accident = new AccInfo();
			client.accident.setAccident(client.Acctmsg, client.key, com);
			System.out.println("asdf");
			analysis_Accident(client.accident);
			System.out.println("qwer");
			System.out.println(TL.ExcuteMakereport(client.key));
			returnString = "���� ���� �ٶ�!";
			client.state = 5;
		}
	      
		send_Command(returnString,client);
	}
	
	public static void analysis_Accident(AccInfo acc){
		// �����༱ ����
		int trainIndex, prev = 0, delay=0, crashTA, crashTP;
		double distanceFA, distanceFP; // �ð��������ΰ���. ������ ����簡 � �ൿ�� ������ �� ���� �⺻������ �����ϰԲ� ��������.
		// distance from accident, distance from previous
		TL.renewalAll();
		TL.Search(acc.getTrainKey()).setDelay( acc.getExtime() ); // ��� ������ ������ �ο�
		
		// ��� ������ acc.delay�� ����.
		// tinfo.positionP = (double)current + currentTime - cur; //���翪(int) ���������κ��� �����ð�(double)
		// tinfo.positionN = (double)current + next - currentTime; //���翪(int) ���������� �����ð�(double)
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
		if(s=="trainAdd"){ //������� �� ����?? //��������� ???
			
			Train t = new Train("",0,0);
			
			String type;
			int key;
			
			System.out.println("type�� key�� �Է��ϼ���");
			type = scan.nextLine(); //�����ʿ� String or file
			key = scan.nextInt();
			if(TL.Search(key)==null){
				t.setType(type);
				t.setKey(key);
				
				TL.AddTrain(t);
				System.out.println("���� ����� �Ϸ�Ǿ����ϴ�.");
			}
			else
				System.out.println("�ش������ �̹� �����մϴ�.");
		}
		if(s=="trainDelete"){ //�������� 
			
			int key;
			Train t = null;
			
			System.out.println("������ ������ key�� �Է��ϼ���");
			key = scan.nextInt(); //�����ʿ�, ���Ϸ� �Է¹������� �־
			t = TL.Search(key);
			if(t==null)
				System.out.println("�ش� ������ �������� �ʽ��ϴ�.");
			else{
				TL.DeleteTrain(t);
				System.out.println("���� ������ �Ϸ�Ǿ����ϴ�.");
			}
		}

		if(s=="stationAdd"){ //�����
			String data;
			Station st =null;
			String name,num;
			
			System.out.println("���̸��� ����ȣ�� �Է��ϼ���");
			data = scan.nextLine();
			
			String[] msg = data.split(" "); //�����ʿ�?
			name = msg[0];
			num = msg[1];
	
			if(SM.CheckValidity(num)==null){
				st.setStationName(name);
				st.setStaionNumber(num);
				SM.AddStation(st);
				System.out.println("�� ����� �Ϸ�Ǿ����ϴ�.");
				
			}
			else{
				System.out.println("�ش� ���� �̹� �����մϴ�.");
			}
			
		}
		if(s=="stationDelete"){ //�� ����
			String num;
			Station st;
			
			System.out.println("������ ����ȣ�� �Է��ϼ��� ");
			num = scan.nextLine();
			st = SM.CheckValidity(num);
			if(st==null){
				System.out.println("�ش� ���� �������� �ʴ� ���Դϴ�. ");
			}
			else{
				SM.DeleteStation(st);
				System.out.println("�� ������ �Ϸ�Ǿ����ϴ�.");
			}
		}
		if(s=="railAdd"){ //���� ���
			System.out.println("����� ������ ��ȣ�� ���̸� �Է��ϼ���");
		}
		if(s=="railDelete"){ //���� ���� 
			System.out.println("������ ������ ��ȣ�� �Է��ϼ���");
		}
	}
	*/
}
