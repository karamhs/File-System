package project;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class FilTest {
    private User user1;
    private User user2;
    private Group group1;
    private Fil fil1;

    @BeforeEach
    void setup(){
        Fil.clean();
        user1 = new User("User 1", "User2004!!", "user1@gmail.com", true);
        user2 = new User("User 2", "User2002!!", "user2@gmail.com",false);
        group1 = new Group("Group 1");
        fil1 = new Fil("File 1", user1, "LETS GOO!!");

    }


    @Test
    @DisplayName("Test permissions")
    void testPermissions(){
        assertEquals(fil1.getOwner(), user1);

        assertThrows(IllegalArgumentException.class, () -> {
            fil1.getContent(user2);
        });

        assertDoesNotThrow(() -> {
            fil1.getContent(user1);
            fil1.setContent(user1, "AA BB CC");
        });

        fil1.addAccess(user2);
        fil1.addWrite(user2);

        assertDoesNotThrow(() -> {
            fil1.getContent(user2);
            fil1.setContent(user2, "CC DD EE");
        });

        fil1.removeAccess(user2);

        assertThrows(IllegalArgumentException.class, () -> {
            fil1.getContent(user2);
        });

        fil1.removeWrite(user2);

        group1.addMember(user1, user2);

        fil1.addAccess(group1);
        fil1.addWrite(group1);

        assertDoesNotThrow(() -> {
            fil1.getContent(user2);
            fil1.setContent(user2, "FF FFF FF");
        });
    }

    @Test
    @DisplayName("Test Saving/Loading")
    void testSaveLoad(){
        //Reversed tests, but it works anyways :)
        group1.addMember(user1, user2);
        
        Fil.save("SaveFile.txt");
        Fil.load("SaveFile.txt");

        assertEquals(Group.getGroups().size(), 2);
        assertEquals(User.getUsers().size(), 2);

        user2 = (User) User.getUsers().get(1);
        assertEquals(user2.getAdmin(), false);
        assertEquals(user2.getName(), "User 2");
        assertEquals(user2.getId(), 3);
        assertEquals(user2.getGroup().getName(), "Group 1");

        fil1 = (Fil) Fil.getFiles().get(0);
        assertEquals(fil1.getName(), "File 1");
        assertEquals(fil1.getId(), 6);
        assertEquals(fil1.getContent(user1), "LETS GOO!!");

    }
}