function formatDate(date) {
    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0'); // Months are zero-indexed
    const year = date.getFullYear();

    return `${day}/${month}/${year}`;
}

// *  Calculate the times
const now = new Date();
const tomorrow = new Date(now);

tomorrow.setDate(now.getDate()); // * Correctly set to today

const dayAfterTomorrow = new Date(now);

dayAfterTomorrow.setDate(now.getDate() + 1); // *  Correctly set to tomorrow

const rentalTimeElement = document.getElementById('rentalTime');

rentalTimeElement.value = `${formatDate(tomorrow)} -> ${formatDate(dayAfterTomorrow)}`;