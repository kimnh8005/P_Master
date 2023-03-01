package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.MasterItemListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.OrgaDiscountRequestDto;

/**
 * <PRE>
* Forbiz Korea
* 올가 할인연동 Service
 * </PRE>
 *
 * <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020. 11. 13.                정형진          최초작성
* =======================================================================
 * </PRE>
 */
public interface GoodsOrgaDisBiz {

	ApiResult<?> getOrgaDisList(OrgaDiscountRequestDto paramDto);

	ExcelDownloadDto getOrgaDisListExcel(OrgaDiscountRequestDto paramDto);


}
