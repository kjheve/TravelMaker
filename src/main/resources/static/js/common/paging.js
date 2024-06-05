class Pagination {
	constructor(recordsPerPage, pagesPerPage) {
		this.totalRecords = 0; //총 레코드수
		this.recordsPerPage = recordsPerPage; //페이지당 레코드수
		this.pagesPerPage = pagesPerPage; //페이지당 페이지수
		this.currentPage = 1; //현재 페이지
		this.currentPageGroupStart = 1; //현재 페이지의 시작페이지
	}

	getTotalRecords() {
		return this.totalRecords;
	}

	getTotalPages() {
		return Math.ceil(this.totalRecords / this.recordsPerPage);
	}

	// ---------------------------- 버튼 뿌리기
	displayNavigation() {
		const totalPages = this.getTotalPages();
		let pageNavigation =
			this.currentPageGroupStart > 1
				? '<button id="first">처음</button> <button id="prev">이전</button> '
				: "";

		for (
			let i = this.currentPageGroupStart;
			i < this.currentPageGroupStart + this.pagesPerPage &&
			i <= totalPages;
			i++
		) {
			//현재 페이지인경우
			if (i === this.currentPage) {
				pageNavigation += `<button class="active" id="page${i}">${i}</button> `;
				//현재 페이지아닌경우
			} else {
				pageNavigation += `<button id="page${i}">${i}</button> `;
			}
		}

		pageNavigation +=
			this.currentPageGroupStart + this.pagesPerPage - 1 < totalPages
				? '<button id="next">다음</button> <button id="last">끝</button>'
				: "";
		return pageNavigation;
	}

	setTotalRecords(totalRecords) {
		this.totalRecords = totalRecords;
	}

	setCurrentPage(pageNumber) {
		this.currentPage = pageNumber;
	}

	setCurrentPageGroupStart(cpgs) {
		this.currentPageGroupStart = cpgs;
	}

	setNextPageGroup() {
		this.currentPageGroupStart += this.pagesPerPage;
	}

	setPrevPageGroup() {
		this.currentPageGroupStart -= this.pagesPerPage;
		if (this.currentPageGroupStart < 1) {
			this.currentPageGroupStart = 1;
		}
	}

	// ---------------------------- 페이지 버튼을 클릭했을 때 함수
	displayPagination(callback) {
		//callback 내부에서 호출된 함수
		document.getElementById("pagination").innerHTML =
			this.displayNavigation();
		for (
			let i = this.currentPageGroupStart;
			i < this.currentPageGroupStart + this.pagesPerPage &&
			i <= this.getTotalPages();
			i++
		) {
			document
				.getElementById(`page${i}`)
				.addEventListener("click", (evt) => {
					this.setCurrentPage(i);
					//요청페이지 처리
					callback();
					this.displayPagination(callback);
				});
		}
		if (document.getElementById("first")) {
			document
				.getElementById("first")
				.addEventListener("click", (evt) => {
					this.setCurrentPage(1);
					this.currentPageGroupStart = 1;
					callback();
					this.displayPagination(callback);
				});
		}
		if (document.getElementById("prev")) {
			document.getElementById("prev").addEventListener("click", (evt) => {
				if (this.currentPageGroupStart > 1) {
					this.setPrevPageGroup();
					this.setCurrentPage(
						this.currentPageGroupStart + this.pagesPerPage - 1
					);
					callback();
					this.displayPagination(callback);
				}
			});
		}
		if (document.getElementById("next")) {
			document.getElementById("next").addEventListener("click", (evt) => {
				if (
					this.currentPageGroupStart + this.pagesPerPage - 1 <
					this.getTotalPages()
				) {
					this.setNextPageGroup();
					this.setCurrentPage(this.currentPageGroupStart);
					callback();
					this.displayPagination(callback);
				}
			});
		}
		if (document.getElementById("last")) {
			document.getElementById("last").addEventListener("click", (evt) => {
				const totalPages = this.getTotalPages();
				this.currentPageGroupStart =
					totalPages - (totalPages % this.pagesPerPage) + 1;
				this.setCurrentPage(totalPages);
				callback();
				this.displayPagination(callback);
			});
		}
	}
}

export { Pagination };