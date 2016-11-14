(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('BubblImage', BubblImage);

    BubblImage.$inject = ['$resource', 'DateUtils'];

    function BubblImage ($resource, DateUtils) {
        var resourceUrl =  'api/bubbl-images/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.uploaded = DateUtils.convertDateTimeFromServer(data.uploaded);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
