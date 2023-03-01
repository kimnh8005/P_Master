package kr.co.pulmuone.v1.goods.goods.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.approval.auth.dto.ApprovalGoodsRequestDto;
import kr.co.pulmuone.v1.approval.auth.dto.vo.ApprovalStatusVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.constants.Constants;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums;
import kr.co.pulmuone.v1.comm.enums.ApprovalEnums.ApprovalAuthType;
import kr.co.pulmuone.v1.comm.enums.BaseEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountMethodType;
import kr.co.pulmuone.v1.comm.enums.GoodsEnums.GoodsDiscountType;
import kr.co.pulmuone.v1.comm.enums.MessageCommEnum;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.comm.mapper.goods.goods.GoodsListMapper;
import kr.co.pulmuone.v1.comm.util.PriceUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsApprovalResponseDto;
import kr.co.pulmuone.v1.comm.base.vo.UserVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsListRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsApprovalResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.GoodsRegistRequestDto;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsPriceInfoResultVo;
import kr.co.pulmuone.v1.goods.goods.dto.vo.GoodsVo;
import kr.co.pulmuone.v1.goods.price.dto.vo.GoodsPriceVo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
* <PRE>
* Forbiz Korea
* 상품리스트 Service
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 10. 05.               손진구          최초작성
* =======================================================================
* </PRE>
*/
@Service
@RequiredArgsConstructor
public class GoodsListService {


    @Autowired
    private final GoodsListMapper goodsListMapper;

    @Autowired
    private GoodsRegistService goodsRegistService;

    /**
     * @Desc 검색키 -> 검색키 리스트 변환
     *       검색키가 빈값이 아니고, 검색키중에 ALL 이 없을 경우 실행
     * @param searchKey
     * @param splitKey
     * @return List<String>
     */
    protected List<String> getSearchKeyToSearchKeyList(String searchKey, String splitKey) {
        List<String> searchKeyList = new ArrayList<String>();

        if (StringUtils.isNotEmpty(searchKey)) {
            searchKeyList.addAll(Stream.of(searchKey.split(splitKey))
                    .map(String::trim)
                    .filter( x -> StringUtils.isNotEmpty(x) )
                    .collect(Collectors.toList()));

            for (String key : searchKeyList) {
            	if ("ALL".equals(key)) {
            		return new ArrayList<String>();
            	}
            }
        }

        return searchKeyList;
    }

    /**
     * @Desc 상품 목록 조회
     * @param goodsListRequestDto
     * @return Page<GoodsVo>
     */
    protected Page<GoodsVo> getGoodsList(GoodsListRequestDto goodsListRequestDto) {
        // 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
    	goodsListRequestDto.setListAuthSupplierId(listAuthSupplierId);
    	goodsListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);

    	PageMethod.startPage(goodsListRequestDto.getPage(), goodsListRequestDto.getPageSize());
        return goodsListMapper.getGoodsList(goodsListRequestDto);
    }

    /**
     * @Desc 상품 판매상태 변경
     * @param goodsVo
     * @return int
     */
    protected int putGoodsSaleStatusChange(GoodsVo goodsVo) {
        return goodsListMapper.putGoodsSaleStatusChange(goodsVo);
    }

    /**
     * @Desc 상품 판매상태 조회
     * @param goodsVo
     * @return GoodsRegistRequestDto
     */
    protected GoodsRegistRequestDto getGoodsSaleStatusInfo(GoodsVo goodsVo) {
        return goodsListMapper.getGoodsSaleStatusInfo(goodsVo);
    }

    /**
     * @Desc 마스터 품목 리스트 엑셀 다운로드 목록 조회
     * @param GoodsListRequestDto : 상품 리스트 검색 조건 request dto
     * @return List<GoodsVo> : 상품 리스트 엑셀 다운로드 목록
     */
    public List<GoodsVo> getGoodsListExcel(GoodsListRequestDto goodsListRequestDto) {
        // 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
    	goodsListRequestDto.setListAuthSupplierId(listAuthSupplierId);
    	goodsListRequestDto.setListAuthWarehouseId(listAuthWarehouseId);

    	List<GoodsVo> goodsList = goodsListMapper.getGoodsListExcel(goodsListRequestDto);
        return goodsList;
    }

    /**
     * @Desc 상품등록 승인 목록 조회
     * @param ApprovalGoodsRequestDto
     * @return Page<GoodsApprovalResultVo>
     */
    protected GoodsApprovalResponseDto getApprovalGoodsRegistList(ApprovalGoodsRequestDto approvalGoodsRequestDto) {
        List<String> findKeywordList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getFindKeyword(), Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS);
		List<String> goodsTypeList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS);
		List<String> approvalStatusList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getSearchApprovalStatus(), Constants.ARRAY_SEPARATORS);

		approvalGoodsRequestDto.setFindKeywordList(findKeywordList);
		approvalGoodsRequestDto.setGoodsTypeList(goodsTypeList);
		approvalGoodsRequestDto.setApprovalStatusArray(approvalStatusList);
        approvalGoodsRequestDto.setApprKindType(ApprovalAuthType.APPR_KIND_TP_GOODS_REG.getCode());

        PageMethod.startPage(approvalGoodsRequestDto.getPage(), approvalGoodsRequestDto.getPageSize());
        Page<GoodsApprovalResultVo> resultVoPage = goodsListMapper.getApprovalGoodsRegistList(approvalGoodsRequestDto);

        return GoodsApprovalResponseDto.builder()
                                       .total(resultVoPage.getTotal())
                                       .rows(resultVoPage)
                                       .build();
    }

    /**
  	 * 상품등록 상태이력 등록
  	 * @param ApprovalStatusVo
  	 * @return int
  	 */
  	protected int addGoodsRegistStatusHistory(ApprovalStatusVo history){
  		return goodsListMapper.addGoodsRegistStatusHistory(history);
  	}

    /**
     * 상품등록 승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalGoodsRegist(ApprovalStatusVo approvalVo) throws Exception {
		if(goodsListMapper.putCancelRequestApprovalGoodsRegist(approvalVo) > 0
			&& this.addGoodsRegistStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 상품등록 승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessGoodsRegist(ApprovalStatusVo approvalVo) throws Exception {
		if(goodsListMapper.putApprovalProcessGoodsRegist(approvalVo) > 0
			&& this.addGoodsRegistStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * @Desc 상품등록 승인 목록 조회
     * @param ApprovalGoodsRequestDto
     * @return Page<GoodsApprovalResultVo>
     */
    protected GoodsApprovalResponseDto getApprovalGoodsClientList(ApprovalGoodsRequestDto approvalGoodsRequestDto) {
        // 권한 설정
        UserVo userVo = SessionUtil.getBosUserVO();
        List<String> listAuthSupplierId = userVo.getListAuthSupplierId();
        listAuthSupplierId.removeIf(s->StringUtils.isEmpty(s));
        List<String> listAuthWarehouseId = userVo.getListAuthWarehouseId();
        listAuthWarehouseId.removeIf(s->StringUtils.isEmpty(s));
        approvalGoodsRequestDto.setListAuthSupplierId(listAuthSupplierId);
        approvalGoodsRequestDto.setListAuthWarehouseId(listAuthWarehouseId);

    	List<String> findKeywordList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getFindKeyword(), Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS);
		List<String> goodsTypeList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS);
		List<String> approvalStatusList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getSearchApprovalStatus(), Constants.ARRAY_SEPARATORS);

		approvalGoodsRequestDto.setFindKeywordList(findKeywordList);
		approvalGoodsRequestDto.setGoodsTypeList(goodsTypeList);
		approvalGoodsRequestDto.setApprovalStatusArray(approvalStatusList);
        approvalGoodsRequestDto.setApprKindType(ApprovalAuthType.APPR_KIND_TP_GOODS_CLIENT.getCode());

        PageMethod.startPage(approvalGoodsRequestDto.getPage(), approvalGoodsRequestDto.getPageSize());
        Page<GoodsApprovalResultVo> resultVoPage = goodsListMapper.getApprovalGoodsRegistList(approvalGoodsRequestDto);

        return GoodsApprovalResponseDto.builder()
                                       .total(resultVoPage.getTotal())
                                       .rows(resultVoPage)
                                       .build();
    }

    /**
     * 상품 수정 상세내역 조회
     * @param ilGoodsApprId
     * @return
     */
    protected ApiResult<?> getApprovalGoodsDetailClient(String ilGoodsApprId) {
        return ApiResult.result(goodsListMapper.getApprovalGoodsDetailClient(ilGoodsApprId), BaseEnums.Default.SUCCESS);
    }

    /**
     * @Desc 상품할인 승인 목록 조회
     * @param ApprovalGoodsRequestDto
     * @return Page<GoodsApprovalResultVo>
     */
    protected GoodsApprovalResponseDto getApprovalGoodsDiscountList(ApprovalGoodsRequestDto approvalGoodsRequestDto) {
        // 조회조건 보정
        List<String> findKeywordList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getFindKeyword(), Constants.ARRAY_LINE_BREAK_OR_COMMA_SEPARATORS);
        List<String> goodsTypeList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getGoodsType(), Constants.ARRAY_SEPARATORS);
        List<String> approvalStatusList = this.getSearchKeyToSearchKeyList(approvalGoodsRequestDto.getSearchApprovalStatus(), Constants.ARRAY_SEPARATORS);

		approvalGoodsRequestDto.setFindKeywordList(findKeywordList);
		approvalGoodsRequestDto.setGoodsTypeList(goodsTypeList);
		approvalGoodsRequestDto.setApprovalStatusArray(approvalStatusList);

        // 조회
        PageMethod.startPage(approvalGoodsRequestDto.getPage(), approvalGoodsRequestDto.getPageSize());
        Page<GoodsApprovalResultVo> resultVoPage = goodsListMapper.getApprovalGoodsDiscountList(approvalGoodsRequestDto);

        // 조회후 데이터 보정
        List<GoodsApprovalResultVo> resultList = resultVoPage.getResult();
        for (GoodsApprovalResultVo vo : resultList) {
            mappingCalculatedDiscountPriceInfo(vo); // 할인 금액 계산
        }

        return GoodsApprovalResponseDto.builder()
                                       .total(resultVoPage.getTotal())
                                       .rows(resultList)
                                       .build();
    }

    /**
     * 상품할인 승인 요청철회
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putCancelRequestApprovalGoodsDiscount(ApprovalStatusVo approvalVo) throws Exception {
		if(goodsListMapper.putCancelRequestApprovalGoodsDiscount(approvalVo) > 0
			&& this.addGoodsDiscountStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 상품할인 승인 폐기처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putDisposalApprovalGoodsDiscount(ApprovalStatusVo approvalVo) throws Exception {
		if(goodsListMapper.putDisposalApprovalGoodsDiscount(approvalVo) > 0
			&& this.addGoodsDiscountStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
     * 상품등록 승인처리
     *
     * @param ApprovalStatusVo
     * @return MessageCommEnum
     */
    protected MessageCommEnum putApprovalProcessGoodsDiscount(ApprovalStatusVo approvalVo) throws Exception {
		if(goodsListMapper.putApprovalProcessGoodsDiscount(approvalVo) > 0
			&& this.addGoodsDiscountStatusHistory(approvalVo) > 0 ) {
			return BaseEnums.Default.SUCCESS;
		}else {
			throw new BaseException(BaseEnums.CommBase.PROGRAM_ERROR);
		}
    }

    /**
  	 * 상품할인 상태이력 등록
  	 * @param ApprovalStatusVo
  	 * @return int
  	 */
  	protected int addGoodsDiscountStatusHistory(ApprovalStatusVo history){
  		return goodsListMapper.addGoodsDiscountStatusHistory(history);
  	}

    /**
     * 정률할인 고정 판매가 할인에 의한 힐인율 할인액 마진율 계산
     * @param resultVo
     */
  	private void mappingCalculatedDiscountPriceInfo(GoodsApprovalResultVo resultVo) {

        int discountRatio = ObjectUtils.isEmpty(resultVo.getDiscountRatio()) ? 0 : resultVo.getDiscountRatio();
        int discountSalePrice = ObjectUtils.isEmpty(resultVo.getDiscountSalePrice()) ? 0 : resultVo.getDiscountSalePrice();
        int discountRatioChg = discountRatio;
        int discountSalePriceChg = discountSalePrice;

        //패키지 상품 & 임직원할인은 정률 할인 설정은 상품별로 설정가능하기 때문에 쿼리에서 평균값을 구하지만, 실제 상품에 계산한값의 할인률이 다르기 때문에 재계산
        //총 할인판매가는 계산 되어있기 때문에 그 값을 이용해 다시 구한다.
        if(GoodsEnums.GoodsType.PACKAGE.getCode().equals(resultVo.getGoodsTypeCode()) && GoodsDiscountType.EMPLOYEE.getCode().equals(resultVo.getDiscountType())) {
            discountRatio = PriceUtil.getRate(resultVo.getRecommendedPrice(), resultVo.getDiscountSalePrice());

            //discountRatioChg = PriceUtil.getRate(resultVo.getRecommendedPriceChg(), resultVo.getDiscountSalePrice());
            if(ApprovalEnums.ApprovalStatus.REQUEST.getCode().equals(resultVo.getApprStat()) || ApprovalEnums.ApprovalStatus.SUB_APPROVED.getCode().equals(resultVo.getApprStat())) {
                //현재가

                //할인이 적용중이라면 같게 됨
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                if(now.isAfter(LocalDateTime.parse(resultVo.getDiscountStartDt(), dateFormatter)) && now.isBefore(LocalDateTime.parse(resultVo.getDiscountEndDt(), dateFormatter))) {
                    discountRatioChg = discountRatio;
                }
            }else {
                //처리당시 가격
                discountRatioChg  = PriceUtil.getRate(resultVo.getRecommendedPriceChg(), resultVo.getDiscountSalePrice());
            }
        }

        if (GoodsDiscountMethodType.FIXED_PRICE.getCode().equals(resultVo.getDiscountMethodType())) {    //고정가 할인
            discountRatio = (PriceUtil.getRate(resultVo.getRecommendedPrice(), discountSalePrice) < 0) ? 0 : PriceUtil.getRate(resultVo.getRecommendedPrice(), discountSalePrice);
            discountRatioChg = (PriceUtil.getRate(resultVo.getRecommendedPriceChg(), discountSalePriceChg) < 0) ? 0 : PriceUtil.getRate(resultVo.getRecommendedPriceChg(), discountSalePriceChg);
        }
        else if (GoodsDiscountMethodType.FIXED_RATE.getCode().equals(resultVo.getDiscountMethodType())) {    //정률 할인
            if(discountRatio > 0) {
                discountSalePrice = PriceUtil.getPriceByRate(resultVo.getRecommendedPrice(), discountRatio);
                discountSalePrice = discountSalePrice - (discountSalePrice % 10); // 원단위 절사
            }

            if(discountRatioChg > 0) {
                discountSalePriceChg = PriceUtil.getPriceByRate(resultVo.getRecommendedPriceChg(), discountRatioChg);
                discountSalePriceChg = discountSalePriceChg - (discountSalePriceChg % 10); // 원단위 절사
            }
        }

        int discountAmt = resultVo.getRecommendedPrice() - discountSalePrice;
        int discountAmtChg = resultVo.getRecommendedPriceChg() - discountSalePriceChg;
        int marginRate;
        int marginRateChg;

        if ("Y".equals(resultVo.getTaxYn())) {
            marginRate = (int) Math.floor((discountSalePrice - (resultVo.getStandardPrice() * 1.1)) / (double) discountSalePrice * 100);
            marginRateChg = (int) Math.floor((discountSalePriceChg - (resultVo.getStandardPriceChg() * 1.1)) / (double) discountSalePrice * 100);
        } else {
            marginRate = (int) Math.floor((discountSalePrice - resultVo.getStandardPrice()) / (double) discountSalePrice * 100);
            marginRateChg = (int) Math.floor((discountSalePriceChg - resultVo.getStandardPriceChg()) / (double) discountSalePrice * 100);
        }

        String discountType = resultVo.getDiscountType();
        String discountTypeName = GoodsDiscountType.findByCode(resultVo.getDiscountType()).getCodeName();
        String discountMethodType = GoodsDiscountMethodType.findByCode(resultVo.getDiscountMethodType()).getCodeName();

        resultVo.setDiscountAmount(discountAmt);
        resultVo.setDiscountAmountChg(discountAmtChg);
        resultVo.setDiscountSalePrice(discountSalePrice);
        resultVo.setDiscountSalePriceChg(discountSalePriceChg);
        resultVo.setMarginRate(marginRate);
        resultVo.setMarginRateChg(marginRateChg);
        resultVo.setDiscountRatio(discountRatio);
        resultVo.setDiscountRatioChg(discountRatioChg);
        resultVo.setDiscountType(discountType);
        resultVo.setDiscountTypeName(discountTypeName);
        resultVo.setDiscountMethodType(discountMethodType);

    }

    protected GoodsPriceVo getCurrentGoodsPriceByGoodsDiscountApprId(String goodsDiscountApprId){
  	    return goodsListMapper.getCurrentGoodsPriceByGoodsDiscountApprId(goodsDiscountApprId);
    }

}
