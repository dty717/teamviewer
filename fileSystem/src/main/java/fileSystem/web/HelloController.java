package fileSystem.web; 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;


@Controller
public class HelloController {
 
    @RequestMapping(value = { "/hello" ,"/Hello"})
    @ResponseBody
    public String example() {
        return "Hello World";
    }
    
}
