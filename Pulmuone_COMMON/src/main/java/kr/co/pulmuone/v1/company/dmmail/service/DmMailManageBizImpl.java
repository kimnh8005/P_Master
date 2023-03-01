package kr.co.pulmuone.v1.company.dmmail.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.DmMailEnums;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageRequestDto;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageResponseDto;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupDetlVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class DmMailManageBizImpl implements DmMailManageBiz{
    @Autowired
    private DmMailManageService dmMailManageService;

    @Override
    public ApiResult<?> selectDmMailList(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.info("# ######################################");
        log.info("# DmMailManageBizImpl.selectDmMailList Start");
        log.info("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.info("# In.dmMailManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.info("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        DmMailManageResponseDto result = new DmMailManageResponseDto();

        // ========================================================================
        // # 처리
        // ========================================================================
        Page<DmMailVo> voList = dmMailManageService.selectDmMailList(dmMailManageRequestDto);
        result.setTotal(voList.getTotal());
        List<DmMailVo> responseList = voList.getResult();
        //for (DmMailVo vo : responseList ) {
        //  vo.setUserGroupList(dmMailManageService.getDmMailUserGroup(vo.getEvDmMailId()));
        //}
        result.setRows(responseList);

        // ========================================================================
        // # 반환
        // ========================================================================
        return ApiResult.success(result);    }

    @Override
    public ApiResult<?> selectDmMailInfo(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.info("# ######################################");
        log.info("# DmMailManageBizImpl.selectDmMailInfo Start");
        log.info("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.info("# In.dmMailManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.info("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        DmMailManageResponseDto result = null;

        // ========================================================================
        // # 처리
        // ========================================================================
        result = dmMailManageService.selectDmMailInfo(dmMailManageRequestDto);

        // ========================================================================
        // # 반환
        // ========================================================================
        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> selectDmMailGroupList(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.info("# ######################################");
        log.info("# DmMailManageBizImpl.selectDmMailGroupList Start");
        log.info("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.info("# In.dmMailManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.info("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        DmMailManageResponseDto result = dmMailManageService.selectDmMailGroupList(dmMailManageRequestDto);

        // ========================================================================
        // # 반환
        // ========================================================================
        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> selectDmMailGroupGoodsList(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.info("# ######################################");
        log.info("# ExhibitManageBizImpl.selectExhibitGroupGoodsList Start");
        log.info("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.info("# In.dmMailManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.info("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        DmMailManageResponseDto result = null;

        // ========================================================================
        // # 처리
        // ========================================================================
        result = dmMailManageService.selectDmMailGroupGoodsList(dmMailManageRequestDto);

        // ========================================================================
        // # 반환
        // ========================================================================
        return ApiResult.success(result);    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> addDmMail(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageBizImpl.addDmMail Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ------------------------------------------------------------------------
        // 1. DM메일 기본정보 등록
        // ------------------------------------------------------------------------
        DmMailManageResponseDto result = dmMailManageService.addDmMail(dmMailManageRequestDto);

        dmMailManageService.makeMailContents(dmMailManageRequestDto);

        return ApiResult.success(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> putDmMail(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageBizImpl.putDmMail Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.dmMailManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.dmMailManageRequestDto is Null");
        }

        // ------------------------------------------------------------------------
        // 1. DM메일 기본정보 등록
        // ------------------------------------------------------------------------
        DmMailManageResponseDto result = dmMailManageService.putDmMail(dmMailManageRequestDto);

        dmMailManageService.makeMailContents(dmMailManageRequestDto);

        return ApiResult.success(result);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {BaseException.class, Exception.class})
    public ApiResult<?> delDmMail(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.debug("# ######################################");
        log.debug("# DmMailManageBizImpl.delDmMail Start");
        log.debug("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.debug("# In.exhibitManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.debug("# In.exhibitManageRequestDto is Null");
        }

        // Controller에서 List<String> 형태로 Set 한다
        DmMailManageResponseDto result = dmMailManageService.delDmMail(dmMailManageRequestDto.getDmMailIdList());

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> delDmMailGroup(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        return null;
    }

    @Override
    public ApiResult<?> delDmMailGroupDetl(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        return null;
    }

    @Override
    public ModelAndView getDmMailContentsPreview(String dmMailId) throws BaseException {
        String mailContents = dmMailManageService.getDmMailContentsPreview(dmMailId);

        ModelAndView mv = new ModelAndView();
        mv.addObject("mailContents", mailContents);
        mv.setViewName("/dm/mailPreview");
        return mv;
    }

    @Override
    public ApiResult<?> selectDmMailContents(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException {
        log.info("# ######################################");
        log.info("# DmMailManageBizImpl.selectDmMailContents Start");
        log.info("# ######################################");
        if (dmMailManageRequestDto != null) {
            log.info("# In.dmMailManageRequestDto :: " + dmMailManageRequestDto.toString());
        }
        else {
            log.info("# In.dmMailManageRequestDto is Null");
        }

        // ========================================================================
        // # 초기화
        // ========================================================================
        DmMailManageResponseDto result = null;

        // ========================================================================
        // # 처리
        // ========================================================================
        result = dmMailManageService.selectDmMailContents(dmMailManageRequestDto);

        // ========================================================================
        // # 반환
        // ========================================================================
        return ApiResult.success(result);
    }

}
