package dB;

import java.util.List;

public interface DAO <T>{
    //sql actions
    boolean add(T t);
    boolean update(T t);
    T get(long id);
    List<T> retrieve();
}
