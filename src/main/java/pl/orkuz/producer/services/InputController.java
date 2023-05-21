package pl.orkuz.producer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.atomic.AtomicBoolean;

@Controller
public class InputController {

    @Autowired
    public ProcessingService processingService;

    public static Boolean waitDisplay;
    public static Boolean downloadDisplay;

    @Value("${spring.application.name}")
    String appName;

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("appName", appName);
        model.addAttribute("waitDisplay", waitDisplay);
        model.addAttribute("downloadDisplay", downloadDisplay);

        return "home";
    }

    @PostMapping("/uploadFile")
    public String putVideo(Model model, @RequestParam("file") MultipartFile videoFile) {
        System.out.println("New request for file upload.");
        processingService.sendVideoToKafka(videoFile);
        waitDisplay = true;
        downloadDisplay = false;
        return "redirect:/";
    }

    @PostMapping("/testUpload")
    public ResponseEntity<String> putTestMessage(@RequestParam("message") String message) {
        return processingService.sendTestMessage(message);
    }
}
