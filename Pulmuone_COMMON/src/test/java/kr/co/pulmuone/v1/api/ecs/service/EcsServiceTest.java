package kr.co.pulmuone.v1.api.ecs.service;

import kr.co.pulmuone.v1.api.ecs.dto.vo.QnaToEcsVo;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.constants.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EcsServiceTest extends CommonServiceTestBaseForJunit5{

	@Autowired
	private EcsService ecsService;

	@Test
	void addQnaToEcs_성공() {
    	QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
											.receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
											.boardDiv("상품Q&A")
											.boardSeq("2020121711111111111")
											.customerNum("c_123456")
											.customerName("테스트")
											.customerPhonearea("010")
											.customerPhonefirst("1234")
											.customerPhonesecond("1234")
											.customerEmail("test@test.com")
											.hdBcode("00031")
											.hdScode("00007")
											.claimGubun("00003")
											.counselDesc("제목:테스트")
											.secCode(Constants.SEC_CODE)
											.build();

    	ecsService.addQnaToEcs(qnaToEcsVo);
	}

	@Test
	void addQnaToEcs_실패() {
    	QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
											.receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
											.boardDiv("상품Q&A")
											.build();

    	assertThrows(Exception.class, () -> {
    		ecsService.addQnaToEcs(qnaToEcsVo);
        });
	}

	@Test
	void putQnaToEcs_성공() {
    	QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
										.receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
										.boardSeq("20166042_7073")
										.hdBcode("00031")
										.hdScode("00007")
										.claimGubun("00003")
										.counselDesc("1111")
										.build();

    	ecsService.putQnaToEcs(qnaToEcsVo);
	}

	@Test
	void putQnaToEcs_실패() {
		QnaToEcsVo qnaToEcsVo = QnaToEcsVo.builder()
										.receiptRoot(Constants.RECEIPT_ROOT_ESHOP_HP)
										.boardSeq("2020121711111")
										.hdBcode("00031")
										.hdScode("00007")
										.claimGubun("00003")
										.counselDesc("1111")
										.build();

		int result = ecsService.putQnaToEcs(qnaToEcsVo);

		assertFalse(result > 0);
	}

	@Test
	void getEcsCodeList_상담대분류조회_성공(){
		List<HashMap<String,String>> result = ecsService.getEcsCodeList(null,null);

		assertTrue(result.size() > 0);
	}

	@Test
	void getEcsCodeList_상담중분류조회_성공(){
		String hdBcode = "00031";
		List<HashMap<String,String>> result = ecsService.getEcsCodeList(hdBcode,null);

		assertTrue(result.size() > 0);
	}

	@Test
	void getEcsCodeList_상담중분류조회_실패(){
		String hdBcode = "0000000";
		List<HashMap<String,String>> result = ecsService.getEcsCodeList(hdBcode,null);

		assertFalse(result.size() > 0);
	}

	@Test
	void getEcsCodeList_상담소분류조회_성공(){
		String hdBcode = "00031";
		String hdScode = "00001";
		List<HashMap<String,String>> result = ecsService.getEcsCodeList(hdBcode,hdScode);

		assertTrue(result.size() > 0);
	}

	@Test
	void getEcsCodeList_상담소분류조회_실패(){
		String hdBcode = "0000000";
		String hdScode = "0000000";
		List<HashMap<String,String>> result = ecsService.getEcsCodeList(hdBcode,hdScode);

		assertFalse(result.size() > 0);
	}

}
