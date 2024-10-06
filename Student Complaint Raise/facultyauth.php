<?php      
    
    $dept = $_POST['dept'];
 $fromdate= $_POST['date'];
$todate = $_POST['date1'];

    $password = $_POST['pass'];  
      
         $con=mysqli_connect("localhost:2811","root","","id21295219_logindb");
        $sql = "select *from facultydb where  password = '$password'";  
        $result = mysqli_query($con, $sql);  
        $row = mysqli_fetch_array($result, MYSQLI_ASSOC);  
        $count = mysqli_num_rows($result);  
          
        if($count == 1){  
             

  $conn=mysqli_connect("localhost:2811","root","","logindb");
// Check connection
if (!$conn) {
  die("Connection failed: " . mysqli_connect_error());
}

$sql = "SELECT dept,complaint FROM compdb WHERE (date between '$fromdate' and '$todate') and dept='$dept' ";
$result = mysqli_query($conn, $sql);

if (mysqli_num_rows($result) > 0) {
  // output data of each row
  while($row = mysqli_fetch_assoc($result)) {
    echo "Dept: " . $row["dept"]. " -------------- " . $row["complaint"].  "<br>";
  }
} else {
  echo "0 results";
}

mysqli_close($conn);


        }  
        else{  
            echo " Login failed. Invalid username or password.";  
        }     
?>  
