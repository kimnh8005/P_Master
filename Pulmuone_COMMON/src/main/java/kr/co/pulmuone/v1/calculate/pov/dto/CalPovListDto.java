package kr.co.pulmuone.v1.calculate.pov.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import kr.co.pulmuone.v1.calculate.pov.dto.vo.CalPovAllocationVo;
import kr.co.pulmuone.v1.comm.util.BigDecimalUtils;
import lombok.Getter;

@Getter
public class CalPovListDto {

	private Map<String, Map<String, BigDecimal>> temporaryAllocationSummary;
	private Map<String, Map<String, BigDecimal>> finalAllocationSummary;
	private Map<String, Map<String, BigDecimal>> differenceSummary;

	public CalPovListDto(List<CalPovAllocationVo> temporaryAllocation, List<CalPovAllocationVo> finalAllocation) {
		this.temporaryAllocationSummary = groupByCorporation(temporaryAllocation);
		this.finalAllocationSummary = groupByCorporation(finalAllocation);
		this.differenceSummary = calculateDifferenceSummary();
	}

	protected Map<String, Map<String, BigDecimal>> groupByCorporation(List<CalPovAllocationVo> vos) {
		Map<String, Map<String, BigDecimal>> group = new HashMap<>();
		if (CollectionUtils.isEmpty(vos)) {
			return group;
		}
		for (CalPovAllocationVo vo : vos) {
			String key = vo.getCorporationCode();
			Map<String, BigDecimal> child = getMap(group, key);

			String childKey = vo.getAllocationType();
			BigDecimal cost = BigDecimalUtils.sum(child.get(childKey), vo.getCost());
			child.put(childKey, cost);

			group.put(key, child);
		}
		return group;
	}

	protected Map<String, Map<String, BigDecimal>> calculateDifferenceSummary() {
		Map<String, Map<String, BigDecimal>> group = new HashMap<>();
		Set<String> keys = mergeKeys();
		for (String key : keys) {
			group.put(key, getDifferenceCostByAllocationType(key));
		}
		return group;
	}

	private Map<String, BigDecimal> getDifferenceCostByAllocationType(String key) {
		Map<String, BigDecimal> child = new HashMap<>();
		Set<String> childKeys = mergeChildKeys(key);
		for (String childKey : childKeys) {
			child.put(childKey, calculateDiffCost(key, childKey));
		}
		return child;
	}

	private BigDecimal calculateDiffCost(String key, String childKey) {
		return BigDecimalUtils.subtract(getMap(this.temporaryAllocationSummary, key).get(childKey),
				getMap(this.finalAllocationSummary, key).get(childKey));
	}

	private Set<String> mergeKeys() {
		return mergeSet(temporaryAllocationSummary.keySet(), finalAllocationSummary.keySet());
	}

	private Set<String> mergeChildKeys(String key) {
		return mergeSet(getMap(this.temporaryAllocationSummary, key).keySet(),
				getMap(this.finalAllocationSummary, key).keySet());
	}

	private <T> Set<T> mergeSet(final Set<T> a, final Set<T> b) {
		return new HashSet<T>() {
			{
				addAll(a);
				addAll(b);
			}
		};
	}

	private <K, V> Map<String, BigDecimal> getMap(Map<K, V> map, Object key) {
		return MapUtils.getMap(map, key, new HashMap());
	}

	public Map<String, BigDecimal> sumTemporarySummaryByAllocationType() {
		return sumByAllocationType(this.temporaryAllocationSummary);
	}

	public Map<String, BigDecimal> sumFinalSummaryByAllocationType() {
		return sumByAllocationType(this.finalAllocationSummary);
	}

	public Map<String, BigDecimal> sumDifferenceSummaryByAllocationType() {
		return sumByAllocationType(this.differenceSummary);
	}

	protected Map<String, BigDecimal> sumByAllocationType(Map<String, Map<String, BigDecimal>> summary) {
		Map<String, BigDecimal> totalSum = new HashMap<>();
		if (MapUtils.isEmpty(summary)) {
			return totalSum;
		}

		for (String key : summary.keySet()) {
			Map<String, BigDecimal> child = summary.get(key);

			for (String childKey : child.keySet()) {
				BigDecimal sum = BigDecimalUtils.sum(totalSum.get(childKey), child.get(childKey));
				totalSum.put(childKey, sum);
			}
		}
		return totalSum;
	}
}
