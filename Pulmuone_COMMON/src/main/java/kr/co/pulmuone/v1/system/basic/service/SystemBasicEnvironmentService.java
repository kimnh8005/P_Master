package kr.co.pulmuone.v1.system.basic.service;

import java.util.ArrayList;
import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.GetEnvironmentListResponseDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.SaveEnvironmentRequestSaveDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetEnvironmentListResultVo;
import kr.co.pulmuone.v1.comm.mapper.system.basic.SystemBasicEnvironmentMapper;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200529    오영민              최초작성
 * =======================================================================
 * </PRE>
 */

@RequiredArgsConstructor
@Service
public class SystemBasicEnvironmentService {

	private final SystemBasicEnvironmentMapper systemBasicEnvironmentMapper;

	/**
	 * 시스템
	 */
	protected GetEnvironmentListResponseDto getEnvironmentList(GetEnvironmentListRequestDto dto) {

		GetEnvironmentListResponseDto result 	= new GetEnvironmentListResponseDto();

		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetEnvironmentListResultVo> rows 	= systemBasicEnvironmentMapper.getEnvironmentList(dto);

		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}

	/**
	 * 시스템 저장
	 */
	protected ApiResult<?> saveEnvironment(SaveEnvironmentRequestDto dto){
		List<SaveEnvironmentRequestSaveDto> insertRequestDtoList = dto.getInsertRequestDtoList();
		List<SaveEnvironmentRequestSaveDto> updateRequestDtoList = dto.getUpdateRequestDtoList();
		List<SaveEnvironmentRequestSaveDto> deleteRequestDtoList = dto.getDeleteRequestDtoList();

		MessageCommEnum returnCode = this.checkEnvironmentDuplicate(insertRequestDtoList);
		if(BaseEnums.Default.SUCCESS == returnCode) {

			//데이터 저장
			if(!insertRequestDtoList.isEmpty()){
				systemBasicEnvironmentMapper.addEnvironment(insertRequestDtoList);
			}
			//데이터 수정
			if(!updateRequestDtoList.isEmpty()){
				systemBasicEnvironmentMapper.putEnvironment(updateRequestDtoList);
			}
			//데이터 삭제
			if(!deleteRequestDtoList.isEmpty()){
				systemBasicEnvironmentMapper.delEnvironment(deleteRequestDtoList);
			}
		}else {
			ApiResult.result(returnCode);
		}

		return ApiResult.success();
	}


	/**
	 * 중복 데이터 체크
	 * @param dtoList
	 * @return
	 * @throws Exception
	 */
	protected MessageCommEnum checkEnvironmentDuplicate(List<SaveEnvironmentRequestSaveDto> dtoList){

		MessageCommEnum  returnCode 	= BaseEnums.Default.SUCCESS;

		List<String> environmentKeyList 	= new ArrayList<String>();
		for(int i=0; i < dtoList.size(); i++) {
			String environmentKey 			= dtoList.get(i).getEnvironmentKey();
			if(i != 0 && environmentKeyList.contains(environmentKey)) {
				returnCode 				= BaseEnums.CommBase.DUPLICATE_DATA;
				return returnCode;
			}
			environmentKeyList.add(environmentKey);
		}

		int count 								= 0;
		if(!environmentKeyList.isEmpty()) {
			count 								= systemBasicEnvironmentMapper.checkEnvironmentDuplicate(environmentKeyList);
			if(count > 0) {
				returnCode 						= BaseEnums.CommBase.DUPLICATE_DATA;
				return returnCode;
			}
		}

		return returnCode;
	}


	/**
	 * 환경설정 조회
	 */
	protected GetEnvironmentListResultVo getEnvironment(GetEnvironmentListRequestDto dto) {
		return systemBasicEnvironmentMapper.getEnvironment(dto);
	}


	/**
	 * 환경설정 정보 저장
	 * @param saveEnvironmentRequestSaveDto
	 * @return
	 * @throws Exception
	 */
	protected GetEnvironmentListResponseDto putEnvironmentEnvVal(SaveEnvironmentRequestSaveDto saveEnvironmentRequestSaveDto) {
		GetEnvironmentListResponseDto getEnvironmentListResponseDto = new GetEnvironmentListResponseDto();
		systemBasicEnvironmentMapper.putEnvironmentEnvVal(saveEnvironmentRequestSaveDto);

		return getEnvironmentListResponseDto;
	}




}

