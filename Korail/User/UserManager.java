package User;

import java.io.Serializable;
import java.util.ArrayList;

public class UserManager implements Serializable{
	ArrayList<User> user;
	
	public UserManager(){
		user = new ArrayList<User>();
	}
	
	// name, id, password를 모두 얻어와야
	public User AllowLogin(String  id, String password)
	{
		User customer = FindUser(id);
		System.out.println("in allowlogin");
		System.out.println(customer.getID());
		System.out.println(customer.getPassword());
		if( customer.getID().equals(id)
				&& customer.getPassword().equals(password))
		{
			System.out.println(customer.getName()+"님이 로그인 하였습니다.");
			return customer;
		}
		return null;
	}
	
	public User FindUser(String id)
	{
		for( int i=0; i < user.size(); i++){
			if( user.get(i).getID().equals(id)){
				return user.get(i);
			}
		}
		
		return null;
	}
	public User FindUserWithName(String name)
	{
		for( int i=0; i < user.size(); i++){
			if( user.get(i).getName().equals(name)){
				return user.get(i);
			}
		}
		
		return null;
	}
	public boolean Existuser(String id){
		for( int i = 0; i < user.size();i++){
			if( user.get(i).getID().equals(id) )
				return true;
		}
		
		return false;
	}
	
	public void AddCustomer(String name,String id,String password){
		User newUser = new Passenger();
		newUser.setPermission(1);
		newUser.setID(id);
		newUser.setName(name); // receive_command
		newUser.setPassword(password); // receive_command
		
		AddUser(newUser);
	}
	public void AddDriver(String name,String id,String password, int key)
	{
		User newUser = new Driver();
		newUser.setPermission(2);
		newUser.setID(id);
		newUser.setName(name); // receive_command
		newUser.setPassword(password); // receive_command
		newUser.setDrivingTrain(key);
		AddUser(newUser);
	}
	public void AddUser(User u)
	{
		user.add(u);
	}
	public void DeleteUer(User u)
	{
		user.remove(u);
	}


}
