package com.followinsider.core.monitoring;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.Date;

@Getter
public class Log {

    Date date;

    String message;

    String stackTrace;

    String path;

    String thread;

    String type;

    String appName;

    String pid;

    @JsonProperty(value = "@timestamp")
    public void setDate(Date date) {
        this.date = date;
    }

    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonProperty("stack_trace")
    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    @JsonProperty(value = "logger_name")
    public void setPath(String path) {
        this.path = path;
    }

    @JsonProperty("thread_name")
    public void setThread(String thread) {
        this.thread = thread;
    }

    @JsonProperty("level")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("appName")
    public void setAppName(String appName) {
        this.appName = appName;
    }

    @JsonProperty("pid")
    public void setPid(String pid) {
        this.pid = pid;
    }

}
