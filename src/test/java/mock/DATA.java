package mock;

import models.Exam;

import java.util.Arrays;
import java.util.List;

public class DATA {
    public final static List<Exam> EXAMS = Arrays.asList(
            new Exam(1L, "Matemáticas"),
            new Exam(2L, "Lenguaje"),
            new Exam(3L, "Fisica"));

    public final static List<String> QUESTIONS = Arrays.asList(
            "aritmetica", "integrales", "derivadas", "trigonometria", "geometria");

    public final static Exam EXAM = new Exam(null, "Fisica");
}
