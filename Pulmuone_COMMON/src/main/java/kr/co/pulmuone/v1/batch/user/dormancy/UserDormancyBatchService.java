package kr.co.pulmuone.v1.batch.user.dormancy;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import kr.co.pulmuone.v1.comm.constants.BuyerConstants;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.batch.send.template.service.SendTemplateBatchBiz;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddEmailIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.AddSmsIssueSelectBatchRequestDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.GetEmailSendBatchResponseDto;
import kr.co.pulmuone.v1.batch.send.template.service.dto.vo.GetEmailSendBatchResultVo;
import kr.co.pulmuone.v1.batch.user.dormancy.dto.ActiveUserDormantRequestDto;
import kr.co.pulmuone.v1.batch.user.dormancy.dto.vo.UserDormancyBatchResultVo;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.mappers.batch.master.user.UserDormancyBatchMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDormancyBatchService {

    @Autowired
    @Qualifier("masterSqlSessionTemplateBatch")
    private SqlSessionTemplate masterSqlSession;

    private UserDormancyBatchMapper userDormancyBatchMapper;

    @Autowired
    private SendTemplateBatchBiz sendTemplateBatchBiz;

    /**
     * 휴면계정 전환 완료 Batch 실행
     */
    public void runUserDormancy() {
        this.userDormancyBatchMapper = masterSqlSession.getMapper(UserDormancyBatchMapper.class);

        List<Long> userIdList = getTargetDormant();
        for (Long urUserId : userIdList) {
            putActiveUserDormant(urUserId);
        }
    }

    /**
     * 휴면전환 대상자 조회
     *
     * @return List<Long>
     */
    private List<Long> getTargetDormant() {
        return userDormancyBatchMapper.getTargetDormant(BuyerConstants.BATCH_TARGET_MOVE_DAY);
    }

    /**
     * 정상회원 휴면전환
     */
    private void putActiveUserDormant(Long urUserId) {
        ActiveUserDormantRequestDto dto = new ActiveUserDormantRequestDto();
        dto.setUrUserId(urUserId);

        userDormancyBatchMapper.addUrUserMove(dto);
        dto.setUrUserMoveId(userDormancyBatchMapper.selUrUserMoveId(dto.getUrUserId()));
        userDormancyBatchMapper.addUrUserMoveLog(dto);
        userDormancyBatchMapper.addUrBuyerMove(dto);
        userDormancyBatchMapper.delUserBuyer(dto);
        userDormancyBatchMapper.putUserMove(dto);

        //휴면회원 결과 조회 Vo
        UserDormancyBatchResultVo userDormancyBatchResultVo = userDormancyBatchMapper.getUserDormancyBatchInfo(urUserId);

        //자동메일/SMS 전송
        getUserDormantCompleted(userDormancyBatchResultVo);

    }

    /**
     * @Desc 휴면계정 전환 완료시 자동메일 발송
     * @param buyerStatusResultVo
     * @return void
     */
    private void getUserDormantCompleted(UserDormancyBatchResultVo userDormancyBatchResultVo) {

    	GetEmailSendBatchResponseDto result = sendTemplateBatchBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.USER_DORMANT_COMPLETED.getCode());
    	GetEmailSendBatchResultVo getEmailSendResultVo = result.getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
    		//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBatchBiz.getDomainManagement() + "/admin/system/emailtmplt/getUserDormantCompletedEmailTmplt?urUserId=" + userDormancyBatchResultVo.getUrUserId();
    		String title = getEmailSendResultVo.getMailTitle();
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
    		String senderName = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_SENDER");
    		String senderMail = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_ADDRESS");

    		AddEmailIssueSelectBatchRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectBatchRequestDto.builder()
    				.senderName(senderName) // SEND_EMAIL_SENDER
    				.senderMail(senderMail) // SEND_EMAIL_ADDRESS
    				.reserveYn(reserveYn)
    				.content(content)
    				.title(title)
    				.urUserId(String.valueOf(userDormancyBatchResultVo.getUrUserId()))
    				.mail(userDormancyBatchResultVo.getMail())
    				.build();

    		sendTemplateBatchBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
    	if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    		String content = sendTemplateBatchBiz.getSMSTmplt(getEmailSendResultVo, userDormancyBatchResultVo);
    		String senderTelephone = sendTemplateBatchBiz.getPsValue("SEND_SMS_NUMBER");
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

    		AddSmsIssueSelectBatchRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectBatchRequestDto.builder()
    				.content(content)
    				.urUserId(String.valueOf(userDormancyBatchResultVo.getUrUserId()))
    				.mobile(userDormancyBatchResultVo.getMobile())
    				.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
    				.reserveYn(reserveYn)
    				.build();

    		sendTemplateBatchBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

    	}
    }

    /**
     * 휴면회원 전환 예정 Batch 실행
     */
    public void runUserDormancyExpect() {
        this.userDormancyBatchMapper = masterSqlSession.getMapper(UserDormancyBatchMapper.class);

        List<Long> userIdList = getTargetDormantExpect();
        for (Long urUserId : userIdList) {

        	 //휴면회원 전환 예정 회원 조회 vo
            UserDormancyBatchResultVo userDormancyBatchResultVo = userDormancyBatchMapper.getUserDormancyExpected(urUserId);

            //자동메일/SMS 전송
            if(StringUtils.isNotEmpty(userDormancyBatchResultVo.getMail())
            		&& StringUtils.isNotEmpty(userDormancyBatchResultVo.getMobile())) {
            	getUserDormantExpected(userDormancyBatchResultVo);
            }
        }
    }

    /**
     * 휴면회원 전환 예정 대상자 조회
     *
     * @return List<Long>
     */
    private List<Long> getTargetDormantExpect() {
        final int TARGET_DAY = 335; // 휴면전환 30일전 대상 일자
        return userDormancyBatchMapper.getTargetDormant(TARGET_DAY);
    }

    /**
     * @Desc 휴면계정 전환 예정 자동메일 발송
     * @param buyerStatusResultVo
     * @return void
     */
    private void getUserDormantExpected(UserDormancyBatchResultVo userDormancyBatchResultVo) {

    	GetEmailSendBatchResponseDto result = sendTemplateBatchBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.USER_DORMANT_EXPECTED.getCode());
    	GetEmailSendBatchResultVo getEmailSendResultVo = result.getRows();

    	//이메일 발송
    	if("Y".equals(getEmailSendResultVo.getMailSendYn())) {
    		//serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
    		String content = sendTemplateBatchBiz.getDomainManagement() + "/admin/system/emailtmplt/getUserDormantExpected?urUserId=" + userDormancyBatchResultVo.getUrUserId();
    		String title = getEmailSendResultVo.getMailTitle();
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
    		String senderName = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_SENDER");
    		String senderMail = sendTemplateBatchBiz.getPsValue("SEND_EMAIL_ADDRESS");

    		AddEmailIssueSelectBatchRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectBatchRequestDto.builder()
    				.senderName(senderName) // SEND_EMAIL_SENDER
    				.senderMail(senderMail) // SEND_EMAIL_ADDRESS
    				.reserveYn(reserveYn)
    				.content(content)
    				.title(title)
    				.urUserId(String.valueOf(userDormancyBatchResultVo.getUrUserId()))
    				.mail(userDormancyBatchResultVo.getMail())
    				.build();

    		sendTemplateBatchBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
    	}

    	//SMS 발송
    	if("Y".equals(getEmailSendResultVo.getSmsSendYn())) {

    		String content = sendTemplateBatchBiz.getSMSTmplt(getEmailSendResultVo, userDormancyBatchResultVo);
    		String senderTelephone = sendTemplateBatchBiz.getPsValue("SEND_SMS_NUMBER");
    		String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)

    		AddSmsIssueSelectBatchRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectBatchRequestDto.builder()
    				.content(content)
    				.urUserId(String.valueOf(userDormancyBatchResultVo.getUrUserId()))
    				.mobile(userDormancyBatchResultVo.getMobile())
    				.senderTelephone(senderTelephone) // SEND_SMS_NUMBER
    				.reserveYn(reserveYn)
    				.build();

    		sendTemplateBatchBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

    	}
    }
}
