<%-- 
    Document   : monProfil
    Created on : 23 avr. 2015, 19:52:52
    Author     : francis
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<h2>Bienvenue sur votre profil</h2>
<h3>Mes amis</h3>
<%--Utilisation de la servlet MonProfil--%>
<ul>
    <c:forEach var="amis" items="${lAmis}">
        <li><a href="profil?profil=${amis}">${amis}</a></li>
    </c:forEach>
</ul>
<br>
<h3>Travaille sur</h3>
<%--Utilisation de la servlet MonProfil--%>
<ul>
    <c:forEach var="document" items="${lDocument}">
        <li><a href="document?id=${document[0]}">${document[1]}</a></li>
    </c:forEach>
</ul>
<br>
<h3>Demande d'amis</h3>
<%--Utilisation de la servlet GererDemandeAmis en GET pour l'affichage et POST pour la soumission du formulaire--%>
<ul>
    <c:forEach var="amis" items="${lDemandeAmis}">
        <li>
            <form action="gerer-demande" method="post">
                <a href="profil?profil=${amis}">${amis}</a>
                <input type="hidden" name="amis" value="${amis}"/>
                <input type="radio" name="choix" value="accepter" checked/> Accepter
                <input type="radio" name="choix" value="refuser"/> Refuser
                <input type="submit"/>
            </form>
        </li>
    </c:forEach>
</ul>
<h3>Faire une demande d'ami</h3>
<%--Utilisation de la servlet DemandeAmis--%>
<form action="demande-amis" method="post">
    <input type="text" name="amis" placeholder="Pseudo de votre potentiel futur ami"/>
    <input type="submit"/>
</form>
<br>
<h3>Creer un document</h3>
<%--Utilisation de la servlet CreerDocument--%>
<form action="creer-document" method="post">
    <input type="text" name="titre" placeholder="Titre du document"/>
    <label for="lecture">Mode de lecture</label>
    <select name="lecture" id="lecture">
        <option value="Public">Public</option>
        <option value="Amis">Amis</option>
        <option value="Utilisateur">Utilisateur</option>
    </select>
    <label for="ecriture">Mode d'écriture</label>
    <select name="ecriture" id="ecriture">
        <option value="Public">Public</option>
        <option value="Amis">Amis</option>
        <option value="Utilisateur">Utilisateur</option>
    </select>
    <input type="submit"/>
</form>
<jsp:include page="footer.jsp"/>
