package com.amazon.railwaycrossingapp.db;

import java.util.ArrayList;
import java.util.Map;

// Database Access Object -> Design Pattern :)
public interface DAO<T>{
	boolean set(T t);
	boolean delete(T t);
	//based on object type in parameter throwing main users or crossing map
	Map<String, ?> retrieve(T t);
	T retrieve(String key);

}
