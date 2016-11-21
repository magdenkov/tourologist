(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('TourRating', TourRating);

    TourRating.$inject = ['$resource', 'DateUtils'];

    function TourRating ($resource, DateUtils) {
        var resourceUrl =  'api/tour-ratings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.time = DateUtils.convertDateTimeFromServer(data.time);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
