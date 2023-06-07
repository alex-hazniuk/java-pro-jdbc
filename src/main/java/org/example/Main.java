package org.example;

import org.example.repository.QuestionRepositoryImpl;
import org.example.repository.TopicRepositoryImpl;
import org.example.repository.dao.QuestionRepository;
import org.example.repository.dao.TopicRepository;
import org.example.service.QuestionService;
import org.example.service.TopicService;

import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println(
                """
                        Glad to see you in our questionarium!
                        Choose the command bellow by number:
                        """
        );
        Arrays.stream(Command.values())
                .forEach(c -> System.out.println(c + " " + c.getTitle()));
        init(scanner)
                .getOrDefault(scanner.nextLine(),
                        () -> System.out.println("Incorrect command"))
                .run();
    }

    private static Map<String, Runnable> init(Scanner scanner) {
        TopicRepository topicRepository = new TopicRepositoryImpl();
        QuestionRepository questionRepository = new QuestionRepositoryImpl();
        Shell shell = new Shell(
                new TopicService(topicRepository),
                new QuestionService(questionRepository),
                scanner
        );
        return Map.of(
                Command.GET_QUESTION_BY_TOPIC.getTitle(), shell.getRandomQuestionByTopicName(),
                Command.GET_RANDOM_QUESTION.getTitle(), shell.getRandomQuestion(),
                Command.ADD_QUESTION.getTitle(), shell.addQuestion(),
                Command.REMOVE_QUESTION.getTitle(), shell.removeQuestion(),
                Command.GET_TOPIC.getTitle(), shell.getTopic(),
                Command.ADD_TOPIC.getTitle(), shell.addTopic()
        );
    }
}
