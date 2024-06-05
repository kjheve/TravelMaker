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
   // 지도가 이미 초기화되어 있지 않다면 초기화 진행
   if (!map) {
      initializeMap();
   }

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
         var circle = item.querySelector(".circle-button");
         circle.textContent = index + 1;
      });
   });
   // ------- item 당 순번찍기 종료

   // item 클릭 시 지도 중심 변경 로직 추가
   document.querySelectorAll(".item").forEach((item) => {
      item.addEventListener("click", function (event) {
         if (!event.target.classList.contains("delete-button")) {
            const trvlX = parseFloat(item.dataset.trvlX);
            const trvlY = parseFloat(item.dataset.trvlY);
            map.setCenter(new naver.maps.LatLng(trvlY, trvlX));
            map.setZoom(12);
         }
      });
   });

   // 동적으로 추가된 delete-button 클릭 시 항목 제거
   document.addEventListener("click", function (event) {
      if (event.target.classList.contains("delete-button")) {
         const item = event.target.closest(".item");
         if (item) {
            const dayIndex =
               Array.from(document.querySelectorAll(".conbox")).indexOf(
                  item.closest(".conbox")
               ) + 1;
            const markerIndex = Array.from(
               item.closest(".plan_wrap").querySelectorAll(".item")
            ).indexOf(item);
            // 마커가 리스트에 존재할 경우 삭제하고 지도에서도 제거
            if (
               markersByDay[dayIndex] &&
               markersByDay[dayIndex][markerIndex]
            ) {
               markersByDay[dayIndex][markerIndex].setMap(null);
               markersByDay[dayIndex].splice(markerIndex, 1);
            }
            // 항목 제거
            item.remove();
            // conbox 안의 숫자 업데이트
            const activeConbox =
               document.querySelectorAll(".conbox")[dayIndex - 1];
            updateOrderIndicators(activeConbox);
            // 폴리라인 및 마커 업데이트
            updateMarkersAndPolyline(dayIndex);
            // 폴리라인 업데이트
            var path = markersByDay[dayIndex].map((marker) =>
               marker.getPosition()
            );
            polylinesByDay[dayIndex].setPath(path);
         }
      }
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
         // 생성된거 클릭시 지도로 이동
         planWrapClick();
      }); // item_wrap 클릭 이벤트 종료

   // 기존 클릭시 지도로 이동
   planWrapClick();
   
   // 각 일차의 plan_wrap 요소에 대해 클릭 이벤트 리스너 등록
   function planWrapClick() {
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
   }

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

   // --------------날짜 수정---------------------------- 자바스크립트
   const spanStartDate = document.querySelector(".trvlStd");
   const spanEndDate = document.querySelector(".trvlLsd");
   const okButton = document.createElement("button");
   okButton.textContent = "확인";
   okButton.classList.add("okBtn");
   const errorMessage = document.createElement("div");
   errorMessage.style.color = "red";
   errorMessage.style.marginLeft = "5px";
   errorMessage.style.marginTop = "3px";
   errorMessage.style.display = "none";

   const createInputField = (currentDate, className) => {
      const inputField = document.createElement("input");
      inputField.type = "date";
      inputField.value = currentDate;
      inputField.classList.add(className);
      return inputField;
   };

   const handleClick = () => {
      const startDate = spanStartDate.textContent;
      const endDate = spanEndDate.textContent;

      const inputStartDate = createInputField(startDate, "inputStartDate");
      const inputEndDate = createInputField(endDate, "inputEndDate");

      spanStartDate.style.display = "none";
      spanEndDate.style.display = "none";

      spanStartDate.parentNode.insertBefore(inputStartDate, spanStartDate);
      spanEndDate.parentNode.insertBefore(inputEndDate, spanEndDate);
      inputEndDate.insertAdjacentElement("afterend", okButton);
      okButton.insertAdjacentElement("afterend", errorMessage);
   };

   spanStartDate.addEventListener("click", handleClick);
   spanEndDate.addEventListener("click", handleClick);

   okButton.addEventListener("click", () => {
      const newStartDate = document.querySelector(".inputStartDate").value;
      const newEndDate = document.querySelector(".inputEndDate").value;

      const start = new Date(newStartDate);
      const end = new Date(newEndDate);
      const diffTime = Math.abs(end - start);
      const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24));

      if (start >= end) {
         // 종료 날짜가 시작 날짜보다 작거나 같은 경우
         errorMessage.textContent = "날짜확인 바람";
         errorMessage.style.display = "block";
      } else if (diffDays > 4) {
         // 최대 4박5일이므로 4일보다 큰 경우 경고
         errorMessage.textContent = "최대 4박5일";
         errorMessage.style.display = "block";
      } else {
         spanStartDate.textContent = newStartDate;
         spanEndDate.textContent = newEndDate;

         document.querySelector(".inputStartDate").remove();
         document.querySelector(".inputEndDate").remove();
         okButton.remove();
         errorMessage.style.display = "none";

         spanStartDate.style.display = "";
         spanEndDate.style.display = "";

         // 새롭게 day 값을 구하여 HTML 업데이트
         updateDays(diffDays + 1);
      }
   });

   // day 값을 업데이트하고 일차별 탭과 conbox를 동적으로 생성하는 함수
   function updateDays(newDays) {
      const planContainer = document.querySelector(".plan-container");
      const planTitle = document.querySelector(".plan_title");
      const currentDays = document.querySelectorAll(".conbox").length;

      // 기존 탭 및 conbox 유지
      for (let i = 1; i <= currentDays; i++) {
         if (i > newDays) {
            // 기존 마커와 폴리라인 제거
            if (markersByDay[i]) {
               markersByDay[i].forEach((marker) => marker.setMap(null));
               delete markersByDay[i];
            }
            if (polylinesByDay[i]) {
               polylinesByDay[i].setMap(null);
               delete polylinesByDay[i];
            }
            // DOM 요소 제거
            document.getElementById(`tab0${i}`).remove();
            document.querySelector(`label[for="tab0${i}"]`).remove();
            document.querySelector(`.con${i}`).remove();
         }
      }

      // 새로운 day에 맞는 탭 및 conbox 생성
      for (let i = currentDays + 1; i <= newDays; i++) {
         const tabInput = document.createElement("input");
         tabInput.type = "radio";
         tabInput.name = "tabmenu";
         tabInput.id = `tab0${i}`;
         if (i === 1) tabInput.checked = true;

         const tabLabel = document.createElement("label");
         tabLabel.htmlFor = `tab0${i}`;
         tabLabel.textContent = `${i}일차`;

         const conboxDiv = document.createElement("div");
         conboxDiv.classList.add("conbox", `con${i}`);

         const form = document.createElement("form");
         form.id = `form${i}`;
         form.classList.add("plan_wrap");

         conboxDiv.appendChild(form);
         planTitle.appendChild(tabInput);
         planTitle.appendChild(tabLabel);
         planContainer.appendChild(conboxDiv);

         // 탭 클릭 이벤트 리스너 등록
         tabInput.addEventListener("click", function () {
            document
               .querySelectorAll(".conbox")
               .forEach((box) => box.classList.remove("active"));
            conboxDiv.classList.add("active");
            initializeMap();
            updateMarkersAndPolyline(i);
         });
      }

      // 첫 번째 탭 활성화 및 해당 마커와 폴리라인 표시
      if (newDays > 0) {
         document.querySelectorAll('input[name="tabmenu"]')[0].click();
      }
   } // 날짜 변경시 자바스크립트 종료 ---

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
}); // DOMContentLoaded 종료-------------------------★