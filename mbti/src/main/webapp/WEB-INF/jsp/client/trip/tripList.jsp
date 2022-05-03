
<!DOCTYPE html>
<html>
<head>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta charset='utf-8'>
    <title>Trip list</title>
</head>
<body>
    trip list page
    <br/>
    ${tripList}
    <br/>
    <c:forEach items="${tripList}" var="list">
        <c:out value="${list.tripNm}"/> <br/>
    </c:forEach>

    

</body>
</html>