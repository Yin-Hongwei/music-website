package com.example.demo.controller;

import com.example.demo.domain.SongList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongListRepository extends JpaRepository<SongList,String> {
    Page<SongList> findAll(Pageable pageable);
}

