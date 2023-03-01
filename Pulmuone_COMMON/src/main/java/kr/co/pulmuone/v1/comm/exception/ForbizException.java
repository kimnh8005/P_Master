package kr.co.pulmuone.v1.comm.exception;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import kr.co.pulmuone.v1.comm.util.PrintUtil;




public class ForbizException implements HandlerExceptionResolver {

    private static final Logger log = LoggerFactory.getLogger(ForbizException.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

		ModelAndView mv = new ModelAndView("jsonView");

		String returnMsg = "";

		if( ex instanceof UserException ){
			returnMsg = ((UserException)ex).getUserExceptionMessage();
			if(returnMsg == null || returnMsg.equals("")){
				returnMsg = ex.getMessage();
			}

			mv.addObject("RETURN_CODE", ((UserException)ex).getUserExceptionCode());
			mv.addObject("RETURN_MSG",  returnMsg);
//			mv.addObject("RETURN_MSG",  ((UserException)ex).getMessage());
		}else{
			//ex.getCause().getMessage()

			ex.printStackTrace();

			try{
				if(ex != null){
					ByteArrayOutputStream bout = new ByteArrayOutputStream();
					ex.printStackTrace(new PrintStream(bout));
					bout.flush();
					returnMsg = new String(bout.toByteArray());
					mv.addObject("RETURN_MSG" , returnMsg);
				}
			}catch(Exception e){
				e.printStackTrace();
			}

			mv.addObject("RETURN_CODE", "999999999");
			//mv.addObject("RETURN_MSG" , "Please contact the person in charge. - " + ex.getMessage() );
//			mv.addObject("RETURN_MSG" , "관리자에게 문의하세요. - " + ex.getMessage() );
			mv.addObject("RETURN_MSG" , "관리자에게 문의하세요." );
		}

		new PrintUtil( returnMsg, "error" );

		log.error(returnMsg);

		return mv;
	}

}
