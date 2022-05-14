package services;

import models.Exam;

import java.util.Optional;

public interface ExamService {
   Optional<Exam> findByExamByName(String name);
   Exam findExamByNameWithQuestions(String nameExam);
   Exam save(Exam exam);
}
