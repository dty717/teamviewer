/**
 *    Copyright 2010-2017 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package fileSystem.service;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.IOException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
// import javax.comm.CommPortIdentifier;
// import javax.comm.PortInUseException;
// import javax.comm.SerialPort;
// import javax.comm.UnsupportedCommOperationException;
import java.io.OutputStream;
import java.io.InputStream;
import java.util.Enumeration;

@Service
public class DesktopService{
    
    private int mode=0;
    public final static int defaultMode=0;
    public final static int inputMode=1;
    public final static int orderMode=2;
    public final static int workMode=3;
    public final static int serialMode=4;
    
    public Object getSpeech(String text) {
        if(text.indexOf("输入模式")!=-1){
            mode=inputMode;
            return "ok";
        }else if(text.indexOf("正常模式")!=-1||text.indexOf("常规模式")!=-1||text.indexOf("默认模式")!=-1){
            mode=defaultMode;
            return "ok";
        }else if(text.indexOf("命令模式")!=-1){
            mode=orderMode;
            return "ok";
        }else if(text.indexOf("工作模式")!=-1){
            mode=workMode;
            return "ok";
        }else if(text.indexOf("串口模式")!=-1){
            mode=serialMode;
            return "ok";
        }
        switch(mode){
            case defaultMode:
                return getSpeechDefault(text);
            case inputMode:
                return getSpeechInput(text);
            case orderMode:
                return getSpeechOrder(text);
            case workMode:
                return getSpeechWork(text);
            // case serialMode:
                // return getSerialWork(text);
        }
        return "error";
    }
    String serialName="com9";
    int bound=9600;
/*    public Object getSerialWork(String text){
        if(text.startsWith("设置串口")){
            serialName=text.substring(4).trim().toLowerCase();
            return "ok";
        }else if(text.startsWith("设置波特率")){
            bound=Integer.parseInt(text.substring(5).trim().toLowerCase());
            return "ok";
        }else{
            Enumeration portList = CommPortIdentifier.getPortIdentifiers();
            //System.out.println(portList.hasMoreElements());
            while (portList.hasMoreElements()) {
            	CommPortIdentifier portId = (CommPortIdentifier) portList.nextElement();
                if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    System.out.println(portId.getName().toLowerCase()+" "+serialName);
                    if (portId.getName().toLowerCase().equals(serialName)) {
                        SerialPort serialPort;
                        try {
                            serialPort = (SerialPort) portId.open("SimpleReadApp", 1000);
                            InputStream inputStream = serialPort.getInputStream();
                            OutputStream outputStream = serialPort.getOutputStream();
                            serialPort.setSerialPortParams(bound,
                                SerialPort.DATABITS_8,
                                SerialPort.STOPBITS_1,
                                SerialPort.PARITY_NONE);
                            for(int i=0;i<2;i++){
                                outputStream.write(("test\r\n").getBytes());
                                Thread.sleep(1000);
                                outputStream.write((text+"\r\n").getBytes());
                                Thread.sleep(500);
                            }
                            System.out.println(123);    
                        	outputStream.close();
                        	serialPort.close();
                		} catch (Exception e) {
                        
                        }
                    }
                }
            

            }
            return "work";
        }
    }
*/
    private boolean needSaveSpeech;

    
    @Transactional
    public void saveSpeech(String text) {
    }
    public Object getSpeechWork(String text){
        if(text.indexOf("水站")!=-1&&(text.indexOf("打开")!=-1||text.indexOf("项目")!=-1)){
            openSite("http://127.0.0.1:8080/?path=C:/Users/xqy/Desktop/github/水站");
        }else if(text.indexOf("ide")!=-1&&(text.indexOf("打开")!=-1||text.indexOf("项目")!=-1)){
            openSite("http://127.0.0.1:8080/?path=C:/Users/xqy/Desktop/新建工程/IDE/代码/IDE/");
        }else if(text.indexOf("打开")!=-1&&text.indexOf("项目")!=-1){
            openSite("http://127.0.0.1:8080");
        }else if(text.indexOf("command")!=-1){
            
            openSite("https://www.google.com/search?q="+text);
        }
        return "work";
    }
    
    public Object getSpeechOrder(String text) {
        if(needSaveSpeech){
            needSaveSpeech=false;
            saveSpeech(text);
            return "save finish";
        }else if(text.indexOf("保存")!=-1){
            needSaveSpeech=true;
            return "save begin";
        }else if(text.startsWith("获取")){
            return "error get";
        }
        return "order";
    }
    public String getSpeechInput(String text) {
    
        StringSelection stringSelection=new StringSelection(text);
        Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection,stringSelection);
        try {
            Robot robot=new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        return "input";
    }
    
    
    public String getSpeechDefault(String text) {
        
        
        if(text.indexOf("反对") != -1)
            return "/嘘  /嘘  /嘘\n不要去反对明知道错误的事情,你与别人不同,你越是反对,你就会越赞同某个人,在潜移默化中你接受别人的错误,你没有说服别人改正错误,反而自己接受别人的错误,这划不来";
        else if(text.indexOf("女") != -1)
            return "/可爱  /可爱  /可爱\n你没有办法区分女人和性之间的关系,对于爱情,你往往会被色欲控制,而不能交到真正的异性.沉溺于色欲,只会让自己丧失主见变得软弱,丧失自己所追求的.而且你会悲伤,因为你失去了她.";
        else if(text.indexOf("目标") != -1)
            return "/表情  /表情  /表情\n你是一个极其有天赋的人,在我看来,电脑与自动化是你最适合的职业,在这个领域展现出自己的与众不同吧";
        else if(text.indexOf("政治") != -1)
            return "/表情  /表情  /表情\n学会放弃关注政治上问题,你看到的政治问题总是带有不平衡,如果你关注了,那么注定你会因为其中的冤屈而不平衡,打抱不平只会使自己接受他们的错误,从而一味的沉浸在政治之中";
        else if(text.indexOf("骄傲") != -1)
            return "/表情  /表情  /表情\n其实生活不过如此,看似不容易,实则轻而易举,当骄傲时,不妨玩玩电机或者电动小车,转移注意力";
        else if(text.indexOf("失控") != -1)
            return "/偷笑  /偷笑  /偷笑\n没有办法解决,顺其自然吧,记住下次一定不要再失控了";
        else if(text.indexOf("累") != -1)
            return "/困  /困  /困\n早点休息吧,休息是为了跟好的工作";
        else if(text.indexOf("紧张") != -1)
            return "/大兵  /大兵  /大兵\n其实没必要紧张,过去的时间不要懊悔,只要现在还有时间就可以接着做,时间永远是一样的,没有先后贵贱之分,处之淡然,如若初始";
        else if(text.indexOf("万") != -1)
            return "/表情  /表情  /表情\n不要担心两百万挣不到,我相信你具有这样的能力做到,你生而与众不同,不要用平凡限制了自己的想象,不要用普通束缚了自己的行为.";
        else if(text.indexOf("危机") != -1)
            return "/表情  /表情  /表情\n其实危机中每个人的利益都会受损,为争夺利益而产生的不平衡随处可见,捍卫自身利益而本能反抗行为也是不可控的,在危机中,冤屈,不平衡随时随地会降到每个人的头上,保持自身平衡,就显得尤为重要.更为重要的是如何在危机中如何理性的避免危险寻求机遇";
        else if(text.indexOf("我") != -1)
            return "/表情  /表情  /表情\n我可以帮你解决的问题有政治,反对,紧张,失控,累,女人,目标,万,骄傲,危机";
    
        if (text.indexOf("你好") != -1 && text.length() < 5) {
            if (text.indexOf("吗") > text.indexOf("你好")) {
                return "我很好,你呢";
            } else
                return "你好";
        }
        if (text.indexOf("Hi") != -1 && text.length() < 3) {
            return "Hi";
        }
        if (text.indexOf("Hello") != -1 && text.length() < 7) {
            return "Hello";
        }
        if((text.indexOf("查询") != -1)){
            test(text.replace("查询",""));

        }else if(text.indexOf("谷歌") != -1){
            test(text.replace("谷歌",""));

        }
        else if(text.indexOf("google") != -1){
            test(text.replace("google",""));
        }else if(text.indexOf("删除") != -1){
            try {
                Robot robot=new Robot();
                robot.keyPress(KeyEvent.VK_BACK_SPACE);

                robot.keyRelease(KeyEvent.VK_CONTROL);

            } catch (AWTException e) {
                e.printStackTrace();
            }
        }else if(text.indexOf("空格") != -1){
            try {
                Robot robot=new Robot();
                robot.keyPress(KeyEvent.VK_SPACE);

                robot.keyRelease(KeyEvent.VK_SPACE);

            } catch (AWTException e) {
                e.printStackTrace();
            }
        }else if(text.indexOf("保存") != -1){
            try {
                Robot robot=new Robot();
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_S);
                robot.keyRelease(KeyEvent.VK_S);
                robot.keyRelease(KeyEvent.VK_CONTROL);

            } catch (AWTException e) {
                e.printStackTrace();
            }
        }else{
            StringSelection stringSelection=new StringSelection(text);
            Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection,stringSelection);
            try {
                Robot robot=new Robot();
                robot.keyPress(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_V);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyPress(KeyEvent.VK_DOWN);
                robot.keyRelease(KeyEvent.VK_DOWN);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
        return "ok,my master";
    }
    
    public String copy(){
        try {
            Robot robot=new Robot();
            robot.keyPress(KeyEvent.VK_CONTROL);
            Thread.sleep(100);
            robot.keyPress(KeyEvent.VK_C);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_C);
            Thread.sleep(100);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            Thread.sleep(1000);
            Clipboard clipboard= Toolkit.getDefaultToolkit().getSystemClipboard();
            Object copyData=clipboard.getData(DataFlavor.stringFlavor); 
            System.out.println(copyData);
            return (String)copyData;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public void openSite(String url){
        try {
            Runtime.getRuntime().exec(
                    new String[]{"C://Program Files/Google/Chrome/Application/chrome.exe",
                            url});
            //System.out.println("Google Chrome launched!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void test(String context) {
        String url="https://www.google.com/search?q=";
        String[]strings=context.split(" ");
        for (int i = 0; i < strings.length; i++) {
            if(i==strings.length-1){
                url+=strings[i];
            }else
                url+=strings[i]+"+";
        }
        try {

            Runtime.getRuntime().exec(
                    new String[]{"C://Program Files/Google/Chrome/Application/chrome.exe",
                            url});

            System.out.println("Google Chrome launched!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
