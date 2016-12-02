(function () {
    'use strict';

    angular
        .module('tourologistApp')
        .controller('PayloadDialogController', PayloadDialogController);

    PayloadDialogController.$inject = ['$timeout', '$scope', '$rootScope', '$uibModalInstance', 'entity', 'Payload', 'Bubbl','ParseLinks'];

    function PayloadDialogController($timeout, $scope, $rootScope, $uibModalInstance, entity, Payload, Bubbl,ParseLinks) {
        var vm = this;

        vm.payload = entity;
        vm.clear = clear;

        vm.save = save;
        vm.bubbls =[];
        vm.loadPage = loadPage;
        vm.page = 0;
        vm.links = {
            last: 0
        };
        vm.size = 250;
        vm.reset = reset;
        loadAll();

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
            loadAll();
        }

        function loadPage(page) {
            vm.page = page;
            loadAll();
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
            Bubbl.createPayload(vm.payload.bubblId,formData ,vm.payload.name, onSaveSuccess, onSaveError);

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
