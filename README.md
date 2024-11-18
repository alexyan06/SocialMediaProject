# SocialMediaProject

# Classes:
Account: This `Account` class represents a user account with associated features like friends and blocked lists. It allows the management of account information such as the user's name, username, password, and the visibility of their profile (whether it's set to "friends only"). The class provides methods to add and remove friends and blocked accounts, ensuring the integrity of the lists by preventing duplicates and allowing mutual updates. Through constructors, it initializes the account from a string of data or with specific lists of friends and blocked accounts. The `toString()` method outputs a detailed string representation of the account, including the user's information and lists of friends and blocked accounts. This class implements basic functionality for managing user relationships in a social or network application.

AccountInterface: The `AccountInterface` defines a contract for managing user account functionalities in a system, such as setting and getting basic account details like the name, username, and password. It includes methods for managing the account's friend and blocked lists, allowing the addition and removal of friends and blocked accounts. It also contains a method to toggle and retrieve the visibility setting for the account (i.e., whether it is visible to friends only). Additionally, the interface requires methods for comparing accounts (`equals()`) and representing an account as a string (`toString()`). Any class implementing this interface, such as the `Account` class, must provide concrete implementations for these methods.

AccountTestCase: The provided `AccountTestCase` class is a JUnit test suite designed to verify the functionality of the `Account` class and its methods. It includes a range of tests for various features such as account creation, adding/removing friends and blocked users, string representation, and getter/setter methods. Each test is wrapped with annotations like `@Test`, ensuring the tests are run individually. The test suite uses the `@Before` and `@After` annotations to handle setup and cleanup, specifically for redirecting the system’s input/output streams during tests. It checks that the `Account` class properly initializes its fields, manages relationships (friends and blocked lists), and accurately updates and compares accounts. It also verifies the string representation of the account and confirms the correctness of `equals()` and other account-related methods. If a test fails, it’s likely due to a mismatch between the expected and actual results, such as when adding/removing friends or blocked users fails to update the lists correctly.

SocialMediaDatabase: The `SocialMediaDatabase` class manages user accounts and direct messages (DMs) for a social media application. It provides functionality to create and manage accounts, log in users, send/receive direct messages, and store this data in text files. Key features include reading and writing account information, creating new DM conversations, validating friendships and blocklists when sending messages, and allowing users to remove their own messages from conversations. The database ensures proper handling of account and message data, with methods to update and retrieve account and DM details. This class is designed to simulate basic social media interactions with file-based data storage.


SocialMediaInterface: The `SocialMediaInterface` defines the methods required for managing a social media database. It includes operations for reading and writing account and direct message (DM) data, handling user login, creating and removing DMs, and managing account information. The interface ensures that accounts can be added, retrieved, and modified, and that messages can be exchanged, added, or deleted between users based on their relationships (e.g., friends, blocked users). This interface lays the foundation for interacting with user data, making it essential for any implementation of a social media platform’s backend.

SocialMediaTestCase: This repository contains a suite of unit tests for the `SocialMediaDatabase` class, implemented using JUnit 4.x, to ensure the proper functionality of various social media operations. The tests cover key features such as account management (creation, login, and modifications), direct message handling (adding/removing messages, creating message files, and reading/writing messages), and file persistence (handling account and direct message data). Each test method checks a specific functionality, verifying that the system correctly processes data, manages files, and interacts with users. The tests ensure the reliability and correctness of the social media database by validating the expected behavior of its components.


TESTING THE CLIENT/SERVER

Start the server first, then the client. Once connected, the client should output a message saying “Connected to server...” The client will ask if you want to create an account or log in. Create an account by entering 2. Enter a name for username and a password for password, and true for friends-only messages.  

For log-in, enter 1. Then enter your username and then your password when asked. If successful, you will see a message saying, “Logged in!” You will then see a menu asking you what you want to do. If your username or password are incorrect, you will see a message saying, “Username or password is wrong.” It will prompt you to either log-in again or create an account. Once logged in, there is a menu that pops up with seven options.  

Server: This server application allows multiple clients to connect and interact with a social media platform. Clients can create accounts, add friends, manage blocked users, create and send direct messages (DMs), and read existing DMs. The server utilizes a thread pool to handle concurrent client connections efficiently. It also uses a SocialMediaDatabase class to manage user accounts and direct message data.

Client: This code implements a client-side application for a social media platform. It establishes a connection to a server, allows users to log in or create accounts, and provides a menu of actions like adding/removing friends, managing blocked users, and interacting with direct messages. The client sends user input to the server and displays the server's responses, providing a basic user interface for the social media platform.

Menu: 

1. Change friends only. It will ask “Friends Only for messages (true/false). Enter true if you only want your friends to see the messages or false if you don’t. Follow the format that is provided. It will show a message saying, “Friends Only changed.” 

2. Add a friend. It will prompt you to enter who you’re adding as a friend. Enter their username. If they’re already your friend, then you will see a message on the server and the client. If someone isn’t your friend and isn’t blocked, your friends list will be updated. If they are blocked, and you add them as a friend, they will become your friend and will no longer be blocked.  

3. Remove a friend. It will prompt you to remove a friend. If you type in their username correctly and they are your friend, you will see a message confirming that they have been removed as a friend. Otherwise, you will see a message indicating failure.  

4. Block a user. It will prompt you to type in a username to block. If successful, you will see a message indicating success. If the user is already blocked or if their username is typed incorrectly, you will see a message indicating failure. If you block a friend, they will be removed from the friends list and added to blocked.  

5. Remove a blocked user. It will prompt you to block a user. If you type in their username correctly and they are part of your blocked list, you will see a message confirming that they have been removed as blocked. Otherwise, you will see a message indicating failure. 

6. Access DMs. You will see another menu.  

  1. Starting a DM. It will prompt you to enter the username of who you’re starting a DM with. If successful, it will write “Success!” in the client, and a new .txt file in the format of (User,Getter.txt) will be made with the first line being “[0] start DMs” Else, it will say failure 

  2. Reading a DM. It will prompt you to enter the username of who you want to read a DM with. If that DM txt file doesn't exist, it will say failure. If it exists, it will print out the current messages within that DM txt file. 

  3. Send a DM. It will prompt you to enter the username of who you want to enter a message to, if you can’t send them a DM, it will read failure, otherwise it will ask you to enter a message. After entering, it will be input into that txt file previously made through option 1. If you are blocked by someone, you will not be able to message them. 

  4. Delete DM. It will prompt you to enter the name of the user you want to delete the DM with. Next, it will prompt you to enter the index of the DM (starting from 1) that you want to delete. If successful, it will indicate that it was successful.  

7. End (bye)
