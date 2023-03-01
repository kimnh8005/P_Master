package kr.co.pulmuone.bos.goods.regist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.bos.comm.constant.BosStorageInfoEnum;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.StorageType;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistCategoryRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistCategoryResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistItemWarehouseRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistItemWarehouseResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistPackageGoodsMappingDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistResponseDto;
import kr.co.pulmuone.v1.goods.goods.dto.MallGoodsDetailDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistReserveOptionVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsRegistVo;
import kr.co.pulmuone.v1.goods.goods.service.GoodsGoodsBiz;
import kr.co.pulmuone.v1.goods.goods.service.GoodsRegistBiz;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPackageEmployeeDiscountResponseDto;
import kr.co.pulmuone.v1.goods.price.dto.GoodsPriceRequestDto;
import kr.co.pulmuone.v1.goods.price.service.GoodsPriceBiz;

@RestController
public class GoodsRegistController {

	@Autowired(required = true)
	private GoodsRegistBiz goodsRegistBiz;

	@Autowired(required = true)
	private GoodsPriceBiz goodsPriceBiz;

	@Autowired(required = true)
	private GoodsGoodsBiz goodsGoodsBiz;

	@ApiOperation(value = "마스터품목 내역")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/getItemDetail")
	public ApiResult<?> getItemDetail(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.getItemDetail(goodsRegistRequestDto);
	}

	@ApiOperation(value = "마스터품목 상품정보 제공고시")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/getItemSpecValueList")
	@ResponseBody
	public ApiResult<?> getItemSpecValueList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.getItemSpecValueList(goodsRegistRequestDto);
	}

	@ApiOperation(value = "마스터품목 상품 영양정보")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/getItemNutritionList")
	@ResponseBody
	public ApiResult<?> getItemNutritionList(@RequestParam(value = "ilItemCode", required = true) String ilItemCode) throws Exception {
		return goodsRegistBiz.getItemNutritionList(ilItemCode);
	}

	@ApiOperation(value = "마스터품목 상품 인증정보")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/getItemCertificationList")
	@ResponseBody
	public ApiResult<?> getItemCertificationList(@RequestParam(value = "ilItemCode", required = true) String ilItemCode) throws Exception {
		return goodsRegistBiz.getItemCertificationList(ilItemCode);
	}

	@ApiOperation(value = "마스터품목 상품 이미지")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/getItemImageList")
	@ResponseBody
	public ApiResult<?> getItemImageList(@RequestParam(value = "ilItemCode", required = true) String ilItemCode) throws Exception {
		return goodsRegistBiz.getItemImageList(ilItemCode);
	}

	@ApiOperation(value = "가격정보 > 마스터 품목 가격정보")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsPriceInfoResultVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/itemPriceList")
	@ResponseBody
	public ApiResult<?> itemPriceList(@RequestParam(value = "ilItemCode", required = true) String ilItemCode) throws Exception {
		return goodsRegistBiz.itemPriceList(ilItemCode);
	}

	@ApiOperation(value = "상품 등록")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistResponseDto.class),
		@ApiResponse(code = 901, message = "" +
			"[ADD_GOODS_FAIL] 1000 - 상품 생성시 오류가 발생하였습니다."
		)
	})
	@PostMapping(value = "/admin/goods/regist/addGoods")
	public ApiResult<?> addGoods(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		// 상품 이미지 등록 관련 : BosStorageInfoEnum 에 선언된 public 저장소의 최상위 저장 디렉토리 경로를 dto 에 세팅
		String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
		goodsRegistRequestDto.setImageRootStoragePath(publicRootStoragePath);

		return goodsRegistBiz.addGoods(goodsRegistRequestDto);
	}

	@ApiOperation(value = "상품 수정")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistResponseDto.class),
		@ApiResponse(code = 901, message = "" +
			"[MODIFY_GOODS_FAIL] 1002 - 상품 수정시 오류가 발생하였습니다."
		)
	})
	@PostMapping(value = "/admin/goods/regist/modifyGoods")
	public ApiResult<?> modifyGoods(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		// 상품 이미지 등록 관련 : BosStorageInfoEnum 에 선언된 public 저장소의 최상위 저장 디렉토리 경로를 dto 에 세팅
		String publicRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
		goodsRegistRequestDto.setImageRootStoragePath(publicRootStoragePath);

		return goodsRegistBiz.modifyGoods(goodsRegistRequestDto);
	}

	@ApiOperation(value = "배송/발주 정보 > 배송유형 > 품목별 출고처별 배송유형 리스트")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistItemWarehouseResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/itemWarehouseList")
	@ResponseBody
	public ApiResult<?> itemWarehouseList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) throws Exception {
		return goodsRegistBiz.itemWarehouseList(goodsRegistItemWarehouseRequestDto);
	}

	@ApiOperation(value = "배송/발주 정보 > 배송유형 > 품목별 출고처별 배송유형 > 배송 정책 리스트")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistItemWarehouseResponseDto.class)
	})
	@RequestMapping(value = "/admin/goods/regist/itemWarehouseShippingTemplateList")
	public ApiResult<?> itemWarehouseShippingTemplateList(GoodsRegistItemWarehouseRequestDto goodsRegistItemWarehouseRequestDto) throws Exception {
		return goodsRegistBiz.itemWarehouseShippingTemplateList(goodsRegistItemWarehouseRequestDto);
	}

	@ApiOperation(value = "상품 내역")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsDetail")
	public ApiResult<?> goodsDetail(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.goodsDetail(goodsRegistRequestDto);
	}

	@ApiOperation(value = "상품 등록 중복 체크")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistResponseDto.class),
		@ApiResponse(code = 901, message = "" +
			"[DUPLICATE_GOODS] 2000 - 이미 등록된 상품이 있습니다. \n"
		)
	})
	@PostMapping(value = "/admin/goods/regist/duplicateGoods")
	public ApiResult<?> duplicateGoods(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.duplicateGoods(goodsRegistRequestDto);
	}

	@ApiOperation(value = "전시/몰인몰 카테고리")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistCategoryResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/getDisplayCategoryList")
	@ResponseBody
	public ApiResult<?> getDisplayCategoryList(GoodsRegistCategoryRequestDto goodsRegistCategoryRequestDto) throws Exception {
		return goodsRegistBiz.getDisplayCategoryList(goodsRegistCategoryRequestDto);
	}

	@ApiOperation(value = "묶음 상품 > 기준상품, 묶음 상품 정보 불러오기")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/getGoodsList")
	public ApiResult<?> getGoodsList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.getGoodsList(goodsRegistRequestDto);
	}

	@ApiOperation(value = "묶음 상품 > 상품정보제공고시 목록, 상품 영양정보 목록")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/getGoodsInfo")
	public ApiResult<?> getGoodsInfo(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.getGoodsInfo(goodsRegistRequestDto);
	}

	@ApiOperation(value = "묶음 상품 리스트 조합의 배송 불가 지역, 반품 가능 기간 산출")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/etcAssemble")
	public ApiResult<?> etcAssemble(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.etcAssemble(goodsRegistRequestDto);
	}

	@ApiOperation(value = "묶음 상품 > 기본할인 등록 > 가격 계산 상품 리스트")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsPackageCalcList")
	public ApiResult<?> goodsPackageCalcList(GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.goodsPackageCalcList(goodsRegistRequestDto);
	}

	@ApiOperation(value = "일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일, 출고예정일, 도착예정일 산출")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsReservationDateCalcList")
	public ApiResult<?> goodsReservationDateCalcList(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.goodsReservationDateCalcList(goodsRegistRequestDto);
	}

	@ApiOperation(value = "일반상품 > 판매 정보 > 일일 판매 옵션 설정 > 예약판매 > 주문수집I/F일 배송패턴에 따른 요일 날짜 제한")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistReserveOptionVo.class)
	})
	@PostMapping(value = "/admin/goods/regist/orderIfDateLimitList")
	public ApiResult<?> orderIfDateLimitList(@RequestParam(value = "urWarehouseId", required = true) String urWarehouseId) throws Exception {
		return goodsRegistBiz.orderIfDateLimitList(urWarehouseId);
	}

	@ApiOperation(value = "묶음상품 > 임직원 할인 정보 > 임직원 개별할인 정보 > 변경이력 보기")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsPackageEmployeeDiscountResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsPackageEmployeeDiscountHistoryGridList")
	public ApiResult<?> goodsPackageEmployeeDiscountHistoryGridList(GoodsPriceRequestDto goodsPriceRequestDto) throws Exception {
		GoodsPackageEmployeeDiscountResponseDto result = goodsPriceBiz.goodsPackageEmployeeDiscountHistoryGridList(goodsPriceRequestDto);
		return ApiResult.success(result);
	}

	@ApiOperation(value = "Mall 상품 상세 정보 조회")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = MallGoodsDetailDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/getMallGoodsDetail")
	public ApiResult<?> getMallGoodsDetail(@RequestParam(value = "ilGoodsId", required = true) Long ilGoodsId) throws Exception {
		return ApiResult.success(goodsGoodsBiz.getMallGoodsDetail(ilGoodsId));
	}

	@ApiOperation(value = "해당 구성 상품으로 구성된 묶음상품 정보가 있는지 체크")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsRegistResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsPackageExistChk")
	public ApiResult<?> goodsPackageExistChk(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.goodsPackageExistChk(goodsRegistRequestDto);
	}

	@ApiOperation(value = "묶음상품 > 판매 가격정보 > 변경이력 보기 > 상세보기")
	@ApiResponses(value = {
		@ApiResponse(code = 900, message = "response data", response = GoodsPackageEmployeeDiscountResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsPackageGoodsMappingPrice")
	public ApiResult<?> goodsPackageGoodsMappingPrice(
			@RequestParam(value = "ilGoodsPriceId", required = true) String ilGoodsPriceId
		,	@RequestParam(value = "ilGoodsDiscountId", required = true) String ilGoodsDiscountId) throws Exception {
		GoodsPackageEmployeeDiscountResponseDto result = goodsPriceBiz.goodsPackageGoodsMappingPrice(ilGoodsPriceId, ilGoodsDiscountId);
		return ApiResult.success(result);
	}

	@ApiOperation(value = "할인기간수정, 할인삭제에 따른 가격정보 > 판매 가격정보 새로고침 처리")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsRegistResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsPriceRefresh")
	public ApiResult<?> goodsPriceRefresh(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.goodsPriceRefresh(goodsRegistRequestDto);
	}


	@ApiOperation(value = "풀무원샵 상품코드 정보가 있는지 체크")
	@ApiResponses(value = {
			@ApiResponse(code = 900, message = "response data", response = GoodsRegistResponseDto.class)
	})
	@PostMapping(value = "/admin/goods/regist/goodsCodeExistChk")
	public ApiResult<?> goodsCodeExistChk(@RequestBody GoodsRegistRequestDto goodsRegistRequestDto) throws Exception {
		return goodsRegistBiz.goodsCodeExistChk(goodsRegistRequestDto);
	}

}
