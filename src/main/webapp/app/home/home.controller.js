(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal',  '$state'];

    function HomeController ($scope, Principal, $state) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = $state.go('login');
        vm.register = register;
        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
