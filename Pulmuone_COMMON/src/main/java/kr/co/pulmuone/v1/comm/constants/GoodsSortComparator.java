package kr.co.pulmuone.v1.comm.constants;

import java.util.Comparator;
import java.util.List;

import kr.co.pulmuone.v1.search.searcher.dto.GoodsSearchResultDto;

public class GoodsSortComparator implements Comparator<GoodsSearchResultDto>{

	private List<Long> sortList;

	public GoodsSortComparator(List<Long> sortList) {
		this.sortList = sortList;
	}

	@Override
	public int compare(GoodsSearchResultDto o1, GoodsSearchResultDto o2) {
		return sortList.indexOf(o1.getGoodsId()) > sortList.indexOf(o2.getGoodsId()) ? 1 : -1;
	}

}