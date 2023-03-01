package kr.co.pulmuone.v1.display.qrcode.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeRequestDto;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeResponseDto;

/**
* <PRE>
* Forbiz Korea
* 전시관리 COMMON Impl
*
* </PRE>
*
* <PRE>
* <B>History:</B>
* =======================================================================
*  버전  :   작성일                :  작성자      :  작성내역
* -----------------------------------------------------------------------
*  1.0    2020.11.18     최윤지   최초작성
* =======================================================================
* </PRE>
*/

@Service
public class DisplayQrcodeBizImpl implements DisplayQrcodeBiz {

  @Autowired
  private DisplayQrcodeService displayQrcodeService;

	/**
	 * @Desc  QR코드 생성
	 * @param addQrcodeRequestDto
	 * @throws Exception
	 */
	@Override
	public AddQrcodeResponseDto addQrcode(AddQrcodeRequestDto addQrcodeRequestDto) throws Exception {

		return displayQrcodeService.addQrcode(addQrcodeRequestDto);
	}

}