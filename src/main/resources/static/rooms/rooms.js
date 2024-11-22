$(document).ready(function () {
    // Kiểm tra nếu đang ở trang tất cả các phòng hoặc trang theo danh mục
    if (window.location.pathname.includes('/rooms') || window.location.pathname.includes('/room/')) {
        var slug = window.location.pathname.split("/").pop();
        if (slug && slug !== "rooms") {
            getRoomsByCategory(slug);
        } else {
            getAllRooms();
        }
    }
});

// Hàm lấy tất cả các phòng
const getAllRooms = () => {
    $.ajax({
        url: 'http://localhost:8080/api/rooms',
        method: 'GET',
        success: function (data) {
            renderRooms(data);
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải dữ liệu:', error);
        }
    });
};

// Hàm lấy các phòng theo danh mục (dựa trên slug)
const getRoomsByCategory = (slug) => {
    $.ajax({
        url: '/api/rooms/list-by-category/' + slug,
        method: 'GET',
        success: function (data) {
            renderRooms(data);
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải dữ liệu theo danh mục:', error);
        }
    });
};

// Hàm render các phòng ra HTML
const renderRooms = (rooms) => {
    let html = '';
    rooms.forEach(room => {
        html += `
            <div class="col-lg-4 col-md-6 ">
                <div class="room-item">
                    <img src="/uploads/images/rooms/${room.image}" alt="${room.name}" height="200px" width="100%">
                    <div class="ri-text">
                        <h4>${room.name}</h4>
                        <h3>${formatCurrency(room.price)}<span>/ ngày, đêm</span></h3>
                        <table>
                            <tbody>
                                <tr>
                                    <td class="r-o">Địa điểm:</td>
                                    <td>${room.location}</td>
                                </tr>
                                <tr>
                                    <td class="r-o">Phòng cho:</td>
                                    <td>${room.person}</td>
                                </tr>
                            </tbody>
                        </table>
                        <a href="/room/room-details/${room.slug}" class="btn-room-detail primary-btn">Xem chi tiết</a>
                    </div>
                </div>
            </div>
        `;
    });
    $('#room-list').html(html);
};


// Hàm format tiền tệ
function formatCurrency(amount) {
    return amount.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });
}
