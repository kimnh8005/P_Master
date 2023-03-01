package kr.co.pulmuone.v1.calculate.collation.service;

import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgListRequestDto;
import kr.co.pulmuone.v1.calculate.collation.dto.CalPgUploadDto;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CalPgServiceTest extends CommonServiceTestBaseForJunit5 {

	@Autowired
    private CalPgService calPgService;

	@Test
	void getPgList_조회_성공() {
		CalPgListRequestDto dto = new CalPgListRequestDto();
		dto.setPage(0);
		dto.setPageSize(2);

		List<CalPgListDto> result = calPgService.getPgList(dto);

		assertTrue(result.size() > 0);
	}


	@Test
	void getPgListCount_조회_성공() {
		CalPgListRequestDto dto = new CalPgListRequestDto();

		long result = calPgService.getPgListCount(dto);

		assertTrue(result > 0);
	}

	@Test
	void addOdOrderMaster_성공(){

		CalPgListRequestDto dto = new CalPgListRequestDto();

		dto.setPgUploadGubun("PG_SERVICE.INICIS");
		dto.setOriginNm("엑셀");
		dto.setCreateId(Long.parseLong("1"));

		calPgService.addOdOrderMaster(dto);

	}

	@Test
	void addOdPgCompareUploadDetail_성공(){

		CalPgUploadDto dto = new CalPgUploadDto();

		dto.setOdPgCompareUploadInfoId(Long.parseLong("1"));
		dto.setPgService("PG_SERVICE.KCP");
		dto.setType("G");
		dto.setOdid("1");
		dto.setApprovalDt("20210501");
		dto.setCreateId(Long.parseLong("1"));

		calPgService.addOdPgCompareUploadDetail(dto);

	}

	@Test
	void addOdPgCompareUploadDetailInfo_성공(){

		CalPgUploadDto dto = new CalPgUploadDto();

		dto.setOdPgCompareUploadDetailId(Long.parseLong("1"));
		dto.setCommission("1000");
		dto.setVat("1000");
		dto.setGiveDt("20210502");
		dto.setEscrowCommission("2000");


		calPgService.addOdPgCompareUploadDetailInfo(dto);

	}


	@Test
	void putPgCountInfo_성공(){

		CalPgListRequestDto dto = new CalPgListRequestDto();

		dto.setSuccessCnt(3);
		dto.setFailCnt(2);
		dto.setOdPgCompareUploadInfoId(Long.parseLong("1"));

		calPgService.putPgCountInfo(dto);

	}


}
