<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
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

	$('#submit').click(function() {  
	       $('input[type="text"]').each(function() { 
	        var $this = $(this); 
	        $this.attr("value", $this.attr("placeholder")).removeAttr("placeholder"); 
	    });

	    });

	})
</script>
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
<title>Product Management System</title>
</head>
<body>
<div class="container">

    <div class = "div-right">  <p>Logged in user: ${username}<p>
               
              <form:form method="POST" action="/MovieRental/admin/logout">  
                    <input type="submit" value="Logout" class="btn btn-info "/>
                    </form:form></div>  
    <div class = "div-left"><h1>Product Update List</h1></div>

</div>
                    
                    <div class="main">
        <form:form method="POST" action="/MovieRental/admin/updateMovie">
        <c:forEach var="u" items="${updateList}">
			<table class="table table-striped">
			<tr>
                          
							<th>Movie Name</th>
							<th>Published year</th>
							<th>Country</th>
							<th>Type</th>
							<th>Rental Price</th>
							<th>Rating</th>

            </tr>
                        <tr>
					
					        <td style="display:none;"><input type = text name = "id" value="${u.id}" /></td>
							<td width = "25%"><c:out value="${u.name}" /></td>
							<td width = "20%"><input type = text name = "year" placeholder="${u.year}" class='form-control input-md'></td>
							<td width = "10%"><input type = text name = "country" placeholder="${u.country}" class='form-control input-md'></td>
							<td width = "15%"><input type = text name = "type" placeholder="${u.type}" class='form-control input-md'></td>
							<td width = "15%"><input type = text name = "rent" placeholder="${u.rent}" class='form-control input-md'></td>
							<td width = "15%"><input type = text name = "rating" placeholder="${u.rating}" class='form-control input-md'></td>

						</tr>
			</table>
			</c:forEach>
			<c:if test="${not empty error}">
				<div class="error">${error}</div>
			</c:if>
		<h2>
			<input type="submit" name="update" value="Update" id ="submit" class="btn btn-info"> 
			<input type="submit" name="cancel" value="Cancel" class="btn btn-info">
			</h2>
			</form:form>
			</div>
</body>
</html>