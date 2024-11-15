import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

public class SocialMediaDatabase implements SocialMediaInterface {
    private final ArrayList<Account> accounts; // Lists of user accounts
    private final String accountInfo; // String for ALL saved account info
    private final ArrayList<String> DMs; // List filenames for all DMs
    private final String DMFileName; // String for ALL DM filenames
    private final ReentrantLock lock = new ReentrantLock(); // Lock for synchronization

    public SocialMediaDatabase(String accountInfo, String DMFileName) {
        this.accountInfo = accountInfo;
        this.DMFileName = DMFileName;
        this.accounts = new ArrayList<>();
        this.DMs = new ArrayList<>();
        readAccountInfo();
        readDMFileNames();
    }

    public boolean readAccountInfo() {
        lock.lock(); // ADDED LOCK
        try {
            BufferedReader br = new BufferedReader(new FileReader(accountInfo));
            String line;
            while ((line = br.readLine()) != null) {
                Account account = new Account(line);
                accounts.add(account);
            }
            br.close();
            return true;
        } catch (IOException e) {
            System.err.println("Error reading accounts file: " + e.getMessage());
            return false;
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public boolean outputAccountInfo() {
        lock.lock(); // ADDED LOCK
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
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public ArrayList<String> readDMFileNames() {
        lock.lock(); // ADDED LOCK
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
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public boolean outputDMFileNames() {
        lock.lock(); // ADDED LOCK
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter(DMFileName));
            for (String file : DMs) {
                fw.write(file + "\n");
            }
            fw.close();
            return true;
        } catch (IOException e) {
            System.out.println("Error writing DMFileNames: " + e.getMessage());
            return false;
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public ArrayList<String> readDMs(String filename) {
        lock.lock(); // ADDED LOCK
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
            System.out.println("Error reading DMs from file: " + e.getMessage());
            return new ArrayList<>();
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public boolean outputDMs(String filename, ArrayList<String> messages) {
        lock.lock(); // ADDED LOCK
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
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public boolean addAccount(String accountStats) {
        lock.lock(); // ADDED LOCK
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
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public Account login(String name, String password) throws BadDataException {
        lock.lock(); // ADDED LOCK
        try {
            for (Account account : accounts) {
                if (account.getName().equals(name) && account.getPassword().equals(password)) {
                    return account;
                }
            }
            throw new BadDataException("Username or password is wrong");
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public Account findAccount(String name) throws BadDataException {
        lock.lock(); // ADDED LOCK
        try {
            for (Account account : accounts) {
                if (account.getName().equals(name)) {
                    return account;
                }
            }
            throw new BadDataException("No account exists with that name");
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public void changeAccount(String accountName, Account change) {
        lock.lock(); // ADDED LOCK
        try {
            for (int i = 0; i < accounts.size(); i++) {
                if (accounts.get(i).getName().equals(accountName)) {
                    accounts.set(i, change);
                    return;
                }
            }
            throw new IllegalArgumentException();
        } finally {
            lock.unlock(); // RELEASE LOCK
        }
    }

    public String getDMFiletxt(Account user, Account user2) {
        ArrayList<String> names = new ArrayList<>(Arrays.asList(user.getName(), user2.getName()));
        Collections.sort(names);
        return names.get(0) + "," + names.get(1) + ".txt";
    }
}
