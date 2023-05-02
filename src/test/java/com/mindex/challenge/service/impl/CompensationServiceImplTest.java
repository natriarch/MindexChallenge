package com.mindex.challenge.service.impl;

import com.mindex.challenge.dao.data.Compensation;
import com.mindex.challenge.dao.data.Employee;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CompensationServiceImplTest {

    private String createUrl;
    private String getUrl;

    @Autowired
    private CompensationService compensationService;
    @Autowired
    private EmployeeService employeeService;

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void setup() {
        createUrl = "http://localhost:" + port + "/compensation";
        getUrl = "http://localhost:" + port + "/compensation/{id}";
    }

    @Test
    public void testCreateRead() {
        Employee testEmployee = new Employee();
        testEmployee.setFirstName("John");
        testEmployee.setLastName("Doe");
        testEmployee.setDepartment("Engineering");
        testEmployee.setPosition("Developer");
        Compensation testComp = new Compensation();
        testComp.setEmployee(testEmployee);
        testComp.setSalary(1000000);
        Date effDate = new Date();
        testComp.setEffectiveDate(effDate);
        employeeService.create(testEmployee);
        testComp.setEmployeeId(employeeService.create(testEmployee).getEmployeeId());


        // Create checks
        Compensation createdComp = restTemplate.postForEntity(createUrl, testComp, Compensation.class).getBody();

        assertNotNull(createdComp.getEmployeeId());
        assertCompensationEquivalence(testComp, createdComp);

        // Read checks
        Employee readComp = restTemplate.getForEntity(getUrl, Employee.class, createdComp.getEmployeeId()).getBody();
        assertEquals(createdComp.getEmployeeId(), readComp.getEmployeeId());
        assertCompensationEquivalence(testComp, createdComp);
    }

    private static void assertEmployeeEquivalence(Employee expected, Employee actual) {
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getDepartment(), actual.getDepartment());
        assertEquals(expected.getPosition(), actual.getPosition());
    }

    private static void assertCompensationEquivalence(Compensation expected, Compensation actual) {
        assertEquals(expected.getSalary(), actual.getSalary(), 0);
        assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
        assertEmployeeEquivalence(expected.getEmployee(), actual.getEmployee());
    }
}
