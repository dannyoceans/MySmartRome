<?php

class MysqlClass
{
  // parametri per la connessione al database
  private $nomehost = "localhost";     
  private $nomeuser = "progettomagistrale";          
  private $password = ""; 
  private $nomedb = "my_progettomagistrale";
          
  // controllo sulle connessioni attive
  private $attiva = false;
 
// funzione per la connessione a MySQL
public function connetti()
 {
   if(!$this->attiva)
    {
     if($connessione = mysql_connect($this->nomehost,$this->nomeuser,$this->password) or die (mysql_error()))
      {
       // selezione del database
       $selezione = mysql_select_db($this->nomedb,$connessione) or die (mysql_error());
      }
     }else{
       return true;
     }
    }
    
    //funzione per l'esecuzione delle query 
public function query($sql)
 {
  if(isset($this->attiva))
  {
  $sql = mysql_query($sql) or die (mysql_error());
  return $sql;
  }else{
  return false; 
  }
 }

// funzione per la chiusura della connessione

public function disconnetti()
{
        if($this->attiva)
        {
                if(mysql_close())
                {
         $this->attiva = false; 
             return true; 
                }else{
                        return false; 
                }
        }
 }
public function login($username, $password)
{
 if(isset($this->attiva))
  {
   $query = "SELECT * FROM business WHERE email = '$username' AND password = '$password'";  
          $result = mysql_query($query);  
          if(mysql_num_rows($result) == 0) {  
              // $this->session_set(mysql_fetch_array($result));  
              return 1;
              
               mysql_close($this->connection); 
          } else {  
             
               mysql_close($this->connection); 
               
               session_start();
               while ($rows= mysql_fetch_array($result))  
 
   					{
 
         				$_SESSION['citta']=$rows['citta'];
                        $_SESSION['via']=$rows['via'];
                        $_SESSION['civico']=$rows['civico'];
 
   					}
               $_SESSION['username'] = $username;
                return 0;
          }  
          
     }

        }
 
 
} 

?>