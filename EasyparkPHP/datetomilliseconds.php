<?php

function datetimetoms(DateTime $datetime) {
    


$date =  $datetime-> format('Y-m-d H:i:s');

$milliseconds = 1000 * strtotime($date);

return $milliseconds;
}
?>