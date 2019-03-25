package fileSystem.web; 

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.core.NestedRuntimeException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

@Controller
public class MailController {
    
    public static final String MAIL_PROP = "mail.";
    public static final String TARGET_EMAIL = "targetEmail";
    public static final String UTF_8 = "UTF-8";
    
    @Autowired
    private MessageSource messages;
    
    private JavaMailSenderImpl mailSender;
    private String mailFrom;
    
    @PostConstruct
    private void init() {
        updateMailConfiguration();
        sendTestMail("1875167603@qq.com");
    }

    public void updateMailConfiguration() {
        String json = "{\n"+
        "	\"mailFrom\": \"Thingsboard <sysadmin@notice.dty717>\",\n"+
        "	\"smtpProtocol\": \"smtp\",\n"+
        "	\"smtpHost\": \"localhost\",\n"+
        "	\"smtpPort\": \"25\",\n"+
        "	\"timeout\": \"10000\",\n"+
        "	\"enableTls\": \"false\",\n"+
        "	\"username\": \"\",\n"+
        "	\"password\": \"\"\n"+
        "}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonConfig=null;
        try {
            jsonConfig= objectMapper.readTree(json);   
        } catch(Exception e) {
        }
        createMailSender(jsonConfig);
        mailFrom = jsonConfig.get("mailFrom").asText();
    }
    
    private void createMailSender(JsonNode jsonConfig) {
        mailSender = new JavaMailSenderImpl();
        mailSender.setHost(jsonConfig.get("smtpHost").asText());
        try {
            mailSender.setPort(Integer.valueOf(jsonConfig.get("smtpPort").asText()));
        } catch(Exception e) {
        }
        mailSender.setUsername(jsonConfig.get("username").asText());
        mailSender.setPassword(jsonConfig.get("password").asText());
        mailSender.setJavaMailProperties(createJavaMailProperties(jsonConfig));
    }

    private Properties createJavaMailProperties(JsonNode jsonConfig) {
        Properties javaMailProperties = new Properties();
        String protocol = jsonConfig.get("smtpProtocol").asText();
        javaMailProperties.put("mail.transport.protocol", protocol);
        javaMailProperties.put(MAIL_PROP + protocol + ".host", jsonConfig.get("smtpHost").asText());
        javaMailProperties.put(MAIL_PROP + protocol + ".port", jsonConfig.get("smtpPort").asText());
        javaMailProperties.put(MAIL_PROP + protocol + ".timeout", jsonConfig.get("timeout").asText());
        javaMailProperties.put(MAIL_PROP + protocol + ".auth", String.valueOf(StringUtils.isNotEmpty(jsonConfig.get("username").asText())));
        javaMailProperties.put(MAIL_PROP + protocol + ".starttls.enable", jsonConfig.has("enableTls") ? jsonConfig.get("enableTls").asText() : "false");
        return javaMailProperties;
    }
    
    private void sendMail(JavaMailSenderImpl mailSender,
                          String mailFrom, String email,
                          String subject, String message){
        try {
            MimeMessage mimeMsg = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMsg, UTF_8);
            helper.setFrom(mailFrom);
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(message, true);
            mailSender.send(helper.getMimeMessage());
        } catch (Exception e) {
            
        }
    }
    public void sendTestMail(String email) {
        JavaMailSenderImpl testMailSender = mailSender;
        
        String subject = "test";

        Map<String, Object> model = new HashMap<String, Object>();
        model.put(TARGET_EMAIL, email);

        String message = "Hello World!";

        sendMail(testMailSender, mailFrom, email, subject, message);
    }

    
    /*@RequestMapping(value = { "/hello" ,"/Hello"})
    @ResponseBody
    public String example() {
        
        return "Hello World";
    }
    
    @RequestMapping(value = { "/uploadFile" },produces = "application/json;charset=utf-8")
    @ResponseBody
    public String uploadThing() {
        return "";
    }*/
    
}
