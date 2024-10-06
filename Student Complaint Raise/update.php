<?php
$username = $_POST['user'];  
    $password = $_POST['pass'];  
      
         $con=mysqli_connect("localhost:2811","root","","id21295219_logindb");
    
// Check connection
if (!$con) {
  die("Connection failed: " . mysqli_connect_error());
}

$sql = "UPDATE data SET password='$password' WHERE email='$username'";

if (mysqli_query($con, $sql)) {
  header("Location:comalert.html");
} else {
  echo "Error updating record: " . mysqli_error($con);
}

mysqli_close($con);
?>
