package project;

import java.util.Iterator;
import java.util.List;




public class filIterator implements Iterator<Entity>{
    private List<List<Entity>> entList;
    private int list = 0;
    private int elem = -1; 

    public filIterator(List<Entity> access, List<Entity> write){
        entList.add(access);
        entList.add(write);
    }

    public boolean hasNext(){
        if(list >= entList.size()){
            return false;
        }

        return elem < entList.get(list).size();
    }

    public Entity next(){
        elem ++;

        if(elem >= entList.get(list).size()){
            list ++;
        }

        if(list == 0){
            System.out.println(String.format("Access Memeber: %s", entList.get(list).get(elem).getName()));
        }

        else{
            System.out.println(String.format("Write Memeber: %s", entList.get(list).get(elem).getName()));
        }

        return entList.get(list).get(elem);
    }
}
