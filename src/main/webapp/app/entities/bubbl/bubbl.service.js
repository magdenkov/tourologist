(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Bubbl', Bubbl);

    Bubbl.$inject = ['$resource', 'DateUtils','BubblEntity','API'];

    function Bubbl ($resource, DateUtils,BubblEntity,API) {
        var resourceUrl =  'api/bubbls/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
            'update': { method:'PUT' }
        });
    }
})();
