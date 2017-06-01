<?php
/*ci colleghiamo al database(attenti perchè se lavorate in locale 
l'host è 10.0.2.2 e non 127.0.0.1)*/
   $telefono = $_GET['telefono'];
   $latitudine = $_GET['latitudine'];
   $longitudine = $_GET['longitudine'];
 mysql_connect("localhost","progettomagistrale",""); 
//selezioniamo il db a cui ci vogliamo connettere
mysql_select_db("my_progettomagistrale");

//creamo al query
$sql="UPDATE Utenti SET latitudine = $latitudine , longitudine = $longitudine WHERE telefono = $telefono";
if(mysql_query($sql)){
    print(("OK"));
} else{
   print(("KO"));
}
 
// Close connection
mysql_close();
?>