<?php


 
file_put_contents("outputfilespot.txt", file_get_contents("php://input"));
 
    $individualparkinglotid = 1;
    $fromTime = 100 + 0 ;
    $toTime = 200 + 0 ;
    
    
    $tableName = "parkingspots";
 	
 	/*echo $latitude;
 	echo $longitude;
 	echo $radius;
 	echo $fromTime;
 	echo $toTime;*/
 	
 
// Let's make sure the file exists and is writable first. 


   // In our example we're opening $filename in append mode. 
   // The file pointer is at the bottom of the file hence  
   // that's where $somecontent will go when we fwrite() it. 
   
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
           WHERE fk_parkinglotsid = $individualparkinglotid and
          ((UNIX_TIMESTAMP(fromtime) * 1000) NOT BETWEEN $fromTime and $toTime )
         and ((UNIX_TIMESTAMP(totime) * 1000) NOT BETWEEN $fromTime and $toTime );";
          
          
	$result = mysql_query($query) or die(mysql_error());

   if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
        
 		$response["parkingspots"] = array();
		while ($row = mysql_fetch_array($result)) 
		{
		
        // temp user array 
            $parkingspot = array();
            $parkingspot["parkingspotsid"] = $row["parkingspotsid"];
            $parkingspot["parkingspotname"] = $row["parkingspotname"];
            
            // push single product into final response array
        array_push($response["parkingspots"], $parkingspot);
            
              }
              echo (json_encode($response));
            // success
           /* array_push($response["parkinglots"], $parkinglot);
 }// While loop*/
            // echoing JSON response
            /*echo json_encode($output);*/
            
        } // rows>0
        else {
            // no product found
            
            $response["message"] = "No parkingspots found"; // >0
 
            // echo no users JSON
            echo json_encode($response);
        }
    }// empty result
     else {
        // no product found
        $response["message"] = "No parkingspots found"; // result empty
 
        // echo no users JSON
        echo json_encode($response);
    }

?>