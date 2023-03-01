package kr.co.pulmuone.v1.comm.mapper.customer.stndpnt;

import java.util.List;

import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointMallRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.StandingPointQnaAttachVo;
import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.customer.qna.dto.QnaBosRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.StandingPointRequestDto;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointAttachResultVo;
import kr.co.pulmuone.v1.customer.stndpnt.dto.vo.GetStandingPointListResultVo;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface StandingPointMapper {

	Page<GetStandingPointListResultVo> getStandingPointList(StandingPointRequestDto standingPointRequestDto) throws Exception;

	List<GetStandingPointListResultVo> getStandingPointExportExcel(StandingPointRequestDto standingPointRequestDto);

	GetStandingPointListResultVo getDetailStandingPoint (StandingPointRequestDto standingPointRequestDto) throws Exception;

	GetStandingPointAttachResultVo getStandingPointAttach(StandingPointRequestDto standingPointRequestDto);

	void putStatusReview(StandingPointRequestDto standingPointRequestDto) throws Exception;

	int putStandingPointStatus(StandingPointRequestDto standingPointRequestDto);

	void addStandingPointQna(StandingPointMallRequestDto dto) throws Exception;

	void addStandingPointQnaAttach(@Param("csStandPntId") Long csStandPntId, @Param("createId") Long createId, @Param("voList") List<StandingPointQnaAttachVo> voList) throws Exception;

}
