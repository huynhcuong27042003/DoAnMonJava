function focusInput(inputId) {
    document.getElementById(inputId).focus();
}

function toggleEdit(elementId, inputId) {
    document.getElementById(elementId).classList.toggle('hidden');
    document.getElementById(inputId).classList.toggle('hidden');

    displayElement.classList.toggle('hidden');
    inputElement.classList.toggle('hidden');
    inputElement.value = displayElement.textContent.trim();
    inputElement.focus();
}

function updateDisplay(displayId, inputId) {
    const inputElement = document.getElementById(inputId);
    const displayElement = document.getElementById(displayId);
    displayElement.textContent = inputElement.value;
    toggleEdit(displayId, inputId);
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('birthdate-input').addEventListener('change', () => {
        updateDisplay('birthdate-display', 'birthdate-input');
    });
    document.getElementById('gender-input').addEventListener('change', () => {
        updateDisplay('gender-display', 'gender-input');
    });
});

function handleImageUpload(event) {
    const reader = new FileReader();
    reader.onload = function (e) {
        const imgElement = document.getElementById('profile-image');
        imgElement.src = e.target.result;
    }
    reader.readAsDataURL(event.target.files[0]);
}

document.getElementById('profile-image').addEventListener('click', function () {
    document.getElementById('image-upload-profile').click();
});
