package imorochi.services;

import imorochi.models.Exam;
import imorochi.respositories.ExamRepository;
import imorochi.respositories.QuestionRepository;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

@Slf4j
public class ExamServiceImpl implements ExamService {

    ExamRepository examRepository;
    QuestionRepository questionRepository;

    public ExamServiceImpl(ExamRepository examRepository, QuestionRepository questionRepository) {
        this.examRepository = examRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public Optional<Exam> findByExamByName(String name) {
        log.info("[ExamServiceImpl] [findByExamByName]");
        return examRepository.findAll()
                .stream()
                .filter(exam -> exam.getName().contains(name))
                .findFirst();
    }

    @Override
    public Exam findExamByNameWithQuestions(String nameExam) {
        log.info("[ExamServiceImpl] [findExamByNameWithQuestions]");
        Optional<Exam> examOptional = findByExamByName(nameExam);
        Exam exam = null;
        if (examOptional.isPresent()) {
            exam = examOptional.orElseThrow();
            List<String> questions = questionRepository.findQuestionByExamId(examOptional.get().getId());
            exam.setQuestions(questions);
        }
        return exam;
    }

    @Override
    public Exam findExamByNameWithQuestionsTwoCall(String nameExam) {
        log.info("[ExamServiceImpl] [findExamByNameWithQuestionsTwoCall]");
        Optional<Exam> examOptional = findByExamByName(nameExam);
        Exam exam = null;
        if (examOptional.isPresent()) {
            exam = examOptional.orElseThrow();
            List<String> questions = questionRepository.findQuestionByExamId(examOptional.get().getId());
            questionRepository.findQuestionByExamId(examOptional.get().getId());
            exam.setQuestions(questions);
        }
        return exam;
    }

    @Override
    public Exam save(Exam exam) {
        if (!exam.getQuestions().isEmpty()) {
            questionRepository.saveAll(exam.getQuestions());
        }
        return examRepository.save(exam);
    }

}
