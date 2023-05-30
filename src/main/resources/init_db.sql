CREATE DATABASE `questionarium` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;
CREATE TABLE `question` (
                            `id` int NOT NULL AUTO_INCREMENT,
                            `text` varchar(45) NOT NULL,
                            `topic_id` int DEFAULT NULL,
                            PRIMARY KEY (`id`),
                            KEY `question_topic_fk_idx` (`topic_id`),
                            CONSTRAINT `question_topic_fk` FOREIGN KEY (`topic_id`) REFERENCES `topic` (`id`)
                                ON DELETE SET NULL ON UPDATE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE TABLE `topic` (
                         `id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(45) NOT NULL,
                         PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

