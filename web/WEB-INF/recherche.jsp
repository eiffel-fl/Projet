<%-- 
    Document   : recherche
    Created on : 12 avr. 2015, 23:11:05
    Author     : francis
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<%--Utilisation de la servlet Recherche--%>
<table>
    <tr>
        <th>Titre</th>
        <th>Auteur</th>
        <th>Lecture</th>
        <th>Ecriture</th>
        <th>Nombre de participants</th>
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
