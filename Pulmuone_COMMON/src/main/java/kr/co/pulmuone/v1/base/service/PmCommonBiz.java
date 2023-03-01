package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.comm.base.ApiResult;

public interface PmCommonBiz {

	ApiResult<?> getPmPointList();

	ApiResult<?> getPmCpnList();
}
