<%-- 
    Document   : document
    Created on : 20 avr. 2015, 18:22:01
    Author     : francis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="header.jsp"/>
<%--titre et fic sont "calculés" par Document.doGet()--%>
<c:if test="${param.erreur eq 'amis'}">
    <h1>ALERTE T4ES PAS AMIS </h1>
    <script type="text/javascript">
        alert("Ce document ne peut être écrit que par les amis de ${param.auteur} et vous n'êtes pas encore amis, n'hésitez pas à lui envoyer une demande d'amis !");
    </script>
</c:if>
<c:if test="${param.erreur eq 'utilisateur'}">
    <h1>ALERTE T4ES PAS UTIL </h1>
    <script type="text/javascript">
        alert("Ce document ne peut être écrit que par ${param.auteur}");
    </script>
</c:if>
<h1>${titre}</h1>
<form action='document' method='post'>
    <textarea name='textarea'>${fic}</textarea><br>    
    <input type='hidden' name='id' value="${id}"/>
    <input type='submit'/>
</form>
<div>
    <p>Les messages ici lol</p>
</div>
<form action='ServletMessage' method='post'>
    <input type="hidden" name="id" value="${param.id}"/>
    ${sessionScope.pseudo}<input type='text' name='message'/>
    <input type='submit'/>
</form>
<jsp:include page="footer.jsp"/>