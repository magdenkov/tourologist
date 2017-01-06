(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .controller('TourConstructorController', TourConstructorController);

    TourConstructorController.$inject = ['$timeout', '$scope', 'Bubbl', '$uibModalInstance', 'entity', 'Tour', 'User', 'Interest', 'ParseLinks', 'Principal', 'uiGmapIsReady'];

    function TourConstructorController($timeout, $scope, Bubbl, $uibModalInstance, entity, Tour, User, Interest, ParseLinks, Principal, uiGmapIsReady) {
        var vm = this;

        vm.mapControl = null;
        vm.showBubblesInRadius = {
            show: true,
            value: 10000
        };
        vm.tour = entity;

        uiGmapIsReady.promise().then(function (maps) {
            vm.mapControl = maps[0].map;
        })

        vm.sortableOptions = {
            update: function (e, ui) {
                alert(e);
            },
            stop: function (e, ui) {
                alert(e);
            }
        };

        vm.close = function () {
            $uibModalInstance.dismiss('cancel');
        };

        // tabs

        vm.tabs = [
            {
                title: 'Main Info',
                url: 'app/tour/services/tour-constructor/tabs/tour-main.tab.html'
            }, {
                title: 'Tour Bubbls',
                url: 'app/tour/services/tour-constructor/tabs/tour-bubbls.tab.html'
            }, {
                title: 'Tour Details',
                url: 'app/tour/services/tour-constructor/tabs/tour-details.tab.html'
            },];

        vm.currentTab = vm.tabs[0];

        vm.onClickTab = function (tab) {
            vm.currentTab = tab;
        }

        vm.isActiveTab = function (tab) {
            return tab == vm.currentTab;
        }

        // tabs

        vm.save = save;
        vm.users = User.query();
        vm.interests = Interest.query();

        vm.add = add;

        vm.removeBubbl = function (bubbl) {
            vm.tour.bubbls.splice(_.findIndex(vm.tour.bubbls, bubbl), 1);
        }

        vm.bubbls = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.size = 250;
        vm.reset = reset;
        vm.account = null;
        vm.isAuthenticated = null;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                changeUrl()
            });
        }

        function changeUrl() {
            if (vm.account.authorities.includes('ROLE_ADMIN')) {
                loadAdmin();
            } else {
                loadAll();
            }
        }

        function loadAdmin() {
            Bubbl.queryAdmin({
                page: vm.page,
                size: vm.size,
            }, onSuccess, onError);

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.bubbls.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function loadAll() {
            Bubbl.query({
                page: vm.page,
                size: vm.size,
            }, onSuccess, onError);


            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                for (var i = 0; i < data.length; i++) {
                    vm.bubbls.push(data[i]);
                }
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function reset() {
            vm.page = 0;
            vm.recipes = [];
            changeUrl();
        }

        function loadPage(page) {
            vm.page = page;

        }

        $scope.user = {
            interest: [vm.interests[0]]
        };

        if (vm.tour.bubbls === undefined) {
            vm.tour.bubbls = [];
        }

        if (vm.tour.bubbls.length === 0) {
            vm.tour.bubbls.push({
                bubblId: '',
                orderNumber: ''
            });
        }

        function add() {
            vm.tour.bubbls.push({
                bubblId: '',
                orderNumber: ''
            });
        }

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function save() {
            vm.isSaving = true;
            if (vm.tour.id !== null) {
                Tour.updateFixedTour(vm.tour, onSaveSuccess, onSaveError);
            } else {
                Tour.createFixedTour(vm.tour, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:tourUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
    }
})();
