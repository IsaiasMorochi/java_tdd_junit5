package services;

import mock.DATA;
import models.Exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import respositories.ExamRepository;
import respositories.ExamRepositoryImpl;
import respositories.QuestionRepository;
import respositories.QuestionRepositoryImpl;

import java.util.*;

import static org.mockito.Mockito.*;
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

        Optional<Exam> exam = Optional.of(service.findExamByNameWithQuestions("Matemáticas"));
        assertTrue(exam.isPresent());
        assertEquals(5, exam.orElseThrow().getQuestions().size());
        assertTrue(exam.get().getQuestions().contains("aritmetica"));
    }

    @Test
    void testQuestionsExamVerify() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);

        Optional<Exam> exam = Optional.of(service.findExamByNameWithQuestions("Matemáticas"));
        assertTrue(exam.isPresent());
        assertEquals(5, exam.orElseThrow().getQuestions().size());
        assertTrue(exam.get().getQuestions().contains("aritmetica"));

        // verificamos que si se llama a los metodos del repository
        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(1L);
    }

    @Test
    void testNotExistExamVerify() {
        // Given
        when(examRepository.findAll()).thenReturn(Collections.emptyList());
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);

        // When
        Exam exam = service.findExamByNameWithQuestions("Matemáticas");

        // Then
        assertNull(exam);
        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(1L);
    }

    @Test
    void testSaveExam() {
        // Given
        Exam newExam = DATA.EXAM;
        newExam.setQuestions(DATA.QUESTIONS);

        when(examRepository.save(any(Exam.class))).then(new Answer<Exam>() {
            // cuando se ejecuta el metodo save() le asignamos un id simulado
            Long secuencia = 1L;
            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(secuencia++);
                return exam;
            }
        });

        // When
        Exam exam = service.save(newExam);

        // Then
        assertNotNull(exam.getId());
        assertEquals(1L, exam.getId());
        assertEquals("Fisica", exam.getName());

        verify(examRepository).save(any(Exam.class));
        verify(questionRepository).saveAll(anyList());
    }

    @Test
    void testUseException() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS_ID_NULL);
        when(questionRepository.findQuestionByExamId(isNull())).thenThrow(IllegalArgumentException.class);

/*        assertThrows(IllegalArgumentException.class, () -> {
            service.findExamByNameWithQuestions("Matematicas");
        });*/
        IllegalArgumentException argumentException = assertThrows(IllegalArgumentException.class, () -> {
            service.findExamByNameWithQuestions("Matemáticas");
        });

        assertEquals(IllegalArgumentException.class, argumentException.getClass());
        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(isNull());
    }

    @Test
    void testArgumentMatchersOne() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);
        service.findExamByNameWithQuestions("Matemáticas");

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(ArgumentMatchers.argThat(arg -> arg != null && arg.equals(1L)));
        verify(questionRepository).findQuestionByExamId(ArgumentMatchers.argThat(arg -> arg != null && arg >= 1L));
        verify(questionRepository).findQuestionByExamId(eq(1L));
    }

    @Test
    void testArgumentMatchersTwo() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS_ID_NEGATIVO);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);
        service.findExamByNameWithQuestions("Matemáticas2");

        // Util para usar Matchers con mensaje personalizado
        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(argThat(new MiArgsMatchers()));
    }

    public static class MiArgsMatchers implements ArgumentMatcher<Long> {

        private Long argument;

        @Override
        public boolean matches(Long argument) {
            this.argument = argument;
            return argument != null && argument > 0;
        }

        @Override
        public String toString() {
            return "es para un mensaje personalizado de error que imprime mockito en caso de que falle el test <" +
                    argument + "> deber un entero positivo";
        }

    }

}