package org.example.service;

import org.example.domain.Question;
import org.example.exception.QuestionAlreadyAddedException;
import org.example.exception.QuestionEmptyException;
import org.example.exception.QuestionNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JavaQuestionService implements QuestionService {

//    private static final int MAX_QUESTIONS = 5;

    private final Collection<Question> questions = new HashSet<>();
    private final Random random = new Random();

    @Override
    public Question add(String question, String answer) {
        return add(new Question(question, answer));
    }

    @Override
    public Question add(Question question) {
        if (!questions.add(question)) {
            throw new QuestionAlreadyAddedException();
        }
        return question;
    }

    @Override
    public Question remove(Question question) {
        if (!questions.remove(question)) {
            throw new QuestionNotFoundException();
        }
        return question;
    }

    @Override
    public Collection<Question> getAll() {
        return Collections.unmodifiableCollection(questions);
    }

    @Override
    public Question getRandomQuestion() {
        if (questions.isEmpty()) {
            throw new QuestionEmptyException();
        }
        int count = random.nextInt(questions.size());
        int counter = 0;
        for (Question question : questions) {
            if (counter == count) {
                return question;
            }
            counter++;
        }
        return null;
    }
}
