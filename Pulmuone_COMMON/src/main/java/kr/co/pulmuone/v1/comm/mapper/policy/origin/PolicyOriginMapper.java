package kr.co.pulmuone.v1.comm.mapper.policy.origin;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.policy.origin.dto.basic.AddPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DelPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.DuplicatePolicyOriginCountParamDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.GetPolicyOriginTypeListRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.basic.PutPolicyOriginRequestDto;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginListResultVo;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginResultVo;
import kr.co.pulmuone.v1.policy.origin.dto.vo.GetPolicyOriginTypeListResultVo;

@Mapper
public interface PolicyOriginMapper {

	/**
	 * @Desc   원산지 목록 조회
	 * @param getPolicyOriginListRequestDtoto
	 * @return Page<GetPolicyOriginListResultVo>
	 */
	Page<GetPolicyOriginListResultVo> getOriginList(GetPolicyOriginListRequestDto getPolicyOriginListRequestDto);

	/**
	 * @Desc  원산지 구분 목록 조회
	 * @param getPolicyOriginTypeListRequestDto
	 * @return List<GetPolicyOriginTypeListResultVo>
	 */
	List<GetPolicyOriginTypeListResultVo> getOriginTypeList(GetPolicyOriginTypeListRequestDto getPolicyOriginTypeListRequestDto);

    /**
     * @Desc 원산지 등록
     * @param addPolicyOriginRequestDto
     * @return int
     */
    int addOrigin(AddPolicyOriginRequestDto addPolicyOriginRequestDto);

    /**
     * @Desc 원산지 수정
     * @param putPolicyOriginRequestDto
     * @return int
     */
    int putOrigin(PutPolicyOriginRequestDto putPolicyOriginRequestDto);

    /**
     * @Desc 원산지 삭제
     * @param delPolicyOriginRequestDto
     * @return int
     */
    int delOrigin(DelPolicyOriginRequestDto delPolicyOriginRequestDto);

    /**
     * @Desc 원산지 상세 조회
     * @param getPolicyOriginRequestDto
     * @return GetPolicyOriginResultVo
     */
    GetPolicyOriginResultVo getOrigin(GetPolicyOriginRequestDto getPolicyOriginRequestDto);

    /**
     * @Desc 원산지 중복체크
     * @param duplicatePolicyOriginCountParamDto
     * @return int
     */
    int duplicateOriginCount(DuplicatePolicyOriginCountParamDto duplicatePolicyOriginCountParamDto);

    /**
	 * @Desc 원산지 목록 조회 엑셀다운로드
	 * @param GetPolicyOriginListRequestDto
	 * @return List<GetPolicyOriginListResultVo>
	 */
	List<GetPolicyOriginListResultVo> getOriginListExportExcel(GetPolicyOriginListRequestDto dto);

}
