package services;

import models.Exam;
import org.junit.jupiter.api.Test;
// import org.mockito.Mockito;
import static org.mockito.Mockito.*;
import respositories.ExamRepository;
import respositories.ExamRepositoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class ExamServiceImplTest {

    @Test
    void findByExamByNameJunit() {
        ExamRepository repository = new ExamRepositoryImpl();
        ExamService service = new ExamServiceImpl(repository);
        Optional<Exam> exam = service.findByExamByName("Matemáticas");
        assertTrue(exam.isPresent());
        assertEquals(1L, exam.orElseThrow().getId());
        assertEquals("Matemáticas", exam.orElseThrow().getName());
    }

    @Test
    void findByExamByNameMockito() {
        ExamRepository repository = mock(ExamRepository.class);
        ExamService service = new ExamServiceImpl(repository);
        List<Exam> data = Arrays.asList(
                new Exam(1L, "Matemáticas"),
                new Exam(2L, "Lenguaje"),
                new Exam(3L, "Fisica"));

        // solo se puede realizar mock de los metodos publicos y default (mismo package)
        // No realiza mock de metodos: private, static, final.
        when(repository.findAll()).thenReturn(data);
        Optional<Exam> exam = service.findByExamByName("Matemáticas");

        assertNotNull(exam);
        assertEquals(1L, exam.orElseThrow().getId());
        assertEquals("Matemáticas", exam.orElseThrow().getName());
    }

    @Test
    void findByExamByNameMockitoEmptyList() {
        ExamRepository repository = mock(ExamRepository.class);
        ExamService service = new ExamServiceImpl(repository);
        List<Exam> data = Collections.emptyList();

        when(repository.findAll()).thenReturn(data);
        Optional<Exam> exam = service.findByExamByName("Matemáticas");

        assertTrue(exam.isPresent());
        assertEquals(1L, exam.orElseThrow().getId());
        assertEquals("Matemáticas", exam.get().getName());
    }
}