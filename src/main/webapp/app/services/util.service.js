(function () {
    'use strict';

    angular
        .module('tourologistApp.services')
        .service('UtilService', ['$q', UtilService]);

    function UtilService($q) {
        var service = this;

        //////////////////////////////////////////////////

        service.isEmail = function (value) {
            return value.match(/^[^@]+@[^@\.]+\..+$/i);
        };

        service.isURL = function (value) {
            return value.match(/^(https?:\/\/(?:www\.|(?!www))[^\s\.]+\.[^\s]{2,}|www\.[^\s]+\.[^\s]{2,})$/i);
        };

        service.isNumber = function (value) {
            return value.toString().match(/^-?((0*[1-9][0-9]*)|0)(\.[0-9]+)?|(\.[0-9]+)$/);
        };

        service.isPresent = function (value) {
            return value !== null && value !== undefined && value !== "";
        };

        service.resolvedPromise = function (value) {
            var deferred = $q.defer();
            deferred.resolve(value);
            return deferred.promise;
        };

        service.rejectedPromise = function (value) {
            var deferred = $q.defer();
            deferred.reject(value);
            return deferred.promise;
        };
    }

})();

