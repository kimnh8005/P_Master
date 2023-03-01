package kr.co.pulmuone.v1.od.order;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.order.order.OutmallOrderDetailListMapper;
import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListRequestDto;
import kr.co.pulmuone.v1.od.order.dto.GetOutmallOrderDetailListResponseDto;
import kr.co.pulmuone.v1.od.order.dto.vo.GetOutmallOrderDetailListResultVo;
import lombok.extern.slf4j.Slf4j;

/**
 * <PRE>
 * Forbiz Korea
 * Class의 기능과 역할을 상세히 기술한다.
 * </PRE>
 *
 * <PRE>
 * <B>History:</B>
 * =======================================================================
 *  버전  :   작성일          :  작성자      :  작성내역
 * -----------------------------------------------------------------------
 *  1.0    20200612    	  천혜현           최초작성
 * =======================================================================
 * </PRE>
 */
@Slf4j
@Service
public class OutmallOrderDetailListService {

    @Autowired
    private OutmallOrderDetailListMapper outmallOrderDetailListMapper;



    /**
     * 휴면회원 이력리스트조회 > 외부몰 주문상세 리스트
     * @param GetOutmallOrderDetailListRequestDto
     * @return GetOutmallOrderDetailListResultVo
     * @throws Exception
     */
    protected GetOutmallOrderDetailListResponseDto getOutmallOrderDetailList(GetOutmallOrderDetailListRequestDto getOutmallOrderDetailListRequestDto) throws Exception {
        if(StringUtils.isNotEmpty(getOutmallOrderDetailListRequestDto.getCondiValue())) {
            ArrayList<String> array = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(getOutmallOrderDetailListRequestDto.getCondiValue(), "\n|,");
            while(st.hasMoreElements()) {
                String object = (String)st.nextElement();
                array.add(object);
            }
            getOutmallOrderDetailListRequestDto.setCondiValueArray(array);
        }

        PageMethod.startPage(getOutmallOrderDetailListRequestDto.getPage(), getOutmallOrderDetailListRequestDto.getPageSize());
        Page<GetOutmallOrderDetailListResultVo> rows = outmallOrderDetailListMapper.getOutmallOrderDetailList(getOutmallOrderDetailListRequestDto);

        return GetOutmallOrderDetailListResponseDto.builder()
                .total((int) rows.getTotal())
                .rows(rows.getResult())
                .build();
    }

}
