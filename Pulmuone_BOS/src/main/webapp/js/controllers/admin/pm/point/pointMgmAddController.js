'use strict';

var pageParam = fnGetPageParam();

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var accountGridDs, accountGridOpt, accountGrid;
var userLevelGridDs, userLevelGridOpt, userLevelGrid;
var ticketGridDs, ticketGridOpt, ticketGrid;

var apprAdminGridDs, apprAdminGridOpt, apprAdminGrid;

var pointAdmin;
var userName;
var loginId;
var urUserId;

var userMaster;
var userGroup;

var viewModel;

var paramData;

if(defaultActivateTab == undefined){
	var defaultActivateTab;
}else{
	$('#urUserId').val(csData.urUserId);
	paramData = {"urUserId" : csData.urUserId};
}

const APPROVE_POINT_BY_SYSTEM_AMOUNT = 10000;
var autoApprCheck = false;

$(document).ready(function() {

    fnInitialize();

    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);

    pointAdmin = parent.POP_PARAM["parameter"].pointAdmin;
    userName = parent.POP_PARAM["parameter"].userName;
    loginId = parent.POP_PARAM["parameter"].loginId;
    urUserId = parent.POP_PARAM["parameter"].urUserId;

    $('#issueQtyLimitSelect').val(null);

	//유효기간 라디오버튼 제어
	$("input[name=validityType]").change(function() {
		var chkValue = $(this).val();
		if(chkValue == 'VALIDITY_TYPE.PERIOD'){
			$("#validityDate").data("kendoDatePicker").enable( true );
			$("#validityDay").attr("disabled", true);
			$('#validityDay').val('');
		}else{
			$("#validityDate").data("kendoDatePicker").enable( false );
			$("#validityDay").attr("disabled", false);
			$('#validityDate').val('');
		}
	});

	//승인 요청처리 여부
	$("#approvalCheckbox").click(function(){
		if($("#approvalCheckbox").prop("checked") == true){
			$('#approvalCheckbox').prop("checked",true);
			$('#apprDiv').show();
		}else{
			$('#approvalCheckbox').prop("checked",false);
			$('#apprDiv').hide();
		}
	});


	// 이용권
	// 지급수량제한 : 적립금 입력 시
	$("input[name=issueValue]").change(function() {

		fnGetBudget();

	});


	// 이용권
	// 지급수량제한 : 생성개수 입력 시
	$("input[name=issueQty]").change(function() {

		fnGetBudget();

	});

	//관리자 지급 유효일 변경 시 일자 노출
	$("input[name=pointPaymentAmount]").keyup(function() {
		var today = fnGetToday();
		$("#pointPaymentAmountToDate").data("kendoDatePicker").value(fnGetDayAdd(today, this.value*1));
	});

    function fnInitialize(){
        $scope.$emit('fnIsMenu', { flag : false });

        fnPageInfo({
              PG_ID    : 'pointMgmAdd'
            , callback : fnUI
        });
    }

	function fnUI(){

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitDefaultGrid();	//Initialize Grid ------------------------------------

		fnUserLevelInit();

		fnAccountUploadInit();   //엑셀 업로드 Grid

		fnTicketUploadInit();

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		fnUiSet();

		fnAprpAdminInit();

		fnPointAdminInit();		//회원 적립금 설정

		fnInitKendoUserUpload();

		fnInitKendoTicketUpload();

		// fnOrgSetInit();
	}


	//--------------------------------- Button Start---------------------------------
	function fnInitButton() {
		$('#fnSearch, #fnSave, #fnAddUserLevel, #fnSelectUser, #fnDeptPopupButton, #fnUseExcelUpload').kendoButton();

		$('#fnExcelUpload, #fnExcelTicketUpload, #fnSamepleFormDownload, #fnTicketSamepleFormDownload, #fnTicketExcelUpload').kendoButton();

		$('#fnSamepleFormDownload, #fnTicketSamepleFormDownload').kendoButton();

		$('#fnApprAdmin, #fnApprClear').kendoButton();

		$('#fnPointShortInfo').kendoButton();

		$('#fnUseExcelUpload, #fnSamepleFormDownload, #fnPointShortInfo').hide();
	}

	function fnClose(){
		parent.POP_PARAM = 'SAVE';
		parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
	}

	function fnInitDefaultGrid(){

		$("#pointTypeAutoDiv").hide();
		$("#adminPaymentDiv").hide();
		$("#adminPaymentDetailDiv").hide();
		$("#pointFeedbackDiv").hide();
		$("#userSettingDiv").hide();
		$("#provisionDeductionDiv").hide();
		$("#provisionStandardDiv").hide();
		$("#userGradeDiv").hide();
		$("#fnTicketExcelUpload").hide();
		$("#fnTicketSamepleFormDownload").hide();
		$("#pointPaymentAmountDiv").hide();
		$("#erpOrganizationDiv").show();
		$("#buyerDiv").hide();
		$("#buyerInfoDiv").show();
		$("#adminBuyerDiv").hide();
		$("#payMethodDiv").hide();
		$("#previewValidityDayDiv").hide();
		$("#issueQtyLimitSelect").attr("disabled", true);
	}

	function fnUiSet(){
		$("#validityDate").data("kendoDatePicker").enable( false );
		$("#fixSerialNumber").attr("disabled", true);

/*		$("#issueValue").kendoNumericTextBox({
			  format: "n0", spinners: false	 });*/

//	    $("#issueBudget").kendoNumericTextBox({
//			  format: "n0", spinners: false
//		 });
	    $("#normalAmount").kendoNumericTextBox({
			  format: "n0", spinners: false
		 });
	    $("#photoAmount").kendoNumericTextBox({
			  format: "n0", spinners: false
		 });
	    $("#premiumAmount").kendoNumericTextBox({
			  format: "n0", spinners: false
		 });



		// 분담조직 제어
		$("#erpOrganizationName").attr("disabled", true);


	    $('#uploadFileName').hide();
	    $('#uploadUserFileName').hide();

	    var dropDownList =$('#randNumTypeSelect').data('kendoDropDownList');
		dropDownList.value("SERIAL_NUMBER_TYPE.AUTO_CREATE");

		//승인관리자 정보
		$('#approvalCheckbox').prop("checked",true);
		$('#apprDiv').show();

		// 지급예산 예정금액 표시
//		$('#budget').text('제한없음');

		// 엑셀업로드 문구
		$('#excelCmt').hide();

	}


	function fnPointAdminInit(){

    	$("#toDateDiv").val(fnGetToday());

		if(pointAdmin == 'Y'){
			$("input:radio[name='pointType']").attr('disabled',true);
			$("#pointType_3").prop("checked",true);		//적립금 설정 선택
			$("#pointName").attr("disabled", true);		//적립금 명 비표시
			$("#fnDeptPopupButton").attr("disabled", true);		//분담조직 검색버튼 비표시

			$("#adminPaymentDiv").show();			// 적립구분
			$("#adminPaymentDetailDiv").show();		// 적립 상세 구분
			$("#pointDiv").show();					// 적립금
			$("#userSettingDiv").show();			// 회원설정
			$("#provisionDeductionDiv").show();		// 지급/차감 사유
			$("#pointTypeAutoDiv").hide();
			$("#issueDateDiv").hide();
//			$("#issueBudgetDiv").hide();
			$("#issueQtyLimitDiv").hide();
			$("#provisionStandardDiv").hide();
			$("#pointFeedbackDiv").hide();
			$("#userGradeDiv").hide();
			$("#validityDateDiv").hide();
			$("#issueQtyDiv").hide();
			$("#randomNumDiv").hide();
			$("#erpOrganizationDiv").hide();
			$("#payMethodDiv").hide();             // 적립금 설정 -> 관리자 지급/차감 -> 지급 방법
			$("#buyerDiv").show();
			$("#buyerInfoDiv").hide();
			$("#adminBuyerDiv").show();
			$("#userName").val(userName);
			$("#loginId").val(loginId);
			$('#totalBudget').hide();
			$('#budget').text('');


			fnAjax({
				url     : '/admin/promotion/pointHistory/getLoginInfo',
				params  : {},
				success :
					function( data ){
						$("#roleName").val(data.erpOrganizationName);									//분담조직 정보표시
						if(data.amount != null){
						    $("#roleAmount").val(data.amount.replace(/\B(?=(\d{3})+(?!\d))/g, ","));									//지급 가능 적립금
						}
						if(data.roleTotalPoint != null){
						    $("#roleTotalPoint").val(data.roleTotalPoint.replace(/\B(?=(\d{3})+(?!\d))/g, ","));								//지급 금액
						}
						$("#erpOrganizationCode").val(data.erpOrganizationCode);			//조직코드
						$("#erpRegalCode").val(data.erpRegalCode);			                //회계조직코드
						$("#erpOrganizationName").val(data.erpOrganizationName);			//분담조직명
						$("#erpOrganizationName").prop("required",false); //분담조직 코드
					},
			});

		}
	}


	function fnDelUserLevel(e){
		e.preventDefault();
		var dataItem = ctgryGrid.dataItem($(e.currentTarget).closest('tr'));
		userLevelGridDs.remove(dataItem);
		return;
	}


	// 계정발급 - 회원선택
	function fnSelectUser(){

		fnKendoPopup({
			id     : 'cpnMgmIssueList',
			title  : '회원선택',
			src    : '#/cpnMgmIssueList',
			width  : '1100px',
			height : '900px',
			param  : { },
			success: function(id, data ){
				$("#accountGrid").data("kendoGrid").dataSource.data( data.rows );

				$("#uploadAccountViewControl").show();
                $("#uploadAccountLink").text('총 발급 회원 수 : ' + $("#accountGrid").data("kendoGrid").dataSource.total() + '명');
                if($("#accountGrid").data("kendoGrid").dataSource.total() > 0 ){
                	$('#uploadAccountChk').val('Y');
                }else{
                	$('#uploadAccountChk').val(null);
                }
                $('#accountGridCountTotalSpan').text($("#accountGrid").data("kendoGrid").dataSource.total()  );
			}
		});
	}


	function fnAccountUploadInit(){
		 // 업로드 회원 팝업
	    $("#uploadUserPopup").kendoWindow({
           width : 450,
           height : 500,
           title : "업로드 회원",
           visible : false,
           modal : true
       });

		accountGridDs =  new kendo.data.DataSource();
		accountGridOpt = {
	            dataSource : accountGridDs,
	            editable : false,
	            columns : [ {field : 'loginId'	, title : '회원ID'	, width : '60px', attributes : {style : 'text-align:center'}}     ]
	        };

	        accountGrid = $('#accountGrid').initializeKendoGrid(accountGridOpt).cKendoGrid();
	}

	// 조직검색 팝업 호출
	function fnDeptPopupButton(param){

		fnKendoPopup({
			id     : 'deptPopup',
			title  : '조직검색',
			src    : '#/deptPopup',
			width  : '1100px',
			height : '800px',
			param  : { },
			success: function( stMenuId, data ){

				if(data.erpOrganizationCode != undefined){

                    $('#erpOrganizationName').val(data.erpOrganizationName);
                    $('#erpOrganizationCode').val(data.erpOrganizationCode);
                    $('#erpRegalCode').val(data.finOrganizationCode);
                    $('#erpRegalName').val(data.erpRegalName);

					if($("#pointType_3").prop("checked")){
						if(data.amount != null){
							//분담조직 역할 적립금 기본 제한액
							$('#issueValue').val(data.amount);
						}
						if(data.validityDay != null){
							//분담조직 역할 적립금 기본 유효일
							$('#pointPaymentAmount').val(data.validityDay);
						}
						//적립구분 선택 가능
						$("#pointPaymentType").data("kendoDropDownList").enable(true);	//적립구분 선택 가능
					}
				}

			}
		});
	}

	var fileTagId;
    var file;

    // 회원정보 업로드 Start
    function fnInitKendoUserUpload() {

      var uploadFileTagIdList = ['uploadUserFile'];

      var selectFunction = function(e) {
        if (e.files && e.files[0]) {
          // 엑셀 파일 선택시
          $("#fileInfoUserDiv").text(e.files[0].name);
          // --------------------------------------------------------------------
          // 확장자 2중 체크 위치
          // --------------------------------------------------------------------
          //  켄도 이미지 업로드 확장자 검사
          if(!validateExtension(e)) {
            fnKendoMessage({
              message : '허용되지 않는 확장자 입니다.',
              ok : function(e) {}
            });
            return;
          }

          fileTagId = e.sender.element[0].id;
          let reader = new FileReader();

          reader.onload = function(ele) {
              var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
              file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

              fnExcelUserUpload();
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


    // 이용권 번호 파일 업로드 Start
    function fnInitKendoTicketUpload() {

      var uploadFileTagIdList = ['uploadTicketFile'];

      var selectFunction = function(e) {
        if (e.files && e.files[0]) {
          // 엑셀 파일 선택시
          $("#fileInfoTicketDiv").text(e.files[0].name);
          // --------------------------------------------------------------------
          // 확장자 2중 체크 위치
          // --------------------------------------------------------------------
          //  켄도 이미지 업로드 확장자 검사
          if(!validateExtension(e)) {
            fnKendoMessage({
              message : '허용되지 않는 확장자 입니다.',
              ok : function(e) {}
            });
            return;
          }

          fileTagId = e.sender.element[0].id;
          let reader = new FileReader();

          reader.onload = function(ele) {
              var itemImageScr = ele.target.result; // FileReader 로 생성한 상품 이미지 url
              file = e.files[0].rawFile; // kendoUpload 로 가져온 상품 이미지 file 객체

              fnExcelTicketUpload();
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
    };


    // 회원정보 파일 업로드 이벤트
    function fnExcelUserUpload(event) {

    	if(file == undefined || file == ""){
            fnKendoMessage({
                message : "파일을 선택해주세요.",
                ok : function(e) {
                }
            });
            return;
        }

        var formData = new FormData();
        formData.append('bannerImage', file);
        $.ajax({
            url         : '/admin/pm/cpnMgm/addUserExcelUploadByAdm'
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
			, beforeSend : function(xhr) {
				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
			}
            , success     : function(id, data) {
				let duplicateUserCnt = 0;  // 중복 ID 카운트
				let totalIssueValue = 0; // 적립금 총액
				let maxIssueValue = 0;
				let excelTotalSize = id.data.rows.length;


				let result = id.data.rows.reduce(function(res, obj) {
					if (!(obj.loginId in res)) {
						res.processData.push(res[obj.loginId] = obj);
						totalIssueValue = totalIssueValue + Number(obj.uploadIssueValue);
						if(Number(obj.uploadIssueValue) > maxIssueValue) {
							maxIssueValue = Number(obj.uploadIssueValue);
						}
						// 엑셀 데이터에 적립금 10000원 이상 있는지 체크
						if(Number(obj.uploadIssueValue) >= APPROVE_POINT_BY_SYSTEM_AMOUNT) {
							autoApprCheck = true;
						}
					} else {
						res[obj.loginId].uploadIssueValue =  Number(res[obj.loginId].uploadIssueValue) + Number(obj.uploadIssueValue);
						totalIssueValue = totalIssueValue + Number(obj.uploadIssueValue);
						duplicateUserCnt++;
						if(Number(res[obj.loginId].uploadIssueValue) > maxIssueValue) {
							maxIssueValue = Number(res[obj.loginId].uploadIssueValue);
						}
						// 엑셀 데이터에 적립금 10000원 이상 있는지 체크
						if(Number(obj.uploadIssueValue) >= APPROVE_POINT_BY_SYSTEM_AMOUNT) {
							autoApprCheck = true;
						}
					}
					return res;
				}, {processData:[]});

                id.data.rows = result.processData;

				if (id.data.RETURN_CODE == "NO_ID") {
					fnKendoMessage({
						message: "회원 ID를 입력해 주세요.",
						ok: function () {
						}
					});
				} else if (id.data.RETURN_CODE.substr(0, 9) == "NO_AMOUNT") {
					let noAmountCntTmp = id.data.RETURN_CODE.split("∀") ;
					let noAmountCnt = noAmountCntTmp[1] ;

					if (id.data.rows.length != noAmountCnt) {
						fnKendoMessage({
							message: "적립금 입력이 안된 행이 존재합니다.",
							ok: function () {
							}
						});
					} else {
						//아이디만 부어놓고 공통으로 적립금 세팅하는 경우
						//활성화
						$('#issueValue').data("kendoNumericTextBox").value(null);
						$("#issueValue").css('background-color', '#FFFFFF').prop('disabled', false);
						$("label[for='issueValue']").addClass('req-star-back');
						$('#issueValue').data("kendoNumericTextBox").enable(true);
						$("#issueValue").prop("required",true);
						$("#SystemAmountOverCnt").val(0);

						setExcelInfo(id);
					}
				} else {

					//아이디, 적립금 모두 입력한 경우
                    if (id.data.RETURN_CODE.substr(0, 6) == "OK_CNT") {
                        //적립금 비활성화
                        let okCntTmp = id.data.RETURN_CODE.split("∀");

                        let okCnt = okCntTmp[1].split("∬")[0];
                        let SystemAmountOverTmp = okCntTmp[1].split("∬");
                        let SystemAmountOverCnt = SystemAmountOverTmp[1];
						let okUserCnt = okCnt-duplicateUserCnt;
						let avgIssueValue = Math.ceil(totalIssueValue/okUserCnt);
                        if (excelTotalSize == okCnt) {

                        	// $('#issueValue').data("kendoNumericTextBox").value(null);
							// $("#issueValue").css('background-color', '#4A4D42').prop('disabled', true);
							$("label[for='issueValue']").removeClass('req-star-back');
							// $('#issueValue').data("kendoNumericTextBox").enable(false);
                            $("#issueValue").attr("disabled", true);
                            $("#okCnt").val(okCnt); // 총 건수
                            $("#SystemAmountOverCnt").val(SystemAmountOverCnt);
                            $("#duplicateUserCnt").val(duplicateUserCnt);  // 중복 ID 수
							$("#avgIssueValue").val(avgIssueValue);    // 1인당 평균 지급액
							$("#maxIssueValue").val(maxIssueValue);    // 1인 지급 최대금액
							$("#okUserCnt").val(okUserCnt);            // 총 인원 수
                            $("#issueValue").val(totalIssueValue);     // 총 적립금
							// 자동승인 여부값 지정
							if(maxIssueValue > 10000) {
								$("#autoApprStatus").val("N");
							} else {
								$("#autoApprStatus").val("Y");
							}

							setExcelInfo(id);
							
                        } else {
							fnKendoMessage({
								message: "입력되지 않은 데이터가 있습니다. 엑셀자료를 확인해 주십시오.",
								ok: function () {
								}
							});                        	
						}
                    }

				}
			}
        });
    }

    //엑셀업로드 성공시 업로드정보 표시
    function setExcelInfo(id) {
		var fileName = file.name;
		$("#accountGrid").data("kendoGrid").dataSource.data(id.data.rows);
		$("#uploadAccountViewControl").show();
		$("#uploadUserFileName").show();
		// $("#uploadAccountLink").text('총 발급 회원 수 : ' + $("#accountGrid").data("kendoGrid").dataSource.total() + '명');
		if ($("#accountGrid").data("kendoGrid").dataSource.total() > 0) {
			$('#uploadAccountChk').val('Y');
		} else {
			$('#uploadAccountChk').val(null);
		}
		$('#accountGridCountTotalSpan').text($("#accountGrid").data("kendoGrid").dataSource.total());
		$("#uploadUserFileName").text('파일명 :' + fileName);

	}

    // 이용권번호 파일 업로드 이벤트
    function fnExcelTicketUpload(event) {

    	if(file == undefined || file == ""){
            fnKendoMessage({
                message : "파일을 선택해주세요.",
                ok : function(e) {
                }
            });
            return;
        }

        var formData = new FormData();
        formData.append('bannerImage', file);

        $.ajax({
            url         : '/admin/pm/cpnMgm/addTicketExcelUpload'
            , data        : formData
            , type        : 'POST'
            , contentType : false
            , processData : false
            , async       : false
            , beforeSend : function(xhr) {
            				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
            			}
            , success     : function(id, data) {
                    let localMessage = "";
                    if(data.code == 'FILE_ERROR' || data.code == 'EXCEL_TRANSFORM_FAIL' || data.code == 'EXCEL_UPLOAD_NONE'){
                        localMessage = data.message;
                    }else{

                    	var fileName = file.name;
                    	$("#ticketGrid").data("kendoGrid").dataSource.data(id.data.rows);
                        $("#uploadTicketViewControl").show();
                        $("#uploadFileName").show();
                        $("#uploadTicketLink").text('이용권 생성 개수 : ' + $("#ticketGrid").data("kendoGrid").dataSource.total() + '건');
                        $('#ticketGridCountTotalSpan').text($("#ticketGrid").data("kendoGrid").dataSource.total()  );
                        $("#uploadFileName").text('파일명 :' + fileName);
                        $("#issueQty").val($("#ticketGrid").data("kendoGrid").dataSource.total());

                        var issueValue;
                		var budget;
                		var issueQty;

            			issueQty =  $("#ticketGrid").data("kendoGrid").dataSource.total();
                		issueValue = Number($('#issueValue').val());

                		budget = issueValue * issueQty;

        				$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');

                    }

                  }
        });
    }
    // # 파일업로드 End
    // ##########################################################################


    // 분담조직정보 설정
    function fnOrgSetInit(){

		fnAjax({
			url     : '/admiun/pm/cpnMgm/getOrgInfo',
			params : {"stCommonCodeMasterCode" : "PROMOTION_ORG_VALUE", "useYn" :"Y"},
			success :
				function( data ){
					// 적립금 설정이 이용권인 경우 분담조직 default값 제거
					if($('#pointType').getRadioVal() != "POINT_TYPE.SERIAL_NUMBER") {
						$('#erpOrganizationName').val(data.rows[0].NAME);
						$('#erpOrganizationCode').val(data.rows[1].NAME);
						$('#erpRegalName').val(data.rows[2].NAME);
						$('#erpRegalCode').val(data.rows[4].NAME);
					} else {
						$('#erpOrganizationName').val("");
						$('#erpOrganizationCode').val("");
						$('#erpRegalName').val("");
						$('#erpRegalCode').val("");
					}
				},
			isAction : 'batch'
		});
    }

	// 업로드 회원등록 버튼 클릭
    function fnUseExcelUpload(){
        $("#uploadUserFile").trigger("click");
    }

    // 이용권 파일업로드 (난수)
    function fnTicketExcelUpload(){
    	$("#uploadTicketFile").trigger("click");
    }

    // 회원 엑셀 업로드 (SheetJs)
    function excelExport(event) {

        // Excel Data => Javascript Object 로 변환
        var input = event.target;
        var reader = new FileReader();

        var fileName = event.target.files[0].name;

        reader.onload = function() {
            var fileData = reader.result;
            var wb = XLSX.read(fileData, {
                type : 'binary'
            });

            wb.SheetNames.forEach(function(sheetName) {
                var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);

                $("#accountGrid").data("kendoGrid").dataSource.data(excelData);
                $("#uploadAccountViewControl").show();
                $("#uploadUserFileName").show();
                $("#uploadAccountLink").text('총 발급 회원 수 : ' + $("#accountGrid").data("kendoGrid").dataSource.total() + '명');
                if($("#accountGrid").data("kendoGrid").dataSource.total() > 0 ){
                	$('#uploadAccountChk').val('Y');
                }else{
                	$('#uploadAccountChk').val(null);
                }
                $('#accountGridCountTotalSpan').text($("#accountGrid").data("kendoGrid").dataSource.total()  );
                $("#uploadUserFileName").text('파일명 :' + fileName);
            })
        };

        reader.readAsBinaryString(input.files[0]);
    }


    // 이용권 엑셀 업로드 (SheetJs)
    function excelTicketExport(event) {

        // Excel Data => Javascript Object 로 변환
        var input = event.target;
        var reader = new FileReader();

        var fileName = event.target.files[0].name;

        reader.onload = function() {
            var fileData = reader.result;
            var wb = XLSX.read(fileData, {
                type : 'binary'
            });

            wb.SheetNames.forEach(function(sheetName) {
                var excelData = XLSX.utils.sheet_to_json(wb.Sheets[sheetName]);

                $("#ticketGrid").data("kendoGrid").dataSource.data(excelData);
                $("#uploadTicketViewControl").show();
                $("#uploadFileName").show();
                $("#uploadTicketLink").text('이용권 생성 개수 : ' + $("#ticketGrid").data("kendoGrid").dataSource.total() + '건');
                $('#ticketGridCountTotalSpan').text($("#ticketGrid").data("kendoGrid").dataSource.total()  );
                $("#uploadFileName").text('파일명 :' + fileName);
            })
        };

        reader.readAsBinaryString(input.files[0]);
    }


	function fnAccountUploadInit(){
		 // 업로드 회원 팝업
	    $("#uploadUserPopup").kendoWindow({
          width : 450,
          height : 500,
          title : "업로드 회원",
          visible : false,
          modal : true
      });

		accountGridDs =  new kendo.data.DataSource();

		accountGridOpt = {
	            dataSource : accountGridDs,
	            editable : false,
	            columns : [ {field : 'loginId'	, title : '회원ID'	, width : '60px', attributes : {style : 'text-align:center'}}     ]
	        };

	        accountGrid = $('#accountGrid').initializeKendoGrid(accountGridOpt).cKendoGrid();
	}



	function fnTicketUploadInit(){
		 // 업로드 회원 팝업
	    $("#uploadTicketPopup").kendoWindow({
          width : 450,
          height : 500,
          title : "업로드 이용권 난수",
          visible : false,
          modal : true
      });

		ticketGridDs =  new kendo.data.DataSource();

		ticketGridOpt = {
	            dataSource : ticketGridDs,
	            editable : false,
	            columns : [ {field : 'serialNumber'	, title : '이용권 번호'	, width : '60px', attributes : {style : 'text-align:center'}}     ]
	        };

	        ticketGrid = $('#ticketGrid').initializeKendoGrid(ticketGridOpt).cKendoGrid();
	}

	function fnSave(){

		var url  = '/admin/promotion/point/addPointSetting';
		var cbId = 'insert';

		var pointType;

		if($("#pointType_0").prop("checked")){
			pointType = 'POINT_TYPE.SERIAL_NUMBER';
		}else if($("#pointType_1").prop("checked")){
			pointType = 'POINT_TYPE.FEEDBACK';
		}else if($("#pointType_2").prop("checked")){
			pointType = 'POINT_TYPE.AUTO';
		}else if($("#pointType_3").prop("checked")){
			pointType = 'POINT_TYPE.ADMIN';
		}


		var ticketType;
		if (pointType == 'POINT_TYPE.SERIAL_NUMBER') {			// 적립금 설정 : 이용권
			if($("#serialNumberType_0").prop("checked")){
				ticketType = $('#randNumTypeSelect').val();
				$('#serialNumberType').val(ticketType);

			}else{
				ticketType = 'SERIAL_NUMBER_TYPE.FIXED_VALUE';
				$('#serialNumberType').val(ticketType);
			}

			if(ticketType != 'SERIAL_NUMBER_TYPE.FIXED_VALUE'){
				$('#fixSerialNumber').val('N');
			}

			// 난수생성 등록
			var uploadTicket = kendo.stringify( ticketGrid._data );
			$('#uploadTicket').val(uploadTicket);

		}else{

			// 단일코드
			$('#fixSerialNumber').val('N');

			// 생성개수
			if($('#issueQty').val() == ''){
				$('#issueQty').val('_');
			}
		}


		if(pointType == 'POINT_TYPE.ADMIN'){			// 적립금 설정 : 관리자 지급/차감
			// 관리자 지급 회원정보 등록
			var uploadUser = kendo.stringify( accountGrid._data );
			$('#uploadUser').val(uploadUser);

			// 기간 시작일
			if($('#issueStartDate').val() == ''){
				$('#issueStartDate').val('_');
			}

			// 기간 종료일
			if($('#issueEndDate').val() == ''){
				$('#issueEndDate').val('_');
			}

			//회원관리에서 관리자 지급시
			if(pointAdmin == 'Y'){
				$('#pointName').val('_');
				$('#pointAdmin').val('Y');
				$('#uploadUser').val(JSON.stringify([{'urUserId':urUserId}]));

			}else{
				$('#pointAdmin').val('N');

			}

			if($('#pointPaymentType').val() == 'POINT_PAYMENT_TP.DEDUCTION'){ //관리자 지급/차감 : 적립구분 차감인 Case
				$('#pointPaymentAmount').val('N');
			}

		}else{

			$('#pointPaymentAmount').val('N');

			// 지급/차감 상세사유
			$('#issueReason').val('N');

			// 적립구분
			if($('#pointPaymentType').val() == ''){
				$('#pointPaymentType').val('_');
			}
			// 적립구분 상세
			if($('#pointPaymentDetailType').val() == ''){
				$('#pointPaymentDetailType').val('_');
			}
			// 지급/차감 사유
			if($('#issueReasonType').val() == ''){
				$('#issueReasonType').val('_');
			}

			// 회원설정 체크
			$('#uploadAccountChk').val('N');

		}

		if(pointType == 'POINT_TYPE.FEEDBACK' || pointType == 'POINT_TYPE.ADMIN' ){

			//유효기간
			if($('#validityDay').val() == ''){
				$('#validityDay').val('_');
			}
		}else{
			if($('#validityType_1').prop('checked')){
				//유효기간
				if($('#validityDay').val() == ''){
					$('#validityDay').val('_');
				}
			}
		}

		if(pointType != 'POINT_TYPE.FEEDBACK'){
			// 지급기준
			if($('#issueDayCount').val() == ''){
				$('#issueDayCount').val('_');
			}

			//유효기간
			if($('#feedbackValidityDay').val() == ''){
				$('#feedbackValidityDay').val('_');
			}

			if($('#feedbackValidityDay').val() == ''){
				$('#feedbackValidityDay').val('_');
			}

			$('#normalAmount').val('N');
			$('#photoAmount').val('N');
			$('#premiumAmount').val('N');

		}else{										//  적립금 설정 : 후기
			$('#fixSerialNumber').val('N');

			if($('#issueValue').val() == ''){
				$('#issueValue').val('_');
			}
		}




		// 지급 수량 제한
		if($('#issueQtyLimitSelect').val() != ''){
			$('#issueQtyLimit').val($('#issueQtyLimitSelect').val());
		}

		if(pointType == 'POINT_TYPE.SERIAL_NUMBER'){
			if($('#issueQtyLimitSelect').val() == null){
				$('#issueQtyLimitSelect').val('_');
			}
		}


		if(pointType == 'POINT_TYPE.AUTO'){
			$('#pointPaymentDetailType').val($('#autoIssueSelect').val());
		}


		// 지급 수량 제한(제한없음)
		if($('#issueQtyLimitType').getRadioVal() == "N"){
			$('#issueQtyLimit').val(0);
			$('#issueQtyLimitSelect').val(null);
		}

		// 자동지금 외 필수값 처리
		if(pointType != 'POINT_TYPE.AUTO'){
			$('#autoIssueSelect').val('N');
		}

		// 유효기간 체크
		if(pointType == 'POINT_TYPE.SERIAL_NUMBER' || pointType == 'POINT_TYPE.AUTO'){
			if($("#validityType_0").prop("checked")){
				$('#validityDate').val('N');
			}else{
				$('#validityDay').val('N');
			}
		}else{
			$('#validityDate').val('N');
		}

		if(pointAdmin == 'Y'){
			$('#uploadAccountChk').val('N');
		}


// ********************************************************************************************************************

		var data = $('#inputForm').formSerialize(true);

// ********************************************************************************************************************

		// 적립금
		if($('#issueValue').val() == '_'){
			$('#issueValue').val('');
			data.issueValue = '';
		}

		// 적립금 명
		if(pointAdmin == 'Y'){
			$('#pointName').val('');
		}

		// 기간 시작일
		if($('#issueStartDate').val() == '_'){
			$('#issueStartDate').val('');
			data.issueStartDate = '0000-00-00';
		}

		// 기간 종료일
		if($('#issueEndDate').val() == '_'){
			$('#issueEndDate').val('');
			data.issueEndDate = '0000-00-00';
		}

		// 지급기준
		if($('#issueDayCount').val() == '_'){
			$('#issueDayCount').val('');
			data.issueDayCount = '';
		}

		// 지급 수량 제한
		if($('#issueQtyLimitType').getRadioVal() == "N"){
			$('#issueQtyLimitSelect').val('');
			data.issueQtyLimitSelect = '';
		}

		// 적립구분
		if($('#pointPaymentType').val() == '_'){
			$('#pointPaymentType').val('');
			data.pointPaymentType = '';
		}

		// 생성개수
		if($('#issueQty').val() == '_'){
			$('#issueQty').val('');
			data.issueQty = '';
		}

		// 적립구분 상세
		if($('#pointPaymentDetailType').val() == '_'){
			$('#pointPaymentDetailType').val('');
			data.pointPaymentDetailType = '';
		}
		// 지급차감 사유
		if($('#issueReasonType').val() == '_'){
			$('#issueReasonType').val('');
			data.issueReasonType = '';
		}

		//지금 수량 제한
		if($('#issueQtyLimitSelect').val() == '_'){
			$('#issueQtyLimitSelect').val('');
			data.issueQtyLimitSelect = '';
		}

		//유효기간
		if($('#feedbackValidityDay').val() == '_'){
			$('#feedbackValidityDay').val('');
			data.feedbackValidityDay = '';
		}

		if($('#validityDay').val() == '_'){
			$('#validityDay').val('');
			data.validityDay = '';
		}

		//단일코드
		if($('#fixSerialNumber').val() == 'N'){
			$('#fixSerialNumber').val('');
			data.fixSerialNumber = '';
		}

		//적립구분
		if($('#pointPaymentAmount').val() == 'N'){
			$('#pointPaymentAmount').val('');
			data.pointPaymentAmount = '';
		}

		// 회원설정 체크
		if($('#uploadAccountChk').val() == 'N'){
			$('#uploadAccountChk').val('');
			data.uploadAccountChk = '';
		}

		// 지급/차감 상세사유
		if($('#issueReason').val() == 'N'){
			$('#issueReason').val('');
			data.issueReason = '';
		}

		if(ticketType == 'SERIAL_NUMBER_TYPE.EXCEL_UPLOAD'){
			data.serialNumberType = ticketType;
		}

		var userGrid = [];
		var userGridList = [];
		let userGradeListJson;
		if(pointType == 'POINT_TYPE.FEEDBACK'){
			userGradeListJson = JSON.stringify(viewModel.userGradeList.data());
		}else{
			data.normalAmount = '';
			data.photoAmount = '';
			data.premiumAmount = '';
			$('#normalAmount').val('');
			$('#photoAmount').val('');
			$('#premiumAmount').val('');
		}

		data["userGradeList"] = userGradeListJson;

		if(pointType != 'POINT_TYPE.AUTO'){
			data.autoIssueSelect = '';
			$('#autoIssueSelect').val('');
		}


		if(pointType == 'POINT_TYPE.SERIAL_NUMBER' || pointType == 'POINT_TYPE.AUTO'){
			if($("#validityType_0").prop("checked")){
				$('#validityDate').val('');
				data.validityDate = '';
			}else{
				$('#validityDay').val('');
				data.validityDay= '';
			}
		}else{
			$('#validityDate').val('');
			data.validityDate = '';
		}

		// 지급예산 금액
		if(pointType == 'POINT_TYPE.SERIAL_NUMBER' ){
			if($("#serialNumberType_0").prop("checked") == true){
				if(ticketType == 'SERIAL_NUMBER_TYPE.AUTO_CREATE'){
					data.issueBudget = Number($('#issueValue').val()) * Number($('#issueQty').val());
				}else{
					data.issueBudget = Number($('#issueValue').val()) * Number($("#ticketGrid").data("kendoGrid").dataSource.total());
				}
			}
		}else{
			data.issueBudget = 0;
		}

		// 관리자 지급/차감
		if(pointType == 'POINT_TYPE.ADMIN'){
            data.validityType = 'VALIDITY_TYPE.VALIDITY';
        }

		if( data.rtnValid ){

			let todatyDt = Number(fnGetToday().replaceAll('-',''));

			if(pointType != 'POINT_TYPE.ADMIN'){
				let issueStartDt = Number($('#issueStartDate').val().replaceAll('-',''));

				// 기간 시작일 체크
				if(issueStartDt < todatyDt || issueStartDt > '29991231'){
					fnKendoMessage({message:'기간 시작일이 유효하지 않습니다.'});
					return;
				}

				let issueEndDt = Number($('#issueEndDate').val().replaceAll('-',''));

				// 기간 종료일 체크
				if(issueEndDt < todatyDt || issueEndDt > '29991231'){
					fnKendoMessage({message:'기간 종료일이 유효하지 않습니다.'});
					return;
				}
			}

			if(pointType == 'POINT_TYPE.SERIAL_NUMBER' || pointType == 'POINT_TYPE.AUTO'){
				let validityDt = Number($('#validityDate').val().replaceAll('-',''));

				// 유효기간 체크
				if(data.validityType == 'VALIDITY_TYPE.PERIOD' && (validityDt < todatyDt || validityDt > '29991231')){
					fnKendoMessage({message:'기간설정일이 유효하지 않습니다.'});
					return;
				}

				// 유효기간 체크
				if(data.validityType == 'VALIDITY_TYPE.PERIOD' && data.validityDate != '' && (data.validityDate < data.issueEndDate)){
					fnKendoMessage({message:'유효기간을 확인해주세요.'});
					return;
				}

				// 유효기간 : 기간설정 체크
				if(data.validityType == 'VALIDITY_TYPE.PERIOD' && data.validityDate == ''){
					fnKendoMessage({message:'유효기간을 확인 해 주세요.'});
					return;
				}
			}



			// 후기 : 적립금 체크
			if(pointType == 'POINT_TYPE.FEEDBACK'){
				// 적립금
				if($('#issueValue').val() == ''){
					$('#issueValue').val('_');
				}

				if( $('#normalAmount').val() == null || $('#normalAmount').val() == '' ) {
		        	fnKendoMessage({message : '일반후기 적립금이 입력되지 않았습니다.'});
		            return;
		        }

		        if( $('#photoAmount').val() == null || $('#photoAmount').val() == '' ) {
		        	fnKendoMessage({message : '포토후기 적립금이 입력되지 않았습니다.'});
		            return;
		        }

		        if( $('#premiumAmount').val() == null || $('#premiumAmount').val() == '' ) {
		        	fnKendoMessage({message : '프리미엄후기 적립금이 입력되지 않았습니다.'});
		            return;
		        }

			}


			// 자동지급 :
			if(pointType == 'POINT_TYPE.AUTO'){
				if( $('#autoIssueSelect').val() == null || $('#autoIssueSelect').val() == '' ) {
		        	fnKendoMessage({message : '자동지금 적립금 설정이 선택 되지 않았습니다.'});
		            return;
		        }
			}

			// 지급수량 제한 체크
			if($('#issueQtyLimitSelect').val() == null && data.issueQtyLimitType == 'Y'){
				fnKendoMessage({message:'지급 수량 제한 건수를 확인해주세요.'});
				return;
			}


			if(pointType == 'POINT_TYPE.SERIAL_NUMBER'){
				// 지급예산 :issueBudget
				// 적립금 : issueValue
				// 생성개수 : issueQty
//				if(data.issueBudget != 0){
//					var value = Math.floor(data.issueBudget/data.issueValue);
//					if(data.issueQty > value){
//						fnKendoMessage({message:'최대 생성개수는 ' + value + '개 입니다.'});
//						return;
//					}
//				}

				if(data.issueQty == 0 &&  data.randNumTypeSelect != 'SERIAL_NUMBER_TYPE.EXCEL_UPLOAD'){
					fnKendoMessage({message:'생성개수는 0개로 등록 불가합니다.'});
					return;
				}

				if(data.randNumTypeSelect == 'SERIAL_NUMBER_TYPE.EXCEL_UPLOAD'){
					data.issueQty = $("#ticketGrid").data("kendoGrid").dataSource.total();
				}
			}

			if(data.userMaster == ''){
				data.userMaster = 0;
			}


			if (pointType == 'POINT_TYPE.SERIAL_NUMBER') {
				if($("#serialNumberType_0").prop("checked")){
					ticketType = $('#randNumTypeSelect').val();
					if(ticketType == 'SERIAL_NUMBER_TYPE.EXCEL_UPLOAD'){
						if(	 $("#ticketGrid").data("kendoGrid").dataSource.total() == 0){
							fnKendoMessage({message : '엑셀업로드를 진행해주세요.'});
							return;
						}
					}
				}
			}

			if(pointType == 'POINT_TYPE.ADMIN'){
				if(pointAdmin != 'Y'){
					if(	 $("#accountGrid").data("kendoGrid").dataSource.total() == 0){
						fnKendoMessage({message : '회원설정 정보를 입력해주세요.'});
						return;
					}
				}
			}

			//승인요청 처리 선택 구분
			if($("#approvalCheckbox").prop("checked")){
				data.approvalCheck= 'Y';

				//업로드한 엑셀목록에 10000원 이상 적립금이 존재할때
				if( $('#apprUserId').val() == '' && $('#SystemAmountOverCnt').val() > 0 ){
					fnKendoMessage({message:'승인관리자가 지정되지 않았습니다.'});
					return;
				}

				if($('#apprUserId').val() == '' && $('#pointType').getRadioVal() == "POINT_TYPE.ADMIN" && Number($('#issueValue').val()) > APPROVE_POINT_BY_SYSTEM_AMOUNT){
					fnKendoMessage({message:'승인관리자가 지정되지 않았습니다.'});
					return;
				}

				if($('#apprUserId').val() == '' && $('#pointType').getRadioVal() != "POINT_TYPE.ADMIN" ){
					fnKendoMessage({message:'승인관리자가 지정되지 않았습니다.'});
					return;
				}

			}else{
				data.approvalCheck= 'N';

				if($('#apprUserId').val() == '' && $('#pointType').getRadioVal() == "POINT_TYPE.ADMIN"  && Number($('#issueValue').val()) > APPROVE_POINT_BY_SYSTEM_AMOUNT && pointAdmin == 'Y'){
					fnKendoMessage({message:'승인관리자가 지정되지 않았습니다.'});
					return;
				}

			}

			if($('#issueValue').val() == '0'){
				fnKendoMessage({message:'적립금은 0원으로 설정 할 수 없습니다.'});
				return;
			}

			if($("#payMethodType").getRadioVal() == "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY") {
				if(Number($("#maxIssueValue").val()) > 500000 ) {
					fnKendoMessage({message:'적립금은 50만원을 초과할 수 없습니다.'});
					return;
				}
			} else {
				if(Number($('#issueValue').val()) > 500000){
					fnKendoMessage({message:'적립금은 50만원을 초과할 수 없습니다.'});
					return;
				}
			}
		}


		if( data.rtnValid ){

			if((pointType == 'POINT_TYPE.ADMIN'
				&& Number($('#issueValue').val()) <= APPROVE_POINT_BY_SYSTEM_AMOUNT) || $("#payMethodType").getRadioVal() == "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY" && !autoApprCheck){

				let StrMessage = '';
				// 업로드한 엑셀자료에 10000 원이상 건이 있을 경우
				//if ($('#SystemAmountOverCnt').val() > 0 ) {
				//	StrMessage = '적립 금이 10000원 이하건은 자동승인 되고 10000원 이상건은 승인이 필요합니다.';
				//} else {
				// StrMessage = '적립 금이 10000원 이하이므로 자동승인으로 진행 됩니다.';
				//}

				if ($('#SystemAmountOverCnt').val() < 1) {
					StrMessage = '적립 금이 10000원 이하이므로 자동승인으로 진행 됩니다.';
				}

				fnKendoMessage({
					type : 'confirm',
					message : StrMessage,
					ok : function(e) {
						fnAjax({
							url     : url,
							params  : data,
							contentType : 'application/json',
							success :
								function( data ){
									if(data == "DEPOSIT_POINT_EXCEEDED") {
										fnKendoMessage({
											message: "적립금 한도가 초과되어 지급하지 못한 적립금이 있습니다.<br> 적립금 미지급내역을 확인해주세요."
											, ok: function () {
												fnBizCallback(cbId, data);
											}
										});
									} else {
										fnBizCallback(cbId, data);
									}
								},
							error : function(xhr, status, strError){
								fnKendoMessage({ message : xhr.responseJSON.message });
							},
							isAction : 'batch'
						});
					}
				});


			} else {
				if((pointType == 'POINT_TYPE.ADMIN'
					&& Number($('#issueValue').val()) > APPROVE_POINT_BY_SYSTEM_AMOUNT
					&& $('#pointPaymentType').val() == 'POINT_PAYMENT_TP.PROVISION') || ($("#payMethodType").getRadioVal() == "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY" && autoApprCheck)){
					var dataAdmin;

					fnAjax({
						url     : '/admin/promotion/point/getAdminAmountCheck',
						params  : {},
						success :
							function( dataAdmin ){
							var useAmount = Number(dataAdmin.useAmount);
							var roleValidityAmount = Number(dataAdmin.roleValidityAmount);
							var issueVal = Number($('#issueValue').val());

							if(roleValidityAmount < (useAmount + issueVal)) {
								fnKendoMessage({ message : '적립금 지급한도 부족으로, 승인 후 적립금이 지급 됩니다.' ,
				                    ok      : function(){	fnConfirmSave(url, data, cbId); }});
							}else{
								fnConfirmSave(url, data,cbId);
							}

						},
					});
				}else{
					fnConfirmSave(url, data,cbId);
				}

			}
		}

	}

	// 적립금 정보 저장
	function fnConfirmSave(url, data, cbId){
		fnAjax({
			url     : url,
			params  : data,
			contentType : 'application/json',
			success :
				function( data ){
				fnBizCallback(cbId, data);
			},
			error : function(xhr, status, strError){
				fnKendoMessage({ message : xhr.responseJSON.message });
			},
			isAction : 'batch'
		});
	}


	//--------------------------------- Button End---------------------------------


	//--------------------------------------------------------------------------
	//------------------------------- Grid Start -------------------------------
	//--------------------------------------------------------------------------



	function fnUserLevelInit(){

		 viewModel = new kendo.data.ObservableObject({
			 userGradeList : new kendo.data.DataSource({ // 회원등급별 차등 정보
	                schema: {
	                    model: {
	                        id: "userGroupCode",
	                        fields: {
	                        	userGroupName : { type: "String"},
	                        	normalAmount : { type: "number"}, // 일반후기 적립금
	                        	photoAmount : { type: "number"}, // 포토후기 적립금
	                        	premiumAmount : { type: "number"}, // 프리미엄 후기 적립금
	                        	userMaster : { type: "number"} // 프리미엄 후기 적립금
	                        }
	                    }
	                }
	            }),
	            fnAddUserLevel : function(e){ // 회원등급별 정보 추가
	                e.preventDefault();

	                let userGroupName = $('#userGroup').val();
	                let userGradeList = viewModel.userGradeList.data();

	                if(userGroupName == ''){
	                	fnKendoMessage({message : '회원등급이 선택되지 않았습니다.'});
            			return;
	                }

	                for(let i=0, rowCount=userGradeList.length; i < rowCount; i++){
	                    if( userGradeList[i].userGroupName == userGroupName ){
	                    	fnKendoMessage({message : '회원등급 정보가 존재합니다'});
	            			return;
	                    }
	                }


	                viewModel.userGradeList.add(
	                {
	                  userGroupName : $('#userGroup').val(),
	                  normalAmount : null,
	                  photoAmount : null,
	                  premiumAmount : null,
	                  userMaster : $('#userMaster').val()
	                });
	            },
	            fnUserGroupDeleteRow : function(e){ // ROW 회원등급별 정보 삭제
	                e.preventDefault();

	                fnKendoMessage({
	                    type    : "confirm",
	                    message : "삭제하시겠습니까?",
	                    ok      : function(){
	                        if( e.data.userGroupName != null && e.data.userGroupName > 0 ){
	                        }else{
	                            viewModel.fnUserGroupDatasourceRemove(e.data);
	                        }
	                    },
	                    cancel  : function(){

	                    }
	                });

	            },
	            fnUserGroupDatasourceRemove : function( userGroupInfo ){ // datasource 삭제 및 할인추가 버튼 제어
	                viewModel.userGradeList.remove(userGroupInfo);
	            }

		});

		kendo.bind($("#inputForm"), viewModel);

	}

	function fnPointShortInfo(){

    	if(pointAdmin != "Y" && $("#accountGrid").data("kendoGrid").dataSource.total() == 0) {
    		return false;
		}

    	let str = "";
    	str += "총 지급인원 : " + $("#okUserCnt").val() + "명<br>";
    	str += "총 건수 : " + $("#okCnt").val() + "건<br>";
    	str += "중복 지급인원 : " + $("#duplicateUserCnt").val() + "명<br><br>";
    	str += "1인당 평균 지급금액 : " + fnNumberWithCommas($("#avgIssueValue").val()) + "원<br>";
    	str += "1인 지급 최대금액 : " + fnNumberWithCommas($("#maxIssueValue").val()) + "원<br>";

		fnKendoMessage({
			message: str,
			ok      : function(){}
		});
	}

	// 승인관리자 선택 팝업 호출
	function fnApprAdmin(){
		var param  = {'taskCode' : 'APPR_KIND_TP.POINT' };
		fnKendoPopup({
			id     : 'approvalManagerSearchPopup',
			title  : '승인관리자 선택',
			src    : '#/approvalManagerSearchPopup',
			param  : param,
			width  : '1100px',
			height : '800px',
			scrollable : "yes",
			success: function( stMenuId, data ){

				if(data && !fnIsEmpty(data) && data.authManager2nd){
					$('#apprGrid').gridClear(true);
					var authManager1 = data.authManager1st;
					var authManager2 = data.authManager2nd;

					if(authManager1.adminTypeName != undefined){
						var objManager1 = new Object();

						objManager1["apprAdminInfo"] = '1차 승인관리자';
						objManager1["adminTypeName"] = authManager1.adminTypeName;
						objManager1["apprUserName"] = authManager1.apprUserName;
						objManager1["apprLoginId"] = authManager1.apprLoginId;
						objManager1["organizationName"] = authManager1.organizationName;
						objManager1["userStatusName"] = authManager1.userStatusName;
						objManager1["teamLeaderYn"] = authManager1.teamLeaderYn;
						objManager1["grantAuthYn"] = authManager1.grantAuthYn;
						objManager1["grantUserName"] = authManager1.grantUserName;
						objManager1["grantLoginId"] = authManager1.grantLoginId;
						objManager1["grantAuthStartDt"] = authManager1.grantAuthStartDt;
						objManager1["grantAuthEndDt"] = authManager1.grantAuthEndDt;
						objManager1["grantUserStatusName"] = authManager1.grantUserStatusName;

						apprAdminGridDs.add(objManager1);
						$('#apprSubUserId').val(authManager1.apprUserId);
					}


					if(authManager2 != undefined){
						var objManager2 = new Object();

						objManager2["apprAdminInfo"] = '최종 승인관리자';
						objManager2["adminTypeName"] = authManager2.adminTypeName;
						objManager2["apprUserName"] = authManager2.apprUserName;
						objManager2["apprLoginId"] = authManager2.apprLoginId;
						objManager2["organizationName"] = authManager2.organizationName;
						objManager2["userStatusName"] = authManager2.userStatusName;
						objManager2["teamLeaderYn"] = authManager2.teamLeaderYn;
						objManager2["grantAuthYn"] = authManager2.grantAuthYn;
						objManager2["grantUserName"] = authManager2.grantUserName;
						objManager2["grantLoginId"] = authManager2.grantLoginId;
						objManager2["grantAuthStartDt"] = authManager2.grantAuthStartDt;
						objManager2["grantAuthEndDt"] = authManager2.grantAuthEndDt;
						objManager2["grantUserStatusName"] = authManager2.grantUserStatusName;

						apprAdminGridDs.add(objManager2);
						$('#apprUserId').val(authManager2.apprUserId);
					}
				}


			}
		});
	}

	// 승인관리자 그리드 초기화
	function fnApprClear(){
		$('#apprGrid').gridClear(true);
		$('#apprSubUserId').val('');
		$('#apprUserId').val('');
	}


    //==================================================================================
    //--------------- Initialize Option Box Start --------------------------------------
    //==================================================================================
    function fnInitOptionBox() {


		//적립금 선택 화면 제어
		fnTagMkRadio({
			id    : 'pointType',
			tagId : 'pointType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "POINT_TYPE", "useYn" :"Y"},
			async : false,
			chkVal: 'POINT_TYPE.SERIAL_NUMBER',
			style : {},
			change : function(e){
			    // 관리자 지급/차감 -> 엑셀 대량 지급에서 업로드 후 적립금 disabled 처리추가하면서 적용
				$("input[name='issueValue']").attr("disabled",false);
				if($('#pointType').getRadioVal() == "POINT_TYPE.SERIAL_NUMBER"){		//적립금 설정 - 이용권
						//$('#inputForm').formClear(true);
						$("#pointType_0").prop("checked",true);

						$("#issueDateDiv").show();				// 기간
						$("#issueQtyLimitDiv").show();			// 지급 수량 제한
						$("#pointDiv").show();					// 적립금
						$("#validityDateDiv").show();			// 유효기간
						$("#randomNumDiv").show();				// 난수생성
						$("#issueQtyDiv").show();				// 생성개수

						$("#pointTypeAutoDiv").hide();
						$("#pointTypeAutoDiv").hide();
						$("#adminPaymentDiv").hide();
						$("#adminPaymentDetailDiv").hide();
						$("#pointFeedbackDiv").hide();
						$("#userSettingDiv").hide();
						$("#provisionDeductionDiv").hide();
						$("#provisionStandardDiv").hide();
						$("#userGradeDiv").hide();

						$("#previewValidityDayDiv").hide();

						$("#erpOrganizationDiv").show();
						$("#buyerDiv").hide();
						$("#payMethodDiv").hide();
						$("#fnPointShortInfo").hide();

						$("#pointPaymentType").data("kendoDropDownList").enable(true);	//적립구분 선택 가능

						$("#pointPaymentAmountDiv").hide();

						$("#serialNumberType_0").prop("checked",true);		//난수생성 초기값

						$("#issueQtyLimitType_0").prop("checked",true);		//지급수량제한 : 제한없음

						$('#totalBudget').show();
						$('#budget').text('');
						$('#issueQtyLimitSelect').val('');
						$("#randNumTypeSelect").data("kendoDropDownList").enable(true);

						$("#issueQty").attr("disabled", false);
		                // 엑셀업로드 문구
						$('#excelCmt').hide();

						$("#randNumTypeSelect").data('kendoDropDownList').value("SERIAL_NUMBER_TYPE.AUTO_CREATE");

						// 엑셀업로드 초기화
						$("#uploadTicketLink").text('');
		                $('#ticketGridCountTotalSpan').text('');
		                $("#uploadFileName").text('');
		                $('#issueQty').val('');

					}else if($('#pointType').getRadioVal() == "POINT_TYPE.FEEDBACK"){		//적립금 설정 - 후기
						//$('#inputForm').formClear(true);
						$("#pointType_1").prop("checked",true);

						$("#issueDateDiv").show();				// 기간
						$("#pointFeedbackDiv").show();			// 후기 적립금
						$("#provisionStandardDiv").show();		// 지급기준
						$('#issueDayCount').val(0);				// 초기값 0 설정
						$("#userGradeDiv").show();				// 회원등급별 차등
						$("#issueQtyDiv").hide();

						$("#pointTypeAutoDiv").hide();
						$("#issueQtyLimitDiv").hide();
						$("#adminPaymentDiv").hide();
						$("#adminPaymentDetailDiv").hide();
						$("#pointDiv").hide();
						$("#userSettingDiv").hide();
						$("#provisionDeductionDiv").hide();
						$("#validityDateDiv").hide();
						$("#randomNumDiv").hide();

						$("#previewValidityDayDiv").show();

						$("#erpOrganizationDiv").show();
						$("#buyerDiv").hide();
						$("#payMethodDiv").hide();
						$("#fnPointShortInfo").hide();

						$("#pointPaymentType").data("kendoDropDownList").enable(true);	//적립구분 선택 가능

						$("#pointPaymentAmountDiv").hide();

						$('#totalBudget').hide();
						$('#budget').text('');

						$("#issueQty").attr("disabled", false);
		                // 엑셀업로드 문구
						$('#excelCmt').hide();

					}else if($('#pointType').getRadioVal() == "POINT_TYPE.AUTO"){		//적립금 설정 - 자동지급
						$("#pointType_2").prop("checked",true);

						$("#pointTypeAutoDiv").show();			// 자동지급 설정타입
						$("#issueDateDiv").show();				// 기간
						$("#pointDiv").show();					// 적립금
						$("#validityDateDiv").show();			// 유효기간
						$("#issueQtyDiv").hide();

						$("#issueQtyLimitDiv").hide();
						$("#adminPaymentDiv").hide();
						$("#adminPaymentDetailDiv").hide();
						$("#userSettingDiv").hide();
						$("#provisionDeductionDiv").hide();
						$("#pointFeedbackDiv").hide();
						$("#userGradeDiv").hide();
						$("#randomNumDiv").hide();
						$("#provisionStandardDiv").hide();

						$("#previewValidityDayDiv").hide();


						$("#erpOrganizationDiv").show();
						$("#buyerDiv").hide();
						$("#payMethodDiv").hide();
						$("#fnPointShortInfo").hide();


						$("#pointPaymentType").data("kendoDropDownList").enable(true);	//적립구분 선택 가능

						$("#pointPaymentAmountDiv").hide();

						$('#totalBudget').hide();
						$('#budget').text('');

						$("#issueQty").attr("disabled", false);
		                // 엑셀업로드 문구
						$('#excelCmt').hide();

					}else if($('#pointType').getRadioVal() == "POINT_TYPE.ADMIN"){		//적립금 설정 - 관리자 지급/차감

						$("#pointType_3").prop("checked",true);

						$("#adminPaymentDiv").show();			// 적립구분
						$("#adminPaymentDetailDiv").show();		// 적립 상세 구분
						$("#pointDiv").show();					// 적립금
						$("#userSettingDiv").show();			// 회원설정
						$("#provisionDeductionDiv").show();		// 지급/차감 사유
						$("#payMethodDiv").show();		// 지급 방법
						$("#issueQtyDiv").hide();
						if($("#payMethodType").getRadioVal() == "POINT_PAY_METHOD_TP.SINGLE_PAY"){
							$("#fnPointShortInfo").hide();
						} else if($("#payMethodType").getRadioVal() == "POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY"){
							$("#fnPointShortInfo").show();
							$("input[name='issueValue']").attr("disabled",true);
						}

						$("#pointTypeAutoDiv").hide();
						$("#issueDateDiv").hide();
						$("#issueQtyLimitDiv").hide();
						$("#provisionStandardDiv").hide();
						$("#pointFeedbackDiv").hide();
						$("#userGradeDiv").hide();
						$("#validityDateDiv").hide();
						$("#randomNumDiv").hide();

						$("#previewValidityDayDiv").hide();

						$("#erpOrganizationDiv").show();
						$("#buyerDiv").hide();

						$('#pointPaymentDetailType').val('');

						if(pointAdmin == 'Y'){
							$("#buyerInfoDiv").hide();
							$("#adminBuyerDiv").show();
						}

						$("#pointPaymentType").data("kendoDropDownList").enable(true);			//적립구분 선택 가능
						$("#pointPaymentDetailType").data("kendoDropDownList").enable(true);	//적립상구분 선택 가능

						$("#pointPaymentAmountDiv").hide();

						$("#issueQty").attr("disabled", false);
						// 엑셀업로드 문구
						$('#excelCmt').hide();
						$('#totalBudget').hide();
						$('#budget').text('');

					}
				fnOrgSetInit();
            }

		});

    	$('#kendoPopup').kendoWindow({
			visible: false
			, modal: true
		});

    	//================ 적립금 자동지급 설정구분 ===============
    	fnKendoDropDownList({
    			id  : 'autoIssueSelect',
    	        url : "/admin/comn/getCodeList",
    	        params : {"stCommonCodeMasterCode" : "POINT_AUTO_ISSUE_TP", "useYn" :"Y"},
    	        textField :"NAME",
    			valueField : "CODE",
    			blank : "선택해주세요"
    	});


		//================ 지급수량 제한 구분 ================
    	fnTagMkRadio({
			id    :  "issueQtyLimitType",
			data  : [	{ "CODE" : "N" , "NAME":"제한없음"}
					, 	{ "CODE" : "Y" , "NAME":"수량제한"}],
			tagId : "issueQtyLimitType",
			chkVal: 'N',
			style : {},
			change : function(e){
				if($('#issueQtyLimitType').getRadioVal() == "N"){
					$("#issueQtyLimitSelect").attr("disabled", true);
					$('#budget').text('');
					$('#issueQtyLimitSelect').val('');
				}else{
					$("#issueQtyLimitSelect").attr("disabled", false);
					$('#budget').text('');
				}
			}
		});

		//================ 관라자 지급/차감 지급 방법  ================
		fnTagMkRadio({
			id    :  "payMethodType",
			tagId : "payMethodType",
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "POINT_PAY_METHOD_TP", "useYn" :"Y"},
			chkVal: 'POINT_PAY_METHOD_TP.SINGLE_PAY',
			style : {},
			change : function(e){
                // 관리자 지급/차감 -> 엑셀 대량 지급에서 업로드 후 적립금 disabled 처리추가하면서 적용

				if($("#payMethodType").getRadioVal() == 'POINT_PAY_METHOD_TP.SINGLE_PAY') {
					// 단일 ( 회원선택 버튼만 보이게 )
					$("#fnSelectUser").show();
					$("#fnUseExcelUpload").hide();
					$("#fnSamepleFormDownload").hide();
					$("#fnPointShortInfo").hide();
					$("input[name='issueValue']").attr("disabled",false);
				} else if($("#payMethodType").getRadioVal() == 'POINT_PAY_METHOD_TP.EXCEL_LARGE_PAY') {
					// 엑셀 대량 ( 엑셀업로드, 샘플양식 다운로드 보이$("#fnPointShortInfo").show();게 )
					$("#fnSelectUser").hide();
					$("#fnUseExcelUpload").show();
					$("#fnSamepleFormDownload").show();
					$("#fnPointShortInfo").show();
					$("input[name='issueValue']").attr("disabled",true);
				}
			}
		});

		//================데이트 피커===============
		fnKendoDatePicker({
			id          : 'issueStartDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('issueStartDate','issueEndDate','발급기간');
			}
		});
		fnKendoDatePicker({
			id          : 'issueEndDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('issueStartDate','issueEndDate','발급기간');
			}
		});

		fnKendoDatePicker({
			id          : 'validityDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('validityDate','validityDate','유효기간');
			}
		});

		fnKendoDatePicker({
			id          : 'pointPaymentAmountToDate',
			format		: 'yyyy-MM-dd',
			min 		: fnGetToday(),
			change      : function(e){
				dateTimeChk('pointPaymentAmountToDate','pointPaymentAmountToDate','유효일날짜보기')

				//유효일자 변경 시 유효일 노출
				var sDate = new Date(fnGetToday());
				var eDate = new Date(e.sender._oldText);
				var diff = Math.abs(eDate.getTime() - sDate.getTime());
				diff = Math.ceil(diff / (1000 * 3600 * 24));
				$("#pointPaymentAmount").val(diff);
			}
		});

		//=================== 난수생성 =======================
		fnKendoDropDownList({
			id    : 'randNumTypeSelect',
			data  : [
					{"CODE":"SERIAL_NUMBER_TYPE.AUTO_CREATE","NAME":"자동생성"}
					,{"CODE":"SERIAL_NUMBER_TYPE.EXCEL_UPLOAD","NAME":"엑셀 업로드"}
					],
			chkVal: 'SERIAL_NUMBER_TYPE.AUTO_CREATE'
		});


		$('#randNumTypeSelect').unbind('change').on('change', function(){
			var dropDownList =$('#randNumTypeSelect').data('kendoDropDownList');
			var data = dropDownList.value();
			switch(data){
			case "SERIAL_NUMBER_TYPE.AUTO_CREATE" :
				$("#fnTicketExcelUpload").hide();
				$("#fnTicketSamepleFormDownload").hide();
				$("#fixSerialNumber").attr("disabled", false);
				$("#uploadTicketLink").text('');
                $('#ticketGridCountTotalSpan').text('');
                $("#uploadFileName").text('');
                $('#issueQty').attr("disabled", false);
				// 엑셀업로드 문구
				$('#excelCmt').hide();
				break;

			case "SERIAL_NUMBER_TYPE.EXCEL_UPLOAD" :
				$("#fnTicketExcelUpload").show();
				$("#fnTicketSamepleFormDownload").show();
				$("#fixSerialNumber").attr("disabled", true);
				$('#issueQty').val('');
				$('#issueQty').attr("disabled", true);
				// 엑셀업로드 문구
				$('#excelCmt').show();
				break;
			}
		});



		$("input[name=serialNumberType]").change(function() {

			var chkValue = $(this).val();
			if(chkValue == 'SERIAL_NUMBER_TYPE.FIXED_VALUE'){
				var dropDownList =$('#randNumTypeSelect').data('kendoDropDownList');
				dropDownList.value("SERIAL_NUMBER_TYPE.AUTO_CREATE");
				$("#randNumTypeSelect").data("kendoDropDownList").enable(false);
				$("#fixSerialNumber").attr("disabled", false);
				$("#fnTicketExcelUpload").hide();
				$("#fnTicketSamepleFormDownload").hide();
				$("#uploadTicketLink").text('');
                $('#ticketGridCountTotalSpan').text('');
                $("#uploadFileName").text('');
                $("#issueQty").attr("disabled", false);
                // 엑셀업로드 문구
				$('#excelCmt').hide();

			}else{
				var dropDownList =$('#randNumTypeSelect').data('kendoDropDownList');
				dropDownList.value("SERIAL_NUMBER_TYPE.AUTO_CREATE");
				$("#fnTicketExcelUpload").hide();
				$("#fnTicketSamepleFormDownload").hide();
				$("#randNumTypeSelect").data("kendoDropDownList").enable(true);
				$("#fixSerialNumber").attr("disabled", true);
				$("#uploadTicketLink").text('');
                $('#ticketGridCountTotalSpan').text('');
                $("#uploadFileName").text('');
                $("#issueQty").attr("disabled", false);
                // 엑셀업로드 문구
				$('#excelCmt').hide();
			}
		});

		// 회원 마스터그룹
        fnKendoDropDownList({
        	id : "userMaster",
        	tagId : "userMaster",
        	url : "/admin/comn/getUserMasterCategoryList",
        	textField : "groupMasterName",
        	valueField : "urGroupMasterId",
        	blank : "회원그룹명 선택",
        	async : false
        });

        // 회원 등급
        fnKendoDropDownList({
            id : "userGroup",
            tagId : "userGroup",
            url : "/admin/comn/getUserGroupCategoryList",
            textField : "groupName",
            valueField : "groupName",
            blank : "회원 등급 선택",
            async : false,
            cscdId : "userMaster",
            cscdField : "urGroupMasterId"
        });

        $('#userMaster').unbind('change').on('change', function(){
			var userMasdterDownList =$('#userMaster').data('kendoDropDownList');
			userMaster = userMasdterDownList._oldText;

		});



		// 적립구분
		fnKendoDropDownList({
			id  : 'pointPaymentType',
			tagId : 'pointPaymentType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "POINT_PAYMENT_TP", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "NAME",
			blank : "선택해주세요"
		});

		$('#pointPaymentType').unbind('change').on('change', function(){
			var dropDownList =$('#pointPaymentType').data('kendoDropDownList');
			var data = dropDownList.value();
			switch(data){
			case "POINT_PAYMENT_TP.PROVISION" :
				$("#pointPaymentAmountDiv").show();
				$("#pointPaymentDetailType").data("kendoDropDownList").enable(true);	//적립상구분 선택 가능
				break;

			case "POINT_PAYMENT_TP.DEDUCTION" :
				$("#pointPaymentAmountDiv").hide();
				$("#pointPaymentDetailType").data("kendoDropDownList").enable(true);	//적립상구분 선택 가능
				break;
			}
		});



		// 적립상세구분
		fnKendoDropDownList({
			id  : 'pointPaymentDetailType',
			tagId : 'pointPaymentDetailType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "POINT_ADMIN_ISSUE_TP", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "NAME",
			blank : "선택해주세요"
		});

		// 지급/차감 사유
		fnKendoDropDownList({
			id  : 'issueReasonType',
			tagId : 'issueReasonType',
			url   : "/admin/comn/getCodeList",
			params : {"stCommonCodeMasterCode" : "PROVISION_DEDUCTION_REASON", "useYn" :"Y"},
			textField :"NAME",
			valueField : "CODE",
			value : "NAME",
			blank : "선택해주세요"
		});


		// 업로드 회원등록 링크 클릭
        $("#uploadAccountLink").on("click", function(e){
            $("#uploadUserPopup").data("kendoWindow").center().open();
        });


        // 업로드 이용권 링크 클릭
        $("#uploadTicketLink").on("click", function(e){
            $("#uploadTicketPopup").data("kendoWindow").center().open();
        });



	}
    //==================================================================================
	//---------------Initialize Option Box End ------------------------------------------------
    //==================================================================================



    //==================================================================================
    //-------------------------------  Common Function start -------------------------------
    //==================================================================================
    function inputFocus() {
        $('#brandName').focus();
    };

    // 이용권내역 호출 팝업
    function fnSerialNumber(pmPointId, useType){

		var map = aGrid.dataItem(aGrid.select());
		var sData = $('#searchForm').formSerialize(true);
		var option = new Object();

		option.url = "#/serialNumberList";
		option.menuId = 807;
		option.data = {
				parentId : pmPointId
				, useType : "SERIAL_NUMBER_USE_TYPE.POINT"
				};

		$scope.goPage(option);
    }

    //-------------------------------  콜백합수 -----------------------------
    function fnBizCallback (id, data) {

        switch (id) {
            case 'select':
                break;
			case 'insert':
				fnKendoMessage({
                    message:"저장되었습니다.",
                    ok      : function(){ fnClose();}
				});
			    break;
        }
    }

    //-- Alert 메세지
	function fnAlertMessage(msg, id) {
		fnKendoMessage(
			{  message : msg
				, ok      : function focusValue() { $("#" + id).focus(); }
			}
		);
        return false;
    }

	// 샘플다운로드 버튼 클릭
	function fnSamepleFormDownload(){
		document.location.href = "/contents/excelsample/point/적립금_계정발급_샘플.xlsx"
	};


	//샘플다운로드 버튼 클릭
	function fnTicketSamepleFormDownload(){
		document.location.href = "/contents/excelsample/point/적립금_이용권발급_샘플.xlsx"
	};

	//승인관리자 정보 Grid
	function fnAprpAdminInit(){
		apprAdminGridDs =  new kendo.data.DataSource();

		apprAdminGridOpt = {
	            dataSource : apprAdminGridDs,
	            editable : false,
	            noRecordMsg: '승인관리자를 선택해 주세요.',
	            columns : [{
			                field : 'apprAdminInfo',
			                title : '승인관리자 정보',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'adminTypeName',
			                title : '계정유형',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'apprUserName',
			                title : '관리자이름/아이디',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'
			                },
			                template : function(dataItem){
								let returnValue;
								returnValue = dataItem.apprUserName + '/' + dataItem.apprLoginId;
								return returnValue;
							}
		                },{
			                field : 'organizationName',
			                title : '조직/거래처 정보',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'teamLeaderYn',
			                title : '조직장여부',
			                width : '80px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'userStatusName',
			                title : 'BOS 계정상태',
			                width : '80px',
			                attributes : {
			                    style : 'text-align:center'}
		                },{
			                field : 'grantUserName',
			                title : '권한위임정보',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:center'},
			                template : function(dataItem){
									let returnValue;
									if(dataItem.grantAuthYn == 'Y'){
										returnValue = dataItem.grantUserName + '/' + dataItem.grantLoginId;
									}else{
										returnValue = '';
									}
									return returnValue;
							}
		                },{
			                field : 'userStatusName',
			                title : '권한위임기간',
			                width : '150px',
			                attributes : {
			                    style : 'text-align:left'},
			                template : function(dataItem){
									let returnValue;
									if(dataItem.grantAuthYn == 'Y'){
										returnValue = dataItem.grantAuthStartDt + '~' + dataItem.grantAuthEndDt;
									}else{
										returnValue = '';
									}
									return returnValue;
							}
		                },{
			                field : 'grantUserStatusName',
			                title : '권한위임자 <br> BOS 계정상태',
			                width : '100px',
			                attributes : {
			                    style : 'text-align:left'},
			                template : function(dataItem){
									let returnValue;
									if(dataItem.grantAuthYn == 'Y'){
										returnValue = dataItem.grantUserStatusName;
									}else{
										returnValue = '';
									}
									return returnValue;
							}
		                },
			            { field:'addCoverageId', hidden:true},
			            { field:'includeYn', hidden:true}
	            ]
	        };

		apprAdminGrid = $('#apprGrid').initializeKendoGrid(apprAdminGridOpt).cKendoGrid();


	}

	// 지급예산 처리
	function fnGetBudget(){

		var issueValue = Number($('#issueValue').val());
		var budget;
		var issueQty;

		var ticketType = $('#randNumTypeSelect').val();
		var ticketCnt;
		if($("#pointType_0").prop("checked")){
			if(ticketType == 'SERIAL_NUMBER_TYPE.EXCEL_UPLOAD'){
				issueQty =  $("#ticketGrid").data("kendoGrid").dataSource.total();
				budget = issueValue * issueQty;
				$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
			}else{
				budget = issueValue * Number($('#issueQty').val());;
				$('#budget').text(budget.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",") + '원');
			}
		}


	}

    //==================================================================================
    //-------------------------------  Common Function end -----------------------------
    //==================================================================================


    //==================================================================================
	//------------------------------- Html 버튼 바인딩  Start -------------------------------
    //==================================================================================
    $scope.fnSearch     = function () { fnSearch(); };
    $scope.fnClear      = function () { fnClear() ; };
    $scope.fnNew        = function () { fnNew()   ; };
    $scope.fnSave       = function () { fnSave()  ; };
    $scope.fnClose      = function () { fnClose() ; };

    $scope.fnExcel      = function () { fnExcel() ; };
    $scope.fnSelectUser = function(){fnSelectUser();};
    $scope.fnDeptPopupButton = function(){fnDeptPopupButton();};
    $scope.fnUseExcelUpload = function(){fnUseExcelUpload();};
	$scope.fnTicketExcelUpload = function(){fnTicketExcelUpload();};
	$scope.fnExcelUpload = function(event) { excelExport(event);} // 엑셀 업로드 버튼
	$scope.fnExcelTicketUpload = function(event) { excelTicketExport(event);} // 엑셀 이용권 난수 업로드 버튼

	$scope.fnSamepleFormDownload = function(event) { fnSamepleFormDownload();}

	$scope.fnTicketSamepleFormDownload = function(){fnTicketSamepleFormDownload();};

	$scope.fnApprAdmin = function(){fnApprAdmin();};	//승인관리자 지증

	$scope.fnApprClear = function(){fnApprClear();};	//승인관리자 초기화
	
	$scope.fnPointShortInfo = function(){fnPointShortInfo();};   // 요약정보 보기

    //==================================================================================
	//------------------------------- Html 버튼 바인딩  End ----------------------------
    //==================================================================================

	//마스터코드값 입력제한 - 숫자 & -
	fnInputValidationByRegexp("issueValue", /[^0-9]/g);
	fnInputValidationByRegexp("normalAmount", /[^0-9]/g);
	fnInputValidationByRegexp("photoAmount", /[^0-9]/g);
	fnInputValidationByRegexp("premiumAmount", /[^0-9]/g);
	fnInputValidationByRegexp("issueQty", /[^0-9]/g);
	fnInputValidationByRegexp("validityDay", /[^0-9]/g);
	fnInputValidationByRegexp("issueDayCount", /[^0-9]/g);
	fnInputValidationByRegexp("pointPaymentAmount", /[^0-9]/g);
	fnInputValidationByRegexp("feedbackValidityDay", /[^0-9]/g);
}); // document ready - END
