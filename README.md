### ✏️ Rule Rule
1. Issue, PR 시 Assignees를 할당한다. (누가 작업했는지 알 수 있게)
2. 작업 시작 전 Issue 생성 (Issue template 사용)
3. 브랜치명은 다음과 같이 사용한다.
    - 새로운 기능 개발 : feat/#이슈번호, ex) `feat/#1`
    - 오류 수정 : fix/#이슈번호, ex) `fix/#1`
    - 기타 : 상의 후 결정
4. 작업 완료 후 PR 생성 (main 브랜치에 자동 squash merge)
    - close, refs 등을 사용하여 Issue와 연결이 가능하다.
    - ex) close #1, refs #1
    - `close #1` 은 Issue 번호가 1번인 것과 연결되며 자동으로 Issue가 닫히게 된다.
    - `refs #1` 은 Issue 번호가 1번인 것과 연결되며 자동으로 Issue가 닫히지는 않는다.
5. main 브랜치에 merge 후 작업했던 branch는 삭제한다. (개발이 완료된 것으로 간주하기 위함)

<br>

### 🚨 Important (main 브랜치에 merge 전)
1. 로컬 서버에서는 `http://localhost:8080/api경로` 로 테스트한다.
2. 반드시 로컬에서 동작하는 것을 확인한다. 
3. 배포 서버에 반영되는 데에는 약 1분 정도 소요된다.
4. 배포 서버에 제대로 반영이 안되거나 문제가 생기면 바로 서로에게 알린다.
