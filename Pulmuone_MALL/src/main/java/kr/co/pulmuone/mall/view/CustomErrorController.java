package kr.co.pulmuone.mall.view;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class CustomErrorController implements ErrorController {
    @GetMapping("${server.error.path:${error.path:error}}")
    public String handleError(HttpServletRequest request) {
        try {
            if(null == request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI)) return "any/error/index";
            else return "any/error/404/index";
        }
        catch(Exception ex) {
            return "any/error/index";
        }
    }

	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return "/error";
	}
}
