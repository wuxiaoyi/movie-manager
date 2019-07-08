INSERT INTO `users` (`name`, `email`, `cellphone`, `password`, `password_slat`)
VALUES
	('系统管理员','admin@qq.com','13581533879','c9ae9d31f1985c9139515348484e7242','a5ac1198899b71825960bf622b71559a');

INSERT INTO  `roles` (`name`) values ('超级管理员');

INSERT INTO  `permission_roles` (`role_id`, `permission_id`) values
  (1, 1), (1, 2), (1,3), (1, 4), (1, 5), (1, 6), (1, 7), (1, 8), (1, 9),
  (1, 10), (1, 11), (1, 12), (1, 13), (1, 14), (1, 15), (1, 16), (1, 17), (1, 18),
  (1, 19), (1, 20), (1, 21), (1, 22), (1, 23), (1, 24), (1, 25), (1, 26);

INSERT INTO  `user_roles` (`user_id`, `role_id`) values (1, 1);


