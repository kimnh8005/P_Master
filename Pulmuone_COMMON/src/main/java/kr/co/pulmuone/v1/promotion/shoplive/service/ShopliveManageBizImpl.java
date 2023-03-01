package kr.co.pulmuone.v1.promotion.shoplive.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.base.vo.BuyerVo;
import kr.co.pulmuone.v1.comm.util.SessionUtil;
import kr.co.pulmuone.v1.comm.util.StringUtil;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveInfoResponseDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveRequestDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.ShopliveResponseDto;
import kr.co.pulmuone.v1.promotion.shoplive.dto.vo.ShopliveInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShopliveManageBizImpl implements ShopliveManageBiz {

    @Autowired
    ShopliveManageService shopliveManageService;

    @Autowired
    ShopliveEventService shopliveEventService;

    @Override
    public ApiResult<?> selectShopliveList(ShopliveRequestDto shopliveRequestDto) throws Exception {

        ShopliveResponseDto result = new ShopliveResponseDto();

        List<ShopliveInfoVo> adminPointSettingVoList = shopliveManageService.selectShopliveList(shopliveRequestDto);

        result.setRows(adminPointSettingVoList);
        result.setTotal(adminPointSettingVoList.size());

        return  ApiResult.success(result);
    }

    @Override
    public ApiResult<?> selectShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        ShopliveInfoResponseDto result = new ShopliveInfoResponseDto();

        result.setRows(shopliveManageService.selectShopliveInfo(shopliveRequestDto));

        return ApiResult.success(result);
    }

    @Override
    public ApiResult<?> addShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        if (SessionUtil.getBosUserVO() != null) {
            shopliveRequestDto.setCreateId((SessionUtil.getBosUserVO()).getUserId());
        } else {
            shopliveRequestDto.setCreateId("0");
        }
        shopliveManageService.addShopliveInfo(shopliveRequestDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> putShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        if (SessionUtil.getBosUserVO() != null) {
            shopliveRequestDto.setModifyId((SessionUtil.getBosUserVO()).getUserId());
        } else {
            shopliveRequestDto.setModifyId("0");
        }
        shopliveManageService.putShopliveInfo(shopliveRequestDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> delShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        shopliveManageService.delShopliveInfo(shopliveRequestDto);
        return ApiResult.success();
    }

    @Override
    public ApiResult<?> getRemoteShopliveInfo(ShopliveRequestDto shopliveRequestDto) throws Exception {
        ShopliveInfoResponseDto result = new ShopliveInfoResponseDto();

        result.setRows(shopliveEventService.getRemoteShopliveInfo(shopliveRequestDto));

        return ApiResult.success(result);
    }
}
