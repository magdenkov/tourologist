(function () {
    'use strict';

    angular
        .module('tourologistApp.tour')
        .controller('TourController', TourController);

    TourController.$inject = ['$scope', '$state', 'Tour', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', 'Principal',
        'TourConstructorService', 'DeleteTourService', '$filter', '$timeout'];

    function TourController($scope, $state, Tour, ParseLinks, AlertService, pagingParams, paginationConstants, Principal, tourConstructor, deleteTour, $filter, $timeout) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $scope.tourType = 'FIXED'; //leave blank if you want to search all tours
        vm.tours = [];
        $scope.searchTours = '';
        $scope.queryBy = 'name';

        vm.account = null;
        vm.isAuthenticated = null;

        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        // DIY button filter
        $scope.getDIY = function () {
            $scope.tourType = 'DIY';
            changeUrl();
        };

        // fixed buytton filter
        $scope.getFixed = function () {
            $scope.tourType = 'FIXED';
            changeUrl();
        };

        $scope.showdeletebutton = function () {
            var trues = $filter("filter")(vm.tours, {
                selected: true
            });
            return trues.length;
        };
        $scope.delete = function () {
            $scope.selectedTours = [];


            // check if checkbox are ticked and push the values to selectedtours
            angular.forEach(vm.tours, function (tour) {
                if (tour.selected) $scope.selectedTours.push(tour.id);

            });


            Tour.delete({id: $scope.selectedTours});
            $timeout(function () {
                changeUrl();
            }, 1000);
            console.log($scope.selectedTours)
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
                type: $scope.tourType
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
                type: $scope.tourType

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
