package com.example.SpringBatchTutorial.job;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class HelloWorldJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job helloWorldJob() {
        return new JobBuilder("helloWorldJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(helloWorldStep())
                .build();

    }

    @JobScope
    @Bean
    public Step helloWorldStep() {
        return new StepBuilder("helloWorldStep", jobRepository)
                .tasklet(helloWorldTasklet(), transactionManager)
                .build();
    }

    @StepScope
    @Bean
    public Tasklet helloWorldTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Hello World");
            return RepeatStatus.FINISHED;
        };
    }
    // Tasklet 인터페이스는 단일 추상 메서드를 가지고 있기 때문에 람다식으로 변환 가능
    // 람다식의 형태 : ( parameters ) -> { body }
}
