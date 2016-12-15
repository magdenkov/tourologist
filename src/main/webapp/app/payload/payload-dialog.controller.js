(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('PayloadDialogController', PayloadDialogController);

    PayloadDialogController.$inject = ['$timeout', '$scope', '$rootScope', '$uibModalInstance', 'entity', 'Payload', 'Bubbl', 'ParseLinks', 'Principal'];

    function PayloadDialogController($timeout, $scope, $rootScope, $uibModalInstance, entity, Payload, Bubbl, ParseLinks, Principal) {
        var vm = this;

        vm.payload = entity;
        vm.clear = clear;

        vm.save = save;
        vm.bubbls = [];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.size = 250;
        vm.reset = reset;
        $(document).ready(function () {
            $(".searchbubbl").select2();
        });

        $scope.searchBubbls = '';
        $scope.queryBy = 'name';
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

        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        $rootScope.$on('setBubbl', function (bubbl, id) {
            vm.payload.bubblId = id;

        });
        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {

            var formData = new FormData();
            formData.append('file', vm.payload.fileForUpload);
            vm.isSaving = true;
            if (vm.payload.id !== null) {
                Payload.update(approve, onSaveSuccess, onSaveError);
            } else {
                Bubbl.createPayload(vm.payload.bubblId, formData, onSaveSuccess, onSaveError);

            }

            console.log(formData);
        }

        function onSaveSuccess(result) {
            $scope.$emit('tourologistApp:payloadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }

    }
})();
angular
    .module('tourologistApp').filter('propsFilter', function () {
    return function (items, props) {
        var out = [];

        if (angular.isArray(items)) {
            items.forEach(function (item) {
                var itemMatches = false;

                var keys = Object.keys(props);
                for (var i = 0; i < keys.length; i++) {
                    var prop = keys[i];
                    var text = props[prop].toLowerCase();
                    if (item[prop].toString().toLowerCase().indexOf(text) !== -1) {
                        itemMatches = true;
                        break;
                    }
                }

                if (itemMatches) {
                    out.push(item);
                }
            });
        } else {
            // Let the output be the input untouched
            out = items;
        }

        return out;
    }
});
