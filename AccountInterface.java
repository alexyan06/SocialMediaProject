import java.util.ArrayList;

public interface AccountInterface {

    public String getName();
    public void setName(String name);

    public String getUsername();
    public void setUsername(String username);

    public String getPassword();
    public void setPassword(String password);

    public ArrayList<Account> getFriends();
    public void setFriends(ArrayList<Account> friends);
    public boolean addFriend(Account friend);
    public boolean removeFriend(Account friend);

    public ArrayList<Account> getBlocked();
    public void setBlocked(ArrayList<Account> blocked);
    public boolean addBlocked(Account blocked);
    public boolean removeBlocked(Account blocked);

    public boolean equals(Account account);
    public String toString();
}
