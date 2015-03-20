package communication.responses;

import java.util.ArrayList;

import models.Group;

public class GroupResponse extends BaseResponse {
	private ArrayList<Group> groups = new ArrayList<Group>();

	public ArrayList<Group> getGroups() {
		return groups;
	}

	public void addGroup(Group Group) {
		groups.add(Group);
	}
}
