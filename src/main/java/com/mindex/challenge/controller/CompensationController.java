package com.mindex.challenge.controller;

import com.mindex.challenge.dao.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CompensationController {
    private static final Logger LOG = LoggerFactory.getLogger(CompensationController.class);

    @Autowired
    private CompensationService compensationService;

    @PostMapping("/compensation")
    public ResponseEntity<Compensation> create(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);
        //I would want to be consistent with the employee controller return types, but decided to illustrate use of ResponseEntity for more control over response
        return ResponseEntity.status(HttpStatus.CREATED).body(compensationService.create(compensation));
    }

    @GetMapping("/compensation/{id}")
    public ResponseEntity<Compensation> read(@PathVariable String id) {
        LOG.debug("Received compensation get request for id [{}]", id);

        return ResponseEntity.status(HttpStatus.OK).body(compensationService.read(id));
    }
}
