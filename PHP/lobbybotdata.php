<?php
    include_once("connection.php");

    //Will get the name, their world and if they are online
    $query = "SELECT Bot.BotID, ItemID, BotName, IsOnline, World
              FROM Bot, Report
              WHERE Bot.BotID = Report.BotID";

    $result  = mysqli_query ($conn, $query);


    while($row = mysqli_fetch_assoc($result)){
      $data[] = $row;
    }

    echo json_encode($data);
 ?>
