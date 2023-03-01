package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallDetlListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalOutmallUploadDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalOutmallServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
	private CalOutmallService calOutmallService;

	@Test
	void getOutmallList_조회_성공() {
		CalOutmallListRequestDto dto = new CalOutmallListRequestDto();
		dto.setPage(0);
		dto.setPageSize(2);

		List<CalOutmallListDto> result = calOutmallService.getOutmallList(dto);

		assertTrue(result.size() > 0);
	}

	@Test
	void getOutmallListCount_조회_성공() {
		CalOutmallListRequestDto dto = new CalOutmallListRequestDto();

		long result = calOutmallService.getOutmallListCount(dto);

		assertTrue(result > 0);
	}


	@Test
	void addOdOrderMaster_성공(){

		CalOutmallListRequestDto dto = new CalOutmallListRequestDto();

		dto.setCreateId(Long.parseLong("1"));
		dto.setOriginNm("엑셀");

		calOutmallService.addOdOrderMaster(dto);

	}

	@Test
	void getSellerInfo_조회_성공() {
		CalOutmallUploadDto result = new CalOutmallUploadDto();

		result = calOutmallService.getSellerInfo("11번가");

		assertTrue(!result.getOmSellersId().isEmpty());
	}

	@Test
	void addOdOrderUploadDetail_성공(){

		CalOutmallUploadDto dto = new CalOutmallUploadDto();

		dto.setCreateId(Long.parseLong("1"));
		dto.setOdOutMallCompareUploadInfoId(Long.parseLong("1"));
		dto.setSellersNm("판매자");

		calOutmallService.addOdOrderUploadDetail(dto);

	}

	@Test
	void putOutmallCountInfo_성공(){

		CalOutmallListRequestDto dto = new CalOutmallListRequestDto();

		dto.setCreateId(Long.parseLong("1"));

		calOutmallService.putOutmallCountInfo(dto);

	}

	@Test
	void getOutmallDetlListCount_성공(){

		CalOutmallListRequestDto dto = new CalOutmallListRequestDto();

		dto.setGrantAuthEmployeeNumber("forbiz");
		CalOutmallDetlListDto result = calOutmallService.getOutmallDetlListCount(dto);

		assertTrue(result.getTotalCnt() > 0);

	}

	@Test
	void getOutmallDetlList_조회_성공() {
		//given
		CalOutmallListRequestDto dto = new CalOutmallListRequestDto();
		dto.setOmSellersIdList(new ArrayList<>());
		dto.setDateSearchType("CREATE_DT");
		dto.setDateSearchStart("2021-04-26");
		dto.setDateSearchEnd("2021-04-27");

		//when
		List<CalOutmallDetlListDto> result = calOutmallService.getOutmallDetlList(dto);

		//then
		assertTrue(result.size() > 0);
	}

}