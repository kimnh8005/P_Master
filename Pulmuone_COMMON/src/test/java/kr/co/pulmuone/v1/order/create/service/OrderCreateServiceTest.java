package kr.co.pulmuone.v1.order.create.service;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.OrderStatus;
import kr.co.pulmuone.v1.comm.enums.OrderEnums.PayType;
import kr.co.pulmuone.v1.comm.enums.PayEnums.PsPay;
import kr.co.pulmuone.v1.order.create.dto.*;
import kr.co.pulmuone.v1.order.create.dto.vo.CreateInfoVo;
import kr.co.pulmuone.v1.order.order.dto.vo.OrderPaymentMasterVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
public class OrderCreateServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    OrderCreateService orderCreateService;

    @Test
    void 엑셀업로드_상품정보_조회() throws Exception {

    	// Request
    	OrderExcelRequestDto orderExcelRequestDto = new OrderExcelRequestDto();
    	List<OrderExeclDto> uploadExcelList = new ArrayList<> ();
    	OrderExeclDto uploadExcelItem = new OrderExeclDto();
    	uploadExcelItem.setRecvNm("홍길동");
    	uploadExcelItem.setRecvHp("01012345678");
    	uploadExcelItem.setRecvZipCd("02321");
    	uploadExcelItem.setRecvAddr1("서울시 서초구");
    	uploadExcelItem.setRecvAddr2("바우뫼로");
    	uploadExcelItem.setIlGoodsId("15734");
    	uploadExcelItem.setOrderCnt("2");
    	uploadExcelItem.setSalePrice("5000");
    	uploadExcelList.add(uploadExcelItem);
    	orderExcelRequestDto.setOrderExcelList(uploadExcelList);

    	// result
    	List<OrderExcelResponseDto> orderExcelList = orderCreateService.getExcelUploadList(orderExcelRequestDto);

    	orderExcelList.forEach(
            i -> log.info("엑셀업로드_상품정보_조회 result : {}",  i)
        );

        // equals
        Assertions.assertTrue(orderExcelList.size() > 0);
    }

    @Test
    void 엑셀업로드_상품상세정보_조회() throws Exception {

    	// Request
    	long ilGoodsId = 1;

    	// result
    	GoodsInfoDto goodsInfo = orderCreateService.getGoodsInfo(ilGoodsId);

    	log.info("엑셀업로드_상품상세정보_조회 result : {}",  goodsInfo);

    	// equals
    	Assertions.assertTrue(!Objects.isNull(goodsInfo));
    }

    @Test
    void 주문생성_회원그룹정보_조회() throws Exception {

    	// Request
    	long urUserId = 421750;

    	// result
    	UserGroupInfoDto userGroupInfo = orderCreateService.getUserGroupInfo(urUserId);

    	log.info("주문생성_회원그룹정보_조회 result : {}",  userGroupInfo);

    	// equals
		Assertions.assertFalse(Objects.isNull(userGroupInfo));
    }

    @Test
    void 주문생성_등록() throws Exception {

    	// Request
    	CreateInfoVo createInfo = CreateInfoVo.builder()
    											.createType("T")
    											.buyerNm("홍길동")
    											.orderPrice(3000)
    											.orderPaymentType(PsPay.CARD.getCode())
    											.successOrderCnt(1)
    											.successOrderDetlCnt(3)
    											.failureOrderCnt(0)
    											.failureOrderDetlCnt(0)
    											.odid("2021020214055551")
    											.createStatus("E")
    											.originNm("원본파일명")
    											.uploadNm("업로드파일명")
    											.uploadPath("업로드경로")
    											.createId(1646893)
    											.build();

    	// result
    	int insertCnt = orderCreateService.addCreateInfo(createInfo);

    	log.info("주문생성_등록 insertCnt : {}",  insertCnt);

    	// equals
    	Assertions.assertEquals(1, insertCnt);
    }

    @Test
    void 주문생성_주문마스터정보_수정() throws Exception {

    	// Request
    	OrderCreateRequestDto orderCreateRequest = new OrderCreateRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("2020121412345678");
    	findOdIdList.add("2020121412345551");
    	findOdIdList.add("2020121412345123");
    	orderCreateRequest.setFindOdIdList(findOdIdList);

    	// result
    	int updateCnt = orderCreateService.putOrderInfo(orderCreateRequest);

    	log.info("주문생성_주문마스터정보_수정_등록 updateCnt : {}",  updateCnt);

    	// equals
    	Assertions.assertTrue(updateCnt > 0);
    }

    @Test
    void 주문생성_내역_조회() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	orderCreateListRequest.setOrderCreateType("T");
    	orderCreateListRequest.setCreateStatus("W");

    	// result
    	Page<CreateInfoDto> orderCreateList = orderCreateService.getOrderCreateList(orderCreateListRequest);
    	List<CreateInfoDto> resultList = orderCreateList.getResult();

    	resultList.forEach(
            i -> log.info("주문생성_내역_조회 result : {}",  i)
        );

        // equals
        Assertions.assertTrue(orderCreateList.getTotal() > 0);
    }

    @Test
    void 주문생성_삭제() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	orderCreateListRequest.setOdCreateInfoId("3");

    	// result
    	int delCnt = orderCreateService.deleteOrderCreateInfo(orderCreateListRequest);

    	log.info("주문생성_삭제 delCnt : {}",  delCnt);

    	// equals
    	Assertions.assertTrue(delCnt > 0);
    }

    @Test
    void 주문_삭제() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("2020121412345678");
    	findOdIdList.add("2020121412345551");
    	findOdIdList.add("2020121412345123");
    	orderCreateListRequest.setFindOdIdList(findOdIdList);

    	// result
    	int delCnt = orderCreateService.deleteOrder(orderCreateListRequest);

    	log.info("주문_삭제 delCnt : {}",  delCnt);

    	// equals
    	Assertions.assertTrue(delCnt > 0);
    }

    @Test
    void 주문_날짜_삭제() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("101");
    	orderCreateListRequest.setFindOdIdList(findOdIdList);

    	// result
    	int delCnt = orderCreateService.deleteOrderDt(orderCreateListRequest);

    	log.info("주문_날짜_삭제 delCnt : {}",  delCnt);

    	// equals
    	Assertions.assertTrue(delCnt > 0);
    }

    @Test
    void 주문_배송지_삭제() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("2020121412345678");
    	findOdIdList.add("2020121412345551");
    	findOdIdList.add("2020121412345123");
    	orderCreateListRequest.setFindOdIdList(findOdIdList);

    	// result
    	int delCnt = orderCreateService.deleteShippingZone(orderCreateListRequest);

    	log.info("주문_배송지_삭제 delCnt : {}",  delCnt);

    	// equals
    	Assertions.assertTrue(delCnt > 0);
    }

    @Test
    void 주문_배송지_이력_삭제() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("21011500001010");
    	orderCreateListRequest.setFindOdIdList(findOdIdList);

    	// result
    	int delCnt = orderCreateService.deleteShippingZoneHist(orderCreateListRequest);

    	log.info("주문_배송지_이력_삭제 delCnt : {}",  delCnt);

    	// equals
    	Assertions.assertTrue(delCnt > 0);
    }

    @Test
    void 주문_배송비_삭제() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("2020121412345678");
    	findOdIdList.add("2020121412345551");
    	findOdIdList.add("2020121412345123");
    	orderCreateListRequest.setFindOdIdList(findOdIdList);

    	// result
    	int delCnt = orderCreateService.deleteShippingPrice(orderCreateListRequest);

    	log.info("주문_배송비_삭제 delCnt : {}",  delCnt);

    	// equals
    	Assertions.assertTrue(delCnt > 0);
    }

    @Test
    void 주문_상세_삭제() throws Exception {

    	// Request
    	OrderCreateListRequestDto orderCreateListRequest = new OrderCreateListRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("2020121412345678");
    	findOdIdList.add("2020121412345551");
    	findOdIdList.add("2020121412345123");
    	orderCreateListRequest.setFindOdIdList(findOdIdList);

    	// result
    	int delCnt = orderCreateService.deleteOrderDetl(orderCreateListRequest);

    	log.info("주문_상세_삭제 delCnt : {}",  delCnt);

    	// equals
    	Assertions.assertTrue(delCnt > 0);
    }

    @Test
    void 신용카드_비인증_주문정보_조회() throws Exception {

    	// Request
    	OrderCardPayRequestDto orderCardPayResquest = new OrderCardPayRequestDto();
    	List<String> findOdIdList = new ArrayList<>();
    	findOdIdList.add("2020121412345678");
    	findOdIdList.add("2020121412345551");
    	findOdIdList.add("2020121412345123");
    	orderCardPayResquest.setFindOdIdList(findOdIdList);

    	// result
    	OrderInfoDto orderInfo = orderCreateService.getOrderInfo(orderCardPayResquest);

    	log.info("신용카드_비인증_주문정보_조회 result : {}",  orderInfo);

    	// equals
    	Assertions.assertTrue(!Objects.isNull(orderInfo));
    }

    @Test
    void 신용카드_비인증_주문결제정보_조회() throws Exception {

    	// Request
    	long odPaymentMsterId = 1002;

    	// result
    	PaymentInfoDto paymentInfo = orderCreateService.getPaymentInfo(odPaymentMsterId);

    	log.info("신용카드_비인증_주문결제정보_조회 result : {}",  paymentInfo);

    	// equals
    	Assertions.assertTrue(!Objects.isNull(paymentInfo));
    }

    @Test
    void 주문결제_마스터_신용카드_무통장_수정() throws Exception {

    	// Request
    	OrderPaymentMasterVo orderPaymentMasterVo = OrderPaymentMasterVo.builder()
    																	.type(PayType.G.getCode())
    																	.status(OrderStatus.INCOM_COMPLETE.getCode())
    																	.build();

    	// result
    	int updateCnt = orderCreateService.putPaymentMasterInfo(orderPaymentMasterVo);

    	log.info("주문결제_마스터_신용카드_무통장_수정 updateCnt : {}",  updateCnt);

    	// equals
    	Assertions.assertEquals(1, updateCnt);
    }


	@Test
	void 주문연동_매출만연동시_상태값_처리() throws Exception {
		long odOrderId = 45653;
		String orderCopySalIfYn = "N";
		String orderCopyOdid = "";
		// 빌드 오류로 주석
		orderCreateService.orderCopySalIfExecute(odOrderId, orderCopySalIfYn, orderCopyOdid);

		Assertions.assertTrue(true);
	}
}
