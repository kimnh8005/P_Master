package kr.co.pulmuone.v1.company.dmmail.service;

import com.fasterxml.jackson.core.json.JsonReadFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.enums.DmMailEnums;
import kr.co.pulmuone.v1.comm.enums.DmMailEnums.DmMailMessage;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.company.dmmail.DmMailManageMapper;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageRequestDto;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageResponseDto;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupDetlVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DmMailManageService {
    @Value("${resource.server.url.mall}")
    private String mallDomain;

    @Value("${app.storage.public.public-storage-url}")
    private String publicStorageUrl; // public 저장소 접근 url

    private String goodsUrl = "/shop/goodsView?goods=";

    private final DmMailManageMapper dmMailManageMapper;

    public Page<DmMailVo> selectDmMailList(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageService.selectDmMailList Start");
        log.debug("# ######################################");
        //log.debug("# In.dpPageId :: " + dpPageId);
        //log.debug("# In.useAllYn :: " + useAllYn);

        // ======================================================================
        // # 초기화
        // ======================================================================
        //DisplayPageResponseDto resultResDto = new DisplayPageResponseDto();
        //resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        //resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        Page<DmMailVo> resultList = null;

        // ======================================================================
        // # 처리
        // ======================================================================

        // --------------------------------------------------------------------
        // 1.1. 세션정보 Set
        // --------------------------------------------------------------------
        if (SessionUtil.getBosUserVO() != null) {
            dmMailManageRequestDto.setCreateId((SessionUtil.getBosUserVO()).getUserId());
            dmMailManageRequestDto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        }
        else {
            dmMailManageRequestDto.setCreateId("0");
            dmMailManageRequestDto.setModifyId("0");
        }

        // ----------------------------------------------------------------------
        // # 조회
        // ----------------------------------------------------------------------
        PageMethod.startPage(dmMailManageRequestDto.getPage(), dmMailManageRequestDto.getPageSize());
        resultList = dmMailManageMapper.selectDmMailList(dmMailManageRequestDto);

        // ======================================================================
        // # 반환
        // ======================================================================
        return resultList;
    }

    public DmMailManageResponseDto selectDmMailInfo(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageService.selectDmMailInfo Start");
        log.debug("# ######################################");
        log.debug("# In.evDmMailId :: " + dmMailManageRequestDto.getDmMailId());

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        // DM메일-기본정보
        DmMailVo resultDetailInfo = null;

        // ======================================================================
        // # 처리
        // ======================================================================

        // ----------------------------------------------------------------------
        // 1.기본정보조회
        // ----------------------------------------------------------------------
        resultDetailInfo = dmMailManageMapper.selectDmMailInfo(dmMailManageRequestDto.getDmMailId());

        resultResDto.setDetail(resultDetailInfo);
        if (resultDetailInfo == null || StringUtil.isEmpty(resultDetailInfo.getDmMailId())) {
            log.debug("# DM메일 기본정보가 없습니다.");
            resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DETAIL_NO_DATA.getCode());
            resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DETAIL_NO_DATA.getMessage());
            return resultResDto;
        }
        resultResDto.setTotal(1);

        // ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }

    public DmMailManageResponseDto selectDmMailGroupList(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageService.selectDmMailGroupList Start");
        log.debug("# ######################################");
        log.debug("# In.evDmMailId :: " + dmMailManageRequestDto.getDmMailId());

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        List<DmMailGroupVo>        resultGroupList             = null;

        // ======================================================================
        // # 처리
        // ======================================================================

        // ----------------------------------------------------------------------
        // 그룹 리스트조회
        // ----------------------------------------------------------------------
        resultGroupList = dmMailManageMapper.selectDmMailGroupList(dmMailManageRequestDto.getDmMailId());
        resultResDto.setRows(resultGroupList);

        if (resultGroupList != null && resultGroupList.size() > 0) {
            resultResDto.setTotal(resultGroupList.size());
        }
        else {
            resultResDto.setTotal(0);
        }

        // ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }

    public DmMailManageResponseDto selectDmMailGroupGoodsList(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageService.selectDmMailGroupGoodsList Start");
        log.debug("# ######################################");
        log.debug("# In.evDmMailId :: " + dmMailManageRequestDto.getDmMailId());

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        List<DmMailGroupDetlVo>    resultGroupGoodsList        = null;

        // ======================================================================
        // # 처리
        // ======================================================================

        // ----------------------------------------------------------------------
        // 그룹상품 리스트조회
        // ----------------------------------------------------------------------
        resultGroupGoodsList = dmMailManageMapper.selectDmMailGroupGoodsList(dmMailManageRequestDto.getDmMailGroupId());
        resultResDto.setRows(resultGroupGoodsList);

        if (resultGroupGoodsList != null && resultGroupGoodsList.size() > 0) {
            resultResDto.setTotal(resultGroupGoodsList.size());
        }
        else {
            resultResDto.setTotal(0);
        }

        // ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 등록
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

    /**
     * DM메일 등록
     * @param dmMailManageRequestDto
     * @return
     * @throws BaseException
     */
    protected DmMailManageResponseDto addDmMail (DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageService.addDmMail Start");
        log.debug("# ######################################");
        log.debug("# In.dmMailManageRequestDto      :: " + dmMailManageRequestDto.toString());

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        int resultInt = 0;

        // ======================================================================
        // # 처리
        // ======================================================================

        try {

            // --------------------------------------------------------------------
            // 1. DM메일 기본정보 등록
            // --------------------------------------------------------------------
            // --------------------------------------------------------------------
            // 1.1. 세션정보 Set
            // --------------------------------------------------------------------
            if (SessionUtil.getBosUserVO() != null) {
                dmMailManageRequestDto.getDmMailInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
                dmMailManageRequestDto.getDmMailInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
            }
            else {
                dmMailManageRequestDto.getDmMailInfo().setCreateId("0");
                dmMailManageRequestDto.getDmMailInfo().setModifyId("0");
            }

            // 항목값 추가 Set
            dmMailManageRequestDto.getDmMailInfo().setDelYn("N");                             // 삭제여부

            resultInt = dmMailManageMapper.addDmMail(dmMailManageRequestDto.getDmMailInfo());

            if (resultInt <= 0) {
                log.debug("# DM메일 기본 정보등록 처리 오류입니다.");
                resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL_PROC.getCode());
                resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL_PROC.getMessage());
                throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL_PROC);
            }
            log.debug("# New evDmMailId :: " + dmMailManageRequestDto.getDmMailInfo().getDmMailId());

            // ---------------------------------------------------------------------
            // 1.2. DM메일 기본정보ID Set
            // ---------------------------------------------------------------------
            dmMailManageRequestDto.setDmMailId(dmMailManageRequestDto.getDmMailInfo().getDmMailId());


            // --------------------------------------------------------------------
            // 2. DM메일별 상세 등록
            // --------------------------------------------------------------------
            // ------------------------------------------------------------------
            // 일반DM메일
            // ------------------------------------------------------------------
            resultResDto = this.addDmMailGroup(dmMailManageRequestDto);

            // --------------------------------------------------------------------
            // 3. 등록 DM메일정보 Set
            // --------------------------------------------------------------------
            if (StringUtil.isEquals(resultResDto.getResultCode(), DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode())) {
                resultResDto.setDetail(dmMailManageRequestDto.getDmMailInfo());
                resultResDto.setTotal(1);
            }

        }
        catch (BaseException be) {
            log.info("# addDmMail BaseException e :: " + be.toString());
            throw be;
        }
        catch (Exception e) {
            log.info("# addDmMail Exception e :: " + e.toString());
            throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_ADD_FAIL);
        }

        //  ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }

    // ########################################################################
    // private
    // ########################################################################
    /**
     * 일반DM메일 등록
     * @param dmMailManageRequestDto
     * @return
     * @throws BaseException
     */
    private DmMailManageResponseDto addDmMailGroup(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException{
        log.debug("# ######################################");
        log.debug("# DmMailManageService.addDmMailGroup Start");
        log.debug("# ######################################");
        log.debug("# In.evDmMailId      :: " + dmMailManageRequestDto.getDmMailId());

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        int resultInt = 0;
        int resultDetlInt = 0;

        // ======================================================================
        // # 처리
        // ======================================================================

        try {

            if (StringUtil.isNotEmpty(dmMailManageRequestDto.getGroupList()) && dmMailManageRequestDto.getGroupList().size() > 0) {

                for (DmMailGroupVo dmMailGroupVo : dmMailManageRequestDto.getGroupList()) {

                    // ----------------------------------------------------------------
                    // 0. Param Set
                    // ----------------------------------------------------------------
                    // DM메일ID
                    dmMailGroupVo.setDmMailId(dmMailManageRequestDto.getDmMailId());

                    // ----------------------------------------------------------------
                    // 1. 그룹정보 등록
                    // ----------------------------------------------------------------
                    resultInt = dmMailManageMapper.addDmMailGroup(dmMailGroupVo);

                    if (resultInt <= 0) {
                        log.debug("# DM메일 그룹 정보등록 처리 오류입니다.");
                        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_ADD_FAIL_PROC.getCode());
                        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_ADD_FAIL_PROC.getMessage());
                        throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_ADD_FAIL_PROC);
                    }

                    log.debug("# New DmMailGroupId :: " + dmMailGroupVo.getDmMailGroupId());

                    // ----------------------------------------------------------------
                    // 2. 그룹상세정보 등록 (그룹상품정보)
                    // ----------------------------------------------------------------
                    if (StringUtil.isNotEmpty(dmMailGroupVo.getGroupGoodsList()) && dmMailGroupVo.getGroupGoodsList().size() > 0) {

                        for (DmMailGroupDetlVo dmMailGroupDetlVo : dmMailGroupVo.getGroupGoodsList()) {

                            // ------------------------------------------------------------
                            // 2.1. DM메일그룹ID Set
                            // ------------------------------------------------------------
                            dmMailGroupDetlVo.setDmMailGroupId(dmMailGroupVo.getDmMailGroupId());

                            // ------------------------------------------------------------
                            // 2.2. DM메일 그룹상세 등록
                            // ------------------------------------------------------------
                            resultDetlInt = dmMailManageMapper.addDmMailGroupDetl(dmMailGroupDetlVo);

                            if (resultDetlInt <= 0) {
                                log.debug("# DM메일 그룹상세 정보등록 처리 오류입니다.");
                                resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_DETL_ADD_FAIL_PROC.getCode());
                                resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_DETL_ADD_FAIL_PROC.getMessage());
                                throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_DETL_ADD_FAIL_PROC);
                            }
                            log.debug("# New DM_MAIL_GROUP_DETL_ID :: [" + dmMailGroupVo.getDmMailGroupId() + "][" + dmMailGroupDetlVo.getDmMailGroupDetlId() + "]");

                        } // End of for (DmMailGroupDetlVo dmMailGroupDetlVo : dmMailGroupVo.getGroupGoodsList())

                    } // End of if (StringUtil.isNotEmpty(dmMailGroupVo.getGroupGoodsList()) && dmMailGroupVo.getGroupGoodsList().size() > 0)

                } // End of for (DmMailGroupVo dmMailGroupVo : dmMailManageRequestDto.getGroupList())

            } // End of if (StringUtil.isNotEmpty(dmMailManageRequestDto.getGroupList()) && dmMailManageRequestDto.getGroupList().size() > 0)

        }
        catch (BaseException be) {
            log.info("# addDmMailNormal BaseException e :: " + be.toString());
            throw be;
        }
        catch (Exception e) {
            log.info("# addDmMailNormal Exception e :: " + e.toString());
            throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_GROUP_ADD_FAIL);
        }

        //  ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }

    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    // @ 수정
    // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    /**
     * DM메일 수정
     * @param dmMailManageRequestDto
     * @return
     * @throws BaseException
     */
    protected DmMailManageResponseDto putDmMail (DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageService.putDmMail Start");
        log.debug("# ######################################");
        log.debug("# In.dmMailManageRequestDto      :: " + dmMailManageRequestDto.toString());

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        int resultInt = 0;
        int resultDelInt = 0;

        // ======================================================================
        // # 처리
        // ======================================================================

        try {

            // --------------------------------------------------------------------
            // 1. DM메일 기본정보 수정
            // --------------------------------------------------------------------
            // --------------------------------------------------------------------
            // 1.1. 세션정보 Set
            // --------------------------------------------------------------------
            if (SessionUtil.getBosUserVO() != null) {
                dmMailManageRequestDto.getDmMailInfo().setCreateId((SessionUtil.getBosUserVO()).getUserId());
                dmMailManageRequestDto.getDmMailInfo().setModifyId((SessionUtil.getBosUserVO()).getUserId());
            }
            else {
                dmMailManageRequestDto.getDmMailInfo().setCreateId("0");
                dmMailManageRequestDto.getDmMailInfo().setModifyId("0");
            }

            // 항목값 추가 Set
            dmMailManageRequestDto.getDmMailInfo().setDelYn("N");                             // 삭제여부

            resultInt = dmMailManageMapper.putDmMail(dmMailManageRequestDto.getDmMailInfo());

            if (resultInt <= 0) {
                log.debug("# DM메일 기본 정보등록 처리 오류입니다.");
                resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL_PROC.getCode());
                resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL_PROC.getMessage());
                throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL_PROC);
            }
            log.debug("# evDmMailId :: " + dmMailManageRequestDto.getDmMailInfo().getDmMailId());
            // ---------------------------------------------------------------------
            // 1.2. DM메일 기본정보ID Set
            // ---------------------------------------------------------------------
            dmMailManageRequestDto.setDmMailId(dmMailManageRequestDto.getDmMailInfo().getDmMailId());

            // --------------------------------------------------------------------
            // 2. DM메일별 상세 수정
            //    - 삭제 후 재등록
            // --------------------------------------------------------------------
            // ------------------------------------------------------------------
            // 2.1. 일반DM메일
            // ------------------------------------------------------------------
            // ------------------------------------------------------------------
            // 2.1.1. 그룹상세 삭제
            // ------------------------------------------------------------------
            resultDelInt = dmMailManageMapper.delDmMailGroupDetlByDmMailId(dmMailManageRequestDto.getDmMailInfo().getDmMailId());
            log.debug("# 그룹상세 삭제 건수 :: " + resultDelInt);

            // ------------------------------------------------------------------
            // 2.1.2. 그룹 삭제
            // ------------------------------------------------------------------
            resultDelInt = dmMailManageMapper.delDmMailGroupByDmMailId(dmMailManageRequestDto.getDmMailInfo().getDmMailId());
            log.debug("# 그룹 삭제 건수 :: " + resultDelInt);

            // ------------------------------------------------------------------
            // 2.1.0. 그룹/그룹상세 등록
            // ------------------------------------------------------------------
            resultResDto = this.addDmMailGroup(dmMailManageRequestDto);

            // --------------------------------------------------------------------
            // 3. 등록 DM메일정보 Set
            // --------------------------------------------------------------------
            if (StringUtil.isEquals(resultResDto.getResultCode(), DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode())) {
                resultResDto.setDetail(dmMailManageRequestDto.getDmMailInfo());
                resultResDto.setTotal(1);
            }

        }
        catch (BaseException be) {
            log.info("# putDmMail BaseException e :: " + be.toString());
            be.printStackTrace();
            throw be;
        }
        catch (Exception e) {
            log.info("# putDmMail Exception e :: " + e.toString());
            e.printStackTrace();
            throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_PUT_FAIL);
        }

        //  ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }

    public DmMailManageResponseDto delDmMail(List<String> dmMailIdList) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageService.delDmMail Start");
        log.debug("# ######################################");
        if (dmMailIdList != null) {
            log.debug("# In.dmMailIdList.size :: " + dmMailIdList.size());
            log.debug("# In.dmMailIdList      :: " + dmMailIdList.toString());
        }
        else {
            log.debug("# In.dmMailList is Null or size 0");
        }

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        int resultInt;

        DmMailVo unitDmMailVo = null;
        List<DmMailVo> resultDmMailList = new ArrayList<>();

        // ======================================================================
        // # 처리
        // ======================================================================

        try {

            if (dmMailIdList == null || dmMailIdList.size() <= 0) {
                // 삭제대상DM메일 없음
                resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_INPUT_TARGET.getCode());
                resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_INPUT_TARGET.getMessage());
                resultResDto.setTotal(0);
                return resultResDto;
            }

            //for (DmMailVo unitDmMailVo : dmMailList) {
            for (String dmMailId : dmMailIdList) {

                unitDmMailVo = new DmMailVo();

                // ------------------------------------------------------------------
                // # 세션정보 Set
                // ------------------------------------------------------------------
                if (SessionUtil.getBosUserVO() != null) {
                    unitDmMailVo.setModifyId((SessionUtil.getBosUserVO()).getUserId());
                }
                else {
                    unitDmMailVo.setModifyId("0");
                }

                // ------------------------------------------------------------------
                // 삭제
                // ------------------------------------------------------------------
                unitDmMailVo.setDmMailId(dmMailId);
                unitDmMailVo.setDelYn("Y");
                resultInt = dmMailManageMapper.delDmMail(unitDmMailVo);

                if (resultInt <= 0) {
                    // # 한건이라도 실패할 경우 모두 롤백
                    log.debug("# 삭제건 없음");
                    resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_PROC.getCode());
                    resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_PROC.getMessage());
                    throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL_PROC);
                }
                // 현재 단건임, 다건인 경우 List로 반환 필요
//                resultResDto.setDetail(unitDmMailVo);
                resultDmMailList.add(unitDmMailVo);

            } // End of for (DmMailVo unitDmMailVo : dmMailList)
            resultResDto.setRows(resultDmMailList);
            resultResDto.setDetail(unitDmMailVo);
        }
        catch (BaseException be) {
            log.info("# delDmMail BaseException e :: " + be.toString());
            throw be;
        }
        catch (Exception e) {
            log.info("# delDmMail Exception e :: " + e.toString());
            throw new BaseException(DmMailMessage.DM_MAIL_MANAGE_DM_MAIL_DEL_FAIL);
        }

        //  ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }

    public String selectDmMailTemplateContents(String dmMailTemplateTp) throws BaseException {
        return dmMailManageMapper.selectDmMailTemplateContents(dmMailTemplateTp);
    }

    void makeMailContents(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        if (dmMailManageRequestDto == null) {
            throw new BaseException("DmMailManageRequestDto is null");
        }
        if (dmMailManageRequestDto.getDmMailId() == null) {
            throw new BaseException("DmMailId is null");
        }
        String dmMailId = dmMailManageRequestDto.getDmMailId();
        DmMailVo dmMailVo = dmMailManageMapper.selectDmMailInfo(dmMailId);
        Map<String, String> templateContents;

        String templateJson = dmMailManageMapper.selectDmMailTemplateContents(dmMailVo.getDmMailTemplateTp());

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.configure(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature(), true);
            templateContents =  mapper.readValue(templateJson, Map.class);
        } catch(Exception e) {
            throw new BaseException(e.toString());
        }
        if(templateContents != null && templateContents.size() > 0) {
            String mailContents = templateContents.get("CONTENTS");
            mailContents = mailContents.replace("{{DESCRIPTION}}", dmMailVo.getDescription());
            if (dmMailVo.getGoodsDispYn().equals("Y")) {
                String groupList = templateContents.get("GROUP_DISP_YN");
                String groupContents = templateContents.get("DM_GROUP");
                String groupDescription = templateContents.get("DM_GROUP_DESC");
                String groupGoodsContents = templateContents.get("DM_GROUP_GOODS");
                String groupGoodsPriceDiscount = templateContents.get("DM_GROUP_GOODS_PRICE_DISCOUNT");
                String groupGoodsPriceNoDiscount = templateContents.get("DM_GROUP_GOODS_PRICE_NO_DISCOUNT");
                String groupGoodsPriceNotUse = templateContents.get("DM_GROUP_GOODS_PRICE_NOT_USE");
                StringBuilder groups = new StringBuilder();
                List<DmMailGroupVo> dmMailGroupVoList = dmMailManageMapper.selectDmMailGroupList(dmMailId);
                for (DmMailGroupVo dmMailGroupVo : dmMailGroupVoList) {
                    String tempGroup = groupContents;
                    tempGroup = tempGroup.replace("{{DM_GROUP_NM}}", dmMailGroupVo.getGroupNm());
                    if(StringUtil.isNotEmpty(dmMailGroupVo.getDescription())) {
                        tempGroup = tempGroup.replace("{{DM_GROUP_DESC}}", groupDescription.replace("{{GROUP_DESCRIPTION}}", dmMailGroupVo.getDescription()));
                    } else {
                        tempGroup = tempGroup.replace("{{DM_GROUP_DESC}}", "");
                    }
                    StringBuilder goods = new StringBuilder();
                    List<DmMailGroupDetlVo> dmMailGroupDetlVoList = dmMailManageMapper.selectDmMailGroupGoodsList(dmMailGroupVo.getDmMailGroupId());
                    for (DmMailGroupDetlVo dmMailGroupDetlVo : dmMailGroupDetlVoList) {
                        String tempGoods = groupGoodsContents;
                        tempGoods = tempGoods.replace("{{DM_GROUP_GOODS_LINK}}", mallDomain + goodsUrl + dmMailGroupDetlVo.getIlGoodsId())
                                .replace("{{DM_GROUP_GOODS_IMG}}", publicStorageUrl + dmMailGroupDetlVo.getGoodsBigImagePath())
                                .replace("{{DP_BRAND_NM}}", dmMailGroupDetlVo.getDpBrandNm())
                                .replace("{{DM_GROUP_GOODS_NM}}", dmMailGroupDetlVo.getGoodsName());
                        String recommendedPrice = "";
                        String salePrice = "";
                        String discountRate = "";
                        DecimalFormat formatter = new DecimalFormat("###,###");
                        String groupGoodsPrice;
                        if (dmMailGroupVo.getDispPriceTp().equals(DmMailEnums.DispPriceTp.EMPLOYEE_PRICE.getCode())) {
                            if (dmMailGroupDetlVo.getEmployeeSalePrice().equals(dmMailGroupDetlVo.getRecommendedPrice())) {
                                groupGoodsPrice = groupGoodsPriceNoDiscount;
                            } else {
                                groupGoodsPrice = groupGoodsPriceDiscount;
                                recommendedPrice = formatter.format(Integer.parseInt(dmMailGroupDetlVo.getRecommendedPrice()));
                                discountRate = dmMailGroupDetlVo.getEmployeeDiscountRate() + "%";
                            }
                            salePrice = formatter.format(Integer.parseInt(dmMailGroupDetlVo.getEmployeeSalePrice()));
                        } else if (dmMailGroupVo.getDispPriceTp().equals(DmMailEnums.DispPriceTp.NORMAL_PRICE.getCode())) {
                            if (dmMailGroupDetlVo.getSalePrice().equals(dmMailGroupDetlVo.getRecommendedPrice())) {
                                groupGoodsPrice = groupGoodsPriceNoDiscount;
                            } else {
                                groupGoodsPrice = groupGoodsPriceDiscount;
                                recommendedPrice = formatter.format(Integer.parseInt(dmMailGroupDetlVo.getRecommendedPrice()));
                                discountRate = dmMailGroupDetlVo.getDiscountRate() + "%";
                            }
                            salePrice = formatter.format(Integer.parseInt(dmMailGroupDetlVo.getSalePrice()));
                        } else {
                            groupGoodsPrice = groupGoodsPriceNotUse;
                        }
                        groupGoodsPrice = groupGoodsPrice.replace("{{RECOMMENDED_PRICE}}", recommendedPrice)
                                .replace("{{SALE_PRICE}}", salePrice)
                                .replace("{{DISCOUNT_RATE}}", discountRate);
                        goods.append(tempGoods.replace("{{DM_GROUP_GOODS_PRICE}}", groupGoodsPrice));
                    }
                    tempGroup = tempGroup.replace("{{DM_GROUP_GOODS}}", goods.toString());
                    groups.append(tempGroup);
                }
                mailContents = mailContents.replace("{{GROUP_DISP_YN}}", groupList.replace("{{DM_GROUP}}", groups.toString()));
            } else {
                mailContents = mailContents.replace("{{GROUP_DISP_YN}}", "");
            }
            dmMailVo.setMailContents(mailContents);
            dmMailManageMapper.putDmMailContents(dmMailVo);
        } else {
            throw new BaseException("Template Contents is Null");
        }
    }

    public String getDmMailContentsPreview(String dmMailId) throws BaseException{
        return dmMailManageMapper.selectDmMailContents(dmMailId);
    }

    public DmMailManageResponseDto selectDmMailContents(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException{
        log.debug("# ######################################");
        log.debug("# DmMailManageService.selectDmMailInfo Start");
        log.debug("# ######################################");
        log.debug("# In.evDmMailId :: " + dmMailManageRequestDto.getDmMailId());

        // ======================================================================
        // # 초기화
        // ======================================================================
        DmMailManageResponseDto resultResDto = new DmMailManageResponseDto();
        resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getCode());
        resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_SUCCESS.getMessage());
        // DM메일-기본정보
        DmMailVo resultDetailInfo = new DmMailVo();

        // ======================================================================
        // # 처리
        // ======================================================================

        // ----------------------------------------------------------------------
        // 1.메일 컨텐츠 조회
        // ----------------------------------------------------------------------
        resultDetailInfo.setMailContents(dmMailManageMapper.selectDmMailContents(dmMailManageRequestDto.getDmMailId()));

        resultResDto.setDetail(resultDetailInfo);
        if (StringUtil.isEmpty(resultDetailInfo.getMailContents())) {
            log.debug("# DM메일 컨텐츠가 없습니다.");
            resultResDto.setResultCode(DmMailMessage.DM_MAIL_MANAGE_DETAIL_NO_DATA.getCode());
            resultResDto.setResultMessage(DmMailMessage.DM_MAIL_MANAGE_DETAIL_NO_DATA.getMessage());
            return resultResDto;
        }
        resultResDto.setTotal(1);

        // ======================================================================
        // # 반환
        // ======================================================================
        return resultResDto;
    }
}
