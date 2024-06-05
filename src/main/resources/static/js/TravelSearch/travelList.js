document.addEventListener('DOMContentLoaded', () => {
    let isRequestInProgress = false;
    // 모든 travel_item 요소를 가져옵니다.
    const travelItems = document.querySelectorAll('.travel_item');
    //모든 tag아이디를 가진 엘리먼트 가져오기
    const tags = document.querySelectorAll('.tag');
    //모든 trvl_type아이디를 가진 엘리먼트 가져오기
    const types = document.querySelectorAll('.trvl_type');

    //여행지상세조회 선택한 item의 trvlcd를 가지고 여행지 상세정보 페이지 구성
    travelItems.forEach((item) => {
        // 각 travel_item 요소에 클릭 이벤트 리스너를 추가합니다.
        item.addEventListener('click', () => {
            // data-tvlspot-id 속성의 값을 가져옵니다.
            const trvlcd = item.getAttribute('data-trvlspot-id');
            console.log(`Travel Spot ID: ${trvlcd}`);

            location.href = `/travelSearch/${trvlcd}/detail`;
            // fetchPublicData(trvlcd);
        });
    });

    //지역선택(복수선택가능)
    tags.forEach((tag) => {
        tag.addEventListener('click', (item) => {
            // 'active' 클래스가 없는 경우에는 'active' 클래스를 추가하고 백그라운드 컬러를 변경
            if (!item.currentTarget.classList.contains('active')) {
                item.currentTarget.classList.add('active');
                // 여기서 다른 스타일도 추가할 수 있습니다.
            } else {
                // 'active' 클래스가 있는 경우에는 'active' 클래스를 제거하고 백그라운드 컬러를 기본값으로 변경
                item.currentTarget.classList.remove('active');
                // 여기서 다른 스타일을 초기화할 수 있습니다.
            }
        });
    });
    // 여행타입선택(복수선택가능)
    types.forEach((type) => {
        type.addEventListener('click', (evt) => {
            // 'active' 클래스가 없는 경우에는 'active' 클래스를 추가하고 백그라운드 컬러를 변경
            if (!evt.currentTarget.classList.contains('active')) {
                evt.currentTarget.classList.add('active');
                // 여기서 다른 스타일도 추가할 수 있습니다.
            } else {
                // 'active' 클래스가 있는 경우에는 'active' 클래스를 제거하고 백그라운드 컬러를 기본값으로 변경
                evt.currentTarget.classList.remove('active');
                // 여기서 다른 스타일을 초기화할 수 있습니다.
            }
        });
    });
    const submitBtn = document.querySelector('.input_button');
    // 버튼 클릭 시 선택된 태그 값 수집하여 폼 제출
    submitBtn.addEventListener('click', function (event) {
        event.preventDefault();

        if (isRequestInProgress) return; // 요청이 진행 중이면 중복 요청 방지

        isRequestInProgress = true; // 요청 시작

        let selectedTags = [];
        let selectedTypes = [];

        tags.forEach((tag) => {
            if (tag.classList.contains('active')) {
                selectedTags.push(tag.getAttribute('data-code'));
            }
        });

        types.forEach((type) => {
            if (type.classList.contains('active')) {
                selectedTypes.push(type.textContent.trim());
            }
        });
        // selectedTags = document.querySelectorAll("#tag .active");
        // selectedTypes = document.querySelectorAll("#trvl_cd .active");
        console.log(selectedTags);
        console.log(selectedTypes);
        console.log(document.querySelector('.input_text').value);
        // 수집한 태그 값을 폼에 추가
        document.getElementById('tagValues').value =
            JSON.stringify(selectedTags);
        document.getElementById('typeValues').value =
            JSON.stringify(selectedTypes);
        document.getElementById('keyword').value =
            document.querySelector('.input_text').value;

        // reqPage, recCnt, cpgs, cp 초기화
        document.querySelector("input[name='reqPage']").value = 1;
        document.querySelector("input[name='recCnt']").value = 16;
        document.querySelector("input[name='cpgs']").value = 1;
        document.querySelector("input[name='cp']").value = 1;

        // 폼 제출
        document.getElementById('searchForm').submit();
    });

    // Enter 키 입력 시 폼 제출
    document.addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
            submitBtn.click(); // 버튼 클릭 이벤트 발생
        }
    });

    setTimeout(() => {
        isRequestInProgress = false;
    }, 1000);

    // 리셋버튼 => 누르면 활성화된 태그 비활성화로 바뀌고 검색창에 입력된 데이터 ""으로 초기화
    document
        .querySelector('.refresh_btn')
        .addEventListener('click', function () {
            const activeElements = document.querySelectorAll('.active');
            // 각 요소에서 .active 클래스를 제거
            activeElements.forEach(function (element) {
                element.classList.remove('active');
            });
            document.querySelector('.input_text').value = '';
        });

    // 여행지 검색버튼 => 누르면 처음 여행지 검색창으로 돌아감
    document.querySelector('.reco').addEventListener('click', function () {
        location.href = '/travelSearch?reqPage=1&recCnt=16&cpgs=1&cp=1';
    });
});
