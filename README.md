# 스프링부트 JPA, MySQL, Security 인스타그램
------------------------------------------------------------
### -Heroku Demo(heroku는 시스템권한 설정 불가로 인해 영어만 입력가능)
https://instagramweb.herokuapp.com/

<img src="https://user-images.githubusercontent.com/55027765/101129823-b1e2b480-3645-11eb-95a3-84ed96792e91.png" width="100%">


## 사용기술
![image](https://user-images.githubusercontent.com/55027765/101130369-b2c81600-3646-11eb-908a-574f1a3c43c3.png)
![image](https://user-images.githubusercontent.com/55027765/101130487-e86cff00-3646-11eb-8788-96dafd645e0d.png)




## MySQL 세팅

1. MySQL 한글 설정 (my.ini)

```ini
[client]
default-character-set=utf8

[mysql]
default-character-set=utf8

[mysqld]
collation-server = utf8_unicode_ci
init-connect='SET NAMES utf8'
init_connect='SET collation_connection = utf8_general_ci'
character-set-server=utf8
```

2. MySQL 데이터베이스 및 사용자 생성

- create user 'insta'@'%' identified by 'bitc5600';
- GRANT ALL PRIVILEGES ON 별.별 TO 'insta'@'%';
- create database insta;
- use insta;


## 맞팔 쿼리, 좋아요 카운트 쿼리

1. 좋아요 수 쿼리 (스칼라 서브쿼리)
   ![blog](https://postfiles.pstatic.net/MjAyMDA4MjRfMTYw/MDAxNTk4MjM5NzUwMjMy.VZH7JMI_P8AwMhJCSXxHfFSQq8uaJ7w6ufEjsvlae44g.mJoyoc69PAY-kHK5jeQW2JtrpOUA6i_qQFGcpqeHNNAg.PNG.getinthere/Screenshot_49.png?type=w773)

```sql
select
i.id,
i.caption,
(select count(*) from likes l where l.imageId = i.id) as 좋아요
from image i;
```

2. 맞팔 유무 쿼리 (Left outer Join 과 스칼라 서브쿼리)
   ![blog](https://postfiles.pstatic.net/MjAyMDA4MjRfMjAy/MDAxNTk4MjM3ODE4MjUw.pDKhnS9IE1usJqVXVVo9iNJOo5FPbC7YDOLBP4IwCQIg.3tTT-qYv5b27K9AMP-dZP1YauCvD-7MJLm_j6FvIvJkg.PNG.getinthere/Screenshot_48.png?type=w773)

```sql
select f1.id, f1.fromUserId, f1.toUserId, f1.createDate,
if(f2.fromUserId is null, false, true) "matpal"
from follow f1 left outer join follow f2
on f1.fromUserId = f2.toUserId and f1.toUserId = f2.fromUserId
order by f1.id;


select f1.id, f1.fromUserId, f1.toUserId,
f1.createDate,
(
select 1 from follow f2
where f1.fromUserId = f2.toUserId
and f1.toUserId = f2.fromUserId
) "matpal"
from follow f1;
```

## 회원 프로필 페이지
![blog](https://postfiles.pstatic.net/MjAyMDA4MzFfMTY0/MDAxNTk4ODQ2ODIyMjE1.h6LvLgpw7k-xjJAuGU04sx397NJovgMY8ktL_fuMPdcg._83FHpv7Y8ly5By_Z5-_56kewhkiCD5bgKOjf3L0pjAg.PNG.getinthere/Screenshot_1.png?type=w773)

- WebMvcConfig.java 수정
```java
// 이미지 경로 찾기를 위해 추가 시작
@Value("${file.path}")
private String uploadFolder;

@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
	WebMvcConfigurer.super.addResourceHandlers(registry);

	registry.addResourceHandler("/upload/**").addResourceLocations("file:///" + uploadFolder).setCachePeriod(3600)
			.resourceChain(true).addResolver(new PathResourceResolver());
}
// 이미지 경로 찾기를 위해 추가 끝
```
