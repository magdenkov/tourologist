(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('TourBubbl', TourBubbl);

    TourBubbl.$inject = ['$resource'];

    function TourBubbl ($resource) {
        var resourceUrl =  'api/tour-bubbls/:id';

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
