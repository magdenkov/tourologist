(function () {
    'use strict';
    angular
        .module('tourologistApp.dataservices')
        .factory('Tour', Tour);

    Tour.$inject = ['$resource', 'DateUtils', 'Principal'];

    function Tour($resource, DateUtils, Principal) {
        var resourceUrl = 'api/tours/:id';

        return $resource(resourceUrl, {}, {
            'query': {
                method: 'GET',
                isArray: true,
                url: 'api/my/tours/:id'
            },
            'queryAdmin': {
                method: 'GET',
                isArray: true,
                url: 'api/tours/:id'
            },
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
            updateFixedTour: {
                method: 'PUT',
                url: resourceUrl + "/fixed",
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
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
