package com.trip.mbti.batch.trip.tripinfo;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
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

import com.trip.mbti.rest.trip.TripService;

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
    
    @Value("${apiServiceKey}")
    String apiServiceKey;
    
    static final int CHUNK_SIZE = 10;
    
    @Bean
    @StepScope
    public ItemReader<TripInfoDto> TripInfoItemReader() {
        log.info(">>>>>>>>>>batchpoint itemReader");
        return new TripInfoReader(apiServiceKey, tripService);
        // ItemProcessor를 구현하고 데이터 처리 로직을 작성
    }
    
    
    @Bean
    @StepScope
    public ItemWriter<TripInfoDto> TripInfoItemWriter() {
        log.info(">>>>>>>>>>batchpoint itemWriter");
        return new TripInfoWriter(tripService);
        // ItemWriter를 구현하고 데이터를 출력하는 로직을 작성
    }
    
    @Bean
    @JobScope
    public Step TripInfoStep() {
        log.info(">>>>>>>>>>batchpoint step");

        return stepBuilderFactory.get("TripInfoStep")
                .<TripInfoDto, TripInfoDto>chunk(CHUNK_SIZE) // Chunk 사이즈 설정
                .reader(TripInfoItemReader())
                .writer(TripInfoItemWriter())
                .startLimit(10)
                .allowStartIfComplete(true)
                .build();
    }
    
    @Bean
    public Job TripInfoJob() {
        log.info(">>>>>>>>>>batchpoint jobtripinfo job");
        Job job = jobBuilderFactory.get("jobtripinfo")
                .incrementer(new RunIdIncrementer()).start(TripInfoStep())
                    .build();
        ReferenceJobFactory factory = new ReferenceJobFactory(job);
        try {
            jobRegistry.register(factory);
        } catch (DuplicateJobException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return job;
    }
}

