package kr.co.pulmuone.v1.statics.data.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.comm.exception.BaseException;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsRequestDto;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class DataDownloadStaticsBizImpl implements DataDownloadStaticsBiz {

    @Autowired
    private DataDownloadStaticsService outboundStaticsService;

    @Override
    public ApiResult<?> getDataDownloadStaticsList(DataDownloadStaticsRequestDto dto) throws BaseException {
        return ApiResult.success(outboundStaticsService.getDataDownloadStaticsList(dto));
    }


}
