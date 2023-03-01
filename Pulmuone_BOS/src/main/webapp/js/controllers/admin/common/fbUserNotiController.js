"use strict";

const userNotiPlugin = {
	init : function() {
		const self = this;
		self._setEvnet();
	},
	notReadCheck : function() {
		const self = this;
		// 공통이기 때문에 ajax로 처리
		$.ajax({
			type : "POST",
			dataType : "json",
			url : "/admin/comn/user/noti/isNotReadNoti",
			data : {},
			success : function(res, status, xhr) {
				if (res.code == "0000" && res.data) {
					self._showDot();
				} else {
					self._hiddenDot();
				}
			},
			error : function(xhr, status, strError) {
				fnKendoMessage({
					message : xhr.responseText
				});
			}
		});
	},
	_popupId : 'userNotiPopup',
	_$popupGridId : 'userNotiPopupGrid',
	_$popupGridDs : null,
	_$area : $('#userNotiArea'),
	_$dot : $('#userNotiDot'),
	_setPopup : function() {
		const self = this;
		$('<div id="userNotiPopup"><div id="userNotiPopupGrid"></div></div>')
				.kendoWindow({
					visible : false,
					modal : true
				});
	},
	_setPopupGrid : function() {
		const self = this;
		self._$popupGridDs = fnGetDataSource({
			url : ''
		});
		var userNotiPopupGridG;
		var gridOpt = {
			dataSource : self._$popupGridDs,
			height : '500px',
			navigatable : true,
			scrollable : true,
			columns : [
					{
						field : 'notiMsg',
						title : '알림내용',
						width : '200px',
						attributes : {
							style : 'text-align:left'
						},
						template : "#=(clickYn=='N') ? '<span style=\"font-weight: bold;\">' : ''# [#: userNotiTypeName #] #: notiMsg# #=(clickYn=='N') ? '</span>' : ''#"
					},
					{
						field : 'createDate',
						title : '시간',
						width : '100px',
						attributes : {
							style : 'text-align:center;'
						},
						template : "#=(clickYn=='N') ? '<span style=\"font-weight: bold;\">' : ''# #: createDate# #=(clickYn=='N') ? '</span>' : ''#"
					}
					 ]
		};
		userNotiPopupGridG = $('#' + self._$popupGridId).initializeKendoGrid(gridOpt).cKendoGrid();

		$('#' + self._$popupGridId).on("click", "tbody > tr > td", function(e) {
		    let dataItem = userNotiPopupGridG.dataItem(userNotiPopupGridG.select());

            // 클릭 처리
            $.ajax({
                type : "POST",
                dataType : "json",
                contentType : "application/x-www-form-urlencoded; charset=UTF-8",
                url : "/admin/comn/user/noti/putNotiReadClick",
                data : {
                    urNotiId : dataItem.urNotiId
                }
            });

            //화면이동
            var option = new Object();
            let targetType = dataItem.targetType;
            let targetPk = dataItem.targetPk;
            if (dataItem.userNotiType == 'USER_NOTI_TYPE.BOS_NOTI') {
                // 일반
                option.url    = '#/notice';
                option.menuId = 1306;
                option.target = '_blank';
                option.data = {
//                      id : targetPk
                };
            } else if (dataItem.userNotiType == 'USER_NOTI_TYPE.EVENT_START' || dataItem.userNotiType == 'USER_NOTI_TYPE.EVENT_END') {
                if(targetType == null) return;
                if(targetPk == null) return;
                if (targetType == 'EVENT_TP.EXPERIENCE') {
                    // 체험단
                    option.url    = '#/eventExprncList';
                    option.menuId = 1091;
                    option.target = '_blank';
                    option.data = {
                          evEventId : targetPk
                    };
                }
                else {
                    // 체험단이외
                    option.url    = '#/eventNormalList';
                    option.menuId = 1090;
                    option.target = '_blank';
                    option.data = {
                          evEventId : targetPk
                    };
                }
            } else if (dataItem.userNotiType == 'USER_NOTI_TYPE.EXHIBIT_START') {
                if(targetType == null) return;
                if(targetPk == null) return;
                if (targetType == 'EXHIBIT_TP.NORMAL') {   // 일반
                    option.url    = '#/exhibitNormalList';
                    option.menuId = 956;
                    option.target = '_blank';
                    option.data = {
                          evExhibitId : targetPk
                    };
                } else if (targetType == 'EXHIBIT_TP.SELECT') {
                    // 골라담기
                    option.url    = '#/exhibitSelectList';
                    option.menuId = 957;
                    option.target = '_blank';
                    option.data = {
                          evExhibitId : targetPk
                    };
                } else if (targetType == 'EXHIBIT_TP.GIFT') {
                    // 증정행사
                    option.url    = '#/exhibitGiftList';
                    option.menuId = 958;
                    option.target = '_blank';
                    option.data = {
                          evExhibitId : targetPk
                    };
                }
            }
//            fnGoPage(option);
            fnGoNewPage(option);
        });

	},
	_setEvnet : function() {
		const self = this;
		// 알림 클릭시 모달 처리
		self._$area.click(function() {
			$.ajax({
				type : "POST",
				dataType : "json",
				url : "/admin/comn/user/noti/getNotiListByUser",
				data : {},
				success : function(res, status, xhr) {
					if (res.code == "0000" && res.data != null) {
						self._$popupGridDs.data(res.data);
						self._readAction(res.data);
					} else {
						self._$popupGridDs.data([]);
					}
				},
				error : function(xhr, status, strError) {
					self._$popupGridDs.data([]);
					fnKendoMessage({
						message : xhr.responseText
					});
				}
			});

			if ($('#userNotiPopup').length == 0) {
				self._setPopup();
				self._setPopupGrid();
			}

			fnKendoInputPoup({
				id : self._popupId,
				height : "auto",
				width : "600px",
				title : {
					nullMsg : '관리자 알림'
				}
			});
		});
	},
	_readAction : function(list) {
		const self = this;
		var urNotiIds = [];
		$.each(list, function(i, data) {
			if (data.readYn == 'N') {
				urNotiIds.push(data.urNotiId);
			}
		});
		if (urNotiIds.length > 0) {
			$
					.ajax({
						type : "POST",
						dataType : "json",
						contentType : "application/x-www-form-urlencoded; charset=UTF-8",
						url : "/admin/comn/user/noti/putNotiRead",
						data : {
							urNotiIds : urNotiIds.join(",")
						},
						success : function(res, status, xhr) {
							if (res.code == "0000") {
								self._hiddenDot();
							}
						},
						error : function(xhr, status, strError) {
							fnKendoMessage({
								message : xhr.responseText
							});
						}
					});
		}
	},
	_showDot : function() {
		const self = this;
		self._$dot.show();
	},
	_hiddenDot : function() {
		const self = this;
		self._$dot.hide();
	}
}
