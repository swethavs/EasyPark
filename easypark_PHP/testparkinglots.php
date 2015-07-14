<?php

$response = array();

 

 
    $origLat = 45.5235948;
    $origLon = -122.888965;
    $dist = 2;
    $fromTime = 1436765460000 + 0 ;
    $toTime = 1436765880000 + 0 ;
    
   $mil = 1227643821310;
$fromseconds = $fromTime / 1000;
$fromDate = date("Y-m-d H:i:s", $fromseconds);
echo $fromDate;
echo 'strottime';
echo strtotime($fromDate);
$toseconds = $fromTime / 1000;
$toDate =  date("Y-m-d H:i:s", $toseconds);

$time = strtotime($fromDate.'utc');
$dateInLocal = date("Y-m-d H:i:s", $time);
echo 'utc';
    echo $dateInLocal;
    $fromquery = "select from_unixtime ($fromTime) as fromTime;";
    $result = mysql_query($fromquery) or die(mysql_error());
   /* while ($row = mysql_fetch_array($result))
    {
    echo 'hi';
   	echo $row["fromTime"];
   	}*/
    
    $tableName = "parkinglots";
    $tableName2 = "parkingspots";
 	
 	/*echo $latitude;
 	echo $longitude;
 	echo $radius;
 	echo $fromTime;
 	echo $toTime;*/
 	
 
 	
 	$filename = 'test.txt'; 
$line1 = "Add this line1 to the file\n"; 
$line2 = "Add this line2 to the file\n"; 

// Let's make sure the file exists and is writable first. 


   // In our example we're opening $filename in append mode. 
   // The file pointer is at the bottom of the file hence  
   // that's where $somecontent will go when we fwrite() it. 
   
$fp = fopen("test.txt", "w") or die("Unable to open file!");
fwrite($fp, $origLat);
fwrite($fp, $origLon);
fwrite($fp, $dist);
fwrite($fp, $fromTime);
fwrite($fp, $toTime);
   // Write $somecontent to our opened file. 
   fclose($fp); 


 	
 	
 	
    // include db connect class
    require_once __DIR__ . '/db_connect.php';
 	
    // connecting to db
    $db = new DB_CONNECT();
 
   /* $query = "SELECT  distinct parkinglotsid,address, cost,3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as miles FROM $tableName 
          inner join  $tableName2  ON $tableName.parkinglotsid = $tableName2.fk_parkinglotsid
           WHERE 
          longitude between ($origLon-$dist/abs(cos(radians($origLat))*69)) 
          and ($origLon+$dist/abs(cos(radians($origLat))*69)) 
          and latitude between ($origLat-($dist/69)) 
          and ($origLat+($dist/69)) and ((UNIX_TIMESTAMP(fromtime) * 1000) NOT BETWEEN $fromTime and $toTime )
          and ((UNIX_TIMESTAMP(totime) * 1000) NOT BETWEEN $fromTime and $toTime )
          having miles < $dist ORDER BY miles limit 100;"; */
          
          
          $query = "SELECT  distinct  parkinglotsid,address, cost,3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as miles FROM $tableName 
          inner join  $tableName2  ON parkinglotsid = fk_parkinglotsid
           WHERE 
          longitude between ($origLon-$dist/abs(cos(radians($origLat))*69)) 
          and ($origLon+$dist/abs(cos(radians($origLat))*69)) 
          and latitude between ($origLat-($dist/69)) 
          and ($origLat+($dist/69)) and ($fromTime  NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000))
          and ($toTime NOT BETWEEN (UNIX_TIMESTAMP(fromtime) * 1000) and (UNIX_TIMESTAMP(totime) * 1000))
          and parkinglotsid = 1
          having miles < $dist ORDER BY miles limit 100;";
          
          
	$result = mysql_query($query) or die(mysql_error());

   if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
                    $response["success"] = 1;
                    
 		$response["parkinglots"] = array();
		while ($row = mysql_fetch_array($result)) 
		{
		
        // temp user array 
            $parkinglot = array();
            $parkinglot["parkinglotsid"] = $row["parkinglotsid"];
            $parkinglot["address"] = $row["address"];
            $parkinglot["miles"] = $row["miles"];
            $parkinglot["cost"] = $row["cost"];
            // push single product into final response array
        array_push($response["parkinglots"], $parkinglot);
            
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
            $response["success"] = 0;
            $response["message"] = "No parkinglots found"; // >0
 
            // echo no users JSON
            echo json_encode($response);
        }
    }// empty result
     else {
        // no product found
         $response["success"] = 0;
        $response["message"] = "No parkinglots found"; // result empty
 
        // echo no users JSON
        echo json_encode($response);
    }

?>