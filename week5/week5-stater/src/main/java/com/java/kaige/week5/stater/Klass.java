package com.java.kaige.week5.stater;

import lombok.Data;

import java.util.List;

@Data
public class Klass {

    List<Student> students;

    public void dong(){
        System.out.println("===========Klass====" + this.getStudents());
    }

}
