package pl.orkuz.producer.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class OutputController {
    @Autowired
    public ProcessingService processingService;

    public static Boolean waitDisplay = InputController.waitDisplay;
    public static Boolean downloadDisplay = InputController.downloadDisplay;

    @RequestMapping(value = "/download", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_OCTET_STREAM_VALUE )
    @ResponseBody
    public FileSystemResource downloadFile(){
        //downloadDisplay = false;
        System.out.println("Zażądano pobrania pliku wideo.");
        return new FileSystemResource(processingService.getCurrentConvertedFile());
    }
}
