(function () {
    'use strict';

    angular
        .module('tourologistApp.services')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
