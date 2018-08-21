package com.ef.service.impl;

import com.ef.dao.BlockedIPDao;
import com.ef.model.BlockedIP;
import com.ef.model.Log;
import com.ef.service.BlockedIPService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BlockedIPServiceImpl implements BlockedIPService {

    @Autowired
    private BlockedIPDao ipDao;

    @Override
    public void add(List<BlockedIP> ips) {
        ipDao.add(ips);
    }
}
