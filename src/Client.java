import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * CS 180 Group Project: Client
 *
 * Defines Client, which is the Client side that connects to the server. This is
 * functionally what the users directly interact with.
 */

public class Client {
    private static final String SERVER_IP = "localhost";
    private static final int SERVER_PORT = 12346;

    public static void main(String[] args) {
        try (
                Socket socket = new Socket(SERVER_IP, SERVER_PORT);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scan = new Scanner(System.in);
        ) {
            System.out.println("Connected to server...");


            while (true) {
                System.out.println("Would you like to log in (1) or create an account (2)?");
                String logOrCreateString = scan.nextLine();
                boolean logOrCreateCheck = false;
                int logOrCreate = 0;
                try {
                    logOrCreate = Integer.parseInt(logOrCreateString);
                    if (logOrCreate == 1 || logOrCreate == 2) {
                        logOrCreateCheck = true;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection");
                    continue;
                }

                if (logOrCreateCheck) {
                    if (logOrCreate == 1) {             //log in
                        //to server
                        out.write("1");
                        out.println();
                        out.flush();

                        System.out.println("Enter Username:");
                        String username = scan.nextLine();
                        System.out.println("Enter Password:");
                        String password = scan.nextLine();
                        if (username.contains("<") || username.contains(",")
                                || password.contains("<") || password.contains(",")) {
                            System.out.println("Invalid selection, no '<' or ','");

                            //to server. INVALID
                            out.write(":,:"); //?
                            out.println();
                            out.flush();

                            continue;
                        } else {

                            //to server. VALID
                            out.write(username + "," + password);
                            out.println();
                            out.flush();

                            String response = in.readLine();
                            System.out.println(response);
                            if (!response.equals("Logged in!")) {
                                continue;
                            }
                        }

                    } else {                            //create account
                        //to server
                        out.write("2");
                        out.println();
                        out.flush();

                        System.out.println("Make Username:");
                        String newUsername = scan.nextLine();
                        System.out.println("Make Password:");
                        String newPassword = scan.nextLine();
                        System.out.println("Friends Only for messages? (true/false)");
                        String friendsOnly = scan.nextLine();
                        if (!friendsOnly.equals("true") && !friendsOnly.equals("false")) {
                            System.out.println("Invalid selection, only true or false.");

                            //to server. INVALID
                            out.write(":,:,:"); //documentation
                            out.println();
                            out.flush();

                            continue;
                        }
                        if (newUsername.contains("<") || newUsername.contains(",")
                                || newPassword.contains("<") || newPassword.contains(",")) {
                            System.out.println("Invalid selection, no '<' or ','");

                            //to server. INVALID
                            out.write(":,:,:");
                            out.println();
                            out.flush();

                            continue;
                        } else {

                            //to server. VALID
                            out.write(newUsername + "," + newPassword + "," + friendsOnly);
                            out.println();
                            out.flush();

                            String response = in.readLine();
                            System.out.println(response);
                            if (!response.equals("Account created!")) {
                                continue;
                            }
                        }
                    }
                } else {
                    continue;
                }

                break;
            }


            //MENU:
            while (true) {
                System.out.println("What would you like to do?\n(1)Change FriendsOnly\n(2)Add a friend" +
                        "\n(3)Remove a friend\n(4)Add blocked\n(5)Remove blocked\n(6)Access a DM\n(7)exit");
                String menuString = scan.nextLine();
                int menu = 0;
                try {
                    menu = Integer.parseInt(menuString);
                    if (menu != 1 && menu != 2 && menu != 3 && menu != 4
                            && menu != 5 && menu != 6 && menu != 7) {
                        System.out.println("Invalid number");
                        continue;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid selection");
                    continue;
                }

                out.write(menuString);
                out.println();
                out.flush();

                if (menu == 1) {
                    System.out.println("Friends Only for messages? (true/false)");
                    String friendsOnly = scan.nextLine();
                    if (!friendsOnly.equals("true") && !friendsOnly.equals("false")) {
                        System.out.println("Invalid selection, only true or false.");

                        //to server. INVALID
                        out.write(";");
                        out.println();
                        out.flush();

                        continue;
                    } else {
                        //to server. VALID
                        out.write(friendsOnly);
                        out.println();
                        out.flush();

                        continue;
                    }
                }

                if (menu == 2) {
                    System.out.println("Who are you adding as a friend?");
                    String newFriend = scan.nextLine();

                    //to server. VALID
                    out.write(newFriend);
                    out.println();
                    out.flush();

                    String response = in.readLine();
                    System.out.println(response);
                    continue;
                }

                if (menu == 3) {
                    System.out.println("Who are removing as a friend?");
                    String removeFriend = scan.nextLine();

                    //to server. VALID
                    out.write(removeFriend);
                    out.println();
                    out.flush();

                    String response = in.readLine();
                    System.out.println(response);
                    continue;
                }

                if (menu == 4) {
                    System.out.println("Who are you blocking?");
                    String newBlocked = scan.nextLine();

                    //to server. VALID
                    out.write(newBlocked);
                    out.println();
                    out.flush();

                    String response = in.readLine();
                    System.out.println(response);
                    continue;
                }

                if (menu == 5) {
                    System.out.println("Who are removing from blocked?");
                    String removeBlocked = scan.nextLine();

                    //to server. VALID
                    out.write(removeBlocked);
                    out.println();
                    out.flush();

                    String response = in.readLine();
                    System.out.println(response);
                    continue;
                }

                if (menu == 6) {

                    System.out.println("What are you doing?\n(1)Starting a DM\n(2)Reading DMs" +
                            "\n(3)Sending a DM\n(4)Deleting a DM");
                    String dmMenuString = scan.nextLine();
                    int dmMenu = 0;
                    try {
                        dmMenu = Integer.parseInt(dmMenuString);
                        if (dmMenu != 1 && dmMenu != 2 && dmMenu != 3 && dmMenu != 4) {
                            System.out.println("Invalid number");

                            //to server. INVALID
                            out.write(";");
                            out.println();
                            out.flush();

                            continue;
                        } 
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid selection");

                        //to server. INVALID
                        out.write(";");
                        out.println();
                        out.flush();

                        continue;
                    }

                    //to server. VALID
                    out.write(dmMenuString);
                    out.println();
                    out.flush();

                    if (dmMenu == 1) {
                        System.out.println("Who are you starting a DM with?");
                        String dmStartTargetName = scan.nextLine();

                        //to server. VALID
                        out.write(dmStartTargetName);
                        out.println();
                        out.flush();

                        String response = in.readLine();
                        System.out.println(response);
                        continue;
                    }

                    if (dmMenu == 2) {
                        System.out.println("Who are you reading DMs with?");
                        String dmReadTargetName = scan.nextLine();

                        //to server. VALID
                        out.write(dmReadTargetName);
                        out.println();
                        out.flush();

                        String response = in.readLine();
                        System.out.println(response);

                        if (response.equals("Success!")) {
                            String strLength = in.readLine();
                            int messagesLength = Integer.parseInt(strLength);

                            for (int i = 0; i < messagesLength; i++) {
                                String message = in.readLine();
                                System.out.println(message);
                            }
                        }

                        continue;
                    }

                    if (dmMenu == 3) {  
                        System.out.println("Who are you sending a DM to?");
                        String dmSendTargetName = scan.nextLine();

                        //to server. VALID
                        out.write(dmSendTargetName);
                        out.println();
                        out.flush();

                        String response = in.readLine();
                        System.out.println(response);

                        if (response.equals("Enter Message:")) {
                            String message = scan.nextLine();

                            //to server. VALID
                            out.write(message);
                            out.println();
                            out.flush();

                            String response2 = in.readLine();
                            System.out.println(response2);
                        }
                        continue;
                    }

                    if (dmMenu == 4) {
                        System.out.println("Who are you removing a DM to?");
                        String dmRemoveTargetName = scan.nextLine();

                        //to server. VALID
                        out.write(dmRemoveTargetName);
                        out.println();
                        out.flush();

                        String response = in.readLine();
                        System.out.println(response);

                        if (response.equals("Enter Index of Message to remove:")) {
                            String indexRemove = scan.nextLine();

                            //to server. VALID
                            out.write(indexRemove);
                            out.println();
                            out.flush();

                            String response2 = in.readLine();
                            System.out.println(response2);
                        }
                        continue;
                    }

                    //end of DM Menu
                }

                if (menu == 7) {
                    System.out.println("Bye");
                    break;
                }
            }


        } catch (UnknownHostException e) {
            System.err.println("Unknown host " + SERVER_IP);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("Error connecting to the server");
            e.printStackTrace();
        }
    }
}
