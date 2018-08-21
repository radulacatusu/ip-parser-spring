package com.ef.service.impl;

import com.ef.dao.LogDao;
import com.ef.model.Log;
import com.ef.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Transactional
    @Override
    public void add(Log log) {
        logDao.add(log);
    }

    @Transactional
    @Override
    public void add(List<Log> log) {
        logDao.add(log);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Log> getLogDetails() {
        return logDao.getLogDetails();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> getIps(String startTime, String endTime, String threshold) {
        return logDao.getIps(startTime, endTime, threshold);
    }

    @Transactional
    @Override
    public void truncateTable() {
        logDao.truncateTable();
    }
}
