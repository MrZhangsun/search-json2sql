drop table if exists `order`;
drop table if exists `customer`;
drop table if exists `product`;
create table `order` (
                         `id` int primary key,
                         `code` varchar(48) comment '订单号',
                         `money` double comment '价格',
                         `created_time` datetime comment '创建时间',
                         `customer_id` int comment '客户ID',
                         `product_id` int comment '产品ID'
) comment '订单表';

create table `customer`(
                           `id` int primary key,
                           `name` varchar(256) comment '客户名字',
                           `birthday` date comment '客户生日',
                           `age` int comment '客户年龄'
) comment '客户表';



create table `product`(
                          `id` int primary key,
                          `sku` varchar(48) comment 'sku',
                          `title` varchar(1000) comment '标题',
                          `description` text comment '描述'
) comment '产品表';


insert into `order` (id, code, money, created_time, customer_id, product_id) VALUES
(1, '10001', 10.0, now(), 1, 1),
(2, '10002', 12.0, now(), 2, 1),
(3, '10003', 10.0, now(), 1, 1),
(4, '10005', 11.0, now(), 2, 1);
insert into customer (id, name, birthday, age) values (1, '张三', '1990-01-02', 33);
insert into customer (id, name, birthday, age) values (2, '里斯', '1992-01-02', 31);
insert into product (id, sku, title, description) values (1, 'sku0001', '烫发剂', '这个烫发剂很赞哦');
insert into product (id, sku, title, description) values (2, 'sku0002', '烧烤架', '这个烧烤架很赞哦');