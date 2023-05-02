package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.data.Compensation;
import com.mindex.challenge.dao.data.Employee;
import com.mindex.challenge.service.CompensationService;
import de.bwaldvogel.mongo.bson.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(CompensationServiceImpl.class);

    @Autowired
    CompensationRepository compensationRepository;
    @Autowired
    EmployeeRepository employeeRepository;

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compensation [{}]", compensation);

        Employee targetEmployee = employeeRepository.findByEmployeeId(compensation.getEmployee().getEmployeeId());

        // If a compensation record includes an employee that does not exist we error out to prevent mismatched data between the 2 related types.
        // User must create the employee first
        if (null == targetEmployee)
            throw new RuntimeException("Compensation could not be created - Employee with employeeId: " + compensation.getEmployee().getEmployeeId() + " not found");

        // Allows the posted object to only have the employeeId field, the rest will be filled in by this call.
        // Additionally prevents inconsistent data between employee and compensation types by always using the values in the employee repository
        compensation.setEmployee(targetEmployee);

        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation record for employee with id [{}]", employeeId);

        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);

        if (compensation == null)
            throw new RuntimeException("Invalid compensation record for employee with id: " + employeeId);

        return compensation;
    }
}
