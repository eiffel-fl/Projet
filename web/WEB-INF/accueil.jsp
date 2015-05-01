<%-- 
    Document   : accueil
    Created on : 11 avr. 2015, 22:57:20
    Author     : francis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Accueil</title>
    </head>
    <body>
        <div>
            <h3>Connexion</h3>
            <form method="post" action="Connexion">
                <label for="pseudo"> Pseudo : </label> <input type="text" name ="pseudo" id="pseudo" placeholder="Entrez votre pseudo" required/><br>
                <label for="password"> Mot de passe : </label> <input type="password" name="password" id ="password" placeholder="Entrez votre mot de passe" required/><br>
                <label for="conserver"> Conserver mes identifiants ? </label> <input type="checkbox" name="conserver" id="conserver"/><br>
                <button type="submit">Connexion</button>
            </form>
        </div>
        <div>
            <h3>Inscription</h3>
            <form method="post" action="Inscription">
                <label for="pseudoinscription"> Pseudo : </label> <input type="text" name="pseudo" id="pseudoinscription" placeholder="Entrez votre pseudo" required/><br>
                <label for="passwordinscription"> Mot de passe : </label> <input type="password" name="password" id ="passwordinscription"  placeholder="Entrez votre mot de passe" required/><br>
                <label for="password2inscription"> Confirmation du mot de passe : </label> <input type="password" name="password2" id ="password2inscription" placeholder="Confirmez votre mot de passe" required/><br>
                <label for="mailinscription"> Adresse mail </label> <input type="email" name="mail" id ="mailinscription" placeholder="Entrez votre adresse mail" required/><br>
                <label for="conserverinscription"> Conserver mes identifiants ? </label> <input type="checkbox" name="conserver" id="conserverinscription"/><br>
                <button type="submit">Inscription</button>
            </form>
        </div>
    </body>
    <jsp:include page="footer.jsp"/>
