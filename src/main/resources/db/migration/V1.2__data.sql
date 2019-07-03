insert into `contract_subjects` (name) values ("北京腔调");
insert into `contract_subjects` (name) values ("深圳腔调");

insert into `providers` (name, bank_name, bank_account, cellphone) values ("测试供应商1", "北京银行", "8862172728281919", "13581533879");
insert into `providers` (name, bank_name, bank_account, cellphone) values ("测试供应商2", "华夏银行", "8282717162625353", "13581533879");

insert into `staffs` (name, cellphone, ascription) values ("秦超", "13817262626", 1);
insert into `staffs` (name, cellphone, ascription) values ("吴逍逸", "18372626162", 2);
insert into `staffs` (name, cellphone, ascription) values ("沈智", "19273625152", 2);

insert into `fee_category` (name, category_type, stage) values ("前置费", 1, 1);
insert into `fee_category` (name, category_type, stage) values ("人工费", 1, 1);
insert into `fee_category` (name, category_type, stage) values ("器材费", 1, 1);
insert into `fee_category` (name, category_type, stage) values ("场地及搭景", 1, 1);
insert into `fee_category` (name, category_type, stage) values ("道具服装费", 1, 1);
insert into `fee_category` (name, category_type, stage) values ("演员费", 1, 1);
insert into `fee_category` (name, category_type, stage) values ("交通及膳宿费超时", 1, 1);

insert into `fee_category` (name, category_type, stage) values ("素材", 1, 2);
insert into `fee_category` (name, category_type, stage) values ("外包及委托", 1, 2);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("制片备用金", 2, 1, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("前期企划", 2, 1, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("分镜脚本", 2, 1, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("试镜", 2, 1, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("前期筹备交通费（打车、汽油、高速、停车）", 2, 1, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("前期筹备餐饮", 2, 1, 1);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("监制", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("导演", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("执行导演", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("制片", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("制片助理", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("场务", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("摄影师", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("平面摄影师", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("灯光师", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("录音师", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("美术指导", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("服装师", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("道具师", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("化妆师", 2, 2, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("特殊人员", 2, 2, 1);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("摄影器材", 2, 3, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("特殊器材", 2, 3, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("灯光器材", 2, 3, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("录音器材", 2, 3, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("其他", 2, 3, 1);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("影棚租金", 2, 4, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("搭景", 2, 4, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("外景场地", 2, 4, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("取暖炉、电费、棚长劳务等", 2, 4, 1);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("道具费", 2, 5, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("服装费", 2, 5, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("化妆费", 2, 5, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("美术费", 2, 5, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("其他", 2, 5, 1);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("主要演员", 2, 6, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("演员", 2, 6, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("群众演员", 2, 6, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("其他", 2, 6, 1);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("租车费", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("器材车/道具车", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("飞机票/火车票", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("住宿费", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("拍摄日伙食饮料", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("拍摄日交通费", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("杂支", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("不可预见", 2, 7, 1);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("超时", 2, 7, 1);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("音乐素材", 2, 8, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("视频素材", 2, 8, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("平面素材", 2, 8, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("其他", 2, 8, 2);

insert into `fee_category` (name, category_type, parent_category_id, stage) values ("配音", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("配乐（音乐制作）", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("调色", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("三维动画-材质", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("三维动画-灯光", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("三维动画-渲染", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("三维动画-贴图", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("三维动画-layout预演", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("三维动画-最终渲染合成", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("MG动画", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("剪辑", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("合成", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("特效", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("插画", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("分镜", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("翻译", 2, 9, 2);
insert into `fee_category` (name, category_type, parent_category_id, stage) values ("其他", 2, 9, 2);