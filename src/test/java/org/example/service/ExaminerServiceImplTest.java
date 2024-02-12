package org.example.service;

import org.example.domain.Question;
import org.example.exception.ManyQuestionsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ExaminerServiceImplTest {

    @Mock
    private JavaQuestionService javaQuestionService;

    @InjectMocks
    private ExaminerServiceImpl examinerService;

    private final List<Question> questions = List.of(
            new Question("question1", "answer1"),
            new Question("question2", "answer2"),
            new Question("question3", "answer3"),
            new Question("question4", "answer4"),
            new Question("question5", "answer5")
    );

    public void beforeEasch() {
        when(javaQuestionService.getAll()).thenReturn(questions);
    }

    @Test
    public void getQuestionPositiveTest() {
        when(javaQuestionService.getAll()).thenReturn(questions);

        assertThatExceptionOfType(ManyQuestionsException.class)
                .isThrownBy(() -> examinerService.getQuestions(questions.size() + 1));
    }

    @Test
    public void getQuestionNegativeTest() {
        when(javaQuestionService.getAll()).thenReturn(questions);

        when(javaQuestionService.getRandomQuestion())
                .thenReturn(
                        new Question("question1", "answer1"),
                        new Question("question2", "answer2"),
                        new Question("question2", "answer2"),
                        new Question("question4", "answer4"),
                        new Question("question3", "answer3"),
                        new Question("question5", "answer5")
                );

        assertThat(examinerService.getQuestions(5))
                .containsExactlyInAnyOrder(
                        new Question("question1", "answer1"),
                        new Question("question2", "answer2"),
                        new Question("question3", "answer3"),
                        new Question("question4", "answer4"),
                        new Question("question5", "answer5")
                );
    }

}