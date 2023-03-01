<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="userId" value='<%=request.getParameter("userId")%>' scope="page"/>

<!DOCTYPE html>
<html lang="ko">
​
<head>
    <meta charset=utf-8><meta http-equiv=X-UA-Compatible content="IE=edge">
    <title>Title</title>
</head>
​
<body style="margin: 0;padding: 0">
    <table style="border:1px solid #cccccc; border-collapse: collapse" width="720" align="center" cellpadding="0"
        cellspacing="0">
        <tr>
            <td style="padding: 30px 0">
                <table style="border-collapse: collapse" align="center" cellpadding="0" cellspacing="0" width="660"
                    border="0">
                    <thead>
                        <tr>
                            <td style="border-bottom:3px solid #80c342;padding-top: 8px;font-size: 0;line-height: 0">
                                <img src="// 이미지 링크" alt="" style="margin-top: 8px;margin-bottom: 27px">
                            </td>
                        </tr>
                    </thead>
                    <tbody>

                        <tr>
                            <td>
                                <p style="font-size: 16px;font-weight: bold;padding:50px 0 16px ;margin: 0">비밀번호 찾기</p>
                                <table width="100%" align="center" cellpadding="0" cellspacing="0"
                                    style="border-collapse: collapse;border-top:1px solid #e1e1e1">
                                    <tr style="height: 47px;border-bottom: 1px solid #e1e1e1">
                                        <td
                                            style="background:#f8f8f8;padding-left:20px;font-weight: bold;font-size: 12px;width: 160px">
                                            아이디</td>
                                        <td style=";padding-left:20px;font-size: 12px;color:#666666">${findUserId}</td>
                                    </tr>
                                    <tr style="height: 47px;border-bottom: 1px solid #e1e1e1">
                                        <td
                                            style="background:#f8f8f8;padding-left:20px;font-weight: bold;font-size: 12px;width: 160px">
                                            이름</td>
                                        <td style=";padding-left:20px;font-size: 12px;color:#666666">${findUserName}</td>
                                    </tr>
                                    <tr style="height: 47px;border-bottom: 1px solid #e1e1e1">
                                        <td
                                            style="background:#f8f8f8;padding-left:20px;font-weight: bold;font-size: 12px;width: 160px">
                                            임시비밀번호</td>
                                        <td style=";padding-left:20px;font-size: 12px;color:#666666">${findUserPasswordId}</td>
                                    </tr>
                                 </table>
                            </td>
                        </tr>
                        <tr>
                            <td
                                style="height:50px;line-height: 50px;text-align: center;margin: 0 auto;padding-top: 60px">
                                <a href="{SITE_URL}"
                                    style="text-decoration: none;color: #ffffff;display:inline-block;font-size: 14px;background: #80c342;width: 240px;font-weight: bold">{comName}
                                    바로가기 <i style="font-weight: normal;font-style: normal">&gt;</i></a>
                            </td>
                        </tr>
                        <!--// contents-->
                    </tbody>
                </table>
            </td>
        </tr>
​
    </table>
</body>
​
</html>