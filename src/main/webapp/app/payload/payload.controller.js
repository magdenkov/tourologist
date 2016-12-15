(function() {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('PayloadController', PayloadController);

    PayloadController.$inject = ['$scope', '$state', 'Payload', 'ParseLinks', 'AlertService', 'pagingParams', 'paginationConstants','$sce','Principal'];

    function PayloadController ($scope, $state, Payload, ParseLinks, AlertService, pagingParams, paginationConstants,$sce,Principal) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        $scope.searchPayloads = '';
        $scope.queryBy = 'name';
        $scope.trustSrc = function(src) {
            return $sce.trustAsResourceUrl(src);
        };


        vm.account = null;
        vm.isAuthenticated = null;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function (account) {
                vm.isAuthenticated = Principal.isAuthenticated;
                changeUrl()
            });


        }

        function changeUrl() {
            console.log(vm.account);

            if (vm.account.authorities.includes('ROLE_ADMIN')) {
                loadAdmin();
            } else {
                loadAll();
            }


        }


        function loadAdmin () {
            Payload.queryAdmin({
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
                vm.payloads = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }


        function loadAll () {
            Payload.query({
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
                vm.payloads = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
