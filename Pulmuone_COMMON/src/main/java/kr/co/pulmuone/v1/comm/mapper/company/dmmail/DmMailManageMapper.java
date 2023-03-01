package kr.co.pulmuone.v1.comm.mapper.company.dmmail;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.company.dmmail.dto.DmMailManageRequestDto;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupDetlVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailGroupVo;
import kr.co.pulmuone.v1.company.dmmail.dto.vo.DmMailVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DmMailManageMapper {
    // 조회
    Page<DmMailVo> selectDmMailList(DmMailManageRequestDto dmMailManageRequestDto) throws BaseException;

    DmMailVo selectDmMailInfo(String dmMailId) throws BaseException;

    List<DmMailGroupVo> selectDmMailGroupList(String dmMailId) throws BaseException;

    List<DmMailGroupDetlVo> selectDmMailGroupGoodsList(String dmMailGroupId) throws BaseException;

    // ==========================================================================
    // 생성
    // ==========================================================================
    // DM메일 생성 - 기본
    int addDmMail (DmMailVo dmMailVo) throws BaseException;

    // DM메일 생성 - 전시그룹
    int addDmMailGroup (DmMailGroupVo dmMailGroupVo) throws BaseException;

    // DM메일 생성 - 전시그룹상세
    int addDmMailGroupDetl (DmMailGroupDetlVo dmMailGroupDetlVo) throws BaseException;

    // ==========================================================================
    // 수정
    // ==========================================================================
    // DM메일 수정 - 기본
    int putDmMail (DmMailVo dmMailVo) throws BaseException;

    int putDmMailContents(DmMailVo dmMailVo) throws BaseException;
    // ==========================================================================
    // 삭제
    // ==========================================================================
    // DM메일 삭제 - 기본
    int delDmMail (DmMailVo dmMailVo) throws BaseException;

    // DM메일 삭제 - 전시그룹 - DM메일ID 기준
    int delDmMailGroupByDmMailId (@Param("dmMailId") String dmMailId) throws BaseException;

    // DM메일 삭제 - 전시그룹상세 - DM메일ID 기준
    int delDmMailGroupDetlByDmMailId (@Param("dmMailId") String dmMailId) throws BaseException;
    
    // 개별 삭제 - 전시그룹
    int delDmMailGroup (@Param("evdmMailGroupId") String dmMailGroupId) throws BaseException;

    // 개별 삭제 - 전시그룹상세
    int delDmMailGroupDetl (@Param("evdmMailGroupDetlId") String dmMailGroupDetlId) throws BaseException;

    String selectDmMailTemplateContents (@Param("dmMailTemplateTp") String dmMailTemplateTp) throws BaseException;

    String selectDmMailContents(@Param("dmMailId") String dmMailId) throws BaseException;
}
