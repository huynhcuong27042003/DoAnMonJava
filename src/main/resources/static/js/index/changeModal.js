function changeModal(event) {
    const clickedElement = event.target
    const elementId = clickedElement.id

    if (elementId === "haveAccount") {
        document.getElementById('registerModal').classList.remove('show')
        document.getElementById('loginModal').classList.add('show')
    } else {
        document.getElementById('loginModal').classList.remove('show')
        document.getElementById('registerModal').classList.add('show')
    }

    event.preventDefault()
}