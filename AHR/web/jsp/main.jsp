<%@ page import="coe.ahr.User" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!--------------------------------------------->
<!-- Main page in AHR  application           -->
<!--------------------------------------------->

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <!-- Metas -->
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Personeel Administration - Login</title>

    <!--Style Sheets -->
    <link rel="stylesheet" type="text/css" href="../css/COE.css"/>
    <link rel="stylesheet" type="text/css" href="../css/ahr.css"/>

    <!-- CSS -->
    <style type="text/css">
        .logo {
            float: left;
            width: 586px;
            height: 380px;
            background: url("../img/People.jpg");
            background-size: 586px 380px;
            background-repeat: no-repeat;
            margin: 0 50px 0 0;
        }

        .contenidoPag {
            margin: 200px 0 0 200px;
            font-size: 18px;
            color: #843425;
        }

    </style>
</head>

<body>

<%
    Object userName = session.getAttribute("userName") ;
    boolean isAdmin = userName != null && User.admin.getName().equals(userName.toString()) ;

    final String MSG_USER_NORMAL = "Sorry, but you are not allowed to view the secret report.<br>Please, log in as an administrator to view it...";
    final String MSG_ADMIN = "Congratulations!!. This is the secret message: Licor 43";
    String msg = isAdmin ? MSG_ADMIN : MSG_USER_NORMAL ;
%>


<!--Center content -->
<div class="paginaCentrada">

    <!-- Titles -->
    <div class="titulos">
        <div class="tituloPpal">Human Resources</div>
        <div class="tituloSecundario">Personnel Administration</div>
    </div>

    <!--Imagen principal-->
    <div class="logo"></div>

    <div class="contenidoPag">
        <%= msg %>
    </div>

</div>

</body>
</html>
