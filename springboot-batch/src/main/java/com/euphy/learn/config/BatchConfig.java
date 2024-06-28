package com.euphy.learn.config;

import com.euphy.learn.batch.AccountItemProcessor;
import com.euphy.learn.batch.AccountItemWriter;
import com.euphy.learn.batch.JobCompletionNotificationListener;
import com.euphy.learn.model.User;
import com.euphy.learn.model.Account;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;

    public BatchConfig(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Bean
    public FlatFileItemReader<User> reader() {
        return new FlatFileItemReaderBuilder<User>()
                .name("userItemReader")
                .resource(new ClassPathResource("test-users.csv"))
                .delimited()
                .names("name", "email", "password")
                .targetType(User.class)
                .build();
    }

    @Bean
    public AccountItemProcessor processor() {
        return new AccountItemProcessor();
    }

    @Bean
    public AccountItemWriter writer() {
        return new AccountItemWriter();
    }

    @Bean
    public Job importUserJob(Step step1, JobCompletionNotificationListener listener) {
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(DataSourceTransactionManager transactionManager,
                      FlatFileItemReader<User> reader,
                      AccountItemProcessor processor,
                      AccountItemWriter writer) {

        return new StepBuilder("step1", jobRepository)
                .<User, Account>chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

}
