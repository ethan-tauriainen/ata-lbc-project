package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ComicBookRecord;

import com.kenzie.appserver.service.model.ComicBook;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

@EnableScan
public interface ComicBookRepository extends CrudRepository<ComicBookRecord, String> {
    List<ComicBookRecord> findAll();
    Optional<ComicBookRecord> findByAsin(String asin);
    void deleteByAsin(String asin);
}
