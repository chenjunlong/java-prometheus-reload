package com.github.jpreload.model;

import java.io.Serializable;

/**
 * Created by chenjunlong on 2018/8/1.
 */
public class Params implements Serializable {

    private static final long serialVersionUID = 7650185918906444025L;

    private String jobName;

    private String host;

    private String port;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
