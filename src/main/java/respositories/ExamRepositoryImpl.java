package respositories;

import models.Exam;

import java.util.Arrays;
import java.util.List;

public class ExamRepositoryImpl implements ExamRepository {
    @Override
    public List<Exam> findAll() {
        return Arrays.asList(
                new Exam(1L, "Matematicas"),
                new Exam(2L, "Lenguaje"),
                new Exam(3L, "Fisica"));
    }
}
