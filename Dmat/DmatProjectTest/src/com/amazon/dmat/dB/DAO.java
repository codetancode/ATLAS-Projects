package com.amazon.dmat.dB;

import java.util.List;

public interface DAO<T> {

    int insert(T object);
    int update(T object);

}
