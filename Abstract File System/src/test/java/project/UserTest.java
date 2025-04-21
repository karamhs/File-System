package project;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class UserTest {
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void setup(){
        user1 = new User("User 1", "User12004!", "user1@gmail.com", false);
        user2 = new User("User 2", "User22004!", "user2@gmail.no", false);
        user3 = new User("User 3", "User32004!", "user3@cisco.org", false);
    }

    @Test
    @DisplayName("Test Constructor")
    public void testConstructor() {
        assertDoesNotThrow(() -> {
            User user4 = new User("User 4" ,"User2004!", "user1@gmail.com", false);
            assertNull(user4.getGroup());
            assertEquals(4, user4.getId());
        });
    }

    private void checkInvalidName(String name){
        assertThrows(IllegalArgumentException.class, () -> {
            user1.setName(name, User.getUsers());
        });
    }

    @Test
    @DisplayName("Test Name")
    public void testSetName(){
        checkInvalidName("!231?_*");
        checkInvalidName("2User");
        checkInvalidName("_TT_");
        checkInvalidName(" USer 1");
        checkInvalidName("User 2");
        checkInvalidName("User 3");
        checkInvalidName("");
        checkInvalidName("User1!");
        checkInvalidName("User ");
        checkInvalidName("User 3");
        checkInvalidName("IASDas     1212");
        checkInvalidName("User 2");
    }

    private void checkInvalidEmail(String email){
        assertThrows(IllegalArgumentException.class, () -> {
            user1.setEmail(email);
        });
    }

    private void checkValidEmail(String email){
        assertDoesNotThrow(() -> {
            user2.setEmail(email);
        });
    }

    @Test
    @DisplayName("Test Email")
    public void testsetEmail(){
        checkInvalidEmail("NOWAY");
        checkInvalidEmail("Letsgo@sad!.com");
        checkInvalidEmail("Yes@");
        checkInvalidEmail("No@gmail.com!");
        checkInvalidEmail("gmail.com");
        checkInvalidEmail("@matematikk.net");
        checkInvalidEmail(".no");
        checkInvalidEmail("");
        checkInvalidEmail("asdsa!.com@gmail");
        checkInvalidEmail("@gmail@.comASd@gmail.no");      
        checkInvalidEmail("user 1@gmail.com");
        checkInvalidEmail("user1@ gmail.com");      
        checkInvalidEmail("user1@gmail. com");
        checkInvalidEmail(" user1  @gmail . com");      
  
        checkValidEmail("iser1@gmail.com");
        checkValidEmail("user1@cisco.org");
        checkValidEmail("user1@noway1.co");
        checkValidEmail("usane@bruh2.ukj");

    }

    private void checkInvalidPasswd(String passwd){
        assertThrows(IllegalArgumentException.class, () -> {
            user3.setPasswd(passwd);
        });
    }

    private void checkValidPasswd(String passwd){
        assertDoesNotThrow(() -> {
            user3.setPasswd(passwd);
        });
    }

    @Test
    @DisplayName("Test Password")
    public void testPasswd(){
        checkInvalidPasswd("2004user1");
        checkInvalidPasswd("User1!");
        checkInvalidPasswd("12345678");
        checkInvalidPasswd("User2004");
        checkInvalidPasswd("ACSDWDSES");
        checkInvalidPasswd("!!!!!!!!");

        //Could use doesNotThrow on all of them straight..
        checkValidPasswd("Abcd200!");
        checkValidPasswd("2004Us!!@");
        checkValidPasswd("_213Ac!!#");
        checkValidPasswd("1ACSAWE!@_");
    }

    @Test
    @DisplayName("Test Add User to Group")
    public void testaddToGroup(){
        Group group1 = new Group("Group 1");
        Group group2 = new Group("Group 2");
        Group group3 = null;


        assertThrows(IllegalArgumentException.class, () -> {
            user1.addToGroup(user1, group1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user1.addToGroup(user2, group1);            
        });

        user1.setAdmin(true);

        assertDoesNotThrow(() -> {
            user1.addToGroup(user1, group1);
            user1.addToGroup(user2, group1);            
        });

        List<User> expected = new ArrayList<>();
        expected.add(user1);
        expected.add(user2);
        assertEquals(expected, group1.getMembers());

        assertThrows(IllegalArgumentException.class, () -> {
            user1.addToGroup(user1, group2);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user1.addToGroup(user2, group1);       
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user1.addToGroup(user3, group3);     
        });
    
    }

    @Test
    @DisplayName("Remove From Group")
    public void testremoveFromGroup(){
        Group group1 = new Group("Group 1");
        Group group2 = new Group("Group 2");

        user1.setAdmin(true);
        user1.addToGroup(user3, group1);
        user1.setAdmin(false);

        assertThrows(IllegalArgumentException.class, () -> {
            user1.removeFromGroup(user1);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            user1.removeFromGroup(user2);
        });

        user1.setAdmin(true);
        user1.addToGroup(user1, group1);
        user1.addToGroup(user2, group2);

        assertEquals(group2, user2.getGroup());

        assertDoesNotThrow(() -> {
            user1.removeFromGroup(user1);
            user1.removeFromGroup(user2);        
        });

    }
}