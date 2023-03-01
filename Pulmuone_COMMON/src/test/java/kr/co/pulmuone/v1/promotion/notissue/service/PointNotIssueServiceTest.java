package kr.co.pulmuone.v1.promotion.notissue.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.promotion.notissue.dto.PointNotIssueListRequestDto;
import kr.co.pulmuone.v1.promotion.notissue.dto.vo.PointNotIssueListVo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PointNotIssueServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	PointNotIssueService pointNotIssueService;


	@Test
    void getPointNotIssueList_정상() throws Exception {

		PointNotIssueListRequestDto dto = new PointNotIssueListRequestDto();

        Page<PointNotIssueListVo> voList = pointNotIssueService.getPointNotIssueList(dto);

        assertTrue(voList.getResult().size() > 0);

    }


	@Test
    void getPointNotIssueListExportExcel_정상() throws Exception {

		PointNotIssueListRequestDto dto = new PointNotIssueListRequestDto();

        List<PointNotIssueListVo> voList = pointNotIssueService.getPointNotIssueListExportExcel(dto);

        assertNotNull(voList);

    }

}
