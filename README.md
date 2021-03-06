# 카카오페이 뿌리기 기능 구현하기

## 개발 환경
- Java 11
- Spring Boot 2.3.1
- Spring Data JDBC, H2
- JUnit5

## 실행

```bash
# Test
$ ./mvnw test

# Run
$ ./mvnw spring-boot:run
```

## 용어사전

| 한글 | 영어 | 설명 |
| -- | -- | -- |
| 돈 뿌리기 | Sprinkling Money | 사용자가 대화방에 돈을 뿌리는 기능 |
| 돈 줍기 | Picking Money | 사용자가 돈을 뿌린 대화방에서 돈을 가져가는 기능 |
| 뿌려진 돈 | Sprinkled Money | 사용자가 대화방에 뿌린 돈 |
| 줏은 돈 | Picked Money | 방의 사용자가 뿌려진 돈을 돈 줍기를 통해 받은 돈 |
| 토큰 | Token | 뿌려진 돈의 식별자로 사용되며 3자리 문자열로 이루어져 있다 |

## 해결 전략

### 뿌리기
- 고유 Token 생성
  - Token을 생성하는 기능을 **TokenGenerator**로 추상화하여 Token 생성 전략에 따라 쉽게 갈아끼울 수 있도록 하였다.
- 금액 분배 로직
  - 금액 분배 로직도 기획에 따른 금액 분배 전략에 따라 쉽게 갈아끼울 수 있도록 **MoneyDivider**로 추상화하였다.

### 받기
- 받기의 경우 뿌리기 시 분배해 놓은 돈을 선착순으로 가져가는 방식이기 때문에 Race condition을 고려해야한다.
  - 요청에 의해 뿌려진 돈에 대한 원자성을 지키기 위해 여러 방식을 사용할 수 있는데 그 중에서 DB isolation level을 **READ_COMMITTED**로 두어 dirty read를 방지하도록 하였다.
- 요청 유저의 대화방 확인
  - 받기를 요청한 유저가 뿌리기가 호출된 대화방에 속해 있는지 확인하기 위한 방식은 API 호출, DB 조회 등 여러 방식이 있을 수 있고,
    그 방법에 대한 구체적인 정보가 없어 확인 기능을 interface로 두었고 소스에는 모두 정상인 것으로 처리하였다.

### 조회
- 단순 단건 조회 기능이기에 기존 Repository를 사용하여 조회하는 방식으로 구현하였다.
  - 하지만 다량의 트래픽을 고려한다면 Caching, NoSQL 등을 활용하여 CQRS 방식을 고려할 만하다.

## 추가로 고려할 만한 점 
- Token 생성
  - alphanumeric 3자리 문자열은 Identifier로써 사용하기엔 조합의 경우의 수가 제한이 있다고 생각된다. (26+10)^3 = 46,656 가지
  - Room ID + Token을 Identifier로 사용한다면 결국 제한은 있지만 어느정도 고유성을 갖을 순 있을 것이다.
- 클라이언트에서 사용자의 의도와 다르게 동일한 요청을 두 번 연달아 보냈을 때 이를 막아주는 기능이 없다.
  - 동일 유저의 동일 요청값에 대해 주어진 시간 내에 반복 요청을 체크하는 기능을 달아주면 막아줄 수 있을 것이다.
- SQL DB를 사용하기 때문에 Scale out 되더라도 다량의 트래픽에서는 DB가 병목으로 작용할 가능성이 있다.
  - API 애플리케이션 앞단에 요청 queue를 사용하거나,
  - Redis와 같은 in-memory DB나 Reactive 등을 활용하여 사용자 요청을 빠르고 효율적으로 처리할 수 있도록 할 수 있을 것이다.
