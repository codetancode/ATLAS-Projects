package com.amazon.railwaycrossingapp.model;
import java.util.LinkedHashMap;


public class RailwayCrossing {
	int id;
	String name;
	String address;
	int status;				// 0 for open and 1 for close
	User personInCharge;
	Schedule schedule;
	
	public RailwayCrossing() {
		name = "";
		address = "";
		status = 0; //if 0 open, 1 close
		schedule = new Schedule();

	}

	public RailwayCrossing(int id, String name, String address, int status, User personInCharge, int from, int to) {
		this.id = id;
		this.name = name;
		this.address = address;
		this.status = status;
		this.personInCharge = personInCharge;
		this.schedule = new Schedule(from, to);
	}
	public int getID(){return this.id;}
	public int setID(int id){return this.id = id;}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public User getPersonInCharge() {
		return personInCharge;
	}

	public void setPersonInCharge(User personInCharge) {
		this.personInCharge = personInCharge;
	}

	public Schedule getSchedules() {
		return this.schedule;
	}

	public void setSchedules(Schedule s) {
		this.schedule = s;
	}

	@Override
	public String toString() {
		String crossingStatus = (status == 0) ? "OPEN" : "CLOSE";
		String railwayCrossingText = "\n~~~~Crossing ID:"+id+" :"+name+"~~~~"
									+"Crossing Name: "+name+"||"
									+"Crossing Address: "+address+"||"
									+"Crossing Status: "+crossingStatus+"||"
									+"Crossing Schedule: "+schedule.toString()+"||"
									+"Crossing Person InCharge: "+personInCharge.getName()+"||"
									+"Crossing Person InCharge: "+personInCharge.getEmail()+"||"
				+"~~~~\n";
		
		return railwayCrossingText;
	}

}
