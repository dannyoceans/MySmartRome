<?php
if(isset($_GET['invia'])){
	include 'config.php';
    $ch = curl_init("https://fcm.googleapis.com/fcm/send");

    //The device token.
    $token = '/topics/global'; //token here 

    //Title of the Notification.
    $title = str_replace(" ","__",$_GET["title"]);
    //$title = 'titolo';

    //Body of the Notification.
    $body = str_replace(" ","__",$_GET["body"]);
    //$body = 'body';
      session_start();
      $citta = str_replace(" ","__",$_SESSION["citta"]);
        $via = str_replace(" ","__",$_SESSION["via"]);
          $civico = str_replace(" ","__",$_SESSION["civico"]);
    //Creating the notification array.
    $data = array('title' =>$title, 'body' => $body, 'citta'=>$citta, 'via'=>$via,'civico'=>$civico);

    //This array contains, the token and the notification. The 'to' attribute stores the token.
    $arrayToSend = array('to' => $token, 'data' => $data);

    //Generating JSON encoded string form the above array.
    $json = json_encode($arrayToSend);
    //Setup headers:
    $headers = array();
    $headers[] = 'Content-Type: application/json';
    $headers[] = 'Authorization: key='.FIREBASE_API_KEY ; // key here

    //Setup curl, add headers and post parameters.
    curl_setopt($ch, CURLOPT_CUSTOMREQUEST, "POST");                                                                     
    curl_setopt($ch, CURLOPT_POSTFIELDS, $json);
    curl_setopt($ch, CURLOPT_HTTPHEADER,$headers);       

    //Send the request
    $response = curl_exec($ch);

    //Close request
    curl_close($ch);
    //echo $response;
    //echo '<br>';
    //echo $json;
}

?>