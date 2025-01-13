package com.Spring_Batch_app.controller;

import com.Spring_Batch_app.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/Batch")
@RequiredArgsConstructor
public class StudentController {

    private  JobLauncher jobLauncher;

    private  Job job;

    @PostMapping(value = "/FirstJob")
    public void importCSVtoDBJob() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        JobParameters jobParameters = new JobParametersBuilder().
                addLong("startAt", System.currentTimeMillis())
                .toJobParameters();

        jobLauncher.run(job,jobParameters);


    }

    @GetMapping(value = "/FirstJobGet")
    public String prueba() throws JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        return "funciona";


    }

}
