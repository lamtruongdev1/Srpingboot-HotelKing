const getRoomBySlug = (slug) => {
    $.ajax({
        url: `http://localhost:8080/api/rooms/${slug}`,
        method: 'GET',
        success: function (data) {
            let room = data;
            let html = `
                    <div class="row">
                        <div class="col-lg-8">
                            <div class="room-details-item">
                                   <img src="/uploads/images/rooms/${room.image}" alt="${room.name}" height="400px" width="100%">
                                <div class="rd-text">
                                    <div class="rd-title">
                                        <h3>${room.name}</h3>
                                        <div class="rdt-right">
                                            <div class="rating">
                                                <i class="icon_star"></i>
                                                <i class="icon_star"></i>
                                                <i class="icon_star"></i>
                                                <i class="icon_star"></i>
                                                <i class="icon_star-half_alt"></i>
                                            </div>
                                            <a href="/checkout?slug=${slug}">Booking Now</a>
                                        </div>
                                    </div>
                                    <h2>${formatCurrency(room.price)}<span>/ ngày đêm</span></h2>
                                    <table>
                                        <tbody>
                                            <tr>
                                                <td class="r-o">Địa điểm:</td>
                                                <td>${room.location}</td>
                                            </tr>
                                            <tr>
                                                <td class="r-o">Phòng dành cho:</td>
                                                <td>${room.person}</td>
                                            </tr>
                                            <tr>
                                                <td class="r-o">Dịch vụ:</td>
                                                <td>
                                                    <div class="row">
                                                        <div class="service-item">
                                                            <i class="icon-bathroom"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-bedroom"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-view"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-outdoors"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-kitchen"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-activity"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-living-area"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-media"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-food"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-front-desk"></i>
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-entertainment"></i> 
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-safety"></i>
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-accessibility"></i>
                                                        </div>
                                                        <div class="service-item">
                                                            <i class="icon-language"></i> 
                                                        </div>
                                                    </div>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </div>
                            </div>
                        </div>
                        <div class="col-lg-4">
                            <div class="room-booking">
                                <h3>Đặt phòng</h3>
                                <form action="#">
                                    <div class="check-date">
                                        <label for="date-in">Ngày checkin:</label>
                                        <input type="date" class="date-input" id="date-in">
                                    </div>
                                    <div class="check-date">
                                        <label for="date-out">Ngày checkout:</label>
                                        <input type="date" class="date-input" id="date-out">
                                    </div>
                             
                                            <div class="mb-3">
                                                <label for="adult-guest" class="form-label me-2">Người lớn:</label>
                                                    <div class="quantity-wrapper">
                                                        <button class="btn btn-outline-secondary quantity-btn" type="button" onclick="changeQuantity('adult', -1)">-</button>
                                                        <input type="number" id="adult-guest" class="form-control quantity-input mt-4" value="0" min="0" readonly>
                                                        <button class="btn btn-outline-secondary quantity-btn" type="button" onclick="changeQuantity('adult', 1)">+</button>
                                                    </div>
                                            </div>
                                            <div class="mb-3">
                                                    <label for="child-guest" class="form-label me-2">Trẻ em:</label>
                                                    <div class="quantity-wrapper">
                                                        <button class="btn btn-outline-secondary quantity-btn" type="button" onclick="changeQuantity('child', -1)">-</button>
                                                        <input type="number" id="child-guest" class="form-control quantity-input mt-4" value="0" min="0" readonly>
                                                        <button class="btn btn-outline-secondary quantity-btn" type="button" onclick="changeQuantity('child', 1)">+</button>
                                                    </div>
                                            </div>
                                  
                                        <label for="room">Phòng:</label>
                                                 <div class="quantity-wrapper">
                                                     <button class="btn btn-outline-secondary quantity-btn" type="button" onclick="changeQuantity('room', -1)">-</button>
                                                     <input type="number" id="room-quantity" class="form-control quantity-input mt-4" value="0" min="0" readonly>
                                                     <button class="btn btn-outline-secondary quantity-btn" type="button" onclick="changeQuantity('room', 1)">+</button>
                                                 </div>
                         
                                    <button type="submit" class="btn btn-primary">Kiểm tra trạng thái phòng</button>
                                </form>
                            </div>
                        </div>
                    </div>
                `;
            $('#room-detail').html(html);
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi tải dữ liệu:', error);
        }
    });
};

function formatCurrency(amount) {
    return amount.toLocaleString('vi-VN', {
        style: 'currency',
        currency: 'VND'
    });
}

function changeQuantity(type, change) {
    const input = document.getElementById(type + '-guest') || document.getElementById('room-quantity');
    let value = parseInt(input.value, 10);
    value = isNaN(value) ? 0 : value;
    value += change;
    if (value < 0) value = 0;
    input.value = value;
}

function validateInputs(dateIn, dateOut, adultGuests, childGuests, rooms) {
    const today = new Date();

    if (!dateIn || !dateOut) {
        alert('Vui lòng chọn ngày check-in và check-out.');
        return false;
    }

    const checkInDate = new Date(dateIn);
    const checkOutDate = new Date(dateOut);

    if (checkInDate <= today || checkOutDate <= today) {
        alert('Vui lòng chọn ngày trong tương lai.');
        return false;
    }

    if (checkInDate >= checkOutDate) {
        alert('Ngày check-in phải trước ngày check-out.');
        return false;
    }

    if (parseInt(adultGuests) <= 0 && parseInt(childGuests) <= 0) {
        alert('Vui lòng chọn số lượng khách.');
        return false;
    }

    if (parseInt(rooms) <= 0) {
        alert('Phòng phải từ 1 trở lên.');
        return false;
    }

    return true;
}

function checkRoomAvailability() {
    const dateIn = $('#date-in').val();
    const dateOut = $('#date-out').val();
    const adultGuests = $('#adult-guest').val();
    const childGuests = $('#child-guest').val();
    const rooms = $('#room-quantity').val();

    if (!validateInputs(dateIn, dateOut, adultGuests, childGuests, rooms)) {
        return;
    }

    $.ajax({
        url: 'http://localhost:8080/api/rooms/check-room',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({
            dateIn: dateIn,
            dateOut: dateOut,
            adultGuests: adultGuests,
            childGuests: childGuests,
            rooms: rooms
        }),
        success: function (response) {
            if (response.isAvailable) {
                alert('Phòng còn trống! Đặt ngay kẻo lỡ!');
            } else {
                alert('Phòng không còn trống.');
            }
        },
        error: function (xhr, status, error) {
            console.error('Lỗi khi kiểm tra trạng thái phòng:', error);
            alert('Đã xảy ra lỗi khi kiểm tra trạng thái phòng.');
        }
    });
}


$(document).on('submit', 'form', function (e) {
    e.preventDefault();
    checkRoomAvailability();
});

$(document).ready(function () {
    const currentUrl = window.location.href;
    const slug = currentUrl.substring(currentUrl.lastIndexOf('/') + 1);

    getRoomBySlug(slug);
});