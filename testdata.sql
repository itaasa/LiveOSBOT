
INSERT INTO SKILL (SkillID, SkillName)
  VALUES
  (8, 'Woodcutting'),
  (10, 'Fishing'),
  (14, 'Mining');


INSERT INTO ITEM (ItemID, ItemName, ItemXP, ItemPrice, SkillID)
  VALUES
  (1511, 'Log', 25, 30, 8),
  (1519, 'Willow Log', 67, 11, 8),
  (1515, 'Yew Log', 175, 298, 8),
  (317, 'Raw Shrimp', 10, 17, 10),
  (377, 'Raw Lobster', 90, 131, 10),
  (383, 'Raw Shark', 110, 566, 10),
  (440, 'Iron ore', 35, 108, 14),
  (453, 'Coal', 50, 183, 14),
  (447, 'Mithril ore', 80, 175, 14);


INSERT INTO BOT (BotID, BotName, IsActive, IsOnline, SkillID)
  VALUES
  (0, 'Polygon' , 1, 1, 10),
  (1, 'Pangwin' , 1, 1, 8),
  (2, 'LebronJames' , 1, 1, 10),
  (3, 'James Bot' , 1, 1, 10),
  (4, 'Vcheckz1337', 1, 1, 14),
  (5, 'Happy4Horses', 1, 1, 14),
  (6, 'RickCaron', 1, 1, 8),
  (7, 'Kate', 1, 1, 14),
  (8, 'RackCityBot', 1, 1, 10),
  (9, 'RudeRob2008', 1, 1, 8),
  (10, 'Fishgutz', 1, 1, 8),
  (11, 'Microwave', 1, 1, 14);


INSERT INTO REPORT (BotID, ItemID, NumOfItems, XpPerHour, GpPerHour)
  VALUES
  (0, 377, 0, 0, 0),
  (1, 1515, 0, 0, 0),
  (2, 383, 0, 0, 0),
  (3, 317, 0, 0, 0),
  (4, 453, 0, 0, 0),
  (5, 447, 0, 0, 0),
  (6, 1519, 0, 0, 0),
  (7, 440, 0, 0, 0),
  (8, 377, 0, 0, 0),
  (9, 1515, 0, 0, 0),
  (10, 1519, 0, 0, 0),
  (11, 440, 0, 0, 0);
