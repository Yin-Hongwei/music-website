package com.example.demo.controller;

import com.example.demo.domain.Consumer;
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
public class ConsumerPageController {

    @Autowired
    private ConsumerRepository consumerRepository;

    @RequestMapping(value = "/api/consumerPage", method= RequestMethod.GET)
    public PagedResources<Resource<Consumer>> query(Pageable pageable, PagedResourcesAssembler assembler){

        return assembler.toResource(consumerRepository.findAll(pageable));

    }
}
