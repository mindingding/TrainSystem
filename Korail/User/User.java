package User;

import java.io.Serializable;

import Train.Train;

public abstract class User implements Serializable {
	
	private String name;
	private String ID;
	private String password;
	private int permission;

	User(){
		name = ""; ID = ""; password = ""; permission = -1;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getPermission() {
		return permission;
	}

	public void setPermission(int permission) {
		this.permission = permission;
	}

	public abstract void setDrivingTrain(int _drivingTrain);
	
	public abstract int getDrivingTrain();
}
