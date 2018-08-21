package com.ef.dao;

import com.ef.model.Log;

import java.util.List;

public interface LogDao {

    void add(Log log);

    void add(List<Log> log);

    List<Log> getLogDetails();

    List<String> getIps(String startTime, String endTime, String threshold);

    void truncateTable();
}
