<?php      
    
    $username = $_POST['user'];  
    $password = $_POST['pass'];  
      
         $con=mysqli_connect("localhost:2811","root","","id21295219_logindb");
        $sql = "select *from data where email = '$username' and password = '$password'";  
        $result = mysqli_query($con, $sql);  
        $row = mysqli_fetch_array($result, MYSQLI_ASSOC);  
        $count = mysqli_num_rows($result);  
          
        if($count == 1){  
            header("Location:complaint.html");
        }  
        else{  
            header("Location:loginfail.html");
        }     
?>  
