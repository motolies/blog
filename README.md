








## 빌드

### 다중 아키텍처 빌드시
현재 메이븐 오류로 m1에서만 양뱡향 빌드가 되드라
```shell
# 기본 docker buildx 로는 바로 빌드가 되지 않는다.
# 그래서 신규로 하나 생성하여 주자
docker buildx create --name jarvis \
&& docker buildx use jarvis \
&& docker buildx inspect --bootstrap

# -t 옵션을 붙이면 tag를 추가해서 업로드 가능하다
docker buildx build --platform linux/amd64,linux/arm64 --no-cache --push -t docker.hvy.kr/blog-back  .
```

### 단일 빌드시
```shell
# 빌드
docker build --no-cache -t docker.hvy.kr/blog-back .

# 이미지 푸쉬
docker push --all-tags docker.hvy.kr/blog-back
```

## 실행


```shell
# 삭제
docker rm -f blogback


# 실행(테스트용)
docker run -d --restart=unless-stopped \
--pull always \
-p 9999:8080 \
-e DB_URL=mariadb:3306 --link mariadb \
-e FILE_PATH=/skyscape/file
-v /skyscape/file:/skyscape/file
--name blogback docker.hvy.kr/blog-back

# 실행(테스트용 - windows)
docker run -d --restart=unless-stopped --pull always -p 9999:8080 -e DB_URL=mariadb:3306 --link mariadb -e FILE_PATH="/skyscape/file" -v "C:\Users\user\skyscape\file:/skyscape/file" --name blogback docker.hvy.kr/blog-back

# 실행(프로덕션)
docker run -d --restart=unless-stopped \
--pull always \
-p 9090:8080 \
-e DB_URL=blogdb:3306 --link blogdb \
-v /volume1/docker/blog/file:/skyscape/file \
--name blogback docker.hvy.kr/blog-back
```


compose를 사용해서 실행하려 했으나 db를 시놀로지에서 같이 실행시키는데 다른 컨테이너와 같이 사용하므로 그냥 link로 실행하기로 함

```shell
# 완전한 삭제
docker-compose stop && docker-compose rm -f 

# 온전히 새로 다운 받아서 실행
docker-compose pull && docker-compose up -d
```
