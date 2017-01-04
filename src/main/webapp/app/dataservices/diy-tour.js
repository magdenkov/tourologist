(function () {
    'use strict';
    angular.module('tourologistApp.dataservices').factory('DIYTour', DIYTour);

    DIYTour.$inject = ['$resource', 'DateUtils', 'Principal'];

    function DIYTour($resource, DateUtils, Principal) {
        var resourceUrl = 'api/tours/diy';

        return $resource(resourceUrl, {}, {
            'get': {
                method: 'GET',
                isArray: true
            }
        });
    }
})();
