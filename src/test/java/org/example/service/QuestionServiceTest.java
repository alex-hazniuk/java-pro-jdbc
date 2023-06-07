package org.example.service;

import org.example.model.Question;
import org.example.repository.dao.QuestionRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class QuestionServiceTest {

    private static QuestionService questionService;
    private static List<Question> questions;

    @BeforeClass
    public static void beforeClass() {
        QuestionRepository repository = new QuestionRepository() {
            @Override
            public Question save(Question question) {
                questions.add(question);
                return question;
            }

            @Override
            public Question get(int id) {
                return null;
            }

            @Override
            public boolean remove(int id) {
                for (Question question : questions) {
                    if (question.getId() == id) {
                        return questions.remove(question);
                    }
                }
                return false;
            }

            @Override
            public boolean update(Question question) {
                questions.set(question.getId() - 1, question);
                return questions.contains(question);
            }

            @Override
            public List<Question> getAll() {
                return questions;
            }

            @Override
            public List<Question> getAllByTopic(int topicId) {
                return questions.stream()
                        .filter(q -> q.getTopicId() == topicId)
                        .toList();
            }

            @Override
            public List<Question> getAllByTopicName(String topicName) {
                return null;
            }
        };
        questionService = new QuestionService(repository);
    }

    @Before
    public void setUp() {
        questions = new ArrayList<>();
        questions.add(Question.builder()
                .id(1)
                .text("Why we need unit test?")
                .topicId(1).build());
        questions.add(Question.builder()
                .id(2)
                .text("What is relational data base?")
                .topicId(1).build());
    }

    @Test
    public void save_question() {
        Question question = Question.builder()
                .id(3)
                .text("What is collision?")
                .topicId(2).build();
        questions.add(question);
        int actual = questions.size();
        int expected = 3;
        assertEquals(expected, actual);
        assertEquals(question, questions.get(questions.size() - 1));
    }

    @Test
    public void remove_questionByQuestionId() {
        int questionId = 2;
        assertTrue("Invalid question id: " + questionId,
                questionService.remove(questionId));
        int actual = questions.size();
        int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void update_question() {
        Question question = Question.builder()
                .id(1)
                .text("Why we need integrity tests?")
                .topicId(1).build();
        assertTrue(questionService.update(question));
        int actual = questions.size();
        int expected = 2;
        assertEquals(expected, actual);
    }

    @Test
    public void getAll_questions() {
        List<Question> expected = List.of(
                Question.builder()
                        .id(1)
                        .text("Why we need unit test?")
                        .topicId(1).build(),
                Question.builder()
                        .id(2)
                        .text("What is relational data base?")
                        .topicId(1).build()
        );
        List<Question> actual = questionService.getAll();
        assertEquals(expected, actual);
    }

    @Test
    public void get_random() {
        System.out.println(questionService.getRandom());
    }

    @Test
    public void get_randomByTopic() {
        int topicId = 1;
        System.out.println(questionService.getRandomByTopic(topicId));
    }
}
