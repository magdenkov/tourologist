(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .service('TourConstructorService', TourConstructorService);

    TourConstructorService.$inject = ['$state'];

    function TourConstructorService($state) {
        var service = this;

        service.call = function (tour) {
            var params = {};
            if (tour && tour.id) {
                params = {id: tour.id};
            }
            $state.go('tour.constructor', params);
        }
    }
})();
