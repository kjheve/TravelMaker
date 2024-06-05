document.addEventListener("DOMContentLoaded", () => {
	document.querySelectorAll(".thumbs_item").forEach((item) => {
		item.addEventListener("click", () => {
			const overlayText = item.querySelector(".overlay").textContent;
			location.href = `/travelmaker/dateselect?ragion=${overlayText}`;
		});
	});
});
