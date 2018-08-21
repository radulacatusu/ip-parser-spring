package com.ef.service;

import com.ef.model.Log;

import java.util.List;

public interface LogService {
    void add(Log log);

    void add(List<Log> log);

    List<Log> getLogDetails();

    List<String> getIps(String startTime, String endTime, String threshold);

    void truncateTable();

}
