package imorochi.respositories;

import imorochi.mock.DATA;

import java.util.Collections;
import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository {
    @Override
    public List<String> findQuestionByExamId(Long examId) {
        return DATA.QUESTIONS;
    }

    @Override
    public void saveAll(List<String> questions) {

    }
}
