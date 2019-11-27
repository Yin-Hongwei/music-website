package com.example.demo.controller;

import com.example.demo.domain.Singer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SingerRepository extends JpaRepository<Singer,Long> {
    Page<Singer> findAll(Pageable pageable);
}
