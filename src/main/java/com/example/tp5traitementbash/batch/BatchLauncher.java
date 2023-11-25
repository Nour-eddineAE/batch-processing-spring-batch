package com.example.tp5traitementbash.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

@Service("batchLauncher")
@RequiredArgsConstructor
public class BatchLauncher {

    private final JobLauncher jobLauncher;
    private final Job importTransactionsJob;
    public void launchBatchJob() {
        try {
            JobParameters parameters = new JobParametersBuilder()
                    .addLong("startAt", System.currentTimeMillis()).toJobParameters();

            JobExecution jobExecution = jobLauncher.run(importTransactionsJob, parameters);

            while(jobExecution.isRunning()){
                System.out.println(".....");
            }

            // log or handle execution status
            System.out.println("Job Execution Status: " + jobExecution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}