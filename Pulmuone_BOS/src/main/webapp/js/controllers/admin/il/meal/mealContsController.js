/**-----------------------------------------------------------------------------
 * description 		 : 식단컨텐츠관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2021.09.07		안치열          최초생성
 * @
 * **/
'use strict';


var PAGE_SIZE = 50;
var aGridDs, aGridOpt, aGrid;
var gFileTagId;
var gFile;
var CUR_SERVER_URL = fnGetServerUrl().mallUrl;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'mealList',
			callback : fnUI
		});


	}


	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitKendoUpload();

//		fnSearch();

	}




	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSearch, #fnNew, #fnSave, #fnClear, #fnClose, #fnExcelDown, #fnMealSampleDownload, #fnMealUpload').kendoButton();

		$('#kendoPopupContentsPreview').kendoWindow({
			visible: false,
			modal: true
		});
	}
	function fnSearch(){
		$('#inputForm').formClear(false);
		var data;
		data = $('#searchForm').formSerialize(true);

		var query = {
					page         : 1,
					pageSize     : PAGE_SIZE,
					filterLength : fnSearchData(data).length,
					filter :  {
						filters : fnSearchData(data)
					}
		};
		aGridDs.query( query );
	}
	function fnClear(){
		$('#searchForm').formClear(true);

		$(".date-controller button").each(function() {
			$(this).attr("fb-btn-active", false);
		})

		//$(".dateSearchDiv").find('button[data-id="fnDateBtn3"]').attr("fb-btn-active", true);
	}

	//식단 개별 등록
	function fnNew(){

		fnKendoPopup({
			id: 'mealContsMgm',
			title: fnGetLangData({ nullMsg: '컨텐츠 등록/수정' }),
			param: { "type" : "add"},
			src: '#/mealContsMgm',
			width: '80%',
			height: '90%',
			success: function(id, data) {
				fnSearch();
			},
			error : function(){
				//openPopup = false;
			},
		});
	}

	function fnSave(){

	}

	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	// 식단 컨텐츠 엑셀 다운로드
	function fnExcelDown(){
	    var data = $('#searchForm').formSerialize(true);

	    if(aGridDs != undefined && aGridDs._total > 0 ){
            fnExcelDownload('/admin/il/meal/getExportExcelMealContsList', data);
	    }else{
	        fnKendoMessage({message : "조회 목록이 없습니다."});
            return;
	    }

	}

	// 식단 업로드 양식 다운로드
	function fnMealSampleDownload(){
        document.location.href = "/contents/excelsample/meal/풀무원_상품_관리_일일상품_식단컨텐츠_관리_업로드_양식.xlsx";
	}

	// 식단 일괄 업로드
    function fnMealUploadRun(){
		if(gFile == undefined || gFile == ""){
			fnKendoMessage({
				message : "엑셀파일을 등록해주세요.",
				ok : function(e) {
				}
			});
			return;
		}
		fnExcelUpload(gFile, gFileTagId);
    }

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : "/admin/il/meal/getMealContsList",
			pageSize : PAGE_SIZE
		});
		aGridOpt = {
			dataSource: aGridDs
			,  pageable  : {
				pageSizes: [20, 30, 50, 100],
				buttonCount : 5
			}
			,navigatable: true
			,sortable: {
				initialDirection: "desc"
			}
			,columns   : [
				 { field:'no'	                    ,title : 'No'	            , width:'100px',attributes:{ style:'text-align:center' }, template: "<span class='row-number'></span>", sortable: false}
				,{ field:'mallDivNm'	            ,title : '식단분류'	        , width:'150px',attributes:{ style:'text-align:center' }, sortable: false}
				,{ field:'ilGoodsDailyMealContsCd'	,title : '식단 품목 코드'	    , width:'150px',attributes:{ style:'text-align:center' }, sortable: false}
				,{ field:'ilGoodsDailyMealNm'		,title : '식단명'		    , width:'200px',attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }, sortable: false}
				,{ field:'allergyYn'	 		    ,title : '알러지 식단여부' 	, width:'150px',attributes:{ style:'text-align:center' }}
				,{ field:'createInfo'		        ,title : '등록일 / 최근 수정일', width:'200px',attributes:{ style:'text-align:center' }, sortable: false
					, template: function(dataItem) {
						let createInfo = dataItem.createInfo;
						if(createInfo.indexOf("/") != -1){
							createInfo = createInfo.replace("/","/<BR>")
						}
						return createInfo;
					}}
				,{ field:'admin'		            ,title : '관리'		        , width:'200px',attributes:{ style:'text-align:center' }, sortable: false
				    , command: [
                                  { text    : '미리보기' , className: "btn-point btn-s"
                                  , click   : function(e) {
                                                e.preventDefault();
                                                var tr = $(e.target).closest("tr");
                                                var data = this.dataItem(tr);
                                                fnMealReview(data.ilGoodsDailyMealContsCd);
                                              }
                                  }
                                , { text    : '삭제'  ,  imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
                                  , click   : function(e) {
                                                e.preventDefault();
                                                var tr = $(e.target).closest("tr");
                                                var data = this.dataItem(tr);
                                                fnMealDelete(data);
                                              }
                                  }
                                ]
				}
				,{ field:'psShippingPatternId'	, hidden:true}
				,{ field:'deletePossibleYn'		, hidden:true}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
			$("#aGrid").on("click", "tbody>tr>td", function () {

				var index = $(this).index();
				var map = aGrid.dataItem(aGrid.select());

				if(index == 3){
					// 식단컨텐츠 등록/수정 팝업
					fnGridClick(map);
				}
		});

		aGrid.bind("dataBound", function() {
			//row number
			var row_number = aGridDs._total - ((aGridDs._page - 1) * aGridDs._pageSize);
			$("#aGrid tbody > tr .row-number").each(function(){
				$(this).html(row_number);
				row_number--;
			});

			//total count
            $('#totalCnt').text(aGridDs._total);
        });

	}




	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		// 식단분류
        fnKendoDropDownList({
            id : "mallDiv",
            tagId : "mallDiv",
            data : [ { "CODE" : ""	, "NAME":'전체' },
                     { "CODE" : "MALL_DIV.BABYMEAL", "NAME" : "베이비밀" },
                     { "CODE" : "MALL_DIV.EATSLIM", "NAME" : "잇슬림" }
                   ]
        });

		// 검색기간
        fnKendoDropDownList({
            id : "dateSearchType",
            tagId : "dateSearchType",
            data : [ { "CODE" : "CREATE_DATE", "NAME" : "등록일" },
                     { "CODE" : "MODIFY_DATE", "NAME" : "최근 수정일" }
                   ]
        });

        // 등록일 시작
        fnKendoDatePicker({
            id: "startDt",
            format: "yyyy-MM-dd",
            btnStartId: "startDt",
            btnEndId: "endDt",
            //defVal : fnGetDayMinus(fnGetToday(),6),
            //defType : 'oneWeek',
            change : function(e) {
                fnStartCalChange("startDt", "endDt");
            }
        });

        // 등록일 종료
        fnKendoDatePicker({
            id: "endDt",
            format: "yyyy-MM-dd",
            btnStyle: true,
            btnStartId: "startDt",
            btnEndId: "endDt",
            //defVal : fnGetToday(),
            //defType : 'oneWeek',
            change : function(e) {
                fnEndCalChange("startDt", "endDt");
            }
        });

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function fnInitKendoUpload() {
		var uploadFileTagIdList = ['excelFile'];

		var selectFunction = function(e) {
			if (e.files && e.files[0]) {
				// 엑셀 파일 선택시
				//$("#fileInfoDiv").text(e.files[0].name);
				// --------------------------------------------------------------------
				// 확장자 2중 체크 위치
				// --------------------------------------------------------------------
				// var imageExtension = e.files[0]['extension'].toLowerCase();
				// 전역변수에 선언한 허용 확장자와 비교해서 처리
				// itemMgmController.js 의 allowedImageExtensionList 참조

				//  켄도 이미지 업로드 확장자 검사
				if(!validateExtension(e)) {
					fnKendoMessage({
						message : '허용되지 않는 확장자 입니다.',
						ok : function(e) {}
					});
					return;
				}

				gFileTagId = e.sender.element[0].id;
				let reader = new FileReader();

				reader.onload = function(ele) {
					var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
					gFile = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

//              fnExcelUpload(file, fileTagId);

					// 파일 업로드
					fnMealUploadRun();
				};

				reader.readAsDataURL(e.files[0].rawFile);


			} // End of if (e.files && e.files[0])
		} // End of var selectFunction = function(e)

		for (var i = 0; i < uploadFileTagIdList.length; i++) {
			fnKendoUpload({
				id : uploadFileTagIdList[i]
				, select : selectFunction
			});
		} // End of for (var i = 0; i < uploadFileTagIdList.length; i++)


	}

	// ==========================================================================
	// # 파일업로드-validateExtension
	// ==========================================================================
	function validateExtension(e) {

		var allowedExt = '';
		var ext = e.files[0].extension;
		var $el = e.sender.element;

		if( !$el.length ) return;

		if( $el[0].accept && $el[0].accept.length ) {
			// 공백 제거
			allowedExt = $el[0].accept.replace(/\s/g, '');
			allowedExt = allowedExt.split(',');
		} else {
			allowedExt = allowedImageExtensionList;
		}

		return allowedExt.includes(ext);
	}

	// 업로드 파일첨부 버튼 클릭
	function fnExcelUpload(file, fileTagId) {

		var formData = new FormData();
		formData.append('bannerImage', file);

		fnAjax({
			url         : '/admin/il/meal/mealContsExcelUpload'
			, params        : formData
			, type        : 'POST'
			, contentType : false
			, processData : false
			, async       : false
			, success     : function(data, resultcode) {

				let localMessage = "";
				if(data.failCount > 0){
					localMessage += "<BR>" + data.failMessage;
				}else{
					localMessage += "식단 일괄 업로드에 성공했습니다.";

				}
				gFile = "";

				fnKendoMessage({
					message : localMessage,
					ok : function(e) {
						fnSearch();
					}
				});
			}
		});
	}

    // 식단 컨텐츠 미리보기
    function fnMealReview(ilGoodsDailyMealContsCd){

		let url = CUR_SERVER_URL + "/shop/modals/diet?id="+ilGoodsDailyMealContsCd;
		$("#mealContsPreview").attr('src',url);
		fnKendoInputPoup({id:"kendoPopupContentsPreview",height:"900px" ,width:"1000px",title:{nullMsg :'컨텐츠 미리보기'} });

		$("#kendoPopupContentsPreview").data('kendoWindow').bind("activate", function(e){
			$("#kendoPopupContentsPreview").css('overflow','hidden');
		});
    }

    // 식단 컨텐츠 삭제
    function fnMealDelete(gridData){

		if(gridData.deletePossibleYn == 'Y'){
			fnKendoMessage({message : "해당 식단이 등록된 패턴이 존재할 경우 삭제가 불가합니다."});
		}else{
			fnKendoMessage({
				type : "confirm",
				message : "삭제하시겠습니까?",
				ok : function() {
					fnAjax({
						url : '/admin/il/meal/delMealConts',
						params : {
							ilGoodsDailyMealContsCd : gridData.ilGoodsDailyMealContsCd
						},
						success : function(data) {
							fnSearch();
						},
						isAction : 'select'
					});
				},
				cancel : function() {
					//fnClear();
				}
			});
		}
    }
    
    // 식단 컨텐츠 등록/수정 팝업 호출  
	function fnGridClick(map){
		fnKendoPopup({
			id: 'mealContsMgm',
			title: fnGetLangData({ nullMsg: '컨텐츠 등록/수정' }),
			param: { "ilGoodsDailyMealContsCd": map.ilGoodsDailyMealContsCd , "type" : "put"},
			src: '#/mealContsMgm',
			width: '80%',
			height: '90%',
			success: function(id, data) {
				fnSearch();
			},
			error : function(){
				//openPopup = false;
			},
		});
	}

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				$('#inputForm').bindingForm(data, "rows", true);

				$(".supplierCompany-box__list__item").remove();

				fnKendoInputPoup({height:"1300px" ,width:"600px",title:{key :"5876",nullMsg :'컨텐츠 수정' } });

				break;
			case 'insert':
			case 'update':
				fnKendoMessage({
						message:"저장되었습니다.",
						ok:function(){
							fnSearch();
							fnClose();
						}
				});
			break;

		}
	}


	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function( ) {	fnSearch();	};
	/** Common Clear*/
	$scope.fnClear =function(){	 fnClear();	};
	/** Common New*/
	$scope.fnNew = function( ){	fnNew();};
	/** Common Save*/
	$scope.fnSave = function(){	 fnSave();};
	/** Common Close*/
	$scope.fnClose = function( ){  fnClose();};

	$scope.fnExcelDown = function( ){  fnExcelDown();};

	$scope.fnMealSampleDownload = function( ){  fnMealSampleDownload();};

	$scope.fnMealUpload = function() {$('#excelFile').trigger('click');};





	//마스터코드값 입력제한 - 숫자 & -
	fnInputValidationByRegexp("accountTelephone1", /[^0-9]/g);
	fnInputValidationByRegexp("accountTelephone2", /[^0-9]/g);
	fnInputValidationByRegexp("accountTelephone3", /[^0-9]/g);
	fnInputValidationByRegexp("receiverZipCode", /[^0-9]/g);
	fnInputValidationByRegexp("limitCount", /[^0-9]/g);
	//fnInputValidationLimitSpecialCharacter("inputWarehouseName");
	fnInputValidationByRegexp("warehouseTelephone1", /[^0-9]/g);
	fnInputValidationByRegexp("warehouseTelephone2", /[^0-9]/g);
	fnInputValidationByRegexp("warehouseTelephone3", /[^0-9]/g);
	fnInputValidationByRegexp("dawnLimitCnt", /[^0-9]/g);
	fnInputValidationByRegexp("storeLimitCnt", /[^0-9]/g);


	// 입력가능 : 한글 & 영어(대,소) & 숫자 & 특수문자  []~!@#$%^&*()_+|<>?:{}
	fnInputValidationByRegexp("inputWarehouseName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);
	fnInputValidationByRegexp("inputCompanyName", /[^ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z0-9\{\}\[\]\/?.,;:|\)*~`!^\-_+ <>@\#$%&\'\"\\\(\=]/g);


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
