package kr.co.pulmuone.v1.promotion.pointhistory.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;
import kr.co.pulmuone.v1.comm.aop.service.UserMaskingRun;
import kr.co.pulmuone.v1.comm.mapper.promotion.pointhistory.PointHistoryMapper;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointAdminInfoResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointDetailHistoryRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointHistoryListRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointHistoryListResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointDetailHistoryVo;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointHistoryVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

@Slf4j
@Service
public class PointHistoryService {

	@Autowired
	private PointHistoryMapper pointHistoryMapper;

   /**
     * 적립금 내역 리스트
     * @param  pointHistoryListRequestDto
     * @return PointHistoryListResponseDto
     * @throws Exception
     */
    protected PointHistoryListResponseDto getPointNormalHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto)  throws Exception{
        PointHistoryListResponseDto result = new PointHistoryListResponseDto();

    	if(pointHistoryListRequestDto.getCondiValue() != null && !pointHistoryListRequestDto.getCondiValue().isEmpty()) {
			ArrayList<String> array = new ArrayList<>();
			StringTokenizer st = new StringTokenizer(pointHistoryListRequestDto.getCondiValue(), "\n|,");
			while(st.hasMoreElements()) {
				String object = (String)st.nextElement();
				array.add(object);
			}
			pointHistoryListRequestDto.setCondiValueArray(array);
		}
        PageMethod.startPage(pointHistoryListRequestDto.getPage(), pointHistoryListRequestDto.getPageSize());
    	Page<PointHistoryVo> pointHistoryList = pointHistoryMapper.getPointHistoryList(pointHistoryListRequestDto);
        PointHistoryListResponseDto pointDto = pointHistoryMapper.getTotalNormalPointHistory(pointHistoryListRequestDto);

        result.setTotal(pointHistoryList.getTotal());
        result.setRows(pointHistoryList.getResult());
        if(pointDto != null) {
            if(pointDto.getTotalIssuePoint() != null) {
                result.setTotalIssuePoint(pointDto.getTotalIssuePoint());
            }
            if(pointDto.getTotalUsePoint() != null) {
                result.setTotalUsePoint(pointDto.getTotalUsePoint());
            }
            if(pointDto.getTotalExpirationPoint() != null) {
                result.setTotalExpirationPoint(pointDto.getTotalExpirationPoint());
            }
            if(pointDto.getTotalMonthExpirationPoint() != null) {
                result.setTotalMonthExpirationPoint(pointDto.getTotalMonthExpirationPoint());
            }
        }

        return result;
    }

    /**
     * 올가 적립금 내역 리스트
     * @param pointHistoryListRequestDto
     * @return PointHistoryListResponseDto
     * @throws Exception
     */
    protected Page<PointHistoryVo> getPointHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto)  throws Exception{
        PageMethod.startPage(pointHistoryListRequestDto.getPage(), pointHistoryListRequestDto.getPageSize());
        return pointHistoryMapper.getPointHistoryList(pointHistoryListRequestDto);
    }

    /**
     * 적립금 상세 내역 리스트
     * @param pointDetailHistoryRequestDto
     * @return
     * @throws Exception
     */
    protected List<PointDetailHistoryVo> getPointDetailHistory(PointDetailHistoryRequestDto pointDetailHistoryRequestDto)  throws Exception{
        return pointHistoryMapper.getPointDetailHistory(pointDetailHistoryRequestDto);
    }

    /**
     * 로그인 정보 조회
     * @param pointDetailHistoryRequestDto
     * @return
     * @throws Exception
     */
    protected PointAdminInfoResponseDto getLoginInfo(PointDetailHistoryRequestDto pointDetailHistoryRequestDto)  throws Exception{
        return pointHistoryMapper.getLoginInfo(pointDetailHistoryRequestDto);
    }


    /**
     * 적립금 상세 내역 리스트
     * @param pointHistoryListRequestDto
     * @return
     * @throws Exception
     */
    protected PointAdminInfoResponseDto getTotalPointHistory(PointHistoryListRequestDto pointHistoryListRequestDto)  throws Exception{
        return pointHistoryMapper.getTotalPointHistory(pointHistoryListRequestDto);
    }



	/**
	 * 적립금 내역 엑셀 다운로드
	 *
	 * @param pointHistoryListRequestDto
	 * @return List<PointHistoryVo>
	 * @throws Exception
	 */
	@UserMaskingRun(system = "MUST_MASKING")
	protected List<PointHistoryVo> getPointHistoryListExportExcel(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception
	{
        List<PointHistoryVo> result = new ArrayList<>();


	    if("orderByList".equals(pointHistoryListRequestDto.getExcelDownType())) {
            // 주문건별 내역
            result = pointHistoryMapper.getPointHistoryListExportExcel(pointHistoryListRequestDto);
        } else if("orgaByList".equals(pointHistoryListRequestDto.getExcelDownType())) {
	        // 분담조직별 내역
            result = pointHistoryMapper.getPointHistoryOrgaListExportExcel(pointHistoryListRequestDto);
        }

        return result;
	}

}
