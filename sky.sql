create database  if not exists `sky_take_out` ;
use `sky_take_out`;

drop table if exists `address_book`;
create table `address_book` (
  `id` bigint not null auto_increment comment '主键',
  `user_id` bigint not null comment '用户id',
  `consignee` varchar(50) collate utf8_bin default null comment '收货人',
  `sex` varchar(2) collate utf8_bin default null comment '性别',
  `phone` varchar(11) collate utf8_bin not null comment '手机号',
  `province_code` varchar(12) character set utf8mb4  default null comment '省级区划编号',
  `province_name` varchar(32) character set utf8mb4  default null comment '省级名称',
  `city_code` varchar(12) character set utf8mb4  default null comment '市级区划编号',
  `city_name` varchar(32) character set utf8mb4  default null comment '市级名称',
  `district_code` varchar(12) character set utf8mb4  default null comment '区级区划编号',
  `district_name` varchar(32) character set utf8mb4  default null comment '区级名称',
  `detail` varchar(200) character set utf8mb4  default null comment '详细地址',
  `label` varchar(100) character set utf8mb4  default null comment '标签',
  `is_default` tinyint(1) not null default '0' comment '默认 0 否 1是',
  primary key (`id`)
) engine=innodb auto_increment=2 default charset=utf8mb3 collate=utf8_bin comment='地址簿';

drop table if exists `category`;
create table `category` (
  `id` bigint not null auto_increment comment '主键',
  `type` int default null comment '类型   1 菜品分类 2 套餐分类',
  `name` varchar(32) collate utf8_bin not null comment '分类名称',
  `sort` int not null default '0' comment '顺序',
  `status` int default null comment '分类状态 0:禁用，1:启用',
  `create_time` datetime default null comment '创建时间',
  `update_time` datetime default null comment '更新时间',
  `create_user` bigint default null comment '创建人',
  `update_user` bigint default null comment '修改人',
  primary key (`id`),
  unique key `idx_category_name` (`name`)
) engine=innodb auto_increment=23 default charset=utf8mb3 collate=utf8_bin comment='菜品及套餐分类';

insert into `category` values (11,1,'酒水饮料',10,1,'2022-06-09 22:09:18','2022-06-09 22:09:18',1,1);
insert into `category` values (12,1,'传统主食',9,1,'2022-06-09 22:09:32','2022-06-09 22:18:53',1,1);
insert into `category` values (13,2,'人气套餐',12,1,'2022-06-09 22:11:38','2022-06-10 11:04:40',1,1);
insert into `category` values (15,2,'商务套餐',13,1,'2022-06-09 22:14:10','2022-06-10 11:04:48',1,1);
insert into `category` values (16,1,'蜀味烤鱼',4,1,'2022-06-09 22:15:37','2022-08-31 14:27:25',1,1);
insert into `category` values (17,1,'蜀味牛蛙',5,1,'2022-06-09 22:16:14','2022-08-31 14:39:44',1,1);
insert into `category` values (18,1,'特色蒸菜',6,1,'2022-06-09 22:17:42','2022-06-09 22:17:42',1,1);
insert into `category` values (19,1,'新鲜时蔬',7,1,'2022-06-09 22:18:12','2022-06-09 22:18:28',1,1);
insert into `category` values (20,1,'水煮鱼',8,1,'2022-06-09 22:22:29','2022-06-09 22:23:45',1,1);
insert into `category` values (21,1,'汤类',11,1,'2022-06-10 10:51:47','2022-06-10 10:51:47',1,1);

drop table if exists `dish`;
create table `dish` (
  `id` bigint not null auto_increment comment '主键',
  `name` varchar(32) collate utf8_bin not null comment '菜品名称',
  `category_id` bigint not null comment '菜品分类id',
  `price` decimal(10,2) default null comment '菜品价格',
  `image` varchar(255) collate utf8_bin default null comment '图片',
  `description` varchar(255) collate utf8_bin default null comment '描述信息',
  `status` int default '1' comment '0 停售 1 起售',
  `create_time` datetime default null comment '创建时间',
  `update_time` datetime default null comment '更新时间',
  `create_user` bigint default null comment '创建人',
  `update_user` bigint default null comment '修改人',
  primary key (`id`),
  unique key `idx_dish_name` (`name`)
) engine=innodb auto_increment=70 default charset=utf8mb3 collate=utf8_bin comment='菜品';

insert into `dish` values (46,'王老吉',11,6.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/41bfcacf-7ad4-4927-8b26-df366553a94c.png','',1,'2022-06-09 22:40:47','2022-06-09 22:40:47',1,1);
insert into `dish` values (47,'北冰洋',11,4.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/4451d4be-89a2-4939-9c69-3a87151cb979.png','还是小时候的味道',1,'2022-06-10 09:18:49','2022-06-10 09:18:49',1,1);
insert into `dish` values (48,'雪花啤酒',11,4.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/bf8cbfc1-04d2-40e8-9826-061ee41ab87c.png','',1,'2022-06-10 09:22:54','2022-06-10 09:22:54',1,1);
insert into `dish` values (49,'米饭',12,2.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/76752350-2121-44d2-b477-10791c23a8ec.png','精选五常大米',1,'2022-06-10 09:30:17','2022-06-10 09:30:17',1,1);
insert into `dish` values (50,'馒头',12,1.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/475cc599-8661-4899-8f9e-121dd8ef7d02.png','优质面粉',1,'2022-06-10 09:34:28','2022-06-10 09:34:28',1,1);
insert into `dish` values (51,'老坛酸菜鱼',20,56.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/4a9cefba-6a74-467e-9fde-6e687ea725d7.png','原料：汤，草鱼，酸菜',1,'2022-06-10 09:40:51','2022-06-10 09:40:51',1,1);
insert into `dish` values (52,'经典酸菜鮰鱼',20,66.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/5260ff39-986c-4a97-8850-2ec8c7583efc.png','原料：酸菜，江团，鮰鱼',1,'2022-06-10 09:46:02','2022-06-10 09:46:02',1,1);
insert into `dish` values (53,'蜀味水煮草鱼',20,38.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/a6953d5a-4c18-4b30-9319-4926ee77261f.png','原料：草鱼，汤',1,'2022-06-10 09:48:37','2022-06-10 09:48:37',1,1);
insert into `dish` values (54,'清炒小油菜',19,18.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/3613d38e-5614-41c2-90ed-ff175bf50716.png','原料：小油菜',1,'2022-06-10 09:51:46','2022-06-10 09:51:46',1,1);
insert into `dish` values (55,'蒜蓉娃娃菜',19,18.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/4879ed66-3860-4b28-ba14-306ac025fdec.png','原料：蒜，娃娃菜',1,'2022-06-10 09:53:37','2022-06-10 09:53:37',1,1);
insert into `dish` values (56,'清炒西兰花',19,18.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/e9ec4ba4-4b22-4fc8-9be0-4946e6aeb937.png','原料：西兰花',1,'2022-06-10 09:55:44','2022-06-10 09:55:44',1,1);
insert into `dish` values (57,'炝炒圆白菜',19,18.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/22f59feb-0d44-430e-a6cd-6a49f27453ca.png','原料：圆白菜',1,'2022-06-10 09:58:35','2022-06-10 09:58:35',1,1);
insert into `dish` values (58,'清蒸鲈鱼',18,98.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/c18b5c67-3b71-466c-a75a-e63c6449f21c.png','原料：鲈鱼',1,'2022-06-10 10:12:28','2022-06-10 10:12:28',1,1);
insert into `dish` values (59,'东坡肘子',18,138.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/a80a4b8c-c93e-4f43-ac8a-856b0d5cc451.png','原料：猪肘棒',1,'2022-06-10 10:24:03','2022-06-10 10:24:03',1,1);
insert into `dish` values (60,'梅菜扣肉',18,58.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/6080b118-e30a-4577-aab4-45042e3f88be.png','原料：猪肉，梅菜',1,'2022-06-10 10:26:03','2022-06-10 10:26:03',1,1);
insert into `dish` values (61,'剁椒鱼头',18,66.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/13da832f-ef2c-484d-8370-5934a1045a06.png','原料：鲢鱼，剁椒',1,'2022-06-10 10:28:54','2022-06-10 10:28:54',1,1);
insert into `dish` values (62,'金汤酸菜牛蛙',17,88.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/7694a5d8-7938-4e9d-8b9e-2075983a2e38.png','原料：鲜活牛蛙，酸菜',1,'2022-06-10 10:33:05','2022-06-10 10:33:05',1,1);
insert into `dish` values (63,'香锅牛蛙',17,88.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/f5ac8455-4793-450c-97ba-173795c34626.png','配料：鲜活牛蛙，莲藕，青笋',1,'2022-06-10 10:35:40','2022-06-10 10:35:40',1,1);
insert into `dish` values (64,'馋嘴牛蛙',17,88.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/7a55b845-1f2b-41fa-9486-76d187ee9ee1.png','配料：鲜活牛蛙，丝瓜，黄豆芽',1,'2022-06-10 10:37:52','2022-06-10 10:37:52',1,1);
insert into `dish` values (65,'草鱼2斤',16,68.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/b544d3ba-a1ae-4d20-a860-81cb5dec9e03.png','原料：草鱼，黄豆芽，莲藕',1,'2022-06-10 10:41:08','2022-06-10 10:41:08',1,1);
insert into `dish` values (66,'江团鱼2斤',16,119.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/a101a1e9-8f8b-47b2-afa4-1abd47ea0a87.png','配料：江团鱼，黄豆芽，莲藕',1,'2022-06-10 10:42:42','2022-06-10 10:42:42',1,1);
insert into `dish` values (67,'鮰鱼2斤',16,72.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/8cfcc576-4b66-4a09-ac68-ad5b273c2590.png','原料：鮰鱼，黄豆芽，莲藕',1,'2022-06-10 10:43:56','2022-06-10 10:43:56',1,1);
insert into `dish` values (68,'鸡蛋汤',21,4.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/c09a0ee8-9d19-428d-81b9-746221824113.png','配料：鸡蛋，紫菜',1,'2022-06-10 10:54:25','2022-06-10 10:54:25',1,1);
insert into `dish` values (69,'平菇豆腐汤',21,6.00,'https://sky-itcast.oss-cn-beijing.aliyuncs.com/16d0a3d6-2253-4cfc-9b49-bf7bd9eb2ad2.png','配料：豆腐，平菇',1,'2022-06-10 10:55:02','2022-06-10 10:55:02',1,1);

drop table if exists `dish_flavor`;
create table `dish_flavor` (
  `id` bigint not null auto_increment comment '主键',
  `dish_id` bigint not null comment '菜品',
  `name` varchar(32) collate utf8_bin default null comment '口味名称',
  `value` varchar(255) collate utf8_bin default null comment '口味数据list',
  primary key (`id`)
) engine=innodb auto_increment=104 default charset=utf8mb3 collate=utf8_bin comment='菜品口味关系表';

insert into `dish_flavor` values (40,10,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
insert into `dish_flavor` values (41,7,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (42,7,'温度','[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]');
insert into `dish_flavor` values (45,6,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (46,6,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
insert into `dish_flavor` values (47,5,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
insert into `dish_flavor` values (48,5,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
insert into `dish_flavor` values (49,2,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
insert into `dish_flavor` values (50,4,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
insert into `dish_flavor` values (51,3,'甜味','[\"无糖\",\"少糖\",\"半糖\",\"多糖\",\"全糖\"]');
insert into `dish_flavor` values (52,3,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (86,52,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (87,52,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
insert into `dish_flavor` values (88,51,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (89,51,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
insert into `dish_flavor` values (92,53,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (93,53,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
insert into `dish_flavor` values (94,54,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\"]');
insert into `dish_flavor` values (95,56,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (96,57,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (97,60,'忌口','[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]');
insert into `dish_flavor` values (101,66,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
insert into `dish_flavor` values (102,67,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');
insert into `dish_flavor` values (103,65,'辣度','[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]');

drop table if exists `employee`;
create table `employee` (
  `id` bigint not null auto_increment comment '主键',
  `name` varchar(32) collate utf8_bin not null comment '姓名',
  `username` varchar(32) collate utf8_bin not null comment '用户名',
  `password` varchar(64) collate utf8_bin not null comment '密码',
  `phone` varchar(11) collate utf8_bin not null comment '手机号',
  `sex` varchar(2) collate utf8_bin not null comment '性别',
  `id_number` varchar(18) collate utf8_bin not null comment '身份证号',
  `status` int not null default '1' comment '状态 0:禁用，1:启用',
  `create_time` datetime default null comment '创建时间',
  `update_time` datetime default null comment '更新时间',
  `create_user` bigint default null comment '创建人',
  `update_user` bigint default null comment '修改人',
  primary key (`id`),
  unique key `idx_username` (`username`)
) engine=innodb auto_increment=2 default charset=utf8mb3 collate=utf8_bin comment='员工信息';

insert into `employee` values (1,'管理员','admin','123456','13812312312','1','110101199001010047',1,'2022-02-15 15:51:20','2022-02-17 09:16:20',10,1);

drop table if exists `order_detail`;
create table `order_detail` (
  `id` bigint not null auto_increment comment '主键',
  `name` varchar(32) collate utf8_bin default null comment '名字',
  `image` varchar(255) collate utf8_bin default null comment '图片',
  `order_id` bigint not null comment '订单id',
  `dish_id` bigint default null comment '菜品id',
  `setmeal_id` bigint default null comment '套餐id',
  `dish_flavor` varchar(50) collate utf8_bin default null comment '口味',
  `number` int not null default '1' comment '数量',
  `amount` decimal(10,2) not null comment '金额',
  primary key (`id`)
) engine=innodb auto_increment=5 default charset=utf8mb3 collate=utf8_bin comment='订单明细表';

drop table if exists `orders`;
create table `orders` (
  `id` bigint not null auto_increment comment '主键',
  `number` varchar(50) collate utf8_bin default null comment '订单号',
  `status` int not null default '1' comment '订单状态 1待付款 2待接单 3已接单 4派送中 5已完成 6已取消 7退款',
  `user_id` bigint not null comment '下单用户',
  `address_book_id` bigint not null comment '地址id',
  `order_time` datetime not null comment '下单时间',
  `checkout_time` datetime default null comment '结账时间',
  `pay_method` int not null default '1' comment '支付方式 1微信,2支付宝',
  `pay_status` tinyint not null default '0' comment '支付状态 0未支付 1已支付 2退款',
  `amount` decimal(10,2) not null comment '实收金额',
  `remark` varchar(100) collate utf8_bin default null comment '备注',
  `phone` varchar(11) collate utf8_bin default null comment '手机号',
  `address` varchar(255) collate utf8_bin default null comment '地址',
  `user_name` varchar(32) collate utf8_bin default null comment '用户名称',
  `consignee` varchar(32) collate utf8_bin default null comment '收货人',
  `cancel_reason` varchar(255) collate utf8_bin default null comment '订单取消原因',
  `rejection_reason` varchar(255) collate utf8_bin default null comment '订单拒绝原因',
  `cancel_time` datetime default null comment '订单取消时间',
  `estimated_delivery_time` datetime default null comment '预计送达时间',
  `delivery_status` tinyint(1) not null default '1' comment '配送状态  1立即送出  0选择具体时间',
  `delivery_time` datetime default null comment '送达时间',
  `pack_amount` int default null comment '打包费',
  `tableware_number` int default null comment '餐具数量',
  `tableware_status` tinyint(1) not null default '1' comment '餐具数量状态  1按餐量提供  0选择具体数量',
  primary key (`id`)
) engine=innodb auto_increment=4 default charset=utf8mb3 collate=utf8_bin comment='订单表';

drop table if exists `setmeal`;
create table `setmeal` (
  `id` bigint not null auto_increment comment '主键',
  `category_id` bigint not null comment '菜品分类id',
  `name` varchar(32) collate utf8_bin not null comment '套餐名称',
  `price` decimal(10,2) not null comment '套餐价格',
  `status` int default '1' comment '售卖状态 0:停售 1:起售',
  `description` varchar(255) collate utf8_bin default null comment '描述信息',
  `image` varchar(255) collate utf8_bin default null comment '图片',
  `create_time` datetime default null comment '创建时间',
  `update_time` datetime default null comment '更新时间',
  `create_user` bigint default null comment '创建人',
  `update_user` bigint default null comment '修改人',
  primary key (`id`),
  unique key `idx_setmeal_name` (`name`)
) engine=innodb auto_increment=32 default charset=utf8mb3 collate=utf8_bin comment='套餐';

drop table if exists `setmeal_dish`;
create table `setmeal_dish` (
  `id` bigint not null auto_increment comment '主键',
  `setmeal_id` bigint default null comment '套餐id',
  `dish_id` bigint default null comment '菜品id',
  `name` varchar(32) collate utf8_bin default null comment '菜品名称 （冗余字段）',
  `price` decimal(10,2) default null comment '菜品单价（冗余字段）',
  `copies` int default null comment '菜品份数',
  primary key (`id`)
) engine=innodb auto_increment=47 default charset=utf8mb3 collate=utf8_bin comment='套餐菜品关系';

drop table if exists `shopping_cart`;
create table `shopping_cart` (
  `id` bigint not null auto_increment comment '主键',
  `name` varchar(32) collate utf8_bin default null comment '商品名称',
  `image` varchar(255) collate utf8_bin default null comment '图片',
  `user_id` bigint not null comment '主键',
  `dish_id` bigint default null comment '菜品id',
  `setmeal_id` bigint default null comment '套餐id',
  `dish_flavor` varchar(50) collate utf8_bin default null comment '口味',
  `number` int not null default '1' comment '数量',
  `amount` decimal(10,2) not null comment '金额',
  `create_time` datetime default null comment '创建时间',
  primary key (`id`)
) engine=innodb auto_increment=9 default charset=utf8mb3 collate=utf8_bin comment='购物车';

drop table if exists `user`;
create table `user` (
  `id` bigint not null auto_increment comment '主键',
  `openid` varchar(45) collate utf8_bin default null comment '微信用户唯一标识',
  `name` varchar(32) collate utf8_bin default null comment '姓名',
  `phone` varchar(11) collate utf8_bin default null comment '手机号',
  `sex` varchar(2) collate utf8_bin default null comment '性别',
  `id_number` varchar(18) collate utf8_bin default null comment '身份证号',
  `avatar` varchar(500) collate utf8_bin default null comment '头像',
  `create_time` datetime default null,
  primary key (`id`)
) engine=innodb auto_increment=4 default charset=utf8mb3 collate=utf8_bin comment='用户信息';