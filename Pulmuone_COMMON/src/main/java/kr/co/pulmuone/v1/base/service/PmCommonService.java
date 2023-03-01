package kr.co.pulmuone.v1.base.service;

import kr.co.pulmuone.v1.base.dto.CouponCommonResponseDto;
import kr.co.pulmuone.v1.base.dto.PointCommonResponseDto;
import kr.co.pulmuone.v1.base.dto.vo.PmCouponCommonResultVo;
import kr.co.pulmuone.v1.base.dto.vo.PmPointCommonResultVo;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.base.PmCommonMapper;

import java.util.List;

@Service
public class PmCommonService {

	@Autowired
	private PmCommonMapper pmCommonMapper;

	/**
	 * 적립금 전체 리스트조회
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> getPmPointList() {
		PointCommonResponseDto result = new PointCommonResponseDto();
		List<PmPointCommonResultVo> rows = pmCommonMapper.getPmPointList();
		result.setRows(rows);

		return ApiResult.success(result);
	}

	/**
	 * 쿠폰목록 전체 리스트조회
	 * @param
	 * @return
	 * @throws Exception
	 */
	protected ApiResult<?> getPmCpnList() {
		CouponCommonResponseDto result = new CouponCommonResponseDto();

		List<PmCouponCommonResultVo> rows = pmCommonMapper.getPmCpnList();
		result.setRows(rows);

		return ApiResult.success(result);
	}


}
