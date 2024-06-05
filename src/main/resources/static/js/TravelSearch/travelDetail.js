document.addEventListener("DOMContentLoaded", function () {
	// travelId 변수를 사용하여 작업 수행
	console.log("trvlCd :", trvlCd);

	const serviceKey =
		"HJanR2yzlPBEX%2BZ6TJ2smY%2Bo%2BWAyAazxDwi0sZsBF3AV%2BOpWPsBs%2Bcl7%2FKRr1A9o5e9B15ypLI3FqAZzm%2BIkKQ%3D%3D";
	const apiUrl = `http://apis.data.go.kr/B551011/KorService1/detailCommon1?serviceKey=${serviceKey}&contentId=${trvlCd}&defaultYN=Y&firstImageYN=Y&addrinfoYN=Y&overviewYN=Y&MobileOS=ETC&MobileApp=AppTest&_type=json`;

	fetch(apiUrl)
		.then((response) => {
			if (!response.ok) {
				throw new Error("에러발생");
			}
			return response.json();
		})
		.then((data) => {
			//  세부데이터 받아오는데 성공 하면 fetch로 json 보내고 페이지 생성하기
			return data["response"]["body"]["items"]["item"];
		})
		.then((data) => {
			insertDataIntoElement(data);
		})
		.catch((error) => {
			console.error("Fetch error:", error);
		});
});

// html에 공공데이터 띄우기
function insertDataIntoElement(data) {
	const container = document.querySelector(".data_container");
	console.log(data);
	console.log(data.entries);
	console.log(data.entries.length);
	// 데이터가 없는 경우 처리
	if (!data.length || data.length === 0) {
		container.innerHTML = "<p>No data available</p>";
		return;
	}

	// 데이터를 HTML 엘리먼트로 변환하여 삽입
	data.forEach((entry) => {
		const entryElement = document.createElement("div");
		entryElement.className = "entry";
		entryElement.innerHTML = `
            <p class="app-title mo-show">
            <h2 class="align-left">${entry.title}</h2>
            </p>
            <div class="half-wrap">
                <div class="big-img">
                <img src="${entry.firstimage || "/img/main_logo.png"}" alt="${
			entry.title
		}">
                </div>
                <div class="right-wrap">
                    <div class="right-info">
                        <p>기본정보</p>
                    </div>
                    <table class="table-type1" id="table1" tabindex="-1">
                        <tbody>
                            <tr>
                                <td class="col30 th">우편번호</td>
                                <td>
                                    ${entry.zipcode}
                                </td>
                            </tr>
                            </tr>
                            <tr>
                                <td class="th">주소</td>
                                <td>${entry.addr1}</td>
                            </tr>
                            <tr>
                                <td class="th">홈페이지</td>
                                <td>
                                    <div class="ft-blue2">${
										entry.homepage
									}</div>
                                </td>
                            </tr>
                        </tbody>
                    </table>

                </div>
            </div>
            <div class="bot">
                <div>개요</div>
                <div>${entry.overview}</div>
            </div>
    `;

		container.appendChild(entryElement);
	});
}
