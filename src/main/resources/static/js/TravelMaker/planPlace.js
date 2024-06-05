// 전역 변수로 map 선언
var map = null;

// 지도 생성 초기화 함수
function initializeMap() {
   var centerCoords;
   var zoom;

   switch (document.getElementById("planRagion").textContent) {
      case "서울특별시":
         centerCoords = new naver.maps.LatLng(37.5729, 126.9794);
         zoom = 11;
         break;
      case "부산광역시":
         centerCoords = new naver.maps.LatLng(35.1068, 129.0312);
         zoom = 11;
         break;
      case "대구광역시":
         centerCoords = new naver.maps.LatLng(35.8683, 128.5988);
         zoom = 11;
         break;
      case "인천광역시":
         centerCoords = new naver.maps.LatLng(37.4643, 126.5904);
         zoom = 10;
         break;
      case "광주광역시":
         centerCoords = new naver.maps.LatLng(35.1399, 126.9194);
         zoom = 12;
         break;
      case "대전광역시":
         centerCoords = new naver.maps.LatLng(36.3515, 127.4239);
         zoom = 12;
         break;
      case "울산광역시":
         centerCoords = new naver.maps.LatLng(35.5664, 129.319);
         zoom = 11;
         break;
      case "세종특별자치시":
         centerCoords = new naver.maps.LatLng(36.4801, 127.2889);
         zoom = 11;
         break;
      case "경기도":
         centerCoords = new naver.maps.LatLng(37.2636, 127.0286);
         zoom = 10;
         break;
      case "강원도":
         centerCoords = new naver.maps.LatLng(37.8816, 127.7291);
         zoom = 9;
         break;
      case "충청북도":
         centerCoords = new naver.maps.LatLng(36.6419, 127.4898);
         zoom = 9;
         break;
      case "충청남도":
         centerCoords = new naver.maps.LatLng(36.8145, 127.1469);
         zoom = 9;
         break;
      case "전라북도":
         centerCoords = new naver.maps.LatLng(35.8242, 127.1489);
         zoom = 9;
         break;
      case "전라남도":
         centerCoords = new naver.maps.LatLng(34.812, 126.3917);
         zoom = 9;
         break;
      case "경상북도":
         centerCoords = new naver.maps.LatLng(36.0194, 129.3434);
         zoom = 9;
         break;
      case "경상남도":
         centerCoords = new naver.maps.LatLng(35.2372, 128.6811);
         zoom = 9;
         break;
      case "제주특별자치도":
         centerCoords = new naver.maps.LatLng(33.4996, 126.5312);
         zoom = 11;
         break;
      default:
         // 기본 값은 서울특별시로 설정
         centerCoords = new naver.maps.LatLng(37.5729, 126.9794);
         zoom = 11;
   }

   // 지도 생성 옵션
   var mapOptions = {
      center: centerCoords, // 맵의 초기값
      mapDataControl: false, // 맵 출처 숨김
      zoomControl: true,
      zoomControlOptions: {
         position: naver.maps.Position.TOP_RIGHT
      },
      zoom: zoom,
      minZoom: 8,
      maxZoom: 14,
   };

   // 지도 생성
   map = new naver.maps.Map("map", mapOptions);
}

// --------------------------------------------------------
// DOMContentLoaded 시작 ----- ★
document.addEventListener("DOMContentLoaded", function () {
	// 지도가 이미 초기화되어 있지 않다면 초기화 진행
	if (!map) {
		initializeMap();
	}

	// 각 일차별로 마커를 관리하기 위한 객체 생성
	const markersByDay = {};

	// 각 일차별로 폴리라인을 관리하기 위한 객체 생성
	const polylinesByDay = {};

	// 음식 선택해야지 하위 코드 보이게  -----------------------------
	const cateSelect = document.querySelector('select[name="cate_select"]');
	const foodSelect = document.querySelector('select[name="food_select"]');

	function toggleFoodSelect() {
		// '음식'이 선택되면 .foot_select를 보이게 함
		if (cateSelect.value === "A07") {
			foodSelect.style.display = "block";
		} else {
			foodSelect.style.display = "none";
		}
	}
	// cateSelect의 값이 변경될 때 실행됨
	cateSelect.addEventListener("change", toggleFoodSelect);
	// 페이지 로드 시 '음식'이 이미 선택되어 있는지 확인 및 적용
	toggleFoodSelect();
	// ----------------------------------------------------------

	// XX일차 탭박스 -------------시작
	// 모든 탭 버튼을 선택
	var tabs = document.querySelectorAll('input[name="tabmenu"]');

	function changeTab() {
		// 모든 탭 내용을 숨김
		var allConboxes = document.querySelectorAll(".conbox");
		for (var i = 0; i < allConboxes.length; i++) {
			allConboxes[i].classList.remove("active");
		}

		// 현재 선택된 탭의 인덱스에 해당하는 탭 내용만 보여줌
		var index = this.id.replace("tab0", "") - 1;
		document.querySelectorAll(".conbox")[index].classList.add("active");

    // 일차 누를 때 마다 지도 초기화
    initializeMap();
		// 선택된 일차에 맞는 마커와 폴리라인만 표시
		updateMarkersAndPolyline(index + 1);
	}

	// 각 탭 버튼에 클릭 이벤트 리스너 추가
	for (var i = 0; i < tabs.length; i++) {
		tabs[i].addEventListener("click", changeTab);
	}

	// 페이지 로드 시 첫 번째 탭 활성화
	if (tabs.length > 0) {
		tabs[0].click();
	}
	// -----------XX일차 탭박스 종료

	// 여행지 추가 이벤트 시작 ---------------------------------------

	// 이벤트 위임을 사용하여 .item_wrap 요소에 이벤트 리스너 등록 ---------------------------!!!!!
	document
		.querySelector(".item_wrap")
		.addEventListener("click", function (event) {
			if (
				event.target.closest(".item") &&
				!event.target.classList.contains("check-button")
			) {
				const activeConbox = document.querySelector(".conbox.active");
				if (!activeConbox) return;

				const dayIndex =
					Array.from(document.querySelectorAll(".conbox")).indexOf(
						activeConbox
					) + 1;

				// .plan_wrap 요소에 이미 10개 이상의 항목이 있는지 확인
				if (activeConbox.querySelectorAll(".item").length >= 10) {
					alert(
						"더 이상 항목을 추가할 수 없습니다. 최대 10개까지 가능합니다."
					);
					return; // 항목 추가 중단
				}

				// -------마커 찍기---🗺---
				// .item 요소에서 data-trvl-x와 data-trvl-y 값을 가져옴
				const trvlX = parseFloat(
					event.target.closest(".item").dataset.trvlX
				);
				const trvlY = parseFloat(
					event.target.closest(".item").dataset.trvlY
				);

				// 지도 중심 좌표 이동
				map.setCenter(new naver.maps.LatLng(trvlY, trvlX));

				// 현재 dayIndex에 해당하는 항목 수
				const order = activeConbox.querySelectorAll(".item").length + 1;

				// 마커 이미지 파일 경로 설정 (예: /img/marker/marker01.png)
				const markerImageUrl = `/img/marker/marker${order
					.toString()
					.padStart(2, "0")}.png`;

				// 마커 생성 시도
				var marker = new naver.maps.Marker({
					position: new naver.maps.LatLng(trvlY, trvlX),
					map: map,
					icon: {
						url: markerImageUrl,
						size: new naver.maps.Size(30, 32),
						origin: new naver.maps.Point(0, 0),
						anchor: new naver.maps.Point(15, 32),
					},
				});

				console.log("마커 생성 시도:", markerImageUrl);
				console.log("마커 객체:", marker);

				// 해당 일차의 마커 리스트에 추가
				if (!markersByDay[dayIndex]) {
					markersByDay[dayIndex] = [];
				}
				markersByDay[dayIndex].push(marker);

				// 폴리라인 생성 또는 업데이트
				if (!polylinesByDay[dayIndex]) {
					polylinesByDay[dayIndex] = new naver.maps.Polyline({
						map: map,
						path: [],
						strokeColor: "#5347AA",
						strokeWeight: 2,
					});
				}
				var path = markersByDay[dayIndex].map((marker) =>
					marker.getPosition()
				);
				polylinesByDay[dayIndex].setPath(path);

				// 현재 일차의 마커와 폴리라인만 표시
				updateMarkersAndPolyline(dayIndex);
				// -------------------

				// .item_wrap에 있는거 .plan_wrap으로 복제
				const selectedItem = event.target
					.closest(".item")
					.cloneNode(true);
				if (selectedItem) {
					const checkButton =
						selectedItem.querySelector(".check-button");
					if (checkButton) {
						checkButton.remove();
					}
					const orderIndicator = document.createElement("div");
					const order =
						activeConbox.querySelectorAll(".item").length + 1; // 각 conbox 내의 item 수를 기준으로 order를 설정
					orderIndicator.textContent = order;
					orderIndicator.classList.add("circle-button");
					selectedItem.insertBefore(
						orderIndicator,
						selectedItem.firstChild
					);

					// button-wrap 생성
					const buttonWrap = document.createElement("div");
					buttonWrap.classList.add("button-wrap");

					// 🗺️버튼 생성
					const distanceButton = document.createElement("div");
					distanceButton.textContent = "🗺️";
					distanceButton.classList.add("distance-button");

					// ❌버튼 생성
					const deleteButton = document.createElement("div");
					deleteButton.textContent = "❌";
					deleteButton.classList.add("delete-button");

					// ❌버튼 이벤트 (작성일정목록 제거, 마커 제거)
					deleteButton.addEventListener("click", () => {
						// 선택된 마커의 인덱스를 가져옴
						const index = markersByDay[dayIndex].indexOf(marker);

						// 마커가 리스트에 존재할 경우 삭제하고 지도에서도 제거
						if (index > -1) {
							markersByDay[dayIndex][index].setMap(null); // 지도에서 마커 제거
							markersByDay[dayIndex].splice(index, 1); // 리스트에서 마커 제거
						}
						selectedItem.remove();
						// conbox안의 숫자 업데이트
						updateOrderIndicators(activeConbox);
						// 폴리라인 및 마커 업데이트
						updateMarkersAndPolyline(dayIndex);

						// 폴리라인 업데이트
						var path = markersByDay[dayIndex].map((marker) =>
							marker.getPosition()
						);
						polylinesByDay[dayIndex].setPath(path);
					});

					// button-wrap을 추가하고 하위에 🗺️과 X버튼 추가
					selectedItem.appendChild(buttonWrap);
					buttonWrap.appendChild(distanceButton);
					buttonWrap.appendChild(deleteButton);

					activeConbox
						.querySelector(".plan_wrap")
						.appendChild(selectedItem);
				}
			}
		});

	// 각 일차의 plan_wrap 요소에 대해 클릭 이벤트 리스너 등록
	document.querySelectorAll(".plan_wrap").forEach((planWrap) => {
		planWrap.addEventListener("click", function (event) {
			if (
				event.target.closest(".item") &&
				!event.target.classList.contains("delete-button")
			) {
				const trvlX = parseFloat(
					event.target.closest(".item").dataset.trvlX
				);
				const trvlY = parseFloat(
					event.target.closest(".item").dataset.trvlY
				);
				map.setCenter(new naver.maps.LatLng(trvlY, trvlX));
        map.setZoom(12);
			}
		});
	});

	// '상세' 클릭시 detail url 이동
	document
		.querySelector(".item_wrap")
		.addEventListener("click", function (event) {
			if (event.target.classList.contains("check-button")) {
				const trvlId = event.target.closest(".item").dataset.trvlId;
				const url = `http://localhost:9080/travelSearch/${trvlId}/detail`;
				// 팝업 차단 회피를 위해 setTimeout 사용
				setTimeout(() => {
					window.open(url, "_blank");
				}, 0);
			}
		});

	// updateOrderIndicators() 함수 (목록추가시 숫자 표시 함수)
	function updateOrderIndicators(conbox) {
		const items = conbox.querySelectorAll(".item");
		items.forEach((item, index) => {
			const orderIndicator = item.querySelector(".circle-button");
			if (orderIndicator) {
				orderIndicator.textContent = index + 1;
			}
		});
	}

	// 마커와 폴리라인 업데이트 함수 내에 마커 아이콘 업데이트 로직 추가
	function updateMarkersAndPolyline(dayIndex) {
		// 모든 마커 숨기기
		Object.values(markersByDay)
			.flat()
			.forEach((marker) => {
				marker.setMap(null);
			});

		// 모든 폴리라인 숨기기
		Object.values(polylinesByDay).forEach((polyline) => {
			polyline.setMap(null);
		});

		// 현재 선택된 일차의 마커만 지도에 표시
		if (markersByDay[dayIndex]) {
			markersByDay[dayIndex].forEach((marker, index) => {
				marker.setMap(map);
				// 마커 아이콘 업데이트
				const newMarkerImageUrl = `/img/marker/marker${(index + 1)
					.toString()
					.padStart(2, "0")}.png`;
				marker.setIcon({
					url: newMarkerImageUrl,
					size: new naver.maps.Size(30, 32),
					origin: new naver.maps.Point(0, 0),
					anchor: new naver.maps.Point(15, 32),
				});
			});
		}

		// 현재 선택된 일차의 폴리라인만 지도에 표시
		if (polylinesByDay[dayIndex]) {
			polylinesByDay[dayIndex].setMap(map);
		}
	}

	// 유효성 검사
	// register_btn 클릭시 plan_wrap 클래스를 가진 요소를 모두 돌아서 plan_wrap요소중 하나라도 자식요소가 하나도 없는경우 이벤트 발생안시키고 alter창(일차별로 1개이상 여행지를 넣어주세요)띄우기

	//  ----------------------------------------------------------
}); // DOMContentLoaded 종료 ------- ★