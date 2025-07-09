package com.trip.info.batch.trip.tripDtlInfo;

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
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.trip.info.rest.trip.TripDto;
import com.trip.info.rest.trip.TripService;

import ch.qos.logback.classic.Logger;

@Configuration
public class TripDtlInfoBatchConfig {

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
    public ItemReader<TripDto> TripDtlInfoItemReader() {
        log.info(">>>>>>>>>>batchpoint itemReader");
        return new TripDtlInfoReader(tripService);
    }
    
    @Bean
    @StepScope
    public ItemProcessor<TripDto, TripDto> TripDtlInfoItemProcessor(@Value("#{jobParameters['apiKey']}") String apiKey) {
        log.info(">>>>>>>>>>batchpoint itemReader");
        log.info("API Key in TripInfoItemReader: " + apiKey);
        return new TripDtlInfoProcess(apiKey, tripService);
    }
    
    @Bean
    @StepScope
    public ItemWriter<TripDto> TripDtlInfoItemWriter() {
        log.info(">>>>>>>>>>batchpoint itemWriter");
        return new TripDtlInfoWriter(tripService);
    }
    
    @Bean
    @JobScope
    public Step TripDtlInfoStep(@Value("#{jobParameters['apiKey']}") String apiKey) {
        log.info(">>>>>>>>>>batchpoint step");
        log.info("API Key in TripInfoStep: " + apiKey);

        return stepBuilderFactory.get("TripDtlInfoStep")
                .<TripDto, TripDto>chunk(CHUNK_SIZE)
                .reader(TripDtlInfoItemReader())
                .processor(TripDtlInfoItemProcessor(apiKey))
                .writer(TripDtlInfoItemWriter())
                .startLimit(10)
                .allowStartIfComplete(true)
                .build();
    }
    
    @Bean
    public Job TripDtlInfoJob() {
        log.info(">>>>>>>>>>batchpoint jobtripdtlinfo job");
        Job job = jobBuilderFactory.get("jobtripdtlinfo")
                .incrementer(new RunIdIncrementer())
                .start(TripDtlInfoStep(null))
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

