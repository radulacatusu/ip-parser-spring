package com.ef.dao.impl;

import com.ef.dao.BlockedIPDao;
import com.ef.dao.LogDao;
import com.ef.model.BlockedIP;
import com.ef.model.Log;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

@Component
public class BlockedIPDaoImpl implements BlockedIPDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Transactional
    @Override
    public void add(List<BlockedIP> ips) {
        for(BlockedIP ip:ips) {
            sessionFactory.getCurrentSession().save(ip);
        }
    }
}
