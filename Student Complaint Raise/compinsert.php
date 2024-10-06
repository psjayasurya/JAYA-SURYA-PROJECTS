<?php
$department = $_POST['dept'];
 $datee= $_POST['date'];
$complaintt= $_POST['complaint'];
$con=mysqli_connect("localhost:2811","root","","id21295219_logindb");
 $sql = "INSERT INTO compdb(dept,date,complaint)
 VALUES ('$department','$datee','$complaintt')";
$result=mysqli_query($con,$sql);
          
        if($result){  
	
            header("Location:comalert.html");
        }  
       

	 

?>
