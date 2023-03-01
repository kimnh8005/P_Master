package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.MenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.PrivacyMenuOperLogRequestDto;
import kr.co.pulmuone.v1.base.dto.vo.MenuUrlResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;

import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;
import org.apache.velocity.tools.generic.NumberTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Service
public class ComnBizImpl implements ComnBiz {

    @Autowired
    ComnService comnService;

	@Autowired
	private SendTemplateBiz sendTemplateBiz;

    @Override
    public ApiResult<?> hasSessionInfoByLoginId(HttpServletRequest req) throws Exception {
        return comnService.hasSessionInfoByLoginId(req);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public ApiResult<?> addMenuAccessLog(HttpServletRequest request, UserVo userVo) throws Exception {
    	String systemMenuId = StringUtil.nvl(request.getHeader("authMenuID"));
    	String url = request.getRequestURI();

    	/*
    	 *  authMenuID = null 인 경우 공통 함수 호출등 (ex>/admin/comn/hasSessionInfoByLoginId , /admin/comn/getProgramList)
    	 *  처리 방법 확인 필요
    	*/
    	if(!"".equals(systemMenuId)) {
    		//url로 정보 받기
	    	MenuUrlResultVo menuUrlResultVo = comnService.getMenuUrlDataByUrl(systemMenuId, url);

	    	//menuUrlResultVo = null 인 경우 처리 확인 필요
	    	if(menuUrlResultVo != null) {
		    	//메뉴이력 저장
		    	MenuOperLogRequestDto menuOperLogRequestDto = new MenuOperLogRequestDto();
				menuOperLogRequestDto.setSystemMenuId(systemMenuId);
				menuOperLogRequestDto.setSystemMenuUrlId(menuUrlResultVo.getSystemMenuUrlId());
				menuOperLogRequestDto.setMenuName(menuUrlResultVo.getMenuName());
				menuOperLogRequestDto.setUrlName(menuUrlResultVo.getUrlName());

				try {
          comnService.addMenuOperLog(menuOperLogRequestDto, request);
        }
				catch(Exception e) {
				  // 오류 발생 시 업무에 영향 없이 오류 로그만 출력하고 계속 진행
				  log.error("메뉴이력등록 오류 :: " + e.toString());
				  e.printStackTrace();
        }

				//개인정보처리이력 저장 'Y' 인 경우
				if("Y".equals(menuUrlResultVo.getPrivacyLogYn())) {
					Enumeration params = request.getParameterNames();
					String paramValue = "";
					while (params.hasMoreElements()){
					    String name = (String)params.nextElement();
					    String value = !"".equals(request.getParameter(name)) ? name + "=" + request.getParameter(name) : "";
					    //특정 값만 추출로 변경 필요?
					    //filter 제외?
					    paramValue += !"".equals(paramValue) ? "&" + value : value;
					}


					PrivacyMenuOperLogRequestDto privacyMenuOperLogRequestDto = new PrivacyMenuOperLogRequestDto();
					privacyMenuOperLogRequestDto.setSystemMenuUrlId(menuUrlResultVo.getSystemMenuUrlId());
					privacyMenuOperLogRequestDto.setUrlName(menuUrlResultVo.getUrlName());
					privacyMenuOperLogRequestDto.setUrl(url);
					privacyMenuOperLogRequestDto.setCrudType(menuUrlResultVo.getCrudType());
					privacyMenuOperLogRequestDto.setParamValue(paramValue);

					comnService.addPrivacyMenuOperLog(privacyMenuOperLogRequestDto, request);

				}
	    	}

    	}


        return null;
    }


    /**
     * @Desc 메일 SMS 템플릿 조회
     * @param templateCode
     * @param vo
     * @return
     */
    @Override
    public ApiResult<?> getSendTmplt(String templateCode, Object vo) {
		ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(templateCode);
		GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto)result.getData()).getRows();
		Map<String, Object> data = new HashMap<String, Object>();

		if("Y".equals(getEmailSendResultVo.getMailSendYn())) data.put("mailBody", getEmailTmplt(getEmailSendResultVo, vo));

		if("Y".equals(getEmailSendResultVo.getSmsSendYn())) data.put("smsBody", "");

		return ApiResult.success(data);
    }


    /**
     * @Desc 이메일 내용 받기
     * @param getEmailSendResultVo
     * @param vo
     * @return String
     */
    @Override
    public String getEmailTmplt(GetEmailSendResultVo getEmailSendResultVo, Object vo) {
    	//메일 템플릿 처리
        return getTmpltContext(StringUtil.htmlSingToText(getEmailSendResultVo.getMailBody()), vo);
    }

    /**
     * @Desc SMS 내용 받기 --> 확정 후 개발 필요 임시
     * @param getEmailSendResultVo
     * @param vo
     * @return String
     */
    @Override
    public String getSMSTmplt(GetEmailSendResultVo getEmailSendResultVo, Object vo) {
    	String smsBody = getTmpltContext(getEmailSendResultVo.getSmsBody(), vo);

    	//정책 확인 후 SMS 발송 로직 개발 필요
        return smsBody;
    }



    /**
     * @Desc 이메일sms 치환 데이터 조회
     * @param Context
     * @param vo
     * @return
     */
    public String getTmpltContext(String Context, Object vo) {

		VelocityEngine engine = new VelocityEngine();
		engine.setProperty(Velocity.RESOURCE_LOADER, "string");
	    engine.addProperty("string.resource.loader.class", StringResourceLoader.class.getName());
	    engine.addProperty("string.resource.loader.repository.static", "false");
	    engine.init();

	    // Initialize my template repository. You can replace the "Hello $w" with your String.
	    StringResourceRepository repo = (StringResourceRepository) engine.getApplicationAttribute(StringResourceLoader.REPOSITORY_NAME_DEFAULT);
	    repo.putStringResource("template", Context);

	    // Get and merge the template with my parameters.(치환)
	    Template template = engine.getTemplate("template");

	    VelocityContext context = new VelocityContext();
	    context.put("data", vo);	//변경하려는 데이터릉 vo 에 담아 넘긴다 (사용시 ex> userId = data.getUserId)
	    context.put("number", new NumberTool());

	    StringWriter writer = new StringWriter();
	    template.merge(context, writer);


        return writer.toString();
    }





}
