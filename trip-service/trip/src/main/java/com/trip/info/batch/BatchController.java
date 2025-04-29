package com.trip.info.batch;

import java.util.Date;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/trips/batch")
public class BatchController {
    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    @Autowired
    private JobLocator jobLocator;
    
    @Autowired
    private JobLauncher jobLauncher;

    /*
     * 배치 수동 호출
     */
    @GetMapping("/{jobname}")
    public void batch(@PathVariable String jobname, @RequestParam String apiKey){
        log.info("batch call >>>> " + jobname);
        log.info("API Key: " + apiKey);
        
        try {
            if ("all".equals(jobname)) {
                // jobcategoty 실행
                Job categoryJob = jobLocator.getJob("jobcategoty");
                JobParametersBuilder categoryBuilder = new JobParametersBuilder();
                categoryBuilder.addDate("date", new Date());
                categoryBuilder.addString("apiKey", apiKey);
                jobLauncher.run(categoryJob, categoryBuilder.toJobParameters());
                
                // jobtripinfo 실행
                Job tripInfoJob = jobLocator.getJob("jobtripinfo");
                JobParametersBuilder tripInfoBuilder = new JobParametersBuilder();
                tripInfoBuilder.addDate("date", new Date());
                tripInfoBuilder.addString("apiKey", apiKey);
                jobLauncher.run(tripInfoJob, tripInfoBuilder.toJobParameters());
            } else {
                // 단일 job 실행
                Job job = jobLocator.getJob(jobname);
                JobParametersBuilder builder = new JobParametersBuilder();
                builder.addDate("date", new Date());
                builder.addString("apiKey", apiKey);
                jobLauncher.run(job, builder.toJobParameters());
            }
        } catch (NoSuchJobException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
            log.info("batchController ex " + e);
        }
    }
}
