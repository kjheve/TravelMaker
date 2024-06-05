// 전역 변수로 map 및 마커와 폴리라인 관리 객체 선언
var map = null;
var markersByDay = {};
var polylinesByDay = {};

function updateMarkersAndPolyline(dayIndex) {
	// 모든 마커와 폴리라인 숨김
	Object.values(markersByDay)
		.flat()
		.forEach((marker) => marker.setMap(null));
	Object.values(polylinesByDay).forEach((polyline) => polyline.setMap(null));

	// 선택된 일차의 마커와 폴리라인 표시
	markersByDay[dayIndex]?.forEach((marker) => marker.setMap(map));
	polylinesByDay[dayIndex]?.setMap(map);
}

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

// ------------------------------- DOMContentLoaded 시작 ----- ★
document.addEventListener("DOMContentLoaded", function () {
	// ------- item당 순번찍기
	// 모든 conbox를 선택
	var conboxes = document.querySelectorAll(".conbox");

	// 각 conbox에 대해 실행
	conboxes.forEach(function (conbox) {
		// 해당 conbox 내의 모든 .item을 선택
		var items = conbox.querySelectorAll(".item");
		// 각 item에 대해 순서를 업데이트
		items.forEach(function (item, index) {
			// .circle을 찾아서 내부 텍스트를 인덱스 + 1로 설정 (1부터 시작하도록)
			var circle = item.querySelector(".circle");
			circle.textContent = index + 1;
		});
	});
	// ------- item 당 순번찍기 종료

	// 지도가 이미 초기화되어 있지 않다면 초기화 진행
	if (!map) {
		initializeMap();
	}

	// item 클릭 시 지도 중심 변경 로직 추가
	document.querySelectorAll(".item").forEach((item) => {
		item.addEventListener("click", function () {
			const trvlX = parseFloat(item.dataset.trvlX);
			const trvlY = parseFloat(item.dataset.trvlY);
			map.setCenter(new naver.maps.LatLng(trvlY, trvlX));
			map.setZoom(12);
		});
	});

	// 일차별 탭메뉴
	var tabs = document.querySelectorAll('input[name="tabmenu"]');
	tabs.forEach((tab, index) => {
		tab.addEventListener("click", function () {
			document
				.querySelectorAll(".conbox")
				.forEach((box) => box.classList.remove("active"));
			const activeConbox = document.querySelectorAll(".conbox")[index];
			activeConbox.classList.add("active");

			initializeMap(); // 일차 클릭시 맵 초기화
			updateMarkersAndPolyline(index + 1); // 일차는 1부터 시작하므로 index + 1
		});
	});

	// 초기 마커 및 폴리라인 생성 로직 (초기화 시 모든 데이터 기반으로 생성)
	document.querySelectorAll(".conbox").forEach((conbox, dayIndex) => {
		const items = conbox.querySelectorAll(".item");
		const path = [];

		items.forEach((item, index) => {
			const x = parseFloat(item.dataset.trvlX);
			const y = parseFloat(item.dataset.trvlY);
			const position = new naver.maps.LatLng(y, x);
			const marker = new naver.maps.Marker({
				position,
				map,
				icon: {
					url: `/img/marker/marker${(index + 1)
						.toString()
						.padStart(2, "0")}.png`,
					size: new naver.maps.Size(30, 32),
					origin: new naver.maps.Point(0, 0),
					anchor: new naver.maps.Point(15, 32),
				},
			});
			path.push(position);

			// 마커를 해당 일차 객체에 저장
			markersByDay[dayIndex + 1] = markersByDay[dayIndex + 1] || [];
			markersByDay[dayIndex + 1].push(marker);
		});

		// 폴리라인 생성 및 저장
		const polyline = new naver.maps.Polyline({
			map,
			path,
			strokeColor: "#5347AA",
			strokeWeight: 2,
		});
		polylinesByDay[dayIndex + 1] = polyline;
	});

	// 페이지 로드 시 첫 번째 탭 활성화 및 해당 마커와 폴리라인 표시
	if (tabs.length > 0) {
		tabs[0].click();
	}

	// 여행일정 수정버튼누르면 modifyForm submit 하는 코드

	document
		.getElementById("modifyBtn")
		.addEventListener("click", function (event) {
			const form = document.getElementById("modifyForm");

			document.getElementsByName("ragionNm")[0].value =
				document.getElementById("planRagion").textContent;
			document.getElementsByName("trvlStd")[0].value =
				document.getElementById("trvlStd").textContent;
			document.getElementsByName("trvlLsd")[0].value =
				document.getElementById("trvlLsd").textContent;

			console.log(document.getElementsByName("planId")[0].value);
			console.log(document.getElementsByName("ragionNm")[0].value);
			console.log(document.getElementsByName("trvlStd")[0].value);
			console.log(document.getElementsByName("trvlLsd")[0].value);

			form.submit();
		});
}); // DOMContentLoaded 종료-------------------------★
