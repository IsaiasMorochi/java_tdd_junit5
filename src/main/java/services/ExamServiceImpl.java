package services;

import models.Exam;
import respositories.ExamRepository;
import respositories.QuestionRepository;

import java.util.List;
import java.util.Optional;

public class ExamServiceImpl implements ExamService {

    ExamRepository examRepository;
    QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Exam> findByExamByName(String name) {
        return examRepository.findAll()
                .stream()
                .filter(exam -> exam.getName().contains(name))
                .findFirst();
    }

    @Override
    public Optional<Exam> findExamByNameWithQuestions(String nameExam) {
        Optional<Exam> examOptional = findByExamByName(nameExam);
        if (examOptional.isPresent()) {
            List<String> questions = questionRepository.findQuestionByExamId(examOptional.get().getId());
            examOptional.get().setQuestions(questions);
        }
        return examOptional;
    }
}
