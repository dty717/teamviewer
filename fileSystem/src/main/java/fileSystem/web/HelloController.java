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
    
    @RequestMapping(value = { "/uploadFile" },produces = "application/json;charset=utf-8")
    @ResponseBody
    public String uploadThing(@RequestParam(value="path", required = true)String path,@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        String orgName = multipartFile.getOriginalFilename();
        try {
            path=new String(path.getBytes("ISO-8859-1"),"utf-8");
            orgName=new String(orgName.getBytes("ISO-8859-1"),"utf-8");
        } catch(Exception e) {
            
        }
        File file=new File(path);
        if(!file.getParentFile().exists())
            return "{\"error\":\"文件夹不存在\"}";
        else if(file.isFile()){
            return "{\"error\":\"文件已存在\"}";
        }
        File dest = new File(file.getAbsolutePath()+"/"+orgName);
        try {
            multipartFile.transferTo(dest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return "{\"error\":\"File "+orgName+"uploaded failed\"}";
        } catch (IOException e) {
            e.printStackTrace();
            return "{\"error\":\"File "+orgName+"uploaded failed\"}";
        }
        return "{\"success\":\"upload finished\"}";
    }
    
}
