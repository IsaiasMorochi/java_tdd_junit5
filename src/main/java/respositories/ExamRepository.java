package respositories;

import models.Exam;

import java.util.List;

public interface ExamRepository {
    List<Exam> findAll();
    Exam findExamByNameWithQuestions(String nameExam);
}
