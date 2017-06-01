<?php
if($_SERVER['REQUEST_METHOD']=='POST'){
 
 $image = $_POST['image'];
 $id=$_POST['telefono'];
 
 require_once('dbConnect.php');
 
 $sql ="SELECT id FROM photos ORDER BY id ASC";
 
 $res = mysqli_query($con,$sql);
 
 $path = "picture/$id.png";
 
 $actualpath = "http://progettomagistrale.altervista.org/$path";
 
 $sql = "INSERT INTO photos (image) VALUES ('$actualpath')";
 
 if(mysqli_query($con,$sql)){
 file_put_contents($path,base64_decode($image));
 echo "Successfully Uploaded";
 }
 
 mysqli_close($con);
 }else{
 echo "Error";
 }

 
 ?>