<?php

$response = array();
if (isset($_POST['latitude']) && isset($_POST['longitude']) && isset($_POST['radius']) 
	&& isset($_POST['fromtime']) && isset($_POST['endtime'])
	&& isset($_POST['zipcode'])  && isset($_POST['isradius']))
 {
 
file_put_contents("outputfile.txt", file_get_contents("php://input"));
 
    $origLat = doubleval($_POST['latitude']);
    $origLon = doubleval($_POST['longitude']);
    $dist = doubleval($_POST['radius']);
    $isradius = strtolower($_POST['isradius']);
    $zipcode = $_POST['zipcode'];
    $fromTime = $_POST['fromtime'] + 0 ;
    $toTime = $_POST['endtime'] + 0 ;
    
    $tableName = "parkinglots";
    $tableName2 = "parkingspots";
 	
 	
 	$filename = 'test.txt'; 
$line1 = "Add this line1 to the file\n"; 
$line2 = "Add this line2 to the file\n"; 


   
$fp = fopen("test.txt", "w") or die("Unable to open file!");
fwrite($fp, $origLat);
fwrite($fp, $origLon);
fwrite($fp, $dist);
fwrite($fp, $fromTime);
fwrite($fp, $toTime);
   // Write $somecontent to our opened file. 
   fclose($fp); 


 
    require_once __DIR__ . '/db_connect.php';
 	
    $db = new DB_CONNECT();
 
          
          if ($isradius == 'true')
          {
          $query = "SELECT  distinct  parkinglotsid,address, cost,3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as miles, latitude, longitude FROM $tableName 
          inner join  $tableName2  ON parkinglotsid = fk_parkinglotsid
           WHERE 
          longitude between ($origLon-$dist/abs(cos(radians($origLat))*69)) 
          and ($origLon+$dist/abs(cos(radians($origLat))*69)) 
          and latitude between ($origLat-($dist/69)) 
          and ($origLat+($dist/69)) 
          and ($fromTime  NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000))
         and ($toTime  NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000))
         and ((UNIX_TIMESTAMP(fromtime) * 1000) NOT BETWEEN $fromTime AND $toTime )
         AND ((UNIX_TIMESTAMP(totime) * 1000) NOT BETWEEN $fromTime AND $toTime )
         having miles < $dist ORDER BY miles limit 100;";
         }
         else
         {
          $query = "SELECT  distinct  parkinglotsid,address, cost,3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as miles, latitude, longitude FROM $tableName 
          inner join  $tableName2  ON parkinglotsid = fk_parkinglotsid
           WHERE
            zipcode = $zipcode
          and ($fromTime  NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000))
         and ($toTime  NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000))
         and ((UNIX_TIMESTAMP(fromtime) * 1000) NOT BETWEEN $fromTime AND $toTime )
         AND ((UNIX_TIMESTAMP(totime) * 1000) NOT BETWEEN $fromTime AND $toTime )
          ORDER BY miles limit 100;";
         }
         
          
          
	$result = mysql_query($query) or die(mysql_error());

   if (!empty($result)) {
       
        if (mysql_num_rows($result) > 0) {
                    $response["success"] = 1;
                    
 		$response["parkinglots"] = array();
		while ($row = mysql_fetch_array($result)) 
		{
		
        
            $parkinglot = array();
            $parkinglot["parkinglotsid"] = $row["parkinglotsid"];
            $parkinglot["address"] = $row["address"];
            $parkinglot["miles"] = $row["miles"];
            $parkinglot["cost"] = $row["cost"];
            $parkinglot["latitude"] = $row["latitude"];
            $parkinglot["longitude"] = $row["longitude"];
            
        array_push($response["parkinglots"], $parkinglot);
            
              }
              echo (json_encode($response));
           
            
        } // rows>0
        else {
            // no product found
            $response["success"] = 0;
            $response["message"] = "No parkinglots found"; // >0
 
            // echo no users JSON
            echo json_encode($response);
        }
    }// empty result
     else {
       
         $response["success"] = 0;
        $response["message"] = "No parkinglots found"; // result empty
 
        // echo no users JSON
        echo json_encode($response);
    }
} 
else {
    // required field is missing
    $response["success"] = 0;
    $response["message"] = "Required field(s) is missing";// field missing
 
    // echoing JSON response
    echo json_encode($response);
}
?>