package com.example.demo.controller;

import com.example.demo.domain.Consumer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConsumerRepository extends JpaRepository<Consumer,String>{
    Page<Consumer> findAll(Pageable pageable);
}
