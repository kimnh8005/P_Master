package kr.co.pulmuone.v1.user.warehouse.service;

import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.page.PageMethod;

import kr.co.pulmuone.v1.comm.mapper.user.warehouse.DeliveryPatternMapper;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternDetailResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternRequestDto;
import kr.co.pulmuone.v1.user.warehouse.dto.DeliveryPatternResponseDto;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternListVo;
import kr.co.pulmuone.v1.user.warehouse.dto.vo.DeliveryPatternVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service
public class DeliveryPatternService {


	 private final DeliveryPatternMapper deliveryPatternMapper;

    protected Page<DeliveryPatternListVo> getDeliveryPatternList(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception {
    	PageMethod.startPage(deliveryPatternRequestDto.getPage(), deliveryPatternRequestDto.getPageSize());
        return deliveryPatternMapper.getDeliveryPatternList(deliveryPatternRequestDto);
    }

    /**
     * 배송패턴 상세조회
     * @param deliveryPatternRequestDto
     * @return
     * @throws Exception
     */
    protected DeliveryPatternDetailResponseDto getShippingPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception {
    	DeliveryPatternDetailResponseDto resultVo = new DeliveryPatternDetailResponseDto();
    	DeliveryPatternVo vo = new DeliveryPatternVo();
    	vo = deliveryPatternMapper.getShippingPattern(deliveryPatternRequestDto);
    	resultVo.setRows(vo);
    	return resultVo;
    }

    /**
     * 배송패턴 등록
     * @param deliveryPatternRequestDto
     * @return
     * @throws Exception
     */
    protected DeliveryPatternResponseDto addDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception{
    	DeliveryPatternResponseDto result = new DeliveryPatternResponseDto();
    	deliveryPatternMapper.addDeliveryPattern(deliveryPatternRequestDto);
    	return result;
    }

    /**
     * 배송패턴 수정
     * @param deliveryPatternRequestDto
     * @return
     * @throws Exception
     */
    protected DeliveryPatternResponseDto putDeliveryPattern(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception{
    	DeliveryPatternResponseDto result = new DeliveryPatternResponseDto();
    	deliveryPatternMapper.putDeliveryPattern(deliveryPatternRequestDto);
    	return result;
    }

    /**
     * 배송패턴 수정
     * @param deliveryPatternRequestDto
     * @return
     * @throws Exception
     */
    protected DeliveryPatternResponseDto addShippingPatternDay(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception{
    	DeliveryPatternResponseDto result = new DeliveryPatternResponseDto();
    	deliveryPatternMapper.addShippingPatternDay(deliveryPatternRequestDto);
    	return result;
    }

    /**
     *
     * @param deliveryPatternRequestDto
     * @return
     * @throws Exception
     */
    protected DeliveryPatternResponseDto removeShippingPatternDay(DeliveryPatternRequestDto deliveryPatternRequestDto) throws Exception{
    	DeliveryPatternResponseDto result = new DeliveryPatternResponseDto();
    	deliveryPatternMapper.removeShippingPatternDay(deliveryPatternRequestDto);
    	return result;
    }





}
