<?php
/*ci colleghiamo al database(attenti perchè se lavorate in locale 
l'host è 10.0.2.2 e non 127.0.0.1)*/
   $telefono = $_GET['telefono'];
   $latitudine = $_GET['latitudine'];
   $longitudine = $_GET['longitudine'];
   $data=$_GET['data'];
 mysql_connect("localhost","progettomagistrale",""); 
//selezioniamo il db a cui ci vogliamo connettere
mysql_select_db("my_progettomagistrale");

//creamo al query
$sql="INSERT INTO posizioni( longitudine, latitudine, id_utente, data) VALUES ($longitudine,$latitudine,$telefono,$data)";
if(mysql_query($sql)){
    print(("OK"));
} else{
   print(("KO"));
      echo $telefono = $_GET['telefono'];
   echo $latitudine = $_GET['latitudine'];
   echo $longitudine = $_GET['longitudine'];
  echo  $data=$_GET['data'];
}
 
// Close connection
mysql_close();
?>