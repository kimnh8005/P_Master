package kr.co.pulmuone.v1.display.qrcode.service;

import kr.co.pulmuone.v1.comm.CommonServiceTestBaseForJunit5;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeRequestDto;
import kr.co.pulmuone.v1.display.qrcode.dto.AddQrcodeResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DisplayQrcodeServiceTest extends CommonServiceTestBaseForJunit5 {

    @Autowired
    private DisplayQrcodeService displayQrcodeService;

    @BeforeEach
    void beforeEach() {
        preLogin();
    }

    @Test
    void QR코드_생성_성공() throws Exception {
        //given
        AddQrcodeRequestDto addQrcodeRequestDto = new AddQrcodeRequestDto();
        addQrcodeRequestDto.setQrCodeUrl("http://localhost:8280/layout.html#/qrCodeMgm");
        addQrcodeRequestDto.setHeight(300);
        addQrcodeRequestDto.setWidth(300);
        addQrcodeRequestDto.setQrCodeImageName("QRCodeImg");
        addQrcodeRequestDto.setQrCodeFullSubStoragePath("test");
        addQrcodeRequestDto.setQrCodeRootStoragePath("test");

        //when
        AddQrcodeResponseDto addQrcodeResponseDto = displayQrcodeService.addQrcode(addQrcodeRequestDto);

        //then
        assertNotNull(addQrcodeResponseDto);
    }

    @Test
    void QR코드_생성_실패() {
        //given
        AddQrcodeRequestDto addQrcodeRequestDto = new AddQrcodeRequestDto();
        addQrcodeRequestDto.setQrCodeUrl("");
        addQrcodeRequestDto.setHeight(300);
        addQrcodeRequestDto.setWidth(300);
        addQrcodeRequestDto.setQrCodeImageName("QRCodeImg");
        addQrcodeRequestDto.setQrCodeFullSubStoragePath(addQrcodeRequestDto.getQrCodeFullSubStoragePath());
        addQrcodeRequestDto.setQrCodeRootStoragePath(addQrcodeRequestDto.getQrCodeRootStoragePath());

        //when, then
        assertThrows(Exception.class, () -> displayQrcodeService.addQrcode(addQrcodeRequestDto));
    }

}
