package com.mindex.challenge.service;

import com.mindex.challenge.dao.data.Employee;
import com.mindex.challenge.dao.data.ReportingStructure;

public interface EmployeeService {
    Employee create(Employee employee);
    Employee read(String id);
    Employee update(Employee employee);
    ReportingStructure generateReportingStructure(String id);
}
