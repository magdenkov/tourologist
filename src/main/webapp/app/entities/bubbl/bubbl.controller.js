(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('BubblController', BubblController);

    BubblController.$inject = ['$scope', '$state', 'Bubbl', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants', '$rootScope', 'angularGridInstance'];

    function BubblController($scope, $state, Bubbl, ParseLinks, AlertService, pagingParams, paginationConstants, $rootScope, angularGridInstance) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        $scope.map = {center: {latitude: 44, longitude: -108}, zoom: 10};
        $scope.options = {scrollwheel: false};
        $scope.circles = [
            {
                id: 1,
                center: {
                    latitude: 44,
                    longitude: -108
                },
                radius: 500000,
                stroke: {
                    color: '#08B21F',
                    weight: 2,
                    opacity: 1
                },
                fill: {
                    color: '#08B21F',
                    opacity: 0.5
                },
                geodesic: true, // optional: defaults to false
                draggable: true, // optional: defaults to false
                clickable: true, // optional: defaults to true
                editable: true, // optional: defaults to false
                visible: true, // optional: defaults to true
                control: {}
            }
        ];

        loadAll();

        $scope.swapvenues = function () {
            $rootScope.multiple = !$rootScope.multiple;
                loadAll();
                angularGridInstance.vm.bubbls.refresh();
        };

        function loadAll() {
            Bubbl.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
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
                vm.bubbls = data;
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
