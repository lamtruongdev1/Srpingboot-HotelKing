$(document).ready(function () {
    const urlParams = new URLSearchParams(window.location.search);
    const slug = urlParams.get('slug');

    if (slug) {
        getRoomBySlug(slug);
    }

    function getRoomBySlug(slug) {
        $.ajax({
            url: `http://localhost:8080/api/rooms/${slug}`,
            method: 'GET',
            success: function (data) {
                let room = data;
                let html = `
                    <div class="checkout-room-details">
                        <h4 class="mb-2">${room.name}</h4>
                        <img src="/uploads/images/rooms/${room.image}" alt="${room.name}" height="220px" width="100%">
                        <p>Giá: ${formatCurrency(room.price)}</p>
                        <p>Địa điểm: ${room.location}</p>
                        <p>Số lượng người: ${room.person}</p>
                    </div>
                `;
                $('#checkout-room-detail').html(html);
            },
            error: function (xhr, status, error) {
                console.error('Lỗi khi tải dữ liệu:', error);
            }
        });
    }

    function formatCurrency(amount) {
        return amount.toLocaleString('vi-VN', {
            style: 'currency',
            currency: 'VND'
        });
    }


    //nút checkout, xử lý việc thanh toán và tạo đơn hàng
    $('#paymentButton').on('click', function (event) {
        event.preventDefault();

        const fullName = $('#fullName').val().trim();
        const email = $('#email').val().trim();
        const phone = $('#phone').val().trim();
        const checkInDate = $('#checkInDate').val();
        const checkOutDate = $('#checkOutDate').val();
        const paymentMethod = $('input[name="paymentMethod"]:checked').val();

        const bookingData = {
            fullName,
            email,
            phone,
            checkInDate,
            checkOutDate,
            paymentMethod,
            slug
        };

        $.ajax({
            url: 'http://localhost:8080/api/rooms/book-room',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(bookingData),
            success: function (response) {
                if (paymentMethod === 'cod') {
                    alert('Đặt phòng thành công! Vui lòng thanh toán khi nhận phòng.');
                    window.location.href = '/home';
                } else if (paymentMethod === 'vnpay') {
                    window.location.href = response.paymentUrl;
                }
            },
            error: function (xhr, status, error) {
                alert('Đã xảy ra lỗi khi đặt phòng. Vui lòng thử lại.');
                console.error(error);
            }
        });
    });
});
