<?php
include 'datetomilliseconds.php';
$fromMYSQL =  new DateTime('2015-07-07 21:46:59');
$milliseconds = datetimetoms($fromMYSQL);

echo $milliseconds;
?>