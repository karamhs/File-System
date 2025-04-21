package project;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class GroupTest {
    private Group group1;
    private Group group2;
    private User admin;
    private User user;


    @BeforeEach
    void setup(){
        group1 = new Group("Group 1");
        group2 = new Group("Group 2");
        admin = new User("User 1", "uSer1200!4", "user1@gmail.com", true);
        user = new User("User 2", "uUser2004!1", "user2@gmail.com", false);
    }

    @Test
    @DisplayName("Test Constructor")
    void testGroup(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Group("Group 1");
        });

        assertEquals(2, group2.getId());
    }

    private void checkInvalidAdd(Group group, User user1, User user2){
        assertThrows(IllegalArgumentException.class, () -> {
            group.addMember(admin, user);
        });
    }

    @Test
    @DisplayName("Test Add Memebers To the club")
    void testAddMemember(){
        checkInvalidAdd(group1, null, admin);
        checkInvalidAdd(group2, user, admin);
        checkInvalidAdd(group1, user, user);

        assertDoesNotThrow(() -> {
            group1.addMember(admin, admin);
            group2.addMember(admin, user);
        });

        checkInvalidAdd(group2, admin, user);
        assertEquals(group1, admin.getGroup());
        assertEquals(group2, user.getGroup());
    
    }

    private void checkInvalidRemove(Group group, User admin, User user){
        assertThrows(IllegalArgumentException.class, () -> {
            group.removeMember(admin, user);
        });
    }

    @Test
    @DisplayName("Test Remove Member")
    void testremoveMemeber(){
        checkInvalidRemove(group1, admin, user);
        checkInvalidRemove(group1, user, admin);
        checkInvalidRemove(group2, admin, user);

        group1.addMember(admin, user);

        assertDoesNotThrow(() -> {
            group1.removeMember(admin, user);
        });

        assertEquals(null, user.getGroup());

    }
}
