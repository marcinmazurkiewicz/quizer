package dev.mazurkiewicz.quizer.questions;

import dev.mazurkiewicz.quizer.TestBeanConfig;
import dev.mazurkiewicz.quizer.config.QuizerConfiguration;
import dev.mazurkiewicz.quizer.exception.PdfRenderException;
import dev.mazurkiewicz.quizer.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static dev.mazurkiewicz.quizer.config.EndpointProperties.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Import(TestBeanConfig.class)
@WebMvcTest(controllers = QuestionController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
class QuestionControllerTest {

    private static final List<QuestionResponse> questionResponses = new ArrayList<>();
    @Autowired
    private MockMvc mvc;
    @Autowired
    private QuizerConfiguration properties;
    @MockBean
    private QuestionService service;

    // questions with an even id have pictures
    @BeforeAll
    private static void prepareQuestionResponses() {
        for (int i = 1; i < 11; i++) {
            questionResponses.add(prepareQuestion(i, i % 2 == 0));
        }
    }

    private static QuestionResponse prepareQuestion(int questionNo, boolean withImg) {
        return new QuestionResponse(questionNo, String.format("Question %d", questionNo),
                Arrays.asList(
                        new Answer(AnswerType.A, String.format("answer A for question %d", questionNo)),
                        new Answer(AnswerType.B, String.format("answer B for question %d", questionNo)),
                        new Answer(AnswerType.C, String.format("answer C for question %d", questionNo)),
                        new Answer(AnswerType.D, String.format("answer D for question %d", questionNo))),
                (withImg ? String.format("/resources/img/img_%d.png", questionNo) : null)
        );
    }

    @Test
    void shouldReturnRandomQuestionResponseWithImgWhenCallGetRandomQuestion() throws Exception {
        when(service.getRandomQuestion()).thenReturn(questionResponses.get(1));

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_RANDOM))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.content", is("Question 2")))
                .andExpect(jsonPath("$.answers", hasSize(4)))
                .andExpect(jsonPath("$.answers[0].value", is("A")))
                .andExpect(jsonPath("$.answers[0].content", is("answer A for question 2")))
                .andExpect(jsonPath("$.answers[1].value", is("B")))
                .andExpect(jsonPath("$.answers[1].content", is("answer B for question 2")))
                .andExpect(jsonPath("$.answers[2].value", is("C")))
                .andExpect(jsonPath("$.answers[2].content", is("answer C for question 2")))
                .andExpect(jsonPath("$.answers[3].value", is("D")))
                .andExpect(jsonPath("$.answers[3].content", is("answer D for question 2")))
                .andExpect(jsonPath("$.img", is("/resources/img/img_2.png")));
    }

    @Test
    void shouldReturnRandomQuestionResponseWithoutImgPropertyWhenRandomQuestionImgIsNull() throws Exception {
        when(service.getRandomQuestion()).thenReturn(questionResponses.get(0));

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_RANDOM))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Question 1")))
                .andExpect(jsonPath("$.answers", hasSize(4)))
                .andExpect(jsonPath("$.answers[0].value", is("A")))
                .andExpect(jsonPath("$.answers[0].content", is("answer A for question 1")))
                .andExpect(jsonPath("$.answers[1].value", is("B")))
                .andExpect(jsonPath("$.answers[1].content", is("answer B for question 1")))
                .andExpect(jsonPath("$.answers[2].value", is("C")))
                .andExpect(jsonPath("$.answers[2].content", is("answer C for question 1")))
                .andExpect(jsonPath("$.answers[3].value", is("D")))
                .andExpect(jsonPath("$.answers[3].content", is("answer D for question 1")))
                .andExpect(jsonPath("$.img").doesNotExist());
    }

    @Test
    void shouldReturnStatus503WhenServiceThrowEmptyResultException() throws Exception {
        when(service.getRandomQuestion()).thenThrow(EmptyResultDataAccessException.class);

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_RANDOM))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void shouldReturnQuestionResponseWhenCallGetQuestionById() throws Exception {
        when(service.getQuestionById(2)).thenReturn(questionResponses.get(1));

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + "/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(2)))
                .andExpect(jsonPath("$.content", is("Question 2")))
                .andExpect(jsonPath("$.answers", hasSize(4)))
                .andExpect(jsonPath("$.answers[0].value", is("A")))
                .andExpect(jsonPath("$.answers[0].content", is("answer A for question 2")))
                .andExpect(jsonPath("$.answers[1].value", is("B")))
                .andExpect(jsonPath("$.answers[1].content", is("answer B for question 2")))
                .andExpect(jsonPath("$.answers[2].value", is("C")))
                .andExpect(jsonPath("$.answers[2].content", is("answer C for question 2")))
                .andExpect(jsonPath("$.answers[3].value", is("D")))
                .andExpect(jsonPath("$.answers[3].content", is("answer D for question 2")))
                .andExpect(jsonPath("$.img", is("/resources/img/img_2.png")));
    }

    @Test
    void shouldReturnQuestionResponseWithoutImgPropertyWhenQuestionImgIsNull() throws Exception {
        when(service.getQuestionById(1)).thenReturn(questionResponses.get(0));

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + "/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.content", is("Question 1")))
                .andExpect(jsonPath("$.answers", hasSize(4)))
                .andExpect(jsonPath("$.answers[0].value", is("A")))
                .andExpect(jsonPath("$.answers[0].content", is("answer A for question 1")))
                .andExpect(jsonPath("$.answers[1].value", is("B")))
                .andExpect(jsonPath("$.answers[1].content", is("answer B for question 1")))
                .andExpect(jsonPath("$.answers[2].value", is("C")))
                .andExpect(jsonPath("$.answers[2].content", is("answer C for question 1")))
                .andExpect(jsonPath("$.answers[3].value", is("D")))
                .andExpect(jsonPath("$.answers[3].content", is("answer D for question 1")))
                .andExpect(jsonPath("$.img").doesNotExist());
    }

    @Test
    void shouldReturnStatus404WhenServiceThrowResourceNotFoundException() throws Exception {
        when(service.getQuestionById(anyInt())).thenThrow(ResourceNotFoundException.class);

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + "/11"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnQuestionNumberResponse() throws Exception {
        when(service.getQuestionsInfo())
                .thenReturn(new QuestionInfoResponse(100L, 2010, 2020));

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_INFO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionNumber", is(100)))
                .andExpect(jsonPath("$.earliestQuestionYear", is(2010)))
                .andExpect(jsonPath("$.latestQuestionYear", is(2020)));
    }

    @Test
    void shouldReturnQuestionNumberResponseWhenYearAreNulls() throws Exception {
        when(service.getQuestionsInfo())
                .thenReturn(new QuestionInfoResponse(0, null, null));

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_INFO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.questionNumber", is(0)))
                .andExpect(jsonPath("$.earliestQuestionYear").doesNotExist())
                .andExpect(jsonPath("$.latestQuestionYear").doesNotExist());
    }


    @Test
    void shouldReturnListOfQuestionResponseWhenCallGetQuestionToTest() throws Exception {
        when(service.getExamData()).thenReturn(new ExamResponse(new ExamTimer(10, 0), questionResponses));

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_EXAM))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.timer.minutes", is(10)))
                .andExpect(jsonPath("$.timer.seconds", is(0)))
                .andExpect(jsonPath("$.questions", hasSize(10)))
                .andExpect(jsonPath("$.questions[0].content", is("Question 1")))
                .andExpect(jsonPath("$.questions[0].answers", hasSize(4)))
                .andExpect(jsonPath("$.questions[0].answers[0].value", is("A")))
                .andExpect(jsonPath("$.questions[0].answers[0].content", is("answer A for question 1")))
                .andExpect(jsonPath("$.questions[0].answers[1].value", is("B")))
                .andExpect(jsonPath("$.questions[0].answers[1].content", is("answer B for question 1")))
                .andExpect(jsonPath("$.questions[0].answers[2].value", is("C")))
                .andExpect(jsonPath("$.questions[0].answers[2].content", is("answer C for question 1")))
                .andExpect(jsonPath("$.questions[0].answers[3].value", is("D")))
                .andExpect(jsonPath("$.questions[0].answers[3].content", is("answer D for question 1")))
                .andExpect(jsonPath("$.questions[0].img").doesNotExist())
                .andExpect(jsonPath("$.questions[9].id", is(10)))
                .andExpect(jsonPath("$.questions[9].content", is("Question 10")))
                .andExpect(jsonPath("$.questions[9].answers", hasSize(4)))
                .andExpect(jsonPath("$.questions[9].answers[0].value", is("A")))
                .andExpect(jsonPath("$.questions[9].answers[0].content", is("answer A for question 10")))
                .andExpect(jsonPath("$.questions[9].answers[1].value", is("B")))
                .andExpect(jsonPath("$.questions[9].answers[1].content", is("answer B for question 10")))
                .andExpect(jsonPath("$.questions[9].answers[2].value", is("C")))
                .andExpect(jsonPath("$.questions[9].answers[2].content", is("answer C for question 10")))
                .andExpect(jsonPath("$.questions[9].answers[3].value", is("D")))
                .andExpect(jsonPath("$.questions[9].answers[3].content", is("answer D for question 10")))
                .andExpect(jsonPath("$.questions[9].img", is("/resources/img/img_10.png")));
    }

    @Test
    void shouldReturnStatus503WhenServiceThrowIncorrectResultSizeException() throws Exception {
        when(service.getExamData()).thenThrow(IncorrectResultSizeDataAccessException.class);

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_EXAM))
                .andExpect(status().isServiceUnavailable());
    }

    @Test
    void shouldReturnByteArrayWhenCallGetPdfTest() throws Exception {
        byte[] returnedBytesArray = "mock content".getBytes();
        when(service.getPdfTest()).thenReturn(returnedBytesArray);

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_PDF))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", MediaType.APPLICATION_PDF_VALUE))
                .andExpect(header().string("Content-Length", String.valueOf(returnedBytesArray.length)))
                .andExpect(header().string("Content-Disposition", "attachment; filename*=UTF-8''" + properties.pdfName()))
                .andExpect(content().bytes(returnedBytesArray));
    }

    @Test
    void shouldReturnStatus503WhenServiceThrownPdfRenderException() throws Exception {
        when(service.getPdfTest()).thenThrow(PdfRenderException.class);

        mvc.perform(get(QUESTIONS_ENDPOINT_MAIN + QUESTIONS_ENDPOINT_PDF))
                .andExpect(status().isInternalServerError());
    }
}