package com.example.demo.controller;

import com.example.demo.domain.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
public class SongPageController {

    @Autowired
    private SongRepository songRepository;

    @RequestMapping(value = "/api/songPage", method= RequestMethod.GET)
    public PagedResources<Resource<Song>> query(Pageable pageable, PagedResourcesAssembler assembler){

        return assembler.toResource(songRepository.findAll(pageable));

    }
}
