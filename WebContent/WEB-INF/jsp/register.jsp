<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1"></meta>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"></link>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
<script>
jQuery(function(){
function checkPasswordMatch() {
    var password = $("#password").val();
    var confirmPassword = $("#confirmPassword").val();
    $("#error").hide();
    $("#javaerror").hide();
    if (password != confirmPassword){
        $("#error").html("Passwords do not match!");
    $("#error").show();
    }
}
$(document).ready(function () {
	   $("#password").keyup(checkPasswordMatch);
	});
$(document).ready(function () {
   $("#confirmPassword").keyup(checkPasswordMatch);
});
})
</script>
<style>
body {
	margin: 0;
}



.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}

.msg {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #31708f;
	background-color: #d9edf7;
	border-color: #bce8f1;
}

#login-box {
	width: 500px;
	padding: 40px;
	margin: 150px auto;
	background: #fff;
	-webkit-border-radius: 2px;
	-moz-border-radius: 2px;
	border: 1px solid #000;
}

</style>
<title>index</title>
</head>
<body>
	<h1 align="center">Product management system</h1>

	<div id="login-box">
		<form:form action="/MovieRental/login/register" method='POST'>
			<h3 align="center">Please fill in the form.</h3>

			<c:if test="${not empty error}">
				<div class="error" id = "javaerror">${error}</div>
			</c:if>
			
           <div class="error" style="display:none" id="error">
            </div>


			<label for="username">Username: </label>
			<input type=text id="username" name="username" class='form-control input-md'/>
			<br>
			<label for="password">Password: </label>
			<input type=password id="password" name="password" class='form-control input-md'/>
			<br>
			<label for="confirmPassword">Confirm Password: </label>
			<input type=password id="confirmPassword" name="confirmPassword" class='form-control input-md'/>
			<br>
			<h2>
			<input type="submit" name = "register" value="Register" class="btn btn-info" />
			<input type="submit" name = "cancel" value="Cancel" class="btn btn-info" />
			</h2>
		</form:form>
	</div>
</body>
</html>