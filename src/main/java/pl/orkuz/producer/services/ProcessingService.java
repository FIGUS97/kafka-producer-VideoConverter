package pl.orkuz.producer.services;

import org.apache.kafka.common.utils.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class ProcessingService {

    @Autowired
    private KafkaTemplate<String, Bytes> kafkaTemplate;

    private File currentConvertedFile;

    public ResponseEntity<String> sendVideoToKafka(MultipartFile video) {

        if(video.isEmpty()) {
            return ResponseEntity.badRequest().body("Przesłany plik jest pusty.");
        } else if (video.getContentType() != null && !video.getContentType().contains("mp4")) {
            System.out.println("Przesłany plik nie ma formatu mp4!");
            return ResponseEntity.badRequest().body("Przesłany plik nie ma formatu mp4!");
        }

        byte[] videoBytes;

        try {
            videoBytes = StreamUtils.copyToByteArray(video.getInputStream());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during conversion");
        }

        kafkaTemplate.send("videoStorage1", new Bytes(videoBytes));

        System.out.println("Plik wideo został wysłany." +
                "\nFormat pliku: " + video.getContentType() +
                "\nNazwa pliku: " + video.getOriginalFilename());
        return ResponseEntity.ok("Plik wideo został poprawnie przesłany." +
                "Format pliku: " + video.getContentType() +
                "\nNazwa pliku: " + video.getOriginalFilename());
    }

    public ResponseEntity<String> sendTestMessage(String message) {

        kafkaTemplate.send("quickstart-events", new Bytes(message.getBytes()));
        return ResponseEntity.ok("Wiadomość testowa wysłana, ale nie wiadomo czy dotarła.");
    }

    @KafkaListener(id="id", topics = "videoStorage2")
    public void processVideo(Bytes kafkaBytes) throws IOException {
        byte[] fileBytes = kafkaBytes.get();
        File videoFile = new File("result.avi");
        FileOutputStream fostream = new FileOutputStream(videoFile);
        fostream.write(fileBytes);
        fostream.flush();
        fostream.close();

        InputController.waitDisplay = false;
        InputController.downloadDisplay = true;
        System.out.println("Otrzymano plik wideo: " + videoFile.getName());
        currentConvertedFile = videoFile;
    }

    public File getCurrentConvertedFile() {
        return currentConvertedFile;
    }
}
