package com.api.controller.dto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @Description TODO
 * @Author Caizy
 * @Date 2018/11/22 11:00
 **/
@Component
@PropertySource("classpath:version.properties")
public class IosVersionDto {

    @Value("${ios.force}")
    private Boolean force;
    @Value("${ios.updateLog}")
    private String updateLog;
    @Value("${ios.version}")
    private String version;
    @Value("${ios.url}")
    private String url;

    public Boolean getForce() {
        return force;
    }

    public void setForce(Boolean force) {
        this.force = force;
    }

    public String getUpdateLog() {
        return updateLog;
    }

    public void setUpdateLog(String updateLog) {
        this.updateLog = updateLog;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
