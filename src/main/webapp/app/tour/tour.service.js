(function () {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Tour', Tour);

    Tour.$inject = ['$resource', 'DateUtils'];

    function Tour($resource, DateUtils) {
        var resourceUrl = 'api/tours/:id';

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdDate = DateUtils.convertDateTimeFromServer(data.createdDate);
                        data.lastModified = DateUtils.convertDateTimeFromServer(data.lastModified);
                        data.deleted = DateUtils.convertDateTimeFromServer(data.deleted);
                    }
                    return data;
                }
            },
            'update': {method: 'PUT'},

            createFixedTour: {
                method: 'POST',
                url: resourceUrl + "/fixed",
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }

            }

        });
    }
})();
