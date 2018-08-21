package com.ef.model;

import javax.persistence.*;
import java.sql.Timestamp;


@Entity
@Table(name = "BLOCKED_IPS")
public class BlockedIP {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "IP")
    private String ip;

    @Column(name = "REASON")
    private String reason;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }


}
