(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('TourBubblRoutePoint', TourBubblRoutePoint);

    TourBubblRoutePoint.$inject = ['$resource'];

    function TourBubblRoutePoint ($resource) {
        var resourceUrl =  'api/tour-bubbl-route-points/:id';

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
