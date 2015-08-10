<?php

$response = array();
if (isset($_POST['latitude']) && isset($_POST['longitude']))
 {
 
file_put_contents("selectfreelotsinput.txt", file_get_contents("php://input"));
 
  $origLat = doubleval($_POST['latitude']);
  $origLon = doubleval($_POST['longitude']);
  $dist = 45;
   
    
    $tableName = "freeparkinglots";
 	  
$fp = fopen("testinsertfreeparkinglots.txt", "w") or die("Unable to open file!");

fwrite($fp, $origLon);
fwrite($fp, $origLon);


   fclose($fp); 	
 	
 	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 	
    // connecting to db
    $db = new DB_CONNECT();
 
  
           $query = "SELECT latitude, longitude, address, nooflots,time,user,3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as miles FROM freeparkinglots WHERE 
          longitude between ($origLon-$dist/abs(cos(radians($origLat))*69)) 
          and ($origLon+$dist/abs(cos(radians($origLat))*69)) 
          and latitude between ($origLat-($dist/69)) 
          and ($origLat+($dist/69)) having miles < $dist ORDER BY time DESC limit 100;";
          
          	$result = mysql_query($query) or die(mysql_error());

	if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
                    $response["success"] = 1;
                    
 		$response["freeparkinglots"] = array();
		while ($row = mysql_fetch_array($result)) 
		{
		
        // temp user array 
            $freeparkinglot = array();
            $freeparkinglot["latitude"] = $row["latitude"];
            $freeparkinglot["longitude"] = $row["longitude"];
            $freeparkinglot["nooflots"] = $row["nooflots"];
		    $freeparkinglot["miles"] = $row["miles"];
            $freeparkinglot["time"] = $row["time"];
		    $freeparkinglot["user"] = $row["user"];
		    $freeparkinglot["address"] = $row["address"];
           
        array_push($response["freeparkinglots"], $freeparkinglot);
            
              }
              echo (json_encode($response));
            
            
        } // rows>0
        else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No free parkinglots found"; // >0
 
            // echo no users JSON
            echo json_encode($response);
        }
    }// empty result
     else {
        // no product found
         $response["success"] = 0;
        $response["message"] = "No free parkinglots found"; // result empty
 
        // echo no users JSON
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