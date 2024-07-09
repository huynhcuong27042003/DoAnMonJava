dayjs.extend(window.dayjs_plugin_advancedFormat);
dayjs.extend(window.dayjs_plugin_isSameOrBefore);
dayjs.extend(window.dayjs_plugin_isSameOrAfter);

let currentMonth = dayjs().startOf('month');
let startDate = dayjs().startOf('day');
let endDate = dayjs().add(1, 'day').startOf('day');

document.addEventListener('DOMContentLoaded', function () {
    function convertPrice() {
        const pricePerDay = document.getElementById('pricePerDay');

        let priceText = pricePerDay.textContent;
        let number = parseFloat(priceText);
        let integerNumber = Math.floor(number);

        pricePerDay.textContent = integerNumber + 'đ /ngày';
    }

    function fillDateRangeWhenLoading() {
        const selectedDatesText = document.getElementById('rentalTime').value;
        const dateRange = splitDateRange(selectedDatesText);

        formatDateForInput(dateRange.startDate, 'startDateSearch');
        formatDateForInput(dateRange.endDate, 'endDateSearch');
    }

    if (document.getElementById('carBooking')) {
        convertPrice();
    }

    if (document.getElementById('rentalTime')) {
        fillDateRangeWhenLoading();
    }
});

function formatDateForInput(dateString, inputId) {
    var parts = dateString.split('/');
    var formattedDate = parts[2] + '-' + parts[1] + '-' + parts[0];

    document.getElementById(inputId).value = formattedDate;
}

function splitDateRange(dateRangeString) {
    var dates = dateRangeString.split(' -> ');
    var startDate = dates[0];
    var endDate = dates[1];

    return {
        startDate: startDate,
        endDate: endDate
    };
}

function openCalendarModal() {
    const modal = document.getElementById('calendarModal');
    const modalContent = modal.querySelector('.calendar-modal-content');

    modal.classList.remove('hidden');
    modalContent.classList.remove('calendar-modal-close');
    modalContent.classList.add('calendar-modal-open');

    renderCalendars();
    updateSelectedDates();
    // setDefaultTime();
    populateTimeOptions('pickupTime');
    populateTimeOptions('returnTime');
}

function closeModal(event) {
    const modal = document.getElementById('calendarModal');
    const modalContent = modal.querySelector('.calendar-modal-content');

    modalContent.classList.remove('calendar-modal-open');
    modalContent.classList.add('calendar-modal-close');

    modalContent.addEventListener('animationend', function handler() {
        modal.classList.add('hidden');
        modalContent.classList.remove('modal-close');
        modalContent.removeEventListener('animationend', handler);
    });

    event.preventDefault();
}

function handleBackdropClick(event) {
    if (event.target === event.currentTarget) {
        closeModal();
    }
}

function prevMonthPair() {
    const newCurrentMonth = currentMonth.subtract(2, 'month');
    if (newCurrentMonth.isBefore(dayjs().startOf('month'), 'month')) {
        return; // * Do not allow navigating to past months
    }
    currentMonth = newCurrentMonth;
    renderCalendars();
}

function nextMonthPair() {
    currentMonth = currentMonth.add(2, 'month');
    renderCalendars();
}

function renderCalendars() {
    const currentMonthElement = document.getElementById('currentMonth');
    const nextMonthElement = document.getElementById('nextMonth');

    currentMonthElement.innerHTML = generateCalendar(currentMonth, true);
    nextMonthElement.innerHTML = generateCalendar(currentMonth.add(1, 'month'), false);

    // Hide prev button if at the current month
    document.querySelector('.prev-button').style.display = currentMonth.isSame(dayjs().startOf('month'), 'month') ? 'none' : 'block';
}

function generateCalendar(date, isCurrent) {
    const startOfMonth = date.startOf('month');
    const endOfMonth = date.endOf('month');
    let calendarHtml = `
        <div>
            <div class="calendar-header">
                ${isCurrent ? '<button class="px-2 py-1 text-gray-700 bg-gray-300 rounded prev-button" onclick="prevMonthPair()">&#9664;</button>' : ''}
                <h3 class="font-bold text-center">${date.format('MMMM YYYY')}</h3>
                ${!isCurrent ? '<button class="px-2 py-1 text-gray-700 bg-gray-300 rounded next-button" onclick="nextMonthPair()">&#9654;</button>' : ''}
            </div>
            <div class="grid grid-cols-7 gap-2 mx-3 mt-2 text-center">
                ${['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'].map(day => `<div class="font-bold">${day}</div>`).join('')}
                ${Array.from({ length: startOfMonth.day() }).map(() => '<div></div>').join('')}
                ${Array.from({ length: endOfMonth.date() }).map((_, i) => {
        const currentDate = startOfMonth.add(i, 'day');
        const isSelected = currentDate.isSame(startDate, 'day') || currentDate.isSame(endDate, 'day');
        const isInRange = currentDate.isAfter(startDate, 'day') && currentDate.isBefore(endDate, 'day');
        const isStartOrEnd = isSelected || isInRange;
        const isDisabled = isCurrent && currentDate.isBefore(dayjs().startOf('day'), 'day');
        return `
                        <div class="${isDisabled ? 'disabled-date' : isSelected ? 'bg-yellow-300' : isStartOrEnd ? 'bg-yellow-500' : 'bg-green-200 cursor-pointer hover:bg-green-400'} rounded" ${isDisabled ? '' : `onclick="selectDate('${currentDate.format('YYYY-MM-DD')}')"`}>
                            ${currentDate.date()}
                        </div>`;
    }).join('')}
            </div>
        </div>
    `;
    return calendarHtml;
}

function selectDate(dateString) {
    const selectedDate = dayjs(dateString);

    if (!startDate || (startDate && endDate)) {
        startDate = selectedDate;
        endDate = null;
    } else if (selectedDate.isBefore(startDate)) {
        endDate = startDate;
        startDate = selectedDate;
    } else {
        endDate = selectedDate;
    }

    renderCalendars();
    updateSelectedDates();
}

function updateSelectedDates() {
    let endDateText = "";

    if (endDate) {
        endDateText = endDate.format('DD/MM/YYYY');
    }

    let selectedDatesText = `${startDate.format('DD/MM/YYYY')} -> ${endDateText}`;
    let daysDifference;

    if (document.getElementById('rentalTime')) {
        document.getElementById('rentalTime').value = selectedDatesText;

        const dateRange = splitDateRange(selectedDatesText);

        formatDateForInput(dateRange.startDate, 'startDateSearch');
        formatDateForInput(dateRange.endDate, 'endDateSearch');
    }

    if (startDate && endDate) {
        daysDifference = endDate.diff(startDate, 'day');

        selectedDatesText += ` (${daysDifference} ngày)`;

        if (document.getElementById('totalDates')) {
            document.getElementById('totalDates').textContent = daysDifference;
        }
    }

    document.getElementById('selectedDates').textContent = selectedDatesText;

    // * This if will performs in Info page
    if (document.getElementById('carBooking')) {
        const formattedStartDate = startDate.format('DD/MM/YYYY').toString();

        let formattedEndDate = "";

        if (endDate) {
            formattedEndDate = endDate.format('DD/MM/YYYY').toString();
        }

        let daysDifference;

        if (startDate && endDate) {
            daysDifference = endDate.diff(startDate, 'day');
        }

        document.getElementById('startDate').value = formattedStartDate;
        document.getElementById('endDate').value = formattedEndDate;

        const rentalPriceRaw = document.getElementById('rentalPrice').textContent;
        const rentalPrice = rentalPriceRaw.match(/\d+/g).join('');
        const totalPrice = rentalPrice * daysDifference;
        const deposit = totalPrice * 0.2;
        const restTotal = totalPrice - deposit;

        let depositText = "0 đ";
        let restTotalText = "0 đ";
        let totalPriceText = "0 đ";

        if (!isNaN(Math.floor(deposit))) {
            depositText = Math.floor(deposit) + 'đ';
        }

        if (!isNaN(Math.floor(restTotal))) {
            restTotalText = Math.floor(restTotal) + 'đ';
        }

        if (!isNaN(totalPrice)) {
            totalPriceText = totalPrice.toString() + 'đ';
        }

        document.getElementById('depositPrice').textContent = depositText;
        document.getElementById('restTotalPrice').textContent = restTotalText;
        document.getElementById('totalPrice').textContent = totalPriceText;

        formatDateForInput(document.getElementById('startDate').value, 'startDateCalendar')
        formatDateForInput(document.getElementById('endDate').value, 'endDateCalendar')
    }
}

function generateTimeOptions(type) {
    let options = '';
    for (let i = 0; i < 24; i++) {
        for (let j = 0; j < 60; j += 30) {
            const time = `${i.toString().padStart(2, '0')}:${j.toString().padStart(2, '0')}`;
            options += `
                <div id="${type}Option-${time.replace(':', '-')}" class="p-2 cursor-pointer hover:bg-gray-200" onclick="selectTime('${type}', '${time}')">
                    ${time}
                </div>`;
        }
    }
    return options;
}

function populateTimeOptions(type) {
    if (document.getElementById('pickupTimeMenu') && document.getElementById('pickupTimeDisplay')) {
        const menu = document.getElementById(`${type}Menu`);
        menu.innerHTML = generateTimeOptions(type);

        const selectedTime = document.getElementById(`${type}Display`).textContent;
        const selectedOption = document.getElementById(`${type}Option-${selectedTime.replace(':', '-')}`);

        if (selectedOption) {
            selectedOption.classList.add('selected-time');
            menu.scrollTop = selectedOption.offsetTop - menu.clientHeight / 2 + selectedOption.clientHeight / 2;
        }
    }
}

function toggleDropdown(type) {
    document.getElementById(`${type}Menu`).classList.toggle('open');
    populateTimeOptions(type);
}

// function selectTime(type, time) {
//     document.getElementById(`${type}Display`).textContent = time;
//     document.getElementById(`${type}Menu`).classList.remove('open');
//     updateSelectedDates();
// }

// function setDefaultTime() {
//     if (document.getElementById('pickupTimeDisplay')) {
//         selectTime('pickupTime', '20:00');
//     }

//     if (document.getElementById('pickupTimeMenu')) {
//         selectTime('returnTime', '21:00');
//     }
// }

document.addEventListener('click', function (event) {
    if (!event.target.closest('#pickupTimeButton') && !event.target.closest('#pickupTimeMenu')) {
        if (document.getElementById('pickupTimeMenu')) {
            document.getElementById('pickupTimeMenu').classList.remove('open');
        }
    }
    if (!event.target.closest('#returnTimeButton') && !event.target.closest('#returnTimeMenu')) {
        if (document.getElementById('returnTimeMenu')) {
            document.getElementById('returnTimeMenu').classList.remove('open');
        }
    }
});