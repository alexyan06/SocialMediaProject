import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import java.io.*;
import java.util.ArrayList;
import static org.junit.Assert.*;
/**
 * The Cube class represents a cube in 3D space, composed of six faces.
 *
 * @author Alex Yan yan517
 * @version 1.0 (2024-10-22)
 */
@RunWith(Enclosed.class)
public class AccountTestCase {
    public static class TestCase {
        private final PrintStream originalOutput = System.out;
        private final InputStream originalSysin = System.in;

        private String name;
        private String userName;
        private String password;
        private ArrayList<Account> friends;
        private ArrayList<Account> blocked;

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

        @Test public void testAccountConstructor() {
            Account test = new Account("Alex, aAlex123, 123password, true");

            assertNotNull(test.getName());
            assertNotNull(test.getPassword());
            assertEquals("123password", test.getPassword());
            assertEquals("Alex", test.getName());
        }

        @Test public void testAccountConstructorWithParameters() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Joey, rJoey123, jJoeyPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            ArrayList<Account> friendly = new ArrayList<Account>();
            friendly.add(friend1);
            friendly.add(friend2);
            ArrayList<Account> blocker = new ArrayList<Account>();
            blocker.add(block1);

            Account tester = new Account("Alex, aAlex123, 123password, true", friendly, blocker);

            assertNotNull(tester.getFriends());
            assertNotNull(tester.getBlocked());
            assertNotNull(tester.getPassword());
            assertNotNull(tester.getName());
            assertEquals("Alex", tester.getName());
            assertEquals("123password", tester.getPassword());
            assertTrue(block1.getFriendsOnly());

            assertEquals(friendly, tester.getFriends());
            assertEquals(blocker, tester.getBlocked());
        }

        @Test public void testAddFriend() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Joey, rJoey123, jJoeyPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            ArrayList<Account> friendly = new ArrayList<Account>();
            friendly.add(friend1);
            friendly.add(friend2);
            ArrayList<Account> blocker = new ArrayList<Account>();
            blocker.add(block1);

            Account test = new Account("Alex, aAlex123, 123password, true", friendly, blocker);
            Account testerFriend = new Account("Candy, cCandy123, AwesomePassword23, true");
            test.addFriend(testerFriend);
            ArrayList<Account> testFriends = new ArrayList<>();
            testFriends.add(friend1);
            testFriends.add(friend2);
            testFriends.add(testerFriend);

            assertEquals(testFriends, test.getFriends());
            Account testBlocked = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");
            boolean block = test.addFriend(testBlocked);
            assertEquals(true, block);
            assertTrue(block1.getFriendsOnly());
        }
        // Says failed, but the concepts being compared are in fact identical


        @Test public void testAddBlocked() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Joey, rJoey123, jJoeyPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            ArrayList<Account> friendly = new ArrayList<Account>();
            friendly.add(friend1);
            friendly.add(friend2);
            ArrayList<Account> blocker = new ArrayList<Account>();
            blocker.add(block1);

            Account test = new Account("Alex, aAlex123, 123password, true", friendly, blocker);
            Account testBlocked = new Account("Joey, rJoey123, jJoeyPassword, true");

            test.addBlocked(testBlocked);
            ArrayList<Account> testerBlocked = new ArrayList();
            testerBlocked.add(block1);
            testerBlocked.add(testBlocked);

            assertEquals(testerBlocked, test.getBlocked());
            ArrayList<Account> testFriends = new ArrayList<>();
            testFriends.add(friend1);
            testFriends.add(friend2);

            assertEquals(testFriends, test.getFriends());
            Account alreadyBlock = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");
            boolean bk = test.addBlocked(alreadyBlock);
            assertEquals(true, test.addBlocked(testBlocked));
            assertEquals(true, bk);
            assertTrue(block1.getFriendsOnly());

        }

        @Test public void testRemoveFriend() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Joey, rJoey123, jJoeyPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            ArrayList<Account> friendly = new ArrayList<Account>();
            friendly.add(friend1);
            friendly.add(friend2);
            ArrayList<Account> blocker = new ArrayList<Account>();
            blocker.add(block1);

            Account test = new Account("Alex, aAlex123, 123password, true", friendly, blocker);

            test.removeFriend(friend2);
            ArrayList<Account> newFriends = new ArrayList<>();
            newFriends.add(friend1);

            assertEquals(false, test.removeFriend(friend2));
            assertEquals(newFriends, test.getFriends());
            assertTrue(block1.getFriendsOnly());
        }
        // Says failed, but the concepts being compared are in fact identical

        @Test public void testRemoveBlocked() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Joey, rJoey123, jJoeyPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            ArrayList<Account> friendly = new ArrayList<Account>();
            friendly.add(friend1);
            friendly.add(friend2);
            ArrayList<Account> blocker = new ArrayList<Account>();
            blocker.add(block1);

            Account test = new Account("Alex, aAlex123, 123password, true", friendly, blocker);
            Account blockAccount = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            test.removeBlocked(blockAccount);
            ArrayList<Account> block = new ArrayList<>();
            block.add(blockAccount);

            assertEquals(false, test.removeBlocked(blockAccount));
            assertTrue(block1.getFriendsOnly());
        }
        // Says failed, but the concepts being compared are in fact identical


        @Test public void testAccountToString() {
            Account test = new Account("Alex, aAlex123, 123password, true");
            String written = test.toString();

            assertEquals("Alex, aAlex123, 123password, true", written);
        }

        @Test public void testGetMethods() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Joey, rJoey123, jJoeyPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            ArrayList<Account> friendly = new ArrayList<Account>();
            friendly.add(friend1);
            friendly.add(friend2);
            ArrayList<Account> blocker = new ArrayList<Account>();
            blocker.add(block1);

            Account test = new Account("Alex, aAlex123, 123password, true", friendly, blocker);

            String written = test.toString();

            ArrayList<Account> testFriends = new ArrayList<>();
            testFriends.add(friend1);
            testFriends.add(friend2);
            ArrayList<Account> testBlocked = new ArrayList<>();
            testBlocked.add(block1);

            assertEquals(testBlocked, test.getBlocked());
            assertEquals(testFriends, test.getFriends());
            assertEquals("Alex", test.getName());
            assertEquals("123password", test.getPassword());
            assertEquals("Alex, aAlex123, 123password, true, <Naomi, Joey, <Rebecca", written);
        }

        @Test public void testSetMethods() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Joey, rJoey123, jJoeyPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            ArrayList<Account> testFriends = new ArrayList<>();
            testFriends.add(friend1);
            ArrayList<Account> testBlocked = new ArrayList<>();
            testBlocked.add(block1);
            friend1.setBlocked(testBlocked);
            friend1.setFriends(testFriends);
            friend1.setName("Alex");
            friend1.setPassword("aAlex123");

            assertEquals(testBlocked, friend1.getBlocked());
            assertEquals(testFriends, friend1.getFriends());
            assertEquals("Alex", friend1.getName());
            assertEquals("aAlex123", friend1.getPassword());
        }

        @Test public void testEquals() {
            Account friend1 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account friend2 = new Account("Naomi, aNaomi123, nNaomiPassword, true");
            Account block1 = new Account("Rebecca, aRebecca123, rRebeccaPassword, true");

            assertEquals(true, friend1.equals(friend2));
            assertEquals(false, friend1.equals(block1));

        }
    }
}
