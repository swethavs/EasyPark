<?php

/*
 * Use the Haversine Formula to display the 100 closest matches to $origLat, $origLon
 * Only search the MySQL table $tableName for matches within a 10 mile ($dist) radius.
 */
 
// http://stackoverflow.com/questions/2234204/latitude-longitude-find-nearest-latitude-longitude-complex-sql-or-complex-calc


  // include db connect class
    require_once __DIR__ . '/db_connect.php';
 
    // connecting to db
    $db = new DB_CONNECT();
    
    $tableName = "parkinglots";
    $tableName2 = "parkingspots";
	$origLat = 45.5094093;
	$origLon = -122.6813566;
	$dist = 100; // This is the maximum distance (in miles) away from $origLat, $origLon in which to search
	$fromTime = 1436469420000 ; //2015-07-07 21:46:59
	$toTime = 1436469660000;  //2015-07-07 23:46:59// 1436453340000
	
	file_put_contents("testfile.txt", $tableName);
	$query = "SELECT parkinglotsid,address, cost, 3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as distance,
         
          FROM $tableName WHERE 
          longitude between ($origLon-$dist/abs(cos(radians($origLat))*69)) 
          and ($origLon+$dist/abs(cos(radians($origLat))*69)) 
          and latitude between ($origLat-($dist/69)) 
          and ($origLat+($dist/69)) 
          having distance < $dist ORDER BY distance limit 100;"; 
          
          $query2 = "SELECT  distinct  parkinglotsid,address, cost,3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as miles FROM $tableName 
          inner join  $tableName2  ON parkinglotsid = fk_parkinglotsid
           WHERE 
          longitude between ($origLon-$dist/abs(cos(radians($origLat))*69)) 
          and ($origLon+$dist/abs(cos(radians($origLat))*69)) 
          and latitude between ($origLat-($dist/69)) 
          and ($origLat+($dist/69)) and ((UNIX_TIMESTAMP(fromtime) * 1000) NOT BETWEEN $fromTime and $toTime )
         and ((UNIX_TIMESTAMP(totime) * 1000) NOT BETWEEN $fromTime and $toTime )
          having miles < $dist ORDER BY miles limit 100;";
          
          $query3 = 
       
          // and ((UNIX_TIMESTAMP(fromtime) * 1000) NOT BETWEEN $fromTime and $toTime )
         // and ((UNIX_TIMESTAMP(totime) * 1000) NOT BETWEEN $fromTime and $toTime )
          $query1 = "SELECT distinct  parkinglotsid,address, cost,3956 * 2 * 
          ASIN(SQRT( POWER(SIN(($origLat - abs(latitude))*pi()/180/2),2)
          +COS($origLat*pi()/180 )*COS(abs(latitude)*pi()/180)
          *POWER(SIN(($origLon-longitude)*pi()/180/2),2))) 
          as distance,
           (UNIX_TIMESTAMP(fromtime) * 1000) as fromtime FROM $tableName 
          inner join  $tableName2  ON $tableName.parkinglotsid = $tableName2.fk_parkinglotsid
           WHERE 
          longitude between ($origLon-$dist/abs(cos(radians($origLat))*69)) 
          and ($origLon+$dist/abs(cos(radians($origLat))*69)) 
          and latitude between ($origLat-($dist/69)) 
          and ($origLat+($dist/69)) 
          having distance < $dist ORDER BY distance limit 100;"; 
	$result1 = mysql_query($query1) or die(mysql_error());
	$result = mysql_query($query2) or die(mysql_error());
	
	if (!empty($result)) {
        // check for empty result
        if (mysql_num_rows($result) > 0) {
        
 		$response["parkinglots"] = array();
		while ($row = mysql_fetch_array($result)) 
		{
		
        // temp user array 
            $parkinglot = array();
            $parkinglot["parkinglotsid"] = $row["parkinglotsid"];
            $parkinglot["address"] = $row["address"];
            $parkinglot["miles"] = $row["miles"];
            $parkinglot["cost"] = $row["cost"];
            echo json_encode($parkinglot);
              }
            // success
           /* array_push($response["parkinglots"], $parkinglot);
 }// While loop*/
            // echoing JSON response
            /*echo json_encode($output);*/
            
        } // rows>0
        else {
            // no product found
            
            echo "No parkinglots found"; // >0
 
            // echo no users JSON
       
        }
    }// empty result
     else {
        // no product found
        $response["message"] = "No parkinglots found"; // result empty
 
        // echo no users JSON
        echo json_encode($response);
    }
 // checking post condition

	
    


?>