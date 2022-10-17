let index = {
    init: function () {
        $("#btn-save").on("click", () => { // ()=>{}을 사용하는 이유는 코드를 줄이려고 하는게 아니라 this를 바인딩 하기 위해서!!
            this.save();
        });
        $("#btn-login").on("click", () => {
            this.login();
        });
    },

    save: function () {
        //alert("user의 save함수 호출 됨");
        let data = {
            username: $("#username").val(),
            password: $("#password").val(),
            email: $("#email").val()
        };
        //console.log(data);

        $.ajax({
            type: "post",
            url: "/api/user",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",//body데이터가 어떤 타입인지(MIME)
            dataType: "json"//<- 이렇게 응답받는 데이터 타입을 명시하지 않아도 서버에서 통신 성공시에 보내는
            //    데이터 MIME타입을 가지고 jQuery가 알아서 추론해서
            //                  서버에서 보내는 데이터가 json이라면 js object로 변환해준다.
        }).done(function (resp) {
            alert("회원가입이 완료되었습니다.");
            //console.log(resp);
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    login: function () {
        //alert("user의 save함수 호출 됨");
        let data = {
            username: $("#username").val(),
            password: $("#password").val()
        };
        //console.log(data);

        $.ajax({
            type: "post",
            url: "/api/user/login",
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            dataType: "json"
        }).done(function (resp) {
            alert("로그인이 완료되었습니다.");
            location.href = "/";
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }
}
index.init();