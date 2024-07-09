const startDateElement = document.getElementById('startDate');
const endDateElement = document.getElementById('endDate');

startDateElement.value = formatDate(tomorrow);
endDateElement.value = formatDate(dayAfterTomorrow);

