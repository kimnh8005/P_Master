/**-----------------------------------------------------------------------------
 * description 		 : 회원 상세 팝업
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.06.30		천혜현          최초생성
 * @
 * @
 * **/
'use strict';

var PAGE_SIZE = 20;
var aGridDs, aGridOpt, aGrid;
var bGridDs, bGridOpt, bGrid;
var cGridDs, cGridOpt, cGrid;
var dGridDs, dGridOpt, dGrid;
var eGridDs, eGridOpt, eGrid;
var fGridDs, fGridOpt, fGrid;
var gGridDs, gGridOpt, gGrid;
var hGridDs, hGridOpt, hGrid;
var iGridDs, iGridOpt, iGrid; //적립금정보
var jGridDs, jGridOpt, jGrid;
var edpGridDs, edpGridOpt, edpGrid; //임직원 할인내역 조회 과거내역

var isCheckMail;
var isValidBankAccount = true;

var BEFORE_OBJECT;
var personalInformationAccessYn  ="N" ;

var paramData ;
if(parent.POP_PARAM['parameter']){
	paramData = parent.POP_PARAM['parameter'];
}

if(defaultActivateTab == undefined){
	var defaultActivateTab;
	var menuFlag = false;
}else{
	var menuFlag = true;
	$('#urUserId').val(csData.urUserId);
	$('#urErpEmployeeCd').val(csData.urErpEmployeeCd);
	$('#userName').val(csData.userName);
	$('#bday').val(csData.bday);
	paramData = {"urUserId" : csData.urUserId};
	var checkFlag = 'N';
}

$(document).ready(function() {

	fnInitialize();	//Initialize Page Call ---------------------------------

	// 정상설정 파일첨부
    fnKendoUpload({
        id : "uploadFile",
        select : function(e) {
            let f = e.files[0];
            let ext = f.extension.substring(1, f.extension.length); // 확장자

            if (false) { // $.inArray(ext.toLowerCase(), [ "xls", "xlsx" ]) == -1
                //  fnKendoMessage({
                //                    message : "엑셀 파일만 첨부가능합니다."
                //  });
                //  e.preventDefault();
            } else {
            	if (e.files && e.files[0]) {
                    let reader = new FileReader();

                    reader.onload = function(ele) {
                        //fnGetExcelUploadUser();
                         $("#uploadUserViewControl").show();
                         $("#uploadUserLink").text(f.name);

                         //파일첨부 버튼 숨김처리
                         $("#fnFileUpload").css('display','none');
                         $("#uploadFileDeleteYn").val('');
                    };

                    reader.readAsDataURL(f.rawFile);
                }
            }
        },
        localization : "파일첨부"
    });

	//Initialize PageR
	function fnInitialize(){

		$scope.$emit('fnIsMenu', { flag : menuFlag });
		//$scope.$emit('fnIsMenu', { flag : true });

		fnPageInfo({
			PG_ID  : 'buyerPopup',
			callback : fnUI
		});

	}

	function fnUI(){
		fnInitLayout();

		fnTranslate();	// 다국어 변환--------------------------------------------

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitGrid();	//Initialize Grid ------------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------

		//레이어팝업 일때만 [고객문의관리 이동] 버튼 보이게
		if( $("#document").hasClass("ifr_popup") ) {
			$("#areaCustomerQnaSearch").show();
		}
	}

	function fnInitLayout() {

		if( $("#document").hasClass("ifr_popup") ) {
			fnAddPopupClass("fitContents");
		}
	}

	//--------------------------------- Button Start---------------------------------

	function fnInitButton(){
		$('#fnSavePutBuyer, #fnCloseA,#fnSaveRefundBank,#fnCloseB,#fnNewB,#fnNewShippingAddress,#fnSaveC,#fnCloseBuyerHist,#fnSearchC,#fnDelA,#pwdClearBtn,#fnKendoPopupBuyerHist,#fnKendoPopupPutBuyerStop,#fnSavePutBuyerStop,#fnKendoPopupPutBuyerNormal,#fnSavePutBuyerNormal,#fnKendoPopupGetBuyerGroupHist,#fnCloseD,#fnCloseE,#fnKendoPopupMaliciousClaimHist,#fnCloseE,#fnKendoPopupAddUserBlack,#fnCloseG,#fnSaveG,#fnKendoPopupUserBlackHist,#fnSaveShippingAddress,#fnIsCheckMail,#fnValidBankAccount,#fnPointAdmin,#kendoPopupEmployeeDiscountPast').kendoButton();
		$('#kendoPopup2').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupShippingAddress').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupBuyerHist').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupPutBuyerStop').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupPutBuyerNormal').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupGetBuyerGroupHist').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupGetBuyerRecommList').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupMaliciousClaimHist').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupAddUserBlack').kendoWindow({
			visible: false,
			modal: true
		});
		$('#kendoPopupEmployeeDiscountPast').kendoWindow({
            visible: false,
            modal: true
        });
	}
	function fnSearch(param){
		//환불계좌정보
		if(param =="refundBank"){
			$('#inputFormRefundBank').formClear(false);
			var data;
			data = $('#inputForm').formSerialize(false);
			data['SS_USER_ID'] = $('#UR_USER_ID').val();
			aGridDs.read(data);


			if(defaultActivateTab == 'CS_REFUND'){
//				var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
//				tabStrip.disable(tabStrip.tabGroup.children().eq(0));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(2));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(3));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(4));

				$("#addTab6").css("display","none");
			}

		//배송지관리
		}else if(param=="shippingAddress"){
			$('#inputFormShippingAddress').formClear(false);
			var data;
			data = $('#inputFormShippingAddress').formSerialize(false);
			data.urUserId = $('#urUserId').val();

			var query = {
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
			};
			bGridDs.query( query );

			if(defaultActivateTab == 'CS_SHIPPING_ADDRESS'){
//				var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
//				tabStrip.disable(tabStrip.tabGroup.children().eq(0));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(1));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(3));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(4));

				$("#addTab6").css("display","none");
			}

		//쿠폰정보
		}else if(param == 'coupon'){
			//$('#couponSearchForm').formClear(false);
			var data;
			data = $('#couponSearchForm').formSerialize(false);
			data.urUserId = $('#urUserId').val();

			var query = {
				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
			};
			hGridDs.query( query );

			if(defaultActivateTab == 'CS_COUPON'){
//				var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
//				tabStrip.disable(tabStrip.tabGroup.children().eq(0));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(1));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(2));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(4));

				$("#addTab6").css("display","none");
			}

		//적립금정보
		}else if(param == 'point'){
			//$('#pointSearchForm').formClear(false);
			var data;
			data = $('#pointSearchForm').formSerialize(false);
			data.urUserId = $('#urUserId').val();

			data.mileageStartCreateDate = data.pointStartCreateDate;
			data.mileageEndCreateDate = data.pointEndCreateDate;

			var query = {
				page         : 1
                , pageSize     : PAGE_SIZE
				, filterLength : fnSearchData(data).length
				, filter :  {
					filters : fnSearchData(data)
				}
			};
			iGridDs.query( query );

			if(defaultActivateTab == 'CS_POINT'){
//				var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
//				tabStrip.disable(tabStrip.tabGroup.children().eq(0));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(1));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(2));
//				tabStrip.disable(tabStrip.tabGroup.children().eq(3));

				$("#addTab6").css("display","none");
			}

		//임직원할인한도액
		}else if(param == 'employeeDiscount'){

            var data = $('#inputForm').formSerialize(true);

            if(data.userName == null || data.userName == ''){
                $("#employeeDisCountUserName").text("{" + $('#userName').val() + "}");
            }else{
                $("#employeeDisCountUserName").text("{" + data.userName + "}");
            }

            fnAjax({
                    url     : '/admin/ur/buyer/getEmployeeDiscount',
                    params  : {"urErpEmployeeCd" : data.urErpEmployeeCd},
                    success :
                        function( data ){
                            let employeeDiscountHtml = "<table role='grid' tabindex='-1' data-role='selectable' class='k-selectable'>";
                            employeeDiscountHtml += "<colgroup>";
                            employeeDiscountHtml += "   <col style='width:200px'>";
                            employeeDiscountHtml += "   <col style='width:100px'>";
                            employeeDiscountHtml += "   <col style='width:80px'>";
                            employeeDiscountHtml += "   <col style='width:100px'>";
                            employeeDiscountHtml += "   <col style='width:100px'>";
                            employeeDiscountHtml += "   <col style='width:100px'>";
                            employeeDiscountHtml += "</colgroup>";
                            employeeDiscountHtml += "<tbody role='rowgroup'>";

                            data.forEach(function(vo){
                                let limitAmount = vo.limitAmount;
                                let useAmount = vo.useAmount;
                                let remainAmount = vo.remainAmount;
                                let employeeDiscountLimitCycleTypeName = vo.employeeDiscountLimitCycleTypeName;
                                let list = vo.list;
                                var brandTotalRowspan = 0;
                                for (let i=0; i<list.length; i++) {
                                	brandTotalRowspan += list[i].brand.length;
                                }
                                var action = true;
                                for (let i=0; i<list.length; i++) {
                                    let brand = list[i].brand;
                                    let discountRatio = list[i].discountRatio;
                                    for (let j=0; j<brand.length; j++) {
                                        employeeDiscountHtml += "<tr>";
                                        employeeDiscountHtml += "<td style='text-align:center'>" + brand[j].brandName + "</td>";
                                        if(j === 0){
                                            employeeDiscountHtml += "<td style='text-align:center' rowspan='" + brand.length + "'>" + discountRatio + "%</td>";
                                            if(action){
	                                            employeeDiscountHtml += "<td style='text-align:center' rowspan='" + brandTotalRowspan + "'>" + employeeDiscountLimitCycleTypeName + "</td>";
	                                            employeeDiscountHtml += "<td style='text-align:center' rowspan='" + brandTotalRowspan + "'>" + fnNumberWithCommas(limitAmount) + "원</td>";
	                                            employeeDiscountHtml += "<td style='text-align:center' rowspan='" + brandTotalRowspan + "'>" + fnNumberWithCommas(useAmount) + "원</td>";
	                                            employeeDiscountHtml += "<td style='text-align:center' rowspan='" + brandTotalRowspan + "'>" + fnNumberWithCommas(remainAmount) + "원</td>";
	                                            action = false;
                                            }
                                        }
                                        employeeDiscountHtml += "</tr>";
                                    }
                                }
                            });
                            employeeDiscountHtml += "</tbody>";
                            employeeDiscountHtml += "</table>";
                            $("#employeeDiscountContent").html(employeeDiscountHtml);
                        },
                    isAction : 'select'
            });

		//블랙리스트 추가
		}else if(param == 'addUserBlack'){
			var data = $('#inputForm').formSerialize(true);

			var query = {
 				filterLength : fnSearchData(data).length,
				filter :  {
					filters : fnSearchData(data)
				}
			};
			gGridDs.query( query );
		}

	}
	function fnClear(param){
		if(param == 'couponSearchForm'){
			$('#couponSearchForm').formClear(true);
		}else if(param == 'pointSearchForm'){
			$('#pointSearchForm').formClear(true);
		}else if(param == 'employeeSearchForm'){
			$('#employeeSearchForm').formClear(true);
		}else{
			$('#searchForm').formClear(true);
		}

		$(".set-btn-type6").attr("fb-btn-active" , false );
	}

	//신규
	function fnNew(param){
		if(param =="b"){
			$('#inputFormRefundBank').formClear(true);
			$('#BANK_NAME').focus();
			aGrid.clearSelection();
			fnKendoInputPoup({id:"kendoPopup2",height:"500px" ,width:"580px",title:{key :"6031",nullMsg :'환급통장정보 수정' } });
		}else if(param=="shippingAddress"){
			$('#inputFormShippingAddress').formClear(true);
			//bGrid.clearSelection();
			var gridLength = bGrid.dataItems().length;
			if( gridLength == 0 ){
				$("input:radio[name=defaultYn]:input[value='Y']").prop('checked',true);
				$("input:radio[name=defaultYn]").prop('disabled',true);
			}else{
				$("input:radio[name=defaultYn]:input[value='N']").prop('checked',true);
				$("input:radio[name=defaultYn]").prop('disabled',false);
				$("#shippingComment").css('display','none');
				$("#accessInformationPassword").css('display','none');
			}
			fnKendoInputPoup({id:"kendoPopupShippingAddress",height:"460px" ,width:"650px",title:{nullMsg :'배송지정보' }});
		}else if(param==="employeeDiscountPast"){
            fnKendoInputPoup({id:"kendoPopupEmployeeDiscountPast",height:"410px" ,width:"500px",title:{nullMsg :'임직원 할인 내역 조회'} });
        }
	}

	//회원상태변경 컬럼 값 저장 위한 object세팅
	function fnChangeLogDataSetting(formData,beforeData){
		var obj = new Object();
		obj["before"] = beforeData;

		//불필요한 데이터 삭제
		delete formData.mobile1;
		delete formData.mobile2;
		delete formData.mobile3;
		delete formData.mail1;
		delete formData.mail2;
		delete formData.mail3;
		delete formData.employeeType;
		delete formData.employeeInfo;
		delete formData.erpRegalName;
		delete formData.erpPositionName;
		delete formData.urErpEmployeeCd;
//		delete formData.bankCode;

		obj["after"]  = formData;
		formData["changeLogArray"] = kendo.stringify( fnCheckChangeLog( obj ) );

		return formData;
	}

	function fnCheckChangeLog( obj ){
		var data =obj["after"];
		var searchData = new Array();
		var subColums;
		var bfData = obj["before"];
		//환불계좌 데이터
		var refundBankData;

		if(bfData != undefined ){
			$.each(data, function(key, value) {
				if(key =="rtnValid") return;

				if(bfData[key] == null) bfData[key] == "";

				if( bfData[key] != value && bfData[key] != "" && value != "" && bfData[key] != undefined){
					if (key.toUpperCase() == 'BANKCODE') return true; //bankcode 제외

					subColums= new Object();
					subColums['BEFORE_DATA'] = bfData[key];
					subColums['AFTER_DATA'] = value;
					subColums['COLUMN_NM'] = key;
					subColums['COLUMN_NM_UPPER_CASE'] = key.toUpperCase();
					searchData.push(subColums);
				}

			});

		//환불계좌 추가일때
		}else if(bfData == undefined && data.accountNumber != "" ){
			refundBankData = new Object();
			refundBankData['accountNumber'] = data.accountNumber;
			refundBankData['bankName'] = data.bankName;
			refundBankData['holderName'] = data.holderName;

			$.each(refundBankData, function(key, value) {
				if(key =="rtnValid") return;

				if( value != "" ){
					subColums= new Object();
					subColums['BEFORE_DATA'] = "";
					subColums['AFTER_DATA'] = value;
					subColums['COLUMN_NM'] = key;
					subColums['COLUMN_NM_UPPER_CASE'] = key.toUpperCase();
					searchData.push(subColums);
				}

			});
		}
		return searchData;
	}

	//저장
	function fnSave(param){
		if(param =='putBuyer'){
			var data = $('#inputForm').formSerialize(true);
			if(!$('input[name=smsYn]').prop('checked')){
				data.smsYn ='N';
			}
			if(!$('input[name=mailYn]').prop('checked')){
				data.mailYn ='N';
			}

			if (personalInformationAccessYn =="Y")
	        {
    			data.mobile = data.mobile1 + data.mobile2 + data.mobile3;

    			var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;
    			if(!regExp.test(fnPhoneNumberHyphen(data.mobile))) {
    				return valueCheck("", "휴대폰 형식을 확인해주세요.", 'mobile2');
    			}

    			var afterMail = data.mail1 +'@'+ (data.mail2 == 'write' ?data.mail3:data.mail2);
    			if(data.mail != afterMail && !isCheckMail){
    				return valueCheck("", "이메일 중복체크를 해주세요.", 'mail1');
    			}

    			data.mail = data.mail1 +'@'+ (data.mail2 == 'write' ?data.mail3:data.mail2);
	        }

			data.eventJoinYn = (data.eventJoinYn == '' ? null : data.eventJoinYn);

			var url  = '/admin/ur/buyer/addBuyer';
			var cbId = 'insert';

			if( OPER_TP_CODE == 'U' ){
				url  = '/admin/ur/buyer/putBuyer';
				cbId= 'update';

				var settingData = fnChangeLogDataSetting(data, BEFORE_OBJECT);
			}

			if( data.rtnValid ){

				fnAjax({
					url     : url,
					params  : settingData,
					success :
						function( data ){
							fnKendoMessage({message : fnGetLangData({nullMsg :'수정되었습니다.' }),ok:function(e){
								//fnBuyerSearch(data);
								fnClose();
							}});
						},
					isAction : 'batch'
				});
			}

		}else if(param =='refundBank'){

			var data = $('#inputForm,#inputFormRefundBank').formSerialize(true);
			data.urUserId = $("#urUserId").val();

			var url  = '/admin/ur/userRefund/addRefundBank';
			var cbId = 'insert';

			if( OPER_TP_CODE == 'U' ){
				url  = '/admin/ur/userRefund/putRefundBank';
				cbId= 'update';
			}

			data = fnChangeLogDataSetting(data, BEFORE_OBJECT);

			if(!isValidBankAccount){
				return valueCheck("", "유효계좌인증을 해주세요.", '');
			}

			if( data.rtnValid ){
				fnAjax({
					url     : url,
					params  : data,
					success :
						function( data ){
							if(cbId =="insert"){
								fnKendoMessage({message : fnGetLangData({nullMsg :'입력되었습니다.' }),ok:function(e){fnRefundBank(paramData);}});
							}else{
								fnKendoMessage({message : fnGetLangData({nullMsg :'수정되었습니다.' }),ok:function(e){fnRefundBank(paramData);}});
							}
						},
					isAction : 'batch'
				});
			}
		}else if(param =='shippingAddress'){
			var url  = '/admin/ur/userShipping/addShippingAddress';
			var cbId = 'insert';

			if( OPER_TP_CODE == 'U' ){
				url  = '/admin/ur/userShipping/putShippingAddress';
				cbId= 'update';
			}
			var data = $('#inputFormShippingAddress').formSerialize(true);
			//console.dir("data==?"+JSON.stringify(data));
			data.urUserId = $("#urUserId").val();


			if(data.shippingComment == "") data.shippingComment = data.shippingCommentType;

			if( data.rtnValid ){


				if($("#receiverName").val().length < 2){
					return valueCheck("", "받는 사람은 최소 2자 ~ 최대 20자로 입력 가능합니다.", 'receiverName');
				}

				var regExpReceiverName = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"^0-9]/;
				if(regExpReceiverName.test($("#receiverName").val())) {
					return valueCheck("", "받는사람 정보에 숫자와 특수문자는 입력할 수 없습니다.", 'receiverName');
				}

				data.receiverMobile = data.receiverMobile1 + data.receiverMobile2 + data.receiverMobile3;
				var regExp = /^(01[016789]{1}|02|0[3-9]{1}[0-9]{1})-?[0-9]{3,4}-?[0-9]{4}$/;
				if(!regExp.test(fnPhoneNumberHyphen(data.receiverMobile))) {
					return valueCheck("", "휴대폰 형식을 확인해주세요.", 'receiverMobile2');
				}

				fnAjax({
					url     : url,
					params  : data,
					success :
						function( data ){

							if(cbId =="insert"){
								fnKendoMessage({message : fnGetLangData({nullMsg :'배송지가 저장되었습니다.' }),ok:function(e){
									fnSearch('shippingAddress');
									fnClose('shippingAddress');
								}});
							}else{
								fnKendoMessage({message : fnGetLangData({nullMsg :'배송지가 수정되었습니다.' }) ,ok :function(e){
									fnSearch('shippingAddress');
									fnClose('shippingAddress');
								}});;
							}
						},
					isAction : 'batch'
				});
			}

		}else if(param == 'putBuyerStop'){

			var data = $('#inputFormPutBuyerStop').formSerialize(true);
			var urUserId = $("#urUserId").val();

			if($("#stopReason").val() == ""){
				return valueCheck("", "정지사유를 입력해 주세요.", 'stopReason');
			}

			if( data.rtnValid ){
				fnAjax({
					url     : '/admin/ur/buyerStatus/putBuyerStop',
					params  : {urUserId : urUserId, reason : data.stopReason },
					success :
						function( data ){

						fnKendoMessage({message : fnGetLangData({nullMsg :'정지회원으로 변경되었습니다.' }) ,ok :function(e){
							fnBuyerSearch(data);
							fnClose('putBuyerStop');
						}});;
					},
					isAction : 'batch'
				});
			}
		}else if(param == 'putBuyerNormal'){

			var data = $('#inputFormPutBuyerNormal').formSerialize(true);
			data.urUserId =  $("#urUserId").val();
			data.reason = data.normalReason;

			if($("#normalReason").val() == ""){
				return valueCheck("", "정상전환사유를 입력해 주세요.", 'stopReason');
			}

			if( data.rtnValid ){

				fnAjaxSubmit({
	                form    : 'inputFormPutBuyerNormal',
	                fileUrl : '/fileUpload',
	                url     : '/admin/ur/buyerStatus/putBuyerNormal',
	                storageType : "public", // 추가
	                domain : "ur", 			// 추가
	                params  : data,
	                success : function( successData ){
	                	fnKendoMessage({message : fnGetLangData({nullMsg :'정상회원으로 변경되었습니다.' }) ,ok :function(e){
							fnBuyerSearch(data);
							fnClose('putBuyerNormal');
						}});;
	                },
	                isAction : 'insert'
	            });

			}
		}else if(param == 'addUserBlack'){

			//블랙리스트 등록
			var data = $('#inputFormAddUserBlack').formSerialize(true);
			var urUserId = $("#urUserId").val();

			if( data.rtnValid ){
				fnAjax({
					url     : '/admin/ur/userBlack/addUserBlack',
					params  : {urUserId : urUserId, userBlackReason : data.blackReason},
					success :
						function( data ){

						fnKendoMessage({message : fnGetLangData({nullMsg :'블랙리스트 사유가 등록됐습니다.' }) ,ok :function(e){
							fnBuyerSearch(data);
							fnClose('addUserBlack');
						}});;

					},
					isAction : 'batch'
				});
			}
		}
	}

	function fnDel(){
		fnKendoMessage({message:fnGetLangData({nullMsg :'탈퇴 하시겠습니까?' }), type : "confirm" ,ok :fnDelApply  });
	}
	function fnDelApply(){
		var url  = '/admin/ur/buyer/addBuyerDrop';
		var cbId = 'delete';
		var data = $('#inputForm').formSerialize(true);

		if( data.urUserId == null || data.urUserId == '' || data.urUserId == 'undefined'){
		}else{
			fnAjax({
				url     : url,
				params  : { "urUserId" : data.urUserId },
				success :
					function( data ){
						fnBizCallback(cbId, null);
					},
				isAction : 'batch'
			});
		}
	}
	function fnClose(params){
		if(params){
			//parent.POP_PARAM = params;
			if(params == 'shippingAddress'){
				var kendoWindow =$('#kendoPopupShippingAddress').data('kendoWindow');
				kendoWindow.close();
			}else if(params == 'buyerHist'){
				var kendoWindow =$('#kendoPopupBuyerHist').data('kendoWindow');
				kendoWindow.close();
			}else if(params == 'putBuyerStop'){
				var kendoWindow =$('#kendoPopupPutBuyerStop').data('kendoWindow');
				kendoWindow.close();
			}else if(params == 'putBuyerNormal'){
				var kendoWindow =$('#kendoPopupPutBuyerNormal').data('kendoWindow');
				kendoWindow.close();
			}else if(params == 'buyerGroupHist'){
				var kendoWindow =$('#kendoPopupGetBuyerGroupHist').data('kendoWindow');
				kendoWindow.close();
			}else if(params == 'buyerRecommList'){
				var kendoWindow =$('#kendoPopupGetBuyerRecommList').data('kendoWindow');
				kendoWindow.close();
			}else if(params == 'maliciousClaimHist'){
				var kendoWindow =$('#kendoPopupMaliciousClaimHist').data('kendoWindow');
				kendoWindow.close();
			}else if(params == 'addUserBlack'){
				var kendoWindow =$('#kendoPopupAddUserBlack').data('kendoWindow');
				kendoWindow.close();
			}
		}else{
			parent.LAYER_POPUP_OBJECT.data('kendoWindow').close();
		}
	}

	//--------------------------------- Button End---------------------------------


	//------------------------------- Grid Start -------------------------------
	function fnInitGrid(){
		aGridDs = fnGetDataSource({
			url      : '/admin/ur/userRefund/getRefundBank'
		});
		aGridOpt = {
			dataSource: aGridDs
			,navigatable: true
			,height:490
			,scrollable: true
			,columns   : [
				{ field:'bankName'			,title : '은행명'		, width:'110px'	,attributes:{ style:'text-align:left' }}
				,{ field:'holderName'		,title : '예금주'		, width:'80px'	,attributes:{ style:'text-align:left' }}
				,{ field:'accountNumber'	,title : '계좌번호'	, width:'130px'	,attributes:{ style:'text-align:left' }}
				,{ field:'urRefundBankId'	,hidden: true}
			]
		};
		aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();

		/*$("#aGrid").on("click", "tbody>tr", function () {
				fnGridClick();
		});*/

		bGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/userShipping/getShippingAddressList'
		});
		bGridOpt = {
			dataSource: bGridDs
			//,navigatable: true
			//,height:490
			,scrollable: true
			,columns   : [
				{ field:'receiverName'		,title : '받는사람'	, width:'10%',attributes:{ style:'text-align:center' }}
				,{ field:'receiverMobile'	,title : '휴대폰'	, width:'10%',attributes:{ style:'text-align:center' }
					, template: function(dataItem) {
						var html = kendo.htmlEncode(dataItem.receiverMobile);
						return fnPhoneNumberHyphen(html);
	                }
				}
				,{ field:'receiverZipCode'	,title : '우편번호'	, width:'10%',attributes:{ style:'text-align:center' }}
				,{ field:'receiverAddress'	,title : '주소'		, width:'20%',attributes:{ style:'text-align:center' }}
				,{ field:'deliveryInfo'	    ,title : '배송 가능 유형'	, width:'20%',attributes:{ style:'text-align:center' }
                    , template: function(dataItem) {
                        let result = "";
                        if(dataItem.delivery.shippingCompDelivery){
                            result += "[택배]";
                        }
                        if(dataItem.delivery.dawnDelivery){
                            result += " [새벽]";
                        }
                        if(dataItem.delivery.dailyDelivery){
                            result += " [일일]";
                        }
                        if(dataItem.delivery.dailyDeliveryType == "STORE_DELIVERABLE_ITEM.ALL" || dataItem.delivery.dailyDeliveryType == "STORE_DELIVERABLE_ITEM.FD"){
                            result += " [녹즙]"
                        }
                        if(dataItem.delivery.storeDelivery){
                            result += " [매장]"
                        }
						return result;
	                }
				}
				,{ field:'deliveryStoreName',title : '매장배송 지점'	, width:'15%',attributes:{ style:'text-align:center' }
				    , template: function(dataItem) {
				        let result = "";
                        if(dataItem.delivery.storeName != null){
                            for(let name of dataItem.delivery.storeName){
                                result += "[" + name + "]";
                            }
                        }
                        return result;
                    }
				}
				,{ field:'defaultYn'		,title : '기본'		, width:'10%',attributes:{ style:'text-align:center' }
				,template : "#=(defaultYn=='Y')?fnGetLangData({nullMsg :'예' }):fnGetLangData({nullMsg :'아니오' })#"
				}
				,{ field:'urShippingAddrId'	,hidden : true}
				,{ field:'buildingCode'  ,hidden : true}
			]
		};
		bGrid = $('#bGrid').initializeKendoGrid( bGridOpt ).cKendoGrid();

		$("#bGrid").on("click", "tbody>tr", function () {
				fnBGridClick();
		});



		// 회원상태 변경이력 리스트
		cGridDs = fnGetDataSource({
			url      : '/admin/ur/buyerStatus/getBuyerStatusHistoryList'
		});
		cGridOpt = {
			dataSource: cGridDs
			,navigatable: true
			,height:300
			,scrollable: true
			,columns   : [
				{ field:'status'			,title : '상태'	, width:'15%',attributes:{ style:'text-align:center' }}
				,{ field:'reason'			,title : '사유'	, width:'40%',attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '등록일'	, width:'20%',attributes:{ style:'text-align:center' }}
				,{ field:'createLoginId'	,title : '등록자'	, width:'25%',attributes:{ style:'text-align:center' }}
			]
		};
		cGrid = $('#cGrid').initializeKendoGrid( cGridOpt ).cKendoGrid();


		// 회원등급 변경이력 리스트
		dGridDs = fnGetDataSource({
			url      : '/admin/ur/buyer/getBuyerGroupChangeHistoryList'
		});
		dGridOpt = {
			dataSource: dGridDs
			,navigatable: true
			,height:300
			,scrollable: true
			,columns   : [
				{ field:'groupName'			,title : '등급'	, width:'40%',attributes:{ style:'text-align:center' }}
				,{ field:'createDate'		,title : '등록일'	, width:'60%',attributes:{ style:'text-align:center' }}
			]
		};
		dGrid = $('#dGrid').initializeKendoGrid( dGridOpt ).cKendoGrid();

		// 나를 추천한 추천인 리스트
		eGridDs = fnGetDataSource({
			url      : '/admin/ur/buyer/getBuyerRecommendList'
		});
		eGridOpt = {
			dataSource: eGridDs
			,navigatable: true
			,height:300
			,scrollable: true
			,columns   : [
				{ field:'recommendId'				,title : '추천인'		, width:'40%',attributes:{ style:'text-align:center' }}
				,{ field:'recommendCreateDate'		,title : '추천등록일'	, width:'60%',attributes:{ style:'text-align:center' }}
			]
		};
		eGrid = $('#eGrid').initializeKendoGrid( eGridOpt ).cKendoGrid();


		// 악성클레임 리스트
		fGridDs = fnGetDataSource({
			url      : '/admin/ur/userMaliciousClaim/getUserMaliciousClaimHistoryList'
		});
		fGridOpt = {
			dataSource: fGridDs
			,navigatable: true
			// ,height:300
			,scrollable: true
			,columns   : [
				{ field:'rowNumber'					,title : 'No.'	, width:'10%',attributes:{ style:'text-align:center' }}
				,{ field:'stClassificationName'		,title : '분류명'	, width:'30%',attributes:{ style:'text-align:center' }}
				,{ field:'maliciousClaimReason'		,title : '내용'	, width:'30%',attributes:{ style:'text-align:center' }}
				,{ field:'maliciousClaimCreateDate'	,title : '등록일'	, width:'30%',attributes:{ style:'text-align:center' }}
			]
		};
		fGrid = $('#fGrid').initializeKendoGrid( fGridOpt ).cKendoGrid();

		// 블랙리스트 리스트
		gGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/userBlack/getUserBlackHistoryList'
		});
		gGridOpt = {
			dataSource: gGridDs
			//,navigatable: true
			,height: 200
			,scrollable: true
			,columns   : [
					{ field:'rowNumber'			,title : 'No.'		, width:'30px'	,attributes:{ style:'text-align:center' }}
					,{ field:'reason'			,title : '사유'		, width:'200px'	,attributes:{ style:'text-align:left' }}
					,{ field:'createDate'		,title : '등록일'		, width:'60px'	,attributes:{ style:'text-align:center' }}
					,{ field:'registerUserName'	,title : '등록자'		, width:'80px'	,attributes:{ style:'text-align:center' }}
			]
		};
		gGrid = $('#gGrid').initializeKendoGrid( gGridOpt ).cKendoGrid();



		// 쿠폰정보 리스트
		hGridDs = fnGetPagingDataSource({
			url      : '/admin/ur/buyer/getCouponList'
		});
		hGridOpt = {
			dataSource: hGridDs
			//,navigatable: true
			//,height: 200
			,scrollable: true
			,columns   : [
					{ field: 'rowNumber'		 ,title: 'No.'						, width:'30px'	,attributes:{ style:'text-align:center' }}
					,{ field:'pmCouponId' 		 ,title : '쿠폰코드'					, width:'60px'	,attributes:{ style:'text-align:center;text-decoration: underline;color:blue;' }}
					,{ field:'pmCouponIssueId' 	 ,title : '발급번호'					, width:'60px'	,attributes:{ style:'text-align:center' }}
					,{ field:'couponTpNm' 		 ,title : '쿠폰종류'					, width:'70px'	,attributes:{ style:'text-align:center' }}
					,{ field:'displayCouponName' ,title : '쿠폰명'					, width:'200px'	,attributes:{ style:'text-align:center' }}
					,{ field:'discountValueName' ,title : '할인상세 (금액OR할인율)'	, width:'80px'	,attributes:{ style:'text-align:center' }}
					,{ field:'minPaymentAmount'  ,title : '최소결제금액'				, width:'80px'	,attributes:{ style:'text-align:right' }}
					,{ field:'couponUseYn'		 ,title : '사용여부'					, width:'60px'	,attributes:{ style:'text-align:center' }}
					,{ field:'issueStartDate'	 ,title : '발급일'					, width:'80px'	,attributes:{ style:'text-align:center' }}
					,{ field:'validityDate'		 ,title : '사용/유효기간'			, width:'100px'	,attributes:{ style:'text-align:center' }}
			]
		};
		hGrid = $('#hGrid').initializeKendoGrid( hGridOpt ).cKendoGrid();
		hGrid.bind("dataBound", function(){

			var couponTotal = hGridDs._total;
			var dataList = [];
			var availableCouponTotal = 0;
			if(couponTotal > 0){
				dataList = hGridDs._data;
				for(var i = 0 ; i < dataList.length ; i++){
					if(dataList[i].availableCouponYn == 'Y'){
						availableCouponTotal++;
					}
				}
			}

			$('#couponTotal').text(availableCouponTotal);
        });

		$(hGrid.tbody).on("click", "td", function (e) {

			var row = $(this).closest("tr");
			var rowIdx = $("tr", hGrid.tbody).index(row);
			var colIdx = $("td", row).index(this);
			if(colIdx == 1){
				fnCouponGridClick(e);
			}
		});

		function fnCouponGridClick(e){
			var dataItem = hGrid.dataItem($(e.target).closest('tr'));

			fnKendoPopup({
				id     : 'cpnMgmPut',
				title  : '쿠폰 상세 정보',
				src    : '#/cpnMgmPut',
				param  : {pmCouponId :dataItem.pmCouponId },
				width  : '1400px',
				height : '1400px',
				success: function( id, data ){
					if(data == 'SAVE'){
						fnSearch();
					}
				}
			});

		};


		// 적립금정보 리스트
		iGridDs = fnGetPagingDataSource({
			url      : "/admin/ur/buyer/getPointInfo"
				, pageSize : PAGE_SIZE
				, requestEnd: function(e) {
	            }
		});
		iGridOpt = {
			dataSource: iGridDs
//			,pageable  : {
//                pageSizes   : [1, 5, 10],
//                buttonCount : 10
//              }
			,navigatable: true
			,scrollable: true
			,columns   : [
					{field : 'rowNumber'       			,title : 'No.'   	, width: '80px' , attributes : {style : 'text-align:center'}}
					,{ field:'paymentTypeName'		,title : '구분'		    , width:'80px'	,attributes:{ style:'text-align:center' }}
					,{ field:'pointTypeName'		,title : '내용'		    , width:'200px'	,attributes:{ style:'text-align:center' }}
					,{ field:'cmnt'					,title : '지급/차감사유'  , width:'*'	,attributes:{ style:'text-align:left' }}
					,{ field:'odid'					,title : '주문번호'      , width:'200PX'	,attributes:{ style:'text-align:center' }}
					,{ field:'amount'				,title : '적립금'	    , width:'150px'	,attributes:{ style:'text-align:right' }, type:"number", format: "{0:n0}"}
					,{ field:'validityDay'			,title : '유효기간'	    , width:'180px'	,attributes:{ style:'text-align:center' }, template: '#= validityStartDay# ~ #= validityEndDay#'}
					,{ field:'createDate'			,title : '발생일'		, width:'100px'	,attributes:{ style:'text-align:center' }}
			]
		};

		iGrid = $('#iGrid').initializeKendoGrid( iGridOpt ).cKendoGrid();

		iGrid.bind("dataBound", function(){

//			var row_num = iGridDs._total - ((iGridDs._page - 1) * iGridDs._pageSize);
//			$("#iGrid tbody > tr .row-number").each(function(index){
//				$(this).html(row_num);
//				row_num--;
//
//			});
//			if(iGridDs._data.length >0){
//				$('#pointTotal').text(kendo.toString(iGridDs._data[0].totalPoint, "n0"));
				$('#pointTotal').text(kendo.toString(iGridDs._total,"n0"));
//			}
        });

		// 임직원할인한도액 리스트
//		jGridDs = fnGetPagingDataSource({
//			url      : ''
//		});
//		jGridOpt = {
//			dataSource: jGridDs
//			//,navigatable: true
//			//,height: 200
//			,scrollable: true
//			,columns   : [
//					{ field:'rowNumber'		,title : 'No.'			, width:'30px'	,attributes:{ style:'text-align:center' }}
//					,{ field:'groupName'	,title : '그룹명'			, width:'60px'	,attributes:{ style:'text-align:center' }}
//					,{ field:'period'		,title : '혜택기간'		, width:'200px'	,attributes:{ style:'text-align:center' }}
//					,{ field:'limitPrice'	,title : '임직원 할인한도액'	, width:'100px'	,attributes:{ style:'text-align:center' }}
//					,{ field:'usePrice'		,title : '사용금액'		, width:'60px'	,attributes:{ style:'text-align:center' }}
//					,{ field:'restPrice'	,title : '잔여한도액'		, width:'80px'	,attributes:{ style:'text-align:center' }}
//					,{ field:'createDate'	,title : '사용일'			, width:'80px'	,attributes:{ style:'text-align:center' }}
//			]
//		};
//		jGrid = $('#jGrid').initializeKendoGrid( jGridOpt ).cKendoGrid();
        // 임직원할인한도액 리스트

        // 임직원할인한도액 과거내역 리스트
        edpGridDs = fnGetDataSource({
            url      : '/admin/ur/buyer/getEmployeeDiscountPastInfo',
            requestEnd : function(e){
                $("#employeeDiscountSum").text("{" + e.response.data.sumAmount + "}");
            }
        });
        edpGridOpt = {
            dataSource: edpGridDs
            ,navigatable: true
            ,height:300
            ,scrollable: true
            ,columns   : [
                { field:'brandName'			,title : '브랜드'	, width:'60%',attributes:{ style:'text-align:center' }}
                ,{ field:'useAmount'		,title : '사용 금액'	, width:'40%',attributes:{ style:'text-align:center' }}
            ]
        };
        edpGrid = $('#edpGrid').initializeKendoGrid( edpGridOpt ).cKendoGrid();
	}

//	function fnGridClick(){
//		var map = aGrid.dataItem(aGrid.select());
//		fnAjax({
//				url     : '/biz/ur/refund/getRefundBank',
//				params  : {UR_REFUND_BANK_ID : map.UR_REFUND_BANK_ID},
//				success :
//					function( data ){
//						BEFORE_OBJECT = new Object();
//						BEFORE_OBJECT = data.rows;
//						$('#inputFormRefundBank').bindingForm( data,'rows', true);
//						fnKendoInputPoup({id:"kendoPopup2",height:"410px" ,width:"550px",title:{key :"6011",nullMsg :'환급통장정보 수정' } });
//					},
//				isAction : 'select'
//		});
//
//	};
	function fnBGridClick(){
		var map = bGrid.dataItem(bGrid.select());

		fnAjax({
				url     : '/admin/ur/userShipping/getShippingAddress',
				params  : {urShippingAddrId : map.urShippingAddrId},
				success :
					function( data ){

						var mobileHyphen = fnPhoneNumberHyphen(data.rows.receiverMobile);
						var mobile = mobileHyphen.split('-');
						data.rows.receiverMobile1 = mobile[0];
						data.rows.receiverMobile2 = mobile[1];
						data.rows.receiverMobile3 = mobile[2];

						//출입정보
						if(data.rows.accessInformationType == "ACCESS_INFORMATION.FRONT_DOOR_PASSWORD" || data.rows.accessInformationType == "ACCESS_INFORMATION.ETC"){
							$("#accessInformationPassword").css("display","");
						}else{
							$("#accessInformationPassword").css("display","none");
						}

						//배송 요청사항
						if(data.rows.shippingComment != "직접입력") data.rows.shippingCommentType = data.rows.shippingComment;

						$('#inputFormShippingAddress').bindingForm( data,'rows', true);

						//배송요청사항
						if($("#shippingCommentType").val() == "" && data.rows.shippingCommentType != ''){
							$("#shippingComment").css("display","");
							$("#shippingComment").val(data.rows.shippingCommentType);
							$("#shippingCommentType").data('kendoDropDownList').value('직접입력');
						}

						fnKendoInputPoup({id:"kendoPopupShippingAddress",height:"500px" ,width:"650px",title:{nullMsg :'배송지정보' } });

					},
					isAction : 'select'
		});

	};
	//-------------------------------  Grid End  -------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){
		var tabStrip = $("#tabstrip").kendoTabStrip({
									animation:  {
										open: {
											effects: "fadeIn"
										}
									},
									activate : function(e) {

														// 계좌인증 Text 초기화
														isValidBankAccount = false;
														$("#validBankAccountDiv").text('');

														var thisId = $(e.item).attr('id');
														OPER_TP_CODE = "N";

														if((defaultActivateTab == 'CS_REFUND' && $(e.item).attr('id') =='addTab2') ||
																(defaultActivateTab == 'CS_REFUND' && $(e.item).attr('id') =='defaultTab')){
															if(thisId == 'defaultTab' && checkFlag == 'Y'){
																$("#tabstrip").data("kendoTabStrip").select(0);
																thisId = 'defaultTab';
															}else{
																$("#tabstrip").data("kendoTabStrip").select(1);
																thisId = 'addTab2';
																checkFlag = 'Y';
															}
														}else if((defaultActivateTab == 'CS_SHIPPING_ADDRESS' && $(e.item).attr('id') =='addTab3' ) ||
																	(defaultActivateTab == 'CS_SHIPPING_ADDRESS' && $(e.item).attr('id') =='defaultTab')){
															if(thisId == 'defaultTab' && checkFlag == 'Y'){
																$("#tabstrip").data("kendoTabStrip").select(0);
																thisId = 'defaultTab';
															}else{
																$("#tabstrip").data("kendoTabStrip").select(2);
																thisId = 'addTab3';
																checkFlag = 'Y';
															}
														}else if((defaultActivateTab == 'CS_COUPON' && $(e.item).attr('id') =='addTab4') ||
																	(defaultActivateTab == 'CS_COUPON' && $(e.item).attr('id') =='defaultTab')){
															if(thisId == 'defaultTab' && checkFlag == 'Y'){
																$("#tabstrip").data("kendoTabStrip").select(0);
																thisId = 'defaultTab';
															}else{
																$("#tabstrip").data("kendoTabStrip").select(3);
																thisId = 'addTab4';
																checkFlag = 'Y';
															}
														}else if((defaultActivateTab == 'CS_POINT' && $(e.item).attr('id') =='addTab5') ||
																(defaultActivateTab == 'CS_POINT' && $(e.item).attr('id') =='defaultTab')){
															if(thisId == 'defaultTab' && checkFlag == 'Y'){
																$("#tabstrip").data("kendoTabStrip").select(0);
																thisId = 'defaultTab';
															}else{
																$("#tabstrip").data("kendoTabStrip").select(4);
																thisId = 'addTab5';
																checkFlag = 'Y';
															}
														}

														//기본정보
														if(thisId =="defaultTab"){
															if(paramData){
																if(paramData.urUserId){
																	if(paramData.type == 'userDorm'){
																		//휴면회원 상세조회
																		fnBuyerMoveSearch(paramData);
																	}else{
																		//일반회원 상세 조회
																		fnBuyerSearch(paramData);
																	}
																}else{
																	var tabStrip = $("#tabstrip").data("kendoTabStrip");
																	tabStrip.disable(tabStrip.tabGroup.children().eq(1));
																	tabStrip.disable(tabStrip.tabGroup.children().eq(2));
																}
															}else{
																var tabStrip = $("#tabstrip").data("kendoTabStrip");
																	tabStrip.disable(tabStrip.tabGroup.children().eq(1));
																	tabStrip.disable(tabStrip.tabGroup.children().eq(2));
															}
														//환불계좌정보
														}else if(thisId =="addTab2"){
															fnRefundBank(paramData);
															//fnSearch('refundBank');
														//배송지관리
														}else if(thisId =="addTab3"){
															fnSearch('shippingAddress');
														//쿠폰정보
														}else if(thisId =="addTab4"){
															fnSearch('coupon');
														//적립금정보
														}else if(thisId =="addTab5"){
															fnSearch('point');
														//임직원할인한도액
														}else if(thisId =="addTab6"){
															fnSearch('employeeDiscount');
														}
													}
												}).data("kendoTabStrip");
		tabStrip.activateTab( $('#defaultTab') );


		fnKendoDropDownList({
			id  : 'eventLimitTp',
			data  : [
				{"CODE":"Y"	,"NAME":"예"},
				{"CODE":"N"	,"NAME":"아니오"}
			],
			valueField : "CODE",
			textField :"NAME",
			blank : "전체"
		});

		//기본정보 tab
		fnTagMkRadio({
			id    : 'marketingYn',
			data  : [
				{ "CODE" : "Y" , "NAME":"동의"},
				{ "CODE" : "N" , "NAME":"거부"}
			],
			tagId : 'marketingYn'
		});
		fnTagMkChkBox({
			id    : 'smsYn',
			data  : [
				{ "CODE" : "Y" , "NAME":"SMS"}
			],
			tagId : 'smsYn'
		});
		fnTagMkChkBox({
			id    : 'mailYn',
			data  : [
				{ "CODE" : "Y" , "NAME":"EMAIL"}
			],
			tagId : 'mailYn'
		});
		fnTagMkRadio({
			id    : 'pushYn',
			data  : [
				{ "CODE" : "Y" , "NAME":"수신"},
				{ "CODE" : "N" , "NAME":"거부"}
			],
			tagId : 'pushYn'
		});
		fnTagMkRadio({
			id    :  "gender",
			data  : [
						{ "CODE" : "M" , "NAME":'남'}
					,	{ "CODE" : "F" , "NAME":'여'}
					, 	{ "CODE" : "O" , "NAME":'기타'}
					],
			tagId : "gender"
		});
		fnTagMkRadio({
			id    :  "eventJoinYn",
			data  : [
						{ "CODE" : "Y" , "NAME":'예'}
					,	{ "CODE" : "N" , "NAME":'아니오'}
					],
			tagId : "eventJoinYn"
		});

		// 정상설정popup > 첨부파일 삭제
        $("#uploadUserDelete").on("click", function(e){
            e.preventDefault();
            $("#uploadUserViewControl").hide();
            $("#uploadUserLink").text("");
            $("#uploadFileDeleteYn").val("Y");
            $("#fnFileUpload").css('display','');
        });


		//배송지관리 tab
		fnTagMkRadio({
			id    :  "shippingDefaultYn"
			,data  :  [
							{ "CODE" : "Y" , "NAME":'예'}
						, 	{ "CODE" : "N" , "NAME":'아니오'}]
			,tagId : "defaultYn"
			,chkVal: false
		});
		fnTagMkRadioYN({id: "intputActive" , tagId : "USE_YN",chkVal: 'Y'});
		//배송요청사항
		var shippingCommentTypeDropDownList = fnKendoDropDownList({
			id  : 'shippingCommentType',
			data  : [
				{"CODE":"부재시 경비실에 맡겨주세요."		,"NAME":"부재시 경비실에 맡겨주세요."},
				{"CODE":"부재시 문 앞에 놓아주세요."		,"NAME":"부재시 문 앞에 놓아주세요."},
				{"CODE":"부재시 휴대폰으로 연락바랍니다."	,"NAME":"부재시 휴대폰으로 연락바랍니다."},
				{"CODE":"배송 전 연락바랍니다."			,"NAME":"배송 전 연락바랍니다."},
				{"CODE":"직접입력"						,"NAME":"직접입력"}
			],
			textField  :"NAME",
			valueField : "CODE",
			blank : "선택"
		});
		shippingCommentTypeDropDownList.bind('change',function(e){
			if($("#shippingCommentType").val() == '직접입력'){
				 $("#shippingComment").css("display","");
				 $("#shippingComment").val('');
			}else{
				 $("#shippingComment").css("display","none");
				 $("#shippingComment").val('');
			}
		});
		//출입정보
		var accessInformationDropDownList = fnKendoDropDownList({
			id         : 'accessInformationType',
			url        : "/admin/comn/getCodeList",
			autoBind   : true,
			blank      : '선택',
			params : {"stCommonCodeMasterCode" : "ACCESS_INFORMATION", "useYn" :"Y"},
		});
		accessInformationDropDownList.bind('change', function(e){
			if($("#accessInformationType").val() == 'ACCESS_INFORMATION.FRONT_DOOR_PASSWORD' || $("#accessInformationType").val() == 'ACCESS_INFORMATION.ETC' ){
				$("#accessInformationPassword").css("display","");
				$("#accessInformationPassword").val('');
			}else{
				$("#accessInformationPassword").css("display","none");
				$("#accessInformationPassword").val('');
			}
		});


		// 환불계좌관리 tab
		var bankCodeDropDownList = fnKendoDropDownList({
			id         : 'bankCode',
			url        : "/admin/comn/getCodeList",
			autoBind   : true,
			blank      : '선택',
			params : {"stCommonCodeMasterCode" : "BANK_CODE", "useYn" :"Y"},
		});
		bankCodeDropDownList.bind('change', function(e){
			//select 때문에 -1처리
			var index = this.select()-1;
			var param = this.dataSource.data()[index];
			$("#bankName").val(param.NAME);
			isValidBankAccount = false;
			$("#validBankAccountDiv").text('');
		});

		$("#accountNumber, #holderName").bind('blur', function(e){
			isValidBankAccount = false;
			$("#validBankAccountDiv").text('');
		});

		/*fnAjax({
				url     : '/biz/ur/user/getDefaultUserGroupIdByShop',
				//params  : {ID : paramData.UR_BUYER_ID},
				success :
					function( data ){
						$('#UR_GROUP_ID').val(data.rows.UR_GROUP_ID);
						$('#GRP_NAME').val(data.rows.GRP_NAME);
						//fnBizCallback("select",data);
					},
				isAction : 'select'
		});*/
		//숫자체크
//        if (personalInformationAccessYn =="Y")
//        {
    		fnValidateNum('mobile2');
    		fnValidateNum('mobile3');

    		$("#mail2").on('change',function(e){
    			if($("#mail2").val() == 'write'){
    				$("#mail3").css("display","");
    				$("#mail3").val("");
    			}else{
    				$("#mail3").css("display","none");
    			}
    		});

    		$("#mail1, #mail2, #mail3").bind('change keyup',function(e){
    			isCheckMail = false;
    			$("#fnIsCheckMail").text('중복체크');
    		});
        //}


		//쿠폰정보 tab
		fnKendoDropDownList({
			id    : 'couponCondiType',
			data  : [
				{"CODE":"COUPON_CODE"	,"NAME":"쿠폰코드"},
				{"CODE":"COUPON_NAME"	,"NAME":"쿠폰명"},
			],
			textField :"NAME",
			valueField : "CODE",
			blank : "선택"
		});
		fnKendoDropDownList({
			id    : 'couponUseYn',
			data  : [
				{"CODE":"Y"	,"NAME":"사용"},
				{"CODE":"N"	,"NAME":"미사용"},
			],
			textField :"NAME",
			valueField : "CODE",
			blank : "전체"
		});
		fnKendoDatePicker({
            id: 'useFromDate',
            format: 'yyyy-MM-dd',
            defVal : fnGetDayMinus(fnGetToday(),6)
            ,defType : 'oneWeek'
        });
        fnKendoDatePicker({
            id: 'useToDate',
            format: 'yyyy-MM-dd',
            btnStyle: true,
            btnStartId: 'useFromDate',
            btnEndId: 'useToDate',
            defVal : fnGetToday()
            ,defType : 'oneWeek'
            ,change: function(e){
        	   if ($('#useFromDate').val() == "" ) {
                   return valueCheck("", "사용기간 시작일을 선택해주세요.", 'useToDate');
               }
			}
        });
        fnKendoDatePicker({
            id: 'issueFromDate',
            format: 'yyyy-MM-dd'
            //,defVal : fnGetDayMinus(fnGetToday(),7)
        });
        fnKendoDatePicker({
            id: 'issueToDate',
            format: 'yyyy-MM-dd',
            btnStyle: true,
            btnStartId: 'issueFromDate',
            btnEndId: 'issueToDate'
            //,defVal : fnGetToday()
            ,change: function(e){
        	   if ($('#issueFromDate').val() == "" ) {
                   return valueCheck("", "발행일 시작일을 선택해주세요.", 'issueToDate');
               }
			}
        });


        //적립금정보 tab
        fnKendoDropDownList({
			id    : 'paymentType',
			data  : [
				{"CODE":"1"	,"NAME":"사용"},
				{"CODE":"2"	,"NAME":"적립"},
				{"CODE":"3"	,"NAME":"소멸"}
			],
			textField :"NAME",
			valueField : "CODE",
			blank : "전체"
		});
        fnKendoDatePicker({
            id: 'pointStartCreateDate',
            format: 'yyyy-MM-dd',
            defVal : fnGetDayMinus(fnGetToday(),6)
            ,defType : 'oneWeek'
        });
        fnKendoDatePicker({
            id: 'pointEndCreateDate',
            format: 'yyyy-MM-dd',
            btnStyle: true,
            btnStartId: 'pointStartCreateDate',
            btnEndId: 'pointEndCreateDate',
            defVal : fnGetToday()
            ,defType : 'oneWeek'
            ,change: function(e){
        	   if ($('#pointStartCreateDate').val() == "" ) {
                   return valueCheck("", "발생일 시작일을 선택해주세요.", 'pointEndCreateDate');
               }
			}
        });

        //임직원 할인 한도액 TAB - POPUP 과거 내역조회
        let searchYearData = [];
        let month = new String(new Date().getMonth());
        let searchYearCode;
        if(month == '0'){
            searchYearCode = new Date().getFullYear() - 1;
            month = 11;
        }else{
            searchYearCode = new Date().getFullYear();
        }
        searchYearData.push({"CODE":searchYearCode, "NAME":searchYearCode+"년"});

        let searchMonthData = [];
        for (let i = 0; i <= month; i++) {
            let monthCode = new String(i + 1);
            if(monthCode.length === 1){
                monthCode = '0' + monthCode;
            }
            searchMonthData.push({"CODE":monthCode, "NAME":(i+1) + "월"});
        }

        fnKendoDropDownList({
            id    : 'searchYear',
            data  : searchYearData,
            textField :"NAME",
            valueField : "CODE",
            blank : "선택"
        });
        fnKendoDropDownList({
            id    : 'searchMonth',
            data  : searchMonthData,
            textField :"NAME",
            valueField : "CODE",
            blank : "선택"
        });

        $("#searchMonth").off().on('change', function() {
            fnKendoPopupGetEmployeeDiscountPast();
        });

    	//================ 상세구분 ===============
//    	fnKendoDropDownList({
//    			id  : 'searchPointDetailType',
//    	        url : "/admin/comn/getCodeList",
//    	        params : {"stCommonCodeMasterCode" : "POINT_PROCESS_TP", "useYn" :"Y"},
//    	        textField :"NAME",
//    			valueField : "CODE",
//    			blank : "전체"
//    	});

	}
	//---------------Initialize Option Box End ------------------------------------------------
	//-------------------------------  Common Function start -------------------------------

	function inputFocus(){
		$('#GRP_NAME').focus();
	};
		function condiFocus(){
		$('#condition1').focus();
	};
	function fnBuyerSearch(paramData){

		fnAjax({
				url     : '/admin/ur/buyer/getBuyer',
				params  : {urUserId : paramData.urUserId},
				success :
					function( data ){

						data.rows.foreignerYn = (data.rows.foreignerYn == 'N' ? '내국인':'외국인');

						var mobileHyphen = fnPhoneNumberHyphen(data.rows.mobile);


						personalInformationAccessYn = data.rows.personalInformationAccessYn;

						if ( personalInformationAccessYn =='Y')
					    {
							//환불계좌정보, 배송지관리 탭 제공
							$("#addTab2").css("display","");
							$("#addTab3").css("display","");

                           var email = data.rows.mail.split('@');
                            data.rows.mail1 = email[0];
                            data.rows.mail2 = email[1];
                            data.rows.mail3 = email[1];

                            var mobile = mobileHyphen.split('-');
                            data.rows.mobile1 = mobile[0];
                            data.rows.mobile2 = mobile[1];
                            data.rows.mobile3 = mobile[2];
                            $("#bdayMasking").hide();
                            $("#bday").show();
                            $("#mobileMaskig").hide();
                            $("#mobile").show();
                            $("#emailMasking").hide();
                            $("#mail1").show();
                            $("#mail2").show();
                            $("#mail3").show();
                            $("#golvange").show();
                            $("#dash1").show();
                            $("#dash2").show();
                            $("#gender").show();
                            $("#genderMasking").hide();
//                            $('#inputForm').find("[name='masking']").hide();
//                            $('#inputForm').find("[name='masking']").val("")
//                            $('#inputForm').find("[name='unMasking']").show();

                    		fnKendoDatePicker({
                    			id    : 'bday',
                    			format: 'yyyyMMdd',
                    			defVal: '1980-01-01'
                    		});
					    }else
					    {

                            //환불계좌정보, 배송지관리 탭 제공X
                            $("#addTab2").css("display","none");
                            $("#addTab3").css("display","none");

					        var emailMasking =  data.rows.mail;
					        var mobileMaskig =  data.rows.mobile;
					        var bdayMasking  =  data.rows.bday
					        $("#emailMasking").val(emailMasking);
					        $("#bdayMasking").val(bdayMasking);
					        $("#mobileMaskig").val(mobileMaskig);
					        $("#bdayMasking").show();
					        $("#bday").hide();
					        $("#bday").val("");
                            $("#mobileMaskig").show();
                            $("#mobile").hide();
                            $("#mobile1").hide();
                            $("#mobile2").hide();
                            $("#mobile3").hide();
                            $("#golvange").hide();
                            $("#dash1").hide();
                            $("#dash2").hide();
                            $("#mobile").val("");
                            $("#emailMasking").show();
                            $("#fnIsCheckMail").hide();
                            $("#mail").hide();
                            $("#mail").val("");
                            $("#mail1").hide();
                            $("#mail1").val("");
                            $("#mail2").hide();
                            $("#mail2").val("");
                            $("#mail3").hide();
                            $("#mail3").val("");
                            $("#gender").hide();
                            $("#genderMasking").show();
                            $("#genderMasking").val('해당없음');
//                            $('#inputForm').find("[name='masking']").show ();
//                            $('#inputForm').find("[name='unMasking']").hide();
//                            $('#inputForm').find("[name='unMasking']").val("");
					    }



						if(data.rows.employeeYn == 'Y'){
							$("#employeeType").val('임직원');
							$("#employeeTr").css("display","");
							$("#addTab6").css("display","");
						}else{
							$("#employeeType").val('일반');
							$("#employeeTr").css("display","none");
							$("#addTab6").css("display","none");
						}

						if(data.rows.status == '정지'){
							$("#status").css('color','red');
							$("#fnKendoPopupPutBuyerStop").css('display','none');
							$("#fnKendoPopupPutBuyerNormal").css('display','');
							$("#fnKendoPopupAddUserBlack").attr("disabled",true);
						}else{
							$("#status").css('color','');
							$("#fnKendoPopupPutBuyerStop").css('display','');
							$("#fnKendoPopupPutBuyerNormal").css('display','none');
							$("#fnKendoPopupAddUserBlack").attr("disabled",false);
						}
						data.rows.status = data.rows.status + '회원';

						var marketingYnDate = data.rows.marketingYnDate == null ? '' : '(동의일 : '+data.rows.marketingYnDate+')';
						$("#marketingYnDate").text(marketingYnDate);

						if(data.rows.smsYn == 'Y'){
							var smsYnDate = data.rows.smsYnDate == null ? '' : '(SMS : '+data.rows.smsYnDate+') ';
						}else{
							var smsYnDate = '';
						}
						$("#smsYnDate").text(smsYnDate);

						if(data.rows.mailYn == 'Y'){
							var mailYnDate = data.rows.mailYnDate == null ? '' : '(EMAIL : '+data.rows.mailYnDate+')';
						}else{
							var mailYnDate = '';
						}
						$("#mailYnDate").text(mailYnDate);

						var pushYnDate = data.rows.pushYnDate == null ? '' : '(수신일: '+data.rows.pushYnDate+')';
						$("#pushYnDate").text(pushYnDate);
						if(data.rows.deviceCount == '0'){
							$("input:radio[name='pushYn']").attr('disabled',true);
						}


						var aclauseVersion = data.rows.aexecuteDate == null? '' : data.rows.aexecuteDate+' / '+data.rows.aclauseCreateDate+' 동의';
						$("#aclauseVersion").val(aclauseVersion);
						var bclauseVersion = data.rows.bexecuteDate == null? '' : +data.rows.bexecuteDate+' / '+data.rows.bclauseCreateDate+' 동의';
						$("#bclauseVersion").val(bclauseVersion);

						if(data.rows.recommendUserId != null ) $("#recommendUserId").text(data.rows.recommendUserId);
						$("#recommendCount").text(data.rows.recommendCount+'명');

						var blackList;
						if(data.rows.blackList == 'Y'){
							blackList = '예';
							$("#fnKendoPopupUserBlackHist").css("display","");
							$("input:radio[name='eventJoinYn']").attr('disabled',false);
							if(data.rows.eventJoinYn == "" || data.rows.eventJoinYn == null){
								$("input:radio[name='eventJoinYn']:input[value='Y']").attr('checked',true);
							}
						}else{
							blackList = '아니오';
							$("#fnKendoPopupUserBlackHist").css("display","non");
							$("input:radio[name='eventJoinYn']").attr('disabled',true);
						}
						$("#blackList").text(blackList);

						var maliciousClaim;
						if(data.rows.maliciousClaimCount == null){
							maliciousClaim = '0건';
							$("#fnKendoPopupMaliciousClaimHist").css("display","none");
						}else{
							maliciousClaim = data.rows.maliciousClaimCount+'건 (최근 클레임 이력 : '+data.rows.maliciousClaimDate+')' ;
							$("#fnKendoPopupMaliciousClaimHist").css("display","");
						}
						$("#maliciousClaim").text(maliciousClaim);

						//정지회원 수정버튼 비활성화
						if(paramData.type == 'userStop' || data.rows.status == '정지회원'){
							//기본정보 영역
							$("#fnSavePutBuyer").css("display","none");
							$("#fnUserDrop").css("display","none");
							$("#fnIsCheckMail").attr("disabled",true);
							$("#pwdClearBtn").css("display","none");
							$("#fnKendoPopupAddUserBlack").attr("disabled",true);
							//환불계좌정보 영역
							$("#fnSaveRefundBank").attr("disabled",true);
							$("#fnValidBankAccount").attr("disabled",true);
							//배송지관리 영역
							$("#fnNewShippingAddress").attr("disabled",true);
							$("#fnSaveShippingAddress").attr("disabled",true);
						}

						//마스킹 정책 레벨2 관리자 수정 권한 없음
						if( personalInformationAccessYn !='Y'){
							$("#fnSavePutBuyer").css("display","none");
							$("#fnUserDrop").css("display","none");
							$("#fnIsCheckMail").attr("disabled",true);
							$("#pwdClearBtn").css("display","none");
							$("#fnKendoPopupPutBuyerStop").attr("disabled",true);
							$("#fnKendoPopupPutBuyerNormal").attr("disabled",true);
							$("#fnKendoPopupAddUserBlack").attr("disabled",true);
						}

						fnBizCallback("select",data);

						if(defaultActivateTab == 'CS_BUYER' ||
								defaultActivateTab == 'CS_REFUND' ||
								defaultActivateTab == 'CS_SHIPPING_ADDRESS' ||
								defaultActivateTab == 'CS_COUPON' ||
								defaultActivateTab == 'CS_POINT' ){
//							var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
//							tabStrip.disable(tabStrip.tabGroup.children().eq(1));
//							tabStrip.disable(tabStrip.tabGroup.children().eq(2));
//							tabStrip.disable(tabStrip.tabGroup.children().eq(3));
//							tabStrip.disable(tabStrip.tabGroup.children().eq(4));

//							$("#addTab6").css("display","none");
						}
					},
				isAction : 'select'
		});
	}

	function fnRefundBank(paramData){
		fnAjax({
			url     : '/admin/ur/userRefund/getRefundBank',
			params  : {urUserId : paramData.urUserId},
			success :
				function( data ){
					BEFORE_OBJECT = new Object();
					BEFORE_OBJECT = data.rows;
					$('#inputFormRefundBank').bindingForm(data, 'rows', true);

				},
			isAction : 'select'
		});

		if(defaultActivateTab == 'CS_REFUND'){
//			var tabStrip = $("#tabstrip").kendoTabStrip().data("kendoTabStrip");
//			tabStrip.disable(tabStrip.tabGroup.children().eq(0));
//			tabStrip.disable(tabStrip.tabGroup.children().eq(2));
//			tabStrip.disable(tabStrip.tabGroup.children().eq(3));
//			tabStrip.disable(tabStrip.tabGroup.children().eq(4));

			$("#addTab6").css("display","none");
		}
	}

	function fnBuyerMoveSearch(paramData){

		fnAjax({
				url     : '/admin/ur/buyer/getBuyerMove',
				params  : {urUserId : paramData.urUserId},
				success :
					function( data ){

						data.rows.foreignerYn = (data.rows.foreignerYn == 'N' ? '내국인':'외국인');
						//$("#foreignerYn").text(data.rows.foreignerYn);

						personalInformationAccessYn = data.rows.personalInformationAccessYn;

						var mobileHyphen = fnPhoneNumberHyphen(data.rows.mobile);

						if ( personalInformationAccessYn =='Y')
					    {
							//환불계좌정보, 배송지관리 탭 제공
							$("#addTab2").css("display","");
							$("#addTab3").css("display","");

                           var email = data.rows.mail.split('@');
                            data.rows.mail1 = email[0];
                            data.rows.mail2 = email[1];
                            data.rows.mail3 = email[1];

                            var mobile = mobileHyphen.split('-');
                            data.rows.mobile1 = mobile[0];
                            data.rows.mobile2 = mobile[1];
                            data.rows.mobile3 = mobile[2];
                            $("#bdayMasking").hide();
                            $("#bday").show();
                            $("#mobileMaskig").hide();
                            $("#mobile").show();
                            $("#emailMasking").hide();
                            $("#mail1").show();
                            $("#mail2").show();
                            $("#mail3").show();
                            $("#golvange").show();
                            $("#dash1").show();
                            $("#dash2").show();
                            $("#gender").show();
                            $("#genderMasking").hide();
//                            $('#inputForm').find("[name='masking']").hide();
//                            $('#inputForm').find("[name='masking']").val("")
//                            $('#inputForm').find("[name='unMasking']").show();

                    		fnKendoDatePicker({
                    			id    : 'bday',
                    			format: 'yyyyMMdd',
                    			defVal: '1980-01-01'
                    		});
					    }else
					    {
                            //환불계좌정보, 배송지관리 탭 제공X
                            $("#addTab2").css("display","none");
                            $("#addTab3").css("display","none");

					        var emailMasking =  data.rows.mail;
					        var mobileMaskig =  data.rows.mobile;
					        var bdayMasking  =  data.rows.bday
					        $("#emailMasking").val(emailMasking);
					        $("#bdayMasking").val(bdayMasking);
					        $("#mobileMaskig").val(mobileMaskig);
					        $("#bdayMasking").show();
					        $("#bday").hide();
					        $("#bday").val("");
                            $("#mobileMaskig").show();
                            $("#mobile").hide();
                            $("#mobile1").hide();
                            $("#mobile2").hide();
                            $("#mobile3").hide();
                            $("#golvange").hide();
                            $("#dash1").hide();
                            $("#dash2").hide();
                            $("#mobile").val("");
                            $("#emailMasking").show();
                            $("#fnIsCheckMail").hide();
                            $("#mail").hide();
                            $("#mail").val("");
                            $("#mail1").hide();
                            $("#mail1").val("");
                            $("#mail2").hide();
                            $("#mail2").val("");
                            $("#mail3").hide();
                            $("#mail3").val("");
                            $("#gender").hide();
                            $("#genderMasking").show();
                            $("#genderMasking").val('해당없음');
                            $('#inputForm').find("[name='masking']").show ();
                            $('#inputForm').find("[name='unMasking']").hide();
                            $('#inputForm').find("[name='unMasking']").val("");
					    }

						if(data.rows.employeeYn == 'Y'){
							$("#employeeType").val('임직원');
							$("#employeeTr").css("display","");
							$("#addTab6").css("display","");
						}else{
							$("#employeeType").val('일반회원');
							$("#employeeTr").css("display","none");
							$("#addTab6").css("display","none");
						}

						$("#buyerMoveStatus").css('display','');
						$("#buyerMoveStatus").val('휴면 /');

						if(data.rows.status == '정지'){
							$("#status").css('color','red');
							$("#fnKendoPopupPutBuyerStop").css('display','none');
							$("#fnKendoPopupPutBuyerNormal").css('display','');
						}else{
							$("#status").css('color','');
							$("#fnKendoPopupPutBuyerStop").css('display','');
							$("#fnKendoPopupPutBuyerNormal").css('display','none');
						}
						data.rows.status = data.rows.status + '회원';

						var marketingYnDate = data.rows.marketingYnDate == null ? '' : '(동의일 : '+data.rows.marketingYnDate+')';
						$("#marketingYnDate").text(marketingYnDate);

						if(data.rows.smsYn == 'Y'){
							var smsYnDate = data.rows.smsYnDate == null ? '' : '(SMS : '+data.rows.smsYnDate+') ';
						}else{
							var smsYnDate = '';
						}
						$("#smsYnDate").text(smsYnDate);

						if(data.rows.mailYn == 'Y'){
							var mailYnDate = data.rows.mailYnDate == null ? '' : '(EMAIL : '+data.rows.mailYnDate+')';
						}else{
							var mailYnDate = '';
						}
						$("#mailYnDate").text(mailYnDate);

						var pushYnDate = data.rows.pushYnDate == null ? '' : '(수신일: '+data.rows.pushYnDate+')';
						$("#pushYnDate").text(pushYnDate);
						if(data.rows.deviceCount == '0'){
							$("input:radio[name='pushYn']").attr('disabled',true);
						}

						var aclauseVersion = data.rows.aexecuteDate == null? '' : data.rows.aexecuteDate+' / '+data.rows.aclauseCreateDate+' 동의';
						$("#aclauseVersion").val(aclauseVersion);
						var bclauseVersion = data.rows.bexecuteDate == null? '' : +data.rows.bexecuteDate+' / '+data.rows.bclauseCreateDate+' 동의';
						$("#bclauseVersion").val(bclauseVersion);

						if(data.rows.recommendUserId != null ) $("#recommendUserId").text(data.rows.recommendUserId);
						$("#recommendCount").text(data.rows.recommendCount+'명');

						var blackList;
						if(data.rows.blackList == 'Y'){
							blackList = '예';
							$("#fnKendoPopupUserBlackHist").css("display","");
							$("input:radio[name='eventJoinYn']").attr('disabled',false);
							if(data.rows.eventJoinYn == "" || data.rows.eventJoinYn == null){
								$("input:radio[name='eventJoinYn']:input[value='Y']").attr('checked',true);
							}
						}else{
							blackList = '아니오';
							$("#fnKendoPopupUserBlackHist").css("display","non");
							$("input:radio[name='eventJoinYn']").attr('disabled',true);
						}
						$("#blackList").text(blackList);

						var maliciousClaim;
						if(data.rows.maliciousClaimCount == null){
							maliciousClaim = '0건';
							$("#fnKendoPopupMaliciousClaimHist").css("display","none");
						}else{
							maliciousClaim = data.rows.maliciousClaimCount+'건 (최근 클레임 이력 : '+data.rows.maliciousClaimDate+')' ;
							$("#fnKendoPopupMaliciousClaimHist").css("display","");
						}
						$("#maliciousClaim").text(maliciousClaim);

						//휴면회원 수정버튼 비활성화
						//기본정보 영역
						$("#fnSavePutBuyer").css("display","none");
						$("#fnUserDrop").css("display","none");
						$("#fnIsCheckMail").attr("disabled",true);
						$("#pwdClearBtn").css("display","none");
						$("#fnKendoPopupPutBuyerStop").attr("disabled",true);
						$("#fnKendoPopupPutBuyerNormal").attr("disabled",true);
						$("#fnKendoPopupAddUserBlack").attr("disabled",true);
						//환불계좌정보 영역
						$("#fnSaveRefundBank").attr("disabled",true);
						$("#fnValidBankAccount").attr("disabled",true);
						//배송지관리 영역
						$("#fnNewShippingAddress").attr("disabled",true);
						$("#fnSaveShippingAddress").attr("disabled",true);

						fnBizCallback("select",data);
					},
				isAction : 'select'
		});
	}
	function fnGroupDcChange(){
		var thisVal = $('#GROUP_DC_TYPE').getRadioVal();
		if(thisVal =="1"){
			$('#noSet').css("display" ,"");
			$('#categorySet').css("display" ,"none");
			$('#wmsSet').css("display" ,"none");
		}else if(thisVal =="2"){
			$('#noSet').css("display" ,"none");
			$('#categorySet').css("display" ,"");
			$('#wmsSet').css("display" ,"none");
		}else if(thisVal =="3"){
			$('#noSet').css("display" ,"none");
			$('#categorySet').css("display" ,"none");
			$('#wmsSet').css("display" ,"");
		}
	}
	function fnPopupButton(param){
		switch(param){
			case "ZIP" :
				fnKendoPopup({
					id     : 'mailInfo',
					title  : fnGetLangData({key :"4890",nullMsg :'우편번호 팝업' }),
					src    : '#/mailInfo',
					width  : '660px',
					height : '470px',
					success: function( id, data ){
						if(data.zipNo){
							$('#ZIP').val(data.zipNo);
							$('#ADDR1').val(data.roadAddr);

						}
					}
				});
				break;
			case "ZIPc" :
				fnKendoPopup({
					id     : 'mailInfo',
					title  : fnGetLangData({key :"4890",nullMsg :'우편번호 팝업' }),
					src    : '#/mailInfo',
					width  : '660px',
					height : '470px',
					success: function( id, data ){
						if(data.zipNo){
							$('#RECIP_ZIP').val(data.zipNo);
							$('#RECIP_ADDR1').val(data.roadAddr);
						}
					}
				});
				break;
			case "UR_GROUP" :
				fnKendoPopup({
					id     : 'userGroup002',
					title  : fnGetLangData({key :"5221",nullMsg :'회원등급 조회팝업' }),
					src    : '#/userGroup002',
					width  : '700px',
					height : '470px',
					success: function( id, data ){
						if(data.UR_GROUP_ID){
							$('#UR_GROUP_ID').val(data.UR_GROUP_ID);
							$('#GRP_NAME').val(data.GRP_NAME);
						}
					}
				});
				break;
			case "pwClear" :
				var data = $('#inputForm').formSerialize(true);
				data.userType = 'BUYER';

				fnKendoPopup({
		            id: 'pwClearPopup',
		            title: fnGetLangData({nullMsg:'PW 재발송'}),
		            param  : { "data" : data},
		            src: '#/pwClearPopup',
		            width: '450px',
		            height: '280px',
		            success: function(id, data) {
		            }
		        });
				break;
		}
	}

	function fnIsCheckMail(){

		var data = $('#inputForm').formSerialize(true);

		if(data.mail1 == '' || data.mail2 == '' || ( data.mail2 == 'write' && data.mail3 == '')) {
			return valueCheck("", "모든 항목을 기입해주세요.", 'mail1');
		}

		data.mail = data.mail1 +'@'+ (data.mail2 == 'write' ?data.mail3:data.mail2);
		var filter = /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
		if (!filter.test(data.mail)) {
			return valueCheck("", "이메일 형식을 확인해주세요.", 'mail1');
		}

		fnAjax({
			url     : '/admin/ur/buyer/checkDuplicateMail',
			params  : data,
			success :
				function( data ){

					if(data.count > 0){
						fnKendoMessage({message : fnGetLangData({nullMsg :'중복된 이메일이 있습니다.' }),ok:function(e){
							isCheckMail = false;
							$("#fnIsCheckMail").text('중복체크');
						}});
					}else{
						fnKendoMessage({message : fnGetLangData({nullMsg :'사용가능한 이메일입니다.' }),ok:function(e){
							isCheckMail = true;
							$("#fnIsCheckMail").text('중복체크√');
							$("#fnIsCheckMail").css('font-size','11px');
						}});
					}
				},
			isAction : 'batch'
		});

	}

	function fnKendoPopupBuyerHist(){
		var data = $('#inputForm').formSerialize(true);
		cGridDs.read(data);
		fnKendoInputPoup({id:"kendoPopupBuyerHist",height:"450px" ,width:"700px",title:{nullMsg :'회원상태이력'} });
	}

	function fnKendoPopupPutBuyerStop(){
		$('#inputFormPutBuyerStop').formClear(true);

		var date = new Date();
		var year = date.getFullYear();
	    var month = (1 + date.getMonth());
	    month = month >= 10 ? month : '0' + month;
	    var day = date.getDate();
	    day = day >= 10 ? day : '0' + day;
	    var today = year + '-' + month + '-' + day;

	    $("#stopDate").val(today);
		fnKendoInputPoup({id:"kendoPopupPutBuyerStop",height:"300px" ,width:"450px",title:{nullMsg :'정지설정'} });
	}

	function fnKendoPopupPutBuyerNormal(){

		var urBuyerStatusLogId = $("#urBuyerStatusLogId").val();

		fnAjax({
			url     : '/admin/ur/buyerStatus/getBuyerStopLog',
			params  : {urBuyerStatusLogId : urBuyerStatusLogId},
			success :
				function( data ){
					$('#inputFormPutBuyerNormal').formClear(true);
					$('#inputFormPutBuyerNormal').bindingForm( {'rows':data.rows},'rows', true);
					fnKendoInputPoup({id:"kendoPopupPutBuyerNormal",height:"450px" ,width:"450px",title:{nullMsg :'정상설정'} });
				},
			isAction : 'select'
		});

	}

	function fnKendoPopupGetBuyerGroupHist(){
		var data = $('#inputForm').formSerialize(true);
		dGridDs.read(data);
		fnKendoInputPoup({id:"kendoPopupGetBuyerGroupHist",height:"410px" ,width:"500px",title:{nullMsg :'회원등급이력'} });
	}

	function fnRecommendCount(){

		if($("#recommendCount").text() != '0명'){
			var data = $('#inputForm').formSerialize(true);
			eGridDs.read(data);
			fnKendoInputPoup({id:"kendoPopupGetBuyerRecommList",height:"450px" ,width:"650px",title:{nullMsg :'나를 추천한 추천인'} });
		}
	}

	function fnKendoPopupMaliciousClaimHist(){
		var data = $('#inputForm').formSerialize(true);
		$('#inputFormMaliciousClaimHist').bindingForm( {'rows':data},'rows', true);

		fGridDs.read(data);
		fnKendoInputPoup({id:"kendoPopupMaliciousClaimHist",height:"500px" ,width:"800px",title:{nullMsg :'악성 클레임 이력'} });
	}

	function fnKendoPopupAddUserBlack(){
		var data;
		data = $('#inputForm').formSerialize(false);
		$('#inputFormAddUserBlack').formClear(false);
		$('#inputFormAddUserBlack').bindingForm( {'rows':data},'rows', true);

		//gGridDs.read(data);
		fnSearch('addUserBlack');
		fnKendoInputPoup({id:"kendoPopupAddUserBlack",height:"100%" ,width:"800px",title:{nullMsg :'블랙리스트 등록'} });
	}

	function fnKendoPopupUserBlackHist(){
		var data = $('#inputForm').formSerialize(false);

		fnKendoPopup({
            id: 'blackHistPopup',
            title: '블랙리스트 이력',
            param: { "urUserId": data.urUserId },
            src: '#/ursBlackHist',
            width: '800px',
            height: '450px',
            success: function(id, data) {
                //fnSearch();
            }
        });
	}

    function fnKendoPopupGetEmployeeDiscountPast(){
        var userData = $('#inputForm').formSerialize(true);
        if($('#searchYear').val() == ''){
            return;
        }
        if($('#searchMonth').val() == ''){
            return;
        }

        var data = {urErpEmployeeCd :userData.urErpEmployeeCd ,searchDate : $('#searchYear').val() + '-' + $('#searchMonth').val() };
        edpGridDs.read(data);
	}

	function fnValidBankAccount(){
		var data = $('#inputFormRefundBank').formSerialize(false);

		var regExp = /^\d+-?\d+$/;

		if(!regExp.test(data.accountNumber)) {
			return valueCheck("", "계좌번호 형식을 확인해주세요.", '');
		}

		if($("#bankCode").val() == ""){
			return valueCheck("", "은행명을 선택해주세요.", '');
		}

		if($("#holderName").val() ==""){
			return valueCheck("", "예금주를 입력해주세요.", '');
		}

		fnAjax({
			url     : '/admin/ur/userRefund/isValidationBankAccountNumber',
			params  : data,
			success :
				function( data ){
					if(data == true){
						$("#validBankAccountDiv").text("계좌인증 성공");
						isValidBankAccount = true;
					}else{
						$("#validBankAccountDiv").text("계좌인증 실패");
						isValidBankAccount = false;
					}
				},
			isAction : 'select'
		});
	}

	//회원별 적립금 지급/차감 등록
	function fnPointAdmin(){

		var pointAdmin = 'Y';
		var userNameVal;
		var loginIdVal;
		var urUserId;

		if(defaultActivateTab != undefined && csData.userName != null){
			userNameVal = csData.userName;
		}else{
			userNameVal = $('#userName').val();
		}

		if(defaultActivateTab != undefined && csData.loginId != null){
			loginIdVal = csData.loginId;
		}else{
			loginIdVal = $('#loginId').val();
		}

		if(defaultActivateTab != undefined && csData.urUserId != null){
			urUserId = csData.urUserId;
		}else{
			urUserId = $('#urUserId').val();
		}

		fnKendoPopup({
			id     : 'pointMgmAdd',
			title  : '적립금 설정 등록',
			src    : '#/pointMgmAdd',
			param  : {pointAdmin : pointAdmin, loginId : loginIdVal, userName: userNameVal, urUserId : urUserId},
			width  : '2000px',
			height : '1100px',
			success: function( id, data ){
				/*if(data.urSupplierId){
					$('#warehouseId').val(data.urSupplierWarehouseId);
					$('#warehouseName').val(data.warehouseName);
				}*/
				fnSearch();
			}
		});
	}

	function valueCheck(key, nullMsg, ID){
		fnKendoMessage({ message : fnGetLangData({ key : key, nullMsg : nullMsg}), ok : function focusValue(){
			$('#'+ID).focus();
		}});

		return false;
	}

    // 정상설정 첨부파일 버튼 클릭
    function fnFileUpload() {
        $("#uploadFile").trigger("click");
    };

	// 우편번호 팝업
	function fnAddressPopup(){
	    fnDaumPostcode("receiverZipCode", "receiverAddress1", "receiverAddress2","buildingCode");
	};

	/**
	* 콜백합수
	*/
	function fnBizCallback( id, data ){
		switch(id){
			case 'select':
				//form data binding
				BEFORE_OBJECT = new Object();
				BEFORE_OBJECT = data.rows;
				$('#inputForm').bindingForm(data, 'rows', true);
				var tabStrip = $("#tabstrip").data('kendoTabStrip');
				tabStrip.enable(tabStrip.tabGroup.children().eq(1));
				tabStrip.enable(tabStrip.tabGroup.children().eq(2));
				$("span#ATTR_1").html(data.rows.ATTR_1);
				$("span#ATTR_2").html(data.rows.SHOP_NAME + " / " + data.rows.ATTR_2);
				$("span#CUST_NO").html(data.rows.CUST_NO);
		        if (personalInformationAccessYn =="Y")
		        {
    				if($("#mail2").val() == '' || $("#mail2").val() == null){
    					$("#mail3").css("display","");
    					$("#mail2").val('write');
    				}else{
    					$("#mail3").css("display","none");
    				}
		        }

				break;
			case 'insert':
				if(data.rows =="DUP_DATA"){
					fnKendoMessage({message : fnGetLangData({nullMsg :'중복입니다.' })  ,ok :inputFocus});
				}else{
					paramData = data.rows;
					fnKendoMessage({message : fnGetLangData({nullMsg :'입력되었습니다.' }) ,ok :function(e){
						fnBuyerSearch(paramData);
					}});;
				}
				break;
			case 'save':
				fnKendoMessage({message : fnGetLangData({nullMsg :'저장되었습니다.' })});
				break;
			case 'update':
				fnKendoMessage({message : fnGetLangData({nullMsg :'수정되었습니다.' }) });
				break;
			case 'delete':
				aGridDs.remove(data);
				fnNew();
				//aGridDs.total = aGridDs.total-1;
				fnKendoMessage({message : fnGetLangData({nullMsg :'탈퇴 되었습니다.' }) ,ok :fnClose});
				break;

		}
	}

	//-------------------------------  Common Function end -------------------------------


	//------------------------------- Html 버튼 바인딩  Start -------------------------------
	/** Common Search*/
	$scope.fnSearch = function(param) {	fnSearch(param);	};
	/** Common Clear*/
	$scope.fnClear =function(param){ fnClear(param);	};
	/** Common New*/
	$scope.fnNew = function(param){	fnNew(param);};
	/** Common Save*/
	$scope.fnSave = function(param){	 fnSave(param);};
	/** Common Delete*/
	$scope.fnDel = function(){	 fnDel();};
	/** Common Close*/
	$scope.fnClose = function(param ){  fnClose(param);};

	$scope.fnPopupButton = function(data){ fnPopupButton(data); };

	$scope.fnIsCheckMail = function(){ fnIsCheckMail(); };

	$scope.fnKendoPopupBuyerHist = function( ) {	fnKendoPopupBuyerHist();};
	$scope.fnKendoPopupPutBuyerStop = function( ) {	fnKendoPopupPutBuyerStop();};
	$scope.fnKendoPopupPutBuyerNormal = function( ) {	fnKendoPopupPutBuyerNormal();};
	$scope.fnKendoPopupGetBuyerGroupHist = function( ) {	fnKendoPopupGetBuyerGroupHist();};
	$scope.fnRecommendCount = function( ) {	fnRecommendCount();};
	$scope.fnKendoPopupMaliciousClaimHist = function( ) {	fnKendoPopupMaliciousClaimHist();};
	$scope.fnKendoPopupAddUserBlack = function( ) {	fnKendoPopupAddUserBlack();};
	$scope.fnKendoPopupUserBlackHist = function( ) {	fnKendoPopupUserBlackHist();};
	$scope.fnValidBankAccount = function( ) {	fnValidBankAccount();};

	$scope.fnFileUpload = function() { fnFileUpload(); } // 정상설정 파일 업로드
	$scope.fnAddressPopup = function(){ fnAddressPopup(); }; // 주소찾기
	$scope.fnPointAdmin = function( ) {	fnPointAdmin();};	//적립금 지급/차감

	//마스터코드값 입력제한 - 숫자만



	fnInputValidationByRegexp("bday", /[^0-9]/g);
	fnInputValidationByRegexp("receiverMobile2", /[^0-9]/g);
	fnInputValidationByRegexp("receiverMobile3", /[^a-z^A-Z^0-9]/g);
	fnInputValidationForAlphabetNumberSpecialCharacters("mail1");
	fnInputValidationByRegexp("mail3",/[^a-z^A-Z^0-9^.]/g);
	//------------------------------- Html 버튼 바인딩  End -------------------------------

	// [고객문의관리 이동] 버튼 클릭시
	$("#fnCustomerQnaSearch").on("click", function(e){
		e.preventDefault();

		fnGetBuyer();
		window.open("layout.html#/csCustomerQna","_blank","width=1800, height=1000, top=10, resizable=yes, fullscreen=no");
	});

	function fnGetBuyer() {
		fnAjax({
			url		: '/admin/ur/buyer/getBuyer',
			params	: {'urUserId' : $('#urUserId').val()},
			isAction : 'select',
			success	:
				function( data ){

					let paramStr = new Object();
					paramStr.urUserId = data.rows.urUserId;
					paramStr.employeeYn = data.rows.employeeYn;
					paramStr.userName = data.rows.userName;
					paramStr.loginId = data.rows.loginId;
					paramStr.genderView = data.rows.genderView;
					paramStr.userTypeName = data.rows.userTypeName;
					paramStr.groupName = data.rows.groupName;
					paramStr.mail = data.rows.mail;
					paramStr.mobile = data.rows.mobile;
					paramStr.blackList = data.rows.blackList;

					let uri = JSON.stringify(paramStr);
					setCookie("csUserParamData", uri)
				}
		});
	}

	//쿠키값 Set
	function setCookie(cookieName, value, exdays) {
		var exdate = new Date();
		exdate.setDate(exdate.getDate() + exdays);
		var cookieValue = escape(value) + ((exdays == null) ? "" : "; expires=" + exdate.toGMTString());
		document.cookie = cookieName + "=" + cookieValue;
	}
}); // document ready - END
