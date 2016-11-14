(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('TourAdminReview', TourAdminReview);

    TourAdminReview.$inject = ['$resource', 'DateUtils'];

    function TourAdminReview ($resource, DateUtils) {
        var resourceUrl =  'api/tour-admin-reviews/:id';

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
