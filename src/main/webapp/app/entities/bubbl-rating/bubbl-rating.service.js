(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('BubblRating', BubblRating);

    BubblRating.$inject = ['$resource', 'DateUtils'];

    function BubblRating ($resource, DateUtils) {
        var resourceUrl =  'api/bubbl-ratings/:id';

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
