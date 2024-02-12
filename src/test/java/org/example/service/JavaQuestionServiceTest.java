package org.example.service;

import org.example.domain.Question;
import org.example.exception.QuestionAlreadyAddedException;
import org.example.exception.QuestionEmptyException;
import org.example.exception.QuestionNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class JavaQuestionServiceTest {

    private final QuestionService questionService = new JavaQuestionService();

    @AfterEach
    public void afterEach() {
        Collection<Question> questions = new ArrayList<>(questionService.getAll());
        for (Question question : questions) {
            questionService.remove(question);
        }
    }

    @BeforeEach
    public void beforeEach() {
        for (int i = 1; i <= 5; i++) {
            questionService.add(new Question("Question " + i, "Answer " + i));
        }
    }

    @Test
    public void addPositiveTest() {
        int before = questionService.getAll().size();
        Question expected = new Question("Question 6", "Answer 6");
        assertThat(questionService.add("Question 6", "Answer 6")).isEqualTo(expected);
        assertThat(questionService.getAll())
                .hasSize(before + 1)
                .contains(expected);

        expected = new Question("Question 7", "Answer 7");
        assertThat(questionService.add(expected)).isEqualTo(expected);
        assertThat(questionService.getAll())
                .hasSize(before + 2)
                .contains(expected);
    }

    @Test
    public void addNegativeTest() {
        assertThat(questionService.getAll())
                .contains(new Question("Question 5", "Answer 5"));
        assertThatExceptionOfType(QuestionAlreadyAddedException.class)
                .isThrownBy(() -> questionService.add("Question 5", "Answer 5"));
        assertThatExceptionOfType(QuestionAlreadyAddedException.class)
                .isThrownBy(() -> questionService.add(new Question("Question 5", "Answer 5")));
    }

    @Test
    public void removePositiveTest() {
        int before = questionService.getAll().size();
        Question expected = new Question("Question 5", "Answer 5");
        assertThat(questionService.remove(new Question("Question 5", "Answer 5"))).isEqualTo(expected);
        assertThat(questionService.getAll())
                .hasSize(before - 1)
                .doesNotContain(expected);
    }

    @Test
    public void removeNegativeTest() {
        assertThat(questionService.getAll())
                .doesNotContain(new Question("Question 6", "Answer 6"));
        assertThatExceptionOfType(QuestionNotFoundException.class)
                .isThrownBy(() -> questionService.remove(new Question("Question 6", "Answer 6")));
    }

    @Test
    public void getAllTest() {
        assertThat(questionService.getAll())
                .hasSize(5)
                .containsExactlyInAnyOrder(
                        new Question("Question 1", "Answer 1"),
                        new Question("Question 2", "Answer 2"),
                        new Question("Question 3", "Answer 3"),
                        new Question("Question 4", "Answer 4"),
                        new Question("Question 5", "Answer 5")
                );
    }

    @Test
    public void getRandomQuestionPositiveTest() {
        Question actual = questionService.getRandomQuestion();
        assertThat(actual).isIn(questionService.getAll());
    }

    @Test
    public void getRandomQuestionNegativeTest() {
        afterEach();
        assertThatExceptionOfType(QuestionEmptyException.class)
                .isThrownBy(() -> questionService.getRandomQuestion());
    }
}