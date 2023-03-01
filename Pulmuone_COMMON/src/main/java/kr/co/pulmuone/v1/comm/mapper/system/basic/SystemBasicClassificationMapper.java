package kr.co.pulmuone.v1.comm.mapper.system.basic;

import java.util.List;

import com.github.pagehelper.Page;
import kr.co.pulmuone.v1.system.basic.dto.GetClassificationListParamDto;
import kr.co.pulmuone.v1.system.basic.dto.vo.GetClassificationListResultVo;
import org.apache.ibatis.annotations.Mapper;

import kr.co.pulmuone.v1.system.basic.dto.SaveClassificationRequestDto;
import kr.co.pulmuone.v1.system.basic.dto.GetClassificationResultVo;
import kr.co.pulmuone.v1.system.basic.dto.GetTypeListResultVo;

@Mapper
public interface SystemBasicClassificationMapper {
	int getClassificationListCount(GetClassificationListParamDto vo);
	Page<GetClassificationListResultVo> getClassificationList(GetClassificationListParamDto vo);
	GetClassificationResultVo getClassification(Long stClassificationId);
	int checkClassificationDuplicate(SaveClassificationRequestDto dto);
	int addClassification(SaveClassificationRequestDto dto);
	int putClassification(SaveClassificationRequestDto dto);
	int delClassification(Long id);
	List<GetTypeListResultVo> getTypeList();
}
