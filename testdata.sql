INSERT INTO SKILL (SkillID, SkillName)
  VALUES (10, 'Fishing');
  
INSERT INTO SKILL (SkillID, SkillName)
  VALUES (8, 'Woodcutting');

INSERT INTO ITEM (ItemID, ItemName, ItemXP, ItemPrice, SkillID)
  VALUES (377, 'Raw Lobster', 90, 131, 10);

INSERT INTO ITEM (ItemID, ItemName, ItemXP, ItemPrice, SkillID)
  VALUES (1511, 'Log', 25, 30, 8);

INSERT INTO BOT (BotID, BotName, IsActive, IsOnline, SkillID)
  VALUES (12, 'RedRooster' , 1, 1, 10);

INSERT INTO REPORT (BotID, ItemID, NumOfItems, XpPerHour, GpPerHour)
  VALUES (12, 1511, 0, 0, 0);
