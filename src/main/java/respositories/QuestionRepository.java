package respositories;

import java.util.List;

public interface QuestionRepository {
    List<String> findQuestionByExamId(Long examId);
}
