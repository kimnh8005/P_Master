package kr.co.pulmuone.v1.goods.item.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.framework.dto.ExcelDownloadDto;
import kr.co.pulmuone.v1.goods.item.dto.GoodsMealListRequestDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsDto;
import kr.co.pulmuone.v1.goods.item.dto.MealContsListRequestDto;
import org.springframework.web.multipart.MultipartFile;

public interface GoodsMealBiz {

    ApiResult<?> getGoodsMealList(GoodsMealListRequestDto goodsMealListRequestDto);
}
