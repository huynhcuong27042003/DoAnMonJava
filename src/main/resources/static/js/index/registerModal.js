document.getElementById('openRegisterModalBtn').addEventListener('click', function () {
    document.getElementById('registerModal').classList.add('show')
})

document.getElementById('closeRegisterModal').addEventListener('click', function () {
    document.getElementById('registerModal').classList.remove('show')
})

// * Close login modal when clicking outside the modal
window.addEventListener('click', (event) => {
    const registerModal = document.getElementById('registerModal')

    if (event.target === registerModal) {
        registerModal.classList.remove('show')
    }
})