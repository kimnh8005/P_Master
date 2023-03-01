/**-----------------------------------------------------------------------------
 * system 		  : 사업자정보 관리
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.07.16		손진구          최초생성
 * @ 2020.08.13     손진구          QA 수정요청건 반영
 * @
 * **/
"use strict";

var newCreate = true;

$(document).ready(function() {
	fnInitialize();	//Initialize Page Call ---------------------------------

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit("fnIsMenu", { flag : true });

		fnPageInfo({
			PG_ID  : 'bizInfo',
			callback : fnUI
		});
	};

	// 화면 UI 초기화
	function fnUI(){

		fnInitButton();	// Initialize Button  ---------------------------------
		fnInitOptionBox(); // Initialize Option Box ------------------------------------
		fnDefaultValue(); // 등록된 사업자정보 조회
	};

	//--------------------------------- Button Start---------------------------------

	// 버튼 초기화
	function fnInitButton(){
		$("#fnSave").kendoButton();
	};

    // 저장
    function fnSave(){
        let paramData = $("#inputForm").formSerialize(true);

        if( paramData.rtnValid && fnInputValidation() ){

            if( newCreate ){

                fnAjaxSubmit({
                    form    : "inputForm",
                    fileUrl : "/fileUpload",
                    url     : "/admin/user/company/addBizInfo",
                    storageType : "public",
                    domain : "ur",
                    params  : paramData,
                    success : function( successData ){
                        fnKendoMessage({message : "저장이 완료되었습니다.", ok : fnDefaultValue});
                    },
                    isAction : "insert"
                });
            }else{
                if( $("#escrowImageFile").data("kendoUpload").getFiles().length == 0 ){
                    fnAjax({
                        url     : "/admin/user/company/putBizInfo",
                        params  : paramData,
                        success : function( successData ){
                            fnKendoMessage({message : "저장이 완료되었습니다.", ok : fnDefaultValue});
                        },
                        isAction : "insert"
                    });
                }else{
                    fnAjaxSubmit({
                        form    : "inputForm",
                        fileUrl : "/fileUpload",
                        url     : "/admin/user/company/putBizInfo",
                        storageType : "public",
                        domain : "ur",
                        params  : paramData,
                        success : function( successData ){
                            fnKendoMessage({message : "저장이 완료되었습니다.", ok : fnDefaultValue});
                        },
                        isAction : "insert"
                    });
                }
            }
        }
    };

	//--------------------------------- Button End---------------------------------


	//-------------------------------  Common Function start -------------------------------

	// 옵션 초기화
	function fnInitOptionBox() {

	    // 고객센터 점심시간 사용유무
        fnTagMkChkBox({
            id : "lunchTimeYnCheckBox",
            data : [ { "CODE" : "Y", "NAME" : "" } ],
            tagId : "lunchTimeYnCheckBox",
            change : function(e){
                if( $("input:checkbox[name=lunchTimeYnCheckBox]").is(":checked") ){
                    $("#lunchTimeYn").val("Y");
                }else{
                    $("#lunchTimeYn").val("N");
                }
            }
        });

        // 파일첨부
        fnKendoUpload({
            id     : "escrowImageFile",
            select : function(e){
                let f = e.files[0];
                let ext = f.extension.substring(1, f.extension.length);

                if($.inArray(ext.toLowerCase(), ["gif","jpg","png"]) == -1){
                    fnKendoMessage({message : "gif, jpg, png 이미지파일만 첨부가능합니다."});
                }else{
                    let reader = new FileReader();
                    reader.readAsDataURL(f.rawFile);

                    reader.onload = function(theFile) {

                        let escrowImage = new Image();
                        escrowImage.src = theFile.target.result;

                        escrowImage.onload = function() {

                            if( this.width < 40 || this.height < 40 ){
                                fnKendoMessage({message : "최소 이미지 크기는 40px x 40px 입니다."});
                                return;
                            }

                            $("#escrowCertificationInformation").val(f.name);
                        };
                    };
                }
            },
            localization : "파일찾기"
        });

        // 고객센터 운영시작시간
        fnkendoTimePicker({
        	id : "serviceCenterOperatorOpenTime",
        	format : "HH:mm",
        	dateInput : true
        });

        // 고객센터 운영종료시간
        fnkendoTimePicker({
        	id : "serviceCenterOperatorCloseTime",
            format : "HH:mm",
            dateInput : true
        });

        // 고객센터 점심시작시간
        fnkendoTimePicker({
        	id : "serviceCenterLunchTimeStart",
            format : "HH:mm",
            dateInput : true
        });

        // 고객센터 점심종료시간
        fnkendoTimePicker({
        	id : "serviceCenterLunchTimeEnd",
            format : "HH:mm",
            dateInput : true
        });

        // 입력제한
        fnInputValidationForAlphabetHangul("companyCeoName"); // 대표자명
        fnInputValidationForNumberBar("businessNumber"); // 사업자번호
        fnInputValidationForNumberBar("corporationNumber"); // 법인번호
        fnInputValidationForHangulNumberBar("mailOrderNumber"); // 통신판매업번호
        fnInputValidationForHangulNumberBar("healthFunctFoodReport"); // 건강기능식품신고
        fnInputValidationForHangulAlphabetNumberSpace("hostingProvider"); // 호스팅 제공자
        fnInputValidationForAlphabetNumberSpecialCharacters("representativeEmailInformation"); // 대표 Email정보
        fnInputValidationForNumberSpecialCharacters("serviceCenterPhoneNumber"); // 고객센터 전화번호
        fnInputValidationForAlphabetHangulSpace("securityDirector"); // 개인정보보호책임자
	};

	// 우편번호 팝업
	function fnAddressPopup(){
	    fnDaumPostcode("zipCode", "address", "detailAddress","");
	};

	// 에스크로 인증정보 파일찾기
	function fnEscrowImageFileSearch(){
	    $("#escrowImageFile").trigger("click");
	};

	// 입력값 검증
	function fnInputValidation(){
	    // 대표 Email 정보 체크
        if( !fnValidateEmail( $("#representativeEmailInformation").val() ) ){
            fnKendoMessage({ message : "이메일 입력값 오류",
                             ok : function(e) { $("#representativeEmailInformation").focus(); }
            });

            return false;
        }

        // 고객센터 전화번호 체크
/*
        if( fnValidateTel("serviceCenterPhoneNumber") ){
            fnKendoMessage({ message : "고객센터 전화번호 입력값 오류",
                             ok : function(e) { $("#serviceCenterPhoneNumber").focus(); }
            });

            return false;
        }
*/
        // 고객센터 운영시간 시작 체크
        if( !fnValidateHourMinute("serviceCenterOperatorOpenTime") ){
            fnKendoMessage({ message : "고객센터 운영시간 시작시간 입력값 오류",
                ok : function(e) { $("#serviceCenterOperatorOpenTime").focus(); }
            });

            return false;
        }

        // 고객센터 운영시간 종료 체크
        if( !fnValidateHourMinute("serviceCenterOperatorCloseTime") ){
            fnKendoMessage({ message : "고객센터 운영시간 종료시간 입력값 오류",
                             ok : function(e) { $("#serviceCenterOperatorCloseTime").focus(); }
            });

            return false;
        }

        // 고객센터 운영시간 시작, 종료 체크
        if( $("#serviceCenterOperatorOpenTime").val() >= $("#serviceCenterOperatorCloseTime").val() ){
            fnKendoMessage({ message : "고객센터 운영시간 시작시간이 종료시간보다 클수 없습니다.",
                ok : function(e) { $("#serviceCenterOperatorOpenTime").focus(); }
            });

            return false;
        }

        // 고객센터 점심시간 시작 체크
        if( !fnValidateHourMinute("serviceCenterLunchTimeStart") ){
            fnKendoMessage({ message : "고객센터 점심시간 시작시간 입력값 오류",
                             ok : function(e) { $("#serviceCenterLunchTimeStart").focus(); }
            });

            return false;
        }

        // 고객센터 점심시간 종료 체크
        if( !fnValidateHourMinute("serviceCenterLunchTimeEnd") ){
            fnKendoMessage({ message : "고객센터 점심시간 종료시간 입력값 오류",
                             ok : function(e) { $("#serviceCenterLunchTimeEnd").focus(); }
            });

            return false;
        }

        // 고객센터 점심시간 시작, 종료 체크
        if( $("#serviceCenterLunchTimeStart").val() >= $("#serviceCenterLunchTimeEnd").val() ){
            fnKendoMessage({ message : "고객센터 점심시간 시작시간이 종료시간보다 클수 없습니다.",
                ok : function(e) { $("#serviceCenterLunchTimeStart").focus(); }
            });

            return false;
        }

        // 에스크로 인증정보 체크
        if( newCreate ){
            if( $("#escrowImageFile").data("kendoUpload").getFiles().length == 0 ){
                fnKendoMessage({ message : "에스크로 인증정보 이미지를 선택해 주시기 바랍니다." });
                return false;
            }
        }else{
            if( !$("#escrowCertificationInformation").val() ){
                fnKendoMessage({ message : "에스크로 인증정보 이미지를 선택해 주시기 바랍니다." });
                return false;
            }
        }

	    return true;
	};

	// 등록된 사업자정보 조회
	function fnDefaultValue(){
        fnAjax({
            url     : "/admin/user/company/getBizInfo",
            method  : "GET",
            params  : {},
            success : function( data ){
                if( data.bizInfo ){

                    newCreate = false;
                    $("#inputForm").bindingForm(data, "bizInfo", true);
                    setLunchTimeYnCheckBox();
                }else{

                    newCreate = true;
                }
            },
            isAction : "select"
        });
	};

	// 고객센터 점심시간 체크박스 셋팅
	function setLunchTimeYnCheckBox(){
	    if( $("#lunchTimeYn").val() == "Y" ){
	        $("input:checkbox[name=lunchTimeYnCheckBox]").prop("checked", true);
	    }else{
	        $("input:checkbox[name=lunchTimeYnCheckBox]").prop("checked", false);
	    }
	};

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------

	$scope.fnSave =function(){ fnSave(); }; // 저장
	$scope.fnAddressPopup = function(){ fnAddressPopup(); };
	$scope.fnEscrowImageFileSearch = function(){ fnEscrowImageFileSearch(); };

	//------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
