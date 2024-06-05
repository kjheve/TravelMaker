frm.addEventListener('submit', e => {
        e.preventDefault(); //기본 이벤트 막기
        //유효성 체크로직
        //1) 아이디 : 필수, 50자이하, 이메일형식
        if (memberId.value.trim().length == 0 || memberId.value.trim().length >= 12) {
          errMemberId.textContent = '아이디 입력은 필수입니다(12자 이하)';
          memberId.focus();
          return;
        } else {
          errMemberId.textContent = '';
        }


        //2) 비밀번호 : 필수
        if (pw.value.trim().length == 0) {
          errPw.textContent = '비밀번호 입력은 필수 입니다.';
          pw.focus();
          return;
        } else {
          errPw.textContent = '';
        }

        frm.submit();
      });

//      document.getElementById('frm').addEventListener('submit', function(event) {
//        const memberId = document.getElementById('memberId').value;
//        const pw = document.getElementById('pw').value;
//
//        if (!memberId.trim()) {
//          document.getElementById('errMemberId').textContent = '아이디를 입력해주세요.';
//          event.preventDefault();
//        } else {
//          document.getElementById('errMemberId').textContent = '';
//        }
//
//        if (!pw.trim()) {
//          document.getElementById('errPw').textContent = '비밀번호를 입력해주세요.';
//          event.preventDefault();
//        } else {
//          document.getElementById('errPw').textContent = '';
//        }
//      });
