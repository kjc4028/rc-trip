package com.trip.info.batch.trip.tripinfo;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.trip.info.core.trip.TripService;

import ch.qos.logback.classic.Logger;

@Configuration
public class TripInfoBatchConfig {

    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobRegistry jobRegistry;    
    
    @Autowired
    public TripService tripService;
    
    static final int CHUNK_SIZE = 10;
    
    @Bean
    @StepScope
    public ItemReader<TripInfoDto> TripInfoItemReader(@Value("#{jobParameters['apiKey']}") String apiKey) {
        log.info(">>>>>>>>>>batchpoint itemReader");
        log.info("API Key in TripInfoItemReader: " + apiKey);
        return new TripInfoReader(apiKey, tripService);
    }
    
    @Bean
    @StepScope
    public ItemWriter<TripInfoDto> TripInfoItemWriter() {
        log.info(">>>>>>>>>>batchpoint itemWriter");
        return new TripInfoWriter(tripService);
    }
    
    @Bean
    @JobScope
    public Step TripInfoStep(@Value("#{jobParameters['apiKey']}") String apiKey) {
        log.info(">>>>>>>>>>batchpoint step");
        log.info("API Key in TripInfoStep: " + apiKey);

        return stepBuilderFactory.get("TripInfoStep")
                .<TripInfoDto, TripInfoDto>chunk(CHUNK_SIZE)
                .reader(TripInfoItemReader(apiKey))
                .writer(TripInfoItemWriter())
                .startLimit(10)
                .allowStartIfComplete(true)
                .build();
    }
    
    @Bean
    public Job TripInfoJob() {
        log.info(">>>>>>>>>>batchpoint jobtripinfo job");
        Job job = jobBuilderFactory.get("jobtripinfo")
                .incrementer(new RunIdIncrementer())
                .start(TripInfoStep(null))
                .build();
        ReferenceJobFactory factory = new ReferenceJobFactory(job);
        try {
            jobRegistry.register(factory);
        } catch (DuplicateJobException e) {
            e.printStackTrace();
        }

        return job;
    }
}

