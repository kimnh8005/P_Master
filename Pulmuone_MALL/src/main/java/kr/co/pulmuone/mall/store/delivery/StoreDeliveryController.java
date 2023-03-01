package kr.co.pulmuone.mall.store.delivery;

import io.swagger.annotations.*;
import kr.co.pulmuone.mall.store.delivery.service.StoreDeliveryMallService;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.user.buyer.dto.*;
import kr.co.pulmuone.v1.user.buyer.service.UserBuyerBiz;
import kr.co.pulmuone.v1.user.login.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <PRE>
 * Forbiz Korea
 * Class 의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 * 버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 * 1.0    20210216   	 	천혜현            최초작성
 *
 * =======================================================================
 * </PRE>
 */
@ComponentScan(basePackages = {"kr.co.pulmuone.common"})
@RestController
public class StoreDeliveryController {

    @Autowired
    private StoreDeliveryMallService storeDeliveryMallService;


    /**
     * 상품,우편번호,건물번호로 스토어 배송가능여부 조회
     *
     * @param ilGoodsId
     * @param zipCode
     * @param buildingCode
     * @throws Exception
     */
    @ApiOperation(value = "상품,우편번호,건물번호로 스토어 배송가능여부 조회")
    @PostMapping(value = "/store/delivery/isShippingPossibilityStoreDeliveryAreaByGoodsId")
    public ApiResult<?> isShippingPossibilityStoreDeliveryAreaByGoodsId(
    		@RequestParam(value = "ilGoodsId") Long ilGoodsId
    		,@RequestParam(value = "zipCode") String zipCode
    		,@RequestParam(value = "buildingCode") String buildingCode) throws Exception {
        return storeDeliveryMallService.isShippingPossibilityStoreDeliveryAreaByGoodsId(ilGoodsId, zipCode, buildingCode);
    }

}
