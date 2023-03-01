/**-----------------------------------------------------------------------------
 * description 		 : 주문/배송관리 > 스케줄 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.29		김승우          최초생성
 * **/
"use strict";

var SOURCE_ID = "fbSource";
var paramData;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

// 달력 Object
var fbCalendar;
// 스케줄 정보 All
var scheduleInfo;
// 스케줄 정보(상품)상단 전역변수
var goodsInfo = new Object();
// 스케줄 정보(스케줄리스트) 전역변수
var scheduleDelvDateList = new Array();
// 휴일리스트
var holydayList 	= new Array();
// 변경가능일자 리스트
var deliverableList = new Array();
// 배송완료일자 리스트
var completedList	= new Array();
// 달력정보 셋팅 (배송가능일, 출고처 휴일, 공통 휴일정보) 조회 후 셋팅
var calendarInfoCnt	= 0;
// 캘린더 현재(AS-IS) 날짜
var curArriveDate 	= "";
// 캘린더 변경(TO-BE) 날짜
var newArriveDate 	= "";
// 선택한 스케줄 일자
var selectedItemOrgDelvDate	= null;
// 선택한 스케줄 상품정보List
var selectedItemList 		= new Array();

var publicStorageUrl = fnGetPublicStorageUrl();										//이미지 BASE URL

$(document).ready(function() {
	// 초기 화면상수 선언
	const $scheduleContainer	= $(".scheduleList-container");	// 스케줄 관리 페이지
	const $arrivalContainer 	= $(".arrival-container");		// 도착일 변경 페이지
	const $arrivalNotice 		= $("#arrival__notice");		// 도착 안내 Div
	const $scheduleList 		= $("#scheduleList__list");		// 스케줄 리스트
	const $arrivalGoodsCont 	= $("#arrivalGoodsCont");		// 해당 날짜에 존재하는 상품 영역
	const $updateGoodsCont 		= $("#updateGoodsCont");		// 스케줄을 변경할 상품 영역

	// Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', { flag : false });
		fnPageInfo({
			PG_ID  		: 'scheduleMgmPopup',
			callback 	: fnUI
		});
	}

	// 화면 UI 초기화
	function fnUI() {
		fnTranslate();			// 다국어 변환
		fnInitButton();			// 버튼 초기화
		fnSearch();				// 조회
	}
	//--------------------------------- Button Start-------------------------------------------
	// 버튼 초기화
	function fnInitButton() {
		$("#fnDeliveryDayWeek, #fnDeliveryDayChange, #fnUpdateArrivalDate, #fnCancel").kendoButton();
	}

	// 배송요일 변경
	function fnDeliveryDayWeek() {
		fnKendoPopup({
			id: 'deliveryDayWeekPopup',
			title: fnGetLangData({ nullMsg: '배송요일 변경' }),
			param: goodsInfo,
			src: '#/deliveryDayWeekPopup',
			width: '600px',
			height: '400px',
			success: function(id, data) {fnSearch();}
		});
	}

	// 건너뛰기
	function fnDeliveryDayChange() {
		fnKendoPopup({
			id: 'deliveryDayChangePopup',
			title: fnGetLangData({ nullMsg: '건너뛰기' }),
			param: scheduleInfo,
			src: '#/deliveryDayChangePopup',
			width: '600px',
			height: '400px',
			success: function(id, data) {fnSearch();}
		});
	}

	// 도착일 변경 페이지로 이동
	function fnOpenChangeSchedulePop(obj) {
		let itemInfo 		= $(obj).data();
		let targetDelvDate 	= itemInfo.orgdelvdate;

		// 스케줄 관리 페이지 Close, 도착일 변경 페이지 Open
	    $scheduleContainer.toggle();
	    $arrivalContainer.toggle();

	    fnGetCurrentData(targetDelvDate).then(function(item) {
	    	selectedItemOrgDelvDate = targetDelvDate;	// 선택한 스케줄 일자
	    	selectedItemList = item.rows;				// 선택한 상품List
	    	fnSetCurrentArriveInfo(item);
	    });
	}

	// 상품 도착일 변경(저장)
	function fnUpdateArrivalDate() {

		let url 	= '/admin/order/schedule/putScheduleArrivalDate';
		let params	= new Object();
		let scheduleUpdateList	= new Array();

		params.odOrderDetlId 		= goodsInfo.odOrderDetlId;

		// 녹즙인 경우는
		if (goodsInfo.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE') {
			let selectBoxValue = 0;
			$updateGoodsCont.find('.arrival__goodsItem').each(function(index, item) {

				if (parseInt($(this).find(".arrival__totalSelector").val()) > 0) {
					selectBoxValue += parseInt($(this).find(".arrival__totalSelector").val());

					let param = new Object();
					param.id 		= selectedItemList[index].id;
					param.orderCnt	= $(this).find(".arrival__totalSelector").val();
					param.delvDate	= newArriveDate.oFormat('yyyy-MM-dd');
					scheduleUpdateList.push(param);
				}
			});
			if (selectBoxValue < 1) {
				fnKendoMessage({message : "상품수량을 확인해주세요."});
				return false;
			}
		// 베이비밀, 잇슬림
		} else {
			for (let i=0; i<selectedItemList.length; i++) {
				let param = new Object();
				param.id 		= selectedItemList[i].id;
				param.delvDate	= newArriveDate.oFormat('yyyy-MM-dd');
				scheduleUpdateList.push(param);
			}
		}
		params.scheduleUpdateListData = kendo.stringify(scheduleUpdateList);

		fnAjax({
            url     : url,
            params  : params,
            async   : true,
            success : function(resultData) {
            	fnBizCallback('scheduleChange', resultData);
            },
            fail : function(resultData, resultCode) {
            	fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                    	fnBizCallback("fail", resultData);
                    }
                });
			},
            isAction : 'select'
        });
	}

	// 취소
	function fnCancel() {
		// 스케줄 관리 페이지 Open, 도착일 변경 페이지 Close
	    $scheduleContainer.toggle();
	    $arrivalContainer.toggle();

	    fnSetCurrentArriveDate(null);
	    newArriveDate			= '';
	    selectedItemOrgDelvDate = '';
	    selectedItemList 		= new Array();
	    fnSetIsButton(false);
	}

    // 팝업 닫기
    function fnClose() {
    	parent.LAYER_POPUP_OBJECT.data("kendoWindow").close();
    }
	//--------------------------------- Button End---------------------------------------------


	//-------------------------------  Common Function start ----------------------------------
	// 기본 설정
	function fnDefaultSet() {}

	// 초기화
	function fnClear() {}

	// 조회
	function fnSearch() {
		// 스케줄 리스트 조회
		fnGetScheduleList();
		// 배송가능 스케줄 리스트 조회
		fnGetOrderDeliverableList();
		// 출고처 휴일 조회
		fnGetWarehouseHolidayList();
		// 공통 휴일 조회
		fnGetHolidayList();
	}

	// 스케줄 리스트 조회
	function fnGetScheduleList() {
		let url 	= '/admin/order/schedule/getOrderScheduleList';
		let params	= {'odOrderDetlId' : paramData.odOrderDetlId};

		fnAjax({
            url     : url,
            params  : params,
            async   : true,
            success : function(resultData) {
            	fnBizCallback('orderScheduleList', resultData);
            },
            fail : function(resultData, resultCode) {
            	fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                    	fnBizCallback("fail", resultData);
                    }
                });
			},
            isAction : 'select'
        });
	}

	//  배송가능 스케줄 리스트 조회
	function fnGetOrderDeliverableList() {
		let url 	= '/admin/order/schedule/getOrderDeliverableScheduleList';
		let params	= {'odOrderDetlId' : paramData.odOrderDetlId};

		fnAjax({
            url     : url,
            params  : params,
            async   : true,
            success : function(resultData) {
            	fnBizCallback('orderDeliverableList', resultData);
            },
            fail : function(resultData, resultCode) {
            	fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                    	fnBizCallback("fail", resultData);
                    }
                });
			},
            isAction : 'select'
        });
	}

	// 출고처 휴일 조회
	function fnGetWarehouseHolidayList() {
		let url 	= '/admin/ur/warehouse/getWarehouseHolidayListById';
		let params	= {'urWarehouseId' : paramData.urWarehouseId};

		fnAjax({
            url     : url,
            params  : params,
            async   : true,
            success : function(resultData) {
            	fnBizCallback('warehouseHolidayList', resultData);
            },
            fail : function(resultData, resultCode) {
            	fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                    	fnBizCallback("fail", resultData);
                    }
                });
			},
            isAction : 'select'
        });
	}

	// 공통 휴일 조회
	function fnGetHolidayList() {
		let url 	= '/admin/policy/holiday/getAllHolidayList';

		fnAjax({
            url     : url,
            async   : true,
            success : function(resultData) {
            	fnBizCallback('holidayList', resultData);
            },
            fail : function(resultData, resultCode) {
            	fnKendoMessage({
                    message : resultCode.message,
                    ok : function(e) {
                    	fnBizCallback("fail", resultData);
                    }
                });
			},
            isAction : 'select'
        });
	}

	// Biz Callback
	function fnBizCallback(id, result) {
    	switch (id) {
		case 'orderScheduleList' :		// 스케줄 리스트 조회
			result.code == "0000" ? fnSetData(result.data) :
				fnKendoMessage({
                    message : result.message,
                    ok : function(e) {fnClose();}
                });
            break;
		case 'orderDeliverableList' :	// 배송가능 스케줄 리스트 조회
			result.RETURN_CODE == "0000" ? fnSetDeliverableDayData(result.scheduleDelvDateList) : fnKendoMessage({message : result.RETURN_MSG});
            break;
		case 'warehouseHolidayList' :	// 출고처 휴일 조회
			result.RETURN_CODE == "0000" ? fnSetHolyDayData(result.rows) : fnKendoMessage({message : result.RETURN_MSG});
			break;
		case 'holidayList' :			// 공통 휴일 조회
			result.RETURN_CODE == "0000" ? fnSetHolyDayData(result.rows) : fnKendoMessage({message : result.RETURN_MSG});
			break;
		case 'scheduleChange' :			// 상품 도착일 변경(저장)
			if (result.code == "0000") {
				fnKendoMessage({message : "스케줄이 변경되었습니다."});
				fnCancel();
				fnSearch();
			} else {
				fnKendoMessage({message : result.message});
			}
			break;
		case 'fail' :	// 오류
			break;
		}
	}
	//-------------------------------  Common Function end ------------------------------------


	//-------------------------------  Function start -----------------------------------------
	// 스케줄 정보 셋팅
	function fnSetData(param) {
		// 스케줄 정보 전역변수 셋팅
		scheduleInfo 			= param;
		goodsInfo 				= param.goodsInfo;
		scheduleDelvDateList 	= param.scheduleDelvDateList;
		// 스케줄 상단정보 셋팅
		fnSetHeaderData(param.goodsInfo);
		// 스케줄 리스트정보 셋팅
		fnSetScheduleListData(param.scheduleDelvDateList);
	}

	// 변경가능일자 데이터 셋팅
	function fnSetDeliverableDayData(paramArr) {
		if (fnIsEmpty(paramArr) || paramArr.length < 1) return false;
		for (let i=0 ; i<paramArr.length; i++) {
			deliverableList.push(paramArr[i]);
		}
		calendarInfoCnt++;
		// 달력정보 셋팅
		if (calendarInfoCnt == 3 && !fbCalendar) {
			calendarInfoCnt = 0;
			fbCalendar = fnInitCalendar("calendar");
		}
	}

	// 휴일데이터 셋팅
	function fnSetHolyDayData(paramArr) {
		if (fnIsEmpty(paramArr) || paramArr.length < 1) return false;
		for (let i=0 ; i<paramArr.length; i++) {
			!fnIsEmpty(paramArr[i].holidayDate) ? holydayList.push(paramArr[i].holidayDate) : holydayList.push(paramArr[i].holiday)
		}
		calendarInfoCnt++;
		// 달력정보 셋팅
		if (calendarInfoCnt == 3 && !fbCalendar) {
			calendarInfoCnt = 0;
			fbCalendar = fnInitCalendar("calendar");
		}
	}

	// 스케줄 상단정보 셋팅
	function fnSetHeaderData(paramInfo) {
		$(".scheduleList__programName").html(paramInfo.goodsNm);
		$(".scheduleList__periodDate").html(paramInfo.lastDeliveryDate);
		$(".scheduleList__remainCount").html(paramInfo.saleSeq);
		$(".scheduleList__shippingDay").html(paramInfo.goodsDailyCycleTermDays);

		// 녹즙, 내맘대로 골라담기 제외 상품만 배송요일 변경, 건너뛰기 버튼 Show
		if (paramInfo.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE' && paramInfo.promotionYn != 'Y') {
			$('#fnDeliveryDayWeek').show();
			$('#fnDeliveryDayChange').show();

            //하이톡 스위치
			if(parent.isHitokSwitch && paramInfo.orderStatusCd == 'IC') {
				$('#fnDeliveryDayWeek, #fnDeliveryDayChange').attr("disabled", true);
			}
		} else {
			$('#fnDeliveryDayWeek').hide();
			$('#fnDeliveryDayChange').hide();
		}
	}

	// 스케줄 리스트정보 셋팅
	function fnSetScheduleListData(paramArr) {
		if (fnIsEmpty(paramArr) || paramArr.length < 1) return false;

		let htmlString = "";
		$.each(paramArr, function(index, goodsScheduleInfo) {
			// 녹즙은 deliveryYn으로 체크, (베이비밀, 잇슬림)은 배송예정일이 오늘날짜보다 크면 배송완료로 처리
			let deliveryYn = goodsInfo.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE' ? goodsScheduleInfo.deliveryYn :
				fnGetToday('yyyyMMdd') - goodsScheduleInfo.orgDelvDate.replace(/-/g, '') > 0 ? 'Y' : 'N';
			if (deliveryYn == 'Y') completedList.push(goodsScheduleInfo.orgDelvDate);

			// 마감시간 지났으면 4일 아니면 3일
			// let dayCnt = goodsScheduleInfo.cutoffTimeYn == "Y" ? 4 : 3;
			//
			// // 스케줄 변경가능여부  (배송예정일이 오늘 +4일부터 가능, I/F 전송전에만 가능)
			// let changeBtnYn = goodsScheduleInfo.orgDelvDate.replace(/-/g, '') - fnGetDayAdd(fnGetToday(), dayCnt, 'yyyyMMdd') >= 0 ? 'Y' : 'N';

			// 스케줄 변경가능여부
			let changeBtnYn = goodsScheduleInfo.changeDeliveryDateBtnYn;
			// 스케줄 저장을 위한 ID값 셋팅(녹즙인 경우 ID 값 =>odOrderDetlDailySchId);
			if (changeBtnYn == 'Y') {
				for (let i=0; i<goodsScheduleInfo.rows.length; i++) {
					goodsScheduleInfo.rows[i].id = goodsInfo.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE' ? goodsScheduleInfo.rows[i].odOrderDetlDailySchId : goodsScheduleInfo.rows[i].id;
				}
			}

			htmlString +=
				`<li class="scheduleList__schedule" style="border-top: solid 1px;" data-item=${goodsScheduleInfo}>
					<p class="scheduleList__scheduleHeader" style="padding-top: 5px;">
						<span class="scheduleList__date">${goodsScheduleInfo.delvDate} (${goodsScheduleInfo.delvDateWeekDay})</span>
						<input type="hidden" name="scheduleList__dateStr" value="${goodsScheduleInfo.orgDelvDate}" />
						<span class="scheduleList__status">${deliveryYn == "Y" ? "배송완료" : ""}</span>
						${changeBtnYn == 'Y' ? '<button type="button" class="scheduleList__changeBtn btn-point btn-s" data-orgDelvDate=' + goodsScheduleInfo.orgDelvDate + ' onclick="$scope.fnOpenChangeSchedulePop(this);" >변경</button>' : ""}
					</p>
					<div class="scheduleList__goodsContainer">
						<div class="scheduleList__goods">
							<ul class="scheduleList__goodsList">${fnSetScheduleListGoodsData(goodsScheduleInfo.rows)}</ul>
						</div>
					</div>
				</li>`;
		});

		// 스케줄 상품정보List 전역변수 셋팅. ID추가 셋팅완료
		scheduleDelvDateList = paramArr;
		$scheduleList.html(htmlString);
	}

	// 스케줄 리스트 상품정보 셋팅
	function fnSetScheduleListGoodsData(paramArr) {
		if (fnIsEmpty(paramArr) || paramArr.length < 1) return false;

		let htmlString = "";
		$.each(paramArr, function(index, goodsInfo) {
			htmlString +=
				`<li class="scheduleList__goodsItem">
					<div class="scheduleList__goodsImage">
						<figure>
							<img src="${publicStorageUrl}${goodsInfo.goodsImgNm}" alt="${publicStorageUrl}${goodsInfo.goodsNm}">
						</figure>
					</div>
					<div class="scheduleList__goodsInfo">
						<p class="scheduleList__goodsName">${goodsInfo.goodsNm}</p>
						<small>수량: <span class="scheduleList__goodsCount">${goodsInfo.orderCnt}</span></small>
					</div>
				</li>`;
		});
		return htmlString;
	}

	// 도착예정일 변경  변경전 스케줄 정보 셋팅
	function fnRenderCurrentArriveDate(dateStr) {
		let $target = $("#arrival__currentDate");
	    $target.html(dateStr);
	}

	// 도착예정일 변경  변경후 스케줄 정보 셋팅
	function fnRenderNewArriveDate(dateStr) {
		let $target = $("#arrival__newDate");
	    $target.html(dateStr);
	}

	// 도착예정일 변경 상품 영역정보 셋팅
	function fnRenderUpdateGoodsCont(paramArr, paramClear) {
		if (fnIsEmpty(paramArr) || paramArr.length < 1) return false;
		let htmlString = fnRenderArrivalGoods(paramArr, paramClear);

		paramClear ? $updateGoodsCont.html(htmlString) : $updateGoodsCont.append(htmlString);
	}

	// 도착예정일 변경 상품 정보 셋팅(개별 상품인 경우 single, 묶음 상품인 경우 multi)
	function fnRenderArrivalGoods(paramArr, paramClear) {
		// 녹즙인 경우만 수량 변경 가능, 달력 날짜 변경시 보여주는 상품은 단순 확인용
		let paramReadOnly = paramClear == true && goodsInfo.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE' ? false : true;
		let htmlString =
			`<div id="arrivalGoodsInfo" class="arrival__goods ${paramClear == true ? '' : 'arrival__readOnly'}" data-delvDate="${selectedItemOrgDelvDate}">
				<ul class="arrival__goodsList ${paramArr.length > 1 ? "multi" : "single"}" >${fnRenderArrivalGoodsList(paramArr, paramReadOnly)}</ul>
			</div>`;
		return htmlString;
	}

	// 도착예정일 변경  상품 영역 상품 리스트 렌더링
	function fnRenderArrivalGoodsList(paramArr, paramReadOnly) {

		let htmlString = "";
		$.each(paramArr, function(index, goodsInfo) {
			htmlString += `
				<li class="arrival__goodsItem">
					<div class="arrival__goodsImage">
	            		<figure>
	              			<img src="${publicStorageUrl}${goodsInfo.goodsImgNm}" alt="${publicStorageUrl}${goodsInfo.goodsNm}">
	            		</figure>
					</div>
					<div class="arrival__goodsInfo">
	            		<span class="arrival__goodsName">${goodsInfo.goodsNm}</span>
	            		<span style="float: right;">수량:
	            			${paramReadOnly == false
	            			? '<select class="arrival__totalSelector k-state-default">' + fnRenderArrivalSelect(goodsInfo.orderCnt,goodsInfo.orderCnt,paramArr.length) + '</select>'
	            			: '<span class="arrival__goodsTotal">' + goodsInfo.orderCnt + '</span>'}
	            		</span>
					</div>
				</li>`;
		});
		return htmlString;
	}

	// 도착예정일 변경 셀렉트 박스 렌더링
	function fnRenderArrivalSelect(count, max, total) {
		// 내맘대로 골라담기 요일별 품목이 2개 이상인 경우 -> 수량 0 도 선택 가능
		let min = total > 1 ? 0 : 1;
		max = max ? max : 20;

		let htmlString = "";
		for (let i=min; i<=max; i++) {
			htmlString += `<option value="${i}" ${ (i) == count ? "selected" : "" }>${i}</option>`;
		}
		return htmlString;
	}

	// 현재 선택한 일자의 상품 스케줄 정보 조회
	function fnGetCurrentData(paramDate) {
		return new Promise(function(resolve, reject) {
			let targetItem = scheduleDelvDateList.find(function(item) {
				return item.orgDelvDate === paramDate;
			});
			resolve(targetItem);
		});
	}

	// 현재 선택한 일자의 상품 스케줄 정보 셋팅(도착예정일)
	function fnSetCurrentArriveInfo(paramInfo) {
		if (fnIsEmpty(paramInfo) || fnIsEmpty(paramInfo.rows) || paramInfo.rows.length < 1) return false;
		let date = paramInfo.orgDelvDate;

		// 스케줄변경 달력정보 셋팅
		fnSetCurrentArriveDate(date);
		// 스케줄변경 상품정보 셋팅
		fnRenderUpdateGoodsCont(paramInfo.rows, true);
	    fbCalendar.destroy().render();

	    // 베이비밀 주말 배송 안내 문구
	    if (goodsInfo.goodsDailyTp == 'GOODS_DAILY_TP.BABYMEAL' && (paramInfo.delvDateWeekDay == '토' || paramInfo.delvDateWeekDay == '일')) {
	    	fnShowArrivalNotice(true);
	    } else {
	    	fnShowArrivalNotice(false);
	    }
	}

	// 베이비밀 주말 배송 안내 정보 Show/Hide
	function fnShowArrivalNotice(param) {
		param ? $arrivalNotice.addClass("show") : $arrivalNotice.removeClass("show");
	}

	// 달력에 현재 선택한 스케줄 도착 예정일 설정
	function fnSetCurrentArriveDate(date) {
	    if (!fbCalendar) fbCalendar = fnInitCalendar("calendar");

	    if (!date) {
	      fbCalendar.removeAllEvents();
	      fnRenderCurrentArriveDate("");
	    } else {
	    	let curDate	= date.replace(/-/g,'').replace(/\//g,'');
	    	let year  	= curDate.substring(0,4);
	    	let month 	= curDate.substring(4,6);
	    	let day   	= curDate.substring(6,8);
	    	let dt = new Date(year, month-1, day);

		    fbCalendar.setDate(dt);
		    fbCalendar.setEvent(day, dt, SOURCE_ID);
		    fnRenderCurrentArriveDate(fnGetFullDate(dt));
	    }
	    curArriveDate = date;
	    fnRenderNewArriveDate("변경 일자 선택");
	}

	// 도착예정일 변경 날짜 표현식
	function fnGetFullDate(date) {
	    return date.oFormat("yyyy년 MM월 dd일 (E)").replace("요일", "");
	}

	// 상품 도착일 변경 버튼 토글
	function fnSetIsButton(toggle) {
	    let $arrivalContainer = $("#arrival-container");
	    toggle ? $arrivalContainer.addClass("isButton") : $arrivalContainer.removeClass("isButton");
	}
	//-------------------------------  Functions End ------------------------------------------


	//------------------------------- Calendar Start ------------------------------------------
	// 달력 초기 설정
	function fnInitCalendar(id) {
		let FULL_DATE_FORMAT = "yyyy-MM-dd";
		var initialDate = new Date().oFormat(FULL_DATE_FORMAT);

		var calendarEl = document.querySelector("#" + id);
		var calendarOpt = {
			eventSources: [{
				id		: SOURCE_ID,
				events	: [],
		    }],
		    eventContent 	: fnEventContent,
		    eventDidMount	: fnEventDidMount,
			dayCellContent 	: fnDayCellContent,
			dayCellDidMount	: fnDayCellDidMount,
			eventClassNames	: "fb__calendar__event",
			height			: "auto",
		    dateClick		: fnOnDateClick,
			initialDate		: initialDate ? initialDate : null,
			headerToolbar : {
				start	: "prev",
				center	: "title",
				end		: "next",
			},
			views			: {
				dayGridMonth: {
					titleFormat: function(date) {
						var _year 	= date.date.year;
						var _month 	= date.date.month + 1;
						_month = _month >= 10 ? _month : "0" + _month;
						return _year + "-" + _month;
					}
				}
			}
		}
		return new FbCalendar({ target: calendarEl, options: calendarOpt });
	}

	// 이벤트 렌더링
	function fnEventContent(args) {
		let el 		= "";
		let _event 	= args.event;
		let title 	= _event.title;

		el = document.createElement("div");
		el.className = "fb__event__item";
		el.style.flexDirection = "column";
		el.innerHTML = "<div class='fb__event__title'>" + title + "</div><div class='fb__event__title'>선택일</div>";
		let arrayOfDomNodes = [el]
		return { domNodes: arrayOfDomNodes };
    }

	function fnEventDidMount(arg) {
		let $td = arg.el.closest("td");
		$td.classList.add("isEvent");
		return true;
	}

	// 셀 렌더링
	function fnDayCellContent(args) {
		let gmtDate = args.date;
		let date 	= gmtDate.getDate();

		// 현재 달력이 선택된 날짜
		let selectedDate 	= args.view.getCurrentData().currentDate;
		// 오늘 기준 미래
		let isFuture		= args.isFuture;
		// 오늘 기준 과거
		let isPast 			= args.isPast;
		// 다른 월
		let isOther 		= args.isOther;

		let el		 = document.createElement("div");
		el.className = "fbCalendar__cell";
		el.style.flexDirection = "column";

		let $span = document.createElement("span");
		$span.className = "fbCalendar__date";
		$span.innerHTML = date;
		el.appendChild($span);

		// 공휴일 체크 및 셋팅
		if (fnIsHolyDay(gmtDate)) {
			let $span = document.createElement("span");
			$span.className = "fbCalendar__date";
			$span.innerHTML = "공휴일";
			el.appendChild($span);
			el.classList.add("fbCalendar__cell--dayoff");
			el.dataset.selectable = "disabled";
		// 배송완료일 셋팅
		} else if (fnIsCompletedDate(gmtDate)) {
			let $span = document.createElement("span");
			$span.className = "fbCalendar__date";
			$span.innerHTML = "배송완료";
			el.appendChild($span);
			el.classList.add("fbCalendar__cell--completed");
			el.dataset.selectable = "disabled";
		// 배송가능일 셋팅
		} else if(fnIsDeliverableDate(gmtDate)) {
			el.classList.add("fbCalendar__cell--selectable");
			el.dataset.selectable = "selectable";
		// 그 외 선택 불가
		} else {
			el.classList.add("fbCalendar__cell--disabled");
			el.dataset.selectable = "disabled";
		}

		// 변경일(선택일) 셋팅
		if (newArriveDate && fnIsSameDate(gmtDate, newArriveDate)) {
			let $span = document.createElement("span");
			$span.className = "fbCalendar__date";
			$span.innerHTML = "변경일";
			el.appendChild($span);
			el.classList.add("fbCalendar__cell--newArrival");
		}

		let arrayOfDomNodes = [el]
		return { domNodes: arrayOfDomNodes }
	}

	function fnDayCellDidMount(arg) {
		var $td = arg.el.closest("td");
		$td.classList.remove("isEvent");
		return true;
	}

	// 두 날짜가 같은지 체크
	function fnIsSameDate(date1, date2) {
		return date1.setHours(0,0,0,0) === date2.setHours(0,0,0,0);
	}

	// 휴일 여부 체크
	function fnIsHolyDay(date) {
		return holydayList.includes(date.oFormat("yyyy-MM-dd"));
	}

	// 배송완료 여부 체크
	function fnIsCompletedDate(date) {
		return completedList.includes(date.oFormat("yyyy-MM-dd"));
	}

	// 배송가능 여부 체크
	function fnIsDeliverableDate(date) {
		return deliverableList.includes(date.oFormat("yyyy-MM-dd"));
	}

	// 달력 날짜 클릭 이벤트
	function fnOnDateClick(args) {

		args.jsEvent.stopPropagation();

		let $calendar	= $(this.el);
		let $el 		= $(args.dayEl);
		let $cell 		= $el.find(".fbCalendar__cell")
		let date 		= args.date;
		let newDate		= date.oFormat('yyyy-MM-dd');

		// 변경 가능일자 체크
		if (!fnIsDeliverableDate(date)) {
			fnKendoMessage({message: "해당 날짜는 선택하실 수 없습니다."});
			return false;
		}

		// 변경일 중복클릭 X
		if ($cell.hasClass('fbCalendar__cell--newArrival')) {
			return false;
		}

		// 녹즙인 경우만 수량 변경 가능
		// 녹즙 - 내맘대로 요일 별 품목 모두 0으로 변경하고 변경일자 선택한 경우
		if (goodsInfo.goodsDailyTp == 'GOODS_DAILY_TP.GREENJUICE') {
			let selectBoxValue = 0;
			$updateGoodsCont.find('.arrival__goodsItem').each(function() {
				selectBoxValue += parseInt($(this).find(".arrival__totalSelector").val());
			});

			if (selectBoxValue < 1) {
				fnKendoMessage({message: "상품수량을 확인해주세요."});
				return false;
			}
		}

		// 변경예정 일자 상품데이터 영역 초기화
		$updateGoodsCont.find('.arrival__readOnly').remove();

		let targetDelvDate = scheduleDelvDateList.find(function(item) {
			return item.orgDelvDate === newDate;
		});
		targetDelvDate ? fnRenderUpdateGoodsCont(targetDelvDate.rows, false) : '';

		newArriveDate = date;
		fnRenderNewArriveDate(fnGetFullDate(date));
	    fnSetIsButton(true);
	    this.render();
	}
	//------------------------------- Calendar End --------------------------------------------

	//------------------------------- Html 버튼 바인딩  Start --------------------------------------
	/** 배송요일 변경 */
	$scope.fnDeliveryDayWeek		= function() { fnDeliveryDayWeek(); };
	/** 건너뛰기 */
	$scope.fnDeliveryDayChange		= function() { fnDeliveryDayChange(); };
	/** 변경 버튼(상품스케줄 변경) */
	$scope.fnOpenChangeSchedulePop	= function(e) { fnOpenChangeSchedulePop(e); };
	/** 상품 도착일 변경(저장) */
	$scope.fnUpdateArrivalDate		= function() { fnUpdateArrivalDate(); };
	/** 취소 */
	$scope.fnCancel 				= function() { fnCancel(); };
	//------------------------------- Html 버튼 바인딩  End ----------------------------------------
}); // document ready - END
