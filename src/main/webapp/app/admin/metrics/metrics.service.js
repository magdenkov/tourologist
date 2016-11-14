(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .factory('TlMetricsService', TlMetricsService);

    TlMetricsService.$inject = ['$rootScope', '$http'];

    function TlMetricsService ($rootScope, $http) {
        var service = {
            getMetrics: getMetrics,
            threadDump: threadDump
        };

        return service;

        function getMetrics () {
            return $http.get('management/jhipster/metrics').then(function (response) {
                return response.data;
            });
        }

        function threadDump () {
            return $http.get('management/dump').then(function (response) {
                return response.data;
            });
        }
    }
})();
