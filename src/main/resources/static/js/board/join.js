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