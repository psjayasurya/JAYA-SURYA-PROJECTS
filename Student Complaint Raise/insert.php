<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Page title</title>
</head>
<body>
    
</body>
</html><?php
$username = $_POST['user'];
 $password= $_POST['pass'];
$con=mysqli_connect("localhost:2811","root","","id21295219_logindb");
 $sql = "INSERT INTO data(email,password)
 VALUES ('$username',('$password'))";
$result=mysqli_query($con,$sql);
header("Location:alert.html");
header("Location:index.html");


	 

?>
