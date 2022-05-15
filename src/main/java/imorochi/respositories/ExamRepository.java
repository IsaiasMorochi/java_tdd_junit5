package imorochi.respositories;

import imorochi.models.Exam;

import java.util.List;

public interface ExamRepository {
    List<Exam> findAll();
    Exam save(Exam exam);
}
