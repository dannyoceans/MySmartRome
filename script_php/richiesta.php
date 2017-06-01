<?php
/*ci colleghiamo al database(attenti perchè se lavorate in locale 
l'host è 10.0.2.2 e non 127.0.0.1)*/
   $username = $_GET['username'];
   $password = $_GET['password'];
 mysql_connect("localhost","progettomagistrale",""); 
//selezioniamo il db a cui ci vogliamo connettere
mysql_select_db("my_progettomagistrale");
//creamo al query
$sql=mysql_query("SELECT * FROM Utenti WHERE telefono=$username and password=$password"); 
/*Il metodo "mysql_fetch_assoc" restituisce un array in base alla query 
fatta e incrementa il dato*/
while($row=mysql_fetch_assoc($sql)) 
//inseriamo tutto nella variabile output
$output[]=$row;
/*stampiamo l'oggetto json, miraccomando a non stampare a video altri commenti, 
altrimenti quando andremo ad eseguire l'app android si bloccherà in quanto non 
riconoscerà i commenti come caratteri json*/
print(json_encode($output)); 

 
//chiudiamo la connessione
mysql_close();
?>
