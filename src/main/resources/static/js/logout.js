function logout() {
    fetch('/logout', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({})
    })
    .then(response => response.text())
    .then(result => {
        if (result === 'OK') {
            window.location.href = '/login';
        } else {
            alert('로그아웃 실패');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        alert('로그아웃 중 오류 발생');
    });
}
