<?php

$response = array();
if (isset($_POST['latitude']) && isset($_POST['longitude']) && isset($_POST['address']) && $_POST['nooflots']  && isset($_POST['time'])   && $_POST['user'])
 {
 
file_put_contents("insertfreelotsinput.txt", file_get_contents("php://input"));
 
    $latitude = $_POST['latitude'];
    $longitude = $_POST['longitude'] ;
    $time = $_POST['time'];
    $nooflots = $_POST['nooflots'];
    $username = strval($_POST['user']);
    $address = $_POST['address'];

$replaceFrom   = array("+", "%0A", "%2C");
$replaceTo  = array(' ', '', '');
    $replacedAddress = str_replace($replaceFrom, $replaceTo, $address);
    
    $tableName = "freeparkinglots";
 	
 	
$fp = fopen("testinsertfreeparkinglots.txt", "w") or die("Unable to open file!");

fwrite($fp, $latitude);
fwrite($fp, $longitude);
fwrite($fp, $time);
fwrite($fp, $nooflots);
fwrite($fp, $username);


   // Write $somecontent to our opened file. 
   fclose($fp); 


 	
 	
 	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 	
    // connecting to db
    $db = new DB_CONNECT();
 
  
          
          
          $query = "INSERT INTO $tableName (latitude, longitude, address,time, nooflots, user) VALUES ( $latitude, $longitude,  '$replacedAddress', $time, $nooflots, '$username');";

          
	$result = mysql_query($query) or die(mysql_error());

  
           if($result)
           {
           		
             $response["success"] = 1;
 		
              echo (json_encode($response));
        
           }// $result
           else {
    // required field is missing
                        $response["success"] = 0;
    					$response["message"] = "Error in update";// field missing
 
    // echoing JSON response
    echo json_encode($response);
}
     
            
       
} // checking post condition
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";// field missing
 
    // echoing JSON response
    echo json_encode($response);
}
?>