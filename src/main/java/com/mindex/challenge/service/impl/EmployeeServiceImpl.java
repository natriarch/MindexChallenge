package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.EmployeeRepository;
import com.mindex.challenge.dao.data.Employee;
import com.mindex.challenge.dao.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Override
    public Employee create(Employee employee) {
        LOG.debug("Creating employee [{}]", employee);

        employee.setEmployeeId(UUID.randomUUID().toString());
        employeeRepository.insert(employee);

        return employee;
    }

    @Override
    public Employee read(String id) {
        LOG.debug("Reading employee with id [{}]", id);

        Employee employee = employeeRepository.findByEmployeeId(id);

        if (employee == null) {
            throw new RuntimeException("Invalid employeeId: " + id);
        }

        return employee;
    }

    @Override
    public Employee update(Employee employee) {
        LOG.debug("Updating employee [{}]", employee);

        return employeeRepository.save(employee);
    }

    @Override
    public ReportingStructure generateReportingStructure(String id) { //does not persist result
        Employee employee = this.read(id);

        LOG.debug("Generating ReportingStructure for employee with id: {}", id);
        ReportingStructure reportingStructureResult = new ReportingStructure();
        reportingStructureResult.setEmployee(employee);
        reportingStructureResult.setNumberOfReports(calculateNumberOfReports(employee));

        return reportingStructureResult;
    }

    //Helper method to recursively calculate number of reports for an employee
    private int calculateNumberOfReports(Employee employee) {
        int count = 0;
        if (employee.getDirectReports() != null) {
            count = employee.getDirectReports().size();
            for (Employee e : employee.getDirectReports()) {
                count += calculateNumberOfReports(employeeRepository.findByEmployeeId(e.getEmployeeId()));
            }
        }
        return count;
    }
}
