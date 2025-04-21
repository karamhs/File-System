package project;

import java.util.Iterator;
import java.util.List;

public class groupIterator implements Iterator<Entity>{
    private Entity[] members;
    private int idx = -1;

    public groupIterator(List<User> members){
        members.toArray(this.members);
    }

    public boolean hasNext(){
        return idx < members.length;
    }

    public Entity next(){
        idx ++; 
        return members[idx];
    }

}
