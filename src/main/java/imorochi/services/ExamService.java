package imorochi.services;

import imorochi.models.Exam;

import java.util.Optional;

public interface ExamService {
   Optional<Exam> findByExamByName(String name);
   Exam findExamByNameWithQuestions(String nameExam);
   Exam findExamByNameWithQuestionsTwoCall(String nameExam);
   Exam save(Exam exam);
}
