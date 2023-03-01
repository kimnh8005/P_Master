package kr.co.pulmuone.v1.comm.mapper.promotion.notissue;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListRequestDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.vo.PointNotIssueListVo;

@Mapper
public interface PointNotIssueMapper {


	Page<PointNotIssueListVo> getPointNotIssueList(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception;

	List<PointNotIssueListVo> getPointNotIssueListExportExcel(PointNotIssueListRequestDto pointNotIssueListRequestDto) throws Exception;
}
