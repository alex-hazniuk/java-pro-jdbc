package org.example.repository;

import org.example.ConnectionSingleton;
import org.example.exception.*;
import org.example.model.Topic;
import org.example.repository.dao.TopicRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TopicRepositoryImpl implements TopicRepository {
    private static final String SAVE = "INSERT INTO topic (name) VALUES (?)";

    private static final String GET = "SELECT * FROM topic WHERE id = ?";

    private static final String REMOVE = "DELETE FROM topic WHERE id = ?";

    private static final String UPDATE = "UPDATE topic SET name = ? WHERE id = ?";

    private static final String GET_ALL = "SELECT * FROM topic";

    private final Connection connection = ConnectionSingleton.getConnection();

    @Override
    public Topic save(Topic topic) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE,
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, topic.getName());
            preparedStatement.execute();
            ResultSet resultSet = preparedStatement.getGeneratedKeys();
            resultSet.next();
            topic.setId(resultSet.getInt(1));
            return topic;
        } catch (SQLException e) {
            throw new SaveDataException("Can't save data " + topic, e);
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
            throw new GetDataException("Can't get topic by id: " + id, e);
        }
    }

    @Override
    public boolean remove(int id) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(REMOVE);
            preparedStatement.setInt(1, id);
            return preparedStatement.execute();
        } catch (SQLException e) {
            throw new RemoveDataException("Can't remove topic by id: " + id, e);
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
            throw new UpdateDataException("Can't update data " + topic, e);
        }
    }

    @Override
    public List<Topic> getAll() {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(GET_ALL);
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Topic> topics = new ArrayList<>();
            while (resultSet.next()) {
                topics.add(Topic.builder()
                        .id(resultSet.getInt(1))
                        .name(resultSet.getString(2))
                        .build());
            }
            return topics;
        } catch (SQLException e) {
            throw new GetAllDataException("Can't get all topics: ", e);
        }
    }
}
