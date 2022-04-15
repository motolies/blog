








## 빌드

```shell
# 빌드
docker build --no-cache -t docker.hvy.kr/blog-back .

# 이미지 푸쉬
docker push --all-tags docker.hvy.kr/blog-back
```

## 실행


```shell
# 삭제
docker rm -f blog


# 실행(테스트용)
docker run -d --restart=unless-stopped \
--pull always \
-p 9999:8080 \
-e DB_URL=mariadb:3306 --link mariadb \
--name blogback docker.hvy.kr/blog-back


# 실행(프로덕션)
docker run -d --restart=unless-stopped \
--pull always \
-p 8080:8080 \
-e DB_URL=mariadb:3306 --link mariadb \
-v /volume1/docker/blog/file:/skyscape/file \
--name blog docker.hvy.kr/blog
```


compose를 사용해서 실행하려 했으나 db를 시놀로지에서 같이 실행시키는데 다른 컨테이너와 같이 사용하므로 그냥 link로 실행하기로 함

```shell
# 완전한 삭제
docker-compose stop && docker-compose rm -f 

# 온전히 새로 다운 받아서 실행
docker-compose pull && docker-compose up -d
```