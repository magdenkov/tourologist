(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .service('TourConstructorService', TourConstructorService);

    TourConstructorService.$inject = ['$state'];

    function TourConstructorService($state) {
        var service = this;

        service.call = function (tour) {
            $state.go('tour.constructor', {id: tour.id});
        }
    }
})();
