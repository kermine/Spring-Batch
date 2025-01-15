package com.Spring_Batch_config;

import com.Spring_Batch_app.model.Student;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class StudentProcessor implements ItemProcessor<Student, Student> {

    @Override
    public Student process(Student item) throws Exception {
        //Toda la logica de negocio para realizar las operaciones necesarias para transformar los datos
        return item;
    }
}
