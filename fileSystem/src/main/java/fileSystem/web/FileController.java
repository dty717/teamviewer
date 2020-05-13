package fileSystem.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.*;

@Controller
public class FileController {

    @Autowired
    Gson gson;

    //@Autowired
    @RequestMapping(value = { "/uploadFile" },produces = "application/json;charset=utf-8")
    @ResponseBody
    public String uploadThing(@RequestParam("path")String path,@RequestParam(value = "file", required = false) MultipartFile multipartFile) {
        String orgName = multipartFile.getOriginalFilename();
        if(path==null){
            path="";
        }
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