package services;

import models.Exam;

import java.util.Optional;

public interface ExamService {
   Optional<Exam> findByExamByName(String name);
   Optional<Exam> findExamByNameWithQuestions(String nameExam);
}
