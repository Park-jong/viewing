package com.ssafy.interviewstudy.repository;


import java.util.Optional;

public interface CommonRepository<T, S> {
    Optional<T> findById(S id);

    T save(T t);

    void delete(T t);

    void deleteById(S id);
}
