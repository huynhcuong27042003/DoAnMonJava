const slider = document.getElementById('slider');
const prevButton = document.getElementById('prev');
const nextButton = document.getElementById('next');
const size = 3;

let currentIndex = 0;

function updateSliderPosition() {
    const slideWidth = slider.querySelector('.slider-item').clientWidth;
    slider.style.transform = `translateX(-${currentIndex * slideWidth}px)`;
    updateButtons();
}

function updateButtons() {
    const length = slider.querySelectorAll('.slider-item').length;
    const atStart = currentIndex === 0;
    const atEnd = currentIndex === length - size;

    prevButton.classList.toggle("opacity-50", atStart);
    prevButton.classList.toggle("hover:bg-gray-200", !atStart);
    nextButton.classList.toggle("opacity-50", atEnd);
    nextButton.classList.toggle("hover:bg-gray-200", !atEnd);

    prevButton.classList.toggle('disabled', prevButton.disabled);
    nextButton.classList.toggle('disabled', nextButton.disabled);
}

prevButton.addEventListener('click', () => {
    if (currentIndex > 0) {
        currentIndex--;
        updateSliderPosition();
    }
});

nextButton.addEventListener('click', () => {
    const maxIndex = slider.querySelectorAll('.slider-item').length - size;
    if (currentIndex < maxIndex) {
        currentIndex++;
        updateSliderPosition();
    }
});

window.addEventListener('resize', updateSliderPosition);

updateSliderPosition();
