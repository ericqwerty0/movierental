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
<script>
jQuery(function() {
	$('#submit').click(function() {
		var isValid = true;
		$("#error").hide();
		$("#javaError").hide();
		 $('input:text').each(function(){
		       if($.trim($(this).val()).length == 0){
		       $("#error").show();
		       isValid = false;
		       } 
		});
		 return isValid;
	});
})
</script>
<script>
jQuery(function(){
	var i=1;

     $("#add_row").click(function(){
    	 $('#addr'+i).html("<td>"+ (i+1) +"</td><td><input size = 25 name='name' type='text' placeholder='Name' class='form-control input-md' /></td><td><input size = 20  name='year' type='text' placeholder='(e.g:1900)'  class='form-control input-md' /></td><td><input size = 15 name='country' type='text' placeholder='Country'  class='form-control input-md' /></td><td><input size = 10 name='type' type='text' placeholder='Type'  class='form-control input-md' /></td><td><input size = 15 name='rent' type='text' placeholder='(e.g:1.00)'  class='form-control input-md' /></td><td><input size = 15 name='rating' type='text' placeholder='(e.g:6.0)'  class='form-control input-md' /></td>");

      $('#movieCreate').append('<tr id="addr'+(i+1)+'"></tr>');
      i++; 
  });
     $("#delete_row").click(function(){
         if(i>1){
         $("#addr"+(i-1)).html('');
         i--;
         }
     });
     })
     </script>
<title>Create New Product</title>
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

.error {
	padding: 15px;
	margin-bottom: 20px;
	border: 1px solid transparent;
	border-radius: 4px;
	color: #a94442;
	background-color: #f2dede;
	border-color: #ebccd1;
}
</style>
</head>

<body>
<div class="container">

    <div class = "div-right">  <p>Logged in user: ${username}<p>
               
              <form:form method="POST" action="/MovieRental/admin/logout">  
                    <input type="submit" value="Logout" class="btn btn-info "/>
                    </form:form></div>  
    <div class = "div-left"><h1>Product Create List</h1></div>

</div>
	
	<div class="main">
		<form:form method="POST" action="/MovieRental/admin/createMovie">



			<table id="movieCreate" class="table table-striped">
				<thead>
					<tr>
						<th class="text-center">#</th>
						<th class="text-center">Name</th>
						<th class="text-center">Publish Year</th>
						<th class="text-center">Country</th>
						<th class="text-center">Type</th>
						<th class="text-center">Rent</th>
						<th class="text-center">Rating</th>
					</tr>
				</thead>
				<tbody>
					<tr id='addr0'>
						<td>1</td>
						<td><input size=25 type="text" name='name' placeholder='Name'
							class='form-control input-md' /></td>
						<td><input size=20 type="text" name='year' id = 'year1'
							placeholder='(e.g:1900) ' class='form-control input-md' /></td>
						<td><input size=15 type="text" name='country'
							placeholder='Country' class='form-control input-md' /></td>
						<td><input size=10 type="text" name='type' placeholder='Type'
							class='form-control input-md' /></td>
						<td><input size=15 type="text" name='rent' placeholder='(e.g:1.00)' id = 'rent1'
							class='form-control input-md' /></td>
						<td><input size=15 type="text" name='rating'
							placeholder='(e.g:6.0)' id = 'rating1' class='form-control input-md' />
					</tr>
					<tr id='addr1'></tr>
				</tbody>
			</table>
<div id="error" style="display:none" class="error">Please fill in all the blanks.</div>
<c:if test="${not empty error}">
				<div id = "javaError" class="error">${error}</div>
			</c:if>
<h2>
			
			<input type="submit" name="create" value="Create"
				id ="submit" class="Center btn btn-info " />
			<input type="submit" name="cancel" value="Cancel"
				class="Center btn btn-info " />
				<a id="add_row" class="btn btn-info ">Add Row</a>
				<a id='delete_row' class=" btn btn-info">Delete Row</a>
				</h2>
		</form:form>
	</div>

</body>
</html>