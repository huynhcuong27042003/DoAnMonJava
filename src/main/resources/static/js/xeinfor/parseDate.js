document.addEventListener("DOMContentLoaded", function() {
    // Chọn span và input theo id
    const totalDatesSpan = document.getElementById("totalDates");
    const soNgayThueInput = document.getElementById("soNgayThue");

    // Lắng nghe sự thay đổi của span
    totalDatesSpan.addEventListener("DOMSubtreeModified", function() {
        // Lấy giá trị mới từ span
        const totalDatesValue = totalDatesSpan.innerText;

        // Gán giá trị vào input
        soNgayThueInput.value = totalDatesValue;
    });
});
