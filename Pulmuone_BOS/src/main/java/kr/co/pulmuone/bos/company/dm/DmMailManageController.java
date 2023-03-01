package kr.co.pulmuone.bos.company.dm;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.annotations.*;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DmMailEnums.DmMailMessage;
import kr.co.pulmuone.v1.comm.util.BindUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageRequestDto;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageResponseDto;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupDetlVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailVo;
import kr.co.pulmuone.v1.company.dmmail.service.DmMailManageBiz;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class DmMailManageController {

    final String JOB_ADD = "ADD";
    final String JOB_PUT = "PUT";

    @Autowired
    DmMailManageBiz dmMailManageBiz;

    @Autowired(required=true)
    private HttpServletRequest request;

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 조회
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * 전시페이지 DM메일 리스트조회
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/selectDmMailList")
    @ApiOperation(value = "DM메일리스트조회")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "DM_MAIL_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
            )
    })
    @ResponseBody
    public ApiResult<?> selectDmMailList(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.selectDmMailList Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
            log.debug("# Pg.page     :: " + dmMailManageRequestDto.getPage());
            log.debug("# Pg.sPage    :: " + dmMailManageRequestDto.getsPage());
            log.debug("# Pg.ePage    :: " + dmMailManageRequestDto.getePage());
            log.debug("# Pg.pageSize :: " + dmMailManageRequestDto.getPageSize());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        DmMailManageRequestDto reqDto = null;
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }

        reqDto = (DmMailManageRequestDto) BindUtil.convertRequestToObject(request, DmMailManageRequestDto.class);
        log.debug("# DmMailManageController.reqDto[1] :: " + reqDto.toString());

        // 코드값에 ALL 문자열이 포함되어 있어서  indexOf("ALL") != 0 으로 둠, 이 경우 ALL은 항상 맨 먼저 나타나게 해야 함
        // 참고
        //  1) indexOf("ALL") < 0 => ALL 이 아닌 경우
        //  2) indexOf("ALL") > 0 => ALL 이 아니지만, MALL_DIV에서 ALL을 인식한 경우
        // 진행기간-시작일자
        if (StringUtil.isNotEmpty(reqDto.getStartDt())) {
            reqDto.setStartDt(reqDto.getStartDt() + "000000");
        }
        // 진행기간-종료일자
        if (StringUtil.isNotEmpty(reqDto.getEndDt())) {
            reqDto.setEndDt(reqDto.getEndDt() + "235959");
        }
        log.debug("# DmMailManageController.reqDto[2] :: " + reqDto.toString());

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # BOS 서비스 호출
        return dmMailManageBiz.selectDmMailList(reqDto);
        //return dmMailManageBiz.selectDmMailList((DmMailManageRequestDto) BindUtil.convertRequestToObject(request, DmMailManageRequestDto.class));

    }

    /**
     * DM메일상세조회-기본정보
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/selectDmMailInfo")
    @ApiOperation(value = "DM메일상세조회-기본정보")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "DM_MAIL_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                    + "DM_MAIL_MANAGE_PARAM_NO_DM_MAIL_ID - DM메일ID를 입력하세요."
            )
    })
    @ResponseBody
    public ApiResult<?> selectDmMailInfo(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.selectDmMailInfo Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
            log.debug("# Pg.page     :: " + dmMailManageRequestDto.getPage());
            log.debug("# Pg.sPage    :: " + dmMailManageRequestDto.getsPage());
            log.debug("# Pg.ePage    :: " + dmMailManageRequestDto.getePage());
            log.debug("# Pg.pageSize :: " + dmMailManageRequestDto.getPageSize());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }
        if (StringUtil.isEmpty(dmMailManageRequestDto.getDmMailId())) {
            // DM메일ID를 입력하세요.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_DM_MAIL_ID);
        }
        log.debug("# In.dmMailId :: " + dmMailManageRequestDto.getDmMailId());

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # BOS 서비스 호출
        return dmMailManageBiz.selectDmMailInfo(dmMailManageRequestDto);

    }

    /**
     * DM메일 상세조회 - 일반(그룹) - 그룹리스트
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/selectDmMailGroupList")
    @ApiOperation(value = "DM메일상세조회-일반-그룹리스트")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "EXHIBIT_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                    + "EXHIBIT_MANAGE_PARAM_NO_EXHIBIT_ID - DM메일ID를 입력하세요."
            )
    })
    @ResponseBody
    public ApiResult<?> selectDmMailGroupList(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.selectDmMailGroupList Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }
        if (StringUtil.isEmpty(dmMailManageRequestDto.getDmMailId())) {
            // DM메일ID를 입력하세요.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_DM_MAIL_ID);
        }
        log.debug("# In.dmMailId :: " + dmMailManageRequestDto.getDmMailId());

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # BOS 서비스 호출
        return dmMailManageBiz.selectDmMailGroupList(dmMailManageRequestDto);

    }

    /**
     * DM메일 상세조회 - 일반(그룹) - 그룹상품리스트
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/selectDmMailGroupGoodsList")
    @ApiOperation(value = "DM메일상세조회-일반-그룹상품리스트")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "DM_MAIL_MANAGE_PARAM_NO_INPUT - 입력정보가 존재하지 않습니다."
                    + "DM_MAIL_MANAGE_PARAM_NO_DM_MAIL_ID - 그룹ID를 입력하세요."
            )
    })
    @ResponseBody
    public ApiResult<?> selectDmMailGroupGoodsList(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.selectDmMailGroupGoodsList Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }
        if (StringUtil.isEmpty(dmMailManageRequestDto.getDmMailGroupId())) {
            // 그룹ID를 입력하세요.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_DM_MAIL_GROUPT_ID);
        }
        log.debug("# In.evDmMailGroupId :: " + dmMailManageRequestDto.getDmMailGroupId());

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # BOS 서비스 호출
        return dmMailManageBiz.selectDmMailGroupGoodsList(dmMailManageRequestDto);

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 등록
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * DM메일 등록
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/addDmMail")
    @ApiOperation(value = "DM메일등록")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL_INPUT_TARGET - DM메일 기본 정보등록 입력정보 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL - DM메일 기본 정보등록 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL_PROC - DM메일 기본 정보등록 처리 오류입니다."
            )
    })
    @ResponseBody
    public ApiResult<?> addDmMail(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.addDmMail Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }
        if (StringUtil.isEmpty(dmMailManageRequestDto.getDmMailDataJsonString())) {
            // DM메일 기본 정보등록 입력정보 오류입니다
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL_INPUT_TARGET);
        }

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # 입력데이터Set + BOS 서비스 호출
        return this.procDmMail(dmMailManageRequestDto, JOB_ADD);

    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 수정
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * DM메일 수정
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/putDmMail")
    @ApiOperation(value = "DM메일 수정")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL_INPUT_TARGET - DM메일 기본 정보수정 입력정보 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL - DM메일 기본 정보수정 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL_PROC - DM메일 기본 정보수정 처리 오류입니다."
            )
    })
    @ResponseBody
    public ApiResult<?> putDmMail(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.putDmMail Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }
        if (StringUtil.isEmpty(dmMailManageRequestDto.getDmMailDataJsonString())) {
            // DM메일 기본 정보등록 입력정보 오류입니다
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL_INPUT_TARGET);
        }

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # 입력데이터Set + BOS 서비스 호출
        return this.procDmMail(dmMailManageRequestDto, JOB_PUT);

    }

    // ########################################################################
    // private
    // ########################################################################
    /**
     * 등록/수정 입력데이터 Set
     * @param dmMailManageRequestDto
     * @param jobSe
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unused")
    private  ApiResult<?> procDmMail(DmMailManageRequestDto dmMailManageRequestDto, String jobSe) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.procDmMail Start");
        log.debug("# ######################################");

        // ========================================================================
        // # 처리
        // ========================================================================
        // ------------------------------------------------------------------------
        // 데이터 변환 - 기본정보
        // ------------------------------------------------------------------------
        // ------------------------------------------------------------------------
        // @ 1. DM메일 기본정보 변환 : JsonString -> DmMailVo
        // ------------------------------------------------------------------------
        try {
            ObjectMapper objMqpper = new ObjectMapper();
            dmMailManageRequestDto.setDmMailInfo(objMqpper.readValue(dmMailManageRequestDto.getDmMailDataJsonString(), DmMailVo.class));
        }
        catch (Exception e) {
            log.error("# In.dmMailManageRequestDto(1) :: " + dmMailManageRequestDto.toString());
            if (StringUtil.isEquals(jobSe, JOB_ADD)) {
                return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL);
            }
            else {
                return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL);
            }
        }

        // **********************************************************************
        // 2.1. 일반DM메일
        // **********************************************************************
        // ----------------------------------------------------------------------
        // @ 2.1.1. DM메일 그룹리스트정보 변환 : JsonString -> List<DmMailGroupVo>
        // ----------------------------------------------------------------------
        try {
            if (StringUtil.isNotEmpty(dmMailManageRequestDto.getGroupListJsonString())) {
                dmMailManageRequestDto.setGroupList(BindUtil.convertJsonArrayToDtoList(dmMailManageRequestDto.getGroupListJsonString(), DmMailGroupVo.class));
            }
        } catch (Exception e) {
            log.error("# In.dmMailManageRequestDto(2) :: " + dmMailManageRequestDto.toString());
            log.error("# e :: " + e.toString());
            e.printStackTrace();
            if (StringUtil.isEquals(jobSe, JOB_ADD)) {
                return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_ADD_FAIL_INPUT_TARGET);
            }
            else {
                return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_PUT_FAIL_INPUT_TARGET);
            }
        }

        // ----------------------------------------------------------------------
        // @ 2.1.2. DM메일 그룹리스트 내 그룹상품리스트 변환
        // ----------------------------------------------------------------------
        if (dmMailManageRequestDto.getGroupList() != null && dmMailManageRequestDto.getGroupList().size() > 0) {
            for (DmMailGroupVo dmMailGroupVo : dmMailManageRequestDto.getGroupList()) {

                // ----------------------------------------------------------------------
                // @ DM메일 그룹리스트정보 변환 : JsonString -> List<dmMailGroupGoodsVo>
                // ----------------------------------------------------------------------
                try {
                    if (StringUtil.isNotEmpty(dmMailGroupVo.getGroupGoodsListJsonString())) {
                        dmMailGroupVo.setGroupGoodsList(BindUtil.convertJsonArrayToDtoList(dmMailGroupVo.getGroupGoodsListJsonString(), DmMailGroupDetlVo.class));
                    }
                }
                catch (Exception e) {
                    log.error("# In.dmMailManageRequestDto(3) :: " + dmMailManageRequestDto.toString());
                    if (StringUtil.isEquals(jobSe, JOB_ADD)) {
                        return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_DETL_ADD_FAIL_INPUT_TARGET);
                    }
                    else {
                        return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_DETL_PUT_FAIL_INPUT_TARGET);
                    }
                }
            }
        } // End of if (dmMailManageRequestDto.getGroupList() != null && dmMailManageRequestDto.getGroupList().size() > 0)

        // ------------------------------------------------------------------------
        // 임시로그 Start
        log.debug("# In.dmMailManageRequestDto(8) :: " + dmMailManageRequestDto.toString());
        if (StringUtil.isNotEmpty(dmMailManageRequestDto.getDmMailInfo())) {
            log.debug("# In.DmMailInfo :: " + dmMailManageRequestDto.getDmMailInfo().toString());
        }
        else {
            log.debug("# In.getDmMailInfo is Null");
        }
        if (StringUtil.isNotEmpty(dmMailManageRequestDto.getGroupList())) {
            log.debug("# In.GroupList   :: " + dmMailManageRequestDto.getGroupList().toString());
        }
        else {
            log.debug("# In.getGroupList is Null");
        }

        if (StringUtil.isNotEmpty(dmMailManageRequestDto.getGroupList()) && dmMailManageRequestDto.getGroupList().size() > 0) {

            int groupIdx = 1;
            for (DmMailGroupVo dmMailGroupVo : dmMailManageRequestDto.getGroupList()) {
                if (StringUtil.isNotEmpty(dmMailGroupVo.getGroupGoodsList()) && dmMailGroupVo.getGroupGoodsList().size() > 0) {
                    for (DmMailGroupDetlVo dmMailGroupDetlVo : dmMailGroupVo.getGroupGoodsList()) {
                        log.debug("# [" + dmMailGroupVo.getGroupNm() + "]["+groupIdx+"] [" + dmMailGroupDetlVo.getIlGoodsId() + "][" + dmMailGroupDetlVo.getGoodsSort() + "]");
                    }
                }
                else {
                    log.debug("# [" + dmMailGroupVo.getDmMailGroupId() + "] Not Exists Goods");
                }
                groupIdx++;
            }
        }
        // 임시로그 End
        // ------------------------------------------------------------------------

        // ========================================================================
        // # 반환
        // ========================================================================
        // # BOS 서비스 호출
        if (StringUtil.isEquals(jobSe, JOB_ADD)) {
            // 등록
            return dmMailManageBiz.addDmMail(dmMailManageRequestDto);
        }
        else if (StringUtil.isEquals(jobSe, JOB_PUT)) {
            // 수정
            return dmMailManageBiz.putDmMail(dmMailManageRequestDto);
        }
        else {
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_NO_JOB);
        }
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 삭제
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * DM메일 삭제
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/delDmMail")
    @ApiOperation(value = "DM메일삭제")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_INPUT_TARGET - DM메일 기본 정보삭제 입력정보 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL - DM메일 기본 정보삭제 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_PROC - DM메일 기본 정보삭제 처리 오류입니다."
            )
    })
    @ResponseBody
    public ApiResult<?> delDmMail(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ######################################");
        log.debug("# DmMailManageController.delDmMail Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }
        if (StringUtil.isEmpty(dmMailManageRequestDto.getDmMailIdListString())) {

            // DM메일 기본 정보삭제 입력정보 오류입니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_INPUT_TARGET);
        }

        // evDmMailId JsonString -> List<String> 으로 변환하여  dmMailManageRequestDto.setEvDmMailIdList 에 넣음
        try {
            dmMailManageRequestDto.setDmMailIdList(BindUtil.convertJsonArrayToDtoList(dmMailManageRequestDto.getDmMailIdListString(), String.class));
        }
        catch (Exception e) {
            //e.printStackTrace();
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_INPUT_TARGET_CONVERT);
        }
        log.debug("# In.dmMailIdList :: " + dmMailManageRequestDto.getDmMailIdList().toString());

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # BOS 서비스 호출
        return dmMailManageBiz.delDmMail(dmMailManageRequestDto);

    }

    /*
     * 이메일 발송 위한 호출용
     * 이메일 템플릿 html return 필요
     */
    @ApiOperation(value = "DM메일 미리보기")
    @GetMapping(value = "/admin/comn/getDmMailPreview")
    public ModelAndView getDmMailContentsPreview(@RequestParam(value = "dmMailId", required = true) String dmMailId) throws Exception{
        return dmMailManageBiz.getDmMailContentsPreview(dmMailId);
    }

    /**
     * DM메일 삭제
     * @param dmMailManageRequestDto
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/admin/dm/mail/selectDmMailContents")
    @ApiOperation(value = "DM메일 HTML")
    @ApiResponses(value = { @ApiResponse(code = 900, message = "response data", response = DmMailManageResponseDto.class),
            @ApiResponse(code = 901, message = ""
                    + "DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_INPUT_TARGET - DM메일 기본 정보삭제 입력정보 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL - DM메일 기본 정보삭제 오류입니다."
                    + "DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_PROC - DM메일 기본 정보삭제 처리 오류입니다."
            )
    })
    @ResponseBody
    public ApiResult<?> selectDmMailContents(DmMailManageRequestDto dmMailManageRequestDto) throws Exception {
        log.debug("# ##################################################");
        log.debug("# DmMailManageController.selectDmMailContents Start");
        log.debug("# ##################################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto     :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        // # 입력값 체크
        if (dmMailManageRequestDto == null) {
            // 입력정보가 존재하지 않습니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_PARAM_NO_INPUT);
        }
        if (StringUtil.isEmpty(dmMailManageRequestDto.getDmMailId())) {

            // DM메일 기본 정보삭제 입력정보 오류입니다.
            return ApiResult.result(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_INPUT_TARGET);
        }

        // ========================================================================
        // # 처리 및 반환
        // ========================================================================
        // # BOS 서비스 호출
        return dmMailManageBiz.selectDmMailContents(dmMailManageRequestDto);

    }

}
