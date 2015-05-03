<%-- 
    Document   : document
    Created on : 20 avr. 2015, 18:22:01
    Author     : francis
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.loc}"/>
<fmt:setBundle basename="projet"/>
<jsp:include page="header.jsp"/>
<%--titre et fic sont "calculés" par Document.doGet()--%>
<c:if test="${param.erreur eq 'amis'}">
    <script type="text/javascript">
        alert("Ce document ne peut être écrit que par les amis de ${param.auteur} et vous n'êtes pas encore amis, n'hésitez pas à lui envoyer une demande d'amis !");
    </script>
</c:if>
<c:if test="${param.erreur eq 'utilisateur'}">
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
<h3>Chat</h3>
<div style="overflow-y: scroll; height:200px;">
    <c:forEach var="message" items="${lMessage}">
        <p>${message}</p>
    </c:forEach>
</div>
<form action='ServletMessage' method='post'>
    <input type="hidden" name="id" value="${param.id}"/>
    ${sessionScope.pseudo}<input type='text' placeholder="<fmt:message key="message"/>" name='message'/>
    <input type='submit'/>
</form>
<h3><fmt:message key="travailleur"/></h3>
<div>
    <ul>
    <c:forEach var="travailleur" items="${lTravailleur}">
        <li><a href="profil?profil=${travailleur}">${travailleur}</a></li>
    </c:forEach>
</ul>
</div>
<form action="Telecharger" method="post">
    <input type="hidden" name="id" value="${param.id}"/>
    <button type="submit"><fmt:message key="telecharger"/></button>
</form>
<jsp:include page="footer.jsp"/>