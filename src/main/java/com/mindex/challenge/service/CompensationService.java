package com.mindex.challenge.service;

import com.mindex.challenge.dao.data.Compensation;

public interface CompensationService {
    Compensation create(Compensation compensation);
    Compensation read(String employeeId);
}
