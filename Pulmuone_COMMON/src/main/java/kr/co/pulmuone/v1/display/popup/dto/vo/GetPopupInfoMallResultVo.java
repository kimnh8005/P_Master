package kr.co.pulmuone.v1.display.popup.dto.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class GetPopupInfoMallResultVo {

	/*
	 * 전시팝업 PK
	 */
    String dpFrontPopupId;

	/*
	 * 팝업 제목
	 */
    String popupSubject;

	/*
	 * 오늘 그만보기 여부
	 */
    String todayStopYn;

	/*
	 * 팝업 유형
	 */
    String popupType;

	/*
	 * 링크 url
	 */
    String linkUrl;

	/*
	 * 팝업 내용
	 */
    String popupDescription;

	/*
	 * 팝업 이미지 경로
	 */
    String popupImagePath;

	/*
	 * 팝업 이미지명
	 */
    String popupImageOriginName;

}
