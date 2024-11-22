/*  ---------------------------------------------------
    Template Name: Sona
    Description: Sona Hotel Html Template
    Author: Colorlib
    Author URI: https://colorlib.com
    Version: 1.0
    Created: Colorlib
---------------------------------------------------------  */

'use strict';

(function ($) {

    /*------------------
        Preloader
    --------------------*/
    $(window).on('load', function () {
        $(".loader").fadeOut();
        $("#preloder").delay(200).fadeOut("slow");
    });

    /*------------------
        Background Set
    --------------------*/
    $('.set-bg').each(function () {
        var bg = $(this).data('setbg');
        $(this).css('background-image', 'url(' + bg + ')');
    });

    //Offcanvas Menu
    $(".canvas-open").on('click', function () {
        $(".offcanvas-menu-wrapper").addClass("show-offcanvas-menu-wrapper");
        $(".offcanvas-menu-overlay").addClass("active");
    });

    $(".canvas-close, .offcanvas-menu-overlay").on('click', function () {
        $(".offcanvas-menu-wrapper").removeClass("show-offcanvas-menu-wrapper");
        $(".offcanvas-menu-overlay").removeClass("active");
    });

    // Search model
    $('.search-switch').on('click', function () {
        $('.search-model').fadeIn(400);
    });

    $('.search-close-switch').on('click', function () {
        $('.search-model').fadeOut(400, function () {
            $('#search-input').val('');
        });
    });

    /*------------------
		Navigation
	--------------------*/
    $(".mobile-menu").slicknav({
        prependTo: '#mobile-menu-wrap', allowParentLinks: true
    });

    /*------------------
        Hero Slider
    --------------------*/
    $(".hero-slider").owlCarousel({
        loop: true,
        margin: 0,
        items: 1,
        dots: true,
        animateOut: 'fadeOut',
        animateIn: 'fadeIn',
        smartSpeed: 1200,
        autoHeight: false,
        autoplay: true,
        mouseDrag: false
    });

    /*------------------------
		Testimonial Slider
    ----------------------- */
    $(".testimonial-slider").owlCarousel({
        items: 1,
        dots: false,
        autoplay: true,
        loop: true,
        smartSpeed: 1200,
        nav: true,
        navText: ["<i class='arrow_left'></i>", "<i class='arrow_right'></i>"]
    });

    /*------------------
        Magnific Popup
    --------------------*/
    $('.video-popup').magnificPopup({
        type: 'iframe'
    });

    /*------------------
		Date Picker
	--------------------*/
    $(".date-input").datepicker({
        minDate: 0, dateFormat: 'dd MM, yy'
    });

    /*------------------
		Nice Select
	--------------------*/
    $("select").niceSelect();

})(jQuery);

$(document).ready(function () {
    const user = JSON.parse(sessionStorage.getItem('user'));
    console.log(user)
    if (user && user.email) {
        $('.user-not-login').addClass('d-none');
        $('.user-login').removeClass('d-none');
        $('.user-login .user-name').text(user.fullname);
    } else {
        $('.user-not-login').removeClass('d-none');
        $('.user-login').addClass('d-none');
    }

    const handleLogout = () => {
        $.ajax({
            url: '/api/logout', type: 'POST', contentType: 'application/json', success: (response) => {
                console.log(response.message);
                sessionStorage.removeItem('user');
                window.location.href = '/login';
            }, error: (jqXHR, textStatus, errorThrown) => {
                console.error('Logout failed: ', textStatus, errorThrown);
                alert('Logout failed, please try again.');
            }
        });
    }

    $('.btn-logout').click(function () {
        handleLogout();
    })
});

$(document).ready(function() {
    $.ajax({
        url: '/api/rooms/list-categories',
        method: 'GET',
        success: function(response) {
            var categoryList = $('.category-list');
            categoryList.empty();
            console.log(response)
            response.forEach(function(category) {
                categoryList.append('<li><a href="/room/' + category.slug + '">' + category.name + '</a></li>');
            });
        },
        error: function() {
            console.error('Error fetching categories.');
        }
    });
});