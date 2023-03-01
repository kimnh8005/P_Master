package kr.co.pulmuone.v1.comm.util;

import java.math.BigDecimal;

import org.apache.commons.lang.ArrayUtils;

public class BigDecimalUtils {

    public static BigDecimal sum(BigDecimal... values) {
        if (ArrayUtils.isEmpty(values)) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (BigDecimal value : values) {
            sum = sum.add(nvl(value));
        }
        return sum;
    }

    public static BigDecimal nvl(BigDecimal value) {
        return nvl(value, BigDecimal.ZERO);
    }

    public static BigDecimal nvl(BigDecimal value, BigDecimal defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static BigDecimal subtract(BigDecimal value1, BigDecimal value2) {
        return nvl(value1).subtract(nvl(value2));
    }

    public static BigDecimal to(String value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(value);
    }

    public static BigDecimal to(Object value) {
        return value == null ? BigDecimal.ZERO : new BigDecimal(String.valueOf(value));
    }

    public static long to(BigDecimal value) {
        return nvl(value).longValue();
    }
}