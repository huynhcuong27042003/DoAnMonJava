document.getElementById('dropdownDefaultButton').addEventListener('click', function() {
    var dropdownMenu = document.getElementById('dropdown');
    dropdownMenu.classList.toggle('hidden');
});

// Optional: Close dropdown menu if clicked outside
document.addEventListener('click', function(event) {
    var button = document.getElementById('dropdownDefaultButton');
    var dropdownMenu = document.getElementById('dropdown');
    if (!button.contains(event.target) && !dropdownMenu.contains(event.target)) {
        dropdownMenu.classList.add('hidden');
    }
});