package kr.co.pulmuone.bos.display.qrcode.service;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.pulmuone.bos.comm.constant.BosDomainPrefixEnum;
import kr.co.pulmuone.bos.comm.constant.BosStorageInfoEnum;
import kr.co.pulmuone.v1.comm.interfaceType.StorageInfoBaseType.StorageType;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeRequestDto;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeResponseDto;
import kr.co.pulmuone.v1.display.qrcode.service.DisplayQrcodeBiz;

/**
* <PRE>
* Forbiz Korea
* 전시관리>QR코드 관리 BOS ServiceImpl
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.11.18     최윤지          최초작성
* =======================================================================
* </PRE>
*/
@Service
public class QrcodeBosServiceImpl implements QrcodeBosService {

  @Autowired
  private DisplayQrcodeBiz displayQrcodeBiz;

	@Override
	public AddQrcodeResponseDto addQrcode(AddQrcodeRequestDto addQrcodeRequestDto) throws Exception {

		String qrCodeRootStoragePath = BosStorageInfoEnum.getRootStoragePath(StorageType.PUBLIC.name());
		/* BosStorageInfoEnum 참조하여 하위 디렉토리 ( 1차 (모듈별) / 2차 (도메인별) / 3차 (시간별) ) 경로 산정 */
        String qrCodeFullSubStoragePath = BosStorageInfoEnum.getFullSubStoragePath(StorageType.PUBLIC.name(), BosDomainPrefixEnum.QR.name()); // "public","qr"

        // 파일명 난수 생성
        String qrCodeImageName = RandomStringUtils.randomAlphanumeric(5)+".png";

        int width = 300;
        int height = 300;

        addQrcodeRequestDto.setWidth(width);
        addQrcodeRequestDto.setHeight(height);
        addQrcodeRequestDto.setQrCodeRootStoragePath(qrCodeRootStoragePath);
        addQrcodeRequestDto.setQrCodeFullSubStoragePath(qrCodeFullSubStoragePath);
        addQrcodeRequestDto.setQrCodeImageName(qrCodeImageName);

		return displayQrcodeBiz.addQrcode(addQrcodeRequestDto);
	}

}
