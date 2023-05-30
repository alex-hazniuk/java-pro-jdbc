package org.example.service;

import org.example.model.Topic;
import org.example.repository.dao.TopicRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TopicServiceTest {
    private static TopicService topicService;
    private static List<Topic> topics;

    @BeforeClass
    public static void beforeClass() {
        TopicRepository topicRepository = new TopicRepository() {
            @Override
            public boolean save(Topic topic) {
                return topics.add(topic);
            }

            @Override
            public Topic get(int id) {
                return topics.stream()
                        .filter(t -> t.getId() == id)
                        .findFirst()
                        .get();
            }

            @Override
            public boolean remove(int id) {
                return false;
            }

            @Override
            public int update(Topic topic) {
                return 0;
            }
        };
        topicService = new TopicService(topicRepository);
    }

    @Before
    public void setUp() {
        topics = topicsCreation();
    }

    @Test
    public void save_topic() {
        Topic topic = Topic.builder()
                .id(3)
                .name("Exception").build();
        assertTrue("Can't add topic! " + topic, topicService.save(topic));
        int actual = topics.size();
        int expected = 3;
        assertEquals(expected, actual);
    }

    @Test
    public void get_topic() {
        int topicId = 1;
        Topic actual = topicService.get(topicId);
        Topic expected = new Topic(1, "Unit tests");
        assertEquals(expected, actual);
    }

    @After
    public void tearDown() {
        topics = topicsCreation();
    }

    private List<Topic> topicsCreation() {
        List<Topic> topics = new ArrayList<>();
        topics.add(Topic.builder()
                .id(1)
                .name("Unit tests").build());
        topics.add(Topic.builder()
                .id(2)
                .name("Data bases").build());
        return topics;
    }
}
