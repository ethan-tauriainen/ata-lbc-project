package com.kenzie.appserver.repositories;

import com.kenzie.appserver.repositories.model.ComicBookRecord;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan
public interface ComicBookRepository extends CrudRepository<ComicBookRecord, String> {
    List<ComicBookRecord> findAll();
}
