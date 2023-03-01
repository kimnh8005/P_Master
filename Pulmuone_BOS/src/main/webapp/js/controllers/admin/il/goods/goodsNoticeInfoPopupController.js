/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 상품일괄수정 상품공지 보기 팝업 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2017.02.13 신혁 최초생성 @
 ******************************************************************************************************************************************************************************************************/
'use strict';
var popupParameter = parent.POP_PARAM["parameter"]; // 팝업 파라미터 전역변수
var ilGoodsId = popupParameter['ilGoodsId']; // 상품코드
var viewModel;
var publicStorageUrl = fnGetPublicStorageUrl();										//이미지 BASE URL

$(document).ready(function() {
    // Initialize Page Call
    fnInitialize();
	//오늘 날짜
	const FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm";

    var todayDate = new Date();
	todayDate.setHours(0,0,0,0);
	var todayDateTime = todayDate.oFormat(FULL_DATE_FORMAT);

	var lastDate = new Date(2999,11,31,23,59,59);
	var lastDateTime = lastDate.oFormat(FULL_DATE_FORMAT);

	var noticeBelowImageUploadMaxLimit = 512000;										//첨부파일 최대 용량
	var allowedImageExtensionList = ['.jpg', '.jpeg', '.gif']; // 업로드 가능한 이미지 확장자 목록

	var cacheData = { };

    // Initialize PageR
    function fnInitialize() {

        $scope.$emit('fnIsMenu', {
            flag : false
        });

        fnPageInfo({
            PG_ID : 'goodsNoticeInfoPopup',
            callback : fnUI
        });
    };

    //전체화면 구성
    function fnUI() {
        fnTranslate(); // comm.lang.js 안에 있는 공통함수 다국어
        fnViewModelInit();
        fnInitCompont();
        fnSearch(); // 조회
    };

    function fnInitCompont() {
    	//상품공지 > 상세 상단공지 노출기간 시작 년도
		fnKendoDatePicker({
			id : 'noticeBelow1StartYear',
			format : 'yyyy-MM-dd',

		});

		//상품공지 > 상세 상단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow1EndYear',
			format : 'yyyy-MM-dd',

		});

    	//상품공지 > 상세 하단공지 노출기간 시작 년도
		fnKendoDatePicker({
			id : 'noticeBelow2StartYear',
			format : 'yyyy-MM-dd',
		});


		//상품공지 > 상세 하단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow2EndYear',
			format : 'yyyy-MM-dd',

		});

    }
    function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	noticeBelow1StartDate				: '',//defaultStartTime,										// 상품 공지 > 프로모션 시작 기간
				noticeBelow1EndDate					: '',//noticeEndTime,										// 상품 공지 > 프로모션 종료 기간
				noticeBelow1StartYear				: todayDate.oFormat("yyyy-MM-dd"),//kendo.toString(kendo.parseDate(defaultStartTime), "yyyy-MM-dd"),	// 상품 공지 > 상세 상단공지 노출기간 설정 시작일
				noticeBelow1StartHour				: '00',//kendo.toString(kendo.parseDate(defaultStartTime), "HH"),// 상품 공지 > 프로모션 상세 상단공지 노출기간 설정 시작시간
				noticeBelow1StartMinute				: '00',//kendo.toString(kendo.parseDate(defaultStartTime), "mm"),// 상품 공지 > 프로모션 상세 상단공지 노출기간 설정 시작분
				noticeBelow1EndYear					: lastDate.oFormat("yyyy-MM-dd"),	// 상품 공지 > 상세 상단공지 노출기간 설정 종료일
				noticeBelow1EndHour					: lastDate.oFormat("HH"),			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 시간
				noticeBelow1EndMinute				: lastDate.oFormat("mm"),			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 분

				noticeBelow2StartDate				: '',//defaultStartTime,										// 상품 공지 > 프로모션 시작 기간
				noticeBelow2EndDate					: '',//noticeEndTime,										// 상품 공지 > 프로모션 종료 기간
				noticeBelow2StartYear				: todayDate.oFormat("yyyy-MM-dd"),//kendo.toString(kendo.parseDate(defaultStartTime), "yyyy-MM-dd"),	// 상품 공지 > 상세 하단공지 노출기간 설정 시작일
				noticeBelow2StartHour				: '00',//kendo.toString(kendo.parseDate(defaultStartTime), "HH"),// 상품 공지 > 프로모션 상세 하단공지 노출기간 설정 시작시간
				noticeBelow2StartMinute				: '00',//kendo.toString(kendo.parseDate(defaultStartTime), "mm"),// 상품 공지 > 프로모션 상세 하단공지 노출기간 설정 시작분

				noticeBelow2EndYear					: lastDate.oFormat("yyyy-MM-dd"),	// 상품 공지 > 상세 상단공지 노출기간 설정 종료일
				noticeBelow2EndHour					: lastDate.oFormat("HH"),			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 시간
				noticeBelow2EndMinute				: lastDate.oFormat("mm"),			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 분
				isDisabled							: true

            },
            showNoticeBelow1Date : false, // 상세 상단공지 노출기간 Visible 여부 : 최초 숨김 처리
			showNoticeBelow2Date : false, // 상세 하단공지 노출기간 Visible 여부 : 최초 숨김 처리

            /* 상품공지 상세 하단 공지 파일관련 Start */
			noticeBelow1ImageList : [],											//상세 상단공지 첨부파일
			noticeBelow1ImageFileList : [],										//상세 상단공지 첨부파일 목록
			noticeBelow1ImageUploadResultList : [],								//상세 상단공지 첨부파일 업로드 결과 Data : 품목 등록시 사용

			noticeBelow2ImageList : [],											//상세 하단공지 첨부파일
			noticeBelow2ImageFileList : [],										//상세 하단공지 첨부파일 목록
			noticeBelow2ImageUploadResultList : [],								//상세 하단공지 첨부파일 업로드 결과 Data : 품목 등록시 사용
			// 시 Dropdown 데이터
			hourDropdownData : function () {

				let hour = [];
				for(var i = 0; i < 24; i++) {
					hour.push({"CODE":('00'+ i).slice(-2),"NAME":('00'+ i).slice(-2)})
				}
				return hour
			},
			// 분 Dropdown 데이터
			MinuteDropdownData : function () {

				let minute = [];
				for(var i = 0; i < 60; i++) {
					minute.push({"CODE":('00'+ i).slice(-2),"NAME":('00'+ i).slice(-2)})
				}
				return minute
			},

        });


        kendo.bind($("#searchForm"), viewModel);
	};


    function fnSearch() {

        var data = $('#searchForm').formSerialize(true);
        data.ilGoodsId = ilGoodsId; // 상품코드

        var url  = '/admin/goods/list/getGoodsNoticeInfoList';
		var cbId = 'goodsAddList';

		fnAjax({
			url	 : url,
			params  : data,
			async	: false,
			success :
				function( data ){
					fnBizCallback(cbId, data);
				},
				isAction : 'batch'
		});
    };

	function fnBizCallback(cbId, data) {

		var dataRows = data.goodsNoticeInfo;

		if(data.goodsNoticeInfo) {
			//상품공지 > 상세 상단공지 시작일
			if( !isNotValidData(dataRows.noticeBelow1StartDate) ) {
				var noticeBelow1StartDate = kendo.parseDate(dataRows.noticeBelow1StartDate);
				viewModel.searchInfo.set('noticeBelow1StartYear',kendo.toString(noticeBelow1StartDate, "yyyy-MM-dd") );
				viewModel.searchInfo.set('noticeBelow1StartHour', kendo.toString(noticeBelow1StartDate, "HH"));
				viewModel.searchInfo.set('noticeBelow1StartMinute', kendo.toString(noticeBelow1StartDate, "mm"));
			}
			//상품공지 > 상세 상단공지 종료일
			if( !isNotValidData(dataRows.noticeBelow1EndDate) ) {
				var noticeBelow1EndDate = kendo.parseDate(dataRows.noticeBelow1EndDate);

				viewModel.searchInfo.set('noticeBelow1EndYear',kendo.toString(noticeBelow1EndDate, "yyyy-MM-dd") );
				viewModel.searchInfo.set('noticeBelow1EndHour', kendo.toString(noticeBelow1EndDate, "HH"));
				viewModel.searchInfo.set('noticeBelow1EndMinute', kendo.toString(noticeBelow1EndDate, "mm"));
			}
			//상품공지 > 상세 하단공지 시작일
			if( !isNotValidData(dataRows.noticeBelow2StartDate) ) {
				var noticeBelow2StartDate = kendo.parseDate(dataRows.noticeBelow2StartDate);

				viewModel.searchInfo.set('noticeBelow2StartYear',kendo.toString(noticeBelow2StartDate, "yyyy-MM-dd") );
				viewModel.searchInfo.set('noticeBelow2StartHour', kendo.toString(noticeBelow2StartDate, "HH"));
				viewModel.searchInfo.set('noticeBelow2StartMinute', kendo.toString(noticeBelow2StartDate, "mm"));
			}
			//상품공지 > 상세 하단공지 종료일
			if( !isNotValidData(dataRows.noticeBelow2EndDate) ) {
				var noticeBelow2EndDate = kendo.parseDate(dataRows.noticeBelow2EndDate);

				viewModel.searchInfo.set('noticeBelow2EndYear',kendo.toString(noticeBelow2EndDate, "yyyy-MM-dd") );
				viewModel.searchInfo.set('noticeBelow2EndHour', kendo.toString(noticeBelow2EndDate, "HH"));
				viewModel.searchInfo.set('noticeBelow2EndMinute', kendo.toString(noticeBelow2EndDate, "mm"));
			}

			//상품 공지 > 상세 상단공지 이미지
			if(dataRows.noticeBelow1ImageUrl) {
				viewModel.get('noticeBelow1ImageList').push({
					imageName : dataRows.noticeBelow1ImageUrl, // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
					imageOriginalName : dataRows.noticeBelow1ImageUrl, // 원본 File 명
					sort : '', // 정렬순서
					imageSrc : publicStorageUrl+dataRows.noticeBelow1ImageUrl // 상품 이미지 url
				});
			}

			if(dataRows.noticeBelow2ImageUrl) {
				//상품 공지 > 상세 하단공지 이미지
				viewModel.get('noticeBelow2ImageList').push({
					imageNameSecond : dataRows.noticeBelow2ImageUrl, // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
					imageOriginalName : dataRows.noticeBelow2ImageUrl, // 원본 File 명
					sort : '', // 정렬순서
					imageSrcSecond : publicStorageUrl+dataRows.noticeBelow2ImageUrl, // 상품 이미지 url
				});
			}

		}



	};
	//데이터 비었는지 체크하는 함수
	function isNotValidData(data) {
		if( data === undefined || typeof data === "undefined" || data === null || data === "" ) return true;
		else return false;
	};


    // ------------------------------- Html 버튼 바인딩 Start
    // -------------------------------
    /** Common Search */
    $scope.fnSearch = function() {
        fnSearch();
    };

    $scope.fnClose = function() {
        fnClose();
    };

    // ------------------------------- Html 버튼 바인딩 End
    // -------------------------------

}); // document ready - END
