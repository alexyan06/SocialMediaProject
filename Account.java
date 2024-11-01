import java.util.ArrayList;

public class Account implements AccountInterface {

    private String name;
    private String userName;
    private String password;
    private ArrayList<Account> friends;
    private ArrayList<Account> blocked;

    public Account(String data) {
        //exp: "Cassandra Conners, cconners123, password123"
        String[] info = data.split(",");
        for (int i = 0; i < info.length; i++) {
            info[i] = info[i].trim();
        }
        this.name = info[0];
        this.userName = info[1];
        this.password = info[2];
    }

    public Account(String data, ArrayList<Account> friends, ArrayList<Account> blocked) {
        //exp: "Cassandra Conners, cconners123, password123"
        String[] info = data.split(",");
        for (int i = 0; i < info.length; i++) {
            info[i] = info[i].trim();
        }
        this.name = info[0];
        this.userName = info[1];
        this.password = info[2];

        this.friends = friends;
        this.blocked = blocked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String username) {
        this.userName = username;
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

    public boolean addBlocked(Account blocked) {
        if (this.blocked.contains(blocked)) {
            return true;
        }
        if (this.friends.contains(blocked)) {
            removeFriend(blocked);
        }
        return this.blocked.add(blocked);
    }

    public boolean removeBlocked(Account blocked) {
        return this.blocked.remove(blocked);
    }

    public boolean equals(Account account) {
        return this.name.equals(account.getName());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append(", ").append(userName).append(", ").append(password);

        if (friends != null && !friends.isEmpty()) {
            sb.append(" Friends: ");
            for (int i = 0; i < friends.size(); i++) {
                sb.append(friends.get(i).toString());
                if (i < friends.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        if (blocked != null && !blocked.isEmpty()) {
            sb.append(" Blocked: ");
            for (int i = 0; i < blocked.size(); i++) {
                sb.append(blocked.get(i).toString());
                if (i < blocked.size() - 1) {
                    sb.append(", ");
                }
            }
        }

        return sb.toString();
    }
}
