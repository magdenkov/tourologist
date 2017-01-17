(function () {
    'use strict';

    angular
        .module('tourologistApp.directives')
        .service('InitialMapConfig', InitialMapConfig);

    InitialMapConfig.$inject = [];

    function InitialMapConfig() {
        var service = this;
        service.call = function (lat, lng, zoom) {
            var config = {
                center: {latitude: lat || 51.509948, longitude: lng || -0.135137},
                zoom: zoom || 15,
                mapTypeId: google.maps.MapTypeId.ROADMAP,
                scrollwheel: false,
                events: {}
            };
            return config;
        }
    }
})();
