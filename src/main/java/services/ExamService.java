package services;

import models.Exam;

public interface ExamService {
    Exam findByExamByName(String name);
}
