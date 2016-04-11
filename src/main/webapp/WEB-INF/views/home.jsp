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
		<br>
		<br>
		<sf:form commandName="book" method="POST"> 
		Author <sf:input type="text" path="author" />&nbsp;&nbsp;
		Genre <sf:input type="text" path="genre" />&nbsp;&nbsp;
		Title <sf:input type="text" path="title" />&nbsp;&nbsp;
		<input type="submit" value="Filter" />
		</sf:form>
		<br>
		<br>

		<table class="books">
			<tr class="books">
				<th class="books">Author</th>
				<th class="books">Genre</th>
				<th class="books">Title</th>
			</tr>
			<c:forEach items="${bookslist}" var="item">
				<tr class="books">
					<td class="books"><c:out value="${item.author}" /></td>
					<td class="books"><c:out value="${item.genre}" /></td>
					<td class="books"><a href=<c:url value="/books/${item.id}"/>>
							<c:out value="${item.title}" />
					</a></td>
					<sec:authorize access="isAuthenticated()">
						<td><a
							href=<c:url value="/favorites/?action=add&bookid=${item.id}"/>
							onclick="toFavorites(this);return false;"> <img id="fav"
								src="/resources/favoritesOff.png" title="Add to favorites"
								style="width: 24px; height: 24px; border: 0;">
						</a></td>
					</sec:authorize>
				</tr>
			</c:forEach>
		</table>
	</div>

		<script type="text/javascript">
			function toFavorites(event) {

				var url = event.href;
				event.firstElementChild.src = '/resources/favoritesOn.png';
				xmlHttp = new XMLHttpRequest();
				xmlHttp.open("GET", url, true);
				xmlHttp.send();
			}
		</script>

		<div id="footer">Copyright © YP</div>
</div>
</body>
</html>