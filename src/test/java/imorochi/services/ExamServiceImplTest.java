package imorochi.services;

import imorochi.mock.DATA;
import imorochi.models.Exam;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import imorochi.respositories.ExamRepository;
import imorochi.respositories.ExamRepositoryImpl;
import imorochi.respositories.QuestionRepository;
import imorochi.respositories.QuestionRepositoryImpl;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplTest {

    /*@Mock
    ExamRepository examRepository;

    @Mock
    QuestionRepository questionRepository;*/

    @Mock
    ExamRepositoryImpl examRepository;

    @Mock
    QuestionRepositoryImpl questionRepository;

    @InjectMocks //solo injecta implementaciones(class)
    ExamServiceImpl examService;


   /* Uso captor con DI
    @Captor
    ArgumentCaptor<Long> captor;*/

    @BeforeEach
    void setUp() {
        //MockitoAnnotations.openMocks(this);
        /*examRepository = imorochi.mock(ExamRepository.class);
        questionRepository = imorochi.mock(QuestionRepository.class);
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
        // solo se puede realizar imorochi.mock de los metodos publicos y default (mismo package)
        // No realiza imorochi.mock de metodos: private, static, final.
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        Optional<Exam> exam = examService.findByExamByName("Matemáticas");

        assertNotNull(exam);
        assertEquals(1L, exam.orElseThrow().getId());
        assertEquals("Matemáticas", exam.orElseThrow().getName());
    }

    @Test
    void findByExamByNameMockitoEmptyList() {
        List<Exam> data = Collections.emptyList();

        when(examRepository.findAll()).thenReturn(data);
        Optional<Exam> exam = examService.findByExamByName("Matemáticas");

        assertFalse(exam.isPresent());
    }

    @Test
    void testQuestionsExam() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);

        Optional<Exam> exam = Optional.of(examService.findExamByNameWithQuestions("Matemáticas"));
        assertTrue(exam.isPresent());
        assertEquals(5, exam.orElseThrow().getQuestions().size());
        assertTrue(exam.get().getQuestions().contains("aritmetica"));
    }

    @Test
    void testQuestionsExamVerify() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);

        Optional<Exam> exam = Optional.of(examService.findExamByNameWithQuestions("Matemáticas"));
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
        Exam exam = examService.findExamByNameWithQuestions("Matemáticas");

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
        Exam exam = examService.save(newExam);

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
            examService.findExamByNameWithQuestions("Matemáticas");
        });

        assertEquals(IllegalArgumentException.class, argumentException.getClass());
        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(isNull());
    }

    @Test
    void testArgumentMatchersOne() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);
        examService.findExamByNameWithQuestions("Matemáticas");

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(ArgumentMatchers.argThat(arg -> arg != null && arg.equals(1L)));
        verify(questionRepository).findQuestionByExamId(ArgumentMatchers.argThat(arg -> arg != null && arg >= 1L));
        verify(questionRepository).findQuestionByExamId(eq(1L));
    }

    @Test
    void testArgumentMatchersTwo() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS_ID_NEGATIVO);
        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);
        examService.findExamByNameWithQuestions("Matemáticas");

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

    @Test
    void testArgumentCaptor() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        examService.findExamByNameWithQuestions("Matemáticas");

        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        verify(questionRepository).findQuestionByExamId(captor.capture());

        assertEquals(1L, captor.getValue());
    }

    @Test
    void tesDoThrow() {
        Exam exam = DATA.EXAM;
        exam.setQuestions(DATA.QUESTIONS);

        // Util doThrow para imorochi.mock metodos void
        doThrow(IllegalArgumentException.class).when(questionRepository).saveAll(anyList());
        assertThrows(IllegalArgumentException.class, () -> {
            examService.save(exam);
        });
    }

    @Test
    void testDoAnswer() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);
        doAnswer(invocationOnMock -> {
            Long id = invocationOnMock.getArgument(0);
            return id == 1L ? DATA.QUESTIONS : Collections.emptyList();
        }).when(questionRepository).findQuestionByExamId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("Matemáticas");
        assertEquals(5, exam.getQuestions().size());
        assertEquals(1L, exam.getId());
        assertEquals("Matemáticas", exam.getName());
    }

    @Test
    void testdoAnswerSaveExam() {
        // Given
        Exam newExam = DATA.EXAM;
        newExam.setQuestions(DATA.QUESTIONS);

        doAnswer(new Answer<Exam>() {
            // cuando se ejecuta el metodo save() le asignamos un id simulado
            Long secuencia = 1L;

            @Override
            public Exam answer(InvocationOnMock invocationOnMock) throws Throwable {
                Exam exam = invocationOnMock.getArgument(0);
                exam.setId(secuencia++);
                return exam;
            }
        }).when(examRepository).save(any(Exam.class));

        // When
        Exam exam = examService.save(newExam);

        // Then
        assertNotNull(exam.getId());
        assertEquals(1L, exam.getId());
        assertEquals("Fisica", exam.getName());

        verify(examRepository).save(any(Exam.class));
        verify(questionRepository).saveAll(anyList());
    }

    @Test
    void testDoCallRealMethod() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);

        when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(DATA.QUESTIONS);
        doCallRealMethod().when(questionRepository).findQuestionByExamId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("Matemáticas");
        assertEquals(1L, exam.getId());
        assertEquals("Matemáticas", exam.getName());
    }

    /*
     * Mock : 100% simulado y todos sus metodos deben ser mockeados o simular con When Do
     * Spy : Simulamos con when y Do pero el resto realiza la llamada real al metodo que no definamos la simulacion
     *       requiere al metodo real de la clase concreta y no una interfaz sino daria null.
     *       Cuando se usa spy no usar when , usar do
     * */
    @Test
    void testSpy() {
        ExamRepository examRepository = spy(ExamRepositoryImpl.class);
        QuestionRepository questionRepository = spy(QuestionRepositoryImpl.class);

        ExamService examService = new ExamServiceImpl(examRepository, questionRepository);

        // mock
        List<String> questions = Arrays.asList("aritmetica");
        //when(questionRepository.findQuestionByExamId(anyLong())).thenReturn(questions);
        doReturn(questions).when(questionRepository).findQuestionByExamId(anyLong());

        Exam exam = examService.findExamByNameWithQuestions("Matemáticas");
        assertEquals(1, exam.getId());
        assertEquals("Matemáticas", exam.getName());
        assertEquals(1, exam.getQuestions().size());
        assertTrue(exam.getQuestions().contains("aritmetica"));

        verify(examRepository).findAll();
        verify(questionRepository).findQuestionByExamId(anyLong());
    }

    /*
     * Verifica el orden de ejecucion con InOrder
     */
    @Test
    void testOrderOfInvocation() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);

        examService.findExamByNameWithQuestions("Matemáticas");
        examService.findExamByNameWithQuestions("Lenguaje");

        InOrder inOrder = inOrder(questionRepository);
        inOrder.verify(questionRepository).findQuestionByExamId(1L);
        inOrder.verify(questionRepository).findQuestionByExamId(2L);
    }

    @Test
    void testOrderOfInvocationTwo() {
        when(examRepository.findAll()).thenReturn(DATA.EXAMS);

        examService.findExamByNameWithQuestions("Matemáticas"); // llama> findAll, findQuestionByExamId
        examService.findExamByNameWithQuestions("Lenguaje");

        InOrder inOrder = inOrder(examRepository, questionRepository);
        inOrder.verify(examRepository).findAll();
        inOrder.verify(questionRepository).findQuestionByExamId(1L);

        inOrder.verify(examRepository).findAll();
        inOrder.verify(questionRepository).findQuestionByExamId(2L);
    }

}