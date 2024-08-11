package com.example.tp5traitementbash.start;

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
                    .addLong("started-at", System.currentTimeMillis()).toJobParameters();

            JobExecution jobExecution = this.jobLauncher.run(this.importTransactionsJob, parameters);

            // log or handle execution status
            System.out.println("Job Execution Status: " + jobExecution.getStatus());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}