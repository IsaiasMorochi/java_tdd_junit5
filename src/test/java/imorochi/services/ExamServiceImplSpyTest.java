package imorochi.services;

import imorochi.mock.DATA;
import imorochi.models.Exam;
import imorochi.respositories.ExamRepository;
import imorochi.respositories.ExamRepositoryImpl;
import imorochi.respositories.QuestionRepository;
import imorochi.respositories.QuestionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExamServiceImplSpyTest {


    @Spy
    ExamRepositoryImpl examRepository;

    @Spy
    QuestionRepositoryImpl questionRepository;

    @InjectMocks //solo injecta implementaciones(class)
    ExamServiceImpl examService;


    /*
    * Mock : 100% simulado y todos sus metodos deben ser mockeados o simular con When Do
    * Spy : Simulamos con when y Do pero el resto realiza la llamada real al metodo que no definamos la simulacion
    *       requiere al metodo real de la clase concreta y no una interfaz sino daria null.
    *       Cuando se usa spy no usar when , usar do
    * */
    @Test
    void testSpy() {
        // mock
        List<String> questions = Arrays.asList("aritmetica");
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