package com.ef;

import com.ef.model.BlockedIP;
import com.ef.model.Log;
import com.ef.service.BlockedIPService;
import com.ef.service.LogService;
import com.ef.utils.FileReader;
import com.ef.utils.Helper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServiceProvider {

    private static Logger LOGGER = LoggerFactory.getLogger(ServiceProvider.class);

    private Params params;

    @Autowired
    private Helper helper;

    @Autowired
    private FileReader fileReader;

    @Autowired
    private LogService logService;

    @Autowired
    private BlockedIPService blockedIPService;

    public ServiceProvider(){
    }

    public void start() {
        try {
            List<com.ef.model.Log> logs = readFileLogs();
            saveLogs(logs);

            String endDate = helper.calculateEndDate(params.getStartDate(), params.getDuration());
            List<String> ips = selectIPs(endDate);

            if (ips.size() > 0) {
                saveBlockedIPs(endDate, ips);
            }

            truncateTables();
        } catch (SQLException | IOException e) {
            LOGGER.error(e.getMessage());
        }
    }

    private void truncateTables(){
        long startTimeTruncate = System.currentTimeMillis();
        logService.truncateTable();
        long stopTimeTruncate = System.currentTimeMillis();
        long elapsedTime = stopTimeTruncate - startTimeTruncate;
        LOGGER.debug("Time spent to truncate the table: seconds=" + elapsedTime / 1000 + ", milliseconds=" + elapsedTime);
    }

    private void saveBlockedIPs(String endDate, List<String> ips) throws SQLException {
        String reason = generateBlockedMessage(endDate);
        List<BlockedIP> blockedIPS = new ArrayList<>();
        for (String ip : ips) {
            LOGGER.info(ip);
            BlockedIP blockedIP = new BlockedIP();
            blockedIP.setIp(ip);
            blockedIP.setReason(reason);
            blockedIPS.add(blockedIP);
        }
        blockedIPService.add(blockedIPS);
    }

    private void saveLogs(List<Log> logs) {
        long startTimeSave = System.currentTimeMillis();
        logService.add(logs);
        long stopTimeSave = System.currentTimeMillis();
        long elapsedTime = stopTimeSave - startTimeSave;
        LOGGER.debug("Time spent to save the logs in database: seconds=" + elapsedTime / 1000 + ", milliseconds=" + elapsedTime);
    }

    private String generateBlockedMessage(String endDate) {
        return "The IP is blocked because more than " + params.getThreshold() + " entries were found between " +
                params.getStartDate() + " and " + endDate;
    }

    private List<com.ef.model.Log> readFileLogs() throws IOException {
        long startTimeRead = System.currentTimeMillis();
        List<com.ef.model.Log> list = fileReader.readFileLogs(params.getPathToFile());
        long stopTimeRead = System.currentTimeMillis();
        long elapsedTime = stopTimeRead - startTimeRead;
        LOGGER.debug("Time spent to read the logs from file: seconds=" + elapsedTime / 1000 + ", milliseconds=" + elapsedTime);
        return list;
    }

    private List<String> selectIPs(String endDate){
        long startTimeSelect = System.currentTimeMillis();
        List<String> ips = logService.getIps(params.getStartDate(), endDate, params.getThreshold());
        long stopTimeSelect = System.currentTimeMillis();
        long elapsedTime = stopTimeSelect - startTimeSelect;
        LOGGER.debug("Time spent to select the IPS from database: seconds=" + elapsedTime / 1000 + ", milliseconds=" + elapsedTime);
        return ips;
    }
    public void setParams(Params params) {
        this.params = params;
    }
}
