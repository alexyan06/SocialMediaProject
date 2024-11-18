import java.util.ArrayList;
import java.util.Arrays;

/**
 * The Cube class represents a cube in 3D space, composed of six faces.
 *
 * @author Alex Yan yan517
 * @version 1.0 (2024-10-22)
 */
public class Account implements AccountInterface {

    private String name;
    private String password;
    private ArrayList<Account> friends;
    private ArrayList<Account> blocked;
    private boolean friendsOnly;


    public Account(String data) {
        //exp: "Cassandra Conners, cconners123, password123, true"
        String[] info = data.split(",");
        for (int i = 0; i < info.length; i++) {
            info[i] = info[i].trim();
        }
        this.name = info[0];
        this.password = info[1];
        this.friendsOnly = Boolean.parseBoolean(info[2]);
    }

    public Account(String data, ArrayList<Account> friends, ArrayList<Account> blocked) {
        //exp: "Cassandra Conners, password123, true, "
        String[] info = data.split("<", 3);
        String[] userInfo = info[0].split(",");
        for (int i = 0; i < userInfo.length; i++) {
            userInfo[i] = userInfo[i].trim();
        }

        this.name = userInfo[0];
        this.password = userInfo[1];
        this.friendsOnly = Boolean.parseBoolean(userInfo[2]);

        this.friends = friends;
        this.blocked = blocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getFriendsOnly() {
        return friendsOnly;
    }

    public void setFriendsOnly(boolean friendsOnly) {
        this.friendsOnly = friendsOnly;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<Account> getFriends() {
        return friends;
    }

    public ArrayList<String> getFriendsName() {
        ArrayList<String> test = new ArrayList<>();
        if (!friends.isEmpty()) {
            for (int i = 0; i < friends.size(); i++) {
                test.add(friends.get(i).getName());
            }
        }
        return test;
    }

    public ArrayList<String> getBlockedName() {
        ArrayList<String> test = new ArrayList<>();
        if (!blocked.isEmpty()) {
            for (int i = 0; i < blocked.size(); i++) {
                test.add(blocked.get(i).getName());
            }
        }
        return test;
    }


    public boolean addFriend(Account friend) {
        if (friends.contains(friend)) {
            return true;
        }
        if (blocked.contains(friend)) {
            removeBlocked(friend);
        }
        return friends.add(friend);
    }

    public void setFriends(ArrayList<Account> friends) {
        this.friends = friends;
    }

    public boolean removeFriend(Account friend) {
        return friends.remove(friend);
    }

    public ArrayList<Account> getBlocked() {
        return blocked;
    }

    public void setBlocked(ArrayList<Account> blocked) {
        this.blocked = blocked;
    }

    public boolean addBlocked(Account bk) {
        if (this.blocked.contains(bk)) {
            return true;
        }
        if (this.friends.contains(bk)) {
            removeFriend(bk);
        }
        return this.blocked.add(bk);
    }

    public boolean removeBlocked(Account bk) {
        return this.blocked.remove(bk);
    }

    public boolean equals(Account account) {
        return this.name.equals(account.getName());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(", ").append(password).append(", ").append(friendsOnly);

        if (friends != null && !friends.isEmpty()) {
            sb.append("<");
            for (int i = 0; i < friends.size(); i++) {
                sb.append(friends.get(i).getName());
                if (i < friends.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        if (blocked != null && !blocked.isEmpty()) {
            sb.append("<");
            for (int i = 0; i < blocked.size(); i++) {
                sb.append(blocked.get(i).getName());
                if (i < blocked.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }
}
