<!DOCTYPE html>

<html>

<head dir="ltr" lang="en">

    <!-- Metas
   ============================================= -->
    <meta charset="UTF-8">
    <meta name="robots" content="noindex, nofollow">
    <meta name="viewport" content="width=device-width,initial-scale=1,maximum-scale=1.0">


    <!-- Favicon and title
    ============================================= -->
    <title wicket:id="title">Dronsis</title>
    <link rel="icon" href="img/dronsis.ico" type="image/ico" sizes="16x16">


    <!-- Stylesheets
    ============================================= -->
    <!-- Bootstrap is included in its original form, unaltered -->
    <link rel="stylesheet" href="css/bootstrap.min.css">

    <!-- Related styles of various icon packs and plugins -->
    <link rel="stylesheet" href="css/plugins.css">

    <!-- The main stylesheet of this template. All Bootstrap overwrites are defined in here -->
    <link rel="stylesheet" href="css/main.css">

    <!-- The themes stylesheet of this template (for using specific theme color in individual elements - must included last) -->
    <link rel="stylesheet" href="css/themes.css">

    <!-- Some particular and irrelevant css -->
    <link rel="stylesheet" href="css/dronsis.css">


    <!-- JavaScripts
    ============================================= -->
    <!-- JQuery -->
    <script type="text/javascript" src="js/jquery-1.11.3.min.js"></script>

    <!-- Additionally, this page requires: -->
    <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script type="text/javascript" src="js/plugins.js"></script>
    <script type="text/javascript" src="js/app.js"></script>

    <script type="text/javascript" src="js/pages/readyComingSoon.js"></script>


</head>

<body>

<!-- Page Container
============================================= -->
<div id="page-container">


    <!-- Site Header
    ============================================= -->
    <header>
        <div class="container">

            <!-- Site Logo -->
            <a href="#" class="site-logo">
                <i class="gi gi-flash"></i> <strong>D</strong>ronsis
            </a>


            <!-- Main Menu -->
            <nav>
                <ul class="site-nav">

                    <li>
                        <a href="home">Home</a>
                    </li>

                    <li>
                        <a href="shop">Shop</a>
                    </li>

                    <li>
                        <a href="blog">Blog</a>
                    </li>

                    <li>
                        <a href="contact">Contact</a>
                    </li>

                    <li>
                        <a href="about">About</a>
                    </li>

                    <li>
                        <a href="admin">
                            <i class="gi gi-stopwatch" data-toggle="tooltip" data-placement="right" title="" data-original-title="Backend"></i>
                        </a>
                    </li>

                    <li>
                        <a href="login" class="btn btn-primary">Log In</a>
                    </li>

                    <li>
                        <a href="signup" class="btn btn-success">Sign Up</a>
                    </li>

                </ul>
            </nav>

        </div>
    </header>


    <!-- Intro
    ============================================= -->
    <!-- Intro -->
    <section class="site-section site-section-light site-section-top themed-background-dark">
        <div class="container">
            <h1 class="text-center animation-slideDown"><i class="fa fa-support"></i> <strong>Helpdesk</strong></h1>

            <h2 class="h3 text-center animation-slideUp push">How can we help you?</h2>

            <div class="text-center">
                <form action="helpForm" method="post" class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-8 col-md-offset-2">
                            <%--suppress XmlInvalidId --%>
                            <label class="sr-only" for="helpdesk-question">Search</label>

                            <div class="input-group input-group-lg">
                                <input type="text" id="helpdesk-question" name="question" class="form-control"
                                       placeholder="Have a question? Ask or enter a search term..">

                                <div class="input-group-btn">
                                    <button type="submit" value="submit" class="btn btn-primary"><i class="fa fa-search"></i> Search</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </section>


    <!-- Footer
  ============================================= -->
    <!-- Footer
 ============================================= -->
    <footer class="site-footer site-section">

        <div class="container">

            <!--Quick links-->
            <div class="row">
                <div class="col-sm-6 col-md-3">
                    <h4 class="footer-heading">Quick links</h4>
                    <ul class="footer-nav list-inline">
                        <li><a href="help">Help Desk</a></li>
                        <li><a href="javascript:void(0)">Link #2</a></li>
                        <li><a href="javascript:void(0)">Link #3</a></li>
                    </ul>
                </div>

                <!--Social-->
                <div class="col-sm-6 col-md-3">
                    <h4 class="footer-heading">Social</h4>
                    <ul class="footer-nav footer-nav-social list-inline">
                        <li><a href="javascript:void(0)"><i class="fa fa-facebook"></i></a></li>
                        <li><a href="javascript:void(0)"><i class="fa fa-twitter"></i></a></li>
                        <li><a href="javascript:void(0)"><i class="fa fa-google-plus"></i></a></li>
                        <li><a href="javascript:void(0)"><i class="fa fa-dribbble"></i></a></li>
                        <li><a href="javascript:void(0)"><i class="fa fa-rss"></i></a></li>
                    </ul>
                </div>

                <!--With love-->
                <div class="col-sm-6 col-md-3">
                    <h4 class="footer-heading">2016 &copy; <a href="#">Dronsis, Inc.</a></h4>
                    <ul class="footer-nav list-inline">
                        <li>Crafted with <i class="fa fa-heart text-danger"></i> by <a href="http://goo.gl/vNS3I">Dronsis</a></li>
                    </ul>
                </div>

            </div>
        </div>

    </footer>

</div>

</body>


</html>


