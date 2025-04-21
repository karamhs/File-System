package project;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Group extends Entity implements Iterable<Entity>{
    private List<User> members = new ArrayList<>();
    private static List<Entity> groups = new ArrayList<>();

    public Group(String name){
        super(name, groups);
        groups.add(this);
    }

    public Group(String name, String id){
        super(name, groups);
        int parsedId = Integer.parseInt(id);
        setId(parsedId);
        groups.add(this);
    }

    public Iterator<Entity> iterator(){
        return new groupIterator(members);
    }

    public void removeMember(User actor, User user){
        if(user == null || actor == null){
            throw new IllegalArgumentException("User is not defined");
        }

        if(!actor.getAdmin() && actor != user){
            throw new IllegalStateException("Only admins can remove users from groups");
        }

        if(!members.contains(user)){
            throw new IllegalStateException(String.format("%s is not a memeber of this group", user.getName()));
        }

        for(int i = 0; i < members.size(); i++){
            if(members.get(i) == user){
                members.remove(i);
                user.setGroup(null);
                return;
            }
        }

    }

    public void addMember(User actor, User user){
        if(actor == null || user == null){
            throw new IllegalArgumentException("None of the arguments can be null");
        }

        if(!actor.getAdmin()){
            throw new IllegalArgumentException("Actor is not an admin");
        }

        if(members.contains(user)){
            throw new IllegalArgumentException("User is already a member of this group");
        }

        members.add(user);
        user.setGroup(this);
        
    }

    public List<User> getMembers() {
        return members;
    }

    public static List<Entity> getGroups() {
        return groups;
    }

    public static void deleteAll(){
        groups = new ArrayList<>();
    }

    public void removeGroup(User actor){
        if(!actor.getAdmin()){
            throw new IllegalArgumentException(String.format("%s is not an admin", actor.getName()));
        }

        for(User user : members){
            user.setGroup(null);
        }

        groups.remove(groups.indexOf(this));
    }

    @Override
    public String toString() {
        return String.format("Group,%s,%d", name, id);
    }

}
