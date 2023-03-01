package kr.co.pulmuone.v1.customer.reward.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.base.service.ComnBizImpl;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.CustomerEnums;
import kr.co.pulmuone.v1.comm.enums.SendEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelWorkSheetDto;
import kr.co.pulmuone.v1.comm.util.DateUtil;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyRequestBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardApplyResponseBosDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosRequestDto;
import kr.co.pulmuone.v1.customer.reward.dto.RewardBosResponseDto;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardApplyVo;
import kr.co.pulmuone.v1.customer.reward.dto.vo.RewardBosListVo;
import kr.co.pulmuone.v1.send.template.dto.AddEmailIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.AddSmsIssueSelectRequestDto;
import kr.co.pulmuone.v1.send.template.dto.GetEmailSendResponseDto;
import kr.co.pulmuone.v1.send.template.dto.vo.GetEmailSendResultVo;
import kr.co.pulmuone.v1.send.template.service.SendTemplateBiz;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RewardBizImpl implements RewardBiz {

    @Autowired
    private RewardService rewardService;

    @Autowired
    private ComnBizImpl comnBizImpl;

    @Autowired
    private SendTemplateBiz sendTemplateBiz;

	/**
	 * 고객보상제 리스트 조회
	 * @param rewardBosRequestDto
	 * @return
	 * @throws Exception
	 */
    @Override
    public ApiResult<?> getRewardList(RewardBosRequestDto rewardBosRequestDto) throws Exception {
        RewardBosResponseDto result = new RewardBosResponseDto();
        String statusSe = rewardBosRequestDto.getStatusSe();
        List<String> statusSeList = new ArrayList<String>();

        String rewardPayTp = rewardBosRequestDto.getRewardPayTp();
        List<String> rewardPayTpList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(statusSe) && statusSe.indexOf("ALL") < 0 ) {

            statusSeList.addAll(Stream.of(statusSe.split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }
        rewardBosRequestDto.setStatusSeList(statusSeList);

        if( StringUtils.isNotEmpty(rewardPayTp) && rewardPayTp.indexOf("ALL") < 0 ) {

            rewardPayTpList.addAll(Stream.of(rewardPayTp.split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }
        rewardBosRequestDto.setRewardPayTpList(rewardPayTpList);


        Page<RewardBosListVo> voList = rewardService.getRewardList(rewardBosRequestDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }
    
    /**
	 * 고객보상제 등록
	 * @param rewardBosRequestDto
	 * @return
	 * @throws Exception
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addReward(RewardBosRequestDto rewardBosRequestDto) throws Exception {

        return ApiResult.success(rewardService.addReward(rewardBosRequestDto));
    }
    
    /**
	 * 고객보상제 수정
	 * @param rewardBosRequestDto
	 * @return
	 * @throws Exception
	 */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putReward(RewardBosRequestDto rewardBosRequestDto) throws Exception {
        return ApiResult.success(rewardService.putReward(rewardBosRequestDto));
    }

    /**
	 * 고객보상제 상세조회 - 기본정보, 지급
	 * @param rewardBosRequestDto
	 * @return
	 * @throws Exception
	 */
    @Override
    public ApiResult<?> getRewardInfo(RewardBosRequestDto rewardBosRequestDto) throws Exception {
        return ApiResult.success(rewardService.getRewardInfo(rewardBosRequestDto));
    }

     /**
	 * 고객보상제 상세조회 - 적용대상 상품
	 * @param csRewardId
	 * @return
	 * @throws Exception
	 */
    @Override
    public ApiResult<?> getRewardTargetGoodsInfo(String csRewardId) throws Exception {
        return ApiResult.success(rewardService.getRewardTargetGoodsInfo(csRewardId));
    }

     /**
     * 고객보상제 신청관리 조회
     *
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getRewardApplyList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {

        RewardApplyResponseBosDto result = new RewardApplyResponseBosDto();

        String rewardStatus = rewardApplyRequestBosDto.getRewardStatus();

        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(rewardStatus) && rewardStatus.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(rewardStatus.split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }
        rewardApplyRequestBosDto.setRewardStatusList(searchKeyList);

        Page<RewardApplyVo> voList = rewardService.getRewardApplyList(rewardApplyRequestBosDto);

        result.setTotal(voList.getTotal());
        result.setRows(voList.getResult());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> getRewardNmList(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception{
        RewardApplyResponseBosDto result = new RewardApplyResponseBosDto();

        List<RewardApplyVo> rows = rewardService.getRewardNmList(rewardApplyRequestBosDto);

        result.setRows(rows);

        return ApiResult.success(result);
    }

    ;

    /**
     * 고객보상제 신청 상세
     *
     * @throws Exception
     */
    @Override
    @UserMaskingRun(system = "MUST_MASKING")
    public ApiResult<?> getRewardApplyDetail(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        RewardApplyResponseBosDto result = new RewardApplyResponseBosDto();
        RewardApplyVo vo = new RewardApplyVo();
        vo = rewardService.getRewardApplyDetail(rewardApplyRequestBosDto);
        if(vo.getUserName() != null) {
            vo.setNoMaskUserName(vo.getUserName());
        }
        result.setRow(vo);

        return ApiResult.success(result);
    }

    /**
     * @param csRewardApplyId
     * @return ApiResult
     * @throws Exception
     * @Desc 고객보상제 신청 첨부파일 이미지
     */
    @Override
    public ApiResult<?> getImageList(String csRewardApplyId) throws Exception {
        RewardApplyResponseBosDto result = new RewardApplyResponseBosDto();

        List<RewardApplyVo> imageList = rewardService.getImageList(csRewardApplyId);
        result.setRows(imageList);

        return ApiResult.success(result);
    }

    /**
     * 처리진행 상태변경
     * @param rewardApplyRequestBosDto
     *
     * @throws Exception
     */
    @Override
    public ApiResult<?> putRewardApplyConfirmStatus(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        //메일/SMS답변여부 확인
        if (("Y".equals(rewardApplyRequestBosDto.getAnswerMailYn()) || "Y".equals(rewardApplyRequestBosDto.getAnswerSmsYn()))) {
            rewardApplyRequestBosDto.setUserName(rewardApplyRequestBosDto.getNoMaskUserName());
            getRewardApplyCompensation(rewardApplyRequestBosDto);
        }
        return rewardService.putRewardApplyConfirmStatus(rewardApplyRequestBosDto);
    }

    /**
     * 보상제 신청 상세 수정
     * @param rewardApplyRequestBosDto
     *
     * @throws Exception
     */
    @Override
    public ApiResult<?> putRewardApplyInfo(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {
        if (("Y".equals(rewardApplyRequestBosDto.getAnswerMailYn()) || "Y".equals(rewardApplyRequestBosDto.getAnswerSmsYn()))) {
            rewardApplyRequestBosDto.setUserName(rewardApplyRequestBosDto.getNoMaskUserName());
            if (CustomerEnums.RewardApplyStatus.COMPLETE.getCode().equals(rewardApplyRequestBosDto.getRewardApplyResult())) {
                // 보상 완료
                getRewardApplyComplete(rewardApplyRequestBosDto);
            } else if (CustomerEnums.RewardApplyStatus.IMPOSSIBLE.getCode().equals(rewardApplyRequestBosDto.getRewardApplyResult())) {
                // 보상불가
                getRewardApplyDeniedComplete(rewardApplyRequestBosDto);
            } else if(CustomerEnums.RewardApplyStatus.CONFIRM.getCode().equals(rewardApplyRequestBosDto.getRewardApplyResult())) {
                // 확인중
                getRewardApplyCompensation(rewardApplyRequestBosDto);
            }
        }
        return rewardService.putRewardApplyInfo(rewardApplyRequestBosDto);
    }

    /**
     * 고객보상제 신청관리내역 엑셀  다운로드
     */
    @Override
    public ExcelDownloadDto getRewardApplyListExportExcel(RewardApplyRequestBosDto rewardApplyRequestBosDto) throws Exception {

        String excelFileName = "고객보상제 신청관리" + "_" + DateUtil.getCurrentDate(); // 엑셀 파일 이름: 확장자는 xlsx 	자동 설정됨
        String excelSheetName = "sheet"; // 엑셀 파일 내 워크시트 이름

        /*
         * 배열 내 순서가 엑셀 본문의 컬럼 순서와 매칭됨
         *
         */

        /*
         * 컬럼별 width 목록 : 단위 pixel
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 너비는 120 pixel 로 고정됨
         */
        Integer[] widthListOfFirstWorksheet = { //
                300, 300, 400, 600, 200, 200, 300, 300, 300, 200, 300, 200, 400 };

        /*
         * 본문 데이터 컬럼별 정렬 목록
         * ( 필수 아님 ) ExcelWorkSheetDto 에 지정하지 않을 경우 모든 컬럼 정렬은 "left" (좌측 정렬) 로 고정
         * "left", "center", "right", "justify", "distributed" 가 아닌 다른 값 지정시 "left" (좌측 정렬) 로 지정됨
         */
        String[] alignListOfFirstWorksheet = { //
                "center", "center","center", "left","center","center", "center", "center", "center","center","center","center","center" };

        /*
         * 본문 데이터 컬럼별 데이터 property 목록
         * ( 필수 ) 엑셀 데이터 JSON 변환시 각 json Object 의 key 값과 일치해야 함
         */
        String[] propertyListOfFirstWorksheet = { //
                "csRewardApplyId", "rewardNm", "rewardExcel", "rewardApplyContent", "userName", "loginId", "delayYn", "createDate", "modifyDate", "rewardApplyStatusName", "modifyUserName", "rewardPayTpName", "rewardPayDetl"  };

        /*
         * 첫 번째 워크시트의 상단 헤더의 각 행별 정보 : 다단 구성, 셀 머지 가능
         *
         */
        String[] firstHeaderListOfFirstWorksheet = { // 첫 번째 헤더 컬럼 : 동일 이름 연속시 셀 머지
                "접수ID", "보상제명", "신청대상", "신청내용", "회원명", "회원ID", "처리지연여부", "신청일", "처리날짜", "처리상태", "처리담당자", "지급유형", "지급상세"
        };

        /*
         * 워크시트 DTO 생성 후 정보 세팅
         *
         */
        ExcelWorkSheetDto firstWorkSheetDto = ExcelWorkSheetDto.builder() //
                .workSheetName(excelSheetName) // 엑셀 파일내 워크시트 명
                .propertyList(propertyListOfFirstWorksheet) // 컬럼별 데이터 property 목록
                .widthList(widthListOfFirstWorksheet) // 컬럼별 너비 목록
                .alignList(alignListOfFirstWorksheet) // 컬럼별 정렬 목록
                .build();

        firstWorkSheetDto.setHeaderList(0, firstHeaderListOfFirstWorksheet);

        String rewardStatus = rewardApplyRequestBosDto.getRewardStatus();

        List<String> searchKeyList = new ArrayList<String>();

        if( StringUtils.isNotEmpty(rewardStatus) && rewardStatus.indexOf("ALL") < 0 ) {

            searchKeyList.addAll(Stream.of(rewardStatus.split(Constants.ARRAY_SEPARATORS))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));
        }
        rewardApplyRequestBosDto.setRewardStatusList(searchKeyList);

        List<RewardApplyVo> voList = null;
        try
        {
            voList = rewardService.getRewardApplyListExportExcel(rewardApplyRequestBosDto);
        }
        catch (Exception e)
        {
            throw e; // 추후 CustomException 으로 변환 예정
        }
        firstWorkSheetDto.setExcelDataList(voList);

        ExcelDownloadDto excelDownloadDto = ExcelDownloadDto.builder().excelFileName(excelFileName).build();

        excelDownloadDto.addExcelWorkSheet(firstWorkSheetDto);

        return excelDownloadDto;
    }




    /**
     * @param dto
     * @Desc 고객보상제 신청 답변 확인완료 이메일/SMS 전송
     */
    public void getRewardApplyCompensation(RewardApplyRequestBosDto dto) {

        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.REWARD_APPLY_COMPENSATION.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

        //이메일 발송
        if ("Y".equals(dto.getAnswerMailYn())) {
            //serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
            String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getRewardApplyCompensation?csRewardApplyId=" + dto.getCsRewardApplyId();
            String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                    .reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(String.valueOf(dto.getUrUserId()))
                    .mail(dto.getMailSend())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }

        //SMS 발송
        if ("Y".equals(dto.getAnswerSmsYn())) {

            String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, dto);
            String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                    .content(content)
                    .urUserId(String.valueOf(dto.getUrUserId()))
                    .mobile(dto.getMobileSend())
                    .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                    .reserveYn(reserveYn)
                    .build();

            sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

        }
    }

    /**
     * @param dto
     * @Desc 고객보상제 신청 답변(보상완료) 이메일/SMS 전송
     */
    public void getRewardApplyComplete(RewardApplyRequestBosDto dto) {

        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.REWARD_APPLY_COMPLETE.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

        //이메일 발송
        if ("Y".equals(dto.getAnswerMailYn())) {
            //serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
            String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getRewardApplyComplete?csRewardApplyId=" + dto.getCsRewardApplyId();
            String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                    .reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(String.valueOf(dto.getUrUserId()))
                    .mail(dto.getMailSend())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }

        //SMS 발송
        if ("Y".equals(dto.getAnswerSmsYn())) {

            String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, dto);
            String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                    .content(content)
                    .urUserId(String.valueOf(dto.getUrUserId()))
                    .mobile(dto.getMobileSend())
                    .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                    .reserveYn(reserveYn)
                    .build();

            sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

        }
    }

    /**
     * @param dto
     * @Desc 고객보상제 신청 답변(보상불가 )이메일/SMS 전송
     */
    public void getRewardApplyDeniedComplete(RewardApplyRequestBosDto dto) {

        ApiResult<?> result = sendTemplateBiz.getSendTemplateByCode(SendEnums.SendTemplateCode.REWARD_APPLY_DENIED_COMPLETE.getCode());
        GetEmailSendResultVo getEmailSendResultVo = ((GetEmailSendResponseDto) result.getData()).getRows();

        //이메일 발송
        if ("Y".equals(dto.getAnswerMailYn())) {
            //serverUrlBos -> 개발/운영  dev0shopbos.pulmuone.online 도메인 연결 확인 필요
            String content = sendTemplateBiz.getDomainManagement() + "/admin/system/emailtmplt/getRewardApplyDeniedComplete?csRewardApplyId=" + dto.getCsRewardApplyId();
            String title = getEmailSendResultVo.getMailTitle();
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            String senderName = sendTemplateBiz.getPsValue("SEND_EMAIL_SENDER");
            String senderMail = sendTemplateBiz.getPsValue("SEND_EMAIL_ADDRESS");

            AddEmailIssueSelectRequestDto addEmailIssueSelectRequestDto = AddEmailIssueSelectRequestDto.builder()
                    .senderName(senderName) // SEND_EMAIL_SENDER
                    .senderMail(senderMail) // SEND_EMAIL_ADDRESS
                    .reserveYn(reserveYn)
                    .content(content)
                    .title(title)
                    .urUserId(String.valueOf(dto.getUrUserId()))
                    .mail(dto.getMailSend())
                    .build();

            sendTemplateBiz.addEmailIssueSelect(addEmailIssueSelectRequestDto);
        }

        //SMS 발송
        if ("Y".equals(dto.getAnswerSmsYn())) {

            String content = comnBizImpl.getSMSTmplt(getEmailSendResultVo, dto);
            String senderTelephone = sendTemplateBiz.getPsValue("SEND_SMS_NUMBER");
            String reserveYn = "N"; //즉시 발송여부(N:즉시발송, Y:예약발송)
            AddSmsIssueSelectRequestDto addSmsIssueSelectRequestDto = AddSmsIssueSelectRequestDto.builder()
                    .content(content)
                    .urUserId(String.valueOf(dto.getUrUserId()))
                    .mobile(dto.getMobileSend())
                    .senderTelephone(senderTelephone) // SEND_SMS_NUMBER
                    .reserveYn(reserveYn)
                    .build();

            sendTemplateBiz.addSmsIssueSelect(addSmsIssueSelectRequestDto);

        }
    }

}
