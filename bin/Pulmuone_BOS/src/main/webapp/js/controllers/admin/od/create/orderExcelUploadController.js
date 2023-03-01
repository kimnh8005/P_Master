/**-----------------------------------------------------------------------------
 * description 		 : 주문 엑셀업로드
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2020.12.17     강상국			퍼블수정 및 기능개발
 * @
 * **/

'use strict';
var pageParam = fnGetPageParam(); // GET 방식으로 전달된 parameter 객체
var aGridOpt, aGrid;
var gFileTagId;
var gFile;

$(document).ready(function() {

    // Initialize Page Call
	fnInitialize();

    // sheetJs 스크립트 추가
    let myScript = document.createElement("script");
    myScript.setAttribute("src", "js/lib/sheetJs/xlsx.full.min.js");
    document.body.appendChild(myScript);

	// Initialize PageR
	function fnInitialize() {
		$scope.$emit('fnIsMenu', {
			flag : 'true'
		});

		fnPageInfo({
			PG_ID : 'orderExcelUploadC',
			callback : fnUI
		});
	};

	function fnUI() {
		fnTranslate();		// 다국어 변환 --------------------------------------------
		fnInitButton();		// Initialize Button  ---------------------------------
		fnInitGrid(); 		// Initialize Grid ------------------------------------
		fnInitOptionBox(); 	// Initialize Option Box ------------------------------------
		fnInitKendoUpload();
	}

	// 버튼 초기화
	function fnInitButton(){
		$("#fnClear, #fnOrderExcelUpload, #fnSamepleFormDownload").kendoButton();
	};

	// 그리드 초기화
	function fnInitGrid(){
        aGridOpt = {
        	navigatable : true
        	, editable: true
        	, columns : [
        		{field : 'rnum',			title : 'No.', 		width : '80px',		attributes : {style : 'text-align:center'}, format: "{0:n0}"}
        		, {field : 'recvNm', 		title : '수취인명',	width : '100px', 	attributes : {style : 'text-align:center'}, rowspan:true
        			, template :
        				function(data){
	                		let resultBuyerNm = data.recvNm;
	                		if ($.trim(data.errRecvNm) != "") {
	                				data.recvNm = "";
	                				resultBuyerNm = "<span style='color:\#FF0000;'>" + data.errRecvNm + "</span>";
	                		}
	                		resultBuyerNm += "<div style='display:none'>"+data.recvNm+data.buyerHp+data.recvZipCd+data.recvAddr1+data.recvAddr2+"</div>";
	                		return resultBuyerNm;
	                	}
	                , editable :
	                	function(data) {
	                		data.errRecvNm = null;
	                		return data.errRecvNm == null;
	                	}
        		}
        		, {field : 'recvHp', 		title : '수취인 연락처'	, width : '100px', 	attributes : {style : 'text-align:center'}, rowspan:true
        			, template :
        				function(data){
	                		let resultBuyerHp = data.recvHp;
	                		if ($.trim(data.errRecvHp) != "") {
	                			data.recvHp = "";
	                			resultBuyerHp = "<span style='color:#FF0000;'>" + data.errRecvHp  + "</span>";
	                		}
	                		resultBuyerHp += "<div style='display:none'>"+data.recvNm+data.recvHp+data.recvZipCd+data.recvAddr1+data.recvAddr2+"</div>";
	                		return resultBuyerHp;
        				}
        			, editable :
        				function(data) {
	                		data.errRecvHp = null;
	                		return data.errRecvHp == null;
	                	}
        		}
        		, {field : 'recvZipCd', 		title : '우편번호'	    , width : '80px', 	attributes : {style : 'text-align:center'}, rowspan:true
        			, template :
        				function(data){
        					let resultBuyerZip = data.recvZipCd;
	                		if ($.trim(data.errRecvZipCd) != "") {
	                			data.recvZipCd = "";
	                			resultBuyerZip = "<span style='color:#FF0000;'>" + data.errRecvZipCd + "</span>";
	                		}
	                		resultBuyerZip += "<div style='display:none'>"+data.recvNm+data.recvHp+data.recvZipCd+data.recvAddr1+data.recvAddr2+"</div>";
	                		return resultBuyerZip;
	                	}
        			, editable :
        				function(data) {
	                		return false;
	                	}
        		}
        		, {field : 'recvAddr1',	title : '주소1'	    	, width : '200px', 	attributes : {style : 'text-align:left'}, rowspan:true
        			, template :
        				function(data){
	                		let resultBuyerAddr1 = data.recvAddr1;
	                		if ($.trim(data.errRecvAddr1) != "") {
	                			data.recvAddr1 = "";
	                			resultBuyerAddr1 = "<span style='color:#FF0000;'>" + data.errRecvAddr1 + "</span>";
	                		}
	                		resultBuyerAddr1 += "<div style='display:none'>"+data.recvNm+data.recvHp+data.recvZipCd+data.recvAddr1+data.recvAddr2+"</div>";
	                		return resultBuyerAddr1;
	                	}
        			, editable :
        				function(data) {
	                		return false;
	                	}
        		}
        		, {field : 'recvAddr2', 	title : '주소2'	    	, width : '200px', 	attributes : {style : 'text-align:left'}, rowspan:true
        			, template :
        				function(data){
	                		let resultBuyerAddr2 = data.recvAddr2;
	                		if ($.trim(data.errRecvAddr2) != "") {
	                			data.recvAddr2 = "";
	                			resultBuyerAddr2 = "<span style='color:#FF0000;'>" + data.errRecvAddr2 + "</span>";
	                		}
	                		resultBuyerAddr2 += "<div style='display:none'>"+data.recvNm+data.recvHp+data.recvZipCd+data.recvAddr1+data.recvAddr2+"</div>";
	                		return resultBuyerAddr2;
	                	}
        			, editable :
        				function(data) {
	                		return data.errRecvAddr2 == null;
	                	}
        		}
        		, {							title : '주소변경'	, width:'100px',	attributes:{style:'text-align:center'}
        			, command : [
    					{	text: '주소검색' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
							, click :
								function(e) {
									e.preventDefault();
									var grid = $("#aGrid").data("kendoGrid");
									var tr = $(e.target).closest("tr");
									//var data = this.dataItem(tr);
									var rowIdx = $("tr", grid.tbody).index(tr);
									var data = $("#aGrid").data("kendoGrid").dataSource.data()[rowIdx];

									data.set("errRecvZipCd", null);
									data.set("errRecvAddr1", null);
									data.set("errRecvAddr2", null);

									$("#clickIdx").val(rowIdx);

									fnOrderAddr(rowIdx);
								}
							, visible :
								function(data) {
									return data.zipSrchYn == 'Y';
								}
    					}
        			]
        		}
        		, {field : "ilItemCd"				, title : "품목코드/<BR>품목바코드", width : "140px", attributes : {style : "text-align:center;"}
        			, template :
        				function(data){
        					let itemCodeView = data.ilItemCd;
                            if ($.trim(data.itemBarcode) == "" ){
                            	itemCodeView = "/";
                            } else if (data.itemBarcode != "" ){
                            	itemCodeView += "/<br>" + data.itemBarcode;
                            } else {
                            	itemCodeView += "/ - ";
                            }
                            return itemCodeView;
        				}
        			, editable :
        				function(data) {
	                		return false;
	                	}
        		}
        		, {field : 'goodsId'	    		, title : '상품코드'		, width : '100px', 	attributes : {style : 'text-align:center'}
                	, template : function(data){
                		let resultGoodsId = data.goodsId;
                		if ($.trim(data.errIlGoodsId) != "") {
                			data.goodsId = "";
                			resultGoodsId = "<span id='spanGoodsId"+data.rnum+"' style='color:\#FF0000;'>" + data.errIlGoodsId + "</span>";
                		}
                		return resultGoodsId;
                	}
                	, editable : function(data) {
                		return false;
                	}
        		}
        		, {field : 'goodsName'	    		, title : '상품명'		, width : '200px', 	attributes : {style : 'text-align:left'}
        			, editable :
        				function(data) {
        					return false;
        				}
        		}
        		, {field : 'storageMethodTypeName'	, title : '보관방법'		, width : '80px', 	attributes : {style : 'text-align:center'}
	    			, editable :
	    				function(data) {
	    					return false;
	    				}
        		}
        		, {				title : '상품변경'	, width:'100px',attributes:{ style:'text-align:center' }
        			, command: [
                		{	text: '상품검색' , imageClass: "k-i-search", className: "f-grid-search k-margin5", iconClass: "k-icon"
							, click :
								function(e) {
									e.preventDefault();
									var grid = $("#aGrid").data("kendoGrid");
									var tr = $(e.target).closest("tr");
									var data = this.dataItem(tr);
									var rowIdx = $("tr", grid.tbody).index(tr);

									var spanGoodsId = "spanGoodsId" + data.rnum;
									data.set("errIlGoodsId", null);
									data.set("salePriceRemark", null);
									$("#clickIdx").val(rowIdx);

									fnOrderGoods(rowIdx);
								}
							, visible :
								function(data) {
									return data.goodsSrchYn == 'Y';
								}
                		}
	                ]
        		}
        		, {field : 'orderCnt'	    		, title : '수량'			, width : '80px', 	attributes : {style : 'text-align:right'}, format: "{0:n0}"
        			, template :
        				function(data){
	                		let resultOrderCnt = data.orderCnt;
	                		if ($.trim(data.errOrderCnt) != "") {
	                			data.orderCnt = "0";
	                			resultOrderCnt = "<span style='color:#FF0000;'>" + data.errOrderCnt + "</span>";
	                		}
	                		return resultOrderCnt;
	                	}
	    			, editable :
	    				function(data) {
	                		data.errOrderCnt = null;
	                		return data.errOrderCnt == null;
	                	}
        		}
        		, {field : 'recommendedPrice'		, title : '정상가'		, width : '80px', 	attributes : {style : 'text-align:right'}, format: "{0:n0}"}
        		, {field : 'salePrice'	    		, title : '판매가'		, width : '80px', 	attributes : {style : 'text-align:right'}, format: "{0:n0}"}
        		, {	title : '판매가확인'		, width : '80px', 	attributes : {style : 'text-align:right'}
	                	, template : function(data){
	                		let saleMsg = "";
	                		if ($.trim(data.salePriceRemark) != "") {
	                			saleMsg = "<span style='color:#FF0000;'>" + data.salePriceRemark + "</span>"
	                		}
	                		return saleMsg;
	                	}
        		}
        		, {field : 'orderAmt'  	, title : '주문금액'		, width : '100px', 	attributes : {style : 'text-align:right'}, format: "{0:n0}"}
				, {	field : 'saleStatusNm'  	, title : '판매상태'		, width : '80px', 	attributes : {style : 'text-align:right'}
					, template : function(data){
						let saleStatusMsg = data.saleStatusNm;
						if ($.trim(data.errSaleStatus) != "") {
							saleStatusMsg = "<span style='color:#FF0000;'>" + data.errSaleStatus + "</span>"
						}
						return saleStatusMsg;
					}
				}



            ]
        };

        aGrid = $('#aGrid').initializeKendoGrid( aGridOpt ).cKendoGrid();
    };
	// ##########################################################################
	// # 파일업로드 Start
	// ##########################################################################
	// ==========================================================================
	// # 파일업로드-업로드시 사용할 kendoUpload 컴포넌트 초기화
	// ==========================================================================
	function fnInitKendoUpload() {
		var uploadFileTagIdList = ['excelFile'];

		var selectFunction = function(e) {
			if (e.files && e.files[0]) {
				// 엑셀 파일 선택시
				$("#fileInfoDiv").text(e.files[0].name);
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

	// 업로드 버튼 클릭
    function fnOrderExcelUpload(){
    	fileClear();

        $("#uploadFile").trigger("click");
    };

    function fnInitOptionBox() {
    	// 업로드 팝업
	    $("#uploadPopup").kendoWindow({
            width : 800,
            height : 500,
            title : "상품 할인 일괄 업로드",
            visible : false,
            modal : true
        });


        // 업로드 등록 삭제
        $("#uploadDelete").on("click", function(e){
            e.preventDefault();

            fileClear();

            $("#uploadViewControl").hide();
            $("#uploadLink").text("");
            $("#aGrid").data("kendoGrid").dataSource.data( [] );
        });

	    // 업로드  링크 클릭
        $("#uploadLink").on("click", function(e){
            $("#uploadPopup").data("kendoWindow").center().open();
        });

        // 구분
        fnTagMkRadio({
            id : "orderCreateTypeRadio",
            tagId : "orderCreateTypeRadio",
            data  : [ { "CODE" : "PARTY"  	   , "NAME" : "단일주문생성" },
                      { "CODE" : "INDIVIDUAL"  , "NAME" : "개별주문생성" }
                    ],
            style : {}
        });

        $("input:radio[name='orderCreateTypeRadio']:radio[value='PARTY']").attr("checked", true);
    }

    function fileClear() {
    	var agent = navigator.userAgent.toLowerCase();
        if ( (navigator.appName == 'Netscape' && navigator.userAgent.search('Trident') != -1) || (agent.indexOf("msie") != -1) ){
            // ie 일때 input[type=file] init.
            $("#uploadFile").replaceWith( $("#uploadFile").clone(true) );
        } else {
            //other browser 일때 input[type=file] init.
            $("#uploadFile").val("");
        }
    }

    // 샘플다운로드 버튼 클릭
	function fnSamepleFormDownload(){

		document.location.href = "/contents/excelsample/ordercreate/sampleOrderCreate.xlsx"
//	    var opt = {filePath: "/contents/images/",
//        		physicalFileName: "sampleGoodsPriceDiscount.xlsx",
//        		originalFileName: "sampleGoodsPriceDiscount.xlsx"
//        }
//        fnDownloadPublicFile(opt);
	};
	// ==========================================================================
	// # 파일업로드-처리
	// ==========================================================================
	function fnExcelUploadRun(){
		if(gFile == undefined || gFile == ""){
			fnKendoMessage({
				message : "파일을 선택해주세요.",
				ok : function(e) {
				}
			});
			return;
		}
		fnExcelUpload(gFile, gFileTagId);
	}

	// NOTE 파일 업로드 이벤트
	function fnExcelUpload(file, fileTagId) {

		var formData = new FormData();
		formData.append('bannerImage', file);

		$.ajax({
			url         : '/admin/order/create/addBosCreateExcelUpload'
			, data        : formData
			, type        : 'POST'
			, contentType : false
			, processData : false
			, async       : false
			, beforeSend : function(xhr) {
				xhr.setRequestHeader('authMenuID', CLICKED_MENU_ID);
			}
			, success     : function(data) {
				let localMessage = "";
				if(data.code == 'FILE_ERROR' || data.code == 'EXCEL_TRANSFORM_FAIL' || data.code == 'EXCEL_UPLOAD_NONE'){
					localMessage = data.message;
				}else{
					let obj = data.data;

					console.log(obj)
					let uploadFlag = obj.uploadFlag;
					let uploadFlagTxt = (uploadFlag == true) ? "성공" : "실패";

					localMessage = "[결과] : " + uploadFlagTxt+ "<BR>" +
						" [총 건수] : " + obj.totalCount + "<BR>" +
						" [성공 건수] : " + obj.successCount + "<BR>" +
						" [실패 건수] : " + obj.failCount; // + "<BR>" +
						//" [실패 메세지] : " + obj.failMessage;

					let viewList = obj.successList;
					if (uploadFlag != true) {
						viewList = obj.failList;
					}
					$("#aGrid").data("kendoGrid").dataSource.data(obj.successList);
				}

				fnKendoMessage({
					message : localMessage,
					ok : function(e) {}
				});
			}
		});
	}

	// 엑셀 업로드 (SheetJs)
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

                excelData.splice(0,1);

                let DataArray = [];

                for(let i = 0; i < excelData.length; i++){
                    let orgData = {};

                    orgData.recvNm = excelData[i].RECV_NM;
                    orgData.recvHp = excelData[i].RECV_HP;
                    orgData.recvZipCd = excelData[i].RECV_ZIP_CD;
                    orgData.recvAddr1 = excelData[i].RECV_ADDR1;
                    orgData.recvAddr2 = excelData[i].RECV_ADDR2;
                    orgData.ilGoodsId = excelData[i].IL_GOODS_ID;
                    orgData.orderCnt = excelData[i].ORDER_CNT;
                    orgData.salePrice = excelData[i].SALE_PRICE;

                    DataArray[i] = orgData;
                }

                let inputFormData = $("#excelUploadForm").formSerialize(true);

                inputFormData['upload'] = kendo.stringify( DataArray );

                fnAjax({
                    url     : "/admin/order/create/getExcelGoodsList",
                    params  : inputFormData,
    				success : function( data ){
                    	$("#aGrid").data("kendoGrid").dataSource.data(data);
                    },
                    isAction : "select"
                })
                $("#uploadViewControl").show();
                $("#uploadLink").text(fileName);
            })
        };

        reader.readAsBinaryString(input.files[0]);
    };

    // 우편번호 (주소) 찾기
	function fnOrderAddr(idx){
	    new daum.Postcode({
	        oncomplete: function(data) {
	            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

	            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
	            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
	            var addr = ''; // 주소 변수
	            var extraAddr = ''; // 참고항목 변수

	            //도로명 주소가 빈값일 경우 지번주소로 저장
	            if (data.roadAddress !== '') {
	                addr = data.roadAddress;
	            } else {
	                addr = data.jibunAddress;
	            }

	            // 도로명 주소가 빈값이 아닌 경우 참고항목을 조합
	            if(data.roadAddress !== ''){
	                // 법정동명이 있을 경우 추가한다. (법정리는 제외)
	                // 법정동의 경우 마지막 문자가 "동/로/가"로 끝난다.
	                if(data.bname !== '' && /[동|로|가]$/g.test(data.bname)){
	                    extraAddr += data.bname;
	                }
	                // 건물명이 있고, 공동주택일 경우 추가한다.코스트코코리아 양재점
	                if(data.buildingName !== '' && data.apartment === 'Y'){
	                    extraAddr += (extraAddr !== '' ? ', ' + data.buildingName : data.buildingName);
	                }
	                // 표시할 참고항목이 있을 경우, 괄호까지 추가한 최종 문자열을 만든다.
	                if(extraAddr !== ''){
	                    extraAddr = ' (' + extraAddr + ')';
	                }

	                // 주소변수에 참고항목 추가
	                if(extraAddr !== ''){
	                    addr += extraAddr;
	                }
	            }

	    		var gridDataSource = $("#aGrid").data("kendoGrid").dataSource;
	    		var insertRowData = gridDataSource.view()[$("#clickIdx").val()];
	    		insertRowData.set("recvZipCd", 	data.zonecode);
	    		insertRowData.set("recvAddr1", 	addr);
	    		$("#clickIdx").val('');
	            // 커서를 상세주소 필드로 이동한다.
	        }
	    }).open();
	};
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
	// 상품찾기 팝업창
	function fnOrderGoods(idx) {
		let params = {};
		params.goodsType = "GOODS_TYPE.NORMAL";		// 상품유형 ( 일반상품 )
		params.selectType = "single";				// 그리드 체크박스 타입 ( single : 하나만 선택됨, multi : 여러개 선택됨 )
		params.supplierId = "";						// 공급처ID ( 상품유형이 추가 일 경우 필수 )
		params.warehouseId = "";					// 출고처 ID ( 상품유형이 추가 일 경우 필수 )
		params.undeliverableAreaType = "";			// 배송불가지역 ( 상품유형이 추가 일 경우 필수 )

		fnKendoPopup({
			id			: "goodsSearchPopup",
			title		: "상품 검색",  // 해당되는 Title 명 작성
			width		: "1700px",
			height		: "800px",
			scrollable	: "yes",
			src			: "#/goodsSearchPopup",
			param		: params,
			success	: function( id, data ){
				if (data.length == undefined) {
				} else if (data.length > 0 ) {
					var gridDataSource = $("#aGrid").data("kendoGrid").dataSource;
		    		var insertRowData = gridDataSource.view()[$("#clickIdx").val()];
		    		insertRowData.set("ilItemCd", 		data[0].ilItemCd);
		    		insertRowData.set("itemBarcode", 	data[0].itemBarcode);
		    		insertRowData.set("goodsId", 		data[0].goodsId);
		    		insertRowData.set("goodsName", 		data[0].itemName);
		    		insertRowData.set("storageMethodTypeName", 	data[0].storageMethodTypeName);
		    		insertRowData.set("recommendedPrice",	data[0].recommendedPrice);
		    		$("#clickIdx").val('');
				}
			}
		});
	};

	// 주문자정보찾기
	function fnSelectUser(pmCouponId){
        fnKendoPopup({
			id     : 'cpnMgmIssueList',
			title  : '주문생성',
			src    : '#/cpnMgmIssueList',
			width  : '1100px',
			height : '900px',
			param  : { "pmCouponId" : pmCouponId},
			success: function(id, data ){
				if (data.rows.length != 1) {
					alert('주문자는 한명만 선택 할 수 있습니다.');
					return;
				}
				var info = data.rows[0];
				var buyerHp = fnPhoneNumberHyphen(info.mobile).split("-");
				if (buyerHp.length == 3) {
					$("#buyerHp1").val(buyerHp[0])
					$("#buyerHp2").val(buyerHp[1])
					$("#buyerHp3").val(buyerHp[2])
				} else {
					alert('휴대폰 자릿수가 틀립니다. 확인 바랍니다.'); return;
				}
				var buyerMail = info.email.split("@");
				if (buyerMail.length == 2) {
					$("#buyerMail1").val(buyerMail[0]);
					$("#buyerMail2").val(buyerMail[1]);
				}
				$("#buyerNm").val(info.userName);
				$("#buyerHp").val(info.mobile);
				$("#buyerTel").val(info.mobile);
				$("#buyerMail").val(info.email);
				$("#urUserId").val(info.urUserId);
			}
		});
	};

	// 저장
	function fnSave() {
		if(fnValidationCheck() == false) return;

    	$("#fileNm").val($("#uploadLink").text());
    	$("#orderType").val($("input[name='orderCreateTypeRadio']:checked").val());

		var url = "/admin/order/create/addOrderCreate";
		fnKendoMessage({
			type    : "confirm"
			, message : "저장을 하시겠습니까?"
			, ok      : function(e){
							fnInputFormSubmit(url, "save");
			}
			, cancel  : function(e){
			}
		});
	};

    // 엑셀 업로드 submit
    function fnInputFormSubmit(url, cbId){
        let inputFormData = $("#insertForm").formSerialize(true);

        inputFormData['insert'] = kendo.stringify( fnGetDataList() );

        fnAjax({
            url     : url,
            params  : inputFormData,
            success : function( successData ){
            	fnBizCallback(cbId, successData);
            },
            isAction : 'insert'
        })
    };

    // 그리드 목록 가져오기
    function fnGetDataList(){
        let DataArray = [];
        let aGridData = $("#aGrid").data("kendoGrid").dataSource.data();
        let aGridCnt = aGridData.length;

        if( aGridCnt > 0 ){
            for(let i = 0; i < aGridCnt; i++){
                let orgData = {};
                orgData.recvNm      = aGridData[i].recvNm;
                orgData.recvHp  	= aGridData[i].recvHp;
                orgData.recvZipCd   = aGridData[i].recvZipCd;
                orgData.recvAddr1   = aGridData[i].recvAddr1;
                orgData.recvAddr2  	= aGridData[i].recvAddr2;
                orgData.ilItemCd    = aGridData[i].ilItemCd;
                orgData.itemBarcode = aGridData[i].itemBarcode;
                orgData.goodsId     = aGridData[i].goodsId;
                orgData.goodsName   = aGridData[i].goodsName;
                orgData.storageMethodTypeName	= aGridData[i].storageMethodTypeName;
                orgData.orderCnt        		= aGridData[i].orderCnt;
                orgData.recommendedPrice        = aGridData[i].recommendedPrice;
                orgData.salePrice        		= aGridData[i].salePrice;
                orgData.orderAmt        		= aGridData[i].orderAmt;
                DataArray[i] = orgData;
            }
        }
        return DataArray;
    };

    /**
     * 콜백합수
     */
    function fnBizCallback(id, data) {
        switch(id){
            case 'save':
            	var result = data.orderRegistrationResult;
            	if (result == 'SUCCESS') {
            		result = "저장 되었습니다.";
            		fnKendoMessage({
            			message : result
            			, ok : function (e) {
									fileClear();
									$("#uploadViewControl").hide();
									$("#uploadLink").text("");
									$("#aGrid").data("kendoGrid").dataSource.data( [] );
					            }
            		});
            	}
                break;
        }
    };

	function fnValidationCheck() {
        let aGridData = $("#aGrid").data("kendoGrid").dataSource.data();
        let aGridCnt = aGridData.length;
        if( aGridCnt > 0 ){
            for(let i = 0; i < aGridCnt; i++){
            	if (aGridData[i].recvNm == '') {
            		alert("수취인명을 입력해 주세요");
            		return false;
            	}
            	if (aGridData[i].recvHp == '') {
            		alert("핸드폰번호를 입력해 주세요");
            		return false;
            	} else {
            		$("#orderTel").val(aGridData[i].recvHp);
            		if (!fnValidatePhone(aGridData[i].recvHp)) {
            			alert("핸드폰 형식이 틀립니다.")
            			return false;
            		}
            	}
            	if (aGridData[i].recvZipCd == '') {
            		alert("우편번호를 입력해 주세요");
            		return false;
            	}
            	if (aGridData[i].recvAddr1 == '') {
            		alert("주소1를 입력해 주세요");
            		return false;
            	}
            	if (aGridData[i].recvAddr2 == '') {
            		alert("주소2를 입력해 주세요");
            		return false;
            	}
            	if (aGridData[i].goodsId == '') {
            		alert("상품을 입력해 주세요");
            		return false;
            	}
            	if (aGridData[i].orderCnt == '0') {
            		alert("수량을 입력해 주세요");
            		return false;
            	} else {
            		$("#orderNum").val(aGridData[i].orderCnt);
            		if (!checkNum($("#orderNum"))) {
            			alert("숫자만 입력 가능합니다.");
            			return false;
            		}
            	}
            	if (aGridData[i].salePrice == '0') {
            		alert("판매가를 입력해 주세요");
            		return false;
            	} else {
            		$("#salePrice").val(aGridData[i].salePrice);
            		if (!checkNum($("#salePrice"))) {
	            		alert("숫자만 입력 가능합니다.");
	            		return false;
            		}
            	}
            	if (aGridData[i].orderAmt == '0') {
            		if (aGridData[i].orderCnt != '0' && aGridData[i].salePrice != '0') {
            			aGridData[i].set("orderAmt", aGridData[i].salePrice * aGridData[i].orderCnt);
            		} else {
                		alert("주문금액을 입력해 주세요");
                		return false;
            		}
            	} else {
            		$("#orderAmt").val(aGridData[i].orderAmt);
            		if (!checkNum($("#orderAmt"))) {
	            		alert("숫자만 입력 가능합니다.");
	            		return false;
            		}
            	}
            }
        } else {
        	alert('주문생성 엑셀 업로드를 하셔야 합니다.')
        	return false;
        }

        if ($("#buyerNm").val() == '') {
        	alert('주문자명을 입력 해야 합니다.');
        	$("#buyerNm").focus();
        	return false;
        }
        if ($("#buyerHp1").val() == '') {
        	alert('주문자명을 입력 해야 합니다.');
        	$("#buyerHp1").focus();
        	return false;
        }
        if ($("#buyerHp2").val() == '') {
        	alert('주문자명을 입력 해야 합니다.');
        	$("#buyerHp2").focus();
        	return false;
        }
        if ($("#buyerHp3").val() == '') {
        	alert('주문자명을 입력 해야 합니다.');
        	$("#buyerHp3").focus();
        	return false;
        }
        if ($("#buyerMail").val() == '') {
        	alert('메일주소를 입력 해야 합니다.');
        	$("#buyerMail1").focus();
        	return false;
        }
        return true;
	}


	//-------------------------------  Common Function end -------------------------------
	$scope.fnOrderExcelUpload = function(){ fnOrderExcelUpload(); }; // 업로드 파일첨부 버튼
	$scope.fnSamepleFormDownload = function(){ fnSamepleFormDownload(); }; // 샘플다운로드 버튼
	$scope.fnBtnExcelSelect = function(fileType) {$('#' + fileType).trigger('click');};
	$scope.fnExcelUploadRun = function(event) { fnExcelUploadRun(event);} // 엑셀 업로드 버튼
	$scope.fnExcelUpload = function(event) { excelExport(event);} // 엑셀 업로드 버튼
	$scope.fnSelectUser = function(){ fnSelectUser(); };	//회원정보 찾기
	$scope.fnSave = function() { fnSave(); }	//저장

});

