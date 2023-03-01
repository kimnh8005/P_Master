/**-----------------------------------------------------------------------------
 * description 		 : 회원그룹 상세설정
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.08.24		강윤경          최초생성
 * @ 2021.02.08		이원호          회원등급 갱신 반영
 * **/
'use strict';


var PAGE_SIZE = 10;
var aGridDs, aGridOpt, aGrid;
var pageParam = fnGetPageParam();

var viewModel;  // Kendo viewModel

$(document).ready(function() {
	const urGroupMasterId = pageParam.urGroupMasterId;

    /*
     * Kendo UI MVVM Pattern 적용
     */
    var viewModelOriginal = {

		/* 등급 쿠폰 */
		selectedCouponId : '', // Select 에서 선택한 쿠폰
		itemCouponCode : [], // 쿠폰 코드 목록
		itemCouponList : [], // 화면에 출력되는 쿠폰 목록

		itemCouponCodeMap : {}, // 쿠폰코드 Map :
		itemCouponValueMap : {}, // 쿠폰 세부 항목 Map :

		itemCouponDeleteList : [], // 쿠폰 삭제 목록

		/* 추천인 적립금 */
        selectedPointId : '', // Select 에서 선택한 적립금
        itemPointCode : [], // 적립금 코드 목록
        itemPointList : [], // 화면에 출력되는 적립금 목록

        itemPointCodeMap : {}, // 적립금코드 Map :
        itemPointValueMap : {}, // 적립금 세부 항목 Map :

        itemPointDeleteList : [], // 적립금 삭제 목록

		//입력/수정 모드 별 disabled 처리
		disabledGroupName : false,
		disabledGroupLevelType : false,
		disabledUserGroupType : false,
		disabledTopImageFile : false,
		disabledListImageFile : false,
		disabledPurchaseAmountFrom : false,
		disabledPurchaseCountFrom : false,
		disabledCoupon : false,
		disabledPoint : false,

		//쿠폰설정 start
		//쿠폰 추가
		fnAddItemCoupon : function(e) {
	        var selectedCouponId = this.get('selectedCouponId');

	        if (selectedCouponId) { // 유효한  ID 선택시
	        	/* 중복 등록 가능하게 수정요청 by 기획 20200909 Validation 체크 : 이미 추가한 항목이거나 이미 최대 등록가능 건수인 경우 return
                if (fnValidationCheck('addItemCoupon', null) == false) {
                    return;
                }
                */

                var itemCouponCodeObj = this.get('itemCouponCodeMap')[selectedCouponId];
                var itemCouponValueObj = this.get('itemCouponValueMap')[selectedCouponId];

                //노출 data
                var couponName = itemCouponCodeObj['displayCouponName'];	//쿠폰명
                var validityDate = itemCouponCodeObj['validityDate'];		//사용기간(쿠폰: 유효기간)

                this.get('itemCouponList').push({
                	pmCouponId : selectedCouponId,
                    couponName : couponName,
                    validityDate : validityDate
                });

                viewModel.set('selectedCouponId','');
	        }
		},

		//쿠폰 삭제
		fnRemoveItemCoupon : function(e) { // 삭제 버튼을 클릭한 행 삭제
            var index = this.get("itemCouponList").indexOf(e.data); // e.data : "삭제" 버튼을 클릭한 행의  세부항목
            this.get("itemCouponList").splice(index, 1); // viewModel 에서 삭제

            //삭제 array 관리
			if(e.data.urGroupBenefitId) {
				this.get('itemCouponDeleteList').push({urGroupBenefitId:e.data.urGroupBenefitId})
			}
        },
		//쿠폰설정 end

        //적립금설정 start
		//적립금 추가
		fnAddItemPoint : function(e) {
	        var selectedPointId = this.get('selectedPointId');

	        if (selectedPointId) { // 유효한  ID 선택시
	        	/* 중복 등록 가능하게 수정요청 by 기획 20200909 Validation 체크 : 이미 추가한 항목이거나 이미 최대 등록가능 건수인 경우 return
                if (fnValidationCheck('addItemCoupon', null) == false) {
                    return;
                }
                */

                var itemPointCodeObj = this.get('itemPointCodeMap')[selectedPointId];
                var itemPointValueObj = this.get('itemPointValueMap')[selectedPointId];

                //노출 data
                var pointName = itemPointCodeObj['pointName'];	//쿠폰명
                var pointDetailTypeName = itemPointCodeObj['pointDetailTypeName'];
                var issueValue = itemPointCodeObj['issueValue'] + '원';
                var validityDate = itemPointCodeObj['validityDate'];		//사용기간(쿠폰: 유효기간)

                this.get('itemPointList').push({
                	pmPointId : selectedPointId,
                	pointDetailTypeName : pointDetailTypeName,
                    pointName : pointName,
                    issueValue : issueValue,
                    validityDate : validityDate
                });

                viewModel.set('selectedPointId','');
	        }
		},

		//적립금 삭제
		fnRemoveItemPoint : function(e) { // 삭제 버튼을 클릭한 행 삭제
            var index = this.get("itemPointList").indexOf(e.data); // e.data : "삭제" 버튼을 클릭한 행의  세부항목
            this.get("itemPointList").splice(index, 1); // viewModel 에서 삭제

            //삭제 array 관리
			if(e.data.urGroupBenefitId) {
				this.get('itemPointDeleteList').push({urGroupBenefitId:e.data.urGroupBenefitId})
			}
        }
		//쿠폰설정 end

	};

	//viewModelOriginal 을 deepClone 한 객체를 viewModel 로 세팅
	viewModel = kendo.observable(viewModelOriginal);


	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'userGroupDetail',
			callback : fnUI
		});
	}

	function fnUI(){

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnInitSetting();

		fnSearch();

	}
	//초기 기본 세팅
	function fnInitSetting() {

        kendo.bind($("#kendoPopup"), viewModel);

        if(pageParam.editMode == "EDIT") {
            $("#fnConfirm").css("display", "none");
            $("#fnPopSave").css("display", "");
            $("#fnClose").css("display", "");
        }else{
            $("#fnConfirm").css("display", "");
            $("#fnPopSave").css("display", "none");
            $("#fnClose").css("display", "none");
        }
	}

	//kendo template
    function deepClone(obj) {
        if(obj === null || typeof obj !== 'object') {
          return obj;
        }

        const result = Array.isArray(obj) ? [] : {};

        for(let key of Object.keys(obj)) {
          result[key] = deepClone(obj[key])
        }

        return result;
    }

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnNew, #fnBack, #fnPopSave, #fnClose, #fnAddCoupon ').kendoButton();

		if(pageParam.editMode == "VIEW") {
		    $("button[id=fnNew]").each(function() {$(this).attr("disabled", "disabled");});
		}
	}

	//데이터 조회
	function fnSearch(){
		var data = {urGroupMasterId:urGroupMasterId};
		aGridDs.query( data );
	}

	//초기 처리
	function fnClear() {
		//초기화
		aGrid.clearSelection();
		$("#topImageFile").data("kendoUpload").clearAllFiles();
		$("#listImageFile").data("kendoUpload").clearAllFiles();

		$('.fileText').html(''); //파일 텍스트
		viewModel.set('itemCouponList', []);		//쿠폰리스트
		viewModel.set('itemCouponDeleteList', []);	//쿠폰삭제리스트
		viewModel.set('itemPointList', []);		    //적립금리스트
        viewModel.set('itemPointDeleteList', []);	//적립금삭제리스트

		//dieabled 처리
		var type = false;

		if(pageParam.editMode == "VIEW") {
            type = true;
            $("button[id=fnNew]").each(function() {$(this).attr("disabled", "disabled");});
		}
		viewModel.set("disabledGroupName", type);
        viewModel.set("disabledGroupLevelType", type);
        viewModel.set("disabledUserGroupType", type);
        viewModel.set("disabledTopImageFile", type);
        viewModel.set("disabledListImageFile", type);
        viewModel.set("disabledPurchaseAmountFrom", type);
        viewModel.set("disabledPurchaseCountFrom", type);
        //쿠폰 그리드 버튼 활성화 처리
        viewModel.set("disabledCoupon", type);
        viewModel.set("disabledPoint", type);
	}

	//등급 추가 신규등록
	function fnNew(){

		$('#popInputForm').formClear(true);

		fnClear();

		fnKendoInputPoup({height:"624px" ,width:"1100px",title:{key :"4368",nullMsg :'회원등급 설정' } });
	}

	//팝업 등록/수정
	function fnPopSave(){
		var url  = '/admin/ur/userGroup/addUserGroup';
		var cbId = 'insert';

		if( OPER_TP_CODE == 'U' ){
			url  = '/admin/ur/userGroup/putUserGroup';
			cbId= 'update';
		}

		var data = $('#popInputForm').formSerialize(true);
		
		var addItemParam = {
			addItemCouponList : fnAddItemCouponList(),	// 쿠폰 목록
			delItemCouponList : fnDelItemCouponList(),	// 쿠폰 삭제 목록
			addItemPointList : fnAddItemPointList(),	// 적립금 목록
            delItemPointList : fnDelItemPointList(),	// 적립금 삭제 목록
		}
		data = {...data,...addItemParam }

		data.urGroupMasterId = urGroupMasterId;

		if( data.rtnValid ){
		    if($('#groupLevelType').data('kendoDropDownList').value() == ''){
		        fnKendoMessage({message : '회원레벨을 선택해 주세요.'});
                return;
            }

			if($("#topImageFile").data("kendoUpload").getFiles().length == 0 && OPER_TP_CODE != 'U' ) {
				fnKendoMessage({message : '등급아이콘 (상단)을 확인해 주세요.'});
				return;
			}
			if($("#listImageFile").data("kendoUpload").getFiles().length == 0 && OPER_TP_CODE != 'U') {
				fnKendoMessage({message : '등급아이콘 (리스트)을 확인해 주세요.'});
				return;
			}

			fnAjaxSubmit({
	            form : 'popInputForm',
	            fileUrl : '/fileUpload',
	            storageType : "public", // 추가
	            domain : "ur", // 추가
	            url : url,
	            params : data,
	            contentType : 'application/json',
	            success : function(successData) {
	                fnBizCallback(cbId , successData);
	            },
	            isAction : 'batch'
	        });

		}
	}

	//close
	function fnClose(){
		var kendoWindow =$('#kendoPopup').data('kendoWindow');
		kendoWindow.close();
	}

	//뒤로
	function fnBack(){
		location.href="#/userGroup";
	}

	// validation 체크 : 유효하지 않은 경우 false 반환
    function fnValidationCheck(functionName, data) {
        switch (functionName) {
        case 'addItemCoupon': // 쿠폰 추가 버튼 클릭
            // 쿠폰 Select 에서 선택한 쿠폰 ID
            var selectedCouponId = viewModel.get('selectedCouponId');
            // 화면상에 출력된  목록
            var itemCouponList = viewModel.get('itemCouponList');

            // 쿠폰 Select 에서 선택한 쿠폰 ID 에 해당하는  정보가 itemCouponList 에 이미 있는지 확인
            for (var i = itemCouponList.length - 1; i >= 0; i--) {
                if (itemCouponList[i]['pmCouponId'] == selectedCouponId) { // 이미 추가된  경우
                	fnKendoMessage({message:'이미 추가한 항목입니다.'});
                    return false;
                }
            }
            break;
        case 'addItemPoint': // 적립금 추가 버튼 클릭
            // 적립금 Select 에서 선택한 적립금 ID
            var selectedPointId = viewModel.get('selectedPointId');
            // 화면상에 출력된  목록
            var itemPointList = viewModel.get('itemPointList');

            // 적립금 Select 에서 선택한 적립금 ID 에 해당하는  정보가 itemPointList 에 이미 있는지 확인
            for (var i = itemPointList.length - 1; i >= 0; i--) {
                if (itemPointList[i]['pmPointId'] == selectedPointId) { // 이미 추가된  경우
                    fnKendoMessage({message:'이미 추가한 항목입니다.'});
                    return false;
                }
            }
            break;

        } // end of switch

    }

	// 저장할 쿠폰 목록 return
	function fnAddItemCouponList() {
        var addItemCouponList = [];
        if (viewModel.get('itemCouponList') != null && viewModel.get('itemCouponList').length > 0) {
            for (var i = 0; i < viewModel.get('itemCouponList').length; i++) {
                var itemCouponInfo = viewModel.get('itemCouponList')[i]; // 화면상에 추가된쿠폰
                addItemCouponList.push({
                	benefitRelationId : itemCouponInfo['pmCouponId'],
                	urGroupBenefitId: itemCouponInfo['urGroupBenefitId'] ? itemCouponInfo['urGroupBenefitId']  :''
                });
            }
        }
        return addItemCouponList;
    }

	// 삭제할 쿠폰 목록 return
	function fnDelItemCouponList() {
        var delItemCouponList = [];
        if (viewModel.get('itemCouponDeleteList') != null && viewModel.get('itemCouponDeleteList').length > 0) {
            for (var i = 0; i < viewModel.get('itemCouponDeleteList').length; i++) {
                var itemCouponInfo = viewModel.get('itemCouponDeleteList')[i]; // 화면상에 추가된쿠폰
                delItemCouponList.push({
                	urGroupBenefitId : itemCouponInfo['urGroupBenefitId']
                });
            }
        }
        return delItemCouponList;
    }

    // 저장할 적립금 목록 return
	function fnAddItemPointList() {
        var addItemPointList = [];
        if (viewModel.get('itemPointList') != null && viewModel.get('itemPointList').length > 0) {
            for (var i = 0; i < viewModel.get('itemPointList').length; i++) {
                var itemPointInfo = viewModel.get('itemPointList')[i]; // 화면상에 추가된 적립금
                addItemPointList.push({
                	benefitRelationId : itemPointInfo['pmPointId'],
                	urGroupBenefitId: itemPointInfo['urGroupBenefitId'] ? itemPointInfo['urGroupBenefitId']  :''
                });
            }
        }
        return addItemPointList;
    }

	// 삭제할 적립금 목록 return
	function fnDelItemPointList() {
        var delItemPointList = [];
        if (viewModel.get('itemPointDeleteList') != null && viewModel.get('itemPointDeleteList').length > 0) {
            for (var i = 0; i < viewModel.get('itemPointDeleteList').length; i++) {
                var itemPointInfo = viewModel.get('itemPointDeleteList')[i]; // 화면상에 추가된 적립금
                delItemPointList.push({
                	urGroupBenefitId : itemPointInfo['urGroupBenefitId']
                });
            }
        }
        return delItemPointList;
    }

	//수정 모드 설정
	function fnUpdateModeSet(data) {
		fnClear();

		//fileImage
//    	$('.k-upload-sync').addClass("k-upload-empty");
    	$(".topImageFile").html(data.rows.topImageOriginalName);
    	$(".listImageFile").html(data.rows.listImageOriginalName);

		//쿠폰 설정
		var array = data.rows.addItemCouponList;

		for(var i = 0; i < array.length; i++) {
			//노출 data
			var selectedCouponId = array[i].benefitRelationId;
            var couponName = array[i].displayCouponName;	//쿠폰명
            var validityDate = array[i].validityDate;				//사용기간

            viewModel.get('itemCouponList').push({
                pmCouponId : selectedCouponId,
                couponName : couponName,
                validityDate : validityDate,
                urGroupBenefitId: array[i].urGroupBenefitId
            });
		}

        //쿠폰 그리드 버튼 활성화 처리
        if(pageParam.editMode == "VIEW") {
            $("button[id=btnRemoveItemCoupon]").each(function() {$(this).attr("disabled", "disabled");});
            $("button[id=btnRemoveItemPoint]").each(function() {$(this).attr("disabled", "disabled");});
        }

        //적립금 설정
		var array = data.rows.addItemPointList;

		for(var i = 0; i < array.length; i++) {
		    //노출 data
            var selectedPointId = array[i].benefitRelationId;
            var pointName = array[i].pointName;	        //적립금명
            var pointDetailTypeName = array[i].pointDetailTypeName;
            var issueValue = array[i].issueValue + '원';
            var validityDate = array[i].validityDate;	//사용기간

            viewModel.get('itemPointList').push({
                pmPointId : selectedPointId,
                pointDetailTypeName : pointDetailTypeName,
                pointName : pointName,
                issueValue : issueValue,
                validityDate : validityDate,
                urGroupBenefitId: array[i].urGroupBenefitId
            });
		}

        //쿠폰 그리드 버튼 활성화 처리
        if(pageParam.editMode == "VIEW") {
            $("button[id=btnRemoveItemPoint]").each(function() {$(this).attr("disabled", "disabled");});
        }

	}

	//--------------------------------- 파일 업로드---------------------------------
	function fnFilecheck(e, id){
		let f = e.files[0];
        let ext = f.extension.substring(1, f.extension.length);

        if ($.inArray(ext.toLowerCase(), ["jpg", "jpeg", "png" ]) == -1 ){
            fnKendoMessage({
            	message : "jpg jpeg png 파일만 첨부가능합니다."
            });
            e.preventDefault();
            return false;
        } else {
    	  if(f.size <= 200000){
            if (e.files && e.files[0]) {
                let reader = new FileReader();

                reader.onload = function(ele) {
                	$('.k-upload-sync').addClass("k-upload-empty");
                	$("."+id).html(e.files[0].name);
                };

                reader.readAsDataURL(f.rawFile);
            }
    	  } else {
      		fnKendoMessage({
                  message : "이미지는 200kb까지 첨부할 수 있습니다."
				});
				e.preventDefault();
      		}
        }
	}

	//등급아이콘 상단
    fnKendoUpload({
        id : "topImageFile",
        select : function(e) {
        	fnFilecheck(e, "topImageFile");
        },
        localization : "파일등록"
    });

    //등급아이콘 리스트
    fnKendoUpload({
        id : "listImageFile",
        select : function(e) {
        	fnFilecheck(e, "listImageFile");
        },
        localization : "파일등록"
    });

	//--------------------------------- Button End---------------------------------

	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/userGroup/getUserGroupList',
			requestEnd : function(e){
                if(e.response.data.userGroupPageInfo != ""){
                    $('#topForm [id="groupMasterName"]').text(e.response.data.userGroupPageInfo.groupMasterName);
                    $('#topForm [id="calculatePeriod"]').text(e.response.data.userGroupPageInfo.calculatePeriod + "개월");
                    $('#topForm [id="startDate"]').text(e.response.data.userGroupPageInfo.startDate);
                    $('#topForm [id="endDate"]').text(e.response.data.userGroupPageInfo.endDate);
                }
			}

		});
		aGridOpt = {
				dataSource: aGridDs
			,	navigatable: true
			,	columns   : [
					 { field:'urGroupId'	,hidden:true}
					,{ field:'defaultYn'	,title : '기본등급'		, width:'40px'	,attributes:{ style:'text-align:center' },
					    template : function(dataItem){
					        var returnValue = "<input type='radio' class='aGridRadioButton' name='aGridRadio' kind='radio' ";
					        if(pageParam.editMode == "EDIT"){
					            if(dataItem.defaultYn == "Y"){
                                    returnValue += " checked = true/>";
                                }else{
                                    returnValue += " />";
                                }
                            }else{
                                if(dataItem.defaultYn == "Y"){
                                    returnValue += " checked = true disabled='disabled'/>";
                                }else{
                                    returnValue += " disabled='disabled'/>";
                                }
                            }

                            return returnValue;
					    }
					}
					,{ field:'groupName	'	,title : '등급명'		, width:'30px'	,attributes:{ style:'text-align:center' }}
					,{ field:'groupLevelTypeName'		,title : '회원 레벨'		, width:'60px'	,attributes:{ style:'text-align:center' }}
					,{ title : '매출 조건'	, width:'80px'	,attributes:{ style:'text-align:center' }
                        ,template:  function(dataItem){
                            return fnNumberWithCommas(dataItem.purchaseAmountFrom) + '원 이상 ~ ' + fnNumberWithCommas(dataItem.purchaseAmountTo) + '원 미만';
                        }}
					,{ title : '구매 조건'	, width:'80px'	,attributes:{ style:'text-align:center' }
						,template:  function(dataItem){
							return fnNumberWithCommas(dataItem.purchaseCountFrom) + '건 이상 ~ ' + fnNumberWithCommas(dataItem.purchaseCountTo) + '건 미만';
						}}
					,{ field : "management", title : "관리", width : "40px", attributes : {style : "text-align:center"},
			            template : function(dataItem){
			                var button = "";
			                if(pageParam.editMode == "EDIT"){
			                    button = "<a href='#' role='button' class='btn-s btn-blue middle' kind='manage' >관리</a>";
                                button += "<a href='#' role='button' class='btn-s btn-red middle' kind='delete' >삭제</a>";
			                }else{
                                button = "<a href='#' role='button' class='fb-btn btn-gray inputForm__button' kind='manage' >보기</a>";
			                }
			                return button;
			             }}
				],
				noRecordMsg : fnGetLangData({ nullMsg : "등록된 등급 정보가 없습니다." }),
			};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

        // 관리
        $('#aGrid').on("click", "a[kind=manage]", function(e) {
            e.preventDefault();

            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            let param = {urGroupId : dataItem.urGroupId}

            fnAjax({
                url     : '/admin/ur/userGroup/getUserGroup',
                params  : param,
                success :
                    function( data ){
                        fnBizCallback('select',data);
                    },
                isAction : 'select'
            });

        });

        // 삭제
        $('#aGrid').on("click", "a[kind=delete]", function(e) {
            e.preventDefault();

            var dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));
            if(dataItem.defaultYn == "Y"){
                fnKendoMessage({ message:"기본등급은 삭제할 수 없습니다.", ok:function(){ } });
                return false;
            }

            fnKendoMessage({
                message:'삭제 하시겠습니까?',
                type : "confirm" ,
                ok : function(){
                    var data = {urGroupId:dataItem.urGroupId}
                    fnAjax({
                        url     : '/admin/ur/userGroup/delUserGroup',
                        params  : data,
                        success :
                            function( data ){
                                fnBizCallback('delete',data);
                            },
                        isAction : 'select'
                    });
                }
            })
        });

        // 라디오 버튼 클릭
		$('#aGrid').on("click", "input[kind=radio]", function(e) {
            var index = $(this).index();
            let dataItem = aGrid.dataItem($(e.currentTarget).closest("tr"));

            let param = {
                urGroupMasterId : urGroupMasterId,
                urGroupId : dataItem.urGroupId
                };

            fnAjax({
                url     : '/admin/ur/userGroup/putUserGroupDefaultYn',
                params  : param,
                success :
                    function( data ){
                        fnBizCallback('updateDefault',data);
                    },
                isAction : 'update'
            });

        });

	}


	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------


	function fnInitOptionBox(){
		$('#kendoPopup').kendoWindow({
			visible: false,
			modal: true
		});

		//회원레벨
        fnKendoDropDownList({
            id  : 'groupLevelType',
            tagId : 'groupLevelType',
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "GROUP_LEVEL_TP", "useYn" :"Y"},
            blank : '레벨 선택',
            async : false
        });

        //활성화 처리
        $("input[name=groupName]").each(function() {$(this).attr("data-bind", "disabled: disabledGroupName");});
		$("input[name=groupLevelType]").each(function() {$(this).attr("data-bind", "disabled: disabledUserGroupType");});
		$("input[name=topImageFile]").each(function() {$(this).attr("data-bind", "disabled: disabledTopImageFile");});
        $("input[name=listImageFile]").each(function() {$(this).attr("data-bind", "disabled: disabledListImageFile");});
        $("input[name=purchaseAmountFrom]").each(function() {$(this).attr("data-bind", "disabled: disabledPurchaseAmountFrom");});
        $("input[name=purchaseCountFrom]").each(function() {$(this).attr("data-bind", "disabled: disabledPurchaseCountFrom");});
        if(pageParam.editMode == "VIEW") {
            $("input[id=itemCoupon]").each(function() {$(this).attr("data-bind", "disabled: disabledCoupon");});
            $("button[id=fnAddItemCoupon]").each(function() {$(this).attr("data-bind", "disabled: disabledCoupon");});
            $("input[id=itemPoint]").each(function() {$(this).attr("data-bind", "disabled: disabledPoint");});
            $("button[id=fnAddItemPoint]").each(function() {$(this).attr("data-bind", "disabled: disabledPoint");});
        }

        // 쿠폰목록
		fnAjax({
            url : "/admin/comm/getPmCpnList",
            params : {},
            async : false,
            success : function(data, status, xhr) {
            	data = data.rows;
                fnKendoDropDownList({ // 상품정보 제공고시 : 상품군
                    id : 'itemCoupon',
                    data : data,
                    valueField : 'pmCouponId',
                    textField : 'displayCouponName',
                    blank : "쿠폰 선택"
                });

                viewModel.set('itemCouponCode', data);

                // 상품 인증정보 선택 Select 값 변경시 필터링이 편리하도록 Map ( Javascript Object ) 으로 변환
                var itemCouponCodeMap = {};

                for (var i = 0; i < data.length; i++) {
                	itemCouponCodeMap[data[i]['pmCouponId']] = data[i];
                }

                viewModel.set('itemCouponCodeMap', itemCouponCodeMap);
            },
            isAction : 'select'
        });

		// 적립금 목록
		fnAjax({
            url : "/admin/comm/getPmPointList",
            params : {},
            async : false,
            success : function(data, status, xhr) {
            	data = data.rows;
                fnKendoDropDownList({
                    id : 'itemPoint',
                    data : data,
                    valueField : 'pmPointId',
                    textField : 'pointName',
                    blank : "적립금 설정"
                });

                viewModel.set('itemPointCode', data);

                // 상품 인증정보 선택 Select 값 변경시 필터링이 편리하도록 Map ( Javascript Object ) 으로 변환
                var itemPointCodeMap = {};

                for (var i = 0; i < data.length; i++) {
                  	itemPointCodeMap[data[i]['pmPointId']] = data[i];
                }

                viewModel.set('itemPointCodeMap', itemPointCodeMap);
            },
            isAction : 'select'
        });
	}


	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){

			case 'select':
				//form data binding
				$('#popInputForm').bindingForm( {'rows':data.rows},'rows', true);
				fnUpdateModeSet(data);
				fnKendoInputPoup({height:"500px" ,width:"1100px",title:{key :"4368",nullMsg :'회원등급 설정' } });
				break;

			case 'insert':
				fnKendoMessage({
					message:"등록이 완료되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;


			case 'update':
				fnKendoMessage({
					message:"등록이 완료되었습니다.",
					ok:function(){
						fnSearch();
						fnClose();
					}
				});
				break;

			case 'updateDefault':
			    fnKendoMessage({
                    message:"기본등급으로 설정되었습니다.",
                    ok:function(){
                        fnSearch();
                    }
                });
                break;

			case 'delete':
				fnKendoMessage({
					message:"삭제 되었습니다.",
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
	$scope.fnSearch = function( ) {	fnSearch();	};	//상세 조회
	$scope.fnBack = function( ){	fnBack();};	//뒤로
	$scope.fnPopSave = function(){	 fnPopSave();};	//팝업 등록
	$scope.fnPopDel = function( ){	fnPopDel();};	//팝업 삭제
	$scope.fnNew = function( ){	fnNew();};	//팝업 등급 등록/수정
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	fnInputValidationForHangulAlphabetNumberSpace("groupMasterName");	// 한글 + 영문대소문자 + 숫자 + 빈칸
	fnInputValidationForAlphabetHangulSpace("groupName"); // 한글+영문대소문자
	fnInputValidationForNumber("groupLevel");	//숫자
	fnInputValidationForNumber("purchaseAmountFrom");	//숫자
	fnInputValidationForNumber("purchaseCountFrom");	//숫자

}); // document ready - END
