(function () {
    'use strict';

    angular
        .module('tourologistApp.dataservices')
        .factory('Bubbl', Bubbl);

    Bubbl.$inject = ['$resource', 'DateUtils'];

    function Bubbl($resource, DateUtils) {
        var resourceUrl = 'api/bubbls/:id';

          return $resource(resourceUrl, {}, {
            query: {
                method: 'GET',
                url:  'api/my/bubbls/:id',
                isArray: true
            },
            queryAdmin: {
                method: 'GET',
                url:  'api/bubbls/:id',
                isArray: true
            },
            get: {
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
            update: {
                method: 'PUT'
            },
            in_radius: {
                method: "GET",
                url: 'api/bubbls/in_radius',
                isArray: true
            },
            createPayload: {
                method: "POST",
                transFormRequest: angular.identity,
                url: resourceUrl + "/payloads?type=AUDIO",
                headers: {'Content-Type': undefined}
            }

        });
    }
})();
