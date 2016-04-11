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

<body>
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
		<h4 align="center">
			<c:if test="${not empty bookid}">
				The <a href=<c:url value="/books/${bookid}"/>> book </a>	
			</c:if> <c:out value="${result}"/>
		</h4>
	</div>
	
	<div id="footer">Copyright © YP</div>
</div>
</body>
</html>