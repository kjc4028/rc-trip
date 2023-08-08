package com.trip.mbti.batch.trip.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CategoryBatchConfig {

	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;

  

        @Bean
    public ItemReader<CategoryVo> itemReader() {
        List<CategoryVo> list = new ArrayList<CategoryVo>();
        CategoryVo vo = new CategoryVo();
        CategoryVo vo2 = new CategoryVo();
        CategoryVo vo3 = new CategoryVo();
        vo.setA("a1");
        list.add(vo);
        vo2.setA("a2");
        list.add(vo2);
        vo3.setA("a3");
        list.add(vo3);
     
        return new CategoryItemReader(list);
        // ItemProcessor를 구현하고 데이터 처리 로직을 작성
    }

    // @Bean
    // public ItemProcessor<String, String> itemProcessor() {
    //     return "b";
    //     // ItemProcessor를 구현하고 데이터 처리 로직을 작성
    // }

    @Bean
    public ItemWriter<CategoryVo> itemWriter() {
        return new CategoryItemWriter();
        // ItemWriter를 구현하고 데이터를 출력하는 로직을 작성
    }


    @Bean
    public Step step() {
        return stepBuilderFactory.get("step")
            .<CategoryVo, CategoryVo>chunk(10) // Chunk 사이즈 설정
            .reader(itemReader())
            // .processor(itemProcessor())
             .writer(itemWriter())
            .build();
    }

    @Bean
    public Job job() {
        return jobBuilderFactory.get("job")
            .start(step())
            .build();
    }

}