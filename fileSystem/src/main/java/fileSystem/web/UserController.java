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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

// import flexjson.JSONSerializer;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*")
@Controller
public class UserController {
    
    @RequestMapping(value = { "/login"})
    public String login(HttpServletRequest req) {
        System.out.println(req.getRemoteAddr()+" "+req.getRemoteHost()+" "+req.getRemotePort()+" "+req.getRequestedSessionId());
        return "/views/login.html";
    }
    
    @RequestMapping(value="/ajaxLogin", method = RequestMethod.POST)
    @ResponseBody
    public String ajaxLogin(@RequestParam("username")String username,@RequestParam("password")String password) {
        return "Hello World";
    }
    
    /*
    public HttpHeaders getJsonHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return headers;
    }
    
    @RequestMapping(value="/rest/security/login-page", method = RequestMethod.GET)
    public ResponseEntity<String> apiLoginPage() {
        return new ResponseEntity<String>(getJsonHeaders(), HttpStatus.UNAUTHORIZED);
    }
    

    @RequestMapping(value="/rest/security/authentication-failure", method = RequestMethod.GET)
    public ResponseEntity<String> apiAuthenticationFailure() {
        // return HttpStatus.OK to let your front-end know the request completed (no 401, it will cause you to go back to login again, loops, not good)
        // include some message code to indicate unsuccessful login
        return new ResponseEntity<String>("{\"success\" : false, \"message\" : \"authentication-failure\"}", getJsonHeaders(), HttpStatus.OK);
    }

    @RequestMapping(value="/rest/security/default-target", method = RequestMethod.GET)
    public ResponseEntity<String> apiDefaultTarget() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // exclude/include whatever fields you need
        String userJson = new JSONSerializer().exclude("*.class", "*.password").serialize(authentication);
        return new ResponseEntity<String>(userJson, getJsonHeaders(), HttpStatus.OK);
    }
    */

}
