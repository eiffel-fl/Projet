<%-- 
    Document   : monProfil
    Created on : 23 avr. 2015, 19:52:52
    Author     : francis
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.loc}"/>
<fmt:setBundle basename="projet"/>
<jsp:include page="header.jsp"/>
<h2><fmt:message key="bienvenuep"/></h2>
<h3><fmt:message key="amis"/></h3>
<%--Utilisation de la servlet MonProfil--%>
<ul>
    <c:forEach var="amis" items="${lAmis}">
        <li><a href="profil?profil=${amis}">${amis}</a></li>
    </c:forEach>
</ul>
<br>
<h3><fmt:message key="travaille"/></h3>
<%--Utilisation de la servlet MonProfil--%>
<ul>
    <c:forEach var="document" items="${lDocument}">
        <li><a href="document?id=${document[0]}">${document[1]}</a></li>
    </c:forEach>
</ul>
<br>
<h3><fmt:message key="demande"/></h3>
<%--Utilisation de la servlet GererDemandeAmis en GET pour l'affichage et POST pour la soumission du formulaire--%>
<ul>
    <c:forEach var="amis" items="${lDemandeAmis}">
        <li>
            <form action="gerer-demande" method="post">
                <a href="profil?profil=${amis}">${amis}</a>
                <input type="hidden" name="amis" value="${amis}"/>
                <input type="radio" name="choix" value="accepter" checked/> <fmt:message key="accepter"/>
                <input type="radio" name="choix" value="refuser"/> <fmt:message key="refuser"/>
                <input type="submit"/>
            </form>
        </li>
    </c:forEach>
</ul>
<h3><fmt:message key="fairedemande"/></h3>
<%--Utilisation de la servlet DemandeAmis--%>
<form action="demande-amis" method="post">
    <input type="text" name="amis" placeholder="<fmt:message key="fairedemandeplaceholder"/>"/>
    <input type="submit"/>
</form>
<br>
<h3><fmt:message key="creer"/></h3>
<%--Utilisation de la servlet CreerDocument--%>
<form action="creer-document" method="post">
    <input type="text" name="titre" placeholder="<fmt:message key="titre"/>"/>
    <label for="lecture"><fmt:message key="lecturem"/></label>
    <select name="lecture" id="lecture">
        <option value="Public"><fmt:message key="public"/></option>
        <option value="Amis"><fmt:message key="amis"/></option>
        <option value="Utilisateur"><fmt:message key="utilisateur"/></option>
    </select>
    <label for="ecriture"><fmt:message key="ecriturem"/></label>
    <select name="ecriture" id="ecriture">
        <option value="Public"><fmt:message key="public"/></option>
        <option value="Amis"><fmt:message key="amis"/></option>
        <option value="Utilisateur"><fmt:message key="utilisateur"/></option>
    </select>
    <input type="submit"/>
</form>
<jsp:include page="footer.jsp"/>
