<?php

$response = array();
if (isset($_POST['individualparkinglotid']) && isset($_POST['fromtime']) && isset($_POST['endtime']))
 {
 
file_put_contents("outputfilespot.txt", file_get_contents("php://input"));
 
    $individualparkinglotid = $_POST['individualparkinglotid'];
    $fromTime = $_POST['fromtime'] + 0 ;
    $toTime = $_POST['endtime'] + 0 ;
    
    
    $tableName = "parkingspots";
 	
   
$fp = fopen("testspot.txt", "w") or die("Unable to open file!");

fwrite($fp, $individualparkinglotid);
fwrite($fp, $fromTime);
fwrite($fp, $toTime);
   // Write $somecontent to our opened file. 
   fclose($fp); 


 	
 	
 	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 	
    // connecting to db
    $db = new DB_CONNECT();
 
  
          
          
          $query = "SELECT  distinct  parkingspotsid, parkingspotname
          FROM $tableName 
           WHERE fk_parkinglotsid = $individualparkinglotid 
         and ($fromTime  NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000))
         and ($toTime  NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000)
         and ((UNIX_TIMESTAMP(fromtime) * 1000) NOT BETWEEN $fromTime AND $toTime )
         AND ((UNIX_TIMESTAMP(totime) * 1000) NOT BETWEEN $fromTime AND $toTime ));";
          
          
	$result = mysql_query($query) or die(mysql_error());

   if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
                    $response["success"] = 1;
 		$response["parkingspots"] = array();
		while ($row = mysql_fetch_array($result)) 
		{
		
        // temp user array 
            $parkingspot = array();
            $parkingspot["parkingspotsid"] = $row["parkingspotsid"];
            $parkingspot["parkingspotname"] = $row["parkingspotname"];
            

        array_push($response["parkingspots"], $parkingspot);
            
              }
              echo (json_encode($response));
            
            
        } // rows>0
        else {
            
            $response["success"] = 0;
            $response["message"] = "No parkingspots found"; // >0
 
            // echo no users JSON
            echo json_encode($response);
        }
    }// empty result
     else {
        // no product found
                            $response["success"] = 0;
        $response["message"] = "No parkingspots found"; // result empty
 
        // echo no users JSON
        echo json_encode($response);
    }
} // checking post condition
else {
    
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";// field missing
 
    // echoing JSON response
    echo json_encode($response);
}
?>