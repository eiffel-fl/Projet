<%-- 
    Document   : recherche
    Created on : 12 avr. 2015, 23:11:05
    Author     : francis
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.loc}"/>
<fmt:setBundle basename="projet"/>
<jsp:include page="header.jsp"/>
<%--Utilisation de la servlet Recherche--%>
<table>
    <tr>
        <th><fmt:message key="titre"/></th>
        <th><fmt:message key="auteur"/></th>
        <th><fmt:message key="lecture"/></th>
        <th><fmt:message key="ecriture"/></th>
        <th><fmt:message key="nbparticipant"/></th>
    </tr>
    <c:forEach var="recherche" items="${lRecherche}">
        <tr>
            <td><a href="document?id=${recherche[0]}">${recherche[1]}</a></td>
            <td><a href="profil?profil=${recherche[2]}">${recherche[2]}</a></td>
            <td>${recherche[3]}</td>
            <td>${recherche[4]}</td>
            <td>${recherche[5]}</td>
        </tr>
    </c:forEach>
</table>
<jsp:include page="footer.jsp"/>
