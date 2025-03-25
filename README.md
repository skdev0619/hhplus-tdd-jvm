# 포인트 충전 시스템
이 프로그램은 사용자 포인트 관리 기능을 제공합니다. 주요 기능은 다음과 같습니다.
- 포인트 충전: 사용자가 자신의 계정에 포인트를 충전할 수 있습니다.
- 포인트 조회: 현재 계정의 잔여 포인트를 확인할 수 있습니다.
- 포인트 사용: 필요한 만큼 포인트를 차감하여 사용할 수 있습니다.
- 포인트 사용 이력 조회: 특정 유저의 포인트 충전, 사용 내역을 확인할 수 있습니다.

## 요구사항 정리
- [ ] 특정 유저의 포인트를 조회한다
- [ ] 특정 유저가 포인트를 충전한다
  - [x] 포인트는 양의 정수만 충전 가능하다
  - [ ] 포인트는 100만 포인트 이상 충전할 수 없다.
  - [ ] 포인트 충전 시, 포인트 사용 이력이 쌓인다
  - [ ] 여러 사용자가 동시에 동일 계정에 포인트 충전 시, 정상 처리 되어야 한다.
- [ ] 특정 유저가 포인트를 사용한다
  - [ ] 특정 유저의 포인트 잔액이 포인트 사용 금액보다 적으면 사용할 수 없다
  - [ ] 포인트 사용 시, 포인트 사용 이력이 쌓인다
  - [ ] 여러 사용자가 동시에 동일 계정에 포인트 사용 시, 정상 처리 되어야 한다.
- [ ] 특정 유저의 포인트 사용 이력을 조회한다