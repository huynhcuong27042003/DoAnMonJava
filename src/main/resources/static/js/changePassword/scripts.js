function togglePasswordVisibility(passwordId, showPasswordId) {
    var passwordInput = document.getElementById(passwordId);
    var showPasswordIcon = document.getElementById(showPasswordId)

    if (passwordInput.type === "password") {
        passwordInput.type = "text";
        showPasswordIcon.classList.remove('ri-eye-line');
        showPasswordIcon.classList.add('ri-eye-off-line');
    } else {
        passwordInput.type = "password";
        showPasswordIcon.classList.add('ri-eye-line');
        showPasswordIcon.classList.remove('ri-eye-off-line');
    }
}

function checkInputs() {
    var currentPassword = document.getElementById('currentPassword').value;
    var newPassword = document.getElementById('newPassword').value;
    var confirmedNewPassword = document.getElementById('confirmedNewPassword').value;
    var submitButton = document.getElementById('submitButton');

    if (currentPassword && newPassword && confirmedNewPassword) {
        submitButton.disabled = false;
        submitButton.classList.remove('cursor-not-allowed');
        submitButton.classList.add('cursor-pointer', 'hover:bg-yellow-400');
    } else {
        submitButton.disabled = true;
        submitButton.classList.add('cursor-not-allowed');
        submitButton.classList.remove('cursor-pointer', 'hover:bg-yellow-400');
    }
}

function checkSamePassowrd(event) {
    var newPassword = document.getElementById('newPassword').value;
    var confirmedNewPassword = document.getElementById('confirmedNewPassword').value;

    if (newPassword !== confirmedNewPassword) {
        toast({
            title: "Thất bại!",
            message: "Mật khẩu xác nhận không đúng!!",
            type: "error",
            duration: 5000
        });

        event.preventDefault();

        return;
    }
}