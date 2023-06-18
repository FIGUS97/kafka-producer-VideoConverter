package pl.orkuz.producer.services;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.jmx.export.annotation.ManagedOperationParameters;
import org.springframework.jmx.export.annotation.ManagedResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@ManagedResource(objectName = "ProducerStats:category=Producer,name=testProducer")
public class InputController {
    private Counter videosSent;
    @Autowired
    public ProcessingService processingService;

    public static Boolean waitDisplay;
    public static Boolean downloadDisplay;

    @Value("${spring.application.name}")
    String appName;

    public InputController(MeterRegistry registry) {
        videosSent = registry.counter("producer_videos_sent");
    }

    @GetMapping("/")
    @ManagedOperation
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("waitDisplay", waitDisplay);
        model.addAttribute("downloadDisplay", downloadDisplay);

        return "home";
    }

    @PostMapping("/uploadFile")
    @ManagedOperation
    @ManagedOperationParameters()
    public String putVideo(Model model, @RequestParam("file") MultipartFile videoFile) {
        System.out.println("New request for file upload.");
        processingService.sendVideoToKafka(videoFile);
        videosSent.increment();
        waitDisplay = true;
        downloadDisplay = false;
        return "redirect:/";
    }

    @PostMapping("/testUpload")
    public ResponseEntity<String> putTestMessage(@RequestParam("message") String message) {
        return processingService.sendTestMessage(message);
    }
}
