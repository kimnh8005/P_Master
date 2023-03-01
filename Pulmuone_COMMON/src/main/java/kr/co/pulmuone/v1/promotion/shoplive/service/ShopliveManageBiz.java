package kr.co.pulmuone.v1.promotion.shoplive.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveRequestDto;

public interface ShopliveManageBiz {
    ApiResult<?> selectShopliveList(ShopliveRequestDto shopliveRequestDto) throws Exception;

    ApiResult<?> selectShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;

    ApiResult<?> addShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;

    ApiResult<?> putShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;

    ApiResult<?> delShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;

    ApiResult<?> getRemoteShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception;
}
