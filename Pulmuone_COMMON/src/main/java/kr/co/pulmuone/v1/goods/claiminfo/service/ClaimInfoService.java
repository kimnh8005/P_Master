package kr.co.pulmuone.v1.goods.claiminfo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.mapper.goods.claiminfo.ClaimInfoMapper;
import kr.co.pulmuone.v1.goods.claiminfo.dto.ClaimInfoRequestDto;
import kr.co.pulmuone.v1.goods.claiminfo.dto.vo.ClaimInfoVo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClaimInfoService {

	@Autowired
	private final ClaimInfoMapper claimInfoMapper;

	/**
	* @Desc 배송/반품/취소안내 목록 조회
	* @param claimInfoRequestDto
	* @return List<ClaimInfoVo>
	*/
	protected List<ClaimInfoVo> getClaimInfoList(ClaimInfoRequestDto claimInfoRequestDto){
		return claimInfoMapper.getClaimInfoList(claimInfoRequestDto);
	}

	/**
	* @Desc 배송/반품/취소안내 상세 조회
	* @param claimInfoRequestDto
	* @return ClaimInfoVo
	*/
	protected ClaimInfoVo getClaimInfo(String ilClaimInfoId) {
	    return claimInfoMapper.getClaimInfo(ilClaimInfoId);
	}

	/**
	* @Desc 배송/반품/취소안내 상세 조회
	* @param claimInfoRequestDto
	* @return int
	*/
	protected int putClaimInfo(ClaimInfoRequestDto claimInfoRequestDto) {
	    return claimInfoMapper.putClaimInfo(claimInfoRequestDto);
	}
}
