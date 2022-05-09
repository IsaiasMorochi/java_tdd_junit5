package respositories;

import java.util.Collections;
import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository {
    @Override
    public List<String> findQuestionByExamId(Long examId) {
        return Collections.emptyList();
    }
}
