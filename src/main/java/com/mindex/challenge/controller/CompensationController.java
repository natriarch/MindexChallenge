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
    public ResponseEntity<Object> create(@RequestBody Compensation compensation) {
        LOG.debug("Received compensation create request for [{}]", compensation);

        if (null == compensation.getEmployeeId()) {
            if(null != compensation.getEmployee() && null != compensation.getEmployee().getEmployeeId()) {
                //if no employeeId field provided, check if employee field has an id and use that instead
                compensation.setEmployeeId(compensation.getEmployee().getEmployeeId());
            } else {
                //if no employeeId field found on either obj return bad request status
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("EmployeeId must be provided");
            }
        } else if (null != compensation.getEmployeeId() && null != compensation.getEmployee().getEmployeeId() && !compensation.getEmployeeId().equalsIgnoreCase(compensation.getEmployee().getEmployeeId())) {
            //If 2 different employeeIds are provided at top and at employee level then return bad request status
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Conflicting employeeIds provided");
        }

        Compensation result;
        try {
            result = compensationService.create(compensation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("invalid employeeId - not found");
        }

        //I would want to be consistent with the employee controller return types, but decided to illustrate use of ResponseEntity for more control over response
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/compensation/{id}")
    public ResponseEntity<Object> read(@PathVariable String id) {
        LOG.debug("Received compensation get request for id [{}]", id);

        Compensation result;
        try {
            result = compensationService.read(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
