package org.example.batchservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.batchservice.dto.VideoDTO;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.PagingQueryProvider;
import org.springframework.batch.item.database.builder.JdbcPagingItemReaderBuilder;
import org.springframework.batch.item.database.support.SqlPagingQueryProviderFactoryBean;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.support.JdbcTransactionManager;
import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JobConfig {
    private final DataSource dataSource;
    private final Step createStatsStep;
    private final Tasklet testTasklet;
    //private final ItemProcessor<?,?> statsProcessor;
    private final int CHUNK_SIZE = 20;
    /*
        1. 영상 전부 가져오기 (video_id, member_id 필요)
        2. video_id에 해당하는 하루치 재생기록 가져오기
        3. 재생 기록 (조회수), 광고 조회수, 일간 총 재생 시간, 정산 금액 연산
        4. 저장
    */


    @Bean
    public JdbcTransactionManager transactionManager() {
        return new JdbcTransactionManager(dataSource);
    }

    @Bean
    public Job testJob(JobRepository jobRepository) {
        return new JobBuilder("testJob",jobRepository)
                .start(createStatsStep)
                .build();
    }

    @Bean
    @JobScope
    public Step createStatsStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) throws Exception{
        log.info("step start 1");
        return new StepBuilder("createStatsStep",jobRepository)
                .<VideoDTO,VideoDTO>chunk(CHUNK_SIZE,transactionManager)
                .reader(videoReader())
                .build();
    }

    @Bean
    public JdbcPagingItemReader<VideoDTO> videoReader() throws Exception {
        JdbcPagingItemReader<VideoDTO> reader = new JdbcPagingItemReaderBuilder<VideoDTO>()
                .name("videoReader")
                .pageSize(CHUNK_SIZE)
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(VideoDTO.class))
                .queryProvider(createVideoQueryProvider())
                .build();
        return reader;
    }

    @Bean
    public PagingQueryProvider createVideoQueryProvider() throws Exception {
        SqlPagingQueryProviderFactoryBean queryProvider = new SqlPagingQueryProviderFactoryBean();
        queryProvider.setDataSource(dataSource);
        queryProvider.setSelectClause("video_id, member_id");
        queryProvider.setFromClause("from video");

        Map<String, Order> sortKeys = new HashMap<>(1);
        sortKeys.put("video_id",Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        return queryProvider.getObject();
    }

    @Bean
    @StepScope
    public Tasklet testTasklet() {
        return ((contribution, chunkContext) -> {
            log.info("task success");
            return RepeatStatus.FINISHED;
        });
    }

}
