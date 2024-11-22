function togglePassword(toggleButtonId, passwordFieldId) {
    $(`#${toggleButtonId}`).click(function () {
        const passwordField = document.getElementById(passwordFieldId);
        const passwordIcon = this.querySelector('i');
        if (passwordField.type === 'password') {
            passwordField.type = 'text';
            passwordIcon.classList.remove('ti-eye');
            passwordIcon.classList.add('ti-eye-off');
        } else {
            passwordField.type = 'password';
            passwordIcon.classList.remove('ti-eye-off');
            passwordIcon.classList.add('ti-eye');
        }
    });
}

$('#registrationForm').submit(function (event) {
    event.preventDefault();

    const name = $('#name').val();
    const email = $('#email').val();
    const password = $('#password').val();
    const confirmPassword = $('#confirmPassword').val();

    if (password !== confirmPassword) {
        alert('Mật khẩu không khớp!');
        return;
    }

    if (password.length < 6 || confirmPassword.length < 6) {
        alert('Mật khẩu phải từ 6 ký tự!')
        return;
    }

    const requestData = {
        fullname: name,
        email: email,
        password: password
    };

    $('#spinner').removeClass('d-none');

    $.ajax({
        url: '/api/signup',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(requestData),
    })
        .done(data => {
            if (data.success) {
                $('#spinner').addClass('d-none');
                const myModal = new bootstrap.Modal(document.getElementById('successModal'));
                myModal.show();

                $('#redirectHome').click(function () {
                    window.location.href = '/';
                });
            } else {
                alert('Đăng ký thất bại: ' + data.message);
            }
        })
        .fail(error => {
            console.error('Có lỗi xảy ra:', error);
            alert(error.responseJSON.message);
        })
        .always(() => {
            $('#spinner').addClass('d-none');
        });
});

$('#loginForm').on('submit', function (event) {
    event.preventDefault();

    const email = $('#email').val();
    const password = $('#password').val();

    $.ajax({
        url: '/api/login',
        method: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({email, password}),
        success: function (response) {
            console.log(response)
            if (response.message === 'Đăng nhập thành công') {
                sessionStorage.setItem('user', JSON.stringify(
                    {
                        fullname: response.user.fullname,
                        email: response.user.email
                    }
                ));
                window.location.href = '/';
            } else {
                $('#error-message').removeClass('d-none');
            }
        },
        error: function (error) {
            console.error('Có lỗi xảy ra:', error.responseJSON.message);
            $('#error-message').text(error.responseJSON.message).removeClass('d-none');
        }
    });
});