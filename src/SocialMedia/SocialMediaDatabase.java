import java.io.*;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * CS 180 Group Project: Server
 * @author Alex Yan, yan517
 * @author Sripoorna Modugula, smodugul
 * @version 1.0 11/17/2024
 * Server that connects to multiple clients and
 * does the processing of data.
 */

//stores all accounts
public class SocialMediaDatabase extends Thread implements SocialMediaInterface {
    private ArrayList<Account> accounts; //lists of user accounts
    private String accountInfo; //String for ALL saved account info
    private ArrayList<String> dMs; //list filenames for all dms
    private String dMFileName; //String for ALL dm filenames
    private final static Object GATEKEEPER = new Object();

    public SocialMediaDatabase(String accountInfo, String dMFileName) {
        this.accountInfo = accountInfo;
        this.dMFileName = dMFileName;
        this.accounts = new ArrayList<>();
        this.dMs = new ArrayList<>();
        readAccountInfo();
        readDMFileNames();
    }
    //transfer raw accountInfo string (name, password, boolean<friends<blocked)
    //to Account type
    public boolean readAccountInfo() {
        synchronized (GATEKEEPER) {
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
                return true;

            } catch (IOException e) {
                System.err.println("Error reading accounts file: " + e.getMessage());
                return false;
            } catch (BadDataException e) {
                System.err.println("Error reading accounts file: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    //things needed: outputting account data to accountsSaveFile
    public boolean outputAccountInfo() {
        synchronized (GATEKEEPER) {
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
    }
    //read dMFileNames
    public ArrayList<String> readDMFileNames() {
        synchronized (GATEKEEPER) {
            try {
                BufferedReader rd = new BufferedReader(new FileReader(dMFileName));
                String line;
                while ((line = rd.readLine()) != null) {
                    dMs.add(line);
                }
                rd.close();
                return dMs;
            } catch (IOException e) {
                System.out.println("Error reading dMFileNames: " + e.getMessage());
                return new ArrayList<>();
            }
        }
    }
    //output dMFileNames to DMs
    public boolean outputDMFileNames() {
        synchronized (GATEKEEPER) {
            try {
                BufferedWriter fw = new BufferedWriter(new FileWriter(dMFileName));
                for (String file : dMs) {
                    fw.write(file + "\n");
                }
                fw.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
    }
    //read DMs by fileName, returning all messages in an ArrayList
    public ArrayList<String> readDMs(String filename) {
        synchronized (GATEKEEPER) {
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
    }
    //output direct messages
    public boolean outputDMs(String filename, ArrayList<String> messages) {
        synchronized (GATEKEEPER) {
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
    }
    //getters:
    public ArrayList<Account> getAccounts() {
        return accounts;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public ArrayList<String> getDMs() {
        return dMs;
    }

    public String getDMFileName() {
        return dMFileName;
    }

    public void setAccounts(ArrayList<Account> accounts) {
        this.accounts = accounts;
    }
    //add message from a sender to target
    public ArrayList<String> addDM(Account sendMes, Account getMes,
                                   ArrayList<String> messages, String message)
            throws InvalidTargetException {
        synchronized (GATEKEEPER) {
            try {

                if (getMes.getFriendsOnly() && !getMes.getFriendsName().contains(sendMes.getName())) {
                    throw new InvalidTargetException("Getters accepts messages from friends only.");
                }
                if (getMes.getBlockedName().contains(sendMes.getName())) {
                    throw new InvalidTargetException("Getters blocked you.");
                }

                if (sendMes.getFriendsOnly() && !sendMes.getFriendsName().contains(getMes.getName())) {
                    throw new InvalidTargetException("Senders can only send messages to friends");
                }

                if (sendMes.getBlockedName().contains(getMes.getName())) {
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
    }
    //remove message at specific index if sent by remover
    public ArrayList<String> removeDM(ArrayList<String> messages,
                                      Account remove, Account other, int index)
            throws InvalidTargetException {
        synchronized (GATEKEEPER) {
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
    }
    //create DM between sender and target
    public String createDM(Account sendMes, Account getMes) throws InvalidTargetException {
        synchronized (GATEKEEPER) {
            try {
                String filename = getDMFiletxt(sendMes, getMes);
                if (dMs.contains(filename)) {
                    System.err.println("1");
                    throw new InvalidTargetException("DMs with this person already exists.");
                }
                if (getMes.getFriendsOnly() && !getMes.getFriendsName().contains(sendMes.getName())) {
                    System.err.println("2");
                    throw new InvalidTargetException("Getters accepts messages from friends only.");
                }
                if (getMes.getBlockedName().contains(sendMes.getName())) {
                    System.err.println("3");
                    throw new InvalidTargetException("Getters blocked you.");
                }

                if (sendMes.getFriendsOnly() && !sendMes.getFriendsName().contains(getMes.getName())) {
                    System.err.println("4");
                    throw new InvalidTargetException("Senders can only send messages to friends");
                }

                if (sendMes.getBlockedName().contains(getMes.getName())) {
                    System.err.println("5");
                    throw new InvalidTargetException("Getters blocked you.");
                }

                dMs.add(filename);
                BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
                bw.write("[0] start DMs. \n");
                bw.close();
                return filename;

            } catch (IOException e) {
                System.out.println("Error creating dm file: " + e.getMessage());
                return null;
            }
        }
    }

    //add new accounts to accountsSaveFile and accounts
    public boolean addAccount(String accountStats) {
        synchronized (GATEKEEPER) {
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
    }


    //log into account which returns account if name and password matches in accounts
    public Account login(String name, String password) throws BadDataException {
        synchronized (GATEKEEPER) {
            for (Account account : accounts) {
                if (account.getName().equals(name) && account.getPassword().equals(password)) {
                    System.err.println(account);
                    return account;
                }
            }
            throw new BadDataException("Username or password is wrong");
        }
    }
    //find an account by name
    public Account findAccount(String name) throws BadDataException {
        synchronized (GATEKEEPER) {
            for (Account account : accounts) {
                if (account.getName().equals(name)) {
                    return account;
                }
            }
            throw new BadDataException("No account exists with that name");
        }
    }

    //generate a consistent filename based on two account names
    public String getDMFiletxt(Account user, Account user2) {
        synchronized (GATEKEEPER) {
            ArrayList<String> names = new ArrayList<>(Arrays.asList(user.getName(), user2.getName()));
            Collections.sort(names);
            return names.get(0) + "," + names.get(1) + ".txt";
        }
    }
    //change account
    public void changeAccount(String accountName, Account change) {
        synchronized (GATEKEEPER) {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getName().equals(accountName)) {
                    accounts.set(i, change);
                    return;
                }
            }
            throw new IllegalArgumentException();
        }
    }
}
