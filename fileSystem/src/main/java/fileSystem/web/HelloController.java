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
import java.util.LinkedHashMap;

@Controller
public class HelloController {
 
    @RequestMapping(value = { "/hello" ,"/Hello"})
    @ResponseBody
    public String example() {
        return "Hello World";
    }
    
    @Autowired
    LinkedHashMap<String,String> getSets;
    
    @RequestMapping(value = {"get"},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String get(@RequestParam(value="name")String name) {
        return getSets.get(name);
    }
    
    @RequestMapping(value = {"set"},produces = "application/json;charset=utf-8")
    @ResponseBody
    public String set(@RequestParam(value="name")String name,@RequestParam(value="value")String value) {
        getSets.put(name,value);
        return "{\"state\":\"success\"}";
    }
    
}
