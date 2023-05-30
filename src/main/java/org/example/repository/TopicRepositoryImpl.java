package org.example.repository;

import org.example.ConnectionSingleton;
import org.example.model.Topic;
import org.example.repository.dao.TopicRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TopicRepositoryImpl implements TopicRepository {
    private static final String SAVE = "INSERT INTO topic (name) VALUES (?)";

    private static final String GET = "SELECT * FROM topic WHERE id = ?";

    private static final String REMOVE = "DELETE FROM topic WHERE id = ?";

    private static final String UPDATE = "UPDATE topic SET name = ? WHERE id = ?";

    private final Connection connection = ConnectionSingleton.getConnection();

    @Override
    public boolean save(Topic topic) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE);
            preparedStatement.setString(1, topic.getName());
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Can't save data " + topic, e);
        }
    }

    @Override
    public Topic get(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            return Topic.builder()
                    .id(resultSet.getInt("id"))
                    .name(resultSet.getString("name"))
                    .build();
        } catch (SQLException e) {
            throw new RuntimeException("Can't get topic by id: " + id, e);
        }
    }

    @Override
    public boolean remove(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE);
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException("Can't remove topic by id: " + id, e);
        }
    }

    @Override
    public int update(Topic topic) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(UPDATE);
            preparedStatement.setString(1, topic.getName());
            preparedStatement.setInt(2, topic.getId());
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Can't update data " + topic, e);
        }
    }
}
