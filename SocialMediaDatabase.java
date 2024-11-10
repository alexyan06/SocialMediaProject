import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

//stores all accounts
public class SocialMediaDatabase implements SocialMediaInterface{
    private ArrayList<Account> accounts; //lists of user accounts
    private String accountInfo; //String for ALL saved account info
    private ArrayList<String> DMs; //list filenames for all dms
    private String DMFileName;//String for ALL dm filenames

    public SocialMediaDatabase(String accountInfo, String DMFileName) {
        this.accountInfo = accountInfo;
        this.DMFileName = DMFileName;
        this.accounts = new ArrayList<>();
        this.DMs = new ArrayList<>();
        readAccountInfo();
        readDMFileNames();
    }
    //transfer raw accountInfo string (name, password, boolean<friends<blocked)
        //to Account type
    public boolean readAccountInfo() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(accountInfo));
            String line;
            while ((line = br.readLine()) != null) {
                Account account = new Account(line);
                accounts.add(account);
            }
            br.close();

            BufferedReader br2 = new BufferedReader(new FileReader(accountInfo));
            String line2;
            while ((line2 = br2.readLine()) != null) {
                String[] dataSplit = line2.split("<");
                String[] friendsNames = dataSplit[1].split(",");
                String[] blockedNames = dataSplit[2].split(",");

                for (int i = 0; i < friendsNames.length; i++) {
                    friendsNames[i] = friendsNames[i].trim();
                }

                for (int i = 0; i < blockedNames.length; i++) {
                    blockedNames[i] = blockedNames[i].trim();
                }
                ArrayList<Account> friends = new ArrayList<>();
                ArrayList<Account> blocked = new ArrayList<>();

                if (!friendsNames[0].isEmpty()) {
                    for (int j = 0; j < friendsNames.length; j++) {
                        friends.add(findAccount(friendsNames[j]));
                    }
                }

                if (!blockedNames[0].isEmpty()) {
                    for (int j = 0; j < blockedNames.length; j++) {
                        blocked.add(findAccount(blockedNames[j]));
                    }
                }

                Account postAccount = new Account(line2, friends, blocked);
                changeAccount(postAccount.getName(), postAccount);
            }
            br2.close();
            br2 = null;
            return true;

        } catch (IOException e) {
            System.err.println("Error reading accounts file: " + e.getMessage());
            return false;
        } catch (BadDataException e) {
            System.err.println("Error reading accounts file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    //things needed: outputting account data to accountsSaveFile
    public boolean outputAccountInfo() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(accountInfo));
            for (Account account : accounts) {
                bw.write(account.toString() + "\n");
            }
            bw.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing account info: " + e.getMessage());
            return false;
        }
    }
    //read DMFileNames
    public ArrayList<String> readDMFileNames() {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(DMFileName));
            String line;
            while ((line = rd.readLine()) != null) {
                DMs.add(line);
            }
            rd.close();
            return DMs;
        } catch (IOException e) {
            System.out.println("Error reading DMFileNames: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    //output DMFileNames to DMs
    public boolean outputDMFileNames() {
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(DMFileName));
            for (String file : DMs) {
                fw.write(file + "\n");
            }
            fw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    //read DMs by fileName, returning all messages in an ArrayList
    public ArrayList<String> readDMs(String filename) {
        try {
            BufferedReader rd = new BufferedReader(new FileReader(filename));
            String line;
            ArrayList<String> messages = new ArrayList<>();
            while ((line = rd.readLine()) != null) {
                messages.add(line);
            }
            rd.close();
            return messages;
        } catch (IOException e) {
            System.out.println("Error reading Dms from file: " + e.getMessage());
            return new ArrayList<>();
        }
    }
    //output direct messages
    public boolean outputDMs(String filename, ArrayList<String> messages) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            for (String message : messages) {
                bw.write(message + "\n");
            }
            bw.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing DMs to file: " + e.getMessage());
            return false;
        }
    }
    //getters:
    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public ArrayList<String> getDMs() {
        return DMs;
    }

    public String getDMFileName() {
        return DMFileName;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }
    //add message from a sender to target
    public ArrayList<String> addDM(Account sendMes, Account getMes, ArrayList<String> messages, String message) throws InvalidTargetException {
        try {
            String sendMesName = sendMes.getName();
            ArrayList<String> getMesFriendsUsername = new ArrayList<>();
            for (int i = 0; i < getMes.getFriends().size(); i++) {
                getMesFriendsUsername.add(getMes.getFriends().get(i).getName());
            }
            ArrayList<String> getMesBlockedUsername = new ArrayList<>();
            for (int i = 0; i < getMes.getBlocked().size(); i++) {
                getMesBlockedUsername.add(getMes.getBlocked().get(i).getName());
            }

            if (getMes.getFriendsOnly() && !getMesFriendsUsername.contains(sendMesName)) {
                throw new InvalidTargetException("Getters accepts messages from friends only.");
            }
            if (getMesBlockedUsername.contains(sendMesName)) {
                throw new InvalidTargetException("Getters blocked you.");
            }

            int messageSize = messages.size(); //messages is message
            messages.add("[" + messageSize + "] " + sendMes.getName() + ": " + message);

            String filename = getDMFiletxt(sendMes, getMes); //(sendMes,getMes.txt is what's written to
            outputDMs(filename, messages);

            return messages;

        } catch (InvalidTargetException e) {
            System.out.println("Error adding messages: " + e.getMessage());
            throw new InvalidTargetException(e.getMessage());
        }
    }
    //remove message at specific index if sent by remover
    public ArrayList<String> removeDM(ArrayList<String> messages, Account remove, Account other, int index) throws InvalidTargetException {
        try {
            int start = messages.get(index).indexOf("]") + 2;
            int end = messages.get(index).indexOf(":");
            if (!messages.get(index).substring(start, end).equals(remove.getName())) {
                throw new InvalidTargetException("This is not your message!");
            }

            messages.remove(index);
            //reindex
            for (int i = 0; i < messages.size(); i++) {
                int endIndex = messages.get(i).indexOf("]") + 2;
                String reindex = "[" + i + "] " + messages.get(i).substring(endIndex);
                messages.set(i, reindex);
            }

            outputDMs(getDMFiletxt(remove, other), messages);
            return messages;
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Error removing message: " + e.getMessage());
            return null;
        }
    }
    //create DM between sender and target
    public String createDM(Account sendMes, Account getMes) throws InvalidTargetException {
        try {
            String filename = getDMFiletxt(sendMes, getMes);
            if (DMs.contains(filename)) {
                throw new InvalidTargetException("DMs with this person already exists.");
            }
            DMs.add(filename);

            BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
            bw.write("[0] start DMs. \n");
            bw.close();
            return filename;

        } catch (IOException e) {
            System.out.println("Error creating dm file: " + e.getMessage());
            return null;
        }
    }

    //add new accounts to accountsSaveFile and accounts
    public boolean addAccount(String accountStats) {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(accountInfo, true));
            bw.write("\n" + accountStats);
            bw.close();
            Account newAccount = new Account(accountStats);
            accounts.add(newAccount);

            return true;
        } catch (IOException e) {
            System.out.println("Error adding account: " + e.getMessage());
            return false;
        }
    }


    //log into account which returns account if name and password matches in accounts
    public Account login(String name, String password) throws BadDataException {
        for (Account account : accounts) {
            if (account.getName().equals(name) && account.getPassword().equals(password)) {
                System.err.println(account);
                return account;
            }
        }
        throw new BadDataException("Username or password is wrong");
    }
    //find an account by name
    public Account findAccount(String name) throws BadDataException {
        for (Account account : accounts) {
            if (account.getName().equals(name)) {
                return account;
            }
        }
        throw new BadDataException("No account exists with that name");
    }

    //generate a consistent filename based on two account names
    public String getDMFiletxt(Account user, Account user2) {
        ArrayList<String> names = new ArrayList<>(Arrays.asList(user.getName(), user2.getName()));
        Collections.sort(names);
        return names.get(0) + "," + names.get(1) + ".txt";
    }
    //change account
    public void changeAccount(String accountName, Account change) {
        for (int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getName().equals(accountName)) {
                accounts.set(i, change);
                return;
            }
        }
        throw new IllegalArgumentException();
    }
}
