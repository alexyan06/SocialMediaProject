import javax.management.modelmbean.InvalidTargetObjectTypeException;
import java.io.IOException;
import java.util.ArrayList;

public interface SocialMediaInterface {

    // Read account information from file
    boolean readAccountInfo();

    // Output account information to file
    boolean outputAccountInfo();

    // Read DM filenames from file
    ArrayList<String> readDMFileNames();

    // Output DM filenames to file
    boolean outputDMFileNames();

    // Read DMs from specified file
    ArrayList<String> readDMs(String filename);

    // Output DMs to specified file
    boolean outputDMs(String filename, ArrayList<String> messages);

    // Add a direct message from sender to target
    ArrayList<String> addDM(Account sender, Account receiver, ArrayList<String> messages, String message) throws InvalidTargetException;

    // Remove a message at specified index if sent by the remover
    ArrayList<String> removeDM(ArrayList<String> messages, Account remover, Account other, int index) throws InvalidTargetException;

    // Create a new DM file between sender and target
    String createDM(Account sender, Account receiver) throws InvalidTargetException;

    // Add a new account
    boolean addAccount(String accountStats);

    // Log into an account based on username and password
    Account login(String username, String password) throws BadDataException;

    // Find an account by name
    Account findAccount(String name) throws BadDataException;

    // Generate consistent filename for DMs based on two accounts
    String getDMFiletxt(Account user, Account user2);

    // Change an existing account's information
    void changeAccount(String accountName, Account updatedAccount);
}
