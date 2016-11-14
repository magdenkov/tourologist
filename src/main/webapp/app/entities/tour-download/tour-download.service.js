(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('TourDownload', TourDownload);

    TourDownload.$inject = ['$resource', 'DateUtils'];

    function TourDownload ($resource, DateUtils) {
        var resourceUrl =  'api/tour-downloads/:id';

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
