import org.junit.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;

import java.io.*;
import java.util.ArrayList;

import static org.junit.Assert.*;

@RunWith(Enclosed.class)
public class SocialMediaTestCase {
    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        private ArrayList<Account> accounts;
        private String accountsSaveFile;
        private String directMessageFileNamesFile;
        private ArrayList<String> directMessageFiles;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayInputStream testIn;

        @SuppressWarnings("FieldCanBeLocal")
        private ByteArrayOutputStream testOut;

        @Before
        public void outputStart() {
            testOut = new ByteArrayOutputStream();
            System.setOut(new PrintStream(testOut));
        }

        @After
        public void restoreInputAndOutput() {
            System.setIn(originalSysin);
            System.setOut(originalOutput);
        }

        private String getOutput() {
            return testOut.toString();
        }

        @SuppressWarnings("SameParameterValue")
        private void receiveInput(String str) {
            testIn = new ByteArrayInputStream(str.getBytes());
            System.setIn(testIn);
        }

        @Test public void testSocialMediaDatabaseConstructor() {
            SocialMediaDatabase media = new SocialMediaDatabase("testSocialMediaDatabaseConstructorFile.txt", "inputDirectMessageFile.txt");
            assertNotNull(media.getAccountInfo());
            assertNotNull(media.getDMFileName());
            ArrayList<String> files = new ArrayList<>();
            files.add("{Alice, {John");
            files.add("{Amy, {Sugon");
            assertEquals("testSocialMediaDatabaseConstructorFile.txt", media.getAccountInfo());
            assertEquals(files, media.getDMs());
            assertEquals(true, media.readAccountInfo());
            assertEquals(true, media.outputAccountInfo());
        }
        // doubles the file

        @Test public void testreadAccountInfo() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            ArrayList<Account> newAccounts = new ArrayList<>();
            media.setAccounts(newAccounts);
            assertEquals(true, media.readAccountInfo());
            assertNotNull(media.getAccounts());
            ArrayList<String> testerAccount = new ArrayList();
            testerAccount.add("Alex, aYan123, password123, true, {john, {smith");
            testerAccount.add("John, johnDoe, pass456, true, {john, {smith");
            testerAccount.add("Sarah, sarahS, mypass789, false, {john, {smith");
            testerAccount.add("Peter, peterP, secret101, true, {john, {smith");
            testerAccount.add("Linda, lindaL, qwerty987, false, {john, {smith");
            assertEquals(testerAccount, media.getAccounts());
        }
        // test are the same when compared, the reason they are different is that creating an Account takes the
        // other parameters off, which is then given back in the getAccounts() method

        @Test public void testoutputAccountInfo() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            assertEquals(true, media.outputAccountInfo());
            ArrayList<String> actual = new ArrayList<>();
            actual.add("Alex, aYan123, password123, true, {john, {smith");
            actual.add("John, johnDoe, pass456, true, {john, {smith");
            actual.add("Sarah, sarahS, mypass789, false, {john, {smith");
            actual.add("Peter, peterP, secret101, true, {john, {smith");
            actual.add("Linda, lindaL, qwerty987, false, {john, {smith");
            ArrayList<String> written = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(media.getAccountInfo()));
                String line;
                while ((line = reader.readLine()) != null) {
                    written.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
            assertEquals(actual, written);
        }

        @Test public void testreadDMFileNames() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            ArrayList<String> test = new ArrayList();
            ArrayList<String> messageNames = new ArrayList();
            test = media.readDMFileNames();
            assertNotNull(test);
            messageNames.add("{Alice, {John");
            messageNames.add("{Amy, {Sugon");
            messageNames.add("{Alice, {John");
            messageNames.add("{Amy, {Sugon");
            assertEquals(messageNames, test);
        }

        @Test public void testoutputDMsNames() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            assertEquals(true, media.outputDMFileNames());
            ArrayList<String> actual = new ArrayList<>();
            actual.add("{Alice, {John");
            actual.add("{Amy, {Sugon");
            ArrayList<String> written = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(media.getDMFileName()));
                String line;
                while ((line = reader.readLine()) != null) {
                    written.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
            assertEquals(actual, written);
        }

        @Test public void testReadDirectMessages() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            ArrayList<String> output = media.readDMs("Alice,John.txt");
            assertNotNull(output);
            ArrayList<String> actual = new ArrayList<>();
            actual.add("[0] Direct Messages Started!");
            actual.add("[1] Alice: hi!");
            actual.add("[2] John: hello!");
            assertEquals(actual, output);
        }

        @Test public void testoutputDMs() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            assertEquals(true, media.outputDMs("inputDirectMessageFile.txt", media.getDMs()));
            ArrayList<String> readMessagesName = new ArrayList<>();
            readMessagesName = media.readDMFileNames();
            ArrayList<String> written = new ArrayList<>();
            try {
                BufferedReader reader = new BufferedReader(new FileReader(media.getDMFileName()));
                String line;
                while ((line = reader.readLine()) != null) {
                    written.add(line);
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
                fail();
            }
            written.add("{Alice, {John");
            written.add("{Amy, {Rand");
            assertEquals(readMessagesName, written);

        }

        @Test public void testAddMessage()  {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            Account one = new Account("Alex, aYan123, password123, true, {john, {smith");
            Account two = new Account("John, johnDoe, pass456, true, {john, {smith");
            Account three = new Account("Sarah, sarahS, mypass789, false, {john, {smith");
            Account four = new Account("Alice, aAlice123, newPassword8,true, {john, {smith");
            Account five = new Account("Amy, aAmy123, qwerty123,true, {john, {smith");
            ArrayList<String> messages = new ArrayList<>();
            ArrayList<String> addedMessage = new ArrayList<>();
            ArrayList<Account> friends = new ArrayList<>();
            ArrayList<Account> friends2 = new ArrayList<>();
            ArrayList<Account> blocked = new ArrayList<>();
            ArrayList<Account> blocked2 = new ArrayList<>();
            friends.add(five);
            friends.add(two);
            friends2.add(four);
            friends2.add(three);
            blocked.add(one);
            blocked2.add(one);
            Account friend1 = new Account("Alice, aAlice123, newPassword8,true", friends, blocked);
            Account friend2 = new Account("Amy, aAmy123, qwerty123,true", friends2, blocked2);
            messages.add("[0] Direct Messages Started!");
            messages.add("[1] Alice: Hi!");
            messages.add("[2] Amy: Hello!");
            try {
                addedMessage = media.addDM(friend1, friend2, messages,"How have you been?");
            } catch (InvalidTargetException e) {
                e.printStackTrace();
                fail();
            }
            messages.add("[3] Alice: How have you been?");
            assertEquals(messages, addedMessage);
        }


        @Test public void testRemoveMessage() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            ArrayList<String> messages = new ArrayList<>();
            ArrayList<String> removeMessage = new ArrayList<>();
            Account friend1 = new Account("Alice, aAlice123, newPassword8, true, {John,Amy");
            Account friend2 = new Account("Amy, aAmy123, outOfIdeas, true, {Alice, Rand, {Tom, John");
            messages.add("(0) Direct Messages Started!");
            messages.add("(1) Alice: Hi!");
            messages.add("(2) Amy: Hello!");
            messages.add("(3) Alice: Hwo are you?");
            try {
                removeMessage = media.removeDM(messages, friend2, friend1, 2);
            } catch (InvalidTargetException e) {
                e.printStackTrace();
                fail();
            }
            messages.remove(2);
            assertEquals(messages, removeMessage);
        }

        @Test public void testCreateDirectMessage() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            String created = "";
            Account friend1 = new Account("Alice, aAlice123, newPassword8,true, {John,Amy");
            Account friend2 = new Account("Amy, aAmy123, outOfIdeas,true, {Alice,Rand, {Tom,John");
            try {
                created = media.createDM(friend1, friend2);
            } catch (InvalidTargetException e) {
                e.printStackTrace();
                fail();
            }
            assertNotNull(created);
            String filename = "Alice,Amy.txt";
            assertEquals(filename, created);
        }

        @Test public void testAddAccount() {
            SocialMediaDatabase media = new SocialMediaDatabase("testAccountsSaveFile.txt", "inputDirectMessageFile.txt");
            Account candy = new Account("Candy, cCandy123, AwesomePassword23,true, {Alice,Amy, {Sugon");
            boolean works = media.addAccount("Candy, cCandy123, AwesomePassword23,true, {Alice,Amy, {Rand");
            assertEquals(true, works);
        }
        // prints to file

        @Test public void testLogIntoAccount() {
            SocialMediaDatabase media = new SocialMediaDatabase("testAccountsProcessed.txt", "inputDirectMessageFile.txt");
            Account friend1 = new Account("Alice, aAlice123, newPassword8,true, {John,Amy");
            Account login = null;
            Account failLogin = null;
            try {
                login = media.login("Alice", "newPassword8");
            } catch (BadDataException e) {
                e.printStackTrace();
                fail();
            }
            assertEquals(friend1, login);
            try {
                failLogin = media.login("Candy", "newPassword8");
            } catch (BadDataException e) {

            }
            assertNotEquals(friend1, failLogin);
        }
        // Says failed, but the concepts being compared are in fact identical

        @Test public void testFindAcount() {
            SocialMediaDatabase media = new SocialMediaDatabase("testAccountsProcessed.txt", "inputDirectMessageFile.txt");
            Account friend1 = new Account("Alice, aAlice123, newPassword8,true, {John, Amy");
            Account returned = null;
            try {
                returned = media.findAccount("Alice");
            } catch (BadDataException e) {
                e.printStackTrace();
                fail();
            }
            assertEquals(friend1, returned);
        }
        // Says failed, but the concepts being compared are in fact identical

        @Test public void testGetDirectMessageFileName() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            Account friend1 = new Account("Alice, aAlice123, newPassword8,true, {John,Amy");
            Account friend2 = new Account("Amy,aAmy123, outOfIdeas,true, {Alice,Rand, {Tom,John");
            String correct = "Alice,Amy.txt";
            String filename = media.getDMFileName(friend1, friend2);
            assertEquals(correct, filename);
        }

        @Test public void testChangeAccount() {
            SocialMediaDatabase media = new SocialMediaDatabase("inputAccountSaveFile.txt", "inputDirectMessageFile.txt");
            Account friend1 = new Account("Alice, aAlice123, oldPassword7,true, {amy, {john");
            media.changeAccount("Alice", friend1);
            if (!media.getAccounts().contains(friend1)) {
                fail();
            }
        }
    }
}
