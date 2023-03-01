package kr.co.pulmuone.v1.comm.mapper.goods.notice;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;

import kr.co.pulmuone.v1.goods.notice.dto.GoodsNoticeDto;
import kr.co.pulmuone.v1.goods.notice.dto.vo.GoodsNoticeVo;

import java.util.List;

@Mapper
public interface GoodsNoticeMapper {

	GoodsNoticeVo getGoodsNoticeInfo(String ilNoticeId);
	Page<GoodsNoticeVo> getGoodsNoticeList(GoodsNoticeDto dto);
	int addGoodsNotice(GoodsNoticeDto dto);
	int putGoodsNotice(GoodsNoticeDto dto);

	List<GoodsNoticeVo> getGoodsNoticeListByUser();

}
