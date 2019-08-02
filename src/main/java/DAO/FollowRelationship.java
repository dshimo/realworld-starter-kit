package DAO;

public class FollowRelationship {
	private String userID;
	private String friendID;
	
	public FollowRelationship(String userID, String friendID) {
		this.userID = userID;
		this.friendID = friendID;
	}
}
