package services;

import mock.DATA;
import models.Exam;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import respositories.ExamRepository;
import respositories.ExamRepositoryImpl;
import respositories.QuestionRepository;
import respositories.QuestionRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {
    @Mock
    ExamRepository examRepository;

    @Mock
    QuestionRepository questionRepository;

    @InjectMocks //solo injecta implementaciones(class)
    ExamServiceImpl service;

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        /*examRepository = mock(ExamRepository.class);
        questionRepository = mock(QuestionRepository.class);
        service = new ExamServiceImpl(examRepository, questionRepository);*/
    }

    @Test
    void findByExamByNameJunit() {
        ExamRepository repository = new ExamRepositoryImpl();
        QuestionRepository questionRepository1 = new QuestionRepositoryImpl();
        ExamService examService = new ExamServiceImpl(repository, questionRepository1);
        Optional<Exam> exam = examService.findByExamByName("Matemáticas");
        assertTrue(exam.isPresent());
        assertEquals(1L, exam.orElseThrow().getId());
        assertEquals("Matemáticas", exam.orElseThrow().getName());
    }

    @Test
    void findByExamByNameMockito() {
        // solo se puede realizar mock de los metodos publicos y default (mismo package)
        // No realiza mock de metodos: private, static, final.
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        Optional<Exam> exam = service.findByExamByName("Matemáticas");

        assertNotNull(exam);
        assertEquals(1L, exam.orElseThrow().getId());
        assertEquals("Matemáticas", exam.orElseThrow().getName());
    }

    @Test
    void findByExamByNameMockitoEmptyList() {
        List<Exam> data = Collections.emptyList();

        when(examRepository.findAll()).thenReturn(data);
        Optional<Exam> exam = service.findByExamByName("Matemáticas");

        assertFalse(exam.isPresent());
    }

    @Test
    void testQuestionsExam() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);

        Optional<Exam> exam = service.findExamByNameWithQuestions("Matemáticas");
        assertTrue(exam.isPresent());
        assertEquals(5, exam.orElseThrow().getQuestions().size());
        assertTrue(exam.get().getQuestions().contains("aritmetica"));
    }

    @Test
    void testQuestionsExamVerify() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);

        Optional<Exam> exam = service.findExamByNameWithQuestions("Matemáticas");
        assertTrue(exam.isPresent());
        assertEquals(5, exam.orElseThrow().getQuestions().size());
        assertTrue(exam.get().getQuestions().contains("aritmetica"));

        // verificamos que si se llama a los metodos del repository
        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(1L);
    }

    @Test
    void testNotExistExamVerify() {
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);
        Optional<Exam> exam = service.findExamByNameWithQuestions("Matemáticas");
        assertNull(exam);

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(1L);
    }
}