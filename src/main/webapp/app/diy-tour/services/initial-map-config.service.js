(function () {
    'use strict';

    angular
        .module('tourologistApp.diy-tour')
        .service('InitialMapConfigForDiyTour', InitialMapConfigForDiyTour);

    InitialMapConfigForDiyTour.$inject = [];

    function InitialMapConfigForDiyTour() {
        var service = this;

        service.call = function (lat, lng, zoom) {
            var config = {
                center: {latitude: lat || 54.00366, longitude: lng || -2.547855},
                zoom: zoom || 10,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                scrollwheel: false,
                events: {}
            };
            return config;
        }

    }
})();
