package kr.co.pulmuone.v1.comm.mappers.slaveEcs;

import kr.co.pulmuone.v1.api.ecs.dto.vo.QnaToEcsVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface EcsMapper {

	int addQnaToEcs(QnaToEcsVo qnaToEcsVo);

	int putQnaToEcs(QnaToEcsVo qnaToEcsVo);

	int addQnaAnswerToEcs(QnaToEcsVo qnaToEcsVo);
}
