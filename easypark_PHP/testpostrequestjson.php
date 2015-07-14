<?php
if (isset ($_REQUEST['json'])) {
  $decoded = json_decode (stripslashes (link is external)($_REQUEST['json']), TRUE);
  if (is_null ($decoded)) {
    $response['status'] = array (
      'type' => 'error',
      'value' => 'Invalid JSON value found',
    );
    $response['request'] = $_REQUEST['json'];
  }
  else {
    $response['status'] = array (
      'type' => 'message',
      'value' => 'Valid JSON value found',
    );
    //Send the original message back.
    $response['request'] = $decoded;
  }
}
else {
  $response['status'] = array (
    'type' => 'error',
    'value' => 'No JSON value set',
  );
}
$encoded = json_encode ($response);
header ('Content-type: application/json');
exit ($encoded);