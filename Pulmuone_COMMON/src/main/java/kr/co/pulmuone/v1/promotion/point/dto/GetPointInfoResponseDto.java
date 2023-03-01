package kr.co.pulmuone.v1.promotion.point.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetPointInfoResponseDto {
    private int pointUsable;
    private String pointExpectExpired;
}
