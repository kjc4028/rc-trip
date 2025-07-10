package com.trip.info.batch.trip.category;

import javax.sql.DataSource;

import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.trip.info.batch.exception.DuplicationDataException;
import com.trip.info.core.category.CategoryService;

import ch.qos.logback.classic.Logger;

@Configuration
public class CategoryBatchConfig extends DefaultBatchConfigurer {

    private final Logger log = (Logger) LoggerFactory.getLogger(this.getClass().getSimpleName());

    @Override
    public void setDataSource(DataSource dataSource) {
    }

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    public CategoryService categoryService;

    static final int CHUNK_SIZE = 10;

    @Bean
    @StepScope
    public ItemReader<CategoryDto> itemReader(@Value("#{jobParameters['apiKey']}") String apiKey) {
        log.info(">>>>>>>>>>batchpoint itemReader");
        return new CategoryItemReaderByLevel(apiKey, "1", categoryService);
    }

    @Bean
    @StepScope
    public ItemReader<CategoryDto> itemReaderStep2(@Value("#{jobParameters['apiKey']}") String apiKey) {
        log.info(">>>>>>>>>>batchpoint itemReaderStep2");
        return new CategoryItemReaderByLevel(apiKey, "2", categoryService);
    }

    @Bean
    @StepScope
    public ItemReader<CategoryDto> itemReaderStep3(@Value("#{jobParameters['apiKey']}") String apiKey) {
        log.info(">>>>>>>>>>batchpoint itemReaderStep3");
        return new CategoryItemReaderByLevel(apiKey, "3", categoryService);
    }

    @Bean
    @StepScope
    public ItemWriter<CategoryDto> itemWriter() {
        log.info(">>>>>>>>>>batchpoint itemWriter");
        return new CategoryItemWriter("1", categoryService);
    }

    @Bean
    @StepScope
    public ItemWriter<CategoryDto> itemWriterStep2() {
        log.info(">>>>>>>>>>batchpoint writer2");
        return new CategoryItemWriter("2", categoryService);
    }

    @Bean
    @StepScope
    public ItemWriter<CategoryDto> itemWriterStep3() {
        log.info(">>>>>>>>>>batchpoint writer3");
        return new CategoryItemWriter("3", categoryService);
    }

    @Bean
    @StepScope
    public ItemReader<CategoryDto> jsonItemReader() {
        log.info(">>>>>>>>>>batchpoint jsonItemReader");
        ClassPathResource resource = new ClassPathResource("./jsonData/category1.json");
        JacksonJsonObjectReader<CategoryDto> jacksonJsonObjectReader = new JacksonJsonObjectReader<>(CategoryDto.class);
        JsonItemReader<CategoryDto> reader = new JsonItemReader<>(resource, jacksonJsonObjectReader);
        reader.setName("testDataJsonItemReader");
        return reader;
    }

    @Bean
    @JobScope
    public Step step() {
        log.info(">>>>>>>>>>batchpoint step");

        return stepBuilderFactory.get("step")
                .<CategoryDto, CategoryDto>chunk(CHUNK_SIZE)
                .reader(itemReader(null))
                .faultTolerant()
                .noSkip(DuplicationDataException.class)
                .writer(itemWriter())
                .startLimit(10)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @JobScope
    public Step step2() {
        log.info(">>>>>>>>>>batchpoint step2");
        return stepBuilderFactory.get("step2")
                .<CategoryDto, CategoryDto>chunk(CHUNK_SIZE)
                .reader(itemReaderStep2(null))
                .faultTolerant()
                .noSkip(DuplicationDataException.class)
                .writer(itemWriterStep2())
                .startLimit(10)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @JobScope
    public Step step3() {
        log.info(">>>>>>>>>>batchpoint step3");
        return stepBuilderFactory.get("step3")
                .<CategoryDto, CategoryDto>chunk(CHUNK_SIZE)
                .reader(itemReaderStep3(null))
                .faultTolerant()
                .noSkip(DuplicationDataException.class)
                .writer(itemWriterStep3())
                .startLimit(10)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    public Job jobcategoty() {
        log.info(">>>>>>>>>>batchpoint job");
        Job job = jobBuilderFactory.get("jobcategoty")
                .incrementer(new RunIdIncrementer())
                .start(step())
                .on("*")
                .to(step2())
                .on("*")
                .to(step3())
                .end()
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