***
## 1. 프로젝트 정보

프로젝트 이름 : 1336

프로젝트 개요 : 소소하게 게임을 진행하고 포인트를 벌 수 있는 자그마한 게임 사이트.


![](https://velog.velcdn.com/images/lries7897/post/2bce67d2-0aad-4a73-9035-53ddb2b77b3f/image.png)

배포 사이트 : [1336[클릭]](http://1336.s3-website.ap-northeast-2.amazonaws.com/login)

***

## 2. 설계

### 와이어프레임

![](https://velog.velcdn.com/images/lries7897/post/582bb77d-ca5c-4223-bbfc-efd66bff3b60/image.png)

### api명세 

|기능|METHOD|URL|request|response|비고|
|---|---|---|---|---|---|
|회원가입|POST|/api/member/signup|{ "id": "asdf", "passWord": "asdf", "birthDate" : "20030903", "nickName": "asdf"}|{ ‘success’: true, ’data’ : nickName+‘님 회원가입 성공’ ”error” : null } OR { ‘success’: false, ’code’ : ‘SIGNUP_FAILED’ ’message’:’회원가입 실패’ }|   |
|닉네임중복검사|POST|/api/member/chkName|{"nickName" : "닉네임"|{ 'success':true, data :’사용가능한 닉네임입니다.’ ”error” : null } OR { ‘success’: false, ’code’ : “NICKNAME_DUPLICATE’ ’message’:’닉네임 중복’ }|   |
|아이디중복검사|POST|/api/member/chkId|{"id" : "회원아이디"}|{ 'success':true, ’data’ :’사용가능한 아이디입니다.’ ”error” : null } OR { ‘success’: false, ’code’ : “ID_DUPLICATE’ message:’아이디 중복’ }|   |
|성인인증|POST|/api/member/chkAdult|{"birthDate" : "19990101"}|   |   |
|로그인|POST|/api/member/login|{"id" : "아이디","passWord" : "비밀번호"}|{ 'success':true, ’data’ : 로그인한계정의 닉네임 ”error” : null } OR { ‘success’: false, ’code’ : “LOGIN_FAILED’ message:’로그인 실패’ }|   |
|로그아웃|POST|/api/logout|   |   |   |
|유저권한체크|GET|/api/roleCheck|{ 'success': true, data: true error:null }|   |   |
|전체랭킹|GET|/api/ranking|   |{ 'success' : true, 'data' : [ ””totalWinCountList” : [{"nickame" : 닉네임1, “totalWinCount” : 10, }, {"nickame" : 닉네임2, “totalWincount” : 14 }… {"nickame" : 닉네임30, “totalWincount” : 14 }], ””totalPointList" : [{"nickame" : 닉네임1, "totalPoint" : 획득포인트량, }, {"nickame" : 닉네임2, "totalPoint" : 획득포인트량… }]] 'error' : null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’랭킹 불러오기 실패’ }|   |
|게임별랭킹|GET|/api/ranking/{게임고유아이디}|   |{ 'success' : true, 'data' : [ ”gameTitle” : 게임이름 ”totalWinCountList” : [{"nickame" : 닉네임1, "point" : 승리횟수}, {"nickame" : 닉네임1, "point" : 승리횟수…}],” ”totalPointList" : [{nickame" : 닉네임1, "point" : 획득포인트량}, {"nickame" : 닉네임1, "point" : 획득포인트량…}] 'error' : null }] OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’랭킹 불러오기 실패’ }|   |
|홀짝게임|POST|/api/game/oddeven|{point : 걸어둔포인트number:홀(1) 또는(2)}|{ ”success”:true, ”data”: {”result” : , ”oddevenwincount” : , ”getpoint” : , ”nowpoint” : } ”error” : null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’게임 실행 오류“ }|   |
|주사위게임|POST|/api/game/dice|{point : 걸어둔포인트number : 선택한숫자}|{ ”success”:true, ”data”: {”result” : 주사위 눈금, ”dicewincount” : , ”getpoint” : , ”nowpoint” : } ”error” : null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’게임 실행 오류“ }|   |
|로또게임(제출)|POST|/api/game/lotto|{  "num1": 2,  "num2": 3,  "num3": 5,  "num4": 6,  "num5": 7,  "num6": 15}|{ "success": true, "data": "로또 구매완료!", "error": null }|   |
|로또게임(전체결과)|GET|/api/game/lottoresult|   |{ "success": true, "data": { "num": [7,9,2,5,10,3,6], "money1st": 15001500, "member1st": 0, "member2rd": 0, "member3nd": 0 }, "error": null }|1시간 단위로 정산<data 설명>num:당첨번호6개+보너스번호money : 1등 당첨 금액member{n}:n등 인원수  |
|로또게임(나의최근결과)|GET|/api/game/lottomyresult|   |{ "success": true, "data": [ { "no": 1, "luckyNum": [ 1, 5, 4, 3, 9, 2 ], "bonusNum": 6, "rank": 2, "earnPoint": 15003000, "myNum": [ 1, 2, 3, 4, 5, 6 ] }, { "no": 1, "luckyNum": [ 1, 5, 4, 3, 9, 2 ], "bonusNum": 6, "rank": 2, "earnPoint": 15003000, "myNum": [ 1, 2, 3, 4, 5, 6 ] } ], "error": null } or(한번도 구매를 안했을때) { "success": true, "data": "구매한 로또가 없습니다.", "error": null }|   |
|카운트게임|POST|/api/game/counter|{"count" : "77"}|{ ”success”:true, ”data”: {”nowcount” : , ”maxcount” : , ”getpoint” : , ”nowpoint” : } ”error” : null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’게임 실행 오류“ }|   |
|마이페이지보기|GET|/api/user|   |{ "success" : true ”data” : { “id” : "회원아이디" ”nickName” : 닉네임, "point" : "보유포인트" ”winCountOfOddEven” : “승수” ”winCountOfDice” : “승수” ”earnPointOfLotto” : “승수” ”highestCountOfCounter” : “승수” ”error” : null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’마이페이지 불러오기 오류“ }|   |
|내정보수정|PATCH|/api/user|{”nickName” : 닉네임}|{ "success" : true ”data” : { “id” : "회원아이디" ”nickName” : 변경 닉네임, "point" : "보유포인트" ”winCountOfOddEven” : “승수” ”winCountOfDice” : “승수” ”earnPointOfLotto” : “승수” ”highestCountOfCounter” : “승수” ”error” : null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’정보 수정 오류“ }|   |
|프로필사진업로드|PATCH|/api/user/Image|form-data {key:image,value:사진파일}|{ ”success” : true, ”data” : {”imageUrl“ : 이미지url } ”error” : null } OR { ‘success’: false, ’code’ : “UPLOAD_FAIL’ message:’이미지 업로드 실패“ }|   |
|사진url받기|GET|/api/user/Image|   |{ ”success” : true, ”data” : {”imageUrl“ : 이미지url } ”error” : null } OR { ”success” : true, ”data” : “이미지 url 반환실패” ”error” : null |   |
|회원탈퇴|DELETE|/api/user|   |{ ”success” : true, ”data” : “탈퇴완료” ”error” : null }|   |
|관리자페이지불러오기|GET|/api/adminPage|   |{ success : true data : { "id" : "회원아이디" "point" : "보유포인트", "id" : "회원아이디" "point" : "보유포인트", "id" : "회원아이디" "point" : "보유포인트"} errer : null } OR { ‘success’: false, ’code’ : “BAD_REQUEST_ADMIN’ message:’관리 페이지 불러오기 실패“ }|   |
|포인트지급|PATCH|/api/adminPage/{회원아이디}|{"point" : "포인트양"}|{ 'success': true, data: “회원아이디” + “포인트” + 지급완료 error:null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’포인트 지급 실패“ }|   |
|게임댓글작성|POST|/api/comment|{"content" : "댓글내용"}|{ 'success': true, data: {id: 댓글아이디, nickname: 닉네임, content : "댓글내용"}, error:null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’댓글 작성 실패“ }|<게임 고유 아이디>1. 홀짝  2. 주사위 3. 로또 4. 카운트<두번째인자> 댓글 고유 id/ /localhost:8080/api/comment?gameId=1|
|게임댓글수정|PATCH|/api/comment|{"content" : "댓글내용"}|{ 'success': true, data: 수정완료}, error:null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’댓글 수정 실패“ }|/localhost:8080/api/comment?id=1|
|게임댓글보기|GET|/api/comment|   |{ 'success': true, data: “삭제완료”, error:null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’댓글 삭제 실패“ }|/localhost:8080/api/comment?id=1|
|게임댓글삭제|DELETE|/api/comment|   |{ 'success': true, data: [{ id : “댓글id”, nickname: “닉네임”, content: “내용”},{ nickname: “닉네임”, content: “내용”}], error:null } OR { ‘success’: false, ’code’ : “BAD_REQUEST’ message:’댓글 불러오기 실패“ }|/localhost:8080/api/comment?gameId=1|

## 3.구현설명 및 트러블슈팅

### 몇가지 구현설명 

#### 카운터, 홀짝, 주사위 구현방식
**카운터** : 프론트에서 넘어 온 값에 배율을 곱해서 db에 저장하는 방식
**홀짝, 주사위** : 유저가 입력한 포인트 값이 자기가 가진 포인트값보다 크거나 또는 가진포인트가 0인지 확인
정상적이라면 배팅한만큼의 포인트를 차감함.
**홀짝** : 1,2를 랜덤 생성 1이면 홀 2면 짝으로 계산하여 맞추기를 성공한 회원에게 배율에 따른 포인트를 지급함.
카운터 1~6을 랜덤생성 맞추기를 성공한 회원에게 배율에 따른 포인트를 지급함.

#### 마이페이지
마이페이지는 해당 유저의 게임 승리수와, 카운터최대횟수와 로또당첨금액을 보여줌
마이페이지에서 이미지 업로드를 수행할 수 있도록 이미지 업로드 api를 구현함 현재 jpg,jpeg,png,bmp만 올라갈 수 있도록 서버 설정되어있음
닉네임 업데이트시에 닉네임이 변경됨에 따라 기존의 인증토큰들이 유효하지 않게 되므로 인증토큰을 새로 발급할 수 있도록 구현되어있음.

#### 랭킹시스템 서치 구현방식
랭킹페이지에서는 전체 랭킹들과 게임별 랭킹을 확인할 수 있음.
전체랭킹은 모든 유저들의 포인트양과 총 승리횟수를 볼 수 있게 되어있음. db에서는 각 유저별 전적 테이블에서 승리횟수 벌어들인 포인트양등을 검색해서옴.
게임별 랭킹은 게임별로 포인트 획득량 승리수, 최대 클릭횟수등을 db에서 검색해와 확인 할 수 있게 구현함.

### 테이블 개선 및 트러블 슈팅

#### 댓글시스템 구현방식
댓글시스템은 처음 api설계당시 각 게임별 댓글crud를 작성해야해서 controller에 16개의 메소드가 작성되게 됨. 
처음 api설계당시 하나의 댓글테이블을 생성하는 것을 고려해보았지만 댓글이 많아질 경우를 대비하여 맨 처음 api를 사용하였지만 유저수가 많지 않음을 생각하고 다시 api를 수정함. 
그래서 2번의 수정을 거침

1. string query를 이용하여 두개의 파라미터를 받아 올 수 있도록 해서 controller에 메소드를 4개로 줄여서 구현함. 서비스는 switch문을 활용하여 각 게임타입에 맞는 댓글테이블에 댓글이 작성될 수 있도록.
2. 댓글 테이블을 통합하고 댓글 타입 필드를 댓글테이블에 생성함. 코드가 훨씬 간결해짐.

#### 회원탈퇴시 탈퇴된 회원 정보 노출하는 방법
처음 서버가 실행될 때 applicationrunner를 사용하여 기본적으로 탈퇴된 회원입니다 라는 회원하나를 만들게 됨. 
회원 탈퇴가 진행되면 리프레시토큰을 지우며 관련된 전적테이블과 자신이 산 로또들은 함께 삭제되지만 해당 사용자가 작성한 댓글은 댓글의 작성자가 탈퇴된 회원으로 교체되게 됨.
결과적으로 1개의 유저객체로 모든 탈퇴된 사용자의 댓글을 관리할 수 있게됨.
추가적으로 서버가 실행될 때 로또1회차에 대한 정보가 먼저 필요하므로 로또 1회차 테이블의 객체를 한개 생성함. 

### cors 정책 트러블슈팅

#### alloworigin처리
프론트와의 작업을 합치는 과정에서 배포된 서버에 프론트가 접근거부를 당함.
두가지 방법을 구현하여음 filter를 생성하여 allow들의 처리, webMvcConfigurer를 생성하는 것
최종적으로 생성 어플리케이션이 실행되는 부분에 @bean파일로 webmvcconfigurer를 구현하고 프론트엔드파트의 작업환경과 배포사이트출처를 허용해줌.

#### exposeheader처리
서버에서 정상적으로 보내지고 있는 인증토큰 헤더부분이 보이지 않았음.
configurer에서 exposeHeader 설정을 하여 해결함.
method와 data type에 따른 cors 시나리오에 맞춘 처리방식
이미지 업로드를 할때 cors정책 위반관련해서 이슈가 생김. 
cors정책에서 credentials관련 이슈인 것으로 확인하고 해당 값을 true로 설정한 후 option과 필요한 METHOD, HEADER들을 허용함.

#### 자동 재배포 관련 이슈
깃허브의 main브랜치에 머지가 되면 자동으로 ec2서버에 새로 빌드된 실행파일이 들어가고 재실행되는 워크플로우를 작성함.
하지만 서버가 재 실행되지 않아서 몇가지를 실험 한 후 파일은 제대로 ec2에 올라가지만 실행되지 않는것을 확인.
gitignore처리된 aws인증키 및 민감정보가 없어서 실행오류가 발생함을 확인하고 해당파일을 참조 할 수 있게 수정함.
