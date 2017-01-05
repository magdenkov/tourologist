(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .service('DeleteTourService', DeleteTourService);

    DeleteTourService.$inject = ['$state'];

    function DeleteTourService($state) {
        var service = this;

        service.call = function (tour) {
            $state.go('tour.delete', {id: tour.id});
        }
    }
})();
