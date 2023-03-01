/*******************************************************************************************************************************************************************************************************
 * -------------------------------------------------------- description : 상품일괄수정 * @ @ 수정일 수정자 수정내용 @ ------------------------------------------------------ * @ 2020.11.18 정형진 최초생성 @
******************************************************************************************************************************************************************************************************/
'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체

var PAGE_SIZE = 20;
var itemGridOpt, itemGrid, itemGridDs;
var viewModel;

/* 카테고리 관련 변수 */
var ctgryGrid1, ctgryGrid2, ctgryGrid3, ctgryGrid4;
var ctgryGridDs1, ctgryGridDs2, ctgryGridDs3, ctgryGridDs4;
var ctgryGridOpt, ctgryGrid, ctgryGridDs;

var noticeBelow1ImageDelete = false;														//상세 상단공지 이미지 X(삭제)버튼
var noticeBelow2ImageDelete = false;														//상세 하단공지 이미지 X(삭제)버튼


$(document).ready(function() {

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

	// Initialize Page Call
	fnInitialize();

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'goodsAllModify',
			callback : fnUI
		});
	};

	//전체화면 구성
	function fnUI() {

		fnTranslate();			// comm.lang.js 안에 있는 공통함수 다국어
		fnInitButton();			// Initialize Button

		fnInitCompont();		// 상품정보 일괄수정 컴포넌트 Initialize

		fnViewModelInit();

		fnInitCategoryGrid();

		fnInitCtgry(); 			// 전시 카테고리 셋팅

		fnItemGrid();			// 상품정보 일괄 수정 Grid

		setCacheData();

	    //체크 박스 공통 이벤트
        fbCheckboxChange();
	}

	function fnInitButton() {
		$('#fnSearch , #fnClear , #EXCEL_DN, #fnCtgrySelect, #fnSave').kendoButton();
	};

	function fnInitCompont() {

		fnKendoDropDownList({
	        id: "goodsSelectType",
	        data: [
	            { CODE: "promotionAdd", NAME: "프로모션 상품명" },
	            { CODE: "displayCategoryAdd", NAME: "전시 카테고리(추가등록)" },
	            { CODE: "purchaseAdd", NAME: "판매허용범위/쿠폰사용" },
	            { CODE: "goodsNoticeAdd", NAME: "상품공지" },
	            { CODE: "goodsAdd", NAME: "추가상품" },
	        ],
	        valueField: "CODE",
	        textField: "NAME",
	        blank: "수정정보 선택"
	    });

		fnKendoDatePicker({
			id : 'promotionNameStartYear',
			format : 'yyyy-MM-dd',
			defVal : todayDate,
			max: lastDate,
			change: function(e){

				viewModel.fnDateChange(e, "promotionNameStartYear");
				// fnStartCalChange('promotionNameStartYear', 'promotionNameEndYear');
			}
		});

		$("#promotionNameStartYear").data("kendoDatePicker").unbind("blur");

		//기본정보 > 프로모션 상품명 종료 년도
		fnKendoDatePicker({
			id : 'promotionNameEndYear',
			format : 'yyyy-MM-dd',
			min: todayDate,
			max: lastDate,
			change: function(e){


				viewModel.fnDateChange(e, "promotionNameEndYear");
				// fnEndCalChange('promotionNameStartYear', 'promotionNameEndYear');
			}
		});


		//상품공지 > 상세 상단공지 > 첨부파일 저장
		$('#uploadNoticeBelow1ImageForm').html('');
		var acceptStr = "";
		for(var i = 0 ; i < allowedImageExtensionList.length; i ++) {
			acceptStr += allowedImageExtensionList[i];
			if(i < allowedImageExtensionList.length-1) {
				acceptStr += ",";
			}
		}

		var htmlText = '<input type="file" id="uploadNoticeBelow1Image" name="uploadNoticeBelow1Image" accept="'+acceptStr+'">';

		$('#uploadNoticeBelow1ImageForm').append(htmlText);

		fnKendoUpload({
			id : "uploadNoticeBelow1Image",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (noticeBelowImageUploadMaxLimit < e.files[0].size) { // 상세 상단공지 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + parseInt(noticeBelowImageUploadMaxLimit / 1024) + ' kb 입니다.',
							ok : function(e) {}
						});
						return;
					}

					// PJH Start
                    var imageExtension = e.files[0]['extension'].toLowerCase();

                    // 업로드 가능한 이미지 확장자 목록에 포함되어 있는지 확인
                    if( allowedImageExtensionList.indexOf(imageExtension) < 0 ) {
                        fnKendoMessage({
                            message : '업로드 가능한 이미지 확장자가 아닙니다.',
                            ok : function(e) {}
                        });
                        return;
                    }
                    // PJH End

					var noticeBelow1ImageFileList = viewModel.get('noticeBelow1ImageFileList');

					for (var i = noticeBelow1ImageFileList.length - 1; i >= 0; i--) {
						if (noticeBelow1ImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
							fnKendoMessage({
								message : '이미지 파일명이 중복됩니다.',
								ok : function(e) {}
							});
							return;
						}
					}

					let reader = new FileReader();

					reader.onload = function(ele) {
						var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
						var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

						//기존 파일 삭제 처리
						viewModel.get("noticeBelow1ImageList").splice(0, 1);
						viewModel.get('noticeBelow1ImageFileList').splice(0, 1);

						viewModel.get('noticeBelow1ImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('noticeBelow1ImageList').push({
							imageName : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrc : itemImageScr, // 상품 이미지 url
						});

						//이미지가 저장되면 노출기간설정을 필수값으로 변경
						/*HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						$("#noticeBelow1StartDate").attr("required","required");
						$("#noticeBelow1EndDate").attr("required","required");
						*/
						$(".noticeBelow1StartDate").attr("required","required");
						$(".noticeBelow1EndDate").attr("required","required");


						// PJH Start
						viewModel.set('showNoticeBelow1Date', true); // // 상세 상단공지 노출기간 Visible 처리
						// PJH End

						//HGRM-1646 초기화 및 시간달력포맷 공통화 수정
						// fnStartCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
						// fnEndCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');

						viewModel.searchInfo.set('noticeBelow1StartYear', '');
						viewModel.searchInfo.set('noticeBelow1StartHour', '');
						viewModel.searchInfo.set('noticeBelow1StartMinute', '');
						viewModel.searchInfo.set('noticeBelow1EndYear', '');
						viewModel.searchInfo.set('noticeBelow1EndHour', '');
						viewModel.searchInfo.set('noticeBelow1EndMinute', '');
					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});

		//상품공지 > 상세 상단공지 노출기간 시작 년도
		fnKendoDatePicker({
			id : 'noticeBelow1StartYear',
			format : 'yyyy-MM-dd',
			defVal : todayDate,
			//max: viewModel.searchInfo.get("noticeBelow1EndYear"),
			change: function(e){
				// fnStartCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
				viewModel.fnDateChange(e, "noticeBelow1StartYear");
			}
		});

		//상품공지 > 상세 상단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow1EndYear',
			format : 'yyyy-MM-dd',
		//	min: viewModel.searchInfo.get("noticeBelow1StartYear"),
			max: lastDate,
			change: function(e){
				// fnEndCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
				viewModel.fnDateChange(e, "noticeBelow1EndYear");
			}
		});

		//상품공지 > 상세 하단공지 노출기간 시작 년도
		fnKendoDatePicker({
			id : 'noticeBelow2StartYear',
			format : 'yyyy-MM-dd',
			defVal : todayDate,
			//max: viewModel.searchInfo.get("noticeBelow2EndYear"),
			change: function(e){
				// fnStartCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
				viewModel.fnDateChange(e, "noticeBelow2StartYear");
			}
		});

		//상품공지 > 상세 하단공지 노출기간 종료 년도
		fnKendoDatePicker({
			id : 'noticeBelow2EndYear',
			format : 'yyyy-MM-dd',
		//	min: viewModel.searchInfo.get("noticeBelow2StartYear"),
			max: lastDate,
			change: function(e){
				// fnEndCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
				viewModel.fnDateChange(e, "noticeBelow2EndYear");
			}
		});


		//상품공지 > 상세 하단공지 > 첨부파일 저장
		$('#uploadNoticeBelow2ImageForm').html('');
		var htmlText = '<input type="file" id="uploadNoticeBelow2Image" name="uploadNoticeBelow2Image" accept="'+acceptStr+'">';

		$('#uploadNoticeBelow2ImageForm').append(htmlText);

		fnKendoUpload({ // 상품 이미지 첨부 File Tag 를 kendoUpload 로 초기화
			id : "uploadNoticeBelow2Image",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (noticeBelowImageUploadMaxLimit < e.files[0].size) { // 상품 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + parseInt(noticeBelowImageUploadMaxLimit / 1024) + ' kb 입니다.',
							ok : function(e) {}
						});
						return;
					}

	                // PJH Start
                    var imageExtension = e.files[0]['extension'].toLowerCase();

                    // 업로드 가능한 이미지 확장자 목록에 포함되어 있는지 확인
                    if( allowedImageExtensionList.indexOf(imageExtension) < 0 ) {
                        fnKendoMessage({
                            message : '업로드 가능한 이미지 확장자가 아닙니다.',
                            ok : function(e) {}
                        });
                        return;
                    }
                    // PJH End

					var noticeBelow2ImageFileList = viewModel.get('noticeBelow2ImageFileList');

					for (var i = noticeBelow2ImageFileList.length - 1; i >= 0; i--) {
						if (noticeBelow2ImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
							fnKendoMessage({
								message : '이미지 파일명이 중복됩니다.',
								ok : function(e) {}
							});
							return;
						}
					}

					let reader = new FileReader();

					reader.onload = function(ele) {
						var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
						var file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

						//기존 파일 삭제 처리
						viewModel.get("noticeBelow2ImageList").splice(0, 1);
						viewModel.get('noticeBelow2ImageFileList').splice(0, 1);

						viewModel.get('noticeBelow2ImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('noticeBelow2ImageList').push({
							imageNameSecond : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrcSecond : itemImageScr, // 상품 이미지 url
						});

						//이미지가 저장되면 노출기간설정을 필수값으로 변경
						/*
						$("#noticeBelow2StartDate").attr("required","required");
						$("#noticeBelow2EndDate").attr("required","required");
						 */
						$(".noticeBelow2StartDate").attr("required","required");
						$(".noticeBelow2EndDate").attr("required","required");

                        // PJH Start
                        viewModel.set('showNoticeBelow2Date', true); // // 상세 하단공지 노출기간 Visible 처리
                        // PJH End

                        //HGRM-1646 초기화 및 시간달력포맷 공통화 수정
					    // fnStartCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
						// fnEndCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');

						viewModel.searchInfo.set('noticeBelow2StartYear', '');
						viewModel.searchInfo.set('noticeBelow2StartHour', '');
						viewModel.searchInfo.set('noticeBelow2StartMinute', '');
						viewModel.searchInfo.set('noticeBelow2EndYear', '');
						viewModel.searchInfo.set('noticeBelow2EndHour', '');
						viewModel.searchInfo.set('noticeBelow2EndMinute', '');
					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});

		fnTagMkChkBox({
			id : 'purchaseTargetType',
			url : "/admin/comn/getCodeList",
			tagId : 'purchaseTargetType',
			async : false,
			style : {},
			params : {"stCommonCodeMasterCode" : "GOODS_DISPLAY_TYPE", "useYn" :"Y"},
			beforeData : [{ "CODE" : "ALL", "NAME" : "전체" }],
			change : function(e) {

			}
		});

		$("input[name=purchaseTargetType]").attr("data-bind", "checked: searchInfo.purchaseTargetType, events: {change: fnCheckboxChange}");
//		$("input[name=purchaseTargetType]").each(function() {
//			$(this).attr("data-bind", "checked: searchInfo.purchaseTargetType");
//		});

		fnTagMkRadio({
			id : 'couponUseYn',
			data : [ {
				"CODE" : "Y",
				"NAME" : "허용"
			}, {
				"CODE" : "N",
				"NAME" : "허용 안함"
			} ],
			tagId : 'couponUseYn'
		});

		$("input[name=couponUseYn]").each(function() {
			$(this).attr("data-bind", "checked: searchInfo.couponUseYn");
		});


		fnKendoDropDownList({
       		id: "goodsAddType",
            data: [
            	{
            		CODE: "add",
            		NAME: "일괄포함",
            	},
            	{
            		CODE: "del",
            		NAME: "일괄삭제",
            	}
            ],
            valueField: "CODE",
            textField: "NAME",
            value: 'add',
        });

	};

	function fnViewModelInit() {
		viewModel = new kendo.data.ObservableObject({
            searchInfo : { // 조회조건
            	goodsSelectType : "", 			// 상품일괄수정 선택박스
            	searchGoodsId : "",
            	promotionNm : "", 			// 프로모션 상품명

				promotionNameStartYear				: kendo.toString(todayDate, "yyyy-MM-dd"),//todayDate.oFormat("yyyy-MM-dd"),				// 기본정보 > 프로모션 시작일
				promotionNameStartHour				: '00',//todayDate.oFormat("HH"),						// 기본정보 > 프로모션 시작 시간
				promotionNameStartMinute			: '00',//todayDate.oFormat("mm"),						// 기본정보 > 프로모션 시작 분
				promotionNameEndYear				: '',//lastDate.oFormat("yyyy-MM-dd"),				// 기본정보 > 프로모션 종료일
				promotionNameEndHour				: '00',//lastDate.oFormat("HH"),						// 기본정보 > 프로모션 종료 시간
				promotionNameEndMinute				: '00',//lastDate.oFormat("mm"),						// 기본정보 > 프로모션 종료 분

            	noticeBelow1StartDate				: '',//defaultStartTime,										// 상품 공지 > 프로모션 시작 기간
				noticeBelow1EndDate					: '',//noticeEndTime,										// 상품 공지 > 프로모션 종료 기간
				noticeBelow1StartYear				: '',//kendo.toString(kendo.parseDate(defaultStartTime), "yyyy-MM-dd"),	// 상품 공지 > 상세 상단공지 노출기간 설정 시작일
				noticeBelow1StartHour				: '',//kendo.toString(kendo.parseDate(defaultStartTime), "HH"),// 상품 공지 > 프로모션 상세 상단공지 노출기간 설정 시작시간
				noticeBelow1StartMinute				: '',//kendo.toString(kendo.parseDate(defaultStartTime), "mm"),// 상품 공지 > 프로모션 상세 상단공지 노출기간 설정 시작분
				noticeBelow1EndYear					: '',	// 상품 공지 > 상세 상단공지 노출기간 설정 종료일
				noticeBelow1EndHour					: '',			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 시간
				noticeBelow1EndMinute				: '',			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 분

				noticeBelow2StartDate				: '',//defaultStartTime,										// 상품 공지 > 프로모션 시작 기간
				noticeBelow2EndDate					: '',//noticeEndTime,										// 상품 공지 > 프로모션 종료 기간
				noticeBelow2StartYear				: '',//kendo.toString(kendo.parseDate(defaultStartTime), "yyyy-MM-dd"),	// 상품 공지 > 상세 하단공지 노출기간 설정 시작일
				noticeBelow2StartHour				: '',//kendo.toString(kendo.parseDate(defaultStartTime), "HH"),// 상품 공지 > 프로모션 상세 하단공지 노출기간 설정 시작시간
				noticeBelow2StartMinute				: '',//kendo.toString(kendo.parseDate(defaultStartTime), "mm"),// 상품 공지 > 프로모션 상세 하단공지 노출기간 설정 시작분

				noticeBelow2EndYear					: '',	// 상품 공지 > 상세 상단공지 노출기간 설정 종료일
				noticeBelow2EndHour					: '',			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 시간
				noticeBelow2EndMinute				: '',			// 상품 공지 > 상세 상단공지 노출기간 설정 종료 분

				purchaseTargetType					: [],
				couponUseYn							: "",

				goodsAddType						: "add",
            },
            promotionVisiable : false, 			// 프로모션 상품 수정 화면영역
            displayCategoryVisiable : false, 	// 전시카테고리 수정 화면영역
            purchaseVisiable		: false,	// 구매허용범위/ 수정 화면영역
            goodsNoticeVisiable		: false, 	// 상품공지 수정 화면영역
            goodsAddVisiable		: false, 	// 추가상품 화면영역
            showNoticeBelow1Date : false, // 상세 상단공지 노출기간 Visible 여부 : 최초 숨김 처리
			showNoticeBelow2Date : false, // 상세 하단공지 노출기간 Visible 여부 : 최초 숨김 처리


            /* 상품공지 상세 하단 공지 파일관련 Start */
			noticeBelow1ImageList : [],											//상세 상단공지 첨부파일
			noticeBelow1ImageFileList : [],										//상세 상단공지 첨부파일 목록
			noticeBelow1ImageUploadResultList : [],								//상세 상단공지 첨부파일 업로드 결과 Data : 품목 등록시 사용

			noticeBelow2ImageList : [],											//상세 하단공지 첨부파일
			noticeBelow2ImageFileList : [],										//상세 하단공지 첨부파일 목록
			noticeBelow2ImageUploadResultList : [],								//상세 하단공지 첨부파일 업로드 결과 Data : 품목 등록시 사용

			isGoodsAdditionalGoodsMappingNoDataTbody	: true,
			isGoodsAdditionalGoodsMappingTbody		: false,							//추가상품 > Data Tbody Visible
			goodsAdditionalGoodsMappingList			: [],								//추가상품 > 추가 상품 목록, goodsAdditionalGoodsMapping-row-template 사용
			fnCheckboxChange : function(e){ // 체크박스 변경
                e.preventDefault();

                if( e.target.value == "ALL" ){
                    if( $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){

                        $("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
                            if( viewModel.searchInfo.get(e.target.name).indexOf($(this).val()) < 0 ){
                                viewModel.searchInfo.get(e.target.name).push($(this).val());
                            }
                        });
                    }else{

                        $("input[name=" + e.target.name + "]:gt(0)").each(function(idx){
                            viewModel.searchInfo.get(e.target.name).remove($(this).val());
                        });
                    }
                }else{

                    if( !$("#" + e.target.id).is(":checked") && $("input[name=" + e.target.name + "]:eq(0)").is(":checked") ){
                        viewModel.searchInfo.get(e.target.name).remove($("input[name=" + e.target.name + "]:eq(0)").val());
                    }
                    else if( $("#" + e.target.id).is(":checked")
                                && ($("input[name=" + e.target.name + "]").length - 1) == viewModel.searchInfo.get(e.target.name).length )
                    {
                        viewModel.searchInfo.get(e.target.name).push($("input[name=" + e.target.name + "]:eq(0)").val());
                    }
                }
            },
            fnSearch : function(e){ // 조회
                e.preventDefault();

                if($("input[name=selectConditionType]:checked").val() == "multiSection") {
                	viewModel.searchInfo.set("findKeyword", "");
        		}

                let data = $("#searchForm").formSerialize(true);

                if( data.findKeyword == "" ){
                    if( data.dateSearchStart == "" && data.dateSearchEnd != "" ){
                        fnKendoMessage({ message : "시작일을 선택해주세요." });
                        return;
                    }

                    if( data.dateSearchStart != "" && data.dateSearchEnd == "" ){
                        fnKendoMessage({ message : "종료일을 선택해주세요." });
                        return;
                    }
                }

                const _pageSize = goodsGrid && goodsGrid.dataSource ? goodsGrid.dataSource.pageSize() : PAGE_SIZE;


                let searchData = fnSearchData(data);
                let query = { page : 1,
                              pageSize : _pageSize,
                              filterLength : searchData.length,
                              filter : { filters : searchData }
                };

                goodsGridDs.query(query);
            },
            fnClear : function(e){ // 초기화

                e.preventDefault();
                fnDefaultSet();

                if(viewModel.searchInfo.get("goodsSelectType") == "goodsAdd") {
                	$("#itemGrid").data("kendoGrid").dataSource.data( [] );
                }

                $('#selectCtgoryText').text("");
                $("#goodsDisplayCategory2").data("kendoGrid").dataSource.data([]);
                $("#goodsDisplayCategory3").data("kendoGrid").dataSource.data([]);
        		$("#goodsDisplayCategory4").data("kendoGrid").dataSource.data([]);
                $("#goodsDisplayCategoryGrid").data("kendoGrid").dataSource.data( [] );
            },
            onGoodsSelectTypeChange : function(e) {


            	if(viewModel.searchInfo.get("goodsSelectType") == "promotionAdd") {
            		this.set( 'promotionVisiable', true );
            		this.set( 'displayCategoryVisiable', false );
            		this.set( 'purchaseVisiable', false );
            		this.set( 'goodsNoticeVisiable', false );
            		this.set( 'goodsAddVisiable', false );

            	}else if(viewModel.searchInfo.get("goodsSelectType") == "displayCategoryAdd") {
            		this.set( 'promotionVisiable', false );
            		this.set( 'displayCategoryVisiable', true );
            		this.set( 'purchaseVisiable', false );
            		this.set( 'goodsNoticeVisiable', false );
            		this.set( 'goodsAddVisiable', false );
            	}else if(viewModel.searchInfo.get("goodsSelectType") == "purchaseAdd") {
            		this.set( 'promotionVisiable', false );
            		this.set( 'displayCategoryVisiable', false );
            		this.set( 'purchaseVisiable', true );
            		this.set( 'goodsNoticeVisiable', false );
            		this.set( 'goodsAddVisiable', false );
            	}else if(viewModel.searchInfo.get("goodsSelectType") == "goodsNoticeAdd") {
            		this.set( 'promotionVisiable', false );
            		this.set( 'displayCategoryVisiable', false );
            		this.set( 'purchaseVisiable', false );
            		this.set( 'goodsNoticeVisiable', true );
            		this.set( 'goodsAddVisiable', false );
            	}else if(viewModel.searchInfo.get("goodsSelectType") == "goodsAdd") {
                    $("#itemGrid").data("kendoGrid").dataSource.data( [] );

            		this.set( 'promotionVisiable', false );
            		this.set( 'displayCategoryVisiable', false );
            		this.set( 'purchaseVisiable', false );
            		this.set( 'goodsNoticeVisiable', false );
            		this.set( 'goodsAddVisiable', true );
            	}else {
            		this.set( 'promotionVisiable', false );
            		this.set( 'displayCategoryVisiable', false );
            		this.set( 'purchaseVisiable', false );
            		this.set( 'goodsNoticeVisiable', false );
            		this.set( 'goodsAddVisiable', false );
            	}

            },
			//상세 상단공지 첨부파일 thumbnail 내 "X" 클릭 이벤트 : 이미지 삭제
			fnRemoveNoticeBelow1Image : function(e) {

				for (var i = viewModel.get("noticeBelow1ImageList").length - 1; i >= 0; i--) {
					if (viewModel.get("noticeBelow1ImageList")[i]['imageName'] == e.data["imageName"]) {
						viewModel.get("noticeBelow1ImageList").splice(i, 1); // viewModel 에서 삭제
					}
				}

				var noticeBelow1ImageFileList = viewModel.get('noticeBelow1ImageFileList');

				for (var i = noticeBelow1ImageFileList.length - 1; i >= 0; i--) {
					if (noticeBelow1ImageFileList[i]['name'] == e.data['imageName']) {
						noticeBelow1ImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
					}
				}

				var noticeBelow1ImageUploadResultList = viewModel.get('noticeBelow1ImageUploadResultList');

				for (var i = noticeBelow1ImageUploadResultList.length - 1; i >= 0; i--) {
					noticeBelow1ImageUploadResultList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
				}

				//이미지가 삭제되면 노출기간 설정 필수조건 삭제
				/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
				$("#noticeBelow1StartDate").removeAttr("required");
				$("#noticeBelow1EndDate").removeAttr("required");
				*/
				$(".noticeBelow1StartDate").removeAttr("required");
				$(".noticeBelow1EndDate").removeAttr("required");

				// PJH Start
				viewModel.set('showNoticeBelow1Date', false);  // 첨부파일 삭제시 상세 상단공지 노출기간 숨김 처리
				// PJH End

				noticeBelow1ImageDelete = true;
				viewModel.searchInfo.set('noticeBelow1StartYear', '');
				viewModel.searchInfo.set('noticeBelow1StartHour', '');
				viewModel.searchInfo.set('noticeBelow1StartMinute', '');
				viewModel.searchInfo.set('noticeBelow1EndYear', '');
				viewModel.searchInfo.set('noticeBelow1EndHour', '');
				viewModel.searchInfo.set('noticeBelow1EndMinute', '');
			},
			//상세 하단공지 첨부파일 thumbnail 내 "X" 클릭 이벤트 : 이미지 삭제
			fnRemoveNoticeBelow2Image : function(e) {

				for (var i = viewModel.get("noticeBelow2ImageList").length - 1; i >= 0; i--) {
					if (viewModel.get("noticeBelow2ImageList")[i]['imageNameSecond'] == e.data["imageNameSecond"]) {
						viewModel.get("noticeBelow2ImageList").splice(i, 1); // viewModel 에서 삭제
					}
				}

				var noticeBelow2ImageFileList = viewModel.get('noticeBelow2ImageFileList');

				for (var i = noticeBelow2ImageFileList.length - 1; i >= 0; i--) {
					if (noticeBelow2ImageFileList[i]['name'] == e.data['imageNameSecond']) {
						noticeBelow2ImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
					}
				}

				var noticeBelow2ImageUploadResultList = viewModel.get('noticeBelow2ImageUploadResultList');

				for (var i = noticeBelow2ImageUploadResultList.length - 1; i >= 0; i--) {
					noticeBelow2ImageUploadResultList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
				}

				//이미지가 삭제되면 노출기간 설정 필수조건 삭제
				/* HGRM-1646 초기화 및 시간달력포맷 공통화 수정
				$("#noticeBelow2StartDate").removeAttr("required");
				$("#noticeBelow2EndDate").removeAttr("required");
				*/
				$(".noticeBelow2StartDate").removeAttr("required");
				$(".noticeBelow2EndDate").removeAttr("required");

	             // PJH Start
                viewModel.set('showNoticeBelow2Date', false);  // 첨부파일 삭제시 상세 하단공지 노출기간 숨김 처리
                // PJH End

				noticeBelow2ImageDelete = true;
                viewModel.searchInfo.set('noticeBelow2StartYear', '');
				viewModel.searchInfo.set('noticeBelow2StartHour', '');
				viewModel.searchInfo.set('noticeBelow2StartMinute', '');
				viewModel.searchInfo.set('noticeBelow2EndYear', '');
				viewModel.searchInfo.set('noticeBelow2EndHour', '');
				viewModel.searchInfo.set('noticeBelow2EndMinute', '');
			},
			// 상품 공지 > 상세 상단공지 시작일/종료일 초기화
			fnNoticeBelow1DateReset : function() {
				//수정 데이터로 초기화 일 경우

				const currentStartDate = kendo.parseDate(viewModel.searchInfo.get("noticeBelow1StartDate"));
				const currentEndDate = kendo.parseDate(viewModel.searchInfo.get("noticeBelow1EndDate"));

				if( currentStartDate ) {
					viewModel.searchInfo.set('noticeBelow1StartYear',kendo.toString(currentStartDate, "yyyy-MM-dd") );
					viewModel.searchInfo.set('noticeBelow1StartHour', kendo.toString(currentStartDate, "HH"));
					viewModel.searchInfo.set('noticeBelow1StartMinute', kendo.toString(currentStartDate, "mm"));
				} else {
					viewModel.searchInfo.set('noticeBelow1StartYear', "");
					viewModel.searchInfo.set('noticeBelow1StartHour', "00");
					viewModel.searchInfo.set('noticeBelow1StartMinute', "00");
				}

				if( currentEndDate ) {
					viewModel.searchInfo.set('noticeBelow1EndYear', kendo.toString(currentEndDate, "yyyy-MM-dd") );
					viewModel.searchInfo.set('noticeBelow1EndHour', kendo.toString(currentEndDate, "HH"));
					viewModel.searchInfo.set('noticeBelow1EndMinute', kendo.toString(currentEndDate, "mm"));
				} else {
					viewModel.searchInfo.set('noticeBelow1EndYear', "");
					viewModel.searchInfo.set('noticeBelow1EndHour', "00");
					viewModel.searchInfo.set('noticeBelow1EndMinute', "00");
				}


				// fnStartCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
				// fnEndCalChange('noticeBelow1StartYear', 'noticeBelow1EndYear');
			},
            // 상품 공지 > 상세 하단공지 시작일/종료일 초기화
			fnNoticeBelow2DateReset : function() {

				const currentStartDate = kendo.parseDate(viewModel.searchInfo.get("noticeBelow2StartDate"));
				const currentEndDate = kendo.parseDate(viewModel.searchInfo.get("noticeBelow2EndDate"));

				if( currentStartDate ) {
					viewModel.searchInfo.set('noticeBelow2StartYear',kendo.toString(currentStartDate, "yyyy-MM-dd") );
					viewModel.searchInfo.set('noticeBelow2StartHour', kendo.toString(currentStartDate, "HH"));
					viewModel.searchInfo.set('noticeBelow2StartMinute', kendo.toString(currentStartDate, "mm"));
				} else {
					viewModel.searchInfo.set('noticeBelow2StartYear', "");
					viewModel.searchInfo.set('noticeBelow2StartHour', "00");
					viewModel.searchInfo.set('noticeBelow2StartMinute', "00");
				}

				if( currentEndDate ) {
					viewModel.searchInfo.set('noticeBelow2EndYear', kendo.toString(currentEndDate, "yyyy-MM-dd") );
					viewModel.searchInfo.set('noticeBelow2EndHour', kendo.toString(currentEndDate, "HH"));
					viewModel.searchInfo.set('noticeBelow2EndMinute', kendo.toString(currentEndDate, "mm"));
				} else {
					viewModel.searchInfo.set('noticeBelow2EndYear', "");
					viewModel.searchInfo.set('noticeBelow2EndHour', "00");
					viewModel.searchInfo.set('noticeBelow2EndMinute', "00");
				}

				//수정 데이터로 초기화 일 경우
				// fnStartCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
				// fnEndCalChange('noticeBelow2StartYear', 'noticeBelow2EndYear');
			},
			fnPromotionDateReset : function() {
				//수정 데이터로 초기화 일 경우
				viewModel.searchInfo.set('promotionNameStartYear', todayDate);
				viewModel.searchInfo.set('promotionNameStartHour', '00');
				viewModel.searchInfo.set('promotionNameStartMinute', '00');
				viewModel.searchInfo.set('promotionNameEndYear', '');
				viewModel.searchInfo.set('promotionNameEndHour', '00');
				viewModel.searchInfo.set('promotionNameEndMinute', '00');
			},
			//날짜  받기
			fnGetDateFull : function(type) {
				let dateYear = viewModel.searchInfo.get(type+"Year");
				let dateHour = viewModel.searchInfo.get(type+"Hour");
				let dateMinute = viewModel.searchInfo.get(type+"Minute");

				var returnDate = null;

				if(dateYear != "" && dateYear != null){
					returnDate = dateYear + " " + dateHour + ":" + dateMinute;
				}
				return returnDate;
			},
			fnDateChange : function(e, id){


				e.preventDefault();

				const _sender = e.sender;

				const formatedDate = fnFormatDate(_sender.element.val(), _sender.options.format);

				if( !isNotValidData(formatedDate) ) {
					//_old값 직접 할당
					_sender.value(formatedDate);
				} else {
					//포맷이 이상하므로 에러 발생
					const _cacheDate = cacheData[id] ? cacheData[id] : kendo.toString(todayDate, "yyyy-MM-dd");
					_sender.value(cacheData[id]);
					fnKendoMessage({message : "올바른 날짜를 입력해주세요.", ok : function() {
						viewModel.searchInfo[id] = cacheData[id];
						$("#" + id).focus();
					}});
					return;
				}

				//이상한 포맷일경우 _old가 없다.
				if( e.sender._old == null) {
					//여기 타면 안됨
					fnKendoMessage({ message : "기간을 정확히 입력해주세요." , ok : function(){
						viewModel.searchInfo[id] = cacheData[id];
						$("#" + id).focus();

						return;
					}});
					// if((e.sender.element.context.id).indexOf('promotionName') > -1) {
					// 	viewModel.fnPromotionDateReset();
					// } else if((e.sender.element.context.id).indexOf('sale') > -1) {
					// 	viewModel.fnSaleDateReset();
					// } else if((e.sender.element.context.id).indexOf('noticeBelow1') > -1) {
					// 	viewModel.fnNoticeBelow1DateReset();
					// } else if((e.sender.element.context.id).indexOf('noticeBelow2') > -1) {
					// 	viewModel.fnNoticeBelow2DateReset();
					// }
					return;
				}


				//변경된 데이터 set
				// viewModel.searchInfo.set(id, kendo.toString(kendo.parseDate(viewModel.searchInfo.get(id)), "yyyy-MM-dd"));
				viewModel.searchInfo.set(id, kendo.toString(kendo.parseDate(formatedDate), "yyyy-MM-dd"));

				//날짜 시작일 종료일 유효성 체크
				let type = "";
				if((id).indexOf('promotionName') > -1) {
					type = 'promotionName';
				} else if((id).indexOf('sale') > -1) {
					type = 'sale';
				} else if((id).indexOf('noticeBelow1') > -1) {
					type = 'noticeBelow1';
				} else if((id).indexOf('noticeBelow2') > -1) {
					type = 'noticeBelow2';
				}


				let _isStartEnd = "";

				if( !!id.match(/end/i) ) {
					_isStartEnd = "end";
				} else if ( !!id.match(/start/i) ) {
					_isStartEnd = "start";
				} else {
					return;
				}

				if( isNotValidData(_isStartEnd)  ) return;

				const isSuccess = viewModel.fnCheckDateValidation(type, _isStartEnd, "year", id);

				//검증 실패 시 기존 데이터 복원
				if( !isSuccess ) {
					viewModel.searchInfo.set(id, cacheData[id]);
				} else {
					//검증 성공 시 기존 데이터 새로운 값으로 세팅
					cacheData[id] = viewModel.searchInfo.get(id);
				}

				return;
			},
			// 날짜 시작일 종료일 유효성 체크
			fnCheckDateValidation : function(type, isStartEnd, mode, id) {
				//시작일 종료일 체크
				let startDateObj = "";
				let endDateObj = "";

				const _value = viewModel.searchInfo.get(id);
				if( isNotValidData(_value) ) return;

				let _startYear = viewModel.searchInfo.get(type + "StartYear");
				let _startHour = viewModel.searchInfo.get(type + "StartHour");
				let _startMinute = viewModel.searchInfo.get(type + "StartMinute");

				let _endYear = viewModel.searchInfo.get(type + "EndYear");
				let _endHour = viewModel.searchInfo.get(type + "EndHour");
				let _endMinute = viewModel.searchInfo.get(type + "EndMinute");


				startDateObj = new Date(_startYear + " " + _startHour + ":" + _startMinute);
				endDateObj = new Date(_endYear + " " + _endHour + ":" + _endMinute);

				//시작
				if( isStartEnd === "start" ) {
					//오늘 날짜와 비교
					if( todayDate.getTime() > startDateObj.getTime() ) {
						fnKendoMessage({ message : "오늘 날짜 이후를 선택해주세요.", ok : function() {
							$("#" + id).focus();
						}});

						return false;
					}

					//종료 날짜 없을 경우
					if( isNotValidData(_endYear) || !new Date(_endYear) instanceof Date ) return true;

					// 시작일 재설정 - 시/분 설정하지 않았을 시 기본값 설정
					if (isNotValidData(_startHour)) _startHour = 0;
					if (isNotValidData(_startMinute)) _startMinute = 0;
					startDateObj = new Date(_startYear + " " + _startHour + ":" + _startMinute);

					// 종료일 재설정 - 시/분 설정하지 않았을 시 기본값 설정
					if (isNotValidData(_endHour)) _endHour = 23;
					if (isNotValidData(_endMinute)) _endMinute = 59;
					endDateObj = new Date(_endYear + " " + _endHour + ":" + _endMinute);

					if( startDateObj.getTime() > endDateObj.getTime() ) {
						fnKendoMessage({ message : "종료 날짜 이전을 선택해주세요.", ok : function() {
							$("#" + id).focus();
						}});

						return false;
					}

					return true;
				} else {
					//오늘 날짜와 비교
					if( todayDate.getTime() > endDateObj.getTime() ) {
						fnKendoMessage({ message : "오늘 날짜 이후를 선택해주세요.", ok : function() {
							$("#" + id).focus();
						}});

						return false;
					}

					//시작 날짜 없을 경우
					if( isNotValidData(_startYear) || !new Date(_startYear) instanceof Date ) return true;

					// 시작일 재설정 - 시/분 설정하지 않았을 시 기본값 설정
					if (isNotValidData(_startHour)) _startHour = 0;
					if (isNotValidData(_startMinute)) _startMinute = 0;
					startDateObj = new Date(_startYear + " " + _startHour + ":" + _startMinute);

					// 종료일 재설정 - 시/분 설정하지 않았을 시 기본값 설정
					if (isNotValidData(_endHour)) _endHour = 23;
					if (isNotValidData(_endMinute)) _endMinute = 59;
					endDateObj = new Date(_endYear + " " + _endHour + ":" + _endMinute);

					if( startDateObj.getTime() > endDateObj.getTime() ) {
						fnKendoMessage({ message : "시작 날짜 이후를 선택해주세요.", ok : function() {
							$("#" + id).focus();
						}});

						return false;
					}

					return true;
				}
			},
            // 시간 분 변경 체크
			fnHourMinuteChange : function(e){
				e.preventDefault();


				let type = "";
				if((e.sender.element.context.id).indexOf('promotionName') > -1) {
					type = 'promotionName';
				} else if((e.sender.element.context.id).indexOf('sale') > -1) {
					type = 'sale';
				} else if((e.sender.element.context.id).indexOf('noticeBelow1') > -1) {
					type = 'noticeBelow1';
				} else if((e.sender.element.context.id).indexOf('noticeBelow2') > -1) {
					type = 'noticeBelow2';
				}

				const _id = e.sender.element.context.id;

				if( isNotValidData(_id) ) return;

				let _isStartEnd = "";
				let _mode = "";

				if( !!_id.match(/end/i) ) {
					_isStartEnd = "end";
				} else if ( !!_id.match(/start/i) ) {
					_isStartEnd = "start";
				} else {
					return;
				}

				if( !!_id.match(/year/i) ) {
					_mode = "year";
				} else if ( !!_id.match(/hour/i) ) {
					_mode = "hour";
				} else if( !!_id.match(/minute/i) ) {
					_mode = "minute"
				} else {
					return;
				}

				if( isNotValidData(_isStartEnd) ||  isNotValidData(_mode) ) return;

				const isSuccess = viewModel.fnCheckDateValidation(type, _isStartEnd, _mode, _id);

				//검증 실패 시 기존 데이터 복원
				if( !isSuccess ) {
					viewModel.searchInfo.set(_id, cacheData[_id]);
				} else {
					//검증 성공 시 기존 데이터 새로운 값으로 세팅
					cacheData[_id] = viewModel.searchInfo.get(_id);
				}

				return;
			},
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
			fnRemoveGoodsAdditionalGoodsMapping : function(e) {
				fnKendoMessage({
					type : "confirm",
					message : "선택한 추가상품 삭제 시 하단 상품목록은 초기화 됩니다. 진행하시겠습니까?",
					ok : function() {

						viewModel.set("isGoodsAdditionalGoodsMappingNoDataTbody", true);
						viewModel.set("goodsAdditionalGoodsMappingList", []);
		                $("#itemGrid").data("kendoGrid").dataSource.data( [] );


					},
					cancel : function() {
						return;
					}
				});
			},

			fnGoodsInfo : function(rowData, ctrlKey){ // 상품 수정

                // 상품유형에 따라 상품상세화면 이동
                let option = {};
                let goodsTypeCode = rowData.goodsTp;

                option.data = { ilGoodsId : rowData.ilGoodsId };

                switch(goodsTypeCode){
                    case "GOODS_TYPE.ADDITIONAL" : // 추가
                        option.url = "#/goodsAdditional";
                        option.menuId = 865;
                        break;
                    case "GOODS_TYPE.DAILY" : // 일일
                        option.url = "#/goodsDaily";
                        option.menuId = 1;
                        break;
                    case "GOODS_TYPE.DISPOSAL" : // 폐기임박
                        option.url = "#/goodsDisposal";
                        option.menuId = 921;
                        break;
                    case "GOODS_TYPE.GIFT" : // 증정
                        option.url = "#/goodsAdditional";
                        option.menuId = 865;
                        break;
					case "GOODS_TYPE.GIFT_FOOD_MARKETING" : // 식품마케팅증정
						option.url = "#/goodsAdditional";
						break;
                    case "GOODS_TYPE.INCORPOREITY" : // 무형
                        option.url = "#/goodsIncorporeal";
                        break;
                    case "GOODS_TYPE.NORMAL" : // 일반
                        option.url = "#/goodsMgm";
                        option.menuId = 98;
                        break;
                    case "GOODS_TYPE.PACKAGE" : // 묶음
                        option.url = "#/goodsPackage";
                        option.menuId = 768;
                        break;
                    case "GOODS_TYPE.RENTAL" : // 렌탈
                        option.url = "#/goodsRental";
                        option.menuId = 1286;
                        break;
                    case "GOODS_TYPE.SHOP_ONLY" : // 매장전용
                        option.url = "#/goodsShopOnly";
                        option.menuId = 1176;
                        break;
                };
            	option.target = '_blank';
            	fnGoNewPage(option);
			},
        });


        kendo.bind($("#searchForm"), viewModel);
	};

	function fnDefaultSet() {

		 // 데이터 초기화
        viewModel.searchInfo.set("promotionNm", "");
        viewModel.searchInfo.set("promotionNameStartYear", kendo.toString(todayDate, "yyyy-MM-dd"));
        viewModel.searchInfo.set("promotionNameStartHour", "00");
        viewModel.searchInfo.set("promotionNameStartMinute", "00");
        viewModel.searchInfo.set("promotionNameEndYear", "");
        viewModel.searchInfo.set("promotionNameEndHour", "00");
        viewModel.searchInfo.set("promotionNameEndMinute", "00");
        viewModel.searchInfo.set("couponUseYn", "");
        viewModel.searchInfo.set("goodsAddType", "add");

        viewModel.searchInfo.set("noticeBelow1StartDate", "");
        viewModel.searchInfo.set("noticeBelow1EndDate", "");
        viewModel.searchInfo.set("noticeBelow1StartYear", "");
        viewModel.searchInfo.set("noticeBelow1StartHour", "");
        viewModel.searchInfo.set("noticeBelow1StartMinute", "");
        viewModel.searchInfo.set("noticeBelow1EndYear", "");
        viewModel.searchInfo.set("noticeBelow1EndHour", "");
        viewModel.searchInfo.set("noticeBelow1EndMinute", "");

        viewModel.searchInfo.set("noticeBelow2StartDate", "");
        viewModel.searchInfo.set("noticeBelow2EndDate", "");
        viewModel.searchInfo.set("noticeBelow2StartYear", "");
        viewModel.searchInfo.set("noticeBelow2StartHour", "");
        viewModel.searchInfo.set("noticeBelow2StartMinute", "");

        viewModel.searchInfo.set("noticeBelow2EndYear", "");
        viewModel.searchInfo.set("noticeBelow2EndHour", "");
        viewModel.searchInfo.set("noticeBelow2EndMinute", "");

        viewModel.set("noticeBelow1ImageList", []);
        viewModel.set("noticeBelow1ImageFileList", []);
        viewModel.set("noticeBelow1ImageUploadResultList", []);

        viewModel.set("noticeBelow2ImageList", []);
        viewModel.set("noticeBelow2ImageFileList", []);
        viewModel.set("noticeBelow2ImageUploadResultList", []);

        viewModel.set('showNoticeBelow1Date', false);
        viewModel.set('showNoticeBelow2Date', false);

        $("input:radio[name='couponUseYn']").removeAttr("checked");
        viewModel.searchInfo.set("purchaseTargetType", []);
        viewModel.set("goodsAdditionalGoodsMappingList", []);
        viewModel.set("isGoodsAdditionalGoodsMappingNoDataTbody", true);
	};

	function fnInitCtgry() {
		ctgryGridDs1 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});

		var ctgryGridopt1 = {
			dataSource : ctgryGridDs1,
			change : ctgryGridClick1,
			columns : [{
				field : 'categoryName',
				title : '카테고리1',
				width : '80px',
				filterable : {
					cell : {
						enabled: false,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs1
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			scrollable : true,
			height : "225px",
			filterable : {
				mode : "row"
			}
		};
		ctgryGrid1 = $('#goodsDisplayCategory1').initializeKendoGrid(ctgryGridopt1).cKendoGrid();
		ctgryGridDs1.read({
			depth : '1'
		,	mallDiv : 'MALL_DIV.PULMUONE'
		});

		ctgryGridDs2 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt2 = {
			dataSource : ctgryGridDs2,
			change : ctgryGridClick2,
			columns : [{
				field : 'categoryName',
				title : '카테고리2',
				width : '80px',
				filterable : {
					cell : {
						enabled: false,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs2
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		ctgryGrid2 = $('#goodsDisplayCategory2').initializeKendoGrid(ctgryGridopt2).cKendoGrid();

		ctgryGridDs3 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt3 = {
			dataSource : ctgryGridDs3,
			change : ctgryGridClick3,
			columns : [{
				field : 'categoryName',
				title : '카테고리3',
				width : '80px',
				filterable : {
					cell : {
						enabled: false,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs3
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		ctgryGrid3 = $('#goodsDisplayCategory3').initializeKendoGrid(ctgryGridopt3).cKendoGrid();

		ctgryGridDs4 = fnGetDataSource({
			url : '/admin/goods/regist/getDisplayCategoryList'
		});
		var ctgryGridopt4 = {
			dataSource : ctgryGridDs4,
			change : ctgryGridClick4,
			columns : [{
				field : 'categoryName',
				title : '카테고리4',
				width : '80px',
				filterable : {
					cell : {
						enabled: false,
						showOperators : false,
						operator : "contains",
						suggestionOperator: "contains",
						dataSource : ctgryGridDs4
					}
				}
			}, {
				field : 'ilCtgryId',
				hidden : true
			}, {
				field : 'mallDiv',
				hidden : true
			}],
			filterable : {
				mode : "row"
			},
			fbMessage : 'none',
			scrollable : true,
			height : "225px",
		};
		ctgryGrid4 = $('#goodsDisplayCategory4').initializeKendoGrid(ctgryGridopt4).cKendoGrid();

		//헤더 라인 삭제
		$('#goodsDisplayCategory1,#goodsDisplayCategory2,#goodsDisplayCategory3,#goodsDisplayCategory4').find('tr:first').remove().end().each(function(){
			$(this).find(".k-grid-content").css("height", "192");
		});

	};


	function ctgryGridClick1() {
		var params = this.dataItem(this.select());
		ctgryGridDs2.read({
			'ilCtgryId' : params.ilCtgryId
		});
		$("#goodsDisplayCategory3").data("kendoGrid").dataSource.data([]);
		$("#goodsDisplayCategory4").data("kendoGrid").dataSource.data([]);
		setSelectCtgoryText(1);
	}

	function ctgryGridClick2() {
		var params = this.dataItem(this.select());
		ctgryGridDs3.read({
			'ilCtgryId' : params.ilCtgryId
		});
		$("#goodsDisplayCategory4").data("kendoGrid").dataSource.data([]);
		setSelectCtgoryText(2);
	}

	function ctgryGridClick3() {
		var params = this.dataItem(this.select());
		ctgryGridDs4.read({
			'ilCtgryId' : params.ilCtgryId
		});
		setSelectCtgoryText(3);
	}

	function ctgryGridClick4() {
		setSelectCtgoryText(4);
	}

	function setSelectCtgoryText(index) {
		var ctgry1 = ctgryGrid1.select();
		var ctgry2 = ctgryGrid2.select();
		var ctgry3 = ctgryGrid3.select();
		var ctgry4 = ctgryGrid4.select();

		var ctgoryTextArray = [];
		for (var i = index; i > 0; i--) {
			if (eval('ctgryGrid' + i + '.select().length') > 0) {
				ctgoryTextArray[(i - 1)] = eval("ctgryGrid" + i + ".dataItem(ctgryGrid" + i + ".select())").categoryName;
			}
		};
		if (ctgoryTextArray.length > 0) {
			$("#selectCtgoryText-addon").show();
			$('#selectCtgoryText').text(ctgoryTextArray.join(" > "));
		}
	}

	function fnCtgrySelect() {

		var ctgry1 = ctgryGrid1.select();
		var ctgry2 = ctgryGrid2.select();
		var ctgry3 = ctgryGrid3.select();
		var ctgry4 = ctgryGrid4.select();

		for (var i = 4; i > 0; i--) {

			//활성 상태일때 이고 리스트가 있을경우
			if (eval('ctgryGridDs' + i + '.data().length') > 0) {

				//선택 완료
				if (eval('ctgry' + i + '.length') > 0) {

					var data = eval("ctgryGrid" + i + ".dataItem(ctgry" + i + ")");
					var CTGRY = {};
					CTGRY['ilCtgryId'] = data.ilCtgryId;

					if (!fnCheckCtgrybyId(CTGRY['ilCtgryId'])) {
						CTGRY['basicYn'] = "N";
						CTGRY['categoryFullName'] = $('#selectCtgoryText').text();
						CTGRY['mallDiv'] = data.mallDiv;

						ctgryGridDs.add(CTGRY);
						$('#goodsDisplayCategoryGridArea').show();
					}
				} else {
					fnKendoMessage({
						//* 문구 확인후 번역 필요
						message : '최종 분류 선택 시 카테고리 추가가 가능합니다.',  // PJH 수정
						ok : function(e) {
							eval('ctgry' + i + '.focus()');
						}
					});
				}
				break;
			}
		};
	}

	function fnCheckCtgrybyId(ilCtgryId) {
		var datas = ctgryGrid.dataSource.data();
		for (var i = 0; i < datas.length; i++) {
			if (ilCtgryId == datas[i].ilCtgryId) {
				fnKendoMessage({
					message : '동일한 카테고리가 존재합니다.',
					ok : function(e) {
					}
				});
				return true;
			}
		}
		return false;
	}

	//선택된 전시 카테고리 분류 리스트 Grid
	function fnInitCategoryGrid() {
		ctgryGridDs = fnGetEditDataSource({
			url : '',
			model_id : 'ilGoodsCategoryId',
			model_fields : {
				ilGoodsCategoryId : {
					editable : false,
					type : 'number',
					validation : {
						required : true
					}
				},
				categoryFullName : {
					editable : false,
					type : 'string',
					validation : {
						required : true
					}
				},
				ilCtgryId : {
					editable : false,
					type : 'number',
					validation : {
						required : true
					}
				},
				basicYn : {
					editable : true,
					type : 'string',
					validation : {
						required : true
					}
				}
			}
		});

		ctgryGridOpt = {
			dataSource : ctgryGridDs,
			editable : true,
			columns : [{
				field : '',
				title : {
					key : '836',
					nullMsg : '기본'
				},
				width : '10%',
				template : '<input type="radio" name="basicYn" />',
				attributes : {
					style : 'text-align:center'
				}
			}, {
				field : 'categoryFullName',
				title : {
					key : '4410',
					nullMsg : '전시 카테고리'
				},
				width : '75%',
				attributes : {
					style : 'text-align:center'
				}
			}, {
                // PJH Start
                command : [ { text: "삭제", click: function(e) {
                    e.preventDefault();
                    var command = this;

                    const $targetRow = $(e.target).closest("tr");

                    ctgryGrid.select($targetRow);

//                    if (ctgryGrid.dataItem(ctgryGrid.select()).basicYn === "Y") {
//            			fnKendoMessage({ message : "기본 카테고리는 삭제하실 수 없습니다." });
//            			return;
//                    }

                    fnKendoMessage({
                        type : "confirm",
                        message : "삭제하시겠습니까?",
                        ok : function() {
                            var dataItem = command.dataItem($(e.target).closest("tr"));
                            var dataSource = $("#goodsDisplayCategoryGrid").data("kendoGrid").dataSource;

                            dataSource.remove(dataItem);
                        },
                        cancel : function() {
                            return;
                        }
                    })
                } } ],
                title : '관리',
                // PJH END
				width : '15%',
				attributes : {
					style : 'text-align:center',
					class : 'forbiz-cell-readonly'
				}
			}]
		};

		ctgryGrid = $('#goodsDisplayCategoryGrid').initializeKendoGrid(ctgryGridOpt).cKendoGrid();
		ctgryGrid.bind('dataBinding', function(e) {
/*
			if (e.action == 'remove' && $('form#inputForm input[name=basicYn]:checked:not(:eq(' + e.index + '))').length == 0) {
				if (ctgryGrid.dataSource.data().length > 0) {
					fnSetCtgryBasic(ctgryGrid.dataSource.data()[0].uid);
				}
			}
*/
		});

		$('#goodsDisplayCategoryGrid').on('change', ':radio', function(e) {
			fnSetCtgryBasic($(e.target).closest('tr').data('uid'));
		});
	};

	function fnSetCtgryBasic(uid) {
		var datas = ctgryGrid.dataSource.data();
		for (var i = 0; i < datas.length; i++) {
			if (uid == datas[i].uid) {
				datas[i].basicYn = "Y";
				datas[i].dirty = true;
			} else {
				datas[i].basicYn = "N";
				datas[i].dirty = true;
			}
		}
	}


	// 상품정보 일괄 수정 Grid
	function fnItemGrid() {

		itemGridDs = fnGetEditDataSource({
			model_id : 'ilGoodsId'

		});

		itemGridOpt = {
			dataSource : itemGridDs,
			height: 600,
			scrollable : true,
			editable : false,
			navigatable: true,
			columns : [
	                { field : "chk", headerTemplate : '<input type="checkbox" id="checkBoxAll" />',
	                  template : '<input type="checkbox" name="rowCheckbox" class="k-checkbox" />',
	                  width : "32px", attributes : {style : "text-align:center;"}
	                }
	              , { field : "itemCode", title : "품목코드/<BR>품목바코드", width : "140px", attributes : {style : "text-align:center;"},
	                  template : function(dataItem){
	                      let itemCodeView = dataItem.ilItemCd;

	                      if( dataItem.itemBarcode != "" ){
	                            itemCodeView += "/<br>" + dataItem.itemBarcode;
	                      } else {
	                            itemCodeView += "/ - ";
	                      }
	                      return itemCodeView;
	                  },
	                }
	              , { field : "ilGoodsId", title : "상품코드", width : "70px", attributes : { style : "text-align:center" },  }
			      , { field : "promotionNm", title : "프로모션명", width : "150px", attributes : { style : "text-align:center" }}
			      , { field : "supplierName", title : "공급업체", width: "150px", attributes : {style : "text-align:center;"} }
			      , { field : "goodsNm", title : "상품명", width : "150px", attributes : { style : "text-align:center" }}
			      , { field : "saleStatusName", title : "판매상태", width : "100px", attributes : { style : "text-align:center" } }
			      , { field : "dispYn", title : "전시상태", width : "80px", attributes : { style : "text-align:center" },
	                  template : function(dataItem){
	                      return dataItem.displayYn == "Y" ? "전시" : "미전시";
	                  }
	                }
			      , { field : "recommendedPrice", title : "정상가", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}"}
			      , { field : "discountPriceRatio", title : "할인율", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}",
	                  template : function(dataItem){
	                      return dataItem.discountPriceRatio+"%";
	                  }
			      }
			      , { field : "salePrice", title : "판매가", width : "80px", attributes : { style : "text-align:center" }, format: "{0:n0}"}
			      , { field : "categoryDepthName", title : "전시 카테고리", width: "200px", attributes : {style : "text-align:center;"}}
			      , { field : "warehouseNm", title : "출고처명", width: "100px", attributes : {style : "text-align:center;"}}
			      , { field : "displayWeb", title : "판매허용범위<BR>(PC/Mobile/App)", width : "120px", attributes : {style : "text-align:center;"},
	                  template : function(dataItem){
	                      let displayWebName = "";

	                      if( dataItem.displayWebPcYn == "Y" && dataItem.displayWebMobileYn == "Y" && dataItem.displayAppYn == "Y" ){

	                          displayWebName = "전체";
	                      }else{

	                          let displayWebNameArr = [];

	                          if( dataItem.displayWebPcYn == "Y" ){
	                              displayWebNameArr.push("PC");
	                          }

	                          if( dataItem.displayWebMobileYn == "Y" ){
	                              displayWebNameArr.push("Mobile");
	                          }

	                          if( dataItem.displayAppYn == "Y" ){
	                              displayWebNameArr.push("APP");
	                          }

	                          displayWebName = displayWebNameArr.join(", ");
	                      }

	                      return displayWebName;
	                  }
	                }
			      , { field : "couponUseYn", title : "쿠폰사용", width: "60px", attributes : {style : "text-align:center;"} }
			      , { field : "goodsAddInfo", title : "추가상품", width: "90px", attributes : {style:'text-align:left, text-decoration: underline;color:blue;'} }
			      ,	{ field:'preOrderTpNm'		  ,title : '상품공지', width:'80px'	,attributes:{ style:'text-align:center' },
	              	  	template: function(dataItem) {
								var html = '';
								html = "<a role='button' class='k-button k-button-icontext' kind='goodsNotice'>상세<br>보기</a>";
								return html;
	              	  	}
                	 }

			      , { command : [ { name : "수정",
	                      click : function(e) {
	                          e.preventDefault();
	                          let row = $(e.target).closest("tr");
	                          let rowData = this.dataItem(row);

	                          viewModel.fnGoodsInfo(rowData, e.ctrlKey);
	                      }, template: "<a role=\"button\" class=\"k-button k-button-icontext k-grid-수정\">수정</a>"
	                    }
	                  ], title : "관리", width : "80px", attributes : { style : "text-align:center" }
			      }

	              ]
		};
		itemGrid = $('#itemGrid').initializeKendoGrid(itemGridOpt).cKendoGrid();

		itemGrid.bind("dataBound", function() {
            $('#pageTotalText').text( kendo.toString(itemGridDs._total, "n0") );

            if(itemGridDs._total == 0) {
                $("#itemGrid").find('tr > td').text("조회하시고자 하는 검색조건을 선택후 검색해주세요");
            }

        });

		$("#checkBoxAll").on("click", function(index){
            if( $("#checkBoxAll").prop("checked") ){
                $("input[name=rowCheckbox]").prop("checked", true);
            }else{
                $("input[name=rowCheckbox]").prop("checked", false);
            }
        });

		$('#ng-view').on("click", "input[name='rowCheckbox']", function (e) {
			const $checkbox = $("input[name=rowCheckbox]");
			const $checkbox_checked = $("input[name=rowCheckbox]:checked");
			const $checkboxAll = $('#checkBoxAll');

			let len = $checkbox.length;
			let len_checked = $checkbox_checked.length;

			if (len == len_checked){
				$checkboxAll.prop('checked', true);
			} else {
				$checkboxAll.prop('checked', false);
			}
		});

		$("#itemGrid").on("click", "tbody>tr>td", function () {
			//fnGridClick();
			var index = $(this).index();

			if(index == 15) {
				var aMap = itemGrid.dataItem(itemGrid.select());

				if(aMap.goodsAddInfo != "") {
					fnKendoPopup({
						id		 : "goodsAdditionListPopup",
						title	  : "추가상품 목록",
						width : '1000px',
	                    height : '500px',
						scrollable : "yes",
						src		: "#/goodsAdditionListPopup",
						param : {
	                        ilGoodsId : aMap.ilGoodsId,
	                        warehouseId : aMap.urWarehouseId
	                    },
						success	: function( id, data ){

						}
					});
				}

			}

		});

		$('#itemGrid').on("click", "a[kind=goodsNotice]", function(e) {
        	e.preventDefault();
			var dataItem = itemGrid.dataItem($(e.currentTarget).closest("tr"));

			let params = {};
			params.ilGoodsId = dataItem.ilGoodsId;

			fnKendoPopup({
				id			: "goodsNoticeInfoPopup",
				title		: "상품 공지",  // 해당되는 Title 명 작성
				width		: "900px",
				height		: "500px",
				scrollable	: "yes",
				src			: "#/goodsNoticeInfoPopup",
				param		: params,
				success		: function( id, data ){

				}
			});
        });

	};

	//상품공지 > 상세 상단공지 > 파일선택 버튼 이벤트
	function fnNoticeBelow1File() {
		$('#uploadNoticeBelow1Image').trigger('click');
	};

	//상품공지 > 상세 하단공지 > 파일선택 버튼 이벤트
	function fnNoticeBelow2File() {
		$('#uploadNoticeBelow2Image').trigger('click');
	};

	function fnSave() {

		if(viewModel.searchInfo.get("goodsSelectType") == "") {
			fnKendoMessage({ message : "상품 수정 정보를 선택하여 주세요."});
            return;
		}

        if( $("input[name=rowCheckbox]:checked").length == 0 ){
            fnKendoMessage({ message : "선택된 상품이 없습니다." });
            return;
        }

        if(fnValidationCheck() == false) {
        	return;
        }
        var msg = "";

        if(viewModel.searchInfo.get("goodsSelectType") == "goodsAdd") {
        	if(viewModel.searchInfo.get("goodsAddType") == "add") {
        		msg = "등록한 정보로 일괄등록하시겠습니까? 선택한 상품 중 추가상품과 동일한 출고처의 상품에만 반영됩니다.";
        	}else if (viewModel.searchInfo.get("goodsAddType") == "del") {
        		msg = "등록한 정보로 일괄삭제하시겠습니까? 선택한 상품중 해당 추가상품이 포함되어 있을 경우 삭제처리됩니다";
        	}
        }else {
        	msg = "등록한 정보로 일괄등록하시겠습니까?"
        }

        fnKendoMessage({
            type : "confirm",
            message : msg,
            ok : function() {

            	fnModifyGoods();
            },
            cancel : function() {
                return;
            }
        });
	};

	function fnModifyGoods() {

		var goodsList = [];

		$("input[name=rowCheckbox]").each(function(index, item) {
        	if($(this).is(":checked") == true) {
                let row = $(this).closest("tr");
                let dataItem = itemGrid.dataItem(row);

                goodsList.push(dataItem);

        	}

        });

		var data = $('#searchForm').formSerialize(true);

		if (viewModel.get('noticeBelow1ImageFileList').length > 0) {																		// 상세 상단공지 이미지 존재시
			var noticeBelow1ImageUploadResultList = fnUploadNoticeBelow1Image();															// 상세 상단공지 이미지 / 결과 return
			viewModel.set('noticeBelow1ImageUploadResultList', noticeBelow1ImageUploadResultList);
			//viewModel.set('noticeBelow1ImageFileList', []);																					// 기존 상세 상단공지 이미지 초기화
		}

		if (viewModel.get('noticeBelow2ImageFileList').length > 0) {																		// 상세 하단공지 이미지 존재시
			var noticeBelow2ImageUploadResultList = fnUploadNoticeBelow2Image();															// 상세 하단공지 이미지 / 결과 return
			viewModel.set('noticeBelow2ImageUploadResultList', noticeBelow2ImageUploadResultList);
			//viewModel.set('noticeBelow2ImageFileList', []);																					// 기존 상세 하단공지 이미지 초기화
		}

		var modifyGoodsParam = {
				goodsSelectType :  viewModel.searchInfo.get("goodsSelectType")
				,	promotionNm							: viewModel.searchInfo.get("promotionNm")													//프로모션 상품명
				,	promotionNameStartDate					: kendo.toString(viewModel.fnGetDateFull("promotionNameStart"), "yyyy-MM-dd HH:mm")				// 프로모션 시작일
				,	promotionNameEndDate					: kendo.toString(viewModel.fnGetDateFull("promotionNameEnd"), "yyyy-MM-dd HH:mm")				// 프로모션 종료일
				, 	goodsGridList							: goodsList
				,   displayCategoryList						: ctgryGridDs.data()
				,   purchaseTargetType						: viewModel.searchInfo.get("purchaseTargetType")
				,   couponUseYn								: viewModel.searchInfo.get("couponUseYn")
				,	noticeBelow1ImageUploadResultList		: viewModel.get("noticeBelow1ImageUploadResultList")								//상세 상단공지 이미지 Upload 정보
				,	noticeBelow1StartDate					: kendo.toString(viewModel.fnGetDateFull("noticeBelow1Start"),"yyyy-MM-dd HH:mm")	//상세 상단공지 시작일
				,	noticeBelow1EndDate						: kendo.toString(viewModel.fnGetDateFull("noticeBelow1End"),"yyyy-MM-dd HH:mm")		//상세 상단공지 종료일
				,	noticeBelow2ImageUploadResultList		: viewModel.get("noticeBelow2ImageUploadResultList")								//상세 하단공지 이미지 Upload 정보
				,	noticeBelow2StartDate					: kendo.toString(viewModel.fnGetDateFull("noticeBelow2Start"),"yyyy-MM-dd HH:mm")	//상세 하단공지 시작일
				,	noticeBelow2EndDate						: kendo.toString(viewModel.fnGetDateFull("noticeBelow2End"),"yyyy-MM-dd HH:mm")		//상세 하단공지 종료일
				,	goodsAdditionList 						: viewModel.get("goodsAdditionalGoodsMappingList")								// 추가상품 목록
				,   goodsAddType							: viewModel.searchInfo.get("goodsAddType")										// 추가상품 추가삭제유무
		};

		if (viewModel.get('noticeBelow1ImageUploadResultList').length == 0) {
			modifyGoodsParam.noticeBelow1StartDate = null;
			modifyGoodsParam.noticeBelow1EndDate = null;
		}

		if (viewModel.get('noticeBelow2ImageUploadResultList').length == 0) {
			modifyGoodsParam.noticeBelow2StartDate = null;
			modifyGoodsParam.noticeBelow2EndDate = null;
		}

		fnAjax({
            url : "/admin/goods/list/putGoodsAllModify",
            params : modifyGoodsParam,
            contentType : 'application/json',
            isAction : 'insert',
            success : function(data) {
            	if(viewModel.searchInfo.get("goodsSelectType") == "promotionAdd" || viewModel.searchInfo.get("goodsSelectType") == "goodsNoticeAdd") {
            		fnKendoMessage({
    					message : '일괄수정이 완료되었습니다. 설정한 기간에 따라 목록에 표시되지 않을수 있습니다',
    					ok : function() {
    						fnGridRefresh();
    					}
    				});
            	}else if(viewModel.searchInfo.get("goodsSelectType") == "displayCategoryAdd" || viewModel.searchInfo.get("goodsSelectType") == "purchaseAdd") {
            		fnKendoMessage({
    					message : '일괄수정이 완료되었습니다.',
    					ok : function() {
    						fnGridRefresh();
    					}
    				});
            	}else if(viewModel.searchInfo.get("goodsSelectType") == "goodsAdd") {
            		if(viewModel.searchInfo.get("goodsAddType") == "add") {
            			fnKendoMessage({
        					message : '출고처가 다른 상품을 제외한 동일출고처 상품에 일괄수정이 완료되었습니다.',
        					ok : function() {
        						fnGridRefresh();
        					}
        				});
            		}else if(viewModel.searchInfo.get("goodsAddType") == "del") {
            			fnKendoMessage({
        					message : '해당상품이 추가된 상품에 삭제가 완료되었습니다.',
        					ok : function() {
        						fnGridRefresh();
        					}
        				});
            		}
            	}




            },
            error : function(xhr, status, strError) {
            	fnKendoMessage({
            		message : xhr.responseText
            	});
            },
		});
	};

	function fnGridRefresh() {

		var dataItem = $("#itemGrid").data("kendoGrid").dataSource.data();

		var goodsIds = [];

    	for(var i = 0; i < dataItem.length ; i++) {
    		goodsIds.push(dataItem[i].ilGoodsId);

    	}

    	var url  = '/admin/goods/list/getGoodsAllModifyList';
    	var cbId = 'goodsModify';

    	var data = { ilGoodsIds : goodsIds.join(",") }
    	viewModel.searchInfo.set("searchGoodsId", goodsIds.join(","));

    	fnAjax({
    		url	 : url,
    		params  : data,
    		async	: false,
    		success :
    		function( data ){
    			$("#itemGrid").data("kendoGrid").dataSource.data([]);
				$("#checkBoxAll").prop("checked", false);
    			fnBizCallback(cbId, data);
    		},
    		isAction : 'batch'
    	});

	};

	function fnValidationCheck() {

		if(viewModel.searchInfo.get("goodsSelectType") == "promotionAdd") {
			// 프로모션 상품명
			if(viewModel.searchInfo.get("promotionNm").length == 0) {
				valueCheck("프로모션 상품명을 입력해 주세요.", "promotionNm");
				return false;
			}
			if(byteCheck(viewModel.searchInfo.get("promotionNm")) > 0){	//프로모션 상품명이 존재한다면
				if(viewModel.searchInfo.get("promotionNameStartYear").length == 0){
					valueCheck("프로모션 시작일을 입력해 주세요.", "promotionNameStartYear");
					return false;
				}

				if(viewModel.searchInfo.get("promotionNameEndYear").length == 0){
					valueCheck("프로모션 종료일을 입력해 주세요.", "promotionNameEndYear");
					return false;
				}
			}

			if(byteCheck(viewModel.searchInfo.get("promotionNm")) > 12 ){ //프로모션 상품명이 12byte를 초과하면
				valueCheck("프로모션 상품명은 12BYTE (영문12자 또는 한글6자) 까지 등록가능합니다.","promotionNm");
				return false;
			}
		}else if(viewModel.searchInfo.get("goodsSelectType") == "displayCategoryAdd") {
			if (ctgryGridDs.data().length == 0) {
				valueCheck("최소 1개이상의 전시카테고리를 추가해주세요.", "goodsDisplayCategory1");
				$('#goodsDisplayCategory1 input:text').focus();
				return false;
			}
		}else if(viewModel.searchInfo.get("goodsSelectType") == "purchaseAdd") {

			var purchasChk = false;
			$("input:checkbox[name='purchaseTargetType']").each(function(){
            	if($(this).is(":checked") == true) {
            		purchasChk = true;
            	}
            });

			if(!purchasChk) {
				valueCheck("판매 허용 범위 필수정보를 입력해주세요", "goodsDisplayCategory1");
				return false;
			}

			var couponResult = $("input[name='couponUseYn']:checked").val();

			if(couponResult == undefined) {
				valueCheck("쿠폰 사용 허용 정보를 선택해주세요", "");
				return false;
			}

			return purchasChk;
		}else if(viewModel.searchInfo.get("goodsSelectType") == "goodsNoticeAdd") {
			var fileLength1 = parseInt(viewModel.get('noticeBelow1ImageFileList').length);
			var fileLength2 = parseInt(viewModel.get('noticeBelow2ImageFileList').length);

			if (fileLength1 == 0 && fileLength2 == 0) {
				valueCheck("공지 이미지 및 노출기간은 필수값 입니다.", "");
				return false;
			}

			if (fileLength1 > 0) {
				if (
					viewModel.searchInfo.get('noticeBelow1StartYear') == ''
					|| viewModel.searchInfo.get('noticeBelow1StartHour') == ''
					|| viewModel.searchInfo.get('noticeBelow1StartMinute') == ''
					|| viewModel.searchInfo.get('noticeBelow1EndYear') == ''
					|| viewModel.searchInfo.get('noticeBelow1EndHour') == ''
					|| viewModel.searchInfo.get('noticeBelow1EndMinute') == ''
				) {
					valueCheck("공지 이미지 및 노출기간은 필수값 입니다.", "");
					return false;
				}
			}

			if (fileLength2 > 0) {
				if (
					viewModel.searchInfo.get('noticeBelow2StartYear') == ''
					|| viewModel.searchInfo.get('noticeBelow2StartHour') == ''
					|| viewModel.searchInfo.get('noticeBelow2StartMinute') == ''
					|| viewModel.searchInfo.get('noticeBelow2EndYear') == ''
					|| viewModel.searchInfo.get('noticeBelow2EndHour') == ''
					|| viewModel.searchInfo.get('noticeBelow2EndMinute') == ''
				) {
					valueCheck("공지 이미지 및 노출기간은 필수값 입니다.", "");
					return false;
				}
			}

		}else if(viewModel.searchInfo.get("goodsSelectType") == "goodsAdd") {
			if( viewModel.get("goodsAdditionalGoodsMappingList").length == 0) {
    			valueCheck("추가 상품을 선택하여 주세요.", "");
				return false;
    		}
		}
	};

	/**
	 * 상세 상단공지 이미지 업로드
	 **/
	function fnUploadNoticeBelow1Image() {

		var formData = new FormData();
		var noticeBelow1ImageFileList = viewModel.get('noticeBelow1ImageFileList');

		for (var i = 0; i < noticeBelow1ImageFileList.length; i++) {
			// noticeBelow1Image01, noticeBelow1Image02, ... 형식으로 formData 에 이미지 file 객체 append, 현재는 단건만 저장됨!!!!
			formData.append('noticeBelow1Image' + ('0' + (i + 1)).slice(-2), noticeBelow1ImageFileList[i]);
		}

		formData.append('storageType', 'public'); // storageType 지정
		formData.append('domain', 'il'); // domain 지정

		var noticeBelow1ImageUploadResultList; // 상품 이미지 업로드 결과 목록

		$.ajax({
			url : '/comn/fileUpload',
			data : formData,
			type : 'POST',
			contentType : false,
			processData : false,
			async : false,
			success : function(data) {
				data = data.data;
				noticeBelow1ImageUploadResultList = data['addFile'];
			}
		});

		return noticeBelow1ImageUploadResultList;
	}

	/**
	 * 상세 하단공지 이미지 업로드
	 **/
	function fnUploadNoticeBelow2Image() {

		var formData = new FormData();
		var noticeBelow2ImageFileList = viewModel.get('noticeBelow2ImageFileList');

		for (var i = 0; i < noticeBelow2ImageFileList.length; i++) {
			// noticeBelow2Image01, noticeBelow2Image02, ... 형식으로 formData 에 이미지 file 객체 append, 현재는 단건만 저장됨!!!!
			formData.append('noticeBelow2Image' + ('0' + (i + 1)).slice(-2), noticeBelow2ImageFileList[i]);
		}

		formData.append('storageType', 'public'); // storageType 지정
		formData.append('domain', 'il'); // domain 지정

		var noticeBelow2ImageUploadResultList; // 상품 이미지 업로드 결과 목록

		$.ajax({
			url : '/comn/fileUpload',
			data : formData,
			type : 'POST',
			contentType : false,
			processData : false,
			async : false,
			success : function(data) {
				data = data.data
				noticeBelow2ImageUploadResultList = data['addFile'];
			}
		});

		return noticeBelow2ImageUploadResultList;
	}

	/*
	 * 메시지 팝업 호출 함수
	 */
	function valueCheck(nullMsg, id) {
		fnKendoMessage({
			message : nullMsg,
			ok : function focusValue() {
				$('#' + id).focus();
			}
		});

		return false;
	};

	function fnGoodsAdd() {

		var params = {};
		params.selectType = "multi"; // 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )

		params.goodsType = "GOODS_TYPE.SHOP_ONLY,GOODS_TYPE.DISPOSAL,GOODS_TYPE.NORMAL,GOODS_TYPE.PACKAGE";	// 상품유형(복수 검색시 , 로 구분)
		params.supplierIdDisabled = false;
		params.warehouseIdDisabled = false;
		params.warehouseGrpDisabled = false;
		params.goodsTypeReDraw = 'Y';
		params.saleStatus = "SALE_STATUS.ON_SALE";
		params.columnNameHidden = false;
		params.columnDpBrandNameHidden = false;
		params.columnStardardPriceHidden = false;
		params.columnRecommendedPriceHidden = false;
		params.columnSalePriceHidden = false;
		params.columnSaleStatusCodeNameHidden = false;
		params.columnGoodsDisplyYnHidden = false;
		params.pgId = "goodsAllModify";

		var dataSource = itemGrid.dataSource;
		var totalRecords = dataSource.total();

		params.goodsTotal = totalRecords;
		params.goodsIds = [];


    	if(viewModel.searchInfo.get("goodsSelectType") == "goodsAdd") {
    		if( viewModel.get("goodsAdditionalGoodsMappingList").length == 0) {
    			valueCheck("추가 상품을 선택하여 주세요.", "");
				return false;
    		}else {
    			params.warehouseIdDisabled = true;
    			params.warehouseGrpDisabled = true;
    			params.warehouseId = viewModel.get("goodsAdditionalGoodsMappingList")[0].warehouseId;
    			params.warehouseGrpCd = viewModel.get("goodsAdditionalGoodsMappingList")[0].warehouseGrpCd;
    			params.ilShippingTmplId = viewModel.get("goodsAdditionalGoodsMappingList")[0].ilShippingTmplId;
    			params.bundleYn = viewModel.get("goodsAdditionalGoodsMappingList")[0].bundleYn;
    		}
    	}

    	if(totalRecords > 0) {
			$("#itemGrid tbody > tr").each(function(index, item){
				let dataItem = itemGrid.dataItem(item);

				params.goodsIds.push(dataItem.goodsId);
			});
		}

		fnKendoPopup({
			id		 : "goodsSearchPopup",
			title	  : "일괄수정 상품 등록",
			width	  : "1700px",
			height	 : "800px",
			scrollable : "yes",
			src		: "#/goodsSearchPopup",
			param	  : params,
			success	: function( id, data ){

				if (data.length > 0) {
					var goodsIds = [];

					for (var i = 0; i < data.length; i++) {
						goodsIds.push(data[i].goodsId);
					}

					var url  = '/admin/goods/list/getGoodsAllModifyList';
					var cbId = 'goodsModify';

					var data = { ilGoodsIds : goodsIds.join(",") }
					viewModel.searchInfo.set("searchGoodsId", goodsIds.join(","));

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
				}
			}

		});
		// ######### 개발 완료 후 팝업을 여는 실제 적용할 소스 end ############
	};

	function fnGoodsDel() {

		if( $("input[name=rowCheckbox]:checked").length == 0 ){
            fnKendoMessage({ message : "선택된 상품이 없습니다." });
            return;
        }


		$("input[name=rowCheckbox]").each(function(index, item) {
        	if($(this).is(":checked") == true) {
                let row = $(this).closest("tr");
                let dataItem = itemGrid.dataItem(row);

                itemGrid.removeRow(row);
        	}

        });

	};

	function fnBizCallback(cbId, data) {

		var dataRows = data.rows;

		for (var i in dataRows) {
			dataRows[i].baseGoodsYn = "N";

			if (!itemGridDs.get(dataRows[i].ilGoodsId)) {
				itemGridDs.add(dataRows[i]);
			}
		}


	};

	function fnGoodsSearchPopup() {

		let params = {};
		let title = "";

		title = "추가상품 선택";
//		params.supplierId = urSupplierId;							// 공급처ID ( 상품유형이 추가 일 경우 필수 )
//		params.warehouseId = urWarehouseId;							// 출고처 ID ( 상품유형이 추가 일 경우 필수 )

		params.saleStatus = "SALE_STATUS.ON_SALE";
		params.goodsType = "GOODS_TYPE.ADDITIONAL";

		if(viewModel.get("goodsAdditionalGoodsMappingList").length > 0) {
			fnKendoMessage({
                type : "confirm",
                message : "추가상품 변경 시 하단 상품목록은 초기화 됩니다. 진행하시겠습니까?",
                ok : function() {
                	$("#itemGrid").data("kendoGrid").dataSource.data([]);
                	fnKendoPopup({
            			id			: "goodsAdditionPopup",
            			title		: title,  // 해당되는 Title 명 작성
            			width		: "1700px",
            			height		: "800px",
            			scrollable	: "yes",
            			src			: "#/goodsAdditionPopup",
            			param		: params,
            			success		: function( id, data ){
            				if(data.length != undefined) {
            					viewModel.set("isGoodsAdditionalGoodsMappingNoDataTbody", false);
            					viewModel.set("isGoodsAdditionalGoodsMappingTbody", true);
            					viewModel.set('goodsAdditionalGoodsMappingList', data);
            				}
            			}
            		});

                },
                cancel : function() {
                    return;
                }
            })
		}else {
			fnKendoPopup({
				id			: "goodsAdditionPopup",
				title		: title,  // 해당되는 Title 명 작성
				width		: "1700px",
				height		: "800px",
				scrollable	: "yes",
				src			: "#/goodsAdditionPopup",
				param		: params,
				success		: function( id, data ){
					if(data.length != undefined) {
						viewModel.set("isGoodsAdditionalGoodsMappingNoDataTbody", false);
						viewModel.set("isGoodsAdditionalGoodsMappingTbody", true);
						viewModel.set('goodsAdditionalGoodsMappingList', data);
					}

				}
			});
		}


	};

	function setCacheData() {
		cacheData = !isEmpty(viewModel.searchInfo) ?  Object.assign(cacheData, viewModel.searchInfo) : { };
	};

	//데이터 비었는지 체크하는 함수
	function isNotValidData(data) {
		if( data === undefined || typeof data === "undefined" || data === null || data === "" ) return true;
		else return false;
	};

	function isEmpty(obj){
		return Object.keys(obj).length === 0;
	};

	function fnExcelExport() {

//		var data = $('#searchForm').formSerialize(true);

		var data = {};
		data.searchGoodsId = viewModel.searchInfo.get("searchGoodsId");

		fnExcelDownload('/admin/goods/list/createGoodsAllModifyExcel', data);
	}

    /** Common Search */
	$scope.fnSearch = function() {
		fnSearch();
	};

	/** Common Clear */
	$scope.fnClear = function() {
		fnClear();
	};

	/** Common Excel */
	$scope.fnExcelExport = function() {
		fnExcelExport();
	};

	$scope.fnCtgrySelect = function( ){				//전시 카테고리 분류 선택
		fnCtgrySelect();
	};

	$scope.fnNoticeBelow1File = function( ){		//상품공지 > 상세 상단공지 첨부파일
		fnNoticeBelow1File();
	};

	$scope.fnNoticeBelow2File = function( ){		//상품공지 > 상세 상단공지 첨부파일
		fnNoticeBelow2File();
	};

	$scope.fnGoodsAdd = function() {				// 상품 추가 버튼
		fnGoodsAdd();
	};

	$scope.fnGoodsDel = function() {				// 상품 추가한 내역 삭제 버튼
		fnGoodsDel();
	};

	$scope.fnSave = function() {					// 일괄 수정 저장 버튼
		fnSave();
	};

	$scope.fnGoodsSearchPopup = function() {					// 추가상품 추가 팝업
		fnGoodsSearchPopup();
	};





});
