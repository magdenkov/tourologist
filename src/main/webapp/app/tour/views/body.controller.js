(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .controller('TourController', TourController);

    TourController.$inject = ['$scope', '$state', 'Tour', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Principal',
        'TourConstructorService', 'DeleteTourService'];

    function TourController($scope, $state, Tour, ParseLinks, AlertService, pagingParams, paginationConstants, Principal, tourConstructor, deleteTour) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $scope.tourType = 'FIXED';
        vm.tours = [];
        $scope.searchTours = '';
        $scope.queryBy = 'name';

        vm.account = null;
        vm.isAuthenticated = null;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        $scope.getDIY = function(){
            $scope.tourType = 'DIY';
            changeUrl();
        };
        $scope.getFixed = function(){
            $scope.tourType = 'FIXED';
            changeUrl();
        };
        $scope.clearFilter = function(){
            $scope.tourType = '';
            changeUrl();
        };
        getAccount();
        vm.onDeleteTourClick = function (tour) {
            deleteTour.call(tour);
        }

        vm.onEditTourClick = function (tour) {
            tourConstructor.call(tour);
        }

        vm.onCreateTourClick = function () {
            tourConstructor.call({
                id: null,
                description: null,
                name: null
            });
        }

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
                changeUrl();
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
            Tour.queryAdmin({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                type:$scope.tourType
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.tours = data;
                vm.page = pagingParams.page;

            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadAll() {
            Tour.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort(),
                type:$scope.tourType

            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }

            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.tours = data;
                vm.page = pagingParams.page;
            }

            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
