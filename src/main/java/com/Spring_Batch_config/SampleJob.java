package com.Spring_Batch_config;

import com.Spring_Batch_app.model.Student;
import com.Spring_Batch_app.repository.StudentRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
public class SampleJob {


    @Bean
    //Devolvemos FlatFileItemReader porque vamos a procesar un archivo csv
    public FlatFileItemReader<Student> itemReader(){
        FlatFileItemReader<Student> itemReader = new FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/students.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(lineMapper());
        return itemReader;
    }

    @Bean
    public StudentProcessor processor(){
        return new StudentProcessor();
    }

    @Bean
    public RepositoryItemWriter<Student> writer(StudentRepository repository){
        RepositoryItemWriter<Student> writer = new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor asyncTaskExecutor = new SimpleAsyncTaskExecutor("spring_batch_csvtoMySQL");
        asyncTaskExecutor.setConcurrencyLimit(10);
        return new SimpleAsyncTaskExecutor("spring_batch_csvtoMySQL");
    }

    @Bean
    public Step importStep(TaskExecutor taskExecutor, JobRepository jobRepository, StudentProcessor processor, PlatformTransactionManager platformTransactionManager,StudentRepository repository){
        return new StepBuilder("csvImport",jobRepository)
                .<Student,Student>chunk(10,platformTransactionManager)
                .reader(itemReader())
                .processor(processor)
                .writer(writer(repository))
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    public Job runJob(JobRepository jobRepository,Step step1){
        return new JobBuilder("importStudents",jobRepository)
                .start(step1)
                .build();
    }



    private LineMapper<Student> lineMapper(){
        DefaultLineMapper<Student> lineMapper= new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false); // nos permite que si alguna fila le falta algun campo no la deseche
        lineTokenizer.setNames("id","firstname","lastname","age");

        BeanWrapperFieldSetMapper<Student> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Student.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;
    }
}
