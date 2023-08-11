package com.trip.mbti.batch.trip.category;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.DuplicateJobException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.support.ReferenceJobFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.json.JacksonJsonObjectReader;
import org.springframework.batch.item.json.JsonItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import com.trip.mbti.rest.category.CategoryService;

@Configuration
public class CategoryBatchConfig {

    @Autowired
    public JobBuilderFactory jobBuilderFactory;

    @Autowired
    public StepBuilderFactory stepBuilderFactory;

    @Autowired
    public JobRegistry jobRegistry;

    @Autowired
    public CategoryService categoryService;

    @Value("${apiServiceKey}")
    String apiServiceKey;
    
    
    @Bean
    public ItemReader<CategoryVo> itemReader() {
        
        return new CategoryItemReaderLvOne(apiServiceKey);
        // ItemProcessor를 구현하고 데이터 처리 로직을 작성
    }

    // @Bean
    // public ItemReader<CategoryVo> itemReader() {
    //     System.out.println("reader >>> ");
    //     List<CategoryVo> list = new ArrayList<CategoryVo>();
        
    //     for (int i = 0; i < 200; i++) {
    //         CategoryVo vo = new CategoryVo();
    //         vo.setA("a"+i);
    //         list.add(vo);
    //     }

    //     return new CategoryItemReader(list);
    //     // ItemProcessor를 구현하고 데이터 처리 로직을 작성
    // }

    // @Bean
    // public ItemProcessor<String, String> itemProcessor() {
    // return "b";
    // // ItemProcessor를 구현하고 데이터 처리 로직을 작성
    // }

    @Bean
    public ItemWriter<CategoryVo> itemWriter() {
        System.out.println("writer>>> ");
        return new CategoryItemWriter();
        // ItemWriter를 구현하고 데이터를 출력하는 로직을 작성
    }

    @Bean
    public ItemReader<CategoryVo> jsonItemReader() {
        ClassPathResource resource = new ClassPathResource("./jsonData/category1.json");
        JacksonJsonObjectReader<CategoryVo> jacksonJsonObjectReader = new JacksonJsonObjectReader<>(CategoryVo.class);
        JsonItemReader<CategoryVo> reader = new JsonItemReader<>(resource, jacksonJsonObjectReader);
        reader.setName("testDataJsonItemReader");
        return reader;
    }

    @Bean
    public Step step() {
      
      System.out.println("step>>> ");
        return stepBuilderFactory.get("step")
                .<CategoryVo, CategoryVo>chunk(10) // Chunk 사이즈 설정
                .reader(itemReader())
                //.reader(jsonItemReader())
                // .processor(itemProcessor())
                .writer(itemWriter())
                .build();
    }

    @Bean
    public Job job() {
         System.out.println("job>>> ");
                Job job = jobBuilderFactory.get("jobtest")
                .start(step())
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