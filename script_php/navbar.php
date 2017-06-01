<nav class="navbar navbar-light" style="background-color: #ffe100;">
  <div class="container-fluid">
    <div class="navbar-header">
<h1 style="color:blue;font-weight:bold;margin-left:50px"> My smart Rome </h1>
    </div>

<?php
session_start();
if(!isset($_SESSION['username']))
{include 'form_login.php';}
else
{include 'logout.php';}

?>


    </div>
</nav>

