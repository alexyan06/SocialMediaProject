import java.util.ArrayList;
/**
 * The Cube class represents a cube in 3D space, composed of six faces.
 *
 * @author Alex Yan yan517
 * @version 1.0 (2024-10-22)
 */
public interface AccountInterface {

    public String getName();
    public void setName(String name);
    public String getPassword();
    public void setPassword(String password);
    public ArrayList<Account> getFriends();
    public void setFriends(ArrayList<Account> friends);
    public boolean addFriend(Account friend);
    public boolean removeFriend(Account friend);
    public void setFriendsOnly(boolean friendsOnly);
    public boolean getFriendsOnly();
    public ArrayList<Account> getBlocked();
    public void setBlocked(ArrayList<Account> blocked);
    public boolean addBlocked(Account blocked);
    public boolean removeBlocked(Account blocked);
    public boolean equals(Account account);
    public String toString();
}
