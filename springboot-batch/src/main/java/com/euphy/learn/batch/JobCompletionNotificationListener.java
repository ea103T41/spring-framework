package com.euphy.learn.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            Integer accountCount = jdbcTemplate.queryForObject("SELECT count(1) FROM T_ACCOUNT", Integer.class);
            Integer roleCount = jdbcTemplate.queryForObject("SELECT count(1) FROM T_ACCOUNT_ROLE", Integer.class);
            log.info("!!! JOB FINISHED! Inserted account: " + accountCount + " roles: " + roleCount);
        }
    }
}
