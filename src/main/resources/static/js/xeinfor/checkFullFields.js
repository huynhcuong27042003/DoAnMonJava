function checkDates(event) {
    const startDate = document.getElementById('startDate').value;
    const endDate = document.getElementById('endDate').value;

    if (startDate.trim() === '' || endDate.trim() === '') {
        toast({
            title: "Thất bại!",
            message: "Hãy xem xét lại các thông tin",
            type: "error",
            duration: 5000
        });

        event.preventDefault();
    }
}