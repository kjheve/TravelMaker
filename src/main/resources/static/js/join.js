
// 체크박스 변수 --------------------------------------------------------------------------------
const $allCheckbox = document.querySelector('.checkmsg.all input[type="checkbox"]');         // 전체선택 체크박스
const $checkboxes = document.querySelectorAll('.checkmsg:not(.all) input[type="checkbox"]'); // 전체선택빼고 체크박스

// "전체 동의하기" 체크박스 클릭 시 모든 체크박스 선택/해제
$allCheckbox.addEventListener('change', () => {
  $checkboxes.forEach((checkbox) => {
    checkbox.checked = $allCheckbox.checked;
  });
});
// ------------------------------------------체크박스 끝--------------------------------------------
const $essentialCheckboxes = document.querySelectorAll('.checkmsg.essential input[type="checkbox"]');     // 필수 약관 체크박스 요소들
const $submitButton = document.querySelector('.submit_btn>input[type="submit"]');                        // 제출 버튼

// "전체 동의하기" 체크박스 클릭 시 모든 체크박스 선택/해제
$allCheckbox.addEventListener('change', () => {
  $checkboxes.forEach((checkbox) => {
    checkbox.checked = $allCheckbox.checked;
  });
  updateSubmitButtonStatus(); // 모든 체크박스 상태 변경 후 회원가입 버튼 상태 업데이트
});

// 필수 체크박스 상태 변경 시 실행될 함수
function updateSubmitButtonStatus() {
  let allEssentialsChecked = true; // 모든 필수 체크박스가 체크되었는지 여부 초기화
  $essentialCheckboxes.forEach((checkbox) => {
    // 하나라도 체크되지 않았다면 allEssentialsChecked를 false로 설정
    if (!checkbox.checked) {
      allEssentialsChecked = false;
    }
  });

  // 모든 필수 체크박스가 체크되었다면 회원가입 버튼 활성화, 아니면 비활성화
  $submitButton.disabled = !allEssentialsChecked;
}

// 모든 필수 체크박스에 대해 change 이벤트 리스너 추가
$essentialCheckboxes.forEach((checkbox) => {
  checkbox.addEventListener('change', updateSubmitButtonStatus);
});

// 페이지 로딩 시 한 번 실행하여 초기 상태 반영
updateSubmitButtonStatus();
// ------------------------------------------회원가입버튼 활성화 끝--------------------


//---------- 아이디 중복 체크 시작 ----------
// 폼 제출 이벤트 리스너 추가
frm.addEventListener('submit', evt => {
  evt.preventDefault(); // 폼 제출 방지

  // 아이디 중복 체크 확인
  if (!memberId.dataset.dupchk || memberId.dataset.dupchk === 'ok') {
    errmsg_id.textContent = '아이디 중복체크를 해주세요!';
    return;
  }

  // 폼 제출
  evt.currentTarget.submit();
});

// 중복 체크 버튼 클릭 이벤트 리스너 추가
dupchkBtn.addEventListener('click', evt => {
  console.log('중복 체크');

  // 아이디 입력값 확인
  if (memberId.value.trim().length === 0) {
    errmsg_id.textContent = '아이디를 입력해주세요.';
    memberId.focus();
    return;
  }

  // 중복 체크 수행
  dupchk();
});

// 아이디 입력란 값 변경 이벤트 리스너 추가
memberId.addEventListener('input', () => {
  // 중복 체크 상태 초기화
  memberId.dataset.dupchk = 'nok';
  errmsg_id.textContent = '';
  dupchkBtn.disabled = false;
});

// 중복 체크 함수
async function dupchk() {
  const $memberId = document.getElementById('memberId');
  const memberIdValue = $memberId.value.trim();

  // 중복 체크 요청 데이터 생성
  const data = { memberId: memberIdValue };

  const url = `/api/members/dupchk`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'accept': 'application/json',
    },
    body: JSON.stringify(data)
  };

  try {
    const response = await fetch(url, options);
    const msgBody = await response.json();

    // 중복 체크 결과에 따른 처리
    if (msgBody.header.rtcd === '21') {
      console.log('ID exists');
      errmsg_id.textContent = '이미 사용 중인 아이디입니다.';
      $memberId.dataset.dupchk = 'ok';
      $memberId.focus();
    } else {
      console.log('ID does not exist');
      $memberId.dataset.dupchk = 'nok';
      errmsg_id.textContent = '사용 가능한 아이디입니다.';
    }
  } catch (error) {
    console.error('Error:', error);
    alert('서버와의 통신 중 오류가 발생했습니다.');
  }
}


//---------- 이메일 중복 체크 시작 ----------
// 이메일 입력란 값 변경 이벤트 리스너 추가
email.addEventListener('input', () => {
  // 중복 체크 상태 초기화
  email.dataset.dupchk = 'nok';
  errmsg_email.textContent = '';
  dupchkBtn.disabled = false;
});

// 중복 체크 함수 (이메일)
async function dupchkEmail() {
  const $email = document.getElementById('email');
  const emailValue = $email.value.trim();

  // 중복 체크 요청 데이터 생성
  const data = { email: emailValue };

  const url = `/api/members/dupchkEmail`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'accept': 'application/json',
    },
    body: JSON.stringify(data)
  };

  try {
    const response = await fetch(url, options);
    const msgBody = await response.json();

    // 중복 체크 결과에 따른 처리
    if (msgBody.header.rtcd === '21') {
      console.log('Email exists');
      errmsg_email.textContent = '이미 사용 중인 이메일입니다.';
      $email.dataset.dupchk = 'ok';
      $email.focus();
    } else {
      console.log('Email does not exist');
      $email.dataset.dupchk = 'nok';
      errmsg_email.textContent = '사용 가능한 이메일입니다.';
    }
  } catch (error) {
    console.error('Error:', error);
    alert('서버와의 통신 중 오류가 발생했습니다.');
  }
}

// 중복 체크 버튼 클릭 이벤트 리스너 추가 (이메일)
dupchkBtnEmail.addEventListener('click', () => {
  console.log('Email Duplication Check');
  const emailValue = email.value.trim();

  // 이메일 입력값 확인
  if (emailValue.length === 0) {
    errmsg_email.textContent = '이메일을 입력해주세요.';
    email.focus();
    return;
  }

  // 중복 체크 수행
  dupchkEmail();
});

//---------- 닉네임 중복 체크 시작 ----------
// 닉네임 입력란 값 변경 이벤트 리스너 추가
nickname.addEventListener('input', () => {
  // 중복 체크 상태 초기화
  nickname.dataset.dupchk = 'nok';
  errmsg_nickname.textContent = '';
  dupchkBtnNickname.disabled = false;
});

// 중복 체크 함수 (닉네임)
async function dupchkNickname() {
  const $nickname = document.getElementById('nickname');
  const nicknameValue = $nickname.value.trim();

  // 중복 체크 요청 데이터 생성
  const data = { nickname: nicknameValue };

  const url = `/api/members/dupchkNickname`;
  const options = {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      'accept': 'application/json',
    },
    body: JSON.stringify(data)
  };

  try {
    const response = await fetch(url, options);
    const msgBody = await response.json();

    // 중복 체크 결과에 따른 처리
    if (msgBody.header.rtcd === '21') {
      console.log('Nickname exists');
      errmsg_nickname.textContent = '이미 사용 중인 닉네임입니다.';
      $nickname.dataset.dupchk = 'ok';
      $nickname.focus();
    } else {
      console.log('Nickname does not exist');
      $nickname.dataset.dupchk = 'nok';
      errmsg_nickname.textContent = '사용 가능한 닉네임입니다.';
    }
  } catch (error) {
    console.error('Error:', error);
    alert('서버와의 통신 중 오류가 발생했습니다.');
  }
}

// 중복 체크 버튼 클릭 이벤트 리스너 추가 (닉네임)
dupchkBtnNickname.addEventListener('click', () => {
  console.log('Nickname Duplication Check');
  const nicknameValue = nickname.value.trim();

  // 닉네임 입력값 확인
  if (nicknameValue.length === 0) {
    errmsg_nickname.textContent = '닉네임을 입력해주세요.';
    nickname.focus();
    return;
  }

  // 중복 체크 수행
  dupchkNickname();
});