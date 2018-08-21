package com.ef.service;

import com.ef.model.BlockedIP;
import com.ef.model.Log;

import java.util.List;

public interface BlockedIPService {
    void add(List<BlockedIP> ips);
}
