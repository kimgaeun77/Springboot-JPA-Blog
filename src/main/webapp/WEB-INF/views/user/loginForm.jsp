<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@ include file="../layout/header.jsp" %><%--상대 경로(자신(index)이 있는 위치)에서 이 파일을 찾음--%>
<div class="container">
    <form action="/auth/loginProc" method="post">
        <div class="form-group">
            <label for="username">Username</label>
            <input type="text" name="username" class="form-control" placeholder="Enter Username" id="username">
        </div>
        <div class="form-group">
            <label for="password">Password</label>
            <input type="password" name="password" class="form-control" placeholder="Enter password" id="password">
        </div>

        <button id="btn-login" class="btn btn-primary">로그인</button>
        <a href="https://kauth.kakao.com/oauth/authorize?client_id=ba2aa1db098ae55aec6349ea28d6b815&redirect_uri=http://localhost:8000/auth/kakao/callback&response_type=code">
            <img height="38px" src="/image/kakao_login_button.png"/>
        </a>
    </form>
</div>
<script src="/js/user.js"></script>
<%@ include file="../layout/footer.jsp" %>


