package com.amazon.railwaycrossingapp.controller;

import java.util.List;
import java.util.Map;

import com.amazon.railwaycrossingapp.model.RailwayCrossing;
import com.amazon.railwaycrossingapp.model.User;
import com.amazon.railwaycrossingapp.services.RailwayCrossingService;

public class RailwayCrossingController {
	
	private static RailwayCrossingController controller;
	
	private RailwayCrossingController() {}
	
	public static RailwayCrossingController getInstance() {
		if(controller == null) {
			controller = new RailwayCrossingController();
		}
		
		return controller;
	}

	private RailwayCrossingService railWayService = new RailwayCrossingService();
	
	public boolean loginUser(User user) {
		
		if(user.validate()) {
			User retrievedUser = railWayService.uDAO.retrieve(user.getEmail());
			if (retrievedUser != null && retrievedUser.getUserType() == 2) {
				user.setName(retrievedUser.getName());
				return (retrievedUser.getEmail().equalsIgnoreCase(user.getEmail()) && retrievedUser.getPassword().equals(user.getPassword()));
			}
		}
		return false;
	}
	
	public boolean addOrUpdateCrossing(RailwayCrossing crossing) {
		//adding person incharge into user table too
		railWayService.uDAO.set(crossing.getPersonInCharge());
		return railWayService.cDAO.set(crossing);
	}
	
	public boolean deleteCrossing(RailwayCrossing crossing) {
		return railWayService.cDAO.delete(crossing);
	}
	
	public Map<String, ?> fetchCrossings() {
		//based on object type in parameter throwing main users or crossing map
		return railWayService.cDAO.retrieve(new RailwayCrossing());
	}
	
	public int getCrossingsCount() {
		return railWayService.cDAO.getCrossingsCount();
	}

	public List<Map.Entry<String, RailwayCrossing>> sortRailWayCrossing(){
		return railWayService.cDAO.sortRailwayCrossingByFromString();
	}
	public RailwayCrossing searchCrosing(String crossingName) {
		return  railWayService.cDAO.getCrossingByName(crossingName);
	}
	public boolean changeStatus(RailwayCrossing cross){
		return railWayService.cDAO.updateCrossingStatus(cross);
	}

}
