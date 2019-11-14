package com.example.demo.controller;

import com.example.demo.domain.ListSong;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListSongRepository extends JpaRepository<ListSong,String>{
    Page<ListSong> findAll(Pageable pageable);
}
