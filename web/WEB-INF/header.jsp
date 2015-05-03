<%-- 
    Document   : header.jsp
    Created on : 12 avr. 2015, 00:03:33
    Author     : francis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>${sessionScope.pseudo}</title>
        <fmt:setLocale value="${sessionScope.loc}"/>
        <fmt:setBundle basename="projet"/>
    </head>
    <body>
        <h1><fmt:message key="bonjour"/> ${sessionScope.pseudo}</h1>
        <a href="monProfil"><fmt:message key="monprofil"/></a>
        <form action="recherche" method="post">
            <input type="text" placeholder="<fmt:message key="rechercher"/>" name="toSearch"/>
            <label> <fmt:message key="par"/> </label>
            <select name="type">
                <option value="Titre"><fmt:message key="titre"/></option>
                <option value="Auteur"><fmt:message key="auteur"/></option>
            </select> 
            <button type="submit"><fmt:message key="chercher"/></button>
        </form>
        <form action="Deconnexion">
            <button type="submit"><fmt:message key="deco"/></button>
        </form>


