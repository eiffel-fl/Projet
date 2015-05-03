<%-- 
    Document   : profil
    Created on : 12 avr. 2015, 00:01:39
    Author     : francis
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.loc}"/>
<fmt:setBundle basename="projet"/>
<jsp:include page="header.jsp"/>
<h2><fmt:message key="bienvenue"/> ${pseudo}</h2>
<h3><fmt:message key="amis"/></h3>
<%--Utilisation de la servlet Profil--%>
<ul>
    <c:forEach var="amis" items="${lAmis}">
        <li><a href="profil?profil=${amis}">${amis}</a></li>
    </c:forEach>
</ul>
<br>
<h3><fmt:message key="travaille"/></h3>
<%--Utilisation de la servlet Profil--%>
<ul>
    <c:forEach var="document" items="${lDocument}">
        <li><a href="document?id=${document[0]}">${document[1]}</a></li>
    </c:forEach>
</ul>
<jsp:include page="footer.jsp"/>

