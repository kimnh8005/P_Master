/**-----------------------------------------------------------------------------
 * system 			 : 모바일 푸시 발송 (전체)
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.03		jg              최초생성
 * @
 * **/
"use strict";
var aGridOpt, aGrid;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

    // sheetJs 스크립트 추가
    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : "true" });

		fnPageInfo({
			PG_ID  : 'sendPushAll',
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnTranslate();	// 다국어 변환 --------------------------------------------
		fnInitButton();	// Initialize Button  ---------------------------------
		fnInitGrid(); // Initialize Grid ------------------------------------
		fnInitOptionBox(); // Initialize Option Box ------------------------------------

	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnClear, #fnExcelUploadUser, #fnSampleFormDownload, #fnAddPushIssueAll, #fnImageUpload").kendoButton();
	};

	//--------------------------------- Button End---------------------------------

    //------------------------------- Grid Start -------------------------------

	// 그리드 초기화
	function fnInitGrid(){

        aGridOpt = {
            columns   : [
                          {field : 'no'		, title : 'No.'		, width : '20px', attributes : {style : 'text-align:center'}}
                        , {field : 'userId'	, title : '회원ID'	, width : '60px', attributes : {style : 'text-align:center'}}
            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
    };

	//-------------------------------  Grid End  -------------------------------

	//-------------------------------  Common Function start -------------------------------

	// 옵션 초기화
	function fnInitOptionBox() {

	    // 업로드 회원 팝업
	    $("#uploadUserPopup").kendoWindow({
            width : 450,
            height : 500,
            title : "업로드 회원",
            visible : false,
            modal : true
        });

	    // 발송그룹 라디오
		fnTagMkRadio({
				id    :  "sendGroup",
	            data  : [ { "CODE" : "APP_OS_TYPE.ALL", "NAME":"전체"}
//                        , { "CODE" : "UPLOAD_USER", "NAME":"업로드 회원"} ],
                        , { "CODE" : "APP_OS_TYPE.IOS", "NAME":"iOS"}
                        , { "CODE" : "APP_OS_TYPE.ANDROID", "NAME":"Android"} ],
				tagId : "sendGroup",
				chkVal: 'APP_OS_TYPE.ALL',
				style : {},
				change : function(e){
					let sendGroupValue = $("#sendGroup").getRadioVal();

					if( sendGroupValue == "APP_OS_TYPE.ALL" ){
//					    $("#uploadUserRegistrationViewControl").hide();
//					    $("#uploadUserDelete").trigger("click");
					    $("#pushTitleAndroidView").show();
						$("#pushTitleIosView").show();
					}else if( sendGroupValue == "APP_OS_TYPE.ANDROID" ){
						$("#pushTitleAndroidView").show();
						$("#pushTitleIosView").hide();
					}else if( sendGroupValue == "APP_OS_TYPE.IOS" ){
						$("#pushTitleAndroidView").hide();
						$("#pushTitleIosView").show();
					}
//					}else{
//					    $("#uploadUserRegistrationViewControl").show();
//					}
				}
		});

        // 업로드 회원등록 삭제
        $("#uploadUserDelete").on("click", function(e){
            e.preventDefault();
            $("#uploadUserViewControl").hide();
            $("#uploadUserLink").text("");
            $("#aGrid").data("kendoGrid").dataSource.data( [] );
        });

        // 업로드 회원등록 링크 클릭
        $("#uploadUserLink").on("click", function(e){
            $("#uploadUserPopup").data("kendoWindow").center().open();
        });

        // 광고/공지타입
        fnKendoDropDownList({
            id  : 'advertAndNoticeType',
            url : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "PUSH_SEND_TYPE"},
            blank : "선택"
        });

        // 발송구분
        fnTagMkRadio({
            id: "sendClassification",
            tagId : "sendClassification",
            data  : [
                      { "CODE" : "N" , "NAME" : "즉시발송"}
                    , { "CODE" : "Y" , "NAME" : "예약발송"}
                    ],
            chkVal: "N",
            change : function(e){

                if($("#sendClassification").getRadioVal() == "N"){
                    $("#reservationDateViewControl").hide();
                    $("#reservationDate").attr("required" , false);
                }else{
                    $("#reservationDateViewControl").show();
                    $("#reservationDate").attr("required" , true);
                }
            }
        });

        // 발송구분 예약날짜
        $("#reservationDate").kendoDateTimePicker({
            format : "yyyy-MM-dd HH:mm",
            value : fnGetToday("yyyy-MM-dd HH:mm"),
            max : fnGetDayAdd(fnGetToday(), 6, 'yyyy-MM-dd') + " 23:59",
            min : fnGetToday("yyyy-MM-dd HH:mm")
        });

        // 푸시발송타입
        fnTagMkRadio({
            id    : "pushSendType",
            tagId : "pushSendType",
            url   : "/admin/comn/getCodeList",
            params : {"stCommonCodeMasterCode" : "APP_PUSH_SEND_TYPE", "useYn" :"Y"},
            async : false,
            chkVal : "APP_PUSH_SEND_TYPE.TEXT_IMAGE",
            change : function(e){
                let pushSendTypeValue = $('#pushSendType').getRadioVal();
                if( pushSendTypeValue == "APP_PUSH_SEND_TYPE.TEXT_IMAGE"){
                    $("#imageViewControl").show();
                }else{
                    $("#imageViewControl").hide();
                }
            }
        });

        // 이미지 파일첨부
        fnKendoUpload({
            id     : "imageFile",
            select : function(e){
                let f = e.files[0];
                let ext = f.extension.substring(1, f.extension.length);

                if($.inArray(ext.toLowerCase(), ["gif","ico","jpeg","jpg","png"]) == -1){
                    fnKendoMessage({message : "이미지파일만 첨부가능합니다."});
                    e.preventDefault();
                }else{
                    if (typeof(window.FileReader) == "undefined"){
                        $("#basicImage").attr("src", e.sender.element.val());
                    } else {
                        if(f){
                            var reader = new FileReader();
                            reader.readAsDataURL(f.rawFile);

                            reader.onload = function (ele) {
                                $("#basicImage").attr("src", ele.target.result);
                            };
                        }
                    }
                }
            },
            localization : "파일첨부"
        });

        // 이미지 변경
        $("#imageChange").on("click", function() {
            $("#imageFile").trigger("click");
        });

        // 이미지 삭제
        $("#imageDelete").on("click", function(e){
            e.preventDefault();
            $("#basicImage").attr("src","/contents/images/noimg.png");
            $("#imageFile").data("kendoUpload").clearAllFiles();
        });

	};

    // 이미지 첨부파일 유무
    function fnImageYn(){
        if( $("#imageFile").data("kendoUpload").getFiles().length == 0 ){
            return false;
        }else{
            return true;
        }
    };

    // 이미지 파일첨부 버튼 클릭
    function fnImageUpload(){
        $("#imageFile").trigger("click");
    };

    // 업로드 회원등록 버튼 클릭
    function fnExcelUploadUser(){
        $("#uploadUserFile").trigger("click");
    };

	// 샘플다운로드 버튼 클릭
	function fnSampleFormDownload(){
        document.location.href = "/contents/excelsample/usermessage/sendPushSample.xlsx"
	};

	// 초기화
	function fnClear(){
	    $("#inputForm").formClear(true);
	    $("input:radio[name='sendGroup']").eq(0).prop("checked", true);
	    $("input:radio[name='sendClassification']").eq(0).prop("checked", true);
        $("input:radio[name='pushSendType']").eq(0).prop("checked", true);
        $("#uploadUserDelete").trigger("click");
        $("#imageDelete").trigger("click");
	};


	function fnAddPushIssueAll(){
        let url  = "/admin/sn/push/addPushIssueAll";

        let sendGroupValue = $("#sendGroup").getRadioVal();
		if( sendGroupValue == "APP_OS_TYPE.ANDROID" ){
			$("#pushTitleIos").val('∀');
		}else if( sendGroupValue == "APP_OS_TYPE.IOS" ){
			$("#pushTitleAndroid").val('∀');
		}
        let inputFormValidator = $("#inputForm").kendoValidator().data("kendoValidator");

        if( fnDataValidation() == false ){
            return;
        }


        if( inputFormValidator.validate() ){
            fnKendoMessage({
                type    : "confirm",
                message : "푸시메시지를 발송하시겠습니까?",
                ok      : function(e){
                              fnInputFormSubmit(url, "addPushIssueAll");
                },
                cancel  : function(e){  }
            });
        }
	};

	// 데이터 검증
	function fnDataValidation(){
//	    if( $("#sendGroup").getRadioVal() == "UPLOAD_USER" ){
//	        if( $("#aGrid").data("kendoGrid").dataSource.data().length == 0 ){
//	            fnKendoMessage({message : "업로드 회원을 등록해 주세요."});
//	            return false;
//	        }
//
//	        if( $("#aGrid").data("kendoGrid").dataSource.data().length > 1000 ){
//	            fnKendoMessage({message : "업로드 회원은 1000명 이상 선택하실 수 없습니다."});
//	            return false;
//	        }
//	    }

        if( $("#pushSendType").getRadioVal() == "APP_PUSH_SEND_TYPE.TEXT_IMAGE" && fnImageYn() == false ){
            fnKendoMessage({message : "이미지 파일첨부를 해주세요."});
            return false;
        }

	    return true;
	};

    // PUSH발송 submit
    function fnInputFormSubmit(url, cbId){
        let inputFormData = $("#inputForm").formSerialize(true);

        let sendGroupValue = $("#sendGroup").getRadioVal();

        if( sendGroupValue == "APP_OS_TYPE.ANDROID" ){
        	inputFormData.pushTitleIos= '';
		}else if( sendGroupValue == "APP_OS_TYPE.IOS" ){
			inputFormData.pushTitleAndroid = '';
		}

//        inputFormData['uploadUser'] = kendo.stringify( fnGetUserDataList() );

        if( fnImageYn() ){
            fnAjaxSubmit({
                form    : 'inputForm',
                fileUrl : '/fileUpload',
                url     : url,
                storageType : "public", // 추가
                domain : "ur", 			// 추가
                params  : inputFormData,
                success : function( successData ){
                	fnBizCallback(cbId, successData);
                },
                isAction : 'insert'
            });
        }else{
            fnAjax({
                url     : url,
                params  : inputFormData,
                success : function( successData ){
                	fnBizCallback(cbId, successData);
                },
                isAction : 'insert'
            });
        }
    };

    // 회원 목록 가져오기
//    function fnGetUserDataList(){
//        let userDataArray = [];
//        let aGridData = $("#aGrid").data("kendoGrid").dataSource.data();
//        let aGridCnt = aGridData.length;
//
//        if( aGridCnt > 0 && $("#sendGroup").getRadioVal() == "UPLOAD_USER" ){
//            for(let i = 0; i < aGridCnt; i++){
//                let userData = {};
//
//                userData.userId = aGridData[i].userId;
//                userDataArray[i] = userData;
//            }
//        }
//
//        return userDataArray;
//    };

    // 회원 엑셀 업로드 (SheetJs)
//    function excelExport(event) {
//
//        // Excel Data => Javascript Object 로 변환
//        var input = event.target;
//        var reader = new FileReader();
//
//        var fileName = event.target.files[0].name;
//
//        reader.onload = function() {
//            var fileData = reader.result;
//            var wb = XLSX.read(fileData, {
//                type : 'binary'
//            });
//
//            wb.SheetNames.forEach(function(sheetName) {
//                var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);
//
//                $("#aGrid").data("kendoGrid").dataSource.data(excelData);
//                $("#uploadUserViewControl").show();
//                $("#uploadUserLink").text(fileName);
//
//            })
//        };
//
//        reader.readAsBinaryString(input.files[0]);
//    }

    /**
     * 콜백합수
     */
    function fnBizCallback(id, data) {
        switch(id){
            case 'addPushIssueAll':
                fnKendoMessage({message : "발송하였습니다."});
                break;
        }
    };

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnClear =function(){ fnClear(); }; // 초기화
	$scope.fnExcelUploadUser =function(){ fnExcelUploadUser(); }; // 업로드 회원등록 파일첨부 버튼
	$scope.fnSampleFormDownload =function(){ fnSampleFormDownload(); }; // 샘플다운로드 버튼
	$scope.fnAddPushIssueAll =function(){ fnAddPushIssueAll(); }; // 보내기 버튼
//	$scope.fnExcelUpload = function(event) { excelExport(event);} // 엑셀 업로드 버튼
	$scope.fnImageUpload =function(){ fnImageUpload(); }; // 이미지 파일첨부 버튼


	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
