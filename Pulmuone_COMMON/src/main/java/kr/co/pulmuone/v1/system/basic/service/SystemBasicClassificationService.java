package kr.co.pulmuone.v1.system.basic.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.system.basic.dto.GetClassificationListParamDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetClassificationListResultVo;
import kr.co.pulmuone.v1.comm.mapper.system.basic.SystemBasicClassificationMapper;
import kr.co.pulmuone.v1.system.basic.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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
public class SystemBasicClassificationService {
	private final SystemBasicClassificationMapper mapper;

	/**
	 * 분류관리
	 */
	protected GetClassificationListResponseDto getClassificationList(GetClassificationListParamDto dto){
		GetClassificationListResponseDto result = new GetClassificationListResponseDto();
		
		PageMethod.startPage(dto.getPage(), dto.getPageSize());
		Page<GetClassificationListResultVo> rows = mapper.getClassificationList(dto);
		
		result.setTotal((int)rows.getTotal());
		result.setRows(rows.getResult());

		return result;
	}


	/**
	 * 분류관리 상세조회
	 */
	protected GetClassificationResponseDto getClassification(Long stClassificationId){
		GetClassificationResponseDto result = new GetClassificationResponseDto();
		result.setRows(mapper.getClassification(stClassificationId));
		return result;
	}

	/**
	 * 중복 데이터 체크
	 */
	protected boolean checkClassificationDuplicate(SaveClassificationRequestDto dto) {
		if(mapper.checkClassificationDuplicate(dto) > 0) {
			return false;
		}
		return true;
	}

	/**
	 * 분류관리 삽입
	 */
	protected int addClassification(SaveClassificationRequestDto dto) {
		return mapper.addClassification(dto);
	}

	/**
	 * 분류관리 수정
	 */
	protected int putClassification(SaveClassificationRequestDto dto) {
		return mapper.putClassification(dto);
	}

	/**
	 * 분류관리 삭제
	 */
	protected int delClassification(Long id) {
		return mapper.delClassification(id);
	}

	/**
	 * 분류코드 리스트 조회
	 */
	protected GetTypeListResponseDto getTypeList() {
		GetTypeListResponseDto result = new GetTypeListResponseDto();

		List<GetTypeListResultVo> rows = mapper.getTypeList();	// rows

		result.setRows(rows);

		return result;
	}


}

