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
    public Exam findByExamByName(String name) {
        Optional<Exam> examOptional = examRepository.findAll()
                .stream()
                .filter(exam -> exam.getName().contains(name))
                .findFirst();
        return examOptional.isPresent() ? examOptional.orElseThrow() : null;
    }
}
