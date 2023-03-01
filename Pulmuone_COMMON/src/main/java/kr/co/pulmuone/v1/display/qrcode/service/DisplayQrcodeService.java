package kr.co.pulmuone.v1.display.qrcode.service;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.comm.util.QrcodeUtil;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeRequestDto;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeResponseDto;
import lombok.extern.slf4j.Slf4j;

/**
* <PRE>
* Forbiz Korea
* 전시관리 COMMON Service
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.11.18    최윤지   최초작성
* =======================================================================
* </PRE>
*/

@Slf4j
@Service
public class DisplayQrcodeService {

	/**
	 * @Desc  QR코드 생성
	 * @param addQrcodeRequestDto
	 * @throws Exception
	 * @return AddQrcodeResponseDto
	 */
	protected AddQrcodeResponseDto addQrcode(AddQrcodeRequestDto addQrcodeRequestDto) throws Exception {
		AddQrcodeResponseDto addQrcodeResponseDto = new AddQrcodeResponseDto();

		String qrCodeUrl = addQrcodeRequestDto.getQrCodeUrl();
		String qrCodeFullSubStoragePath = addQrcodeRequestDto.getQrCodeFullSubStoragePath().replaceAll("\\\\","//");
		String qrCodeImagePath = addQrcodeRequestDto.getQrCodeRootStoragePath()+addQrcodeRequestDto.getQrCodeFullSubStoragePath();
		String qrCodeImageName = addQrcodeRequestDto.getQrCodeImageName();

		int width = addQrcodeRequestDto.getWidth();
		int height = addQrcodeRequestDto.getHeight();

		try {
			if(qrCodeUrl.equals("") || qrCodeUrl == null) {
				throw new Exception();
			}
		} finally {
			addQrcodeResponseDto.setQrCodeUrl(qrCodeUrl);
		}

		//addQrcodeResponseDto.setQrCodeUrl(qrCodeUrl);
		addQrcodeResponseDto.setQrCodeFullSubStoragePath(qrCodeFullSubStoragePath);
		addQrcodeResponseDto.setQrCodeImageName(qrCodeImageName);

		QrcodeUtil.MakeQR(qrCodeUrl,width,height,qrCodeImagePath,qrCodeImageName);

		// 파일 다운로드
		String qrCodeFilePath = qrCodeFullSubStoragePath;
		String qrCodePhysicalImageName = qrCodeImageName;
		String qrCodeOriginalImageName = qrCodeImageName;

		addQrcodeResponseDto.setQrCodeFilePath(qrCodeFilePath);
		addQrcodeResponseDto.setQrCodePhysicalImageName(qrCodePhysicalImageName);
		addQrcodeResponseDto.setQrCodeOriginalImageName(qrCodeOriginalImageName);

		return addQrcodeResponseDto;
	}

}