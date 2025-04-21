package project;

import java.util.List;

public class Entity {
    protected int id; //UNique across users and groups
    protected String name;
    private static int nextId = 1;

    public Entity(String name, List<Entity> entities){
        checkName(name, entities);
        this.name = name;
        this.id = nextId;
        nextId++;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public static int getNextId(){
        return nextId;
    }

    //Brukes for loading
    public static void setNextId(int nextId){
        Entity.nextId = nextId;
    }

    public void checkName(String name, List<Entity> entities){
        if(name.length() == 0){
            throw new IllegalArgumentException("Null name");
        }

        if(name == this.name){
            return;
        }

        for(Entity entity : entities){
            if(entity.name.equals(name)){
                throw new IllegalStateException("Entity name is taken");
            }
        }

        char firstChr = name.charAt(0);

        if( ! ((firstChr >= 65 && firstChr <= 90) || (firstChr >= 97 && firstChr <= 122)) ){
            throw new IllegalArgumentException("Name should start with a letter");
        }

        //The '.' is for file extensions, also I see now that I may have needed another logic to check filenames.. to check for multiple names like is the name contained more than one dot
        for(int i = 1; i < name.length(); i++){
            if((!Character.isDigit(name.charAt(i)) && !Character.isLetter(name.charAt(i)) && name.charAt(i) != ' ')){
                if(this instanceof Fil && name.charAt(i) == '.'){
                    continue;
                }
                
                throw new IllegalArgumentException("Name should not contain special characters");
            }
        }
    }

    public String getName(){
        return name;
    }

    public void setName(String name, List<Entity> entities){
        checkName(name, entities);
        this.name = name;
    }

}
