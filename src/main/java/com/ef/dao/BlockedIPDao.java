package com.ef.dao;

import com.ef.model.BlockedIP;
import com.ef.model.Log;

import java.util.List;

public interface BlockedIPDao {

    void add(List<BlockedIP> ips);
}
