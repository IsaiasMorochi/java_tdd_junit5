package imorochi.respositories;

import imorochi.mock.DATA;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class QuestionRepositoryImpl implements QuestionRepository {
    @Override
    public List<String> findQuestionByExamId(Long examId) {
        log.info("[QuestionRepositoryImpl] [findQuestionByExamId]");
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return DATA.QUESTIONS;
    }

    @Override
    public void saveAll(List<String> questions) {

    }
}
