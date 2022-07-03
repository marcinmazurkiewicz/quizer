package dev.mazurkiewicz.quizer.question.application;

import dev.mazurkiewicz.quizer.config.EndpointProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping(EndpointProperties.QUESTIONS_ENDPOINT_MAIN)
class QuestionResource {

    private final ApiQuestionService service;

    @GetMapping(EndpointProperties.QUESTIONS_ENDPOINT_RANDOM)
    QuestionResponse getRandomQuestion() {
        try {
            return service.getRandomQuestion();
        } catch (EmptyResultDataAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    QuestionResponse getQuestionById(@PathVariable("id") Integer id) {
        return service.getQuestionById(id);
    }

//    @GetMapping(EndpointProperties.QUESTIONS_ENDPOINT_EXAM)
//    public ExamResponse getExamData() {
//        try {
//            return service.getExamData();
//        } catch (IncorrectResultSizeDataAccessException e) {
//            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, e.getMessage());
//        }
//    }
//
//    @GetMapping(EndpointProperties.QUESTIONS_ENDPOINT_INFO)
//    public QuestionInfoResponse getQuestionNumber() {
//        return service.getQuestionsInfo();
//    }
//
//    @GetMapping(EndpointProperties.QUESTIONS_ENDPOINT_PDF)
//    public ResponseEntity<ByteArrayResource> generatePdf() {
//        String filename = quizerConfiguration.pdfName();
//        byte[] pdfBytes = new byte[0];
//        Timer.Sample timerSample = Timer.start();
//
//        try {
//            pdfBytes = service.getPdfTest();
//        } catch (PdfRenderException e) {
//            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
//        } finally {
//            boolean successfulGeneration = pdfBytes.length > 0;
//            Timer timer = buildTimer(filename, successfulGeneration);
//            timerSample.stop(timer);
//            log.info("PDF generated on {} seconds", timer.totalTime(TimeUnit.SECONDS));
//        }
//
//        ByteArrayResource resource = new ByteArrayResource(pdfBytes);
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentDisposition(
//                ContentDisposition
//                        .attachment()
//                        .filename(filename, StandardCharsets.UTF_8)
//                        .build()
//        );
//        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.setContentLength(resource.contentLength());
//        return new ResponseEntity<>(resource,
//                headers,
//                HttpStatus.OK);
//    }

//    private Timer buildTimer(String filename, Boolean success) {
//        return Timer.builder(MicrometerProperties.GENERATE_FILE_TIMER_NAME)
//                .tag(MicrometerProperties.GENERATE_FILE_TYPE_TAG, MediaType.APPLICATION_PDF_VALUE)
//                .tag(MicrometerProperties.GENERATE_FILE_NAME_TAG, filename)
//                .tag(MicrometerProperties.GENERATE_FILE_SUCCESS_TAG, success.toString())
//                .publishPercentileHistogram(true)
//                .minimumExpectedValue(Duration.ofMillis(1L))
//                .maximumExpectedValue(Duration.ofSeconds(30L))
//                .register(registry);
//    }
}

