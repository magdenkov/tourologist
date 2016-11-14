(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('BubblAdminReview', BubblAdminReview);

    BubblAdminReview.$inject = ['$resource', 'DateUtils'];

    function BubblAdminReview ($resource, DateUtils) {
        var resourceUrl =  'api/bubbl-admin-reviews/:id';

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
