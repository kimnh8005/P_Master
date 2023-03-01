package kr.co.pulmuone.v1.user.store.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.enums.StoreEnums;
import kr.co.pulmuone.v1.comm.framework.dto.UploadFileDto;
import kr.co.pulmuone.v1.comm.mapper.user.store.StoreBosMapper;
import kr.co.pulmuone.v1.goods.goods.dto.ArrivalScheduledDateDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDeliveryAreaListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreDetailRequestDto;
import kr.co.pulmuone.v1.user.store.dto.StoreListRequestDto;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDeliveryAreaVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreDetailVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreImageVo;
import kr.co.pulmuone.v1.user.store.dto.vo.StoreListVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {

	private final StoreBosMapper storeMapper;

	/**
     * 배송권역 관리 리스트
     * @param storeDeliveryAreaListRequestDto
     * @return
     * @throws Exception
     */
    protected Page<StoreDeliveryAreaVo> getStoreDeliveryAreaList(StoreDeliveryAreaListRequestDto storeDeliveryAreaListRequestDto) throws Exception {
    	PageMethod.startPage(storeDeliveryAreaListRequestDto.getPage(), storeDeliveryAreaListRequestDto.getPageSize());
        return storeMapper.getStoreDeliveryAreaList(storeDeliveryAreaListRequestDto);
    }

	/**
     * 매장목록 리스트
     * @param StoreListRequestDto
     * @return
     * @throws Exception
     */
    protected Page<StoreListVo> getStoreList(StoreListRequestDto storeListRequestDto) throws Exception {
    	PageMethod.startPage(storeListRequestDto.getPage(), storeListRequestDto.getPageSize());
        return storeMapper.getStoreList(storeListRequestDto);
    }


	/**
	* @Desc 매장 상세정보
	* @param StoreDetailRequestDto
	* @return StoreDetailVo
	*/
	protected StoreDetailVo getStoreDetail(StoreDetailRequestDto storeDetailRequestDto) {
		return storeMapper.getStoreDetail(storeDetailRequestDto);
	}

	/**
	* @Desc 매장 권역 상세정보
	* @param StoreDetailRequestDto
	* @return StoreDetailVo
	*/
	protected List<StoreDeliveryAreaVo> getStoreDeliveryList(StoreDetailRequestDto storeDetailRequestDto) {
		return storeMapper.getStoreDeliveryList(storeDetailRequestDto);
	}



	/**
	 * 매장 상세정보 수정
	 * @param storeDetailRequestDto
	 * @return
	 */
	protected int modifyStoreDetail(StoreDetailRequestDto storeDetailRequestDto) {

		//매장 PC 첨부 이미지 URL
		if(storeDetailRequestDto.getStorePcImageUploadResultList() != null) {
			int storePcImageCnt = storeDetailRequestDto.getStorePcImageUploadResultList().size();

			if(storePcImageCnt > 0) {
				for (UploadFileDto uploadFileDto : storeDetailRequestDto.getStorePcImageUploadResultList()) {
					storeDetailRequestDto.setStorePcImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
				}

				storeDetailRequestDto.setStoreImageType(StoreEnums.storeImageType.PC.getCode());
				this.delStoreImage(storeDetailRequestDto);

				for(UploadFileDto uploadFileDto : storeDetailRequestDto.getStorePcImageUploadResultList()) {
					StoreImageVo addStorePcImageVo = StoreImageVo.builder()
							.urStoreId(storeDetailRequestDto.getUrStoreId())
							.storeImageType(StoreEnums.storeImageType.PC.getCode())
							.imagePath(uploadFileDto.getServerSubPath())
							.imageName(uploadFileDto.getPhysicalFileName())
							.imageOriginalName(uploadFileDto.getOriginalFileName())								// 원본 파일명
							.createUserId(storeDetailRequestDto.getUserVo().getUserId())
							.build();

					this.addStoreImage(addStorePcImageVo);
				}
			}

		}

		//매장 Mobile 첨부 이미지 URL
		if(storeDetailRequestDto.getStoreMobileImageUploadResultList() != null) {
			int storeMobileImageCnt = storeDetailRequestDto.getStoreMobileImageUploadResultList().size();

			if(storeMobileImageCnt > 0) {
				for (UploadFileDto uploadFileDto : storeDetailRequestDto.getStoreMobileImageUploadResultList()) {
					storeDetailRequestDto.setStoreMobileImageUrl(uploadFileDto.getServerSubPath() + uploadFileDto.getPhysicalFileName());
				}

				storeDetailRequestDto.setStoreImageType(StoreEnums.storeImageType.MOBILE.getCode());
				this.delStoreImage(storeDetailRequestDto);

				for(UploadFileDto uploadFileDto : storeDetailRequestDto.getStoreMobileImageUploadResultList()) {
					StoreImageVo addStoreMobileImageVo = StoreImageVo.builder()
							.urStoreId(storeDetailRequestDto.getUrStoreId())
							.storeImageType(StoreEnums.storeImageType.MOBILE.getCode())
							.imagePath(uploadFileDto.getServerSubPath())
							.imageName(uploadFileDto.getPhysicalFileName())
							.imageOriginalName(uploadFileDto.getOriginalFileName())
							.createUserId(storeDetailRequestDto.getUserVo().getUserId())
							.build();

					this.addStoreImage(addStoreMobileImageVo);
				}
			}

		}

		return storeMapper.modifyStoreDetail(storeDetailRequestDto);
	}


	/**
	* @Desc 매장 이미지 저장
	* @param StoreImageVo
	* @return int
	*/
	protected int addStoreImage(StoreImageVo storeImageVo) {
		return storeMapper.addStoreImage(storeImageVo);
	}


	/**
	* @Desc 매장 이미지 삭제
	* @param StoreImageVo
	* @return int
	*/
	protected int delStoreImage(StoreDetailRequestDto storeDetailRequestDto) {
		return storeMapper.delStoreImage(storeDetailRequestDto);
	}

	protected List<ArrivalScheduledDateDto> rmoveUnDeliveryDate(String urStoreId, List<ArrivalScheduledDateDto> scheduledDateList) throws Exception {
		List<LocalDate> unDeliveryDateList = storeMapper.getCheckStoreUnDeliveryDateList(urStoreId, scheduledDateList);

		if (unDeliveryDateList != null && unDeliveryDateList.size() > 0) {
			for (ArrivalScheduledDateDto dto : scheduledDateList) {
				if (unDeliveryDateList.indexOf(dto.getForwardingScheduledDate()) >= 0) {
					dto.setUnDelivery(true);
				}
			}
		}

		return scheduledDateList;
	}
}
