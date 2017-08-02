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

.navbar {
	overflow: hidden;
	background-color: #333;
	position: fixed;
	bottom: 0;
	width: 100%;
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
	height: 1500px; /* Used in this example to enable scrolling */
}
</style>
</head>
<body>
<div class="container">

    <div class = "div-right">  <p>Logged in user: ${username}<p>
               
              <form:form method="POST" action="/MovieRental/admin/logout">  
                    <input type="submit" value="Logout" class="btn btn-info "/>
                    </form:form></div>  
    <div class = "div-left"><h1>Product Delete List</h1></div>

</div>
                    <div class="main">
            <form:form method="POST" action="/MovieRental/admin/deleteMovie">
			<table class="table table-striped">
			<tr>
							<th>Movie Name</th>
							<th>Published year</th>
							<th>Country</th>
							<th>Type</th>
							<th>Rental Price</th>
							<th>Rating</th>

            </tr>
            <c:forEach var="d" items="${deteleList}">
                        <tr>
                        <td style="display:none;"><input type = text name = "id" value="${d.id}" /></td>
							<td><c:out value="${d.name}"></c:out></td>
							<td><c:out value="${d.year}"></c:out></td>
							<td><c:out value="${d.country}"></c:out></td>
							<td><c:out value="${d.type}"></c:out></td>
							<td><c:out value="${d.rent}"></c:out></td>
							<td><c:out value="${d.rating}"></c:out></td>

						</tr>
               
            </c:forEach>
            
			      
			</table>
			<c:if test="${not empty error}">
				<div class="error">${error}</div>
			</c:if>
			<h2>
				<input type="submit" name="delete" value="Confirm" class="btn btn-info"> 
				<input type="submit" name="cancel" value="Cancel" class="btn btn-info">
			</h2>
			</form:form>
		</div>
</body>
</html>