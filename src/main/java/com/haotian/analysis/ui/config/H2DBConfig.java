package com.haotian.analysis.ui.config;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class H2DBConfig {
    @JsonProperty("url")
    private String jdbcUrl;
    @JsonProperty("username")
    private String jdbcUser;
    @JsonProperty("password")
    private String jdbcPasswd;
    @JsonProperty("init-db")
    private String[] initDb;
    @JsonProperty("sql-mapping")
    private Map<String, String> sqlMapping;

    public Map<String, String> getSqlMapping() {
        return sqlMapping;
    }

    public void setSqlMapping(Map<String, String> sqlMapping) {
        this.sqlMapping = sqlMapping;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcUser() {
        return jdbcUser;
    }

    public void setJdbcUser(String jdbcUser) {
        this.jdbcUser = jdbcUser;
    }

    public String getJdbcPasswd() {
        return jdbcPasswd;
    }

    public void setJdbcPasswd(String jdbcPasswd) {
        this.jdbcPasswd = jdbcPasswd;
    }

    public String[] getInitDb() {
        return initDb;
    }

    public void setInitDb(String[] initDb) {
        this.initDb = initDb;
    }
}
