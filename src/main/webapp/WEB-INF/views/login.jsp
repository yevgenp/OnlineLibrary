<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="sf" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<html>
<head>
	<title>Online Library</title>
	<link rel="stylesheet" type="text/css" href="<c:url value="/resources/style.css" />" >
	<link rel="shortcut icon" href="<c:url value="/resources/books.png"/>" >
</head>

<body onload='document.f.username.focus();'>
<div id="wrapper">

		<div id="header">
			<img src="/resources/library.jpg" style="width:100%;height:200px;">
		</div>
	<div id="nav">
		<ul>
			<li><a class="active" href="/home">Home</a></li>
			
			<li class="dropdown"><a class="dropbtn">Book operations</a>
				<div class="dropdown-content">
				
					<sec:authorize url="/addBook">
					<a href="/addBook">Add new</a>
					</sec:authorize> 
					
					<c:if test="${fn:startsWith(requestScope['javax.servlet.forward.request_uri'],'/books/')}">
					<sec:authorize url="/delete/**">					
					<a href=<c:url value="/delete/${book.id}"/>
					onclick="return confirm('Are you sure you want to delete this book?')">
					Delete</a>
					</sec:authorize>

					<sec:authorize access="isAuthenticated()">
					<a href=<c:url value="/download/${book.id}"/>>Download</a>
					</sec:authorize>
					
					<sec:authorize url="/edit/**">
					<a href=<c:url value="/edit/${book.id}"/>>Edit</a>
					</sec:authorize>
					</c:if>
				</div>
			</li>
			
			<sec:authorize access="isAuthenticated()">
			<sec:authentication property="principal.username" var="username" />
			<li class="dropdown" style="float:right">
				<a href=<c:url value="/users/${username}"/> class="dropbtn"><b>${username}</b></a>
				<div class="dropdown-content">
					<a href=<c:url value="/users/${username}"/>>Favorites</a>
					<a href="/logout">Logout</a>
				</div>
			</li>
			</sec:authorize>
			
			<sec:authorize access="isAnonymous()">
			<li class="dropdown" style="float:right">
				<a href="/login" class="dropbtn">Login/Register</a>
				<div class="dropdown-content">
					<a href="/login">Login</a>
					<a href="/register">Register</a>
				</div>
			</li>
			</sec:authorize>
		</ul>
	</div>

	<div id="content">
		<br><br>
		<sf:form name='f' action="/login" method='POST'>
   		<table>
    		<tr><td>User:</td><td>
        	<input type='text' name='username' value='' /></td></tr>
    		<tr><td>Password:</td>
        	<td><input type='password' name='password'/></td></tr>
    		<tr><td colspan='2'>
    		<input id="remember_me" name="remember-me" type="checkbox"/>
    		<label for="remember_me" class="inline">Remember me</label></td></tr>
    		<tr><td colspan='2'>
        	<input name="submit" type="submit" value="Login"/></td></tr>
   		</table>
 		 </sf:form>
	</div>
	
	<div id="footer">Copyright © YP</div>
</div>
</body>
</html>