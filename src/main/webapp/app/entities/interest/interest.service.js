(function() {
    'use strict';
    angular
        .module('tourologistApp')
        .factory('Interest', Interest);

    Interest.$inject = ['$resource'];

    function Interest ($resource) {
        var resourceUrl =  'api/interests/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
