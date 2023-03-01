package kr.co.pulmuone.bos.item.master;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.item.service.GoodsItemMigrationBiz;

/**
 * <PRE>
 * Forbiz Korea
 * 마스터 품목 데이터 마이그레이션 전용 Controller : 마이그레이션 종료 후 삭제 예정
 *
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일                :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    2020. 10. 29.               박주형         최초작성
 * =======================================================================
 * </PRE>
 */

@RestController
public class GoodsItemMigrationController {

    @Autowired
    private GoodsItemMigrationBiz goodsItemMigrationBiz;

    /**
     * @Desc 마스터 품목 데이터 마이그레이션 정보 조회
     *
     * @param ilitemCode : 품목 코드
     *
     * @return ApiResult<?> :마스터 품목 데이터 마이그레이션 ApiResult
     */
    @GetMapping("/admin/item/master/migration/getMigrationItem")
    @ApiOperation(value = "마스터 품목 데이터 마이그레이션 정보 조회")
    @ApiImplicitParams({ //
            @ApiImplicitParam(name = "ilItemCode", value = "품목 코드", required = true, dataType = "string"), //
    })
    public ApiResult<?> getMigrationItem( //
            @RequestParam(value = "ilItemCode", required = true) String ilItemCode //
    ) {

        return goodsItemMigrationBiz.getMigrationItem(ilItemCode);

    }

}
