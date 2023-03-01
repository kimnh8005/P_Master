package kr.co.pulmuone.bos.statics.data;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import kr.co.pulmuone.v1.comm.base.ApiResult;
import kr.co.pulmuone.v1.statics.data.service.DataDownloadStaticsBiz;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsRequestDto;
import kr.co.pulmuone.v1.statics.data.dto.DataDownloadStaticsResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
public class DataDownloadStaticsController {

    @Autowired
    private DataDownloadStaticsBiz dataDownloadStaticsBiz;

    @RequestMapping(value = "/admin/statics/data/getDataDownloadStaticsList")
    @ApiOperation(value = "데이터 추출 조회")
    @ApiResponses(value = {
            @ApiResponse(code = 900, message = "response data", response = DataDownloadStaticsResponseDto.class)
    })
    public ApiResult<?> selectMissOutboundStaticsList(DataDownloadStaticsRequestDto dataDownloadStaticsRequestDto) throws Exception {
        return dataDownloadStaticsBiz.getDataDownloadStaticsList(dataDownloadStaticsRequestDto);
    }
}
