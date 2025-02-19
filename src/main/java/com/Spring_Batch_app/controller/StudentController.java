package com.Spring_Batch_app.controller;

import jakarta.annotation.PostConstruct;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;




@RestController
public class StudentController {

    private final JobLauncher jobLauncher;
    private final Job job;

    public StudentController(JobLauncher jobLauncher, Job job) {
        this.jobLauncher = jobLauncher;
        this.job = job;
    }

    @PostConstruct
    public void init() {
        System.out.println("StudentController cargado correctamente");
    }

    @PostMapping(value = "FirstJob")
    public void importCSVtoDBJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder().
                addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job,jobParameters);


    }
}
