package services;

import models.Exam;
import respositories.ExamRepository;

import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    ExamRepository examRepository;

    public ExamServiceImpl(ExamRepository examRepository) {
        this.examRepository = examRepository;
    }

    @Override
    public Optional<Exam> findByExamByName(String name) {
        return examRepository.findAll()
                .stream()
                .filter(exam -> exam.getName().contains(name))
                .findFirst();
    }
}
