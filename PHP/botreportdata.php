<?php
    include_once("connection.php");

    //The following query will obtain a specific bots numerical data, along with name, performing skill name...

    if (isset($_POST['inputBotID']) && isset($_POST['inputItemID'])){

      $botID = $_POST['inputBotID'];
      $itemID = $_POST['inputItemID'];

      $query = "SELECT REPORT.*, BOT.BotName, BOT.IsOnline, ITEM.ItemName, SKILL.SkillName
                    FROM REPORT, BOT, SKILL, ITEM
                    WHERE REPORT.BotID = '$botID'
                    AND REPORT.ItemID = '$itemID'
                    AND BOT.BotID = '$botID'
                    AND ITEM.ItemID = '$itemID'
                    AND SKILL.SkillID = ITEM.SkillID";

      $result  = mysqli_query ($conn, $query);

      while($row = mysqli_fetch_assoc($result)){
        $data[] = $row;
      }

      echo json_encode($data);
  }
 ?>
