(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('AppController', AppController);

    AppController.$inject = ['$state', '$scope', '$window', 'Principal'];

    function AppController($state, $scope, $window, Principal) {
        var vm = this;

        vm.isAuthenticated = Principal.isAuthenticated;

        var isIE = !!navigator.userAgent.match(/MSIE/i);
        if (isIE) {
            angular.element($window.document.body).addClass('ie');
        }
        if (isSmartDevice($window)) {
            angular.element($window.document.body).addClass('smart')
        };

        // config
        $scope.app = {
            name: 'Tourologist',
            version: '1.1.0',
            // for chart colors
            color: {
                primary: '#7266ba',
                info: '#23b7e5',
                success: '#27c24c',
                warning: '#fad733',
                danger: '#f05050',
                light: '#e8eff0',
                dark: '#3a3f51',
                black: '#1c2b36'
            },
            settings: {
                themeID: 1,
                navbarHeaderColor: 'bg-danger',
                navbarCollapseColor: 'bg-black',
                asideColor: 'bg-danger',
                headerFixed: false,
                asideFixed: true,
                asideFolded: false,
                asideDock: false,
                container: true
            }
        }

        function isSmartDevice($window) {
            // Adapted from http://www.detectmobilebrowsers.com
            var ua = $window['navigator']['userAgent'] || $window['navigator']['vendor'] || $window['opera'];
            // Checks for iOs, Android, Blackberry, Opera Mini, and Windows mobile devices
            return (/iPhone|iPod|iPad|Silk|Android|BlackBerry|Opera Mini|IEMobile/).test(ua);
        }

        // redirect to bubbl page if isAuthenticated
        return Principal.promise().then(function() {
            if (Principal.isAuthenticated()) {
                $state.go('bubbl');
            }
        });
    }

})();
