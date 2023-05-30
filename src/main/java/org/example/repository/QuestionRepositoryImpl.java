package org.example.repository;

import org.example.ConnectionSingleton;
import org.example.model.Question;
import org.example.repository.dao.QuestionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuestionRepositoryImpl implements QuestionRepository {
    private final static String SAVE =
            """
                    INSERT INTO question (test, topic_id)
                    VALUES (?, ?)
            """;

    private final static String GET =
            """     
                    SELECT * FROM question
                    WHERE id = ?
            """;

    private final static String REMOVE =
            """
                    DELETE FROM question
                    WHERE id = ?
            """;

    private final static String UPDATE =
            """
                    UPDATE question
                    SET text = ?, topic_id = ?
                    WHERE id = ?
            """;

    private final static String GET_ALL =
            """
                    SELECT * FROM question
            """;

    private final static String GET_ALL_BY_TOPIC =
            """
                    SELECT * FROM question
                    WHERE topic_id = ?
            """;

    private final Connection connection = ConnectionSingleton.getConnection();

    @Override
    public boolean save(Question question) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE);
            preparedStatement.setString(1, question.getText());
            preparedStatement.setInt(2, question.getTopicId());
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Can't save data " + question, e);
        }
    }

    @Override
    public Question get(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return getQuestion(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Can't get question by id: " + id, e);
        }
    }

    @Override
    public boolean remove(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE);
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Can't remove question by id: " + id, e);
        }
    }

    @Override
    public int update(Question question) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, question.getText());
            preparedStatement.setInt(2, question.getTopicId());
            preparedStatement.setInt(3, question.getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Can't update data " + question, e);
        }
    }

    @Override
    public List<Question> getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getQuestions(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Can't get all questions: ", e);
        }
    }

    @Override
    public List<Question> getAllByTopic(int topicId) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL_BY_TOPIC);
            preparedStatement.setInt(1, topicId);
            ResultSet resultSet = preparedStatement.executeQuery();
            return getQuestions(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException("Can't get all questions by topic where topic id: "
                    + topicId, e);
        }
    }

    private List<Question> getQuestions(ResultSet resultSet) throws SQLException {
        ArrayList<Question> questions = new ArrayList<>();
        while (resultSet.next()) {
            questions.add(getQuestion(resultSet));
        }
        return questions;
    }

    private Question getQuestion(ResultSet resultSet) throws SQLException {
        return Question.builder()
                .id(resultSet.getInt(1))
                .text(resultSet.getString(2))
                .topicId(resultSet.getInt(3))
                .build();
    }
}
