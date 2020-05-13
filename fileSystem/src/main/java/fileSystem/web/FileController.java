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
import org.springframework.web.bind.annotation.CrossOrigin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.util.*;

@CrossOrigin(origins = "*")
@Controller
public class FileController {

    @Autowired
    Gson gson; 
    
    
    @RequestMapping(value = { "fileList" }, method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String fileList(@RequestParam("path")String path) {
        File file=new File(path);
        if(!file.exists())
            return "";
        if(!file.isDirectory()){
            return "";
        }else {
            File[] children=file.listFiles();
            StringBuilder builder=new StringBuilder();
            builder.append("[");
            for(int i=0;i<children.length;i++){
                builder.append("{\"name\":\""+children[i].getName());
                builder.append("\",\"children\":[]");
                /*if(children[i].isDirectory()){
                    builder.append("\",\"size\":"+fileService.FileDirAccessCount(path+children[i].getName())*200+",\"children\":[]");
                }else{
                    builder.append("\",\"size\":"+fileService.FileAccessCount(path+children[i].getName())*200);
                }*/
                builder.append("}");
                if(i!=children.length-1){
                    builder.append(",");
                }
            }
            builder.append("]");
            return builder.toString();
        }
    }
    
    @RequestMapping(value = { "getFileList" }, method = RequestMethod.GET,produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getFileList(@RequestParam("path")String  path) {
        //model.addAttribute("user", getPrincipal());
        //employeeManagerImpl.getEmployeeById(1);
        //System.out.println(122);
        
        File file=new File(path);
        if(!file.exists())
            return "{\"type\":\"error\"}";
        if(file.isDirectory()){
            return "{\"type\":\"directory\",\"list\":"+gson.toJson(file.list())+"}";
        }else {
            return "{\"type\":\"file\"}";
        }
    }
    
    @RequestMapping(value = { "getFilePicture" },produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseBody
    public byte[] getFilePicture(@RequestParam("path")String  path) {
        //model.addAttribute("user", getPrincipal());
        File file=new File(path);
        if(!file.exists())
            return "error:文件不存在".getBytes();
        if(file.isDirectory()){
            return "error:文件夹".getBytes();
        }else {
            try{
                return FileUtils.readFileToByteArray(file);
                //IOUtils.toByteArray(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error".getBytes();
    }
    
    @RequestMapping(value = { "getFile" },produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    @ResponseBody
    public byte[] getFile(@RequestParam("path")String  path,@RequestParam(value="charset",required=false)String charset) {
        //model.addAttribute("user", getPrincipal());
        File file=new File(path);
        if(!file.exists())
            return "error:文件不存在".getBytes();
        if(file.isDirectory()){
            return "error:文件夹".getBytes();
        }else {
            try{
                if(file.getName().endsWith(".bat")||"gbk".equals(charset)){
                    return FileUtils.readFileToString(file, "GBK").getBytes("UTF-8");
                }else
                    return FileUtils.readFileToByteArray(file);
                    //IOUtils.toByteArray(new FileInputStream(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "error".getBytes();
    }
    
    @RequestMapping(value = { "saveFile" },produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8",method = RequestMethod.POST)
    @ResponseBody
    public String saveFile(@RequestParam("path")String path,@RequestParam("file")String  content,@RequestParam(value="charset",required=false)String charset) {
        //model.addAttribute("user", getPrincipal());
        File file=new File(path);
        try {
            if(!file.exists()){
                path=new String(path.getBytes("ISO-8859-1"),"utf-8");
                file=new File(path);
            }
            content=new String(content.getBytes("ISO-8859-1"),"utf-8");
        } catch(Exception e) {
            
        }
        if(file.exists()){
            try {
                OutputStreamWriter out;
                if("gbk".equals(charset)){
                    out=new OutputStreamWriter(new FileOutputStream(file), Charset.forName("GBK"));
                }else{
                    out=new OutputStreamWriter(new FileOutputStream(file),StandardCharsets.UTF_8);
                }
                out.write(content);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "{\"state\":\"success\"}";
    }
    
    @RequestMapping(value = { "saveFile" },produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8",method = RequestMethod.GET)
    @ResponseBody
    public String saveFile_get(@RequestParam("path")String path,@RequestParam("file")String  content,@RequestParam(value="charset",required=false)String charset) {
        //model.addAttribute("user", getPrincipal());
        File file=new File(path);
        if(!file.exists()){
            try {
                path=new String(path.getBytes("ISO-8859-1"),"utf-8");
                file=new File(path);
                content=new String(content.getBytes("ISO-8859-1"),"utf-8");
            } catch(Exception e) {
                
            }
        }
        if(file.exists()){
            try {
                OutputStreamWriter out;
                if("gbk".equals(charset)){
                    content=new String(content.getBytes("ISO-8859-1"),"utf-8");
                    out=new OutputStreamWriter(new FileOutputStream(file), Charset.forName("GBK"));
                }else{
                    out=new OutputStreamWriter(new FileOutputStream(file),StandardCharsets.UTF_8);
                }
                out.write(content);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "{\"state\":\"success\"}";
    }
    @RequestMapping(value = { "run" },produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String run(@RequestParam("path")String path) {
        try
        {
            File file=new File(path);
            if(file.exists()){
                java.awt.Desktop.getDesktop().open(file);
                
                //file.getAbsolutePath();
            }
        } catch (Throwable t)
        {
            t.printStackTrace();
        }
        return "{\"state\":\"success\"}";
    }
    
    @RequestMapping(value = { "newFile" },produces = MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
    @ResponseBody
    public String newFile(@RequestParam("path")String path,@RequestParam(value = "name")String name) {
        File file=new File(path);
        
        if(!file.exists()){
            return "{\"state\":\"error(no path exist)\"}";
        }
        if(file.isFile()){
            file=file.getParentFile();
        }
        File fileNew=new File(file.getAbsolutePath()+"/"+name);
        try{
            fileNew.createNewFile();
        }catch (Exception e){
            return "{\"state\":\"error(no path exist)\"}";
        }
        return "{\"state\":\"success\"}";
    }
    
    //@Autowired
    @RequestMapping(value = { "/uploadFile" },produces = "application/json;charset=utf-8")
    @ResponseBody
    public String uploadThing(@RequestParam(value = "path", required = false)String path,@RequestParam(value = "file", required = true) MultipartFile multipartFile) {
        String orgName = multipartFile.getOriginalFilename();
        if(path==null){
            path="/";
        }
        try {
            path=new String(path.getBytes("ISO-8859-1"),"utf-8");
            orgName=new String(orgName.getBytes("ISO-8859-1"),"utf-8");
        } catch(Exception e) {
            
        }
        File file=new File(path);
        if(file.getParentFile()!=null&&!file.getParentFile().exists())
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