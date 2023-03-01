package kr.co.pulmuone.v1.goods.etc.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsFieldRequestDto;
import kr.co.pulmuone.v1.goods.etc.dto.SpecificsListRequestDto;

public interface GoodsSpecificsBiz {

    ApiResult<?> getSpecificsMasterList(SpecificsListRequestDto specificsListRequestDto);
    ApiResult<?> getSpecificsMasterFieldList(SpecificsListRequestDto specificsListRequestDto);
    ApiResult<?> getSpecificsFieldList(SpecificsFieldRequestDto specificsFieldRequestDto);
    ApiResult<?> getItemSpecificsValueUseYn(SpecificsFieldRequestDto specificsFieldRequestDto);
    ApiResult<?> delSpecificsField(SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception;
    ApiResult<?> putSpecificsField(SpecificsFieldRequestDto specificsFieldRequestDto) throws Exception;
    ApiResult<?> getItemSpecificsMasterUseYn(SpecificsListRequestDto specificsListRequestDto);
    ApiResult<?> delSpecificsMaster(SpecificsListRequestDto specificsListRequestDto) throws Exception;
    ApiResult<?> putSpecificsMaster(SpecificsListRequestDto specificsListRequestDto) throws Exception;
    ApiResult<?> getSpecificsMasterNameDuplicateYn(SpecificsListRequestDto specificsListRequestDto);
    ApiResult<?> getSpecificsFieldNameDuplicateYn(SpecificsFieldRequestDto specificsFieldRequestDto);
}
