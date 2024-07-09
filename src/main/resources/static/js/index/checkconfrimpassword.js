function validatePasswords() {
    var password = document.getElementById("registerPassword").value;
    var confirmPassword = document.getElementById("confirmedRegisterPassword").value;

    // Kiểm tra xem hai mật khẩu có khớp nhau không
    if (password !== confirmPassword) {
        alert("Mật khẩu nhập lại không khớp!");
        return false; // Ngăn chặn form từ việc gửi yêu cầu
    }
    return true; // Cho phép form gửi yêu cầu
}