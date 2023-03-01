package kr.co.pulmuone.v1.customer.stndpnt.service;

import java.util.List;

import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.mapper.customer.stndpnt.StandingPointMapper;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointAttachResultVo;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointListResultVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StandingPointService{

	private final StandingPointMapper standingPointMapper;

	/**
	 * @Desc 상품입점 상담 관리 리스트 조회
	 * @param StandingPointRequestDto
	 * @throws Exception
	 * @return Page<GetStandingPoinntListResultVo>
	 */
	@UserMaskingRun(system="BOS")
	protected Page<GetStandingPointListResultVo> getStandingPointList(StandingPointRequestDto standingPointRequestDto) throws Exception {

		//상품입점 상담 조회 결과 페이지
		PageMethod.startPage(standingPointRequestDto.getPage(), standingPointRequestDto.getPageSize());
        return standingPointMapper.getStandingPointList(standingPointRequestDto);
	}


    /**
     * @Desc 상품입점 상담 관리 리스트 엑셀 다운로드 목록 조회
     * @param StandingPointRequestDto : 상품입점 상담 관리 리스트 검색 조건 request dto
     * @return List<GetStandingPoinntListResultVo> : 상품입점 상담 관리 리스트 엑셀 다운로드 목록
     */
 	@UserMaskingRun(system="BOS")
    public List<GetStandingPointListResultVo> getStandingPointExportExcel(StandingPointRequestDto standingPointRequestDto) {


        List<GetStandingPointListResultVo> itemList = standingPointMapper.getStandingPointExportExcel(standingPointRequestDto);

        // 화면과 동일하게 역순으로 no 지정
        for (int i = itemList.size() - 1; i >= 0; i--) {
            itemList.get(i).setRowNumber(String.valueOf(itemList.size() - i));
        }

        return itemList;
    }

    /**
     * 상품입점 상담 관리 상세조회
     * @param StandingPointRequestDto
     * @return
     * @throws Exception
     */
    @UserMaskingRun(system="BOS")
    protected GetStandingPointListResultVo getDetailStandingPoint(StandingPointRequestDto standingPointRequestDto)throws Exception {
    	return standingPointMapper.getDetailStandingPoint(standingPointRequestDto);
    }




    /**
     * 상품입점 상담 관리 첨부파일조회
     * @param StandingPointRequestDto
     * @return
     * @throws Exception
     */
    protected GetStandingPointAttachResultVo getStandingPointAttach(StandingPointRequestDto standingPointRequestDto)throws Exception {
    	return standingPointMapper.getStandingPointAttach(standingPointRequestDto);
    }



 	/**
 	 * 상품입점문의 승인 상태변경
 	 * @param dto
 	 * @return
 	 * @throws Exception
 	 */
 	protected ApiResult<?> putStandingPointStatus(StandingPointRequestDto standingPointRequestDto) throws Exception {

 		int result = standingPointMapper.putStandingPointStatus(standingPointRequestDto);

        return ApiResult.success(result);
    }

	/**
	 * 상품입점상담 등록
	 * @param dto StandingPointMallRequestDto
	 * @throws Exception
	 */
	protected void addStandingPointQna(StandingPointMallRequestDto dto) throws Exception {
		standingPointMapper.addStandingPointQna(dto);

		if(dto.getFile() != null && dto.getFile().size() > 0){
			standingPointMapper.addStandingPointQnaAttach(dto.getCsStandPntId(), dto.getCreateId(), dto.getFile());
		}
	}

}
