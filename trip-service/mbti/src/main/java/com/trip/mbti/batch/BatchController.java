package com.trip.mbti.batch;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.qos.logback.classic.Logger;

@RestController
@RequestMapping("/trip/batch")
public class BatchController {
    
    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());
    
    @Autowired
    private JobLocator jobLocator;
    
    @Autowired
    private JobLauncher jobLauncher;

    @RequestMapping("/{jobname}")
    public void batch(@PathVariable String jobname){
     System.out.println("batch call >>>>");
        try {
          
            Job job =  jobLocator.getJob(jobname);
            JobParametersBuilder builder = new JobParametersBuilder();
            builder.addDate("date", new Date());
            jobLauncher.run(job, builder.toJobParameters());

    } catch (NoSuchJobException | JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException | JobParametersInvalidException e) {
        log.info("batchCpntroller ex " + e);
    }
      
    }
}
