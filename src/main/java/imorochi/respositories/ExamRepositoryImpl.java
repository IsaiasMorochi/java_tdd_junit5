package imorochi.respositories;

import imorochi.mock.DATA;
import imorochi.models.Exam;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class ExamRepositoryImpl implements ExamRepository {

    @Override
    public List<Exam> findAll() {
        log.info("[ExamRepositoryImpl] [findAll]");
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return DATA.EXAMS;
    }

    @Override
    public Exam save(Exam exam) {
        log.info("[ExamRepositoryImpl] [save]");
        return DATA.EXAM;
    }

}
