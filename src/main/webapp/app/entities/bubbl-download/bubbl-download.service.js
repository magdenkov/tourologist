(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('BubblDownload', BubblDownload);

    BubblDownload.$inject = ['$resource', 'DateUtils'];

    function BubblDownload ($resource, DateUtils) {
        var resourceUrl =  'api/bubbl-downloads/:id';

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
