<%-- 
    Document   : accueil
    Created on : 11 avr. 2015, 22:57:20
    Author     : francis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <fmt:setLocale value="${sessionScope.loc}"/>
        <fmt:setBundle basename="projet"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title><fmt:message key="accueil"/></title>
    </head>
    <body>
        <div>
            <h3><fmt:message key="connexion"/></h3>
            <form method="post" action="Connexion">
                <label for="pseudo"> <fmt:message key="pseudo"/> : </label> <input type="text" name ="pseudo" id="pseudo" placeholder="<fmt:message key='entrerpsd'/>" required/><br>
                <label for="password"> <fmt:message key="password"/> : </label> <input type="password" name="password" id ="password" placeholder="<fmt:message key='entrerpwd'/>" required/><br>
                <label for="conserver"> <fmt:message key="conserver"/> ? </label> <input type="checkbox" name="conserver" id="conserver"/><br>
                <button type="submit"><fmt:message key="connexion"/></button>
            </form>
        </div>
        <div>
            <h3><fmt:message key="inscription"/></h3>
            <form method="post" action="Inscription">
                <label for="pseudoinscription"> <fmt:message key="pseudo"/> : </label> <input type="text" name="pseudo" id="pseudoinscription" placeholder="<fmt:message key="entrerpsd"/>" required/><br>
                <label for="passwordinscription"> <fmt:message key="password"/> : </label> <input type="password" name="password" id ="passwordinscription"  placeholder="<fmt:message key="entrerpwd"/>" required/><br>
                <label for="password2inscription"> <fmt:message key="confirmer"/> : </label> <input type="password" name="password2" id ="password2inscription" placeholder="<fmt:message key="entrercfm"/>" required/><br>
                <label for="mailinscription"> Email </label> <input type="email" name="mail" id ="mailinscription" placeholder="<fmt:message key="entrermail"/>" required/><br>
                <label for="conserverinscription"> <fmt:message key="conserver"/> ? </label> <input type="checkbox" name="conserver" id="conserverinscription"/><br>
                <button type="submit"><fmt:message key="inscription"/></button>
            </form>
        </div>
    </body>
    <jsp:include page="footer.jsp"/>
