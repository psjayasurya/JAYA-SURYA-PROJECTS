 <?php

  $conn=mysqli_connect("localhost:2811","root","","id21295219_logindb");
// Check connection
if (!$conn) {
  die("Connection failed: " . mysqli_connect_error());
}

$sql = "SELECT dept,complaint FROM compdb WHERE (date between '$fromdate' and '$todate') and dept='$dept' ";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
  // output data of each row
  while($row = mysqli_fetch_assoc($result)) {
    echo "dept: " . $row["dept"]. " - complaint: " . $row["complaint"].  "<br>";
  }
} else {
  echo "0 results";
}

mysqli_close($conn);
?>
