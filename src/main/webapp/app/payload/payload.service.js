(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Payload', Payload);

    Payload.$inject = ['$resource', 'DateUtils', 'Principal'];

    function Payload ($resource, DateUtils, Principal) {
        var resourceUrl =  'api/payloads/:id';

        var isAdmin = false;
        Principal.hasAuthority('ROLE_ADMIN').then(function(result) {
            return isAdmin = result;
        });

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET',
                isArray: true,
                url: isAdmin ? 'api/payloads/:id' : 'api/my/payloads/:id'
            },
            'get': {
                method: 'GET',
                url: isAdmin ? 'api/payloads/:id' : 'api/payloads/:id',
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
