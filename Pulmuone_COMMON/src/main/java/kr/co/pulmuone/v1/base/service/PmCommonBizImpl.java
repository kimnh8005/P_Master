package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PmCommonBizImpl implements PmCommonBiz {

    @Autowired
    private PmCommonService pmCommonService;

    @Override
    public ApiResult<?> getPmPointList() {
        return pmCommonService.getPmPointList();
    }

    @Override
    public ApiResult<?> getPmCpnList() {
        return pmCommonService.getPmCpnList();
    }
}
