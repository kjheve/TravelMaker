// 로그인세션 managementId
let managementId = [[${session.loginMember.managementId}]]
let nickname = [[${session.loginMember.nickname}]]

// 조회수, 좋아요
let hit = [[${bbs.hit}]];
let good = [[${bbs.good}]];
console.log(hit);
console.log(good);

// 글 번호
let bbsId = [[${bbsId}]];
console.log(bbsId);

// 게시판 목록 버튼
document.addEventListener('DOMContentLoaded', function () {
  document.getElementById('listBtn').addEventListener('click', function () {
    location.href = `/board`;
  });
});

// 게시판 수정 버튼
document.addEventListener('DOMContentLoaded', function () {
// "clickable-title" 클래스를 가진 모든 요소에 대해 함수를 실행합니다.
  document.getElementById('editBtn').addEventListener('click', function () {
    location.href = `/board/${bbsId}/edit`;
  });
});

 // 게시판 삭제 버튼
 document.addEventListener('DOMContentLoaded', function () {
// "clickable-title" 클래스를 가진 모든 요소에 대해 함수를 실행합니다.
  document.getElementById('delBtn').addEventListener('click', function () {
    fetch(`/board/${bbsId}/del`, {
      method: 'POST', // POST 요청을 명시
    }).then(response => {
      // 요청이 성공적으로 처리되면 게시판 목록 페이지로 리다이렉트
      if (response.ok) {
        console.log('굳')
        location.href = '/board';
      }
    }).catch(error => {
      // 에러 처리
      console.error('Error:', error);
    });
  });
});

// 댓글 개수
const commentCount = document.querySelectorAll('.commentItem').length;

// 통계 정보 업데이트
const statsElement = document.getElementById('stats');
statsElement.innerHTML = `조회 ${hit} 좋아요 ${good} 댓글 ${commentCount}`

// 날짜 포맷팅 함수 정의
function formatCommentDate(cdate) {
  return new Date(cdate).toLocaleString('ko-KR', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    hour12: false
  });
}


// 등록
document.getElementById('addForm').addEventListener('submit', async function (evt) {

  evt.preventDefault();
  // 댓글 입력 폼
  let rbbsText = document.getElementById('textbox').value;
  console.log(rbbsText);
  const url = `http://localhost:8080/board/${bbsId}/comment`;
  const payload = {
    bbsId: bbsId,
    managementId: managementId,     // 로그인 세션 managementId
    nickname: nickname,             // 로그인 세션 nickname
    bContent: rbbsText
  };
  const option = {
    method: 'POST',
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify(payload),
  };
  try {
    const res = await fetch(url, option);
    if (!res.ok) throw new Error('서버 오류 발생');
    const result = await res.json();
    // 성공
    if (result.header && result.header.rtcd == '00') {
      document.getElementById('textbox').value = '';
      await loadComments(bbsId);
    } else {
      throw new Error(result.header.errMsg || '댓글 등록 실패!');
    }
  } catch (err) {
    console.error(err.message);
    alert(err.message);
  }
});

// 페이지 로드 시 댓글 리스트를 불러옴
document.addEventListener('DOMContentLoaded', function () {
  loadComments(bbsId);
});

async function loadComments(bbsId) {
  try {
    const response = await fetch('/board/' + bbsId + '/comment');
    if (!response.ok) {
      throw new Error('네트워크 응답이 올바르지 않습니다.');
    }
    const data = await response.json();
    console.log(data);
    let commentsHtml = '';
    data.forEach(function (comment) {
      const formattedDate = formatCommentDate(comment.cdate);
      commentsHtml += `
            <div id="comment-${comment.rbbsId}" "class="comment_option">
              <ul class="comment_list">
                <li type="hidden" id="${comment.rbbsId}" class="commentItem">
                  <div class="comment_box">
                    <div class="nick_box">
                      <a class="nick_info">
                        ${comment.nickname}
                      </a>
                    </div>
                    <div class="text_box">
                      <div class="comment_date">
                        ${formattedDate}
                      </div>
                      <div class="text_comment">${comment.bContent}</div>
                    </div>
                  </div>
                </li>
              </ul>`;
      if (comment.managementId == managementId)
      commentsHtml +=
           `<div class="Btns" data-id="${comment.rbbsId}">
              <button class="editBtn" data-id="${comment.rbbsId}">수정</button>
              <button class="deleteBtn" data-id="${comment.rbbsId}">삭제</button>
            </div>`;
      commentsHtml +=  `</div>`
    });
    document.getElementById('comments').innerHTML = commentsHtml;

    // 댓글 수정 버튼 클릭 이벤트 리스너 추가
    document.querySelectorAll('.editBtn').forEach(function (button) {
      button.addEventListener('click', function () {
        let commentId = this.parentElement.getAttribute('data-id');
        editComment(commentId);
      });
    });

    // 댓글 삭제 버튼 클릭 이벤트 리스너 추가
    document.querySelectorAll('.deleteBtn').forEach(function (button) {
      button.addEventListener('click', function () {
        let commentId = this.parentElement.getAttribute('data-id');
        deleteComment(commentId);
      });
    });
  } catch (error) {
    console.error('댓글 목록을 가져오는 데 실패했습니다.', error);
  }
}

function editComment(commentId) {
  console.log("수정버튼누름");

  const commentDiv = document.getElementById(`comment-${commentId}`);

  const buttonsDiv = commentDiv.querySelector('.Btns');

  const editBtn = buttonsDiv.querySelector('.editBtn');
  const deleteBtn = buttonsDiv.querySelector('.deleteBtn');
  const text_comment = commentDiv.querySelector('.text_comment');

  console.log(text_comment);


  // 숨김 처리
  text_comment.style.display = 'none';
  editBtn.style.display = 'none';
  deleteBtn.style.display = 'none';

  // textarea와 저장, 취소 버튼 생성
  const textarea = document.createElement('textarea');
  textarea.value = text_comment.textContent;
  textarea.classList.add('form-control');

  const saveBtn = document.createElement('button');
  saveBtn.textContent = '저장';
  saveBtn.classList.add('btn', 'btn-success','btn-sm');

  const cancelBtn = document.createElement('button');
  cancelBtn.textContent = '취소';
  cancelBtn.classList.add('btn','btn-secondary','btn-sm');

  // textarea와 버튼들 추가
  text_comment.parentNode.insertBefore(textarea, text_comment.nextSibling);
  buttonsDiv.appendChild(saveBtn);
  buttonsDiv.appendChild(cancelBtn);

  // 저장버튼
  saveBtn.addEventListener('click', function() {
  let newContent = textarea.value.trim();
  if (newContent != null) {
    fetch('/board/' + bbsId + '/comment/' + commentId, {
      method: 'PATCH',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ bContent: newContent })
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('댓글 수정에 실패했습니다.');
        }
        return response.json();
      })
      .then(data => {
        alert('댓글이 성공적으로 수정되었습니다.');
        loadComments(bbsId); // 페이지를 새로고침하여 변경된 내용을 반영
      })
      .catch(error => {
        console.error('댓글 수정 중 오류가 발생했습니다.', error);
      });
    }
  })

  // 취소버튼
  cancelBtn.addEventListener('click', function() {
    // 수정 전 상태로 복원
    textarea.remove(); // textarea 제거
    saveBtn.remove(); // 저장 버튼 제거
    cancelBtn.remove(); // 취소 버튼 제거

    // 원래의 댓글 및 버튼들을 다시 표시
    text_comment.style.display = '';
    editBtn.style.display = '';
    deleteBtn.style.display = '';
  });

}

function deleteComment(commentId) {
  console.log(commentId);
  if (confirm('정말 이 댓글을 삭제하시겠습니까?')) {
    fetch('/board/' + bbsId + '/comment/' + commentId, {
      method: 'DELETE'
    })
      .then(response => {
        if (!response.ok) {
          throw new Error('댓글 삭제에 실패했습니다.');
        }
        alert('댓글이 성공적으로 삭제되었습니다.');
        loadComments(bbsId); // 페이지를 새로고침하여 변경된 내용을 반영
      })
      .catch(error => {
        console.error('댓글 삭제 중 오류가 발생했습니다.', error);
      });
  }
}