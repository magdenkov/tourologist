(function () {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Tour', Tour);

    Tour.$inject = ['$resource', 'DateUtils', 'Principal'];

    function Tour($resource, DateUtils, Principal) {
        var resourceUrl = 'api/tours/:id';
        var isAdmin = false;
        Principal.hasAuthority('ROLE_ADMIN').then(function(result) {
            return isAdmin = result;
        });
        // console.log("HAS Authority " + isAdmin);

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET',
                      isArray: true,
                      url: isAdmin ? 'api/tours/:id' : 'api/my/tours/:id',
            },
            'get': {
                method: 'GET',
                url: isAdmin ? 'api/tours/:id' : 'api/my/tours/:id',
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
