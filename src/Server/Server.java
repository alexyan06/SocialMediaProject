import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CS 180 Group Project: Server
 * @author Alex Yan, yan517
 * @author Sripoorna Modugula, smodugul
 * @version 1.0 11/17/2024
 * Server that connects to multiple clients and
 * does the processing of data.
 */

public class Server {
    private static final int PORT = 12346;
    private static ExecutorService pool = Executors.newFixedThreadPool(10);

    private static SocialMediaDatabase mediaDatabase;

    private static final Object LOCK = new Object();

    public static void main(String[] args) {
        mediaDatabase = new SocialMediaDatabase("accountsSave.txt",
                "directMessageFileNames.txt");

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running...");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                pool.execute(new ClientHandler(clientSocket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * CS 180 Group Project: Server
     * Server that connects to multiple clients and
     * does the processing of data.
     */

    private static class ClientHandler implements Runnable {
        private final Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (
                    BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
            ) {
                Account logIn = null;

                while (true) {                          //create or log menu
                    String inputLine = in.readLine();

                    if (inputLine.equals("1")) {
                        String logData = in.readLine();
                        String[] logDataParts = logData.split(",");

                        try {
                            synchronized (LOCK) {
                                logIn = mediaDatabase.login(logDataParts[0], logDataParts[1]);
                            }
                            out.write("Logged in!");
                            out.println();
                            out.flush();

                        } catch (BadDataException e) {
                            System.out.println("Username or password is wrong.");
                            out.write("Username or password is wrong.");
                            out.println();
                            out.flush();

                            continue;
                        }

                    } else {
                        String logData = in.readLine();
                        String[] logDataParts = logData.split(",");


                        try {
                            synchronized (LOCK) {
                                Account existant = mediaDatabase.findAccount(logDataParts[0]);
                            }
                            System.out.println("Username is taken.");
                            out.write("Username is taken.");
                            out.println();
                            out.flush();

                            continue;
                        } catch (BadDataException e) { //IMPORTANT
                            String logInAccountData = logDataParts[0] + "," + logDataParts[1] + "," + logDataParts[2];
                            logIn = new Account(logInAccountData);
                            synchronized (LOCK) {
                                mediaDatabase.addAccount(logInAccountData);
                            }
                            out.write("Account created!");
                            out.println();
                            out.flush();
                        }

                    }

                    break;
                }

                //MENU:
                while (true) {
                    String menuIn = in.readLine();

                    if (menuIn.equals("1")) {                   //option 1
                        String friendsOnly = in.readLine();

                        if (friendsOnly.equals(";")) {
                            out.write("Invalid Input");
                            out.println();
                            out.flush();
                        } else if (friendsOnly.equals("true")) {
                            logIn.setFriendsOnly(true);
                            synchronized (LOCK) {
                                mediaDatabase.changeAccount(logIn.getName(), logIn);
                            }
                            out.write("Friends Only changed");
                            out.println();
                            out.flush();
                        } else {
                            logIn.setFriendsOnly(false);
                            synchronized (LOCK) {
                                mediaDatabase.changeAccount(logIn.getName(), logIn);
                            }
                            out.write("Friends Only changed");
                            out.println();
                            out.flush();
                        }

                        continue;
                    }

                    if (menuIn.equals("2")) {                   //option 2
                        String newFriend = in.readLine();

                        ArrayList<Account> friends = logIn.getFriends();
                        boolean alreadyFriend = false;
                        if (!friends.isEmpty()) {
                            for (int i = 0; i < friends.size(); i++) {
                                if (newFriend.equals(friends.get(i).getName())) {
                                    System.out.println("That person is already your friend!");
                                    alreadyFriend = true;
                                }
                            }
                        }
                        if (alreadyFriend) {
                            out.write("Failure: already a friend");
                            out.println();
                            out.flush();

                            continue;
                        }

                        try {
                            Account addFriend;
                            synchronized (LOCK) {
                                addFriend = mediaDatabase.findAccount(newFriend);
                            }
                            boolean result = logIn.addFriend(addFriend);
                            if (!result) {
                                System.out.println("System error failure");
                                out.write("Failure: system error");
                                out.println();
                                out.flush();
                                continue;
                            }
                            synchronized (LOCK) {
                                mediaDatabase.changeAccount(logIn.getName(), logIn);
                            }
                            out.write("Success!");
                            out.println();
                            out.flush();
                            continue;

                        } catch (BadDataException e) {
                            System.out.println("No account exists by that name");
                            out.write("Failure: no account exists by that name");
                            out.println();
                            out.flush();
                            continue;
                        }
                    }

                    if (menuIn.equals("3")) {                       //option 3
                        String removeFriend = in.readLine();
                        boolean result = false;

                        ArrayList<Account> friends = logIn.getFriends();
                        ArrayList<Account> blocked = logIn.getBlocked();
                        if (!friends.isEmpty()) {
                            for (int i = 0; i < friends.size(); i++) {
                                if (removeFriend.equals(friends.get(i).getName())) {
                                    if (!blocked.isEmpty()) {
                                        for (int j = 0; j < blocked.size(); j++) {
                                            if (blocked.get(j).getName().equals(removeFriend)) {
                                                blocked.remove(blocked.get(j));
                                                break;
                                            }
                                        }
                                    }
                                    logIn.removeFriend(friends.get(i));
                                    System.out.println("done");
                                    synchronized (LOCK) {
                                        mediaDatabase.changeAccount(logIn.getName(), logIn);
                                    }

                                    result = true;
                                }
                            }
                        }
                        if (result) {
                            out.write("Success!");
                            out.println();
                            out.flush();
                        } else {
                            out.write("Failure");
                            out.println();
                            out.flush();
                        }

                        continue;
                    }

                    if (menuIn.equals("4")) {                           //option 4
                        String newBlocked = in.readLine();

                        ArrayList<Account> blocked = logIn.getBlocked();
                        boolean alreadyBlocked = false;
                        for (int i = 0; i < blocked.size(); i++) {
                            if (newBlocked.equals(blocked.get(i).getName())) {
                                System.out.println("That person is already blocked!");
                                alreadyBlocked = true;
                            }
                        }
                        if (alreadyBlocked) {
                            out.write("Failure: already blocked");
                            out.println();
                            out.flush();

                            continue;
                        }

                        try {
                            Account addBlocked;
                            synchronized (LOCK) {
                                addBlocked = mediaDatabase.findAccount(newBlocked);
                            }
                            boolean result = logIn.addBlocked(addBlocked);
                            if (!result) {
                                System.out.println("System error failure");
                                out.write("Failure: system error");
                                out.println();
                                out.flush();
                                continue;
                            }
                            synchronized (LOCK) {
                                mediaDatabase.changeAccount(logIn.getName(), logIn);
                            }
                            out.write("Success");
                            out.println();
                            out.flush();
                            continue;

                        } catch (BadDataException e) {
                            System.out.println("No account exists by that name");
                            out.write("Failure: no account by that name");
                            out.println();
                            out.flush();
                            continue;
                        }
                    }

                    if (menuIn.equals("5")) {                       //option 5
                        String removeBlocked = in.readLine();
                        boolean result = false;

                        ArrayList<Account> blocked = logIn.getBlocked();
                        ArrayList<Account> friends = logIn.getFriends();
                        if (!blocked.isEmpty()) {
                            for (int i = 0; i < blocked.size(); i++) {
                                if (removeBlocked.equals(blocked.get(i).getName())) {
                                    if (!friends.isEmpty()) {
                                        for (int j = 0; j < friends.size(); j++) {
                                            if (friends.get(j).getName().equals(removeBlocked)) {
                                                friends.remove(friends.get(j));
                                                break;
                                            }
                                        }
                                    }
                                    logIn.removeBlocked(blocked.get(i));
                                    System.out.println("done");
                                    synchronized (LOCK) {
                                        mediaDatabase.changeAccount(logIn.getName(), logIn);
                                    }

                                    result = true;
                                }
                            }
                        }

                        if (result) {
                            out.write("Success!");
                            out.println();
                            out.flush();
                        } else {
                            out.write("Failure");
                            out.println();
                            out.flush();
                        }
                        continue;
                    }

                    if (menuIn.equals("6")) {                       //option 6
                        String dmMenu = in.readLine();
                        System.out.println(dmMenu);

                        if (dmMenu.equals("1")) {
                            String dmStartTargetName = in.readLine();

                            try {
                                Account dmStartTarget;
                                synchronized (LOCK) {
                                    dmStartTarget = mediaDatabase.findAccount(dmStartTargetName);
                                }

                                try {
                                    synchronized (LOCK) {
                                        mediaDatabase.createDM(logIn, dmStartTarget);
                                        System.out.println("DM has been created!");
                                        mediaDatabase.outputDMFileNames();              //updates save file
                                    }
                                    out.write("Success!");
                                    out.println();
                                    out.flush();

                                    continue;
                                } catch (InvalidTargetException e) {
                                    System.out.println("You cannot send to this person " +
                                            "OR a DM with them already exists.");

                                    out.write("Failure: invalid target");
                                    out.println();
                                    out.flush();

                                    continue;
                                }

                            } catch (BadDataException e) {
                                System.out.println("No account exists by that name");

                                out.write("Failure: no account exists by that name");
                                out.println();
                                out.flush();

                                continue;
                            }
                        }

                        if (dmMenu.equals("2")) {
                            String dmReadTargetName = in.readLine();

                            try {
                                synchronized (LOCK) {
                                    Account dmReadTarget = mediaDatabase.findAccount(dmReadTargetName);
                                }

                                ArrayList<String> namesForFileName = new ArrayList<>();
                                namesForFileName.add(logIn.getName());
                                namesForFileName.add(dmReadTargetName);
                                namesForFileName.sort(Comparator.naturalOrder());
                                String fileName = namesForFileName.get(0) + "," + namesForFileName.get(1) + ".txt";
                                boolean isThere = false;
                                for (int i = 0; i < mediaDatabase.getDMs().size(); i++) {
                                    if (mediaDatabase.getDMs().contains(fileName)) {
                                        isThere = true;
                                    }
                                }

                                if (!isThere) {
                                    out.write("Failure!");
                                    out.println();
                                    out.flush();
                                } else {
                                    ArrayList<String> messages;
                                    synchronized (LOCK) {
                                        messages = mediaDatabase.readDMs(fileName);
                                        out.write("Success!");
                                        out.println();
                                        out.flush();
                                    }
                                    out.write("" + messages.size());
                                    out.println();
                                    out.flush();

                                    for (int i = 0; i < messages.size(); i++) {
                                        System.out.println(messages.get(i));

                                        out.write(messages.get(i));
                                        out.println();
                                        out.flush();
                                    }
                                    continue;
                                }

                            } catch (BadDataException e) {
                                System.out.println("No account exists by that name");

                                out.write("Failure: no account exists by that name");
                                out.println();
                                out.flush();

                                continue;
                            }
                        }

                        if (dmMenu.equals("3")) {
                            String dmSendTargetName = in.readLine();

                            try {
                                Account dmSendTarget;
                                synchronized (LOCK) {
                                    dmSendTarget = mediaDatabase.findAccount(dmSendTargetName);
                                }

                                ArrayList<String> namesForFileName = new ArrayList<>();
                                namesForFileName.add(logIn.getName());
                                namesForFileName.add(dmSendTargetName);
                                namesForFileName.sort(Comparator.naturalOrder());
                                String fileName = namesForFileName.get(0) + "," + namesForFileName.get(1) + ".txt";

                                ArrayList<String> messages;
                                synchronized (LOCK) {
                                    messages = mediaDatabase.readDMs(fileName);
                                }


                                System.out.println("Enter Message:");
                                out.write("Enter Message:");
                                out.println();
                                out.flush();
                                String message = in.readLine();

                                ArrayList<String> newMessages;
                                try {
                                    synchronized (LOCK) {
                                        newMessages = mediaDatabase.addDM(logIn, dmSendTarget, messages, message);
                                    }
                                } catch (InvalidTargetException e) {
                                    System.out.println("You cannot send a message to this person!");

                                    out.write("Failure: invalid target");
                                    out.println();
                                    out.flush();

                                    continue;
                                }

                                boolean result;
                                synchronized (LOCK) {
                                    result = mediaDatabase.outputDMs(fileName, newMessages);
                                }
                                if (!result) {
                                    System.out.println("System error");
                                    out.write("Failure: system error");
                                    out.println();
                                    out.flush();
                                } else {
                                    System.out.println("Sent");
                                    out.write("Success!");
                                    out.println();
                                    out.flush();
                                }
                                continue;

                            } catch (BadDataException e) {
                                System.out.println("No account exists by that name");
                                out.write("Failure: no account exists by that name");
                                out.println();
                                out.flush();
                                continue;
                            }
                        }

                        if (dmMenu.equals("4")) {
                            String dmRemoveTargetName = in.readLine();

                            try {
                                Account dmRemoveTarget;
                                synchronized (LOCK) {
                                    dmRemoveTarget = mediaDatabase.findAccount(dmRemoveTargetName);
                                }

                                ArrayList<String> namesForFileName = new ArrayList<>();
                                namesForFileName.add(logIn.getName());
                                namesForFileName.add(dmRemoveTargetName);
                                namesForFileName.sort(Comparator.naturalOrder());
                                String fileName = namesForFileName.get(0) + "," + namesForFileName.get(1) + ".txt";

                                ArrayList<String> messages;
                                synchronized (LOCK) {
                                    messages = mediaDatabase.readDMs(fileName);
                                }

                                out.write("Enter Index of Message to remove:");
                                out.println();
                                out.flush();

                                String indexString = in.readLine(); //change to int only
                                int index = 0;
                                try {
                                    index = Integer.parseInt(indexString);
                                } catch (NumberFormatException e) {
                                    System.out.println("Invalid Index");
                                    out.write("Failure: Invalid Index");
                                    out.println();
                                    out.flush();
                                    continue;
                                }

                                ArrayList<String> newMessages;
                                try {
                                    synchronized (LOCK) {
                                        newMessages = mediaDatabase.removeDM(messages, logIn, dmRemoveTarget, index);
                                    }
                                } catch (InvalidTargetException e) {
                                    System.out.println("You cannot delete this message");
                                    out.write("Failure: you cannot delete this message");
                                    out.println();
                                    out.flush();
                                    continue;
                                }

                                boolean result;
                                synchronized (LOCK) {
                                    result = mediaDatabase.outputDMs(fileName, newMessages);
                                }
                                if (!result) {
                                    System.out.println("System error");
                                    out.write("Failure: system error");
                                    out.println();
                                    out.flush();
                                } else {
                                    System.out.println("Removed");
                                    out.write("Success!");
                                    out.println();
                                    out.flush();
                                }
                                continue;

                            } catch (BadDataException e) {
                                System.out.println("No account exists by that name");
                                out.write("Failure: no account exists by that name");
                                out.println();
                                out.flush();
                                continue;
                            }
                        }

                        if (dmMenu.equals(";")) {
                            //do nothing, invalid selection
                            continue;
                        }
                    }

                    if (menuIn.equals("7")) {
                        System.out.println("Bye");
                        synchronized (LOCK) {
                            mediaDatabase.outputAccountInfo();
                            mediaDatabase.outputDMFileNames();
                        }
                        break;
                    }
                }



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
