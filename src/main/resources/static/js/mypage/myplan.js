document.addEventListener("DOMContentLoaded", function () {
	// 삭제 버튼에 이벤트 리스너 추가
	document.querySelectorAll(".delBtn").forEach(function (button) {
		button.addEventListener("click", function (event) {
			// 버튼의 상위 요소 중 'plan_item' 클래스를 가진 가장 가까운 요소 찾기
			let planItem = event.target.closest(".plan_item");

			if (planItem) {
				// data-plan-id 속성 값 추출
				let planId = planItem.getAttribute("data-plan-id");

				if (planId) {
					// 여행일정을 삭제할 것인지 사용자에게 물어보기
					if (confirm("정말로 이 여행일정을 삭제하시겠습니까?")) {
						// GET 요청 URL 생성
						let url = `/mypage/delete/${planId}`;
						// GET 요청 보내기
						window.location.href = url;
					}
				} else {
					console.error("plan_id가 존재하지 않습니다.");
				}
			} else {
				console.error(
					"plan_item 클래스를 가진 요소를 찾을 수 없습니다."
				);
			}
		});
	});

	document.querySelectorAll(".plan_item").forEach(function (planItem) {
		planItem.addEventListener("click", function (event) {
			// 클릭된 요소가 .delBtn일 경우 이벤트를 무시하고 종료
			if (event.target.classList.contains("delBtn")) {
				return;
			}

			const form = document.getElementById("checkFrom");
			// 버튼의 상위 요소 중 'plan_item' 클래스를 가진 가장 가까운 요소 찾기
			const planItem = event.target.closest(".plan_item");

			let planId = planItem.getAttribute("data-plan-id");
			// 'plan_item' 내부의 특정 클래스를 가진 요소의 텍스트 내용 가져오기
			const ragionNm = planItem.querySelector(".area").textContent; // 지역 이름

			// 필요한 텍스트 내용을 콘솔에 출력 또는 다른 처리
			console.log("지역 이름:", ragionNm);
			console.log("planId", planId);

			// 추가적으로 필요한 정보 가져오기
			const trvlStd = planItem
				.querySelector(".travel_date")
				.textContent.split("~")[0]
				.trim(); // 여행 시작 날짜
			const trvlLsd = planItem
				.querySelector(".travel_date")
				.textContent.split("~")[1]
				.trim(); // 여행 종료 날짜

			console.log("여행 시작 날짜:", trvlStd);
			console.log("여행 종료 날짜:", trvlLsd);

			form.planId.value = planItem.getAttribute("data-plan-id");
			form.ragionNm.value = planItem.querySelector(".area").textContent;
			form.trvlStd.value = trvlStd;
			form.trvlLsd.value = trvlLsd;

			// 폼 제출
			form.submit();
		});
	});
});