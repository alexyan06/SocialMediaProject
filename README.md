# SocialMediaProject

# Classes:
Account: This `Account` class represents a user account with associated features like friends and blocked lists. It allows the management of account information such as the user's name, username, password, and the visibility of their profile (whether it's set to "friends only"). The class provides methods to add and remove friends and blocked accounts, ensuring the integrity of the lists by preventing duplicates and allowing mutual updates. Through constructors, it initializes the account from a string of data or with specific lists of friends and blocked accounts. The `toString()` method outputs a detailed string representation of the account, including the user's information and lists of friends and blocked accounts. This class implements basic functionality for managing user relationships in a social or network application.

AccountInterface: The `AccountInterface` defines a contract for managing user account functionalities in a system, such as setting and getting basic account details like the name, username, and password. It includes methods for managing the account's friend and blocked lists, allowing the addition and removal of friends and blocked accounts. It also contains a method to toggle and retrieve the visibility setting for the account (i.e., whether it is visible to friends only). Additionally, the interface requires methods for comparing accounts (`equals()`) and representing an account as a string (`toString()`). Any class implementing this interface, such as the `Account` class, must provide concrete implementations for these methods.

AccountTestCase: The provided `AccountTestCase` class is a JUnit test suite designed to verify the functionality of the `Account` class and its methods. It includes a range of tests for various features such as account creation, adding/removing friends and blocked users, string representation, and getter/setter methods. Each test is wrapped with annotations like `@Test`, ensuring the tests are run individually. The test suite uses the `@Before` and `@After` annotations to handle setup and cleanup, specifically for redirecting the system’s input/output streams during tests. It checks that the `Account` class properly initializes its fields, manages relationships (friends and blocked lists), and accurately updates and compares accounts. It also verifies the string representation of the account and confirms the correctness of `equals()` and other account-related methods. If a test fails, it’s likely due to a mismatch between the expected and actual results, such as when adding/removing friends or blocked users fails to update the lists correctly.

SocialMediaDatabase: The `SocialMediaDatabase` class manages user accounts and direct messages (DMs) for a social media application. It provides functionality to create and manage accounts, log in users, send/receive direct messages, and store this data in text files. Key features include reading and writing account information, creating new DM conversations, validating friendships and blocklists when sending messages, and allowing users to remove their own messages from conversations. The database ensures proper handling of account and message data, with methods to update and retrieve account and DM details. This class is designed to simulate basic social media interactions with file-based data storage.

SocialMediaInterface: The `SocialMediaInterface` defines the methods required for managing a social media database. It includes operations for reading and writing account and direct message (DM) data, handling user login, creating and removing DMs, and managing account information. The interface ensures that accounts can be added, retrieved, and modified, and that messages can be exchanged, added, or deleted between users based on their relationships (e.g., friends, blocked users). This interface lays the foundation for interacting with user data, making it essential for any implementation of a social media platform’s backend.

SocialMediaTestCase: This repository contains a suite of unit tests for the `SocialMediaDatabase` class, implemented using JUnit 4.x, to ensure the proper functionality of various social media operations. The tests cover key features such as account management (creation, login, and modifications), direct message handling (adding/removing messages, creating message files, and reading/writing messages), and file persistence (handling account and direct message data). Each test method checks a specific functionality, verifying that the system correctly processes data, manages files, and interacts with users. The tests ensure the reliability and correctness of the social media database by validating the expected behavior of its components.
