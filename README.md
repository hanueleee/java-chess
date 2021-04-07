# java-chess

체스 게임 구현을 위한 저장소

## TDD DOMAIN

- [x] Piece (체스말)
    - [x] Position : N, M 의 위치
        - [x] 체스 말의 위치가 Grid 범위를 벗어나면 예외 출력
    - [x] move()
    - [x] isMovable()
    - [x] isMovable()
    - [x] validatePositionMoved() - 제자리 검증
        - [x] Empty
            - [x] 빈 칸
            - [x] 말이 이동하였을때, 그 말과 자리 교환
            - [x] 말이 먹혔을때, 새로운 empty생성 후 자리 배치
        - [x] King
            - [x] 상하좌우, 대각선 방향으로 각각 1칸씩만 움직일 수 있다.
        - [x] Rook
            - [x] 상하좌우 방향으로 기물이 없는 칸에 한해서 칸수의 제한 없이 움직일 수 있다.
        - [x] Bishop
            - [x] 대각선 방향으로 기물이 없는 칸에 한해서 칸수의 제한 없이 움직일 수 있다.
        - [x] Queen
            - [x] 상하좌우, 대각선 방향으로 기물이 없는 칸에 한해서 칸수의 제한 없이 움직일 수 있다.
        - [x] Knight
            - [x] 현재 위치한 칸으로부터 같은 랭크, 파일, 대각에 위치하지 않은 칸들 중에서 현재 위치와 가장 가까운 칸으로 이동한다. (수직 방향으로 한칸 움직인 후 수평 방향으로 두칸 움직이거나
              수직 방향으로 두칸 움직인 후 수평 방향으로 한칸 움직이는 것으로, 이는 L자 모양으로 움직인다고 생각하면 이해하기 쉽다.) 나이트는 유일하게 다른 기물을 넘어다닐 수 있다.
        - [x] Pawn
            - [x] 체스의 기물 중 가장 복잡한 행마법을 가지고 있다. 폰은 행마법과 기물을 잡는 법이 다른 유일한 기물이다. 바로 앞의 칸이 비어 있다면 앞으로 한 칸 전진할 수 있다.(바로 앞에
              상대의 기물이 있어도 잡을 수 없다.)
              경기중 단 한번도 움직이지 않은 폰은 바로 앞의 두칸이 비어 있을 때 두칸 전진할 수 있다.(한칸만 전진해도 된다.) 폰은 대각선 방향으로 바로 앞에 위치한 기물을 잡을 수 있다.(대각선
              방향으로 바로 앞에 위치한 칸이 비어 있더라도 그곳으로 전진할 수 없다.)
              폰은 앞쪽으로만 움직이며 절대 뒤쪽으로 행마하지 않는다. 폰은 앙파상과 프로모션 두가지의 특별 행마법을 가진다.(Schiller 2003, 17–19쪽).

- [x] Grid (체스 보드)
    - [x] List<Line>: N * N 의 Piece들의 집합
    - [x] getter

- [x] Line (체스의 가로 한 줄)

## move() 설계

### Grid에서 move(Position source, Position target) 메서드로 입력 받을 것이다.

이 때, 이동할 수 있는 곳인지 체크해야 한다.

- [유효성 검증] Grid 크기의 안에 위치하는 Position인지 체크한다.
    - Position한테 Grid의 크기를 벗어났는지 물어보기
- [유효성 검증] source가 Empty이면 안 된다. & 우리팀 말이어야만 움직일 수 있다.
- [유효성 검증][Pawn만 이 부분 로직이 다름 -> 전략으로 빼자] 출발점에서부터 갈 수 있는 형태의 방향과 거리를 고려했을 때, 갈 수 있는 곳인지 체크한다. 1.1. 가는 도중에 체스판 내의 범위를 벗어난
  경우 -> 더이상 추가하지 않고 끝낸다.      
  1.2. 가는 도중에 장애물을 만난 경우 -> 일단 그 장애물의 위치까지 추가하고 끝낸다. 최종. 갈 수 있는 곳으로 나온 List<Position>에서 target이 포함되는 지 체크하기
- [유효성 검증] 이동하려는 곳이 Empty가 아니라면, source와 target이 다른 색깔이어야만 한다.


- target이 Empty일 경우, 서로의 Piece 위치를 교환한다.
- target이 Empty가 아닐 경우, source를 Empty로 바꾸고 source의 위치를 target의 위치로 바꾼다.

## Pawn

- [유효성 검증] Grid 크기의 안에 위치하는 Position인지 체크한다.
    - Position한테 Grid의 크기를 벗어났는지 물어보기
- [유효성 검증] source가 Empty이면 안 된다. & 우리팀 말이어야만 움직일 수 있다.
- [유효성 검증][Pawn만 이 부분 로직이 다름 -> 전략으로 빼자] 출발점에서부터 갈 수 있는 형태의 방향과 거리를 고려했을 때, 갈 수 있는 곳인지 체크한다.
    0. Pawn의 초기에 앞으로 2칸 가는 경우를 포함해서 계산하기 1.1. 가는 도중에 체스판 내의 범위를 벗어난 경우 -> 더이상 추가하지 않고 끝낸다.      
       1.2. 가는 도중에 장애물을 만난 경우 -> 일단 그 장애물의 위치까지 추가하고 끝낸다. 최종. 갈 수 있는 곳으로 나온 List<Position>에서 target이 포함되는 지 체크하기
- [유효성 검증] 2칸을 이동하는 거라면 처음 움직인 건지 체크하기 -> 처음 움직인 게 아니면
- [유효성 검증] 이동하려는 곳이 Empty가 아니라면, source와 target이 다른 색깔이어야만 한다.

---

## 3단계

- [x] 왕 잡으면 종료되는 기능
- [x] Status: (말이 잡힐때마다 업데이트)
- [x] 승패여부 및 점수 관리(3단계 이후)
- [x] 플레이어가 자신의 말만 옮길 수 있는 유효성 검증 넣기

---