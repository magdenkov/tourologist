(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('TourImage', TourImage);

    TourImage.$inject = ['$resource', 'DateUtils'];

    function TourImage ($resource, DateUtils) {
        var resourceUrl =  'api/tour-images/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.uploaded = DateUtils.convertDateTimeFromServer(data.uploaded);
                        data.lastModified = DateUtils.convertDateTimeFromServer(data.lastModified);
                        data.deleted = DateUtils.convertDateTimeFromServer(data.deleted);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
