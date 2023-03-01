package kr.co.pulmuone.v1.promotion.adminpointsetting.service;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.promotion.adminpointsetting.AdminPointSettingMapper;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingDetailResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingRequestDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.AdminPointSettingResponseDto;
import kr.co.pulmuone.v1.promotion.adminpointsetting.dto.vo.AdminPointSettingVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminPointSettingService {


	 private final AdminPointSettingMapper adminPointSettingMapper;

	/**
	 * 관리자 적립금 지금 한도 설정 조회
	 *
	 * @param AdminPointSettingRequestDto
	 * @return AdminPointSettingResponseDto
	 * @throws Exception
	 */
	protected Page<AdminPointSettingVo> getAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		PageMethod.startPage(adminPointSettingRequestDto.getPage(), adminPointSettingRequestDto.getPageSize());
        return adminPointSettingMapper.getAdminPointSetting(adminPointSettingRequestDto);
    }

	/**
	 * 적립금 설정 조회
	 *
	 * @param AdminPointSettingRequestDto
	 * @return AdminPointSettingResponseDto
	 * @throws Exception
	 */
	protected Page<AdminPointSettingVo> getRoleGroupList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		PageMethod.startPage(adminPointSettingRequestDto.getPage(), adminPointSettingRequestDto.getPageSize());
        return adminPointSettingMapper.getRoleGroupList(adminPointSettingRequestDto);
    }


	/**
	 * 관리자 적립금 지급 한도 이력 조회
	 *
	 * @param AdminPointSettingRequestDto
	 * @return AdminPointSettingResponseDto
	 * @throws Exception
	 */
	protected Page<AdminPointSettingVo> getAdminPointSettingList(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
		PageMethod.startPage(adminPointSettingRequestDto.getPage(), adminPointSettingRequestDto.getPageSize());
        return adminPointSettingMapper.getAdminPointSettingList(adminPointSettingRequestDto);
    }



    /**
     * 적립금 설정 상세 조회
     *
     * @param AdminPointSettingRequestDto
     * @return AdminPointSettingDetailResponseDto
     * @throws Exception
     */
    protected AdminPointSettingDetailResponseDto getAdminPointSettingDetail(AdminPointSettingRequestDto adminPointSettingRequestDto) throws Exception {
    	AdminPointSettingDetailResponseDto result = new AdminPointSettingDetailResponseDto();
    	AdminPointSettingVo vo = new AdminPointSettingVo();

    	// 적립금 상세 정보 조회
		vo = adminPointSettingMapper.getAdminPointSettingDetail(adminPointSettingRequestDto);

        result.setRows(vo);
        return result;
    }

    /**
     * 관리자 적립금 지급 한도 설정 등록
     *
     * @param AdminPointSettingRequestDto
     * @return int
     * @throws 	Exception
     */
    protected AdminPointSettingResponseDto addAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto)throws Exception {

    	AdminPointSettingResponseDto result = new AdminPointSettingResponseDto();

    	// 역할그룹 확인
    	AdminPointSettingVo adminPointSettingVo = adminPointSettingMapper.getAdminPointSettingTarget(adminPointSettingRequestDto);


    	if(adminPointSettingVo == null) {
	    	// 관리자 적립금 지급 한도 설정 등록
	    	adminPointSettingMapper.addAdminPointSetting(adminPointSettingRequestDto);

	    	// 관리자 적립금 사용설정 대상 등록
	    	adminPointSettingMapper.addAdminPointSettingTarget(adminPointSettingRequestDto);
    	}else {
    		adminPointSettingRequestDto.setPmPointAdminSettingId(adminPointSettingVo.getPmPointAdminSettingId());
    		adminPointSettingRequestDto.setDelYn("N");
    		adminPointSettingRequestDto.setCreateUserId(adminPointSettingRequestDto.getUserVo().getUserId());
    		// 관리자 적립금 지급 한도 설정 수정
        	adminPointSettingMapper.putAdminPointSetting(adminPointSettingRequestDto);
    	}

    	// 관리자 적립금 지급 한도 설정 이력 등록
    	adminPointSettingMapper.addAdminPointSettingHistory(adminPointSettingRequestDto);

    	return result;

    }

    /**
     * 관리자 적립금 지급 한도 설정 수정
     *
     * @param AdminPointSettingRequestDto
     * @return int
     * @throws 	Exception
     */
    protected AdminPointSettingResponseDto putAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto)throws Exception {

    	AdminPointSettingResponseDto result = new AdminPointSettingResponseDto();

    	// 관리자 적립금 지급 한도 설정 수정
    	adminPointSettingMapper.putAdminPointSetting(adminPointSettingRequestDto);

    	// 관리자 적립금 지급 한도 설정 이력 등록
    	adminPointSettingMapper.addAdminPointSettingHistory(adminPointSettingRequestDto);


    	return result;

    }


    /**
     * 관리자 적립금 지급 한도 설정 삭제
     *
     * @param AdminPointSettingRequestDto
     * @return int
     * @throws 	Exception
     */
    protected AdminPointSettingResponseDto removeAdminPointSetting(AdminPointSettingRequestDto adminPointSettingRequestDto)throws Exception {

    	AdminPointSettingResponseDto result = new AdminPointSettingResponseDto();

    	// 관리자 적립금 지급 한도 설정 수정
    	adminPointSettingMapper.removeAdminPointSetting(adminPointSettingRequestDto);

    	// 관리자 적립금 지급 한도 설정 이력 수정
//    	adminPointSettingMapper.addAdminPointSettingHistory(adminPointSettingRequestDto);


    	return result;

    }

}
