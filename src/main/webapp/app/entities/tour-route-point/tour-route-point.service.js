(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('TourRoutePoint', TourRoutePoint);

    TourRoutePoint.$inject = ['$resource'];

    function TourRoutePoint ($resource) {
        var resourceUrl =  'api/tour-route-points/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
