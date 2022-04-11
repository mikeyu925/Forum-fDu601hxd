<%--
  Created by IntelliJ IDEA.
  User: 86135
  Date: 2022/4/10
  Time: 15:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>fDu8-601学习交流论坛</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
<%--EL表达式： ${pageContext.request.contextPath} 即获取应用的上下文路径   --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/static/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/jquery.min.js"></script>
</head>
<body>
    <div class="container">
        <ul class="nav nav-tabs">
<%--            for 循环--%>
            <c:forEach items="${categoryList}" var="category">
                <li>
                    <a href="${pageContext.request.contextPath}/topic?method=list&c_id=${category.id}"> ${category.name}</a>
                </li>
            </c:forEach>
            <%--登录部分--%>
            <c:choose>
                <c:when test="${empty loginUser}">
                <%--没有登录则 跳转至登录或注册页面--%>
                <li style="float: right"><a href="${pageContext.request.contextPath}/user/register.jsp"> 注册</a> </li>
                <li style="float: right"><a href="${pageContext.request.contextPath}/user/login.jsp"> 登录</a> </li>
                </c:when>
                <c:otherwise>
                    <li style="float: right"><a href="${pageContext.request.contextPath}/user?method=logout"> 注销</a> </li>
                    <li style="float: right"><a href="#">${loginUser.username}</a> </li>
                    <li style="float: right">
                        <img src="${loginUser.img}" class="img-circle" width="25px" height="25px" style="margin-top: 8.5px">
                    </li>

                    <li style="float: right">
                        <a href="${pageContext.request.contextPath}/publish.jsp"> 发布主题</a>
                    </li>
                </c:otherwise>
            </c:choose>
        </ul>
    </div>

    <div>
        <table class="table table-bordered">
            <thead>
            <tr>
                <th>标题</th>
                <th>内容</th>
                <th>作者</th>
                <th>发布时间</th>
                <th>操作</th>
            </tr>
            </thead>
            <tbody>
                <c:forEach items="${topicPage.list}" var="topic">
                    <tr>
                        <td>${topic.title}</td>
                        <td>${topic.content}</td>
                        <td>${topic.username}</td>
                        <td>${topic.createTime}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/topic?method=findDetailById&topic_id=${topic.id}">详情</a>
                        </td>

                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <div>
            <ul class="pagination">
                <li>
                    <a href="#" aria-label="Previous">
                        <span aria-hidden="true">&laquo;</span>
                    </a>
                </li>

                <c:if test="${topicPage.totalPage>0}">
                    <c:forEach var="i" begin="0" end="${topicPage.totalPage-1}" step="1">
                        <li>
                            <a href="${pageContext.request.contextPath}/topic?method=list&c_id=${param.c_id}&page=${i+1}">${i+1}</a>
                        </li>
                    </c:forEach>
                </c:if>

                <li>
                    <a href="#" aria-label="Next">
                        <span aria-hidden="true">&raquo;</span>
                    </a>
                </li>
            </ul>
    </div>


</body>
</html>


<%-- http://localhost:8080/topic?method=list&c_id=1 --%>