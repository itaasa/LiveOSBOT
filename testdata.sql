INSERT INTO SKILL (SkillID, SkillName)
  VALUES (10, 'Fishing');

INSERT INTO ITEM (ItemID, ItemName, ItemXP, ItemPrice, SkillID)
  VALUES (377, 'Raw Lobster', 90, 131, 10);

INSERT INTO BOT (BotID, BotName, IsActive, IsOnline, SkillID)
  VALUES (12, 'RedRooster' , 1, 1, 10);

INSERT INTO REPORT (BotID, ItemID, NumOfItems, XpPerHour, GpPerHour)
  VALUES (12, 377, 3877, 52343.20, 231443.00);
