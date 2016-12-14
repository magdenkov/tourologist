(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Payload', Payload);

    Payload.$inject = ['$resource', 'DateUtils', 'Principal'];

    function Payload ($resource, DateUtils, Principal) {
        var resourceUrl =  'api/payloads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',
                isArray: true,
                url: 'api/my/payloads/:id'
            },
            'queryAdmin': { method: 'GET',
                isArray: true,
                url: 'api/payloads/:id'
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
            'update': { method:'PUT' }
        });
    }
})();
