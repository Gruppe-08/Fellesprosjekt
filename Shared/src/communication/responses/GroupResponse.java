package communication.responses;

import java.util.ArrayList;

import models.Group;

public class GroupResponse extends BaseResponse {
	private ArrayList<Group> Groups = new ArrayList<Group>();

	public ArrayList<Group> getGroups() {
		return Groups;
	}

	public void addGroup(Group Group) {
		Groups.add(Group);
	}
}
