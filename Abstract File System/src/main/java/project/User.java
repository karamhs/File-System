package project;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User extends Entity{
    private int passwd;
    private boolean isAdmin = false;
    private String email;
    private Group group;
    private static List<Entity> users = new ArrayList<>();

    private static User defaultAdmin = new User("admin", "Admin2004!!", "admin@gmail.com", true);

    public User(String name, String passwd, String email, boolean isAdmin){
        super(name, users);
        setPasswd(passwd);
        setEmail(email);
        this.isAdmin = isAdmin;
        users.add(this);
    }

    public User(String name, String id, String hashedPasswd, String email, String isAdmin, String groupId){
        super(name, users);

        int parsedid = Integer.parseInt(id);
        int parsedhashedPasswd = Integer.parseInt(hashedPasswd);

        setId(parsedid);
        passwd = parsedhashedPasswd;
        setEmail(email); 

        if(isAdmin.contains("true")){
            this.isAdmin = true;
        }

        if(!groupId.equals("null")){
            int parsedGroupID = Integer.parseInt(groupId);
            for(Entity groupi : Group.getGroups()){
                if(groupi.getId() == parsedGroupID){
                    Group group = (Group) groupi;
                    defaultAdmin.addToGroup(this, group);
                }
            }
        }

        users.add(this);

    }
    
    private void checkPasswd(String passwd){
        if(passwd.length() < 8){
            throw new IllegalArgumentException("Password length should more than 7 characters");
        }

        if(passwd.indexOf(this.getName()) != -1){
            throw new IllegalArgumentException("Password cannot contain your name");
        }

        Pattern pattern = Pattern.compile("^(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9]).+$");
        Matcher matcher = pattern.matcher(passwd);
        
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Password must contain at least one capital letter, a number, and a special character.");
        }
    }

    public int hashPasswd(String passwd){
        int hashed = 67;
        for(int i = 0; i < passwd.length(); i++){
            hashed *= 37 + passwd.charAt(i) ^ 0x22; //Prime numbers..
        }

        return hashed;
    }

    private void checkEmail(String email){
        if(email.length() == 0){
            throw new IllegalArgumentException("Email length = 0");
        }

        Pattern pattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        
        if(!matcher.find()){
            throw new IllegalArgumentException("Fake email");
        }
    }

    public void addToGroup(User user, Group group){
        if(!this.isAdmin){
            throw new IllegalStateException("Onlt admins can add members to groups");
        }

        if(user.group != null){
            throw new IllegalStateException(String.format("%s is already a member of %s", user.getName(), group));
        }

        if(group == null){
            throw new IllegalArgumentException("Group cannot be null!");
        }
        
        if(group.getMembers().contains(user)){
            throw new IllegalStateException(String.format("%s is already a memeber of this group", user.getName()));
        }

        group.addMember(this, user);
    }

    public void removeFromGroup(User user){
        if(user.group == null){
            throw new IllegalArgumentException("User is not a member of any group");
        }

        if(!this.isAdmin && this != user){
            throw new IllegalArgumentException(String.format("%s is not an Admin and cannot remove %s from %s", this.getName(), user.getName(), group.getName()));
        }

        user.group.removeMember(this, user);
        user.group = null;

    }

    public void removeUser(User user){
        if(this.isAdmin == false){
            throw new IllegalArgumentException("Only admins can remove users");
        }

        users.remove(users.indexOf(user));

        if(user.group != null){
            group.removeMember(this, user);
        }

    }
   
    // litt enkelt, ellers ville det trenges at entiteten som setter en bruker til admin, må være selv admin, og da må man lage en default admin.
    public void setAdmin(boolean s){
        isAdmin = s;
    }

    public boolean getAdmin(){
        return isAdmin;
    }

    public int getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        checkPasswd(passwd);
        this.passwd = hashPasswd(passwd);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        checkEmail(email);
        this.email = email;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group){
        this.group = group;
    }

    public static List<Entity> getUsers() {
        return users;
    }

    @Override
    public String toString() {
        String text = String.format("User,%s,%d,%d,%s,%s,", name, id, passwd, email, isAdmin);
        if(group != null){
            text += group.getId();
            return text;
        }

        text += "null";
        return text;
    
    }

    public static User getDefaultAdmin(){
        return defaultAdmin;
    }

    public static void deleteAll(){
        users = new ArrayList<>();
    }

}
