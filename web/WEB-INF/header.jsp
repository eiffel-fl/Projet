<%-- 
    Document   : header.jsp
    Created on : 12 avr. 2015, 00:03:33
    Author     : francis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${sessionScope.pseudo}</title>
    </head>
    <body>
        <h1>Bonjour ${sessionScope.pseudo}</h1>
        <a href="monProfil">Mon profil</a>
        <form action="recherche" method="post">
            <input type="text" placeholder="Rechercher un document" name="toSearch"/>
            <label> par </label>
            <select name="type">
                <option value="Titre">Titre</option>
                <option value="Auteur">Auteur</option>
            </select> 
            <button type="submit">Rechercher</button>
        </form>
        <form action="Deconnexion">
            <button type="submit">Deconnexion</button>
        </form>


