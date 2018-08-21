package com.ef.dao.impl;

import com.ef.dao.LogDao;
import com.ef.model.Log;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class LogDaoImpl implements LogDao {

    @Autowired
    private SessionFactory sessionFactory;

    public List<Log> getLogDetails() {
        Criteria criteria = sessionFactory.openSession().createCriteria(Log.class);
        return criteria.list();
    }

    @Override
    public List<String> getIps(String startTime, String endTime, String threshold) {
        String SQL_SELECT = "SELECT  IP, count(IP) as count FROM LOGS where start_date between :startDate and :endDate " +
                " GROUP BY IP " +
                " HAVING count > :threshold" +
                " ORDER by count";
        Query query = sessionFactory.getCurrentSession().createSQLQuery(
                SQL_SELECT)
                .setParameter("startDate", startTime)
                .setParameter("endDate", endTime)
                .setParameter("threshold", threshold);
        List<Object[]> result = query.list();
        List<String> strings = new ArrayList<>();
        for(Object[] o :result){
            strings.add(o[0].toString());
        }
        return strings;
    }

    @Override
    public void truncateTable() {
        sessionFactory.getCurrentSession().createSQLQuery("truncate table LOGS").executeUpdate();
    }

    @Override
    public void add(Log log) {
        sessionFactory.getCurrentSession().save(log);
    }

    @Override
    public void add(List<Log> logs) {
        Session session = sessionFactory.getCurrentSession();

        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                String SQL_INSERT = " insert into LOGS (start_date, IP, request, status, user_agent)"
                        + " values (?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(SQL_INSERT)) {
                    int i = 0;

                    for (Log entity : logs) {
                        statement.setTimestamp(1, entity.getStartDate());
                        statement.setString(2, entity.getIp());
                        statement.setString(3, entity.getRequest());
                        statement.setInt(4, entity.getStatus());
                        statement.setString(5, entity.getUserAgent());

                        statement.addBatch();
                        i++;

                        if (i % 1000 == 0 || i == logs.size()) {
                            statement.executeBatch();
                        }
                    }
                    statement.close();
                }
            }
        });
    }

}
