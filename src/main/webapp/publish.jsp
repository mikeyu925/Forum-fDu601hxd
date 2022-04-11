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
    <title>Title</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
<%--EL表达式： ${pageContext.request.contextPath} 即获取应用的上下文路径   --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/bootstrap.min.css">
    <script src="${pageContext.request.contextPath}/static/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/static/jquery.min.js"></script>
</head>
<body>
    <div class="container">
        <ul class="nav nav-tabs">
            <c:forEach items="${categoryList}" var="category">
                <li>
                    <a href="${pageContext.request.contextPath}/topic?method=list&c_id=${category.id}"> ${category.name}</a>
                </li>
            </c:forEach>
        </ul>
    </div>

    <div style="margin-top: 100px">

        <form class="form-horizontal" role="form" action="${pageContext.request.contextPath}/topic?method=addTopic" method="post">
            <div class="form-group">
                <label  class="col-sm-2 control-label">标题</label>
                <div class="col-lg-3">
                    <input type="text" class="form-control" name="title" placeholder="标题">
                </div>
            </div>

            <div class="form-group">
                <label  class="col-sm-2 control-label">分类</label>
                <div>
                    <select class="form-control" name="c_id">
                        <c:forEach items="${categoryList}" var="category">
                            <option value="${category.id}">${category.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </div>


            <div class="form-group">
                <label  class="col-sm-2 control-label">内容</label>
                <div class="col-lg-3">
                    <textarea class="form-control" name="content" placeholder="请输入内容:"></textarea>
                </div>
            </div>


            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-3">
                    <button type="submit" class="btn btn-default">发布主题</button>
                </div>
            </div>

        </form>

    </div>
</body>
</html>
