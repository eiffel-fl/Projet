<%-- 
    Document   : footer
    Created on : 11 avr. 2015, 22:58:43
    Author     : francis
--%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<fmt:setLocale value="${sessionScope.loc}"/>
<fmt:setBundle basename="projet"/>
<footer>
    <p> <fmt:message key="site"/> : 
        <a href="mailto:anas.hakim.benhajar@etu.univ-st-etienne.fr">BENHAJAR Anas Hakim</a>
        <a href="mailto:anthony.deveaux@etu.univ-st-etienne.fr">DEVEAUX Anthony</a>
        <a href="mailto:guillaume.gras@etu.univ-st-etienne.fr">GRAS Guillaume</a>
        <a href="mailto:francis.laniel@etu.univ-st-etienne.fr">LANIEL Francis</a>
    </p>
</footer>
</body>
</html>
