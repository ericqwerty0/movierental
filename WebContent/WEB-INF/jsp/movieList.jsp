<!DOCTYPE html>
<%@taglib uri = "http://www.springframework.org/tags/form" prefix = "form"%>
<%@taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1"></meta>
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"></link>
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<script type="text/javascript">
	function check_all(obj, cName) {
		var checkboxs = document.getElementsByName(cName);
		for (var i = 0; i < checkboxs.length; i++) {
			checkboxs[i].checked = obj.checked;
		}
	}
</script>
<script>

	jQuery(function() {
		var checkboxes = $("input[type='checkbox']");
		$('#check1').click(function() {
			$("#error").hide();
			if (checkboxes.is(":checked")) {
			} else {
				$("#error").show();
				return false;
			}
		});
		$('#check2').click(function() {
			$("#error").hide();
			if (checkboxes.is(":checked")) {
			} else {
				$("#error").show();
				return false;
			}
		});
	})
</script>

<title>Product Management System</title>
<style>
body {
	margin: 0;
}

.div-left{
    float:left;
    padding-left:10px;
}
.div-right{
    float:right;
    padding-right:10px;
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

.navbar a {
	float: left;
	display: block;
	color: #f2f2f2;
	text-align: center;
	padding: 14px 16px;
	text-decoration: none;
}

.main {
	padding: 16px;
	margin-top: 30px;
	height: 1500px;
}
</style>
</head>
<body>
<div class="container">

    <div class = "div-right">  <p>Logged in user: ${username}<p>
               
              <form:form method="POST" action="/MovieRental/admin/logout">  
                    <input type="submit" value="Logout" class="btn btn-info "/>
                    </form:form></div>  
    <div class = "div-left"><h1>Product List</h1></div>

</div>
             
	<div class="main">


            <form:form method="POST" action="/MovieRental/admin/modify">
			<table class="table table-striped">
			<thead>
			<tr>
                <th><input type="checkbox" name="all"
								onclick="check_all(this,'movieId')" /></th>
							<th>Movie Name</th>
							<th>Published year</th>
							<th>Country</th>
							<th>Type</th>
							<th>Rental Price</th>
							<th>Rating</th>

            </tr>
            </thead>
            <tbody>
            <c:forEach var="movie" items="${movieList}">
                        <tr>
							<td><input type="checkbox" name="movieId" id ="id" value="${movie.id}" /></td>
							<td><c:out value="${movie.name}"></c:out></td>
							<td><c:out value="${movie.year}"></c:out></td>
							<td><c:out value="${movie.country}"></c:out></td>
							<td><c:out value="${movie.type}"></c:out></td>
							<td><c:out value="${movie.rent}"></c:out></td>
							<td><c:out value="${movie.rating}"></c:out></td>

						</tr>
               
            </c:forEach>
            </tbody>
			      
			</table>
			<div id="error" style="display:none" class="error">Please select at least one product</div>
			<c:if test="${not empty error}">
				<div class="error">${error}</div>
			</c:if>
			<h2>
				<input type="submit" name="create" value="Create" class="btn btn-info"> 
				<input type="submit" name="update" value="Update" id = "check1" class="btn btn-info"> 
				<input type="submit" name="delete" value="Delete" id = "check2" class="btn btn-info">
			</h2>
			</form:form>
		</div>
</body>
</html>