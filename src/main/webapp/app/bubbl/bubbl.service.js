(function () {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Bubbl', Bubbl);

    Bubbl.$inject = ['$resource', 'DateUtils', 'BubblEntity', 'API', 'Principal'];

    function Bubbl($resource, DateUtils, BubblEntity, API, Principal) {
        var resourceUrl = 'api/bubbls/:id';

        var isAdmin = false;
        Principal.hasAuthority('ROLE_ADMIN').then(function(result) {
            return isAdmin = result;
        });

        return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true, url:  isAdmin ? 'api/bubbls/:id' : 'api/my/bubbls/:id'},
            'get': {
                method: 'GET',
                url: isAdmin ? 'api/bubbls/:id' : 'api/bubbls/:id',
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


            createPayload: {
                method: "POST",
                transFormRequest: angular.identity,
                url: resourceUrl + "/payloads?type=AUDIO",
                headers: {'Content-Type': undefined}
            }

        });
    }
})();
