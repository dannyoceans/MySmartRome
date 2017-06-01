<!DOCTYPE html>
<html lang="en">
<head>
 <!-- Latest compiled and minified CSS -->
<link rel="stylesheet" href="bootstrap-flatly.css" >

    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<?php
if(!isset($_GET['submit']))
{?>
<div class="row">
    <div class="col-md-3"></div>
    <div class="col-md-6">

        <div class="well">
<form class="form-horizontal" action="#" method="GET">
  <fieldset>
    <legend>Login MySmartRome Business</legend>
    <div class="form-group">
      <label for="inputEmail" class="col-lg-2 control-label">Email</label>
      <div class="col-lg-10">
        <input type="text" class="form-control" id="inputEmail" placeholder="Email" name="username">
      </div>
    </div>
    <div class="form-group">
      <label for="inputPassword" class="col-lg-2 control-label">Password</label>
      <div class="col-lg-10">
        <input type="password" class="form-control" id="inputPassword" placeholder="Password" name="password">

      </div>
    </div>

 
    <div class="form-group">
      <div class="col-lg-10 col-lg-offset-2">
        <button type="reset" class="btn btn-default">Cancel</button>
        <button type="submit" class="btn btn-primary" name="submit">Submit</button>
      </div>
    </div>
  </fieldset>
</form>
    </div>
    </div>
    <div class="col-md-3"></div>
</div>

<?php
}
if(isset($_GET['submit']))
{
   $username = $_GET['username'];
   $password = $_GET['password'];
 mysql_connect("localhost","progettomagistrale",""); 
//selezioniamo il db a cui ci vogliamo connettere
mysql_select_db("my_progettomagistrale");
//creamo al query
$sql=mysql_query("SELECT * FROM Utenti WHERE telefono='$username' and password='$password'"); 
/*Il metodo "mysql_fetch_assoc" restituisce un array in base alla query 
fatta e incrementa il dato*/
while($row=mysql_fetch_assoc($sql)) 
//inseriamo tutto nella variabile output
$output[]=$row;
/*stampiamo l'oggetto json, miraccomando a non stampare a video altri commenti, 
altrimenti quando andremo ad eseguire l'app android si bloccherà in quanto non 
riconoscerà i commenti come caratteri json*/
$json=json_encode($output[0]);
print($json);

//Decodifichiamo il json e lo associamo ad un array (True)
$json_o=json_decode($json,true);

$log=1;
if(json_encode($output)=='null'){
echo "spiacenti utente inesistente";
$log=0;
}
if($log==1)
{
?>

<div class="row">
    <div class="col-md-3"></div>
    <div class="col-md-6">

        <div class="well">
<form class="form-horizontal" action="http://progettomagistrale.altervista.org/index3.php" method="GET">
  <fieldset>
    <legend>Login MySmartRome Business invia una notifica</legend>
    
<div class="form-group">
 <label for="inputEmail" class="col-lg-2 control-label">Titolo</label>
      <div class="col-lg-10">
        <input type="text" class="form-control" id="inputEmail" placeholder="titolo" name="title">
      </div>
      <label for="textArea" class="col-lg-2 control-label" name="body">Textarea</label>
     
      <div class="col-lg-10">
        <textarea class="form-control" rows="3" id="textArea" name="body"></textarea>
	
        <input  type="hidden" name="lat" value="<?php echo $json_o['latitudine']; ?>" >
        <input  type="hidden" name="lng" value="<?php echo $json_o[longitudine]; ?>" > 
        
      </div>
 </div>

 
    <div class="form-group">
      <div class="col-lg-10 col-lg-offset-2">
        <button type="reset" class="btn btn-default">Cancel</button>
        <button type="submit" class="btn btn-primary">invia notifiche</button>
      </div>
    </div>
  </fieldset>
</form>
    </div>
    </div>
    <div class="col-md-3"></div>
</div>
 <?php
}
//chiudiamo la connessione
mysql_close();
}



?>
 

    
</body>
</html>