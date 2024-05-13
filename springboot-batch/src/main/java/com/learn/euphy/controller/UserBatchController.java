package com.learn.euphy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-batch")
public class UserBatchController {

    private static final Logger log = LoggerFactory.getLogger(UserBatchController.class);

    private final JobLauncher jobLauncher;
    private final JobExplorer jobExplorer;
    private final Job job;

    @Autowired
    public UserBatchController(JobLauncher jobLauncher, JobExplorer jobExplorer, Job job) {
        this.jobLauncher = jobLauncher;
        this.jobExplorer = jobExplorer;
        this.job = job;
    }

    @GetMapping("/job/{id}")
    public ResponseEntity<String> getJob(@PathVariable("id") Long id) {
        JobExecution jobExecution = jobExplorer.getJobExecution(id);
        if (jobExecution == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(jobExecution.toString());
    }


    @PostMapping("/init")
    public ResponseEntity<String> initUsers() {
        String jobName = job.getName();
        log.info("Starting job <{}>.", jobName);
        try {
            JobExecution jobExecution = jobLauncher.run(job, new JobParameters());
            long id = jobExecution.getJobId();
            return ResponseEntity.accepted().body("Job id: " + id + " successfully started.");
        } catch (Exception e) {
            log.error("Could not start job.", e);
            return ResponseEntity.status(500).body("Could not start job-" + jobName + ".");
        }
    }
}
