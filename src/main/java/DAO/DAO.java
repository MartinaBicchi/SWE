package DAO;

import java.util.List;

public interface DAO <T, ID>{
    // This method returns the object with the given ID
    T get(ID id) throws Exception;

    // This method returns all the objects
    List<T> getAll() throws Exception;

    // This method inserts the given object
    void insert(T t) throws Exception;

    // This method deletes the given object
    boolean remove(ID id) throws Exception;

}