document.getElementById('openLoginModalBtn').addEventListener('click', function () {
    document.getElementById('loginModal').classList.add('show')
})

document.getElementById('closeLoginModal').addEventListener('click', function () {
    document.getElementById('loginModal').classList.remove('show')
})

// * Close register modal when clicking outside the modal
window.addEventListener('click', (event) => {
    const registerModal = document.getElementById('loginModal')

    if (event.target === registerModal) {
        registerModal.classList.remove('show')
    }
})