package fileSystem.web; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.ByteArrayOutputStream;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.awt.event.KeyEvent;

/* -*-mode:java; c-basic-offset:2; indent-tabs-mode:nil -*- */
/**
 * This program will demonstrate remote exec.
 *   $ CLASSPATH=.:../build javac Exec.java 
 *   $ CLASSPATH=.:../build java Exec
 * You will be asked username, hostname, displayname, passwd and command.
 * If everything works fine, given command will be invoked 
 * on the remote side and outputs will be printed out.
 *
 */
import com.jcraft.jsch.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;

@Controller
public class DesktopController {
    
    @Autowired
    Robot robot;
    
    @RequestMapping(value = "/control", method = RequestMethod.GET, produces = "image/jpg")
    @ResponseBody
    public byte[] getFile(@RequestParam(value = "type", required = true)String type,
        @RequestParam(value = "code", required = true)int code)  {
        if(type.equals("scroll")){
            if(code==0){
                robot.mouseWheel(1);
            }else if(code==1){
                robot.mouseWheel(-1);
            }
            
            return null;
        }
        if(code==13){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_ENTER);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_ENTER);
            }
            return null;
        }else if(code==189){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_MINUS);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_MINUS);
            }
            return null;
        }else if(code==187){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_EQUALS);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_EQUALS);
            }
            return null;
        }else if(code==219){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_OPEN_BRACKET);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_OPEN_BRACKET);
            }
            return null;
        }else if(code==221){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_CLOSE_BRACKET);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_CLOSE_BRACKET);
            }
            return null;
        }else if(code==220){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_BACK_SLASH);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_BACK_SLASH);
            }
            return null;
        }else if(code==186){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_SEMICOLON);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_SEMICOLON);
            }
            return null;
        }else if(code==188){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_COMMA);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_COMMA);
            }
            return null;
        }else if(code==190){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_PERIOD);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_PERIOD);
            }
            return null;
        }else if(code==191){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_SLASH);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_SLASH);
            }
            return null;
        }else if(code==91){
            if(type.equals("keyUp")){
                robot.keyRelease(KeyEvent.VK_WINDOWS);
            }else if(type.equals("keyDown")){
                robot.keyPress(KeyEvent.VK_WINDOWS);
            }
            return null;
        }

        
        if(type.equals("keyUp")){
            robot.keyRelease(code);
        }else if(type.equals("keyDown")){
            robot.keyPress(code);
        }
        return null;
    }
    
    @RequestMapping(value = "/connect", method = RequestMethod.GET)
    @ResponseBody
    public String connect(){
        main(null);
        return "ok";
    }
    
    @RequestMapping(value = "/monitor", method = RequestMethod.GET, produces = "image/jpg")
    @ResponseBody
    public byte[] getFile(@RequestParam(value = "type", required = false)String type,
        @RequestParam(value = "x", required = false)Integer x,@RequestParam(value = "y", required = false)Integer y)  {
        try {
            Rectangle rectangle=new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
            switch(type){
                case "key":
                    robot.keyPress(x);
                    break;
                case "click":
                    robot.mouseMove((int)(rectangle.getWidth()*x/100), (int)(rectangle.getHeight()*y/100));
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
                case "contextmenu":
                    robot.mouseMove((int)(rectangle.getWidth()*x/100), (int)(rectangle.getHeight()*y/100));
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    try {
                        Thread.sleep(300);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    robot.keyPress(KeyEvent.VK_CONTEXT_MENU);
                    robot.keyRelease(KeyEvent.VK_CONTEXT_MENU);
                    break;
                case "dblclick":
                    robot.mouseMove((int)(rectangle.getWidth()*x/100), (int)(rectangle.getHeight()*y/100));
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    break;
            }
            
            
            BufferedImage screencapture = robot.createScreenCapture(rectangle);
            
            // Create a byte array output stream.
            ByteArrayOutputStream img = new ByteArrayOutputStream();
            
            // Write to output stream
            ImageIO.write(screencapture, "jpg", img);
            return img.toByteArray();
        } catch (Exception e) {
            //logger.error(e);
            //throw new RuntimeException(e);
        
            return null;
        }
        
    }
   
    public static void main(String[] arg){
        try{
          JSch jsch=new JSch();  

          String host="144.202.5.178";

          String user="root";
         
          jsch.addIdentity("C:/Users/xqy/.ssh/id_rsa.ppk"
//			 , "passphrase"
			 );
          Session session=jsch.getSession(user, host, 22);
        //   try {
        //       session.setPortForwardingR(8888,"127.0.0.1",8888);
        //   } catch(JSchException e) {
        //       System.out.println("error");
        //   }
          /*
          String xhost="127.0.0.1";
          int xport=0;
          String display=JOptionPane.showInputDialog("Enter display name", 
                                                     xhost+":"+xport);
          xhost=display.substring(0, display.indexOf(':'));
          xport=Integer.parseInt(display.substring(display.indexOf(':')+1));
          session.setX11Host(xhost);
          session.setX11Port(xport+6000);
          */
    
          // username and password will be given via UserInfo interface.
          session.connect();

          Channel channel=session.openChannel("exec");

          //channel.setInputStream(System.in);
          channel.setInputStream(null);
    
          //channel.setOutputStream(System.out);
 
          //FileOutputStream fos=new FileOutputStream("/tmp/stderr");
          //((ChannelExec)channel).setErrStream(fos);
          ((ChannelExec)channel).setErrStream(System.err);
    
          InputStream in=channel.getInputStream();
    
          channel.connect();
    
          byte[] tmp=new byte[1024];
          while(true){
            while(in.available()>0){
              int i=in.read(tmp, 0, 1024);
              if(i<0)break;
              System.out.print(new String(tmp, 0, i));
            }
            if(channel.isClosed()){
              if(in.available()>0) continue; 
              System.out.println("exit-status: "+channel.getExitStatus());
              break;
            }
            try{Thread.sleep(1000);}catch(Exception ee){}
          }
          channel.disconnect();
          session.disconnect();
        }
    catch(Exception e){
      System.out.println(e);
    }
  }

}