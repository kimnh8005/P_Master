﻿/**-----------------------------------------------------------------------------
 * description 		 : 공지사항 상세 < 거래처 공지/문의 < 거래처 관리
 * description 		 : 공지사항(입점사 또는 공급사) 상세 < 공지/문의
 * @
 * @ 수정일			수정자          수정내용
 * @ ------------------------------------------------------
 * @ 2017.02.21		김아란          최초생성
 * @
 * **/
'use strict';

var pageParam = fnGetPageParam();
var viewModel;
var allowedImageExtensionList = ['.jpg', '.jpeg' ];	// 업로드 가능한 이미지 확장자 목록
var storePcImageImageUploadMaxLimit = 512000;			//첨부파일 최대 용량
var storeMobileImageImageUploadMaxLimit = 512000;			//첨부파일 최대 용량
var publicStorageUrl;	// Public 저장소 Root Url

$(document).ready(function() {


	viewModel = new kendo.data.ObservableObject({
		storePcImageList : [],											//매장 이미지(PC) 첨부파일
		storePcImageFileList : [],										//매장 이미지(PC) 첨부파일 목록
		storePcImageUploadResultList : [],								//매장 이미지(PC) 첨부파일 업로드 결과 Data
		storeMobileImageList : [],										//매장 이미지(Mobile) 첨부파일
		storeMobileImageFileList : [],									//매장 이미지(Mobile) 첨부파일 목록
		storeMobileImageUploadResultList : [],							//매장 이미지(Mobile) 첨부파일 업로드 결과 Data

		// 매장 이미지(PC) 첨부파일 thumbnail 내 "X" 클릭 이벤트 : 이미지 삭제
		fnRemoveStorePcImage : function(e) {

			for (var i = viewModel.get("storePcImageList").length - 1; i >= 0; i--) {
				if (viewModel.get("storePcImageList")[i]['imageName'] == e.data["imageName"]) {
					viewModel.get("storePcImageList").splice(i, 1); // viewModel 에서 삭제
				}
			}

			var storePcImageFileList = viewModel.get('storePcImageFileList');

			for (var i = storePcImageFileList.length - 1; i >= 0; i--) {
				if (storePcImageFileList[i]['name'] == e.data['imageName']) {
					storePcImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
				}
			}

			var storePcImageUploadResultList = viewModel.get('storePcImageUploadResultList');

			for (var i = storePcImageUploadResultList.length - 1; i >= 0; i--) {
				storePcImageUploadResultList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
			}

		},

		// 매장 이미지(Mobile) 첨부파일 thumbnail 내 "X" 클릭 이벤트 : 이미지 삭제
		fnRemoveStoreMobileImage : function(e) {

			for (var i = viewModel.get("storeMobileImageList").length - 1; i >= 0; i--) {
				if (viewModel.get("storeMobileImageList")[i]['imageNameMobile'] == e.data["imageNameMobile"]) {
					viewModel.get("storeMobileImageList").splice(i, 1); // viewModel 에서 삭제
				}
			}

			var storeMobileImageFileList = viewModel.get('storeMobileImageFileList');

			for (var i = storeMobileImageFileList.length - 1; i >= 0; i--) {
				if (storeMobileImageFileList[i]['name'] == e.data['imageNameMobile']) {
					storeMobileImageFileList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
				}
			}

			var storeMobileImageUploadResultList = viewModel.get('storeMobileImageUploadResultList');

			for (var i = storeMobileImageUploadResultList.length - 1; i >= 0; i--) {
				storeMobileImageUploadResultList.splice(i, 1); // 삭제한 파일명에 해당하는 file 객체 삭제
			}

		}

	});


	fnInitialize();	//Initialize Page Call ---------------------------------
	publicStorageUrl = fnGetPublicUrlPath(); // Public 저장소 Root Url 조회

	function fnGetPublicUrlPath() { // public 저장소를 경유하지 않고 API 서버로 저장소 접근시 url 경로 조회 ( CORS 회피용 )

		var publicUrlPath;

		fnAjax({
			url : "/comn/getPublicStorageUrl",
			method : 'GET',
			async: false,
			success : function(data, status, xhr) {
				publicUrlPath = data['publicUrlPath'] + '/';
			},
			isAction : 'select'
		});

		return publicUrlPath;
	}

	//Initialize PageR
	function fnInitialize(){
		$scope.$emit('fnIsMenu', { flag : 'true' });

		fnPageInfo({
			PG_ID  : 'shopMgm',
			callback : fnUI
		});
	}

	function fnUI(){

		fnInitUI();

	   	fnDefaultValue();

		fnInitButton();	//Initialize Button  ---------------------------------

		fnInitOptionBox();//Initialize Option Box ------------------------------------
	}




	function fnInitUI(){
		kendo.bind($("#view"), viewModel);
	}

	//--------------------------------- Button Start---------------------------------

	function fnDefaultValue(){
		// 게시글 상세
		fnAjax({
			url     : 'admin/ur/store/getStoreDetail',
			params  : {urStoreId : pageParam.urStoreId },
			success :
				function( data ){
					// 매장유형
					$("#storeCategoryName").html(data.row.storeCategoryName);

					var onlineDivYn= '';
					if(data.row.onlineDivYn == 'Y'){
						onlineDivYn = '예';
					}else{
						onlineDivYn = '아니오';
					}
					// O2O매장여부
					$("#onlineDivYn").html(onlineDivYn);
					// 매장코드
					$("#urStoreId").html(data.row.urStoreId);
					// 매장명
					$("#storeName").html(data.row.storeName);
					// 대표번호
					$("#tel").html(data.row.tel);
					// 매장주소
					$("#address").html(data.row.address);
					// 매장위치
					$("#locate").html(data.row.locate);
					var storeTime = data.row.openTime + '~' + data.row.closeTime
					if(data.row.openTime != null){
						// 매장 영업시간
						$("#storeTime").html(storeTime);
					}
					// 매장 주문설정
					$("#deliveryTypeName").html(data.row.deliveryTypeName);
					// 매장 출고불가일
					$("#undeliverDt").html(data.row.undeliverDt);
					// 최근 업데이트 일자
					$("#modifyDate").val(data.row.modifyDate);
					//매장상태
					$("#statusName").html(data.row.statusName);

					if(data.row.status != 'STORE_STATUS.USE'){
						$("#useYn_0").attr("disabled", true);
						$("#useYn_1").attr("disabled", true);
					}
					// 노출여부
					if(data.row.useYn == 'Y'){
						$('#useYn_0').prop("checked",true);
					}else{
						$('#useYn_1').prop("checked",true);
					}

					var storePcImageUrl = data.row.storePcPath;

					if(storePcImageUrl != null){
						//상품 공지 > 상세 상단공지 이미지
						viewModel.get('storePcImageList').push({
							imageName : data.row.storePcPath, // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : data.row.storePcPath, // 원본 File 명
							sort : '', // 정렬순서
							imageSrc : publicStorageUrl+data.row.storePcPath// 상품 이미지 url
						});
					}

					var storeMobileImageUrl = data.row.storeMobilePath;

					if(storeMobileImageUrl != null){
						//상품 공지 > 상세 상단공지 이미지
						viewModel.get('storeMobileImageList').push({
							imageNameMobile : data.row.storeMobilePath, // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : data.row.storeMobilePath, // 원본 File 명
							sort : '', // 정렬순서
							imageSrcMobile : publicStorageUrl+data.row.storeMobilePath// 상품 이미지 url
						});
					}

					if(data.storeDeliveryArea.length > 0){

						var storeDeliveryHtml = "";
						var urDeliveryAreaId = "";
						storeDeliveryHtml += "<h1 class='title' style='font-size: 26px; font-weight: 600;'> 매장 기본정보</h1>";
						storeDeliveryHtml += "<br>";

						for (var i = 0; i < data.storeDeliveryArea.length ; i++) {

							if(urDeliveryAreaId != data.storeDeliveryArea[i].urDeliveryAreaId){
								storeDeliveryHtml += "<h3>▶"+data.storeDeliveryArea[i].deliveryAreaNm +"</h3>";
								storeDeliveryHtml += "<table class='datatable v-type'>";
								storeDeliveryHtml += "		<colgroup>";
								storeDeliveryHtml += "			<col style='width: 20%'>";
								storeDeliveryHtml += "			<col style='width: 20%'>";
								storeDeliveryHtml += "			<col style='width: 30%'>";
								storeDeliveryHtml += "			<col style='width: 30%'>";
								storeDeliveryHtml += "		</colgroup>";

								storeDeliveryHtml += "		<tr>";
								storeDeliveryHtml += "			<th style='text-align:center;'><label>회차</label></th>";
								storeDeliveryHtml += "			<th style='text-align:center;'><label>주문 마감 시간</label></th>";
								storeDeliveryHtml += "			<th style='text-align:center;'><label>주문 배송 시간</label></th>";
								storeDeliveryHtml += "			<th style='text-align:center;'><label>출고한도(결제 완료 된 배송번호 기준)</label></th>";
								storeDeliveryHtml += "		</tr>";
								storeDeliveryHtml += "	<tr>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].scheduleNo+"</td>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].cutoffTime+"</td>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].storeOrderTime+"</td>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].limitCnt+"</td>";
								storeDeliveryHtml += "	</tr>";
							}else{
								storeDeliveryHtml += "	<tr>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].scheduleNo+"</td>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].cutoffTime+"</td>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].storeOrderTime+"</td>";
								storeDeliveryHtml += "		<td style='text-align:center;'>"+data.storeDeliveryArea[i].limitCnt+"</td>";
								storeDeliveryHtml += "	</tr>";
							}

							if(i<data.storeDeliveryArea.length-1){

								if(data.storeDeliveryArea[i].urDeliveryAreaId != data.storeDeliveryArea[i+1].urDeliveryAreaId){
									storeDeliveryHtml += "</table>";
									storeDeliveryHtml += "<br>";
								}
							}else{
								storeDeliveryHtml += "</table>";
								storeDeliveryHtml += "<br>";
							}

							urDeliveryAreaId = data.storeDeliveryArea[i].urDeliveryAreaId;
						}

						$("#storeDeliveryAreaContent").html(storeDeliveryHtml);
					}else{
						$("#storeDeliveryAreaContent").html("")
					}




//					// 첨부파일 정보
//					if(data.rowsFile != null && data.rowsFile != null){
//						var fileList = "";
//						fileList = fileList + data.rowsFile.physicalAttached+"\n";
//						$("#inputOrgBbsAttcId").val(data.rowsFile.csCompanyBbsAttachId);
//						$("span#V_CS_BBS_ATTC").html(fileList);
//						//$('#attcDown').attr('href', "/comn/getView?FILE_NAME="+data.rowsFile.realAttached+"&ORG_FILE_NAME="+data.rowsFile.physicalAttached+"&SUB_PATH=CS//compBbs//1//");
//						$('#attcDown').attr('href', "javascript:$scope.attchFileDownload('" + data.rowsFile.filePath + "', '" + data.rowsFile.realAttached + "', '" + data.rowsFile.physicalAttached + "')");
//						$('#attcDown').css("display" ,"");
//					}else{
//						$("span#V_CS_BBS_ATTC").html('첨부한 파일이 없습니다.');
//						$('#attcDown').css("display" ,"");
//					}
				},
			isAction : 'batch'
		});
	}
	function fnInitButton(){
		$('#fnSave,  #fnList').kendoButton();
//		var ssRoleId = PG_SESSION.roleId;
//		if(ssRoleId == '10' || ssRoleId == '11'){
//			$("#fnMod").css("display", "none");
//		}
	}


    // 목록화면 이동
    function fnList(){
    	var ssCompanyActType = PG_SESSION.roleId;
    	var option = new Object();

    	option.url = "#/shopList";
    	option.menuId = 755;
    	option.data = {
				};
    	$scope.$emit('goPage', option);

    }

    // 매장상세 저장
    function fnSave(){

    	var data = $('#inputForm').formSerialize(true);
    	var url = '/admin/ur/store/modifyStoreDetail'

		if (viewModel.get('storePcImageFileList').length > 0) {									// 상세 상단공지 이미지 존재시
			var storePcImageUploadResultList = fnUploadStorePcImage();							// 상세 상단공지 이미지 / 결과 return
			viewModel.set('storePcImageUploadResultList', storePcImageUploadResultList);
			viewModel.set('storePcImageFileList', []);											// 기존 상세 상단공지 이미지 초기화
		}

		if (viewModel.get('storeMobileImageFileList').length > 0) {																		// 상세 하단공지 이미지 존재시
			var storeMobileImageUploadResultList = fnUploadStoreMobileImage();															// 상세 하단공지 이미지 / 결과 return
			viewModel.set('storeMobileImageUploadResultList', storeMobileImageUploadResultList);
			viewModel.set('storeMobileImageFileList', []);																					// 기존 상세 하단공지 이미지 초기화
		}
		var addStoreParam = {
			 storePcImageUploadResultList			: viewModel.get("storePcImageUploadResultList")
			,storeMobileImageUploadResultList		: viewModel.get("storeMobileImageUploadResultList")
			,useYn									: data.useYn
			,urStoreId								: pageParam.urStoreId
		}


		fnAjax({
			url : url,
			params : addStoreParam,
			contentType : 'application/json',
			isAction : 'update',
			success : function(data) {
					fnBizCallback('update' , data);
			},
			error : function(xhr, status, strError) {
				fnKendoMessage({
					//message : xhr.responseText
					message : '오류가 발생하였습니다. 관리자에게 문의해 주세요.'
				});
			}
		});

    }


	function fnStoreDetail(){
		//상품 공지 > 상세 상단공지 이미지 Upload Info
		var storePcImageUrl = data.storePcImageUrl;

		if(storePcImageUrl != null){
			//상품 공지 > 상세 상단공지 이미지
			viewModel.get('storePcImageList').push({
				imageName : data.noticeBelow1ImageUrl, // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
				imageOriginalName : data.ilGoodsDetail.noticeBelow1ImageUrl, // 원본 File 명
				sort : '', // 정렬순서
				imageSrc : publicStorageUrl+data.ilGoodsDetail.noticeBelow1ImageUrl // 상품 이미지 url
			});

			var noticeBelow1ImageUrlExtensionSplit = noticeBelow1ImageUrl.split('.');
			var noticeBelow1ImageUrlFileSplit = noticeBelow1ImageUrl.split('/');
			var fileExtension1 = noticeBelow1ImageUrlExtensionSplit[noticeBelow1ImageUrlExtensionSplit.length-1];
			var physicalFileName1 = noticeBelow1ImageUrlFileSplit[noticeBelow1ImageUrlFileSplit.length-1];
			var serverSubPath1 = noticeBelow1ImageUrl.replace(physicalFileName1, "");

			viewModel.get('noticeBelow1ImageUploadResultList').push({
				fieldName : "noticeBelow1Image01",
				originalFileName : "",
				fileExtension : fileExtension1,
				serverSubPath : serverSubPath1,
				physicalFileName : physicalFileName1,
				contentType : "",
				fileSize : ""
			});
		}
	}
	//--------------------------------- Button End---------------------------------

	//---------------Initialize Option Box Start ------------------------------------------------
	function fnInitOptionBox(){

		//노출여부
		fnTagMkRadio({
            id    : 'useYn'
          , tagId : 'useYn'
          , data  : [
                      {"CODE" : "Y", "NAME" : '예' }
                     , {"CODE" : "N", "NAME" : '아니오' }
                    ]
          , chkVal: "Y"
          , style : {}
      });

		// 매장 이미지(PC)
		$('#uploadStorePcImageForm').html('');
		var htmlText = '<input type="file" id="uploadStorePcImage" name="uploadStorePcImage" accept=".jpg, .jpeg ">';

		$('#uploadStorePcImageForm').append(htmlText);

		fnKendoUpload({
			id : "uploadStorePcImage",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (storePcImageImageUploadMaxLimit < e.files[0].size) { // 매장 PC 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + parseInt(storePcImageImageUploadMaxLimit / 1024) + ' kb 입니다.',
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

					var storePcImageFileList = viewModel.get('storePcImageFileList');

					for (var i = storePcImageFileList.length - 1; i >= 0; i--) {
						if (storePcImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
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
						viewModel.get("storePcImageList").splice(0, 1);
						viewModel.get('storePcImageFileList').splice(0, 1);

						viewModel.get('storePcImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('storePcImageList').push({
							imageName : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrc : itemImageScr, // 상품 이미지 url
						});


					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});


		// 매장 이미지(Mobile)
		$('#uploadStoreMobileImageForm').html('');
		var htmlText = '<input type="file" id="uploadStoreMobileImage" name="uploadStoreMobileImage" accept=".jpg, .jpeg ">';

		$('#uploadStoreMobileImageForm').append(htmlText);

		fnKendoUpload({
			id : "uploadStoreMobileImage",
			select : function(e) {

				if (e.files && e.files[0]) { // 이미지 파일 선택시

					if (storeMobileImageImageUploadMaxLimit < e.files[0].size) { // 매장 Mobile 이미지 업로드 용량 체크
						fnKendoMessage({
							message : '이미지 업로드 허용 최대 용량은 ' + parseInt(storeMobileImageImageUploadMaxLimit / 1024) + ' kb 입니다.',
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

					var storeMobileImageFileList = viewModel.get('storeMobileImageFileList');

					for (var i = storeMobileImageFileList.length - 1; i >= 0; i--) {
						if (storeMobileImageFileList[i]['name'] == e.files[0]['name']) { // 파일명 중복 체크
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
						viewModel.get("storeMobileImageList").splice(0, 1);
						viewModel.get('storeMobileImageFileList').splice(0, 1);

						viewModel.get('storeMobileImageFileList').push(file); // 추가된 상품 이미지 File 객체를 전역변수에 추가

						viewModel.get('storeMobileImageList').push({
							imageNameMobile : file['name'], // local 에서 최초 추가한 이미지 : id 역할을 하는 imageName 으로 원본 파일명을 그대로 사용
							imageOriginalName : file['name'], // 원본 File 명
							sort : '', // 정렬순서
							imageSrcMobile : itemImageScr, // 상품 이미지 url
						});


					};

					reader.readAsDataURL(e.files[0].rawFile);
				}
			}
		});


	}
	//---------------Initialize Option Box End ------------------------------------------------

	//-------------------------------  Common Function start -------------------------------

     /**
	 * 콜백합수
	 */
	function fnBizCallback( id, data ){
		switch(id){
			case 'update':
                fnKendoMessage({
                    message : '저장되었습니다.'
                    , ok : function() { $('#fnList').trigger('click'); }
                });
				break;
		}
	}


	/**
	 * 첨부파일 다운로드
	 */
	function attchFileDownload(filePath, physicalFileName, originalFileName) {
		var opt = {
				filePath: filePath,
				physicalFileName: physicalFileName,
			    originalFileName: originalFileName
		}

		console.log("@@@@@@@@@@@@@ ", opt);

		fnDownloadPublicFile(opt);
	}

	function fnStorePcFile() {
		$('#uploadStorePcImage').trigger('click');
	}

	function fnStoreMobileFile() {
		$('#uploadStoreMobileImage').trigger('click');
	}

	function fnUploadStorePcImage() {

		var formData = new FormData();
		var storePcImageFileList = viewModel.get('storePcImageFileList');

		for (var i = 0; i < storePcImageFileList.length; i++) {
			// noticeBelow1Image01, noticeBelow1Image02, ... 형식으로 formData 에 이미지 file 객체 append, 현재는 단건만 저장됨!!!!
			formData.append('storePcImage' + ('0' + (i + 1)).slice(-2), storePcImageFileList[i]);
		}

		formData.append('storageType', 'public'); // storageType 지정
		formData.append('domain', 'ur'); // domain 지정

		var storePcImageUploadResultList; // 상품 이미지 업로드 결과 목록

		$.ajax({
			url : '/comn/fileUpload',
			data : formData,
			type : 'POST',
			contentType : false,
			processData : false,
			async : false,
			success : function(data) {
				data = data.data;

				storePcImageUploadResultList = data['addFile'];
			}
		});

		return storePcImageUploadResultList;
	}


	function fnUploadStoreMobileImage() {

		var formData = new FormData();
		var storeMobileImageFileList = viewModel.get('storeMobileImageFileList');

		for (var i = 0; i < storeMobileImageFileList.length; i++) {
			// noticeBelow1Image01, noticeBelow1Image02, ... 형식으로 formData 에 이미지 file 객체 append, 현재는 단건만 저장됨!!!!
			formData.append('storeMobileImage' + ('0' + (i + 1)).slice(-2), storeMobileImageFileList[i]);
		}

		formData.append('storageType', 'public'); // storageType 지정
		formData.append('domain', 'il'); // domain 지정

		var storeMobileImageUploadResultList; // 매장 이미지 업로드 결과 목록

		$.ajax({
			url : '/comn/fileUpload',
			data : formData,
			type : 'POST',
			contentType : false,
			processData : false,
			async : false,
			success : function(data) {
				data = data.data;

				storeMobileImageUploadResultList = data['addFile'];
			}
		});

		return storeMobileImageUploadResultList;
	}




    //-------------------------------  Common Function end -------------------------------


    //------------------------------- Html 버튼 바인딩  Start -------------------------------
    /** Common Clear*/
//    $scope.fnClear =function(){	 fnClear();};
    /** Common New*/
//    $scope.fnNew = function( ){	fnNew();};
    /** Common Mod */
//    $scope.fnMod = function( ){  fnMod();};
    /** Common Delete*/
//    $scope.fnDel = function(){	 fnDel();};

	$scope.fnSave = function( ){  fnSave();};
    /** Common List*/
    $scope.fnList = function( ){  fnList();};
    /** Common GoDetail*/
    $scope.fnGoDetail = function(csCompanyBbsId){  fnGoDetail(csCompanyBbsId);};

    $scope.fnStorePcFile = function( ){ fnStorePcFile(); };

    $scope.fnStoreMobileFile = function( ){ fnStoreMobileFile(); };

    /** Common GoDetail*/
    $scope.attchFileDownload = function(filePath, physicalFileName, originalFileName){  attchFileDownload(filePath, physicalFileName, originalFileName);};

    //------------------------------- Html 버튼 바인딩  End -------------------------------

}); // document ready - END
