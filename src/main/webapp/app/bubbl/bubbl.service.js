(function () {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Bubbl', Bubbl);

    Bubbl.$inject = ['$resource', 'DateUtils'];

    function Bubbl($resource, DateUtils) {
        var resourceUrl = 'api/bubbls/:id';

          return $resource(resourceUrl, {}, {
            'query': {method: 'GET', isArray: true,
                url:  'api/my/bubbls/:id'
            },
            'queryAdmin': {method: 'GET', isArray: true,
                url:  'api/bubbls/:id'
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
