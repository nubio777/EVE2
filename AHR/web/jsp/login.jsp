<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<!--------------------------------------------->
<!-- Login page to HR1 application -->
<!--------------------------------------------->

<html xmlns="http://www.w3.org/1999/xhtml">

<head>
    <!-- Metas y títulos nobiliarios-->
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Personeel Administration - Login</title>

    <!--Style Sheets -->
    <link rel="stylesheet" type="text/css" href="../css/COE.css"/>
    <link rel="stylesheet" type="text/css" href="../css/ahr.css"/>
    <script type="text/javascript" src="../js/jquery-1.11.3.min.js"></script>

    <!-- CSS particular -->
    <style type="text/css">

        .logo {
            float: left;
            width: 586px;
            height: 380px;
            background: url("../img/People.jpg");
            background-size: 586px 380px;
            background-repeat: no-repeat;
            margin: 120px 50px 0 0;
        }

        .forms {
            overflow: auto;
            margin: 100px 0 0 -150px;
        }

        .formularioLogin {
            margin-bottom: 80px;
        }

        .formularioRecover {
        }

        .formTitle {
            width: 350px;
            color: #962c84;
            /*color: #235369;*/
            padding-bottom: 10px;
            border-bottom: 1px solid;
            margin-bottom: 25px;
        }

        label {
            width: 120px !important;
            color: #4c6350;
        }

        input {
            width: 170px !important;
        }

        .cajaBoton {
            overflow: auto;
            padding: 30px 0 0 195px;
        }

        a.botonSencilloOff {
            padding: 7px 30px !important;
            background-color: #962c84;
        }

        .linea {
            margin-bottom: 10px;
        }
    </style>
</head>

<body>

<!--Center content -->
<div class="paginaCentrada">

    <!-- Titles -->
    <div class="titulos">
        <div class="tituloPpal">Human Resources</div>
        <div class="tituloSecundario">Personnel Administration</div>
    </div>

    <!--Imagen principal-->
    <div class="logo"></div>

    <!--Formularios-->
    <div class="forms">

        <!--Login form -->
        <div class="formTitle">Login</div>
        <div class="formularioLogin">

            <form id="loginForm" class="formulario">

                <div class="linea">
                    <label class="etiquetaALaIzda" for="user">User name</label><input type="text" id="user" name="user"/>
                </div>

                <div class="linea">
                    <label class="etiquetaALaIzda" for="passwd">Password</label> <input type="password" id="passwd" name="pwd"/>
                </div>

                <div class="cajaBoton">
                    <a id="loginSubmit" class="botonSencilloOff">Login</a>
                </div>
            </form>

        </div>

        <!--Recover form -->
        <div class="formTitle">Did you forget your user name or password?</div>
        <div class="formularioRecover">

            <form id="recoverForm" class="formulario">

                <div class="linea">
                    <label class="etiquetaALaIzda" for="email">Email</label> <input type="text" id="email" name="email"/>
                </div>

                <div class="cajaBoton">
                    <a id="recoverSubmit" class="botonSencilloOff">Send</a>
                </div>

            </form>

        </div>
    </div>

</div>
</body>

<script>
    $("#loginSubmit").click(function (e) {

        var url = "/ahr/login";

        $.ajax({
            type: "POST",
            url: url,
            data: $("#loginForm").serialize(), // serializes the form's elements.
            success: function (data) {
                if (data.trim().length == 0)
                    window.location.href = "/ahr/jsp/main.jsp";
                else
                    alert(data); // show error message.
            }
        });

        e.preventDefault(); // avoid to execute the actual submit of the form.
    });

    $("#recoverSubmit").click(function (e) {

        var url = "/ahr/recover";

        $.ajax({
            type: "POST",
            url: url,
            data: $("#recoverForm").serialize(), // serializes the form's elements.
            success: function (data) {
                alert(data); // show error message.
            }
        });

        e.preventDefault(); // avoid to execute the actual submit of the form.
    });
</script>
</html>
