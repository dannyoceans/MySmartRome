<!DOCTYPE html>
<html lang="en">
<head>

<!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">

<!-- Optional theme -->
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" integrity="sha384-rHyoN1iRsVXV4nD0JutlnGaslCJuC7uwjduW9SVrLvRYooPp2bWYgmgJQIXwl/Sp" crossorigin="anonymous">

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>

<!-- Latest compiled and minified JavaScript -->
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>



<?php
 session_start();
 include 'database.php' ;
 include 'index3.php';
 include 'navbar.php' ;

if(isset($_POST['login']))
{ 
  $user=$_POST['user'];
  $pswd=$_POST['pswd'];
  $data = new MysqlClass();
  $data->connetti();
	$log=$data->login($user,$pswd);
    echo $log;
   
    if($log==0)
    {


      //$data->query("SELECT * FROM business WHERE email = '$user' AND password = '$pswd' ");
     // while ($row = $data->fetch_array())
        echo "Benvenuto2 ";
        echo $_SESSION['username'];
        header("location:{$_SERVER['PHP_SELF']}");


    }
   

}
if(isset($_POST['logout']))
{unset($_SESSION['username']);
header("location:{$_SERVER['PHP_SELF']}");
}

if(!isset($_SESSION['username'])){
?>
<div class="container ">
 <div class="well bs-component">
<div class="row">
  
<div class="col-lg-6" style="height:100%">
<?php
include 'left.php';
?>
</div>
<div class="col-lg-6">
<?php
include 'form.php' ;
?>
</div>
</div>
</div>
</div>
<?php
}else{

  include 'invio.php';
}

if(isset($_POST['registration']) )
{   
    $email=$_POST['email'];
    $pswd=$_POST['pswd'];
    $citta=$_POST['citta'];
    $via=$_POST['via'];
    $civico=$_POST['civico'];
    $data = new MysqlClass();
    $data->connetti();
    $data->query("INSERT INTO business(id, email, password, citta, via, civico) VALUES ('0','$email','$pswd','$citta','$via','$civico')");
    $message = "Registration succesfully! Thanks";
echo "<script type='text/javascript'>alert('$message');</script>";
   

}
 
 
 ?>


</body>
</html>