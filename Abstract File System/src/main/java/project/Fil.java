package project;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator;


public class Fil extends Entity implements Iterable<Entity>{
    private User owner;
    private List<Entity> access = new ArrayList<>();
    private List<Entity> write = new ArrayList<>();
    private String content;

    private static List<Entity> files = new ArrayList<>();

    public Fil(String name, User owner, String content){
        super(name, files);
        setOwner(owner);
        this.owner = owner;
        this.content = content;

        files.add(this);
    }

    public Fil(String name, String id, String ownerId, String[] accessList, String[] writeList, String content){
        super(name, files);
        int parsedOwnerId = Integer.parseInt(ownerId);
        setOwner(parsedOwnerId);
        setAccessList(accessList);
        setWriteList(writeList);
        setContent(owner, content);
        this.id = Integer.parseInt(id);
        files.add(this);
    }

    public Iterator<Entity> iterator(){
        return new filIterator(access, write);
    }

    private Entity findEntity(int id){
        for(Entity entity : User.getUsers()){
            if(entity.id == id){
                return entity;
            }
        }

        for(Entity entity : Group.getGroups()){
            if(entity.id == id){
                return entity;
            }
        }

        return null;
    }

    public void setAccessList(String[] accessList){
        Entity entity;
        int id;

        if(accessList.length < 2){
            return;
        }

        for(int i = 1; i < accessList.length; i++){
            id = Integer.parseInt(accessList[i]);
            entity = findEntity(id);
            if(entity == null){
                throw new IllegalStateException(String.format("Malformed save file: No entity with id=%d", id));
            }

            addAccess(entity);
        }
    }

    public void setWriteList(String[] writeList){
        Entity entity;
        int id;

        if(writeList.length < 2){
            return;
        }

        for(int i = 1; i < writeList.length; i++){
            id = Integer.parseInt(writeList[i]);
            entity = findEntity(id);
            if(entity == null){
                throw new IllegalStateException(String.format("Malformed save file: No entity with id=%d", id));
            }

            addWrite(entity);
        }
    }

    public void setOwner(User owner){
        if(owner == null){
            throw new IllegalArgumentException("Owner is null");
        }

        this.owner = owner;
    }

    public void setOwner(int ownerId){
        for(Entity user : User.getUsers()){
            if(user.id == ownerId){
                owner = (User) user;
                return;
            }
        }

        throw new IllegalStateException(String.format("Malformed save file: %s has no owner", name));
    }
    
    public static void save(String fileName){

        try{

            FileWriter writer = new FileWriter(fileName);

            writer.write(String.format("%d", Entity.getNextId()));
            writer.write("\n");

            for(Entity group : Group.getGroups()){
                writer.write(group.toString());
                writer.write("\n");
            }

            for(Entity user : User.getUsers()){
                writer.write(user.toString());
                writer.write("\n");
            }

            //Not a good way, since if I add users afterwards, they would not inherit others permissions..
            //It's because I assumed a more granular approach in the API, while the interface's choices are limited to group/other
            for(Entity file : Fil.files){
                writer.write(file.toString());
            }

            writer.close();

        }


        catch(IOException e){
            System.out.println(e.getMessage());
        }


    }

    public static void clean(){
        // Could have all of them in one list in the super class, and have checks for both id and getClass() in other methods, but I think its ok this way
        User.deleteAll();
        Group.deleteAll();
        Fil.deleteAll();
    }

    public static void load(String name){
        String line;
        String[] splitted;
        String[] access;
        String[] write;
        String content;
        Scanner scanner = null;

        clean();



        // The order of restortion matter, since both files and users reference groups
        try{

            File file = new File(name);
            scanner = new Scanner(file);

            if(scanner.hasNextLine()){
                line = scanner.nextLine();
                Entity.setNextId(Integer.parseInt(line));
            }

            while(scanner.hasNextLine()){
                line = scanner.nextLine();
                splitted = line.split(",");
                if(splitted[0].equals("Group")){
                    new Group(splitted[1], splitted[2]);
                }

                else if(splitted[0].equals("User")){
                    new User(splitted[1], splitted[2], splitted[3], splitted[4], splitted[5], splitted[6]);
                }

                else {
                    access = scanner.nextLine().split(",");
                    write = scanner.nextLine().split(",");
                    content = scanner.nextLine();

                    new Fil(splitted[1], splitted[2], splitted[3], access, write, content);
                }
            }

        }

        catch (FileNotFoundException e ){
            System.out.println(e.getMessage());
        }

        catch (Exception e ){
            System.out.println(e.getMessage());
        }

        finally{
            if(scanner != null){
                scanner.close();
            }
        }

    }

    private void checkEntity(Entity entity, List<Entity> permList){
        if(entity == null){
            throw new IllegalArgumentException("Cannot add this permission to null");
        }

        else if(permList.contains(entity)){
            throw new IllegalStateException("Entity has this permission");
        }

    }

    public void addAccess(Entity entity){
        checkEntity(entity, access);
        access.add(entity);
    }

    public void removeAccess(Entity entity){
        if(entity == owner){
            throw new IllegalArgumentException("Cannot remove permissions of owner");
        }

        if(!access.contains(entity)){
            throw new IllegalArgumentException(String.format("%s is not contained in %s", entity.getName(), name));
        }

        access.remove(access.indexOf(entity));

    }

    public void addWrite(Entity entity){
        checkEntity(entity, write);
        write.add(entity);
    }

    public void removeWrite(Entity entity){
        if(entity == owner){
            throw new IllegalArgumentException("Cannot remove permissions of owner");
        }

        if(!write.contains(entity)){
            throw new IllegalArgumentException(String.format("%s is not contained in %s", entity.getName(), name));
        }

        write.remove(write.indexOf(entity));

    }

    public static void deleteAll(){
        files = new ArrayList<>();
    }

    @Override
    public String toString(){
        String text = String.format("File,%s,%d,%d\n", name, id, owner.getId());

        text += "Read";
        for(Entity entity : access){
            text += ",";
            text += entity.getId();
        }

        text += "\n";

        text += "Write";
        for(Entity entity : write){
            text += ",";
            text += entity.getId();
        }

        text += "\n";
        
        text += content.replace("\n", "#");
        text += "\n";

        return text;
    }

    public static void deleteFile(Fil file){
        for(int i = 0; i < files.size(); i++){
            if(files.get(i) == file){
                files.remove(i);
            }
        }
    }

    //these replacements are important for the way file objects are stored
    public void setContent(User user, String content){
        isValidWriter(user);
        content = content.replaceAll("#", "\n");
        this.content = content;
    }

    public void isValidReader(User user){

        if(user == null){
            throw new IllegalArgumentException("Null Reader");
        }

          //Since the User class will give the loaded user another @id than the saved one (since we are stil in the same process)
        else if(user.getId() == owner.getId()){
            return;
        }

        if(!(access.contains(user))){
            if(!(access.contains(user.getGroup()))){
                throw new IllegalArgumentException(String.format("%s has no access to this file", user.getName()));

            }
        }
    }

    public void isValidWriter(User user){
        if(user == owner){
            return;
        }

        else if(user == null){
            throw new IllegalArgumentException("Null Reader");
        }

        if(!(write.contains(user) || write.contains(user.getGroup()))){
            throw new IllegalArgumentException(String.format("%s has no write permissions on this file", user.getName()));
        }
    }

    public String getContent(User user){
        isValidReader(user);
        return content;
    }

    public User getOwner(){
        return owner;
    }

    public static List<Entity> getFiles(){
        return files;
    }

}
