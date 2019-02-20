package objects;

public class Friendship
{
    private Integer id;
    private Employee friendingId;
    private Employee friendedId;

    public Friendship()
    {
        //For Hibernate
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFriendingId() {
        return friendingId;
    }

    public void setFriendingId(Employee friendingId) {
        this.friendingId = friendingId;
    }

    public Employee getFriendedId() {
        return friendedId;
    }

    public void setFriendedId(Employee friendedId) {
        this.friendedId = friendedId;
    }
}
