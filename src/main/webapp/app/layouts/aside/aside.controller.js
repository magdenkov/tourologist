(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('AsideController', AsideController);

    AsideController.$inject = ['Principal'];

    function AsideController(Principal) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;
    }
})();
