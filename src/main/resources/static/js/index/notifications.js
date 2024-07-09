document.getElementById('notificationButton').addEventListener('click', function () {
    var dropdown = document.getElementById('notificationDropdown');
    if (dropdown.classList.contains('hidden')) {
        dropdown.classList.remove('hidden');
    } else {
        dropdown.classList.add('hidden');
    }
});

window.onclick = function (event) {
    if (!event.target.matches('#notificationButton')) {
        var dropdowns = document.getElementsByClassName("dropdown-content");
        for (var i = 0; i < dropdowns.length; i++) {
            var openDropdown = dropdowns[i];
            if (!openDropdown.classList.contains('hidden')) {
                openDropdown.classList.add('hidden');
            }
        }
    }
}
/*
document.addEventListener('DOMContentLoaded', function () {
    var notificationButton = document.getElementById('notificationButton');
    var notificationDropdown = document.getElementById('notificationDropdown');

    notificationButton.addEventListener('click', function (event) {
        event.stopPropagation();
        notificationDropdown.classList.toggle('hidden');
    });

    document.addEventListener('click', function (event) {
        if (!notificationButton.contains(event.target) && !notificationDropdown.contains(event.target)) {
            notificationDropdown.classList.add('hidden');
        }
    });
});*/
