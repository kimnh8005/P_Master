package kr.co.pulmuone.v1.comm.mapper.promotion.pointhistory;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointAdminInfoResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointDetailHistoryRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointHistoryListRequestDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.PointHistoryListResponseDto;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointDetailHistoryVo;
import kr.co.pulmuone.v1.promotion.pointhistory.dto.vo.PointHistoryVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PointHistoryMapper {

	Page<PointHistoryVo> getPointHistoryList(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;

	List<PointDetailHistoryVo> getPointDetailHistory(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception;

	PointAdminInfoResponseDto getLoginInfo(PointDetailHistoryRequestDto pointDetailHistoryRequestDto) throws Exception;

	PointHistoryListResponseDto getTotalNormalPointHistory(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;

	PointAdminInfoResponseDto getTotalPointHistory(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;

	List<PointHistoryVo> getPointHistoryListExportExcel(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;

	List<PointHistoryVo> getPointHistoryOrgaListExportExcel(PointHistoryListRequestDto pointHistoryListRequestDto) throws Exception;
}
