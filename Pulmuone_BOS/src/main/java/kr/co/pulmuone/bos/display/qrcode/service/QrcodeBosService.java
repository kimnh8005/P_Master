package kr.co.pulmuone.bos.display.qrcode.service;

import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeRequestDto;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeResponseDto;

/**
* <PRE>
* Forbiz Korea
* 전시관리>QR코드 관리 BOS Service
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

public interface QrcodeBosService {

	AddQrcodeResponseDto addQrcode(AddQrcodeRequestDto addQrcodeRequestDto) throws Exception;

}
